package org.bostonandroid.bostonandroid;

import android.app.Activity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.os.AsyncTask;
import android.util.Log;

class TweetTask extends AsyncTask<String, Void, Boolean> {
  private Activity activity;
  private Activity activity() { return this.activity; }
  private AccessToken accessToken;
  private AccessToken accessToken() { return this.accessToken; }

  TweetTask(Activity a, AccessToken at) {
    this.activity = a;
    this.accessToken = at;
  }

  @Override
  protected Boolean doInBackground(String... tweet) {
    try {
      twitter().updateStatus(tweet[0]);
      return Boolean.TRUE;
    } catch (TwitterException e) {
      Log.i("TweetTask", e.toString());
      return Boolean.FALSE;
    }
  }

  @Override
  protected void onPostExecute(Boolean wasSuccess) {
    if (wasSuccess.booleanValue())
      this.activity.showDialog(1);
    else
      this.activity.showDialog(2);
  }


  private Twitter twitter() {
    return new TwitterFactory().getOAuthAuthorizedInstance(
        TwitterKey.KEY,
        TwitterKey.SECRET,
        accessToken());
  }
}
