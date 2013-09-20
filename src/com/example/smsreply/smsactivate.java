package com.example.smsreply;

import java.io.Serializable;
import java.util.Map;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class smsactivate extends Activity{

   
	private replier mySMSReplier;
	private String onlyOnce = "";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_sms);
        Button submit = (Button)findViewById(R.id.SubmitButton);
        final EditText newMessage = (EditText)findViewById(R.id.newMessage);
        //IntentFilter filter = new IntentFilter();
        submit.setTextColor(Color.parseColor("#FFFFFF"));
        
        Bundle extras = getIntent().getExtras(); 
        final String userName = extras.getString("username");
        final String currentReply = extras.getString("currentReply");
        final boolean active = extras.getBoolean("active");
        
        final replier reciever = new replier(true,currentReply);
        if(active)
        {
        	registerReceiver(reciever,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
        
        //want to activate within the replier.java class
        
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Add the information to the database
            	
            	
            	final Firebase increaseRef = new Firebase("https://smsreply.firebaseio.com/usernames/"+userName+"/savedmsgs/length");
        		
        		increaseRef.addValueEventListener(new ValueEventListener() {
        		     @Override
        		     public void onDataChange(DataSnapshot snapshot) {
        		         Object value = snapshot.getValue();
        		         int number;
        		         if (value == null) {
        		        	
        		             	System.out.println("length was not set yet");
        		             	increaseRef.setValue(1);
        		             	number = 1;
        		             	//Firebase ref = new Firebase("https://smsreply.firebaseio.com/usernames/"+userName+"/savedmsgs/");
        		             	Firebase msgAdd = new Firebase("https://smsreply.firebaseio.com/usernames/"+userName+"/savedmsgs/"+number+"/");
        		            	System.out.println(newMessage.getText().toString());
        		            	msgAdd.setValue(newMessage.getText().toString());
        		            	Toast toast = Toast.makeText(getApplicationContext(), "Successfully added : "+newMessage.getText().toString() + " to your message list", Toast.LENGTH_SHORT);
        		        	 	toast.show();
        		            	increaseRef.removeEventListener(this);
        		            	return;
        		         } else {
        		        	 //Exists already in the database
        		             //make toast error here
        		        	 	number = Integer.parseInt(value.toString());
        		        	 	number++;
        		        	 	if(onlyOnce.equals(newMessage.getText().toString()))
        		        	 	{
        		        	 		System.out.println("hit here");
        		        	 		return;
        		        	 	}
        		        	 	increaseRef.setValue(number);
        		            	Firebase msgAdd = new Firebase("https://smsreply.firebaseio.com/usernames/"+userName+"/savedmsgs/"+number+"/");
        		            	msgAdd.setValue(newMessage.getText().toString());
        		        	 	System.out.println(newMessage.getText().toString());
        		        	 	onlyOnce = newMessage.getText().toString();
        		        	 	Toast toast = Toast.makeText(getApplicationContext(), "Successfully added : "+onlyOnce + " to your message list", Toast.LENGTH_SHORT);
        		        	 	toast.show();
        		        	 	increaseRef.removeEventListener(this);
        		        	 	return;
        		         }
        		     }

					@Override
					public void onCancelled() {
						// TODO Auto-generated method stub
						
					}
        		});
        		Intent newIntent = new Intent(v.getContext(), MainActivity.class);
		    	newIntent.putExtra("username", userName);
		    	newIntent.putExtra("active", active);
		    	if(active)
		    	{
		    		unregisterReceiver(reciever);
		    	}
		    	newIntent.putExtra("currentReply", currentReply);
		    	newIntent.putExtra("reciever",reciever);
		    	newIntent.putExtra("fromAdding", true);
		    	//unregisterReceiver(reciever);
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
    
    @Override
	protected void onDestroy() {
	  //unregisterReceiver(mySMSReplier);
	}
}
