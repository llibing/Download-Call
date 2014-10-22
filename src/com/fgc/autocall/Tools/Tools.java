/**
* @Title: 		com.targetv.tools.AndroidTools.java 
* @Description: TODO
* @author 		Ken Lee
* @date 		2012-12-21 下午1:39:22
*/
package com.fgc.autocall.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.SoftReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


public class Tools {
	private static final String LOG_TAG = "Tools";
	private Tools(){}
	
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 

	public static int px2dip(Context context, float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
	} 


	
	public static int getScreenWidth(Context context){
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

	    return windowManager.getDefaultDisplay().getWidth();
	}
	
	public static int getScreenHeight(Context context){
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    return windowManager.getDefaultDisplay().getHeight();
	}
	
	public static long getCurDate(){
		Date cur = new Date();
		return cur.getTime();
	}
	

	
	public static String getFilePathBaseDir(String filePath){
    	if(filePath == null || filePath.length() <= 0)
    		return null;
    	
		int lastPos = filePath.lastIndexOf("/");
		if(lastPos > 0){
			String dirStr =null;
			try{
			dirStr = filePath.substring(0, lastPos);
			}catch(IndexOutOfBoundsException  e){
				return null;
			}
			return dirStr;
		}	
    	return null;
    }
    
	public static final long ONE_DAY_SECONDE_NUM = 3600*24;
	
	public static void DeleteDirorFile(File targetDir){
		if(targetDir == null)
			return;
		if(targetDir.isFile())
			targetDir.delete();
		else{
			File[] files = targetDir.listFiles();
			if(files !=null){
				for(File file : files)
					DeleteDirorFile(file);
			}
			targetDir.delete();
		}
	}

	
	/**
	 * 
	 * @param file
	 * @param bitmapSize , using file size if bitmapSize is 0
	 * @return null if failed
	 */
	public static Bitmap getFileBitmap(File file, int bitmapSize){
		if(file ==null || file.isFile()==false || file.exists()==false)
			return null;
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = bitmapSize;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            if(bitmapSize > 0){
	            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
	                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
	                scale *= 2;
            }
            
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
        	try {
        		return BitmapFactory.decodeStream(new FileInputStream(file),null, o2);
        	} catch (OutOfMemoryError e) {
        		return null;
        	}

        } catch (FileNotFoundException e) {
        	Log.w(LOG_TAG, e.toString());
        }
        return null;
	}
	
	private static Map<String, SoftReference<Drawable>> StaticDrawableMap = new HashMap<String ,SoftReference<Drawable>>();
	public static Drawable decodeFile(Context context,String filePath) {
			if(StringTools.IsEmpty(filePath))
				return null;
			
			SoftReference<Drawable> targetDrawable = StaticDrawableMap.get(filePath);
			if(targetDrawable !=null && targetDrawable.get() != null)
				return targetDrawable.get();
			
		 File imgFile = new File(filePath);
		 
	        try {
	            // Decode image size
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o);

	            // The new size we want to scale to
	            final int REQUIRED_SIZE = 600;

	            // Find the correct scale value. It should be the power of 2.
	            int scale = 1;
	            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
	                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
	                scale *= 2;

	            // Decode with inSampleSize
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inSampleSize = scale;
	            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imgFile),null, o2);
	            ImageView iv = new ImageView(context);
	            
	            iv.setImageBitmap(bitmap);
	            SoftReference<Drawable> softReference = new SoftReference<Drawable>(iv.getDrawable());
	            StaticDrawableMap.put(filePath, softReference);
	            return softReference.get();
	        } catch (FileNotFoundException e) {
	        	
	        }
	        return null;
	    }
	 
	/**
	 *  Note: if the resource in drawable DIR, the result = height * density
	 * @return height of image in PX
	 */
	public static int getImgResourceHeight(Context context, int imgResouceId,boolean useDensity){
		if(context == null)
			return -1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        
        BitmapFactory.decodeResource(context.getResources(), imgResouceId,options);
        if(useDensity == true)
        	return Tools.dip2px(context, options.outHeight);
        else
        	return options.outHeight;
        
	}
	public static int getImgResourceWidth(Context context, int imgResouceId, boolean useDensity){
		if(context == null)
			return -1;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        
        //BitmapFactory.decodeResource(context.getResources(),imgResouceId);
        BitmapFactory.decodeResource(context.getResources(), imgResouceId,options);
        
        if(useDensity == true)
        	return Tools.dip2px(context, options.outWidth);
        else
        	return options.outWidth;
	}
	
	public static int getStatusBarHeight(Activity context){
		        Class<?> c = null; 
		        Object obj = null; 
		        java.lang.reflect.Field field = null; 
		        int x = 0; 
		        int statusBarHeight = 0; 
		        try 
		        { 
		            c = Class.forName("com.android.internal.R$dimen"); 
		            obj = c.newInstance(); 
		            field = c.getField("status_bar_height"); 
		            x = Integer.parseInt(field.get(obj).toString()); 
		            statusBarHeight = context.getResources().getDimensionPixelSize(x); 
		            return statusBarHeight; 
		        } 
		        catch (Exception e) 
		        { 
		            e.printStackTrace(); 
		        } 
		        return statusBarHeight; 
	}

    
    public static void ToastShow(Context context, String txt, boolean isLong){
    	Toast toast = Toast.makeText(context, txt, (isLong==true?Toast.LENGTH_LONG:Toast.LENGTH_SHORT));//.show();
    	toastShowAsyn(toast);
    }
    public static void ToastShow(Context context, int strId, boolean isLong){
    	Toast toast = Toast.makeText(context, strId, (isLong==true?Toast.LENGTH_LONG:Toast.LENGTH_SHORT));//.show();
    	toastShowAsyn(toast);
    }
    
    public static void ToastShow(Context context, String txt, boolean isLong, boolean isInCenter){
    	Toast toast = Toast.makeText(context, txt, (isLong==true?Toast.LENGTH_LONG:Toast.LENGTH_SHORT));
    	if (isInCenter)
    		toast.setGravity(Gravity.CENTER, 0, 0);
    	toastShowAsyn(toast);
    }
    public static void ToastShow(Context context, String txt, boolean isLong,int gravity, int xOffset, int yOffset){
    	Toast toast = Toast.makeText(context, txt, (isLong==true?Toast.LENGTH_LONG:Toast.LENGTH_SHORT));
    	toast.setGravity(gravity, xOffset, yOffset);
    	toastShowAsyn(toast);
    }
    private static void toastShowAsyn(final Toast toast ){
    	Runnable runable =new Runnable(){
			@Override
			public void run() {
				toast.show();
			}
    	};
    	runable.run();
    }
    
    
    public static Bitmap convertViewToBitmap(View view){
    	view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    	view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    	view.buildDrawingCache();
    	Bitmap bitmap = view.getDrawingCache();
    	return bitmap;
  }
    
    public static String getSDPath(){ 
        File sdDir = null; 
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存�?
        if   (sdCardExist)   
        {                               
          sdDir = Environment.getExternalStorageDirectory();//获取跟目�?
       }   
        return sdDir.toString();
 } 
    
    /**
     * 
     * @return  0~100
     */
    private static int SYS_MUSIC_MAX_VOLUE =-1;
    public static int getSysMusicVolume(Context context){
    	AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    	if(SYS_MUSIC_MAX_VOLUE == -1)
    	{
    		SYS_MUSIC_MAX_VOLUE = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    	}
    	int curValue = am.getStreamVolume(AudioManager.STREAM_MUSIC);
    	return (curValue * 100) / SYS_MUSIC_MAX_VOLUE;
    }
    
    public static void vibrate(Context context, long milliseconds) {
    	  Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    	  vib.vibrate(milliseconds);
    	 }
    
    
	public static String[] getImageNames(String folderPath) {
		File dir = new File(folderPath);

		String[] files = dir.list();

		int imageFileNums = 0;
		for (int i = 0; i < files.length; i++) {
			File f = new File(folderPath + "/" + files[i]);

			if (f.isFile()) 
			{
				imageFileNums++;
			}
		}

		String[] rtnImageFiles = new String[imageFileNums];

		int j = 0;
		for (int i = 0; i < files.length; i++) {
			File f = new File(folderPath + "/" + files[i]);

			if (f.isFile()) {
				rtnImageFiles[j] = f.getName();
				j++;
			}
		}
		return rtnImageFiles;
	}
	
	public static void printFunctionCallStack(String tag, int level)
    {
    	Log.w(tag, "print call stack ");
    	
    	StackTraceElement[] ste = new Throwable().getStackTrace();
    	Log.d(LOG_TAG, "length: " + ste.length);
    	if(ste.length >=1)
    	{
    		for(int i = 1; i < ste.length; i++)
    		{
    			if (i>level)
    			{
    				break;
    			}
    			Log.w(tag, "File:" + ste[i].getFileName() + "  Line: " + ste[i].getLineNumber() + "  MethodName: " + ste[i].getMethodName());
    		}
    	}

    }

	
	public static void writeLogToFile(String str)
	{
		File file = null;
		File dir = new File("/mnt/sdcard/targetv/log");
		file = new File("/mnt/sdcard/targetv/log/log.txt");
		if (!file.exists())
		{
		
			dir.mkdirs();
			if (dir.isDirectory())
			{
				file = new File("/mnt/sdcard/targetv/log/log.txt");
				if (file.exists())
				{
					Log.i(LOG_TAG, "file has exsit.");
				}
				else
				{
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else
			{
				Log.e(LOG_TAG, "writeLogToFile error.");
				return;
			}
		}
		
		StringBuilder strBuilder = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file));
			BufferedReader br = new BufferedReader(isr);
			String s = null;
			
			while ((s = br.readLine()) != null) {
				strBuilder.append(s+ "\r\n");
			}
			
			br.close();

		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
//			Log.i(LOG_TAG, "write string: " + strBuilder.toString() + str);
			bw.write(strBuilder.toString() + str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void printFunctionCallStack(int deep)
    {
    	Log.d(LOG_TAG, "print call stack ");
    	
    	StackTraceElement[] ste = new Throwable().getStackTrace();
    	if(ste.length >=1)
    	{
    		for(int i = 0; i < ste.length; i++)
    		{
    			if ( i>=deep )
    			{
    				break;
    			}
    			Log.d(LOG_TAG, "File:" + ste[i].getFileName() + "   Line: " + ste[i].getLineNumber() + "  MethodName:" + ste[i].getMethodName());
    		}
    	}

    }
    
    /**
     * 
     * @param String, example: 13:30
     * @return Calendar
     */
    public static Calendar getCalendarForHourAndMinus(String time)
    {
		if (null == time)
		{
			return new GregorianCalendar();
		}
		
		String[] times = time.split(":|��");
		if (times == null || times.length != 2)
		{
			return new GregorianCalendar();
		}
		
		int hour = Integer.parseInt(times[0]);
		int minus = Integer.parseInt(times[1]);
		
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minus);
				
		return calendar;
    }
    
    /**
     * may be need some permission.
     * @param fileName
     * @param dirPath
     * @return
     */
    public static boolean isExistOneFileInOneDir(String fileName, String dirPath)
    {
    	if (fileName == null || dirPath == null)
    	{
    		Log.e(LOG_TAG, "check param !");
    		return false;
    	}
    	File dir = new File(dirPath);
    	if (!dir.isDirectory())
    	{
    		Log.e(LOG_TAG, "dirPaht is not a dir !");
    		return false;
    	}
    	
    	String[] files = dir.list();
    	if (files == null || files.length == 0)
    	{
    		return false;
    	}
    	
    	for (String s : files)
    	{
    		if (fileName.equals(s))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
}
