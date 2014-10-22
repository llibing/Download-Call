package com.fgc.autocall.ui;

import java.util.ArrayList;
import java.util.List;

import com.fgc.autocall.app.business.MicroPhone;
import com.fgc.autocall.data.ContactPerson;
import com.fgc.autocall.data.ContactPersonWrapper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsListViewWrapper {
	private static final String LOG_TAG = "ContactsListViewWrapper";
	
	private Context mContext;
	private TextView mTextViewNotice;
	private ListView mListViewContent;
	private AdapterContactList mAdapter;
		
	public ContactsListViewWrapper(Context context, TextView textView, ListView listView)
	{
		mContext = context;
		mTextViewNotice = textView;
		mListViewContent = listView;
		init();
	}
	
	private void init()
	{
		mAdapter = new AdapterContactList(mContext);
		mListViewContent.setAdapter(mAdapter);
	}
	
	public void add(List<ContactPersonWrapper> wrappers)
	{
		if (wrappers == null || wrappers.size()==0)
		{
			Log.w(LOG_TAG, "invalid wrappers !");
			return;
		}
		mAdapter.addContacts(wrappers);
		mTextViewNotice.setVisibility(View.GONE);
	}
	
	public void add(ContactPersonWrapper wrapper)
	{
		if (wrapper == null)
		{
			Log.w(LOG_TAG, "invalid person !");
			return;
		}
		mAdapter.addContact(wrapper);
		mTextViewNotice.setVisibility(View.GONE);
	}
	
	public void setWorkingState(int index, int state)
	{
		mAdapter.setWorkingState(index, state);
		mListViewContent.smoothScrollToPosition(index+3);
	}
	
	
}
