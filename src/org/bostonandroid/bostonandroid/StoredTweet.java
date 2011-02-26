package org.bostonandroid.bostonandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;

class StoredTweet {
  private Activity activity;
  private Activity activity() { return this.activity; }

  StoredTweet(Activity a) {
    this.activity = a;
  }

  void store(String tweet) {
    Editor prefEdit = tweetPreferences().edit();
    prefEdit.putString("tweet", tweet);
    prefEdit.commit();
  }

  private SharedPreferences tweetPreferences() {
    return activity().getSharedPreferences("tweet", Context.MODE_PRIVATE);
  }
}
