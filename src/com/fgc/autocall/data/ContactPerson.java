package com.fgc.autocall.data;

public class ContactPerson {
	private String mName;
	private String mPhoneNumber;
	private String mNote1;
	private String mNote2;
	private String mNote3;
	private String mNote4;
	
	public ContactPerson(String name, String phoneNumber)
	{
		mName = name;
		mPhoneNumber = phoneNumber;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public String getPhoneNumber()
	{
		return mPhoneNumber;
	}
	
	public String getNote1()
	{
		return mNote1;
	}
	
	public void setNote1(String note)
	{
		mNote1 = note;
	}
	
	public String getNote2()
	{
		return mNote2;
	}
	
	public void setNote2(String note)
	{
		mNote2 = note;
	}
	
	public String getNote3()
	{
		return mNote3;
	}
	
	public void setNote3(String note)
	{
		mNote3 = note;
	}
	
	public String getNote4()
	{
		return mNote4;
	}
	
	public void setNote4(String note)
	{
		mNote4 = note;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = mName + " : " + mPhoneNumber + " : " + mNote1 + " : " + mNote2 + " : " + mNote3 + " : " + mNote4;
		return s;
	}
}
