package org.bostonandroid.bostonandroid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class Rsvp extends Activity implements OnClickListener {

  protected static final String TAG = "RSVP";
  protected RequestToken rToken;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    new AlarmScheduler(this).execute(getString(R.string.calendar_account));

    sendBroadcast(new Intent(this, AlarmReceiver.class));
    Button rsvpButton = (Button)findViewById(R.id.rsvp_button);
    rsvpButton.setOnClickListener(this);
  }
  
  public void onClick(View v) {
	   
    Intent i;
	try {
		Twitter t = new TwitterFactory().getInstance();
	
		Log.i(TAG, "onClick: "+ oauthToken());
		if (oauthToken() == null)
		{
			Log.i(TAG, "generating request token etc");
			
			 t.setOAuthConsumer ("yQfelP2qk5v5UYZhLm5HQ", "q5vvqwUYWfJVVkcv7VAnSRenLRznkswSuSrl4N6Qt8");
			 String callbackURL = "boston-android:///";
			 rToken = t.getOAuthRequestToken(callbackURL);
			 
			
			 persistRequest ();
			 i = new Intent(Intent.ACTION_VIEW, Uri.parse(rToken.getAuthenticationURL()));
			 startActivity(i);
		}
		else
		{
			Log.i (TAG, "doing authenticaiton");
			Twitter tt = new TwitterFactory ().getInstance();
			tt.setOAuthConsumer ("yQfelP2qk5v5UYZhLm5HQ", "q5vvqwUYWfJVVkcv7VAnSRenLRznkswSuSrl4N6Qt8");
			Log.i(TAG, "OATH VERIFIER: " +oauthVerifier());
		
			AccessToken at = tt.getOAuthAccessToken(rToken, oauthVerifier());
			
			tt.updateStatus("DEV: " +Calendar.getInstance().getTime());
			Log.i(TAG,"Updating Status Complete");
			
			String token = at.getToken();
			String secret = at.getTokenSecret();

			
			Twitter t3 = new TwitterFactory ().getOAuthAuthorizedInstance("yQfelP2qk5v5UYZhLm5HQ", "q5vvqwUYWfJVVkcv7VAnSRenLRznkswSuSrl4N6Qt8", new AccessToken (token, secret));
			t3.updateStatus("2DEV2: " +Calendar.getInstance().getTime());
			Log.i(TAG,"DONE");
		}
	

		
	} catch (TwitterException e) {
		Toast.makeText (this.getApplicationContext(), "Error: " +e, Toast.LENGTH_LONG);
		Log.e(TAG, e.toString());
	}
  }
  
  @Override
  public void onPause ()
  {
	  super.onPause();
	  
  }
  
  protected void loadProvider () {
	  try {
	  FileInputStream fin = this.openFileInput("provider.dat");
	  ObjectInputStream ois = new ObjectInputStream (fin);
	  this.rToken = (RequestToken) ois.readObject();
	  ois.close();
	  }catch (Exception e){
		  Log.e(TAG, e.toString());
	  }  
  }
  
  
  
  protected void persistRequest ()
  {	  
	  Log.i(TAG, "PRESISTING ");
	  try {
	  FileOutputStream fout = this.openFileOutput("provider.dat", MODE_PRIVATE);
	  ObjectOutputStream oos = new ObjectOutputStream (fout);
	  oos.writeObject (this.rToken);
	  oos.close();
	  }
	  catch (Exception e) {
		  Log.e(TAG, e.toString());
	  }
  }
  
  
  
  @Override
  public void onResume() {
    super.onResume();
    try {
	    Uri uri = getIntent().getData();
	    if (uri != null) {
	      String oauthToken = uri.getQueryParameter("oauth_token");
	      String oauthVerifier = uri.getQueryParameter("oauth_verifier");
	      oauthToken (oauthToken);
	      oauthVerifier (oauthVerifier);
	      Log.i(TAG, "oauthToken: " + oauthToken);
	      Log.i(TAG, "oauthVerifier" +oauthVerifier);
	    }
	    
	    loadProvider();
    }
    catch (Exception e)
    {
    	Log.e (TAG, "Error: " +e);
    }
  }

  
  private String rsvpMessage() {
    EditText text = (EditText)findViewById(R.id.rsvp_text);
    return text.getText().toString();
  }
  
  private void startTwitterAuthPrefActivity() {
    Intent i = new Intent(this, TwitterAuthPref.class);
    i.putExtra("message", rsvpMessage());
    startActivity(i);
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }
  
  private boolean isTwitterInfoEntered() {
    return (twitterUsername() != null) && (twitterPassword() != null);
  }
  
  private String twitterUsername() {
    return twitterPreferences().getString("username", null);
  }
  
  private String twitterPassword() {
    return twitterPreferences().getString("password", null);
  }

  private String  oauthVerifier ()
  {
	  return twitterPreferences().getString("oauthVerifier", null);
  }
  
  private void oauthVerifier(String value) {
	  Editor editor = twitterPreferences().edit(); 
	  editor.putString("oauthVerifier", value);
	  editor.commit();
  }
  
  private String oauthToken() {
	   return twitterPreferences().getString("oauthToken", null);
  }
  
  private void oauthToken(String value) {
	  Editor editor = twitterPreferences().edit(); 
	  editor.putString("oauthToken", value);
	  editor.commit();
  }
  
  SharedPreferences twitterPreferences() {
    return getSharedPreferences("twitter", MODE_PRIVATE);
  }

  private TextView when() {
    return (TextView)findViewById(R.id.when);
  }

  void setWhen(String when) {
    when().setText(when);
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
