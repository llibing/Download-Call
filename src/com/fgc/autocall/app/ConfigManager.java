package com.fgc.autocall.app;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.util.Log;

/**
 * manager config item of system.
 * @author fgc
 *
 */
public class ConfigManager {
	private static final String LOG_TAG = "ConfigManager";
		
	private Context mContext;
	
	public ConfigManager(Context context)
	{
		mContext = context;
	}

	/**
	 * @description s
	 */
	public void saveCallInternal(int internal)
	{
		String toSave = String.valueOf(internal);
		Log.i(LOG_TAG, "saveCallInternal: " + toSave);
		Config.setValue(Config.CONFIG_KEY_CALL_INTERNAL, toSave);
	}
	
	/**
	 * @description default is 30*1000 ms
	 * @return ms
	 */
	public int getCallInternal()
	{
		int internal = 30;
		String internalString = Config.getValue(Config.CONFIG_KEY_CALL_INTERNAL, String.valueOf(internal));
		return Integer.parseInt(internalString);
	}
	
	/**
	 * @description 13:30
	 */
	public void  saveStartCallTime(String callTime)
	{
		Log.i(LOG_TAG, "saveStartCallTime: " + callTime);
		Config.setValue(Config.CONFIG_KEY_START_CALL_TIME, callTime);
	}
	
	/**
	 * @description
	 * @return example 13:30
	 */
	public String getStartCallTime()
	{
		String startTime = Config.getValue(Config.CONFIG_KEY_START_CALL_TIME, null);
		return startTime;
	}
	
	/**
	 * 
	 * @param minus
	 */
	public void saveSimCardTimeLength(int minus)
	{
		String toSave = String.valueOf(minus);
		Log.i(LOG_TAG, "saveSimCardTimeLength: " + toSave);
		Config.setValue(Config.CONFIG_KEY_SIM_CARD_TIME_LENGTH, toSave);
	}
	
	/**
	 * 
	 * @return minus
	 */
	public int getSimCardTimeLength()
	{
		String saveValue = Config.getValue(Config.CONFIG_KEY_SIM_CARD_TIME_LENGTH, String.valueOf(0));
		return Integer.parseInt(saveValue);
	}
	
	public void saveWarnningPhoneNumber(String phoneNumber)
	{
		Log.i(LOG_TAG, "saveWarnningPhoneNumber: " + phoneNumber);
		Config.setValue(Config.CONFIG_KEY_WARNNING_PHONE_NUMBER, phoneNumber);
	}
	
	public String getWarnningPhoneNumber()
	{
		return Config.getValue(Config.CONFIG_KEY_WARNNING_PHONE_NUMBER, null);
	}
	
	public void saveFunctionSendMessage(boolean isSend)
	{
		if (isSend)
		{
			Config.setValue(Config.CONFIG_KEY_FUNCTION_SEND_MESSAGE, Config.CONFIG_VALUE_YES);
		}
		else
		{
			Config.setValue(Config.CONFIG_KEY_FUNCTION_SEND_MESSAGE, Config.CONFIG_VALUE_NO);
		}
	}
	
	public boolean isSendMessage()
	{
		String isSendString = Config.getValue(Config.CONFIG_KEY_FUNCTION_SEND_MESSAGE, Config.CONFIG_VALUE_YES);
		if (Config.CONFIG_VALUE_YES.equals(isSendString))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void saveFunctionCall(boolean isCall)
	{
		if (isCall)
		{
			Config.setValue(Config.CONFIG_KEY_FUNCTION_CALL, Config.CONFIG_VALUE_YES);
		}
		else
		{
			Config.setValue(Config.CONFIG_KEY_FUNCTION_CALL, Config.CONFIG_VALUE_NO);
		}
	}
	
	public boolean isCall()
	{
		String isCallString = Config.getValue(Config.CONFIG_KEY_FUNCTION_CALL, Config.CONFIG_VALUE_YES);
		if (Config.CONFIG_VALUE_YES.equals(isCallString))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//обть
	public void saveFunctionDownload(boolean isDownload)
	{
		if (isDownload)
		{
			Config.setValue(Config.CONFIG_KEY_FUNCTION_DOWNLOAD, Config.CONFIG_VALUE_YES);
		}
		else
		{
			Config.setValue(Config.CONFIG_KEY_FUNCTION_DOWNLOAD, Config.CONFIG_VALUE_NO);
		}
	}
	
	public boolean isDownload()
	{
		String isDownloadString = Config.getValue(Config.CONFIG_KEY_FUNCTION_DOWNLOAD, Config.CONFIG_VALUE_YES);
		if (Config.CONFIG_VALUE_YES.equals(isDownloadString))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
