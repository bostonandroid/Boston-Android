package org.bostonandroid.bostonandroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(formatter().parse(s));
      return calendar;
    } catch (ParseException e) {
      return new GregorianCalendar(1970,1,1);
    }
  }

  private static SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  }
}