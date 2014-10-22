package com.fgc.autocall.ui;

import java.util.ArrayList;
import java.util.List;

import com.fgc.autocall.R;
import com.fgc.autocall.data.ContactPersonWrapper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterContactList extends BaseAdapter{
	private static final String LOG_TAG = "AdapterContactList";
	

	
	private static final int INVALID_INT = -1;
	
	private Context mContext;
	private List<ViewData> mViewDatas = new ArrayList<AdapterContactList.ViewData>();
	
	private int mColorTxtGray;
	private int mColorTxtNormal;
	private int mColorTxtHighlight;
		
	public AdapterContactList(Context context)
	{
		mContext = context;
		mColorTxtGray = mContext.getResources().getColor(R.color.gray_txt);
		mColorTxtNormal = mContext.getResources().getColor(R.color.normal_txt);
		mColorTxtHighlight = mContext.getResources().getColor(R.color.highlight_txt);
	}
	
	public void addContacts(List<ContactPersonWrapper> contacts)
	{
		if (contacts == null)
		{
			return;
		}
		for (ContactPersonWrapper wrapper : contacts)
		{
			mViewDatas.add(new ViewData(wrapper, OneByOneWork.ITEM_STATE_NOT_DO));
		}
		notifyDataSetChanged();
	}
	
	public void addContact(ContactPersonWrapper contact)
	{
		if (contact == null)
		{
			return;
		}
		mViewDatas.add(new ViewData(contact, OneByOneWork.ITEM_STATE_NOT_DO));
		notifyDataSetChanged();
	}
	
	public void resetContacts(List<ContactPersonWrapper> contacts)
	{
		if (contacts == null)
		{
			return;
		}
		mViewDatas.clear();
		for (ContactPersonWrapper wrapper : contacts)
		{
			mViewDatas.add(new ViewData(wrapper, OneByOneWork.ITEM_STATE_NOT_DO));
		}
		notifyDataSetChanged();
	}
	
	public void clearAllContacts()
	{
		mViewDatas.clear();
		notifyDataSetChanged();
	}
	
	public List<ContactPersonWrapper> getAllContacts()
	{
		List<ContactPersonWrapper> contacts = new ArrayList<ContactPersonWrapper>();
		for (ViewData data : mViewDatas)
		{
			contacts.add(data.mContactPersonWrapper);
		}
		return contacts;
	}
	
	private class ViewData
	{
		ContactPersonWrapper mContactPersonWrapper; 
		int mState;
		
		public ViewData(ContactPersonWrapper contactPersonWrapper, int state)
		{
			mContactPersonWrapper = contactPersonWrapper;
			mState = state;
		}
	}
	
	public void setWorkingState(int index, int workState)
	{
		if (index<0 || index>=mViewDatas.size())
		{
			Log.w(LOG_TAG, "index is out of bounds");
			return;
		}
		mViewDatas.get(index).mState = workState;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position<0 || position>=mViewDatas.size())
		{
			return null;
		}
		else
		{
			return mViewDatas.get(position).mContactPersonWrapper;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (position<0 || position>=mViewDatas.size())
		{
			return null;
		}
		
		ContactPersonWrapper wrapper = mViewDatas.get(position).mContactPersonWrapper;
		if(wrapper == null)
			return null;
		
		LinearLayout itemView = null;
		if(convertView == null){
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = (LinearLayout)layoutInflater.inflate(R.layout.e_contact_list_item, parent, false);
		}else
			itemView = (LinearLayout)convertView;
		
		ImageView imgWorking = (ImageView)itemView.findViewById(R.id.img_working);
		TextView name = (TextView)itemView.findViewById(R.id.name);
		TextView phoneNumber = (TextView)itemView.findViewById(R.id.phone_number);
		ImageView imgWorkState = (ImageView)itemView.findViewById(R.id.img_work_state);
		name.setText(wrapper.getContactPerson().getName());
		phoneNumber.setText(wrapper.getContactPerson().getPhoneNumber());
		
		int state = mViewDatas.get(position).mState;
		
		
		switch (state)
		{
		case OneByOneWork.ITEM_STATE_NOT_DO:
			name.setTextColor(mColorTxtNormal);
			phoneNumber.setTextColor(mColorTxtNormal);
			break;
		case OneByOneWork.ITEM_STATE_CALLING:
		case OneByOneWork.ITEM_STATE_MESSAGING:
			name.setTextColor(mColorTxtHighlight);
			phoneNumber.setTextColor(mColorTxtHighlight);
			break;
		case OneByOneWork.ITEM_STATE_DONE_OK:
		case OneByOneWork.ITEM_STATE_DONE_FAILED:
			name.setTextColor(mColorTxtGray);
			phoneNumber.setTextColor(mColorTxtGray);
			break;
		}
		
		
		switch (state)
		{
		case OneByOneWork.ITEM_STATE_NOT_DO:
			imgWorking.setVisibility(View.INVISIBLE);
			imgWorkState.setVisibility(View.INVISIBLE);
			break;
		case OneByOneWork.ITEM_STATE_CALLING:
			imgWorking.setVisibility(View.VISIBLE);
			imgWorkState.setVisibility(View.VISIBLE);
			imgWorkState.setImageResource(R.drawable.work_state_calling);
			break;
		case OneByOneWork.ITEM_STATE_MESSAGING:
			imgWorking.setVisibility(View.VISIBLE);
			imgWorkState.setVisibility(View.VISIBLE);
			imgWorkState.setImageResource(R.drawable.work_state_messaging);
			break;
		case OneByOneWork.ITEM_STATE_DONE_OK:
			imgWorking.setVisibility(View.INVISIBLE);
			imgWorkState.setVisibility(View.VISIBLE);
			imgWorkState.setImageResource(R.drawable.work_state_success);
			break;
		case OneByOneWork.ITEM_STATE_DONE_FAILED:
			imgWorking.setVisibility(View.INVISIBLE);
			imgWorkState.setVisibility(View.VISIBLE);
			imgWorkState.setImageResource(R.drawable.work_state_failed);
			break;
		}
		
    	return itemView;
	}

}
