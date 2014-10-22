package com.fgc.autocall.Tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;






import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MyUtil {
	public static String getNowTimeFormated(){
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis()));
	}
	static private Context context;
	static public Context getContext() {
		return context;
	}
	static public void setContext(Context context) {
		MyUtil.context = context;
	}
	static public final String DirName="WeiHanStudy";
	static private File getAppPath(Context context){
		
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (!sdCardExist) {
			Toast.makeText(context, "储存卡不存在", Toast.LENGTH_SHORT).show();
		}
			
		File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		final File path = new File(sdDir.toString() + "/" + DirName);
		if (!path.exists())
			path.mkdirs();
		return path;
	}
	static public File getAppPath(){
		return getAppPath(context);
	}
	
	static public File getDictionaryDatbaseDir(){
		return new File(getAppPath(), "Dictionary_DataBase_File");
	}
	
	static public File getCatalogImageDir(){
		return new File(getAppPath(), "Catalog_Image");
	}
	
	static public File getBookDir(){
		return new File(getAppPath(), "BOOKS");
	}
	static public File getBookImgDir(){
		return new File(getBookDir(), "BookImgs");
	}
	public static TreeMap<String, String> allWeiWordMap;
	static{
		initAllWeiWords();
	}
	static public void initAllWeiWords(){
		allWeiWordMap=new TreeMap<String, String>();
		allWeiWordMap.put("a", "ھ");
		allWeiWordMap.put("b", "ب");
		allWeiWordMap.put("c", "غ");
		allWeiWordMap.put("d", "د");
		allWeiWordMap.put("e", "ې");
		allWeiWordMap.put("f", "ا");
		allWeiWordMap.put("g", "ە");
		allWeiWordMap.put("h", "ى");
		allWeiWordMap.put("i", "ڭ");
		allWeiWordMap.put("j", "ق");
		allWeiWordMap.put("k", "ك");
		allWeiWordMap.put("l", "ل");
		allWeiWordMap.put("m", "م");
		allWeiWordMap.put("n", "ن");
		allWeiWordMap.put("o", "و");
		allWeiWordMap.put("p", "پ");
		allWeiWordMap.put("q", "چ");
		allWeiWordMap.put("r", "ر");
		allWeiWordMap.put("s", "س");
		allWeiWordMap.put("t", "ت");
		allWeiWordMap.put("u", "ۇ");
		allWeiWordMap.put("v", "ۈ");
		allWeiWordMap.put("w", "ۋ");
		allWeiWordMap.put("x", "ش");
		allWeiWordMap.put("y", "ي");
		allWeiWordMap.put("z", "ز");
		allWeiWordMap.put(",", "ئ");
		allWeiWordMap.put("q2", "1");
		allWeiWordMap.put("w2", "2");
		allWeiWordMap.put("e2", "3");
		allWeiWordMap.put("r2", "4");
		allWeiWordMap.put("t2", "5");
		allWeiWordMap.put("y2", "6");
		allWeiWordMap.put("u2", "7");
		allWeiWordMap.put("i2", "8");
		allWeiWordMap.put("o2", "9");
		allWeiWordMap.put("p2", "0");
		allWeiWordMap.put("d2", "ژ");
		allWeiWordMap.put("f2", "ف");
		allWeiWordMap.put("g2", "گ");
		allWeiWordMap.put("h2", "خ");
		allWeiWordMap.put("j2", "ج");
		allWeiWordMap.put("k2", "ۆ");
		allWeiWordMap.put("l2", "لا");
	}

	public static void unzip(InputStream zipIs, String outputDirectory) {
        try {
            ZipInputStream in = new ZipInputStream(zipIs);
            // 获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
            // 当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
            // 输入流读取完成；
            ZipEntry entry = in.getNextEntry();
            BufferedInputStream bis=new BufferedInputStream(in);
            while (entry != null) {

                // 创建以zip包文件名为目录名的根目录
                File file = new File(outputDirectory);
                file.mkdir();
                if (entry.isDirectory()) {
                    String name = entry.getName();
                    name = name.substring(0, name.length() - 1);
              
                    file = new File(outputDirectory + File.separator + name);
                    file.mkdir();
               
                } else {
                	
                	
                	
                    file = new File(outputDirectory + File.separator + entry.getName());
//                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    BufferedOutputStream bos=new BufferedOutputStream(out);
                    byte[] b = new byte[128*1024];
                    int length=0;
                    while ((length=bis.read(b)) != -1) {
                    	bos.write(b,0,length);
                    }
                    bos.close();
                    //
                }
                entry = in.getNextEntry();
            }
            in.close();
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
        	e.printStackTrace();
        }
    } 
	//如果是2.x设备，则转化维文
//	static public String convertStr(String in){
//		if(android.os.Build.VERSION.SDK_INT<11) return UyghurConvert.convert(in);
//		return in;
//	}
	/**弹出系统输入法*/
	static public void showSystemInputBord(EditText et){
		et.requestFocusFromTouch();
		InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(et, 0);
	}
	public static void writeStrToFile(String s,File file){
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new FileWriter(file));
			bw.write(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(bw!=null) bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String readStrFromFile(File file){
		try {
			return readStrFromInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String readStrFromInputStream(InputStream is){
		BufferedReader br=null;
		StringBuilder sb=new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(is));
			char[] chars=new char[1024];
			int length;
			while((length=br.read(chars))!=-1){
				sb.append(chars, 0, length);
			}
		} catch (Exception e) {
		}
		try {
			if (br != null)
				br.close();
		} catch (Exception e) {
		}
		return sb.toString();
	}

	/*static public void openWordView(Context context,Word word){
		openWordView(context,word.getWord(),word.getContent(),word.getId());
    }
	static public void openWordView(Context context,String title,String content,String id){
    	Intent intent=new Intent();
		intent.setClass(context, WordActivity.class);
		intent.putExtra(WordActivity.IntentExtraWordTitle, title);
		intent.putExtra(WordActivity.IntentExtraWordContent, content);
		intent.putExtra(WordActivity.IntentExtraWordId, id);
		context.startActivity(intent);
    }
	static public void openBook(Context context,File file){
		Uri uri=Uri.fromFile(file);
		final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClass(context, LoadPdfDialog.class);
        context.startActivity(intent);
	}*/
	private static String[] sizeunits={"B","Kb","Mb","Gb","Tb"};
	public static String SizeToString(float size){
		if(size<0) return "未知";
    	int i=0;
    	for (i=0;i<5;i++){
    		if(size<1024) break;
    		size=size/1024;
    	}
    	return String.format("%.1f", size)+sizeunits[i];
	}
	public File renameFileIfExist(File in){
		File tempFile=new File(in.getPath());
      int renameTime=0;
      String baseFileName=tempFile.getName();
      while(tempFile.exists()){
      	renameTime++;
      	int lastIndexOfPoint=baseFileName.lastIndexOf(".");
      	if(lastIndexOfPoint<0){
      		tempFile=new File(tempFile.getParentFile(), baseFileName+"-"+renameTime);
      	}else tempFile=new File(tempFile.getParentFile(), baseFileName.substring(0, lastIndexOfPoint)+"-"+renameTime+baseFileName.substring(lastIndexOfPoint));
      }
      return tempFile;
	}
	public static int getAppVersion(Context context){
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
		}
		return 999999999;
	}
	public static String getFileNameInDownloadUrl(String path){
		try {
			URI uri=new URI(path);
			return new File(uri.getPath()).getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//	public static String getPinyinFromChar(char c){
//		try {
//			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//			format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
//			format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
//			return PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return c+"";
//	}
	
	
	public static void copyFile(InputStream is , File newFilePath) throws IOException{
		BufferedInputStream bis=new BufferedInputStream(is);
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(newFilePath));
		byte[] buffer=new byte[1024];
		int length=0;
		while((length=bis.read(buffer))!=-1){
			bos.write(buffer,0,length);
		}
		bos.close();
		bis.close();
	}
}
