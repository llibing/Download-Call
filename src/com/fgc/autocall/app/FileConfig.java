package com.fgc.autocall.app;

import android.content.Context;
import android.content.SharedPreferences;

public class FileConfig implements IConfig {

	public static IConfig Instance() {
		return ourInstance;
	}

	private static IConfig ourInstance = null;

	public FileConfig(Context context) {
		mContext = context;
		ourInstance = this;
	}

	private Context mContext;

	@Override
	public String getValue(String group, String name, String defaultValue) {
		SharedPreferences sp = mContext.getSharedPreferences(group,Context.MODE_WORLD_READABLE);
		return sp.getString(name, defaultValue);
	}

	@Override
	public void removeGroup(String name) {
		SharedPreferences sp = mContext.getSharedPreferences(name,Context.MODE_WORLD_READABLE);
		sp.edit().clear().commit();
	}

	@Override
	public void setValue(String group, String name, String value) {
		SharedPreferences sp = mContext.getSharedPreferences(group,Context.MODE_WORLD_READABLE);
		sp.edit().putString(name, value).commit();
	}

	@Override
	public void unsetValue(String group, String name) {
		SharedPreferences sp = mContext.getSharedPreferences(group,Context.MODE_WORLD_READABLE);
		sp.edit().remove(name).commit();
	}

}
