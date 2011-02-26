package org.bostonandroid.bostonandroid;

import java.util.Calendar;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;

public class Rsvp extends Activity {

  protected static final String TAG = "RSVP";
  protected RequestToken rToken;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    new AlarmScheduler(this).execute(getString(R.string.calendar_account));

    sendBroadcast(new Intent(this, AlarmReceiver.class));
    Button rsvpButton = (Button)findViewById(R.id.rsvp_button);
    if (hasAccessToken())
      rsvpButton.setOnClickListener(new RsvpListener());
    else
      rsvpButton.setOnClickListener(new AccessTokenListener());
  }

  void setWhen(String when) {
    when().setText(when);
  }

  class RsvpListener implements OnClickListener {
    public void onClick(View v) {
      Intent i = new Intent(Rsvp.this, TweetActivity.class);
      startActivity(i);
    }
  }

  class AccessTokenListener implements OnClickListener {
    public void onClick(View v) {
      RequestToken token;
      try {
        token = twitter().getOAuthRequestToken("boston-android:///");
      } catch (TwitterException e) {
        Log.i(TAG, e.toString());
        return;
      }
      saveRequestToken(token);
      Intent i = new Intent(Intent.ACTION_VIEW,
          Uri.parse(token.getAuthenticationURL()));
      startActivity(i);
    }
  }

  /*
        Log.i (TAG, "doing authenticaiton");
        Twitter tt = new TwitterFactory ().getInstance();
        Log.i(TAG, "OATH VERIFIER: " +oauthVerifier());

        AccessToken at = tt.getOAuthAccessToken(rToken, oauthVerifier());

        tt.updateStatus("DEV: " +Calendar.getInstance().getTime());
        Log.i(TAG,"Updating Status Complete");

        String token = at.getToken();
        String secret = at.getTokenSecret();


        Twitter t3 = new TwitterFactory ().getOAuthAuthorizedInstance(new AccessToken(token, secret));
        t3.updateStatus("2DEV2: " +Calendar.getInstance().getTime());
        Log.i(TAG,"DONE");
      }



  }
  */

  private boolean hasAccessToken() {
    return preferences().getString("accessToken", null) != null;
  }

  private void saveRequestToken(RequestToken token) {
    Editor prefEdit = preferences().edit();
    prefEdit.putString("requestToken", token.getToken());
    prefEdit.putString("requestSecret", token.getTokenSecret());
    prefEdit.commit();
  }

  private Twitter twitter() {
    Twitter t = new TwitterFactory().getInstance();
    t.setOAuthConsumer(TwitterKey.KEY, TwitterKey.SECRET);
    return t;
  }

  private TextView when() {
    return (TextView)findViewById(R.id.when);
  }

  private SharedPreferences preferences() {
    return PreferenceManager.getDefaultSharedPreferences(this);
  }


  //  @Override
  //  public boolean onCreateOptionsMenu(Menu menu)
  //  {
  //  	super.onCreateOptionsMenu(menu);
  //  	MenuInflater inflater = getMenuInflater ();
  //  	inflater.inflate (R.menu.main_menu, menu);	
  //  	return true;
  //  }
  //  
  //  @Override
  //  public boolean onOptionsItemSelected (MenuItem item) 
  //  {
  //  	switch (item.getItemId())
  //  	{
  //	    	case R.id.menu_preferences:
  //	    		Log.i(TAG, "Launching Preferences");	   
  //	    		startActivity (new Intent (this, Preference.class));    
  //	    		break;
  //  	    			
  //  		default:
  //  			Toast.makeText(this, "ERROR - MENU OPTION NOT SELECTED", Toast.LENGTH_SHORT).show();
  //  			return true;
  //  	}
  //  	return true;
  //  }
}
