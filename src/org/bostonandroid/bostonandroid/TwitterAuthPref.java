package org.bostonandroid.bostonandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class TwitterAuthPref extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.twitter_auth);
    
    TwitterAuthSaver authSaver = new TwitterAuthSaver(
        this,
        getSharedPreferences("twitter", MODE_PRIVATE),
        twitterUsername(),
        twitterPassword());
    Button saveButton = (Button)findViewById(R.id.twitter_auth_button);
    saveButton.setOnClickListener(authSaver);
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