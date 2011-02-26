package org.bostonandroid.bostonandroid;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

class AccessTokenTracker {
  private Activity activity;
  private Activity activity() { return this.activity; }

  AccessTokenTracker(Activity a) {
    this.activity = a;
  }

  boolean hasAccessToken() {
    return preferences().getString("accessToken", null) != null;
  }

  private SharedPreferences preferences() {
    return PreferenceManager.getDefaultSharedPreferences(activity());
  }
}
