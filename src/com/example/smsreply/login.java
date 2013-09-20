package com.example.smsreply;

import java.io.Serializable;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

public class login extends Activity implements Serializable{

	private static final long serialVersionUID = -6856744969237814170L;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
       
        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        final Button submit = (Button)findViewById(R.id.submit);
        Button create = (Button)findViewById(R.id.create);
        
        
        
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // switch to the activate service screen
            	
            	
        		
        		
        		
                //ref.child("password").setValue(passString);
            	
            	
            	if (username.getText().toString() != null && password.getText().toString() != null)
            	{
            		
            		String userString = username.getText().toString();
            		String passString = password.getText().toString();
            		Firebase f = new Firebase("https://smsreply.firebaseio.com/usernames/"+userString+"/password");
            		f.setValue(passString);
            		
            	}
            	 
            }
        });
            	
            	submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {
                        
                    	final String userString = username.getText().toString();
                		final String passString = password.getText().toString();
                    	
                		System.out.println("getting here");
                		
                    	if (username.getText().toString() != null && password.getText().toString() != null)
                    	{
                    		final Firebase database = new Firebase("https://smsreply.firebaseio.com/usernames/"+userString+"/");
                    		
                    		database.addValueEventListener(new ValueEventListener() {
                    		     @Override
                    		     public void onDataChange(DataSnapshot snapshot) {
                    		         Object value = snapshot.getValue();
                    		         String passworddb="";
                    		         if (value == null) {
                    		        	
                    		             System.out.println("username doesnt exist");
                    		             
                    		         } else {
                    		        	 //Exists already in the database
                    		             //make toast error here
                    		        	 System.out.println("Already exists");
                    		        	 passworddb = (String)((Map)value).get("password");
                    		        	 System.out.println("database password is : "+passworddb);
                    		        	 if(passworddb.equals(passString))
                    		        	 {
                    		        		 Toast toast = Toast.makeText(getApplicationContext(), "Successfully logged in as "+userString, Toast.LENGTH_SHORT);
                    		        		 toast.show();
                    		        		 database.removeEventListener(this);
                    		        		 Intent newIntent = new Intent(v.getContext(), MainActivity.class);
                        		        	 newIntent.putExtra("username",userString);
                        		             startActivityForResult(newIntent,0);
                    		        	 }
                    		        	 else
                    		        	 {
                    		        		 Toast toast = Toast.makeText(getApplicationContext(), "Incorrect Password for  "+userString, Toast.LENGTH_SHORT);
                    		        		 toast.show();
                    		        	 }
                    		        	 
                    		         }
                    		     }
                    		     @Override
                    		     public void onCancelled() {
                    		         System.err.println("Listener was cancelled");
                    		     }
                    		 });
                    	}
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
