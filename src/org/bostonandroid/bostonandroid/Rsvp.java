package org.bostonandroid.bostonandroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

    //alarmManager().set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1, makePendingIntent(makeIntent()));
    sendBroadcast(new Intent(this, AlarmReceiver.class));
  }

  SharedPreferences twitterPreferences() {
    return getSharedPreferences("twitter", MODE_PRIVATE);
  }

  private AlarmManager alarmManager() {
    return (AlarmManager)getSystemService(Context.ALARM_SERVICE);
  }

  private PendingIntent makePendingIntent(Intent i) {
    return PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
  }

  private Intent makeIntent() {
    return new Intent(this, AlarmReceiver.class);
  }

}
