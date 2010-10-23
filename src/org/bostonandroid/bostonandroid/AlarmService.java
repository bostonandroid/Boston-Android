package org.bostonandroid.bostonandroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.util.ServiceException;

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
    CalendarService myService = new CalendarService("Boston-Android");
    try {
      URL feedUrl = new URL("http://www.google.com/calendar/feeds/admins%40bostonandroid.org/public/basic");
      CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
      for (int i = 0; i < resultFeed.getEntries().size(); i++) {
        CalendarEntry entry = resultFeed.getEntries().get(i);
        Log.d("AlarmService", entry.getTitle().getPlainText());
      }
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    } catch (ServiceException e) {
    }
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
