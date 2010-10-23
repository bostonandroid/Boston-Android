package org.bostonandroid.bostonandroid;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TwitterAuthPref extends Activity implements OnClickListener {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.twitter_auth);

    Button saveButton = (Button)findViewById(R.id.twitter_auth_button);
    saveButton.setOnClickListener(this);
  }
  
  public void onClick(View v) {
    savePreferences();
    rsvpViaTwitter(rsvpMessage());
    finish();
  }
  
  private void savePreferences() {
    Editor sPEditor = getSharedPreferences("twitter", MODE_PRIVATE).edit();
    sPEditor.putString("username", twitterUsername());
    sPEditor.putString("password", twitterPassword());
    sPEditor.commit();
  }
  
  private String rsvpMessage() {
    Intent i = getIntent();
    Bundle b = i.getExtras();
    return b.getString("message");
  }
  
  private void rsvpViaTwitter(String message) {
    try {
      twitter().updateStatus(message);
      toast("See you there!");
    } catch(TwitterException e) {
      toast("Failed to RSVP: " + e.getMessage());
    }
  }
  
  private Twitter twitter() {
    return new TwitterFactory().getInstance(twitterUsername(), twitterPassword());
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }
  
  private String twitterUsername() {
    return stringField(R.id.twitter_username);
  }
  
  private String twitterPassword() {
    return stringField(R.id.twitter_password);
  }
  
  private String stringField(int fieldId) {
    EditText usernameField = (EditText)findViewById(fieldId);
    return usernameField.getText().toString();
  }
}