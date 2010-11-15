package org.bostonandroid.bostonandroid;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Rsvp extends Activity implements OnClickListener {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    new AlarmScheduler(this).execute(getString(R.string.calendar_account));

    sendBroadcast(new Intent(this, AlarmReceiver.class));
    Button rsvpButton = (Button)findViewById(R.id.rsvp_button);
    rsvpButton.setOnClickListener(this);
  }
  
  public void onClick(View v) {
    Intent i = new Intent(Intent.ACTION_VIEW, );
    startActivity(i);
  }
  
  @Override
  public void onResume() {
    super.onResume();
    Uri uri = getIntent().getData();
    if (uri != null) {
      String oauthToken = uri.getQueryParameter("oauth_token");
      Log.e("rsvp", "oauthToken: " + oauthToken);
    }
  }

  private String rsvpMessage() {
    EditText text = (EditText)findViewById(R.id.rsvp_text);
    return text.getText().toString();
  }
  
  private boolean isConnectedToTwitter() {
    if (isTwitterInfoEntered()) {
      try {
        twitter().test();
        return true;
      } catch (TwitterException e) {
        toast("Unable to connect to Twitter: " + e.getMessage());
        return false;
      }
    } else {
      return false;
    }
  }
  
  private void rsvpViaTwitter(String message) {
    try {
      twitter().updateStatus(message);
      toast("See you there!");
    } catch(TwitterException e) {
      toast("Failed to RSVP: " + e.getMessage());
    }
  }
  
  private void startTwitterAuthPrefActivity() {
    Intent i = new Intent(this, TwitterAuthPref.class);
    i.putExtra("message", rsvpMessage());
    startActivity(i);
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }
  
  private boolean isTwitterInfoEntered() {
    return (twitterUsername() != null) && (twitterPassword() != null);
  }
  
  private Twitter twitter() {
    Twitter t = new TwitterFactory().getOAuthAuthorizedInstance("hETTbD1lnhQkq2MUiSBMA", "DYVzSOTv9NfICR9SAUF0wFdY2PlaM5bkqz1jRN2Xdzg");
    try {
      RequestToken rt = t.getOAuthRequestToken("bostonandroid://twitter");
      t.getOAuthAccessToken(rt);
      return t;
    } catch (TwitterException e) {
      Log.e("Rsvp", e.toString());
      return null;
    }
  }
  
  private String twitterUsername() {
    return twitterPreferences().getString("username", null);
  }
  
  private String twitterPassword() {
    return twitterPreferences().getString("password", null);
  }

  SharedPreferences twitterPreferences() {
    return getSharedPreferences("twitter", MODE_PRIVATE);
  }

  private TextView when() {
    return (TextView)findViewById(R.id.when);
  }

  void setWhen(String when) {
    when().setText(when);
  }
}
