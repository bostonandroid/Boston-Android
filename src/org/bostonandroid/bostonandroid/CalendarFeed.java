package org.bostonandroid.bostonandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.util.Key;

public class CalendarFeed {
  @Key("entry")
  private List<CalendarEvent> events = new ArrayList<CalendarEvent>();

  public CalendarEvent first() throws EmptyFeedException {
    if (eventIterator().hasNext())
      return eventIterator().next();
    else {
      throw new EmptyFeedException();
    }
  }

  private Iterator<CalendarEvent> eventIterator() {
    return this.events.iterator();
  }
}
