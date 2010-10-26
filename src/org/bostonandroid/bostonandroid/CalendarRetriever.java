package org.bostonandroid.bostonandroid;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.xml.atom.GData;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AtomParser;

public class CalendarRetriever {
  private HttpTransport transport;

  private static final XmlNamespaceDictionary DICTIONARY = new XmlNamespaceDictionary();

  static {
    DICTIONARY.addNamespace("", "http://www.w3.org/2005/Atom");
    DICTIONARY.addNamespace("gd", "http://schemas.google.com/g/2005");
  }

  public CalendarRetriever() {
    this.transport = GoogleTransport.create();
    GoogleHeaders headers = (GoogleHeaders)transport.defaultHeaders;
    headers.gdataVersion = "2";
    AtomParser parser = new AtomParser();
    parser.namespaceDictionary = DICTIONARY;
    this.transport.addParser(parser);
  }

  public Calendar retrieve(String account) {
    HttpRequest request = transport.buildGetRequest();
    request.url = calendarUrl(account);
    try {
      CalendarFeed feed = request.execute().parseAs(CalendarFeed.class);
      return feed.first().startTime();
    } catch (IOException e) {
      return new GregorianCalendar(1970,1,1);
    }
  }

  // FIXME: this does not belong here
  private static CalendarUrl calendarUrl(String account) {
    CalendarUrl url = CalendarUrl.publicFeedFor(account);
    url.maxResults = 1;
    url.singleEvents = "true";
    url.futureEvents = "true";
    url.orderBy = "starttime";
    url.sortOrder = "a";
    url.fields = GData.getFieldsFor(CalendarFeed.class);
    return url;
  }
}
