package org.bostonandroid.bostonandroid;

import com.google.api.client.util.Key;

public class CalendarEvent {
  @Key
  public String title;
  @Key("gd:when")
  private When when;

  public String startTime() {
    return this.when.startTime;
  }

  public String endTime() {
    return this.when.endTime;
  }

  public static class When {
    @Key("@startTime")
    private String startTime;
    @Key("@endTime")
    private String endTime;
  }
}
