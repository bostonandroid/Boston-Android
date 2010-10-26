package org.bostonandroid.bostonandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.util.Key;

public class CalendarFeed {
  @Key("entry") private List<CalendarEvent> events = new ArrayList<CalendarEvent>();

  public CalendarEvent first() {
    if (eventIterator().hasNext())
      return eventIterator().next();
    else
      return new CalendarEvent();
  }

  private Iterator<CalendarEvent> eventIterator() {
    return new ArrayList<CalendarEvent>(this.events).iterator();
  }
}
