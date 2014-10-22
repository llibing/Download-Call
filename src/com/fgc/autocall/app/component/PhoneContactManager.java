package com.fgc.autocall.app.component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class PhoneContactManager {
	private static final String LOG_TAG = "PhoneContactManager";
	private static final int ID_HOME = 1;
	private static final int ID_MOBILE = 2;
	private static final String TEXT_NONE = "";
	public static final String SPACE_STRING_1 = " ";
	public static final String SPACE_STRING_2 = SPACE_STRING_1 + SPACE_STRING_1;
	public static final String SPACE_STRING_4 = SPACE_STRING_2 + SPACE_STRING_2;
	public static final String SPACE_STRING_8 = SPACE_STRING_4 + SPACE_STRING_4;
	public static final String NO_MOBILE_NUM = SPACE_STRING_8 + SPACE_STRING_2 + SPACE_STRING_1;
	public static final char ENTER_CHAR_LINUX = '\n';
	
	private Context mContext;
	private int mCount = 0;
	
	public PhoneContactManager(Context context)
	{
		mContext = context;
	}
	
	/**
	 * ºÄÊ± !
	 */
	public void exportContactsToFile(String fileFullPath)
	{
		if (fileFullPath == null)
		{
			Log.e(LOG_TAG, "fileFullPath is null !");
			return;
		}
		
		File file = new File(fileFullPath);
		if (file.exists())
		{
			if (file.delete())
			{
				Log.i(LOG_TAG, "delete old file success");
			}
			else
			{
				Log.i(LOG_TAG, "delete old file failed");
			}
		}
		
		mCount = 0;
		outputContacts(fileFullPath);
	}
	
	private boolean outputContacts(String fileFullPath) {
		try {
			String result = getFromContactDatabase(mContext);
			writeFile(fileFullPath, result);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error in outputContacts " + e.getMessage());
			return false;
		}
		return true;
	}

	private String getFromContactDatabase(Context context) {
		StringBuilder resultBuilder = new StringBuilder();
		Cursor cursor = context
				.getContentResolver()
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] { StructuredName.DISPLAY_NAME,
								Data.RAW_CONTACT_ID }, Data.MIMETYPE + "= ?",
						new String[] { StructuredName.CONTENT_ITEM_TYPE }, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// get display name and row id
			String displayName = cursor.getString(0);
			int id = cursor.getInt(1);
			
			// get phone num
			Cursor mobileCursor = context.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI,
					new String[] { Phone.NUMBER },
					Data.RAW_CONTACT_ID + " = " + id + " AND " + Data.DATA2
							+ " = " + ID_MOBILE, null, null);
			String mobileNum = TEXT_NONE;
			mobileCursor.moveToFirst();
			if (!mobileCursor.isAfterLast()) {
				mobileNum = mobileCursor.getString(0);
			}
			mobileCursor.close();

			// get home num
			Cursor homeCursor = context.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI,
					new String[] { Phone.NUMBER },
					Data.RAW_CONTACT_ID + " = " + id + " AND " + Data.DATA2
							+ " = " + ID_HOME, null, null);
			String homeNum = "";
			homeCursor.moveToFirst();
			if (!homeCursor.isAfterLast()) {
				homeNum = homeCursor.getString(0);
			}
			homeCursor.close();

			if (displayName != null && (!displayName.equals(TEXT_NONE) || 
					!displayName.equals(TEXT_NONE))) {
				String result = displayName + SPACE_STRING_4;
				if(mobileNum.equals(TEXT_NONE)){
					result += NO_MOBILE_NUM;
				}
				else {
					result += mobileNum;
				}
				result += SPACE_STRING_8 + homeNum;
				result += ENTER_CHAR_LINUX;
				String checkString = resultBuilder.toString();
				if(!checkString.contains(result) && (mobileNum.equals(TEXT_NONE) ||
						!checkString.contains(mobileNum))){
					resultBuilder.append(result);
					mCount++;
				}
			}
			cursor.moveToNext();
		}
		cursor.close();
		return resultBuilder.toString();
	}
	
	private void writeFile(String path, String buffer) {
		FileWriter writer = null;
		try {
			File file = new File(path);
			writer = new FileWriter(file, false);
			writer.write(buffer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();			
		}
		finally
		{
			if (writer != null)
			{
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
