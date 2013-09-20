package com.example.smsreply;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class replier extends BroadcastReceiver implements Serializable{
	
	private static final long serialVersionUID = -1583315096315342275L;
	public String str = "";
	private String response = "Please text me later";
	public boolean active;
	
	public replier()
	{
		this.active = true;
		this.response = "hello there";
		System.out.println(response);
	}
	
	public replier(boolean active, String response)
	{
		this.active = active;
		this.response = response;
		System.out.println("created new reciever");
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		SmsMessage[] messages = null;
		SmsManager sms = SmsManager.getDefault();
		
		
		
		if(bundle != null)
		{
			//active = (Boolean) bundle.get("active");
			Object[] pdus = (Object[]) bundle.get("pdus");
			messages = new SmsMessage[pdus.length];
			
			for(int z = 0 ; z < messages.length ; z++)
			{
				messages[z] = SmsMessage.createFromPdu((byte[])pdus[z]);
				String sendersNumber = messages[z].getOriginatingAddress();
				System.out.println("the sender who sent the message is : "+sendersNumber);
				System.out.println("active is : "+this.active);
				//Here is where you reply to the message
				if(active)
				{
					sms.sendTextMessage(sendersNumber, null , response, null,null);
				}
			}
		}
	}
	
	public void setResponse(String response)
	{
		
		if(response == null)
		{
			//toast saying you must enter in a response
			return;
		}
		else
		{
			System.out.println("Setting the response");
			this.response = response;
		}
	}
	
	public void activate()
	{
		System.out.println("getting activated");
		this.active = true;
		System.out.println(active);
	}
	
	public void deactivate()
	{
		this.active = false;
	}

}
