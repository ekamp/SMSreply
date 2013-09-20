package com.example.smsreply;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class MainActivity extends Activity implements Serializable{

	private static final long serialVersionUID = -6856744969237814170L;

	public boolean active;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String reply;
        
        final replier reciever,newreciever;
        
        Bundle extras = getIntent().getExtras(); 
        
        if(extras.containsKey("active"))
        {
        	active = extras.getBoolean("active"); 
        }
        else
        {
        	active = false;
        }
        
        if(extras.containsKey("currentReply"))
        {
        	System.out.println("we changed the reply");
        	reply = (String)extras.get("currentReply"); 
        }
        else
        {
        	reply = "Please text back later";
        }
        
        //Get the reciever if it exists
        System.out.println("active is :"+active);
        newreciever = new replier(true,reply);
        if(active)
        {
        	registerReceiver(newreciever,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
        
        //registerReceiver(reciever,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        //Get the current user from the bundle
        final String userName = extras.getString("username");
        
        
        //Power button to turn off and on the service
        ImageButton activateService = (ImageButton)findViewById(R.id.power);
        //Add reply button that adds a reply to the users current replies, adds them to the database
        ImageButton addReply  = (ImageButton)findViewById(R.id.addReply);
        //Settings button that allows you to change what reply you are currently using
        ImageButton settings = (ImageButton)findViewById(R.id.settings);
        //Help button tells you have to use the UI
        ImageButton help = (ImageButton)findViewById(R.id.help);
        TextView welcome = (TextView)findViewById(R.id.WelcomeText);
        
        welcome.setTextColor(Color.parseColor("#FFFFFF"));
        welcome.setText("Welcome  "+ userName);
        
        activateService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//register the receiver
            	if(!active)
            	{	
            		registerReceiver(newreciever,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
            		active = true;
            		System.out.println("receiver on");
            	}
            	//Otherwise unreg the receiver
            	else
            	{
            		System.out.println("receiver off");
            		unregisterReceiver(newreciever);
            		active = false;
            	}
            }
        });
        
    
	    settings.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	//change to the screen to allow for modifying current message
	        	Intent newIntent = new Intent(v.getContext(), createReply.class);
		    	newIntent.putExtra("username", userName);
		    	if(active)
		    	{
		    		unregisterReceiver(newreciever);
		    	}
		    	newIntent.putExtra("active", active);
		    	newIntent.putExtra("currentReply", reply);
		    	startActivityForResult(newIntent,0);
	        }
	    });
	    

		addReply.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        // switch to the activate service screen
		    	Intent newIntent = new Intent(v.getContext(), smsactivate.class);
		    	newIntent.putExtra("username", userName);
		    	if(active)
		    	{
		    		unregisterReceiver(newreciever);
		    	}
		    	newIntent.putExtra("active", active);
		    	newIntent.putExtra("currentReply", reply);
		    	startActivityForResult(newIntent,0);
		    	
		    }
		});
    }
		        
    
	@Override
	protected void onDestroy() {
	  //unregisterReceiver(mySMSReplier);
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
