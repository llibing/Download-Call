package com.fgc.autocall.app;


import android.app.Application;
import android.util.Log;

public class ClientApp  extends Application {
	private static final String LOG_TAG = "ClientApp";
	
	private ConfigManager mConfigManager;
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.i(LOG_TAG, "onCreate");

	}
	
    @Override
    public void onTerminate() {
    	Log.i(LOG_TAG, "onTerminate");
    	
    	finishApp();
        super.onTerminate();
    }
    
	public void initApp(){
		Log.i(LOG_TAG, "initApp");
		Config.initConfig(this);
		mConfigManager = new ConfigManager(this);
	}

	public void finishApp(){
		Log.i(LOG_TAG, "finishApp");

	}
	
	public ConfigManager getConfigManager()
	{
		return mConfigManager;
	}
}
