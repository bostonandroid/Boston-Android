package org.bostonandroid.bostonandroid;

import android.app.Activity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.os.AsyncTask;
import android.util.Log;

class TweetTask extends AsyncTask<String, Void, Void> {
  private Activity activity;
  private Activity activity() { return this.activity; }
  private AccessToken accessToken;
  private AccessToken accessToken() { return this.accessToken; }

  TweetTask(Activity a, AccessToken at) {
    this.activity = a;
    this.accessToken = at;
  }

  protected Void doInBackground(String... tweet) {
    try {
      twitter().updateStatus(tweet[0]);
    } catch (TwitterException e) {
      Log.i("TweetTask", e.toString());
    }
    return null;
  }

  private Twitter twitter() {
    return new TwitterFactory().getOAuthAuthorizedInstance(
        TwitterKey.KEY,
        TwitterKey.SECRET,
        accessToken());
  }
}
