package org.bostonandroid.bostonandroid;

import java.util.Calendar;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

class AlarmScheduler extends AsyncTask<String, Void, Calendar> {
  private Context context;
  private Exception exception;

  AlarmScheduler(Context context) {
    this.context = context;
  }

  @Override
  protected Calendar doInBackground(String... params) {
    try {
      return new CalendarRetriever(this.context).retrieve(params[0]);
    } catch (RetrievalException e) {
      this.exception = e;
      return null; // thanks Google
    }
  }

  @Override
  protected void onPostExecute(Calendar calendar) {
    if (calendar != null)
      Log.d("AlarmScheduler", calendar.toString());
    else
      Log.d("AlarmScheduler", this.exception.getMessage());
  }
}
