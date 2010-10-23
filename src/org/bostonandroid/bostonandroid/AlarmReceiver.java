package org.bostonandroid.bostonandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d("AlarmReceiver", "alarm received");
    AlarmService.sendWakefulWork(context, new Intent(context, AlarmService.class));
  }
}
