package org.bostonandroid.bostonandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class Rsvp extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button rsvpButton = (Button) findViewById(R.id.rsvp_button);
    rsvpButton.setOnClickListener(new RsvpHandler(this));
  }
  
  SharedPreferences twitterPreferences() {
    return getSharedPreferences("twitter", MODE_WORLD_READABLE);
  }
}