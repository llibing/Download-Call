package com.fgc.autocall.app.business;


import java.util.List;

import com.fgc.autocall.Tools.StringTools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class MessageSender {
	private static final String LOG_TAG = "MessageSender";
	private static MessageSender mMessageSender;
	
	private MessageSender()
	{
		Log.i(LOG_TAG, "construct MessageSender");
	}
	
	public static MessageSender instance()
	{
		if (mMessageSender == null)
		{
			mMessageSender = new MessageSender();
		}
		
		return mMessageSender;
	}
		
	public void send(String phoneNumber, String msgContent, PendingIntent sendPendingIntent, PendingIntent deliverPendingIntent)
	{
		Log.e(LOG_TAG, "send: " + phoneNumber + " : " + msgContent);
		if (StringTools.IsEmpty(phoneNumber) && StringTools.IsEmpty(msgContent))
		{
			Log.e(LOG_TAG, "please give a valid phone number or msg content");
			return;
		}

    	SmsManager sms = SmsManager.getDefault();
	    List<String> msgs = sms.divideMessage(msgContent);
	    Log.e(LOG_TAG, "msg divide count: " + msgs.size());
	    for (String msg : msgs)
	    {
//	    	sms.sendTextMessage(phoneNumber, null, msg, sendPendingIntent, deliverPendingIntent);
	    }
	}
}
