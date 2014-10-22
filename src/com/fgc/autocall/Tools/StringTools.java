/**
* @Title: 		com.targetv.tools.StringUtils.java 
* @Description: the String utility. 
* @author 		Ken Lee
* @date 		2012-11-29 涓婂崍11:36:25
*/
package com.fgc.autocall.Tools;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class StringTools {
	private StringTools(){};
	
	static public String getFileNameFromFileUrl(String url){
		if(url == null || -1 == url.lastIndexOf("."))
			return null;
		
		int pos = url.lastIndexOf("/");
		if(pos == -1)
			return null;
		return new String(url.substring(pos+1));
	}
	static public String getFileNamePostFixFromFileUrl(String url){
		if(url == null || -1 == url.lastIndexOf("."))
			return null;

		int pos = url.lastIndexOf("/");
		if(pos == -1)
			return null;
		
		String fileName = url.substring(pos+1);
		pos = fileName.lastIndexOf(".");
		if(pos == -1)
			return null;
		return new String(fileName.substring(pos+1));
	}
	
	static public boolean IsEmpty(String str){
		if(str == null || str.length() <= 0)
			return true;
		
		return false;
	}
	
	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	/**
	 * 座机号码验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) { 
		
		if (str == null || str.length() == 0)
		{
			return false;
		}
		
		if (str.startsWith("0"))
		{
			return true;
		}
		
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
	
	public static boolean isPureNumber(String str)
	{
		if (IsEmpty(str))
		{
			return false;
		}
		
		return str.matches("^[0-9]*$");
	}
	
	public static boolean isTimeFormat(String str)
	{
		if (IsEmpty(str))
		{
			return false;
		}
		
		return str.matches("^[0-9][0-9]:[0-9][0-9]$");
	}
}
