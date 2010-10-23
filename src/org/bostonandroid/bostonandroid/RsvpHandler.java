package org.bostonandroid.bostonandroid;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

class RsvpHandler implements OnClickListener {
  private Rsvp rsvp;

  public RsvpHandler(Rsvp r) {
    this.rsvp = r;
  }
  
  public void onClick(View v) {
    if (isConnectedToTwitter()) {
      EditText text = (EditText) this.rsvp.findViewById(R.id.rsvp_text);
      rsvpViaTwitter(text.getText().toString());
    } else {
      startTwitterAuthPrefActivity();
    }
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
    Intent i = new Intent(this.rsvp, TwitterAuthPref.class);
    this.rsvp.startActivity(i);
  }

  private void toast(String s) {
    Toast.makeText(this.rsvp, s, Toast.LENGTH_LONG).show();
  }
  
  private boolean isTwitterInfoEntered() {
    return (twitterUsername() != null) && (twitterPassword() != null);
  }
  
  private Twitter twitter() {
    return new TwitterFactory().getInstance(twitterUsername(), twitterPassword());
  }
  
  private String twitterUsername() {
    return this.rsvp.twitterPreferences().getString("username", null);
  }
  
  private String twitterPassword() {
    return this.rsvp.twitterPreferences().getString("password", null);
  }
}