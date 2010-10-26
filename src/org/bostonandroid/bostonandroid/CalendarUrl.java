package org.bostonandroid.bostonandroid;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class CalendarUrl extends GenericUrl {
  private static final String FEED_URI = "http://www.google.com/calendar/feeds";

  @Key("max-results")
  public Integer maxResults;

  @Key("singleevents")
  public String singleEvents;

  @Key("futureevents")
  public String futureEvents;

  @Key("orderby")
  public String orderBy;

  @Key("sortorder")
  public String sortOrder;

  @Key
  public String fields;

  private CalendarUrl(String url) {
    super(url);
  }

  public static CalendarUrl publicFeedFor(String account) {
    CalendarUrl url = new CalendarUrl(FEED_URI);
    url.pathParts.add(account);
    url.pathParts.add("public");
    url.pathParts.add("full");
    return url;
  }
}
