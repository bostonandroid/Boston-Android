package org.bostonandroid.bostonandroid;

import java.util.List;

import com.google.api.client.util.Key;

public class CalendarFeed {
  @Key("entry") public List<CalendarEvent> events;
}
