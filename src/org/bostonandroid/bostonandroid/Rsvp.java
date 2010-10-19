package org.bostonandroid.bostonandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Rsvp extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button rsvpButton = (Button)findViewById(R.id.rsvp_button);
        rsvpButton.setOnClickListener(this);
       
    }
    
    public void onClick(View v) {
      Toast.makeText(this, "You're so not RSVPed yet.", Toast.LENGTH_LONG).show();
    }
}