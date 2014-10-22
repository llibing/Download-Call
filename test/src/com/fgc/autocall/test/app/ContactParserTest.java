package com.fgc.autocall.test.app;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.fgc.autocall.app.component.ContactParser;
import com.fgc.autocall.data.ContactPerson;

import junit.framework.TestCase;

public class ContactParserTest extends TestCase {

	private final String LOG_TAG = "ContactParserTest";
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testContactParser()
	{
		Log.i(LOG_TAG, "testContactParser");
		List<String> list = new ArrayList<String>();
		
		list.add("李一 ：15157148719 ：  备注1 ：备注2 ：备注3");
		list.add("李二 :15157148720 ：备注1 ：备注2 ：备注3");
		list.add("李三 ：15157148721: 备注1 ：备注2 ：备注3");
		list.add("李四 ：15157148722 ：备注1 ：备注2 ：备注3");
		
		ContactParser parser = new ContactParser(list);
		List<ContactPerson> persons = new ArrayList<ContactPerson>();
		assertTrue(parser.parse(persons));
		for (ContactPerson person : persons)
		{
			Log.i(LOG_TAG, person.toString());
		}
	}

}
