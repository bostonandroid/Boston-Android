package org.bostonandroid.bostonandroid;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class CalendarUrl extends GenericUrl {
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

  public CalendarUrl(String url) {
    super(url);
  }
}
