package org.bostonandroid.bostonandroid;

import twitter4j.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Rsvp extends Activity implements OnClickListener {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button rsvpButton = (Button) findViewById(R.id.rsvp_button);
    rsvpButton.setOnClickListener(this);

    TwitterFactory tf = new twitter4j.TwitterFactory();
    Twitter twitter = tf.getOAuthAuthorizedInstance("hETTbD1lnhQkq2MUiSBMA",
        "DYVzSOTv9NfICR9SAUF0wFdY2PlaM5bkqz1jRN2Xdzg");
    try {
      if (twitter.test())
        toast("Connected to the Twitter.");
      else
        toast("Not connected to the Twitter.");
    } catch (TwitterException e) {
      toast("Definitely not connected to the Twitter.");
    }
  }

  public void onClick(View v) {
    toast("You're so not RSVPed yet.");
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }
}