package org.bostonandroid.bostonandroid;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.preference.PreferenceManager;

public class TweetActivity extends Activity {
  @Override
  public void onResume() {
    super.onResume();

    Uri uri = getIntent().getData();

    if (uri == null) {
      startActivity(new Intent(TweetActivity.this, Rsvp.class));
    } else {
      AccessToken token = getToAccessToken(uri);
      saveAccessToken(token);

      // tweet here
    }
  }

  private AccessToken getToAccessToken(Uri uri) {
    try {
      return twitter().getOAuthAccessToken(
          requestToken(),
          uri.getQueryParameter("oauth_verifier"));
    } catch (TwitterException e) {
      Log.i("TweetActivity", e.toString());
      return null;
    }
  }

  private void saveAccessToken(AccessToken token) {
    Editor prefEdit = preferences().edit();
    prefEdit.putString("accessToken", token.getToken());
    prefEdit.putString("accessSecret", token.getTokenSecret());
    prefEdit.commit();
  }

  private RequestToken requestToken() {
    return new RequestToken(
        preferences().getString("requestToken", null),
        preferences().getString("requestSecret", null));
  }

  private Twitter twitter() {
    Twitter t = new TwitterFactory().getInstance();
    t.setOAuthConsumer(TwitterKey.KEY, TwitterKey.SECRET);
    return t;
  }

  private SharedPreferences preferences() {
    return PreferenceManager.getDefaultSharedPreferences(this);
  }
}
