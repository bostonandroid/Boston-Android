package org.bostonandroid.bostonandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class AlarmService extends WakefulIntentService {
  private NotificationManager notificationManager;

  public AlarmService() {
    super("AlarmService");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @Override
  protected void doWakefulWork(Intent intent) {
  }
  
  private Notification buildNotification(int icon, String contentText, long alertId) {
    Notification notification = new Notification(icon, contentText, System.currentTimeMillis());
    notification.setLatestEventInfo(this, getPackageManager().getApplicationLabel(getApplicationInfo()), contentText,
        PendingIntent.getActivity(this, 0, new Intent(this, Rsvp.class), 0));
    notification.flags |= Notification.FLAG_AUTO_CANCEL;
    return notification;
  }

  void showNotification(int icon, String contentText, long alertId) {
    notificationManager.cancel(1);
    notificationManager.notify(1, buildNotification(icon, contentText, alertId));
  }
}
