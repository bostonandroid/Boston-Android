package org.bostonandroid.bostonandroid;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

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
  private OAuthService service;
  private Token requestToken;
  private static final String TAG = "BostonAndroid";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    this.service = new ServiceBuilder().provider(TwitterApi.class).apiKey(TwitterKey.KEY).apiSecret(TwitterKey.SECRET).callback("boston-android:///").build();

    //new AlarmScheduler(this).execute(getString(R.string.calendar_account));

    Button rsvpButton = (Button)findViewById(R.id.rsvp_button);
    rsvpButton.setOnClickListener(this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    setIntent(intent);
  }

  @Override
  public void onResume() {
    super.onResume();
    Uri data = getIntent().getData();
    if (data != null) {
      Token accessToken = this.service.getAccessToken(this.requestToken, new Verifier(data.getQueryParameter("oauth_verifier")));
      Editor editor = twitterPreferences().edit();
      editor.putString("access_token", accessToken.getToken());
      editor.putString("token_secret", accessToken.getSecret());
      editor.commit();
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

  private void authorizeTwitter() {
    new AsyncTask<OAuthService, Void, Token>() {
      @Override
      protected Token doInBackground(OAuthService... params) {
        return params[0].getRequestToken();
      }

      @Override
      protected void onPostExecute(Token result) {
        if (result != null)
          doSomethingWithToken(result);
        else
          toast("Catastrophic failure");
      }
    }.execute(this.service);
  }
  
  private void doSomethingWithToken(Token result) {
    this.requestToken = result;
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/oauth/authorize?oauth_token="+result.getToken())));
  }
  
  private String rsvpMessage() {
    return ((EditText) findViewById(R.id.rsvp_text)).getText().toString();
  }
  
  private void rsvpViaTwitter(String message) {
    OAuthRequest request = new OAuthRequest(Verb.POST, "http://api.twitter.com/1/statuses/update.xml");
    request.addBodyParameter("status", message);
    this.service.signRequest(new Token(accessToken(), tokenSecret()), request);
    Response response = request.send();
    if (response.getCode() == 200)
      toast("Great success!");
    else {
      Log.d(TAG, response.getBody());
      toast("Less catastrophic failure.");
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
