package com.fgc.autocall.app.component;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

/**
 * @description get useful storage path. 
 * @notice notice add relative permission.
 * @author fgc
 *
 */
public class StorageManagerFgc {
	private static final String LOG_TAG = "StorageManagerFgc";
	
	private Context mContext;
    private StorageManager mStorageManager;  
    private Method mMethodGetPaths;  
    private Method mMethodGetPathsState;
    
	public StorageManagerFgc(Context context)
	{
		mContext = context;
		init();
	}
	
	private void init()
	{
        mStorageManager=(StorageManager)mContext.  
                getSystemService(Context.STORAGE_SERVICE);  
        try{  
        	/*
        	 * ͨ���������ʵ��mStorageManager��getClass()��ȡStorageManager���Ӧ��Class����
        	 * getMethod("getVolumePaths")����StorageManager���Ӧ��Class�����getVolumePaths���������ﲻ������ 
        	 * getDeclaredMethod()----���Բ���ԭ�����ĵ���Ȩ��
        	 */
        	mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");  
        	mMethodGetPathsState=mStorageManager.getClass().getMethod("getVolumeState",String.class);
        }catch(NoSuchMethodException ex){  
            ex.printStackTrace();  
        } 
//        
//        
//        Log.i(LOG_TAG, "external: " + getExternalStoragePath());
//        Log.i(LOG_TAG, "internal: " + getInternalStoragePath());
//        Log.i(LOG_TAG, "app path: " + getAppStoragePath());
//        
//        for (String s : new File(getInternalStoragePath()).list())
//        {
//        	Log.i(LOG_TAG, "--  " + s);
//        }
	}
	
	/**
	 * @return String. example /mnt/sdcard
	 */
	public String getExternalStoragePath()
	{
		String externalPathFromEnvironment = Environment.getExternalStorageDirectory().getPath();
		Log.i(LOG_TAG, "storage path from environment: " + externalPathFromEnvironment);
		return externalPathFromEnvironment;
	}
	
	public String getInternalStoragePath()
	{
		String path = null;
		List<String> allMountedPaths = getMountedStoragePaths();
		for (String s : allMountedPaths)
		{
			if (!s.equals(getExternalStoragePath()))
			{
				path = s;
				break;
			}
		}
		return path;
	}
	
	/**
	 * @return /data/data/com.xxx.xxx/files
	 */
	public String getAppStoragePath()
	{
		String path = mContext.getApplicationContext().getFilesDir().getAbsolutePath();
		Log.i(LOG_TAG, "getAppStoragePath: " + path);
		return path;
	}
	
	
	/**
	 * �������Ѱϵͳ�洢�����ĳһ���ļ������ļ��У�ʹ��������ʵ㣬 ��Ϊ��ȡ�ڲ��洢���ⲿ�洢�������м��������⣬���绪ΪC8813Q�ֻ���ȡ��������洢·���Ƿ��ġ�
	 * @return
	 */
	public List<String> getMountedStoragePaths()
	{	
		List<String> mountedPaths = new ArrayList<String>();
		
		String[] paths = getAllStoragePaths();
        Log.i(LOG_TAG, "all paths:");
        if (paths!=null)
        {
        	for (String path : paths)
        	{
        		Log.i(LOG_TAG, "path: " + path);
        	}
        }
        
        String mountedPath = null;
        
        for (String path : paths)
        {
        	if (isMounted(path))
        	{
        		Log.i(LOG_TAG, "path: " + path + " is mounted");
        		mountedPaths.add(path);
        	}
        }
        
        return mountedPaths;
	}
	 
	private String[] getAllStoragePaths()
	{
		String[] paths  =null;  
        try{  
        	paths=(String[])mMethodGetPaths.invoke(mStorageManager);//���ø÷���  
        }catch(IllegalArgumentException ex){  
            ex.printStackTrace();  
        }catch(IllegalAccessException ex){  
            ex.printStackTrace();     
        }catch(InvocationTargetException ex){  
            ex.printStackTrace();  
        }
        
        return paths;
	}
    
    private String getVolumeState(String mountPoint){  
        String status=null;  
        try{  
            	status=(String)mMethodGetPathsState.invoke(mStorageManager, mountPoint);  
            //���ø÷�����mStorageManager��������mountPoint��ʵ����  
        }catch(IllegalArgumentException ex){  
            ex.printStackTrace();  
        }catch(IllegalAccessException ex){  
            ex.printStackTrace();     
        }catch(InvocationTargetException ex){  
            ex.printStackTrace();  
        }  
        Log.i(LOG_TAG, "mount point: " + mountPoint + "  state: " + status);  
        return status;  
    } 
    
    public boolean isMounted(String mountPoint)
    {  
        String status=null;
        boolean result = false; 
		status = getVolumeState(mountPoint);
		if(Environment.MEDIA_MOUNTED.equals(status)){
			result = true;   			
		}
        return result;  
    }
}
