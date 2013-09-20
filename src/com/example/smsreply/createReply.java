package com.example.smsreply;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

public class createReply extends Activity{

	
	protected String currentReply;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectcurrent);
        //final Spinner messages = (Spinner)findViewById(R.id.messageList);
        final Button change = (Button)findViewById(R.id.change);
        Bundle extras = getIntent().getExtras(); 
        final String userName = extras.getString("username");
        
        //Time now = new Time();
        //now.setToNow();
        
        final boolean active = extras.getBoolean("active");
        
        final replier reciever = new replier(true,currentReply);
        if(active)
        {
        	registerReceiver(reciever,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
        
        //want to activate within the replier.java class
        
        final ArrayAdapter<String> messageAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        
        final Firebase dataRef = new Firebase("https://smsreply.firebaseio.com/usernames/"+userName+"/savedmsgs/");
        dataRef.child("length").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if(value == null)
                {
                	System.out.println("There are no items in the list");
                }
                else
                {
                	int length = Integer.parseInt(value.toString());
                	dataRef.child("length").removeEventListener(this);
                	Spinner messageSpinner = (Spinner)findViewById(R.id.messageList);
                	for(int i = 0 ; i < length ; i++)
                	{
            	        dataRef.child(i+"").addValueEventListener(new ValueEventListener() {
            	            @Override
            	            public void onDataChange(DataSnapshot snapshot) {
            	                Object innervalue = snapshot.getValue();
            	            	if(innervalue == null)
            	            	{
            	            		System.out.println("nothing at this index");
            	            	}
            	            	else
            	            	{
            	            		//add the messages to the spinner
            	            		System.out.println(innervalue.toString());
            	            		messageAdapter.add(innervalue.toString());
            	            	}
            	            }
            	            
            	            @Override
            	            public void onCancelled() {
            	                System.err.println("Listener was cancelled");
            	            }
            	        });
            	        dataRef.child(i+"").removeEventListener(this);
                	}
                	messageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
                	messageSpinner.setAdapter(messageAdapter);
                	messageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                	        String selected = (String)parent.getItemAtPosition(pos);
                	        Toast toast = Toast.makeText(getApplicationContext(), "Selected : "+selected, Toast.LENGTH_SHORT);
    		        	 	toast.show();
    		        	 	currentReply = selected;
    		        	 	//unregisterReceiver(reciever);
    		        	 	//replier newreciever = new replier(true,selected);
    		                //registerReceiver(newreciever,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    		        	 	//make a call here to the braodcast reciever to change the current reply
                	    }
                	    public void onNothingSelected(AdapterView<?> parent) {
                	    	
                	    }
                	});
                }	
            }

            @Override
            public void onCancelled() {
                System.err.println("Listener was cancelled");
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // switch to the activate service screen
            	Intent newIntent = new Intent(v.getContext(), MainActivity.class);
            	newIntent.putExtra("username",userName);
            	if(active)
            	{
            		unregisterReceiver(reciever);
            	}
            	newIntent.putExtra("active",active);
            	newIntent.putExtra("currentReply", currentReply);
            	startActivityForResult(newIntent,0);
            }
        });
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
