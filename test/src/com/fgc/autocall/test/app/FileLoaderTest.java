package com.fgc.autocall.test.app;

import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.fgc.autocall.app.component.ContactParser;
import com.fgc.autocall.app.component.FileLoader;
import com.fgc.autocall.app.component.FileLoader.LoadObserver;
import com.fgc.autocall.data.ContactPerson;

import junit.framework.TestCase;

public class FileLoaderTest extends TestCase {

	private final String LOG_TAG = "FileLoaderTest";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testFileLoader()
	{
		Log.i(LOG_TAG, "testFileLoader");
		String sdcardPath = Environment.getExternalStorageDirectory().getPath();
		String filePath = sdcardPath + "/contact.txt";
		FileLoader fileLoader = new FileLoader(filePath);
		fileLoader.load(new LoadObserver() {
			
			@Override
			public void onloadOver(boolean isSuccess, List<String> contentList) {
				// TODO Auto-generated method stub
				assertTrue(isSuccess);
				
				if (isSuccess)
				{
					Log.i(LOG_TAG, "success");
					for (int i=0; i<contentList.size(); i++)
					{
						Log.i(LOG_TAG, contentList.get(i));
					}
				}
				else
				{
					Log.i(LOG_TAG, "fail");
				}
			}
		});
	}
	
	public void testFileLoaderAndContactParser()
	{
		Log.i(LOG_TAG, "testFileLoader");
		String sdcardPath = Environment.getExternalStorageDirectory().getPath();
		String filePath = sdcardPath + "/contact.txt";
		FileLoader fileLoader = new FileLoader(filePath);
		fileLoader.load(new LoadObserver() {
			
			@Override
			public void onloadOver(boolean isSuccess, List<String> contentList) {
				// TODO Auto-generated method stub
				
				if (isSuccess)
				{
					Log.i(LOG_TAG, "success");
					for (int i=0; i<contentList.size(); i++)
					{
						Log.i(LOG_TAG, contentList.get(i));
					}
					Log.i(LOG_TAG, "--------------------------------");
					ContactParser parser = new ContactParser(contentList);
					List<ContactPerson> persons = new ArrayList<ContactPerson>();
					parser.parse(persons);
					for (ContactPerson person : persons)
					{
						Log.i(LOG_TAG, person.toString());
					}
				}
				else
				{
					Log.i(LOG_TAG, "fail");
				}
			}
		});
	}

}
