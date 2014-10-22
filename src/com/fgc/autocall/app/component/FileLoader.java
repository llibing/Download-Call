package com.fgc.autocall.app.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fgc.autocall.Tools.StringTools;

import android.os.AsyncTask;
import android.util.Log;

public class FileLoader {

	private static final String LOG_TAG = "FileLoader";
	private String mPath;
	private LoadObserver mObserver;
	private TaskLoadFile mTaskLoadFile;
	
	public FileLoader(String path)
	{
		mPath = path;
		mTaskLoadFile = new TaskLoadFile();
	}
	
	public interface LoadObserver
	{
		public void onloadOver(boolean isSuccess, List<String> contentList);
	}
	
	public void load(LoadObserver observer)
	{
		Log.i(LOG_TAG, "load");
		mObserver = observer;
		mTaskLoadFile.execute(mPath);
	}
	
	private class TaskLoadFile extends AsyncTask<String, Integer, List<String>>
	{

		@Override
		protected List<String> doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			Log.i(LOG_TAG, "doInBackground");
			List<String> contentList = new ArrayList<String>();
			File file = new File(mPath);
			if (!file.exists() || !file.isFile())
			{
				Log.w(LOG_TAG, "file not exists");
				return null;
			}
			BufferedReader bufferReader = null;
			try
			{
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
				bufferReader = new BufferedReader(isr);
				String tempString = null;
				while ((tempString=bufferReader.readLine()) != null)
				{
					contentList.add(tempString);
				}
			}
			catch (FileNotFoundException e) 
			{
				// TODO: handle exception
				Log.w(LOG_TAG, "occur file not found exception !");
				e.printStackTrace();
			}
			catch (IOException e)
			{
				Log.w(LOG_TAG, "occur io exception !");
				e.printStackTrace();
			}
			finally
			{
				if (bufferReader != null)
				{
					try {
						Log.i(LOG_TAG, "close reader");
						bufferReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			if (contentList.size() == 0)
			{
				return null;
			}
			else
			{
				return contentList;
			}
		}
		
	   protected void onProgressUpdate(Integer... progress)
	   {
         Log.i(LOG_TAG, "load progress: " + progress);
       }

      protected void onPostExecute(List<String> list) 
      {
    	  Log.i(LOG_TAG, "onPostExecute");
    	  boolean isSuccess = false;
    	  if (list!=null && list.size()>0)
    	  {
    		  isSuccess = true;
    	  }
         if (mObserver != null)
         {
        	 mObserver.onloadOver(isSuccess, list);
         }
      }

		
	}
}
