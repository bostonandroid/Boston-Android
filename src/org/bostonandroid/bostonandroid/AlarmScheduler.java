package org.bostonandroid.bostonandroid;

import java.text.DateFormat;
import java.util.Calendar;

import android.os.AsyncTask;

class AlarmScheduler extends AsyncTask<String, Void, Calendar> {
  private RsvpActivity activity;
  private Exception exception;

  AlarmScheduler(RsvpActivity activity) {
    this.activity = activity;
  }

  @Override
  protected Calendar doInBackground(String... params) {
    try {
      return new CalendarRetriever(this.activity).retrieve(params[0]);
    } catch (RetrievalException e) {
      this.exception = e;
      return null; // thanks Google
    }
  }

  @Override
  protected void onPostExecute(Calendar calendar) {
    if (calendar != null) {
      setWhen(formatter().format(calendar.getTime()));
    } else
      setWhen(this.exception.getMessage());
  }

  // FIXME: this does not belong here
  private DateFormat formatter() {
    return android.text.format.DateFormat.getLongDateFormat(activity);
  }

  private void setWhen(String when) {
    activity.setWhen(when);
  }
}
