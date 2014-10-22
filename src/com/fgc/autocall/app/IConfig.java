package com.fgc.autocall.app;

public interface IConfig {

	public String getValue(String group, String name, String defaultValue);

	public void setValue(String group, String name, String value);

	public void unsetValue(String group, String name);

	public void removeGroup(String name);
}