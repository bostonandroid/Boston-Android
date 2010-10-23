package org.bostonandroid.bostonandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;

class TwitterAuthSaver implements OnClickListener {
  private Activity activity;
  private SharedPreferences sharedPreferences;
  private String username;
  private String password;
  
  TwitterAuthSaver(Activity activity, SharedPreferences sharedPreferences, String username, String password) {
    this.activity = activity;
    this.sharedPreferences = sharedPreferences;
    this.username = username;
    this.password = password;
  }
  
  public void onClick(View v) {
    savePreferences();
    this.activity.finish();
  }
  
  private void savePreferences() {
    Editor sPEditor = this.sharedPreferences.edit();
    sPEditor.putString("username", this.username);
    sPEditor.putString("password", this.password);
    sPEditor.commit();
  }
}