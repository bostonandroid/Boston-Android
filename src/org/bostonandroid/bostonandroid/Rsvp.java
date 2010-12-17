package org.bostonandroid.bostonandroid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Rsvp extends Activity implements OnClickListener {
  private OAuthConsumer consumer;
  private static final String TAG = "BostonAndroid";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    this.consumer = new CommonsHttpOAuthConsumer(TwitterKey.KEY, TwitterKey.SECRET);

    //new AlarmScheduler(this).execute(getString(R.string.calendar_account));

    Button rsvpButton = (Button)findViewById(R.id.rsvp_button);
    rsvpButton.setOnClickListener(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (isTwitterAuthorized())
      setToken();
  }

  @Override
  public void onResume() {
    super.onResume();
    Uri data = getIntent().getData();
    if (data != null) {
      Editor editor = twitterPreferences().edit();
      editor.putString("access_token", data.getQueryParameter("oauth_token"));
      editor.putString("token_secret", data.getQueryParameter("oauth_verifier"));
      editor.commit();
      Log.d(TAG, accessToken());
      Log.d(TAG, tokenSecret());
      setToken();
      rsvpViaTwitter(rsvpMessage());
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.rsvp_button:
      if (isTwitterAuthorized())
       rsvpViaTwitter(rsvpMessage());
      else
        authorizeTwitter();
      break;
    }
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(R.id.logout).setEnabled(isTwitterAuthorized());
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.logout:
      Editor editor = twitterPreferences().edit();
      editor.clear();
      editor.commit();
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  private void setToken() {
    this.consumer.setTokenWithSecret(accessToken(), tokenSecret());
  }

  private void authorizeTwitter() {
    new AsyncTask<Void, Void, Uri>() {
      @Override
      protected Uri doInBackground(Void... params) {
        OAuthProvider provider = new CommonsHttpOAuthProvider("http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token", "http://twitter.com/oauth/authorize");
        try {
          return Uri.parse(provider.retrieveRequestToken(Rsvp.this.consumer, "boston-android:///"));
        } catch (OAuthException e) {
          return null;
        }
      }

      @Override
      protected void onPostExecute(Uri result) {
        if (result != null)
          startActivity(new Intent(Intent.ACTION_VIEW, result));
      }
    }.execute();
  }
  
  private String rsvpMessage() {
    return ((EditText) findViewById(R.id.rsvp_text)).getText().toString();
  }
  
  private void rsvpViaTwitter(String message) {
    HttpPost request = new HttpPost("http://api.twitter.com/1/statuses/update.xml");
    request.setEntity(requestParams(message));
    request.getParams().setBooleanParameter("http.protocol.expect-continue", false);
    // FIXME: async
    if (signRequest(request))
      makeRequest(request);
    else
      toast("Authentication failed");
  }

  private UrlEncodedFormEntity requestParams(String message) {
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("status", message));
    try {
      return new UrlEncodedFormEntity(params);
    } catch (UnsupportedEncodingException e) {
      // FIXME: user entered a message that we coldn't encode
      return null;
    }
  }

  private void makeRequest(HttpPost request) {
    HttpClient client = new DefaultHttpClient();
    try {
      processResponse(client.execute(request));
    } catch (IOException e) {
      request.abort();
      toast("POST failed");
    }
  }

  private void processResponse(HttpResponse response) {
    if (response.getStatusLine().getStatusCode() == 200)
      toast("See you there!");
    else
      toast("Something else failed");
  }

  private boolean signRequest(HttpPost request) {
    try {
      this.consumer.sign(request);
      return true;
    } catch (OAuthException e) {
      request.abort();
      return false;
    }
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }
  
  private boolean isTwitterAuthorized() {
    return (accessToken() != null) && (tokenSecret() != null);
  }
  
  private String accessToken() {
    return twitterPreferences().getString("access_token", null);
  }
  
  private String tokenSecret() {
    return twitterPreferences().getString("token_secret", null);
  }

  private SharedPreferences twitterPreferences() {
    return getSharedPreferences("twitter", MODE_PRIVATE);
  }

  private TextView when() {
    return (TextView) findViewById(R.id.when);
  }

  void setWhen(String when) {
    when().setText(when);
  }
}
