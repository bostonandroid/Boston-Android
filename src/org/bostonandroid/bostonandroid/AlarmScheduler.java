package org.bostonandroid.bostonandroid;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.xml.atom.GData;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AtomParser;

class AlarmScheduler extends AsyncTask<String, Void, Calendar> {
  private HttpTransport transport;

  AlarmScheduler() {
    this.transport = GoogleTransport.create();
    GoogleHeaders headers = (GoogleHeaders)transport.defaultHeaders;
    headers.gdataVersion = "2";
    AtomParser parser = new AtomParser();
    parser.namespaceDictionary = namespaceDictionary();
    this.transport.addParser(parser);
  }

  private XmlNamespaceDictionary namespaceDictionary() {
    XmlNamespaceDictionary namespaceDictionary = new XmlNamespaceDictionary();
    namespaceDictionary.addNamespace("", "http://www.w3.org/2005/Atom");
    namespaceDictionary.addNamespace("gd", "http://schemas.google.com/g/2005");
    return namespaceDictionary;
  }

  @Override
  protected Calendar doInBackground(String... params) {
    HttpRequest request = transport.buildGetRequest();
    CalendarUrl url = new CalendarUrl(params[0]);
    url.maxResults = 1;
    url.singleEvents = "true";
    url.futureEvents = "true";
    url.orderBy = "starttime";
    url.sortOrder = "a";
    url.fields = GData.getFieldsFor(CalendarFeed.class);
    request.url = url;
    Calendar calendar = new GregorianCalendar(1970,1,1);
    try {
      CalendarFeed feed = request.execute().parseAs(CalendarFeed.class);
      for (CalendarEvent calendarEvent : feed.events)
        calendar.setTime(formatter().parse(calendarEvent.startTime()));
      return calendar;
    } catch (IOException e) {
      return calendar;
    } catch (ParseException e) {
      return calendar;
    }
  }

  @Override
  protected void onPostExecute(Calendar calendar) {
    Log.d("AlarmScheduler", calendar.toString());
  }

  private SimpleDateFormat formatter() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  }
}
