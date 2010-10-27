package org.bostonandroid.bostonandroid;

import java.util.Calendar;

import com.google.api.client.util.Key;

public class CalendarEvent {
  @Key
  private String title = "";

  @Key("gd:when")
  private When when = new When();

  public String title() {
    return this.title;
  }

  public Calendar startTime() {
    return this.when.startTime();
  }

  public Calendar endTime() {
    return this.when.endTime();
  }
}
