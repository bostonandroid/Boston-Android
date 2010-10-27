package org.bostonandroid.bostonandroid;

import java.util.Calendar;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public class When {
  @Key("@startTime")
  private String startTime = "";

  @Key("@endTime")
  private String endTime = "";

  public Calendar startTime() {
    return stringToCalendar(this.startTime);
  }

  public Calendar endTime() {
    return stringToCalendar(this.endTime);
  }

  private static Calendar stringToCalendar(String s) {
    Calendar calendar = Calendar.getInstance();
    DateTime t = DateTime.parseRfc3339(s);
    calendar.setTimeInMillis(t.value);
    return calendar;
  }
}