package org.bostonandroid.bostonandroid;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    Bundle metaData;
    try {
      metaData = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
    } catch (NameNotFoundException e) {
      // FIXME: this is really lame, but there's not much we can do if the Package Manager can't find our package
      throw new RuntimeException(e);
    }
    this.consumer = new CommonsHttpOAuthConsumer(metaData.getString("consumer_key"), metaData.getString("consumer_secret"));
    Log.d(TAG, "OAuth consumer key set to "+this.consumer.getConsumerKey());
    Log.d(TAG, "OAuth consumer secret set to "+this.consumer.getConsumerSecret());

    //new AlarmScheduler(this).execute(getString(R.string.calendar_account));

    Button rsvpButton = (Button)findViewById(R.id.rsvp_button);
    rsvpButton.setOnClickListener(this);
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
      Log.d(TAG, "Received accessToken: "+accessToken());
      Log.d(TAG, "Received tokenSecret: "+tokenSecret());
      // FIXME: async?
      this.consumer.setTokenWithSecret(accessToken(), tokenSecret());
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.rsvp_button:
      Log.d(TAG, "RSVP button pressed");
      if (isTwitterAuthorized())
        Log.d(TAG, "Already authorized");
      else
        authorizeTwitter();
      break;
    }
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
    try {
      toast("See you there!");
    } catch (Exception e) {
      toast("Failed to RSVP: " + e.getMessage());
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
