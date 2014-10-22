package com.fgc.autocall.data;

import android.util.Log;

import com.fgc.autocall.Tools.StringTools;

public class ContactPersonWrapper {
	private static final String LOG_TAG = "ContactPersonWrapper";
	
	private static final String DEFAULT_MESSAGE_FORMAT = "%您好，您预订的商品%将于2014年%上架销售，价格为%元，敬请关注，谢谢。";
	private ContactPerson mContactPerson;
	private String mMessageFormat = DEFAULT_MESSAGE_FORMAT;
	
	private boolean mIsCalling;
	
	public ContactPersonWrapper(ContactPerson contactPerson)
	{
		mContactPerson = contactPerson;
	}
	
	public ContactPerson getContactPerson()
	{
		return mContactPerson;
	}
	
	public String generateShortMessage()
	{
		mMessageFormat = mMessageFormat.replaceFirst("%", mContactPerson.getName());
		mMessageFormat = mMessageFormat.replaceFirst("%", mContactPerson.getNote1());
		mMessageFormat = mMessageFormat.replaceFirst("%", mContactPerson.getNote2());
		mMessageFormat = mMessageFormat.replaceFirst("%", mContactPerson.getNote3());
		
		return mMessageFormat;
	}
	
	public void setMessageFormat(String format)
	{
		mMessageFormat = format;
	}
	
	public boolean isSupportMessage()
	{
		boolean isMobile = StringTools.isMobile(mContactPerson.getPhoneNumber());
		if (isMobile)
		{
			Log.i(LOG_TAG, mContactPerson.getPhoneNumber() + "is mobile");
		}
		else
		{
			Log.i(LOG_TAG, mContactPerson.getPhoneNumber() + "is not mobile");
		}
		return isMobile;
	}
	
	public void setIsCalling(boolean isCalling)
	{
		mIsCalling = isCalling;
	}
	
	public boolean getIsCalling()
	{
		return mIsCalling;
	}
}
