package org.bostonandroid.bostonandroid;

import java.util.Calendar;

import android.os.AsyncTask;
import android.util.Log;

class AlarmScheduler extends AsyncTask<String, Void, Calendar> {
  @Override
  protected Calendar doInBackground(String... params) {
    return new CalendarRetriever().retrieve(params[0]);
  }

  @Override
  protected void onPostExecute(Calendar calendar) {
    Log.d("AlarmScheduler", calendar.toString());
  }
}
