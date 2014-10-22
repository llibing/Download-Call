package com.fgc.autocall.app.business;

import com.fgc.autocall.app.ClientApp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class MicroPhone {
	private static final String LOG_TAG = "MicroPhone";
	private static Context mContext;
	private static MicroPhone mMicroPhone;
	
	private MicroPhone()
	{
		Log.i(LOG_TAG, "construct MicroPhone");
	}
	
	public static MicroPhone instance(Context context)
	{
		if (mMicroPhone == null)
		{
			mMicroPhone = new MicroPhone();
		}
		
		if (context != null)
		{
			mContext = context;
		}
		else
		{
			Log.e(LOG_TAG, "context is null !");
		}
		
		return mMicroPhone;
	}
	
	public void call(String phoneNumber)
	{
		Log.e(LOG_TAG, "call: " + phoneNumber);
		Intent phoneIntent = new Intent("android.intent.action.CALL",
		Uri.parse("tel:" + phoneNumber));
		mContext.startActivity(phoneIntent);
	}
}
