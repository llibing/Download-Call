package com.fgc.autocall.app.component;

import java.util.List;

import android.util.Log;

import com.fgc.autocall.data.ContactPerson;

public class ContactParser {
	private static final String LOG_TAG = "ContactParser";
	
	private List<String> mContactRawList;
	
	public ContactParser(List<String> contactRawList)
	{
		mContactRawList = contactRawList;
	}
	
	public boolean parse(List<ContactPerson> contactPersonList)
	{
		if (contactPersonList == null || mContactRawList == null)
		{
			Log.w(LOG_TAG, "contactPersonList == null || mContactRawList == null");
			return false;
		}
		
		for (String s : mContactRawList)
		{
			String[] cells = s.split(":|£º");
			if (cells==null || cells.length<2)
			{
				Log.w(LOG_TAG, "continue !");
				continue;
			}
			ContactPerson person = new ContactPerson(cells[0].trim(), cells[1].trim());			
			for (int i=0; i<cells.length; i++)
			{
				if (i<2)
				{
					continue;
				}
				
				switch(i)
				{
				case 2:
					person.setNote1(cells[i].trim());
					break;
				case 3:
					person.setNote2(cells[i].trim());
					break;
				case 4:
					person.setNote3(cells[i].trim());
					break;
				case 5:
					person.setNote4(cells[i].trim());
					break;
				}
			}
			contactPersonList.add(person);
		}
		
		return true;
	}
	
}
