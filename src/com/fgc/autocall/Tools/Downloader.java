package com.fgc.autocall.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Downloader {  
	private final static int NoticeTimeDiv=1000;
    private Handler handler;  
    private static final int Msg_Connecting=-1;
    private static final int Msg_StartDownload=0;
//    private static final int Msg_ContinueDownload=1;
    private static final int Msg_Downloading=2;
    private static final int Msg_FinishDownload=3;
    private static final int Msg_DownloadError=4;
    private static final int Msg_PauseDownload=5;
    private static final int Msg_ContinueDownload=6;
    private static final int Msg_StopDownload=7;
    private static final String Msg_Date_Speed="speed";
    private static final String Msg_Date_FailMsg="error";
    private DownloadInfo info;
    public DownloadInfo getDownloadInfo(){
    	return info;
    }
    boolean isPause;
    boolean isStop;
    public boolean isStop() {
		return isStop;
	}
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
	private Thread downloadThread;
    private String getDownloadUrl(){
    	return info.downloadUrl;
    }
    public int getDownloadPercent(){
    	try {
			return (int) (info.getDone()*100/info.getFileSize());
		} catch (Exception e) {
		}
    	return 0;
    }
    //检测网络连接状态
    public static boolean isNetworkConnected(Context context) { 
    	if (context != null) { 
    	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
    	.getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
    	if (mNetworkInfo != null) { 
    	return mNetworkInfo.isAvailable(); 
    	} 
    	} 
    	return false; 
    	} 
    
    private static ArrayList<Downloader> downloadingList=new ArrayList<Downloader>();
    
    private Downloader(DownloadInfo bookinfo) {
    	handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case Msg_Connecting:
			    	try {
						if (info.getDlFile()!=null&&!info.getDlFile().exists())
							info.getDlFile().createNewFile();
					} catch (Exception e) {
						e.printStackTrace();
						downloadFail(e.getMessage());
						return;
					}
			    	info.saveInfo();
			    	for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloadConnecting(Downloader.this);
			    	}
					break;
				case Msg_StartDownload:
					for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloadStart(Downloader.this);
			    	}
					break;
				case Msg_Downloading:
					long speed=msg.getData().getLong(Msg_Date_Speed);
					for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloading(Downloader.this,speed);
			    	}
					break;
				case Msg_FinishDownload:
					if(info.getDlFile()!=null) info.getDlFile().renameTo(info.getFinalFile());
					for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloadFinish(Downloader.this);
			    	}
					break;
				case Msg_DownloadError:
					String error=msg.getData().getString(Msg_Date_FailMsg);
					if(info.getDlFile()!=null) info.getDlFile().delete();
					for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloadFail(Downloader.this,error);
			    	}
					break;
				case Msg_PauseDownload:
					for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloadPause(Downloader.this);
			    	}
					break;
				case Msg_ContinueDownload:
					for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloading(Downloader.this,0);
			    	}
					break;
				case Msg_StopDownload:
					if(info.getDlFile()!=null) info.getDlFile().delete();
					for(DownloadListener downloadListener : downloadListeners){
			    		downloadListener.onDownloadStop(Downloader.this);
			    	}
					break;
				}
			}
    	};
		
		this.info=bookinfo;
        downloadingList.add(this);
    }
    
    static public boolean isUrlDownloading(String url){
    	Downloader downloader=getDownloader(url);
    	if(downloader!=null&&!downloader.isPause()) return true;
    	return false;
    }

	static public Downloader getDownloader(DownloadInfo info){
    	for(Downloader downloader:downloadingList){
    		if(downloader.info.downloadUrl.equals(info.downloadUrl)) return downloader;
    	}
    	return null;
    }
	static public Downloader getDownloader(String url){
    	for(Downloader downloader:downloadingList){
    		if(downloader.getDownloadUrl().equals(url)) return downloader;
    	}
    	return null;
    }
    static public Downloader restartDownload(DownloadInfo info){
    	Downloader downloader=getDownloader(info.downloadUrl);
    	if(downloader!=null){
    		downloader.stop();
    	}
    	downloader=new Downloader(info);
    	downloader.restartDownload();
    	
    	Log.d("fax", "startdownload:"+info.downloadUrl);
    	return downloader;
    }
    static public Downloader continueDownload(DownloadInfo info){
    	Downloader downloader=getDownloader(info.downloadUrl);
    	if(downloader!=null){
    		downloader.resume();
    		return downloader;
    	}
    	downloader=new Downloader(info);
    	downloader.startContinueDownload();
    	Log.d("fax", "continueDownload:"+info.downloadUrl);
    	return downloader;
    }
    private void restartDownload(){
    	downloadThread=new Thread() {
			public void run() {
				try {
					restartDownloadMain();
				} catch (Exception e) {
					e.printStackTrace();
					downloadFail(e.getMessage());
				}
			}
		};
		downloadThread.start();
    }
    private void startContinueDownload(){
    	downloadThread=new Thread() {
			public void run() {
				try {
					continueDownloadMain();
				} catch (Exception e) {
					e.printStackTrace();
					downloadFail(e.getMessage());
				}
			}
		};
		downloadThread.start();
    }
    private void restartDownloadMain() throws Exception {  
        handler.obtainMessage(Msg_Connecting).sendToTarget();
        
        URL url = new URL(getDownloadUrl());  
        
        if(info.getDlFile()!=null) info.getDlFile().delete();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        downloadMain(conn);
    }
    private void continueDownloadMain() throws Exception{
        handler.obtainMessage(Msg_Connecting).sendToTarget();
        if(info.filesize<0) info.initFleSize();//初始化文件大小
        
    	URL url = new URL(getDownloadUrl());  
    	
        if(info!=null){
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(info.getDone()>0){
            	Log.d("fax", "continueDownload:"+"bytes=" + info.getDone() + "-" + info.getFileSize());
                conn.setRequestProperty("Range", "bytes=" + (info.getDone())+ "-" + info.getFileSize());
            }
            downloadMain(conn);
        }
        
    }
    private void downloadMain(HttpURLConnection conn) throws Exception{
    	//设置超时时间   
        conn.setConnectTimeout(3000);
        int responseCode=conn.getResponseCode();
        if (responseCode >= 200&&responseCode<300) {
            
            handler.obtainMessage(Msg_StartDownload).sendToTarget();
            InputStream in = null;
            FileOutputStream fos = null;
            try {
//            	RandomAccessFile raf=new RandomAccessFile(getDownloadToFile(), "rws");
//            	raf.seek(info.getDone());
            	fos=new FileOutputStream(info.getDlFile(), true);
                //开始读写数据   
                in = conn.getInputStream();  
                byte[] buf = new byte[1024 * 4];
                long lastFillDataTime=0;
                int downloadedPart=0;
                int len;  
                while ((len = in.read(buf)) != -1&&!isStop) {
                	if(Thread.interrupted()) break;
                	
                    fos.write(buf, 0, len);
                    downloadedPart+=len;
                    long timeuse=System.currentTimeMillis()-lastFillDataTime;
                    if(timeuse>NoticeTimeDiv){
                    	long speed=downloadedPart*1000/timeuse;
                        //新线程中用Handler发送消息，主线程接收消息   
                    	Message m=handler.obtainMessage(Msg_Downloading);
                    	m.getData().putLong(Msg_Date_Speed, speed);
                    	m.sendToTarget();
                    	
                    	downloadedPart=0;
                    	lastFillDataTime=System.currentTimeMillis();
                    }
                    

                    if (isPause()) {
                        //使用线程锁锁定该线程   
                        synchronized (handler) {  
                            try {  
                            	handler.wait();
                            } catch (InterruptedException e) {  
                                break;
                            }  
                        }  
                    }
                    
                }
                in.close();  
                fos.close();  
                downloadingList.remove(this);
                if(isStop){
                	handler.obtainMessage(Msg_StopDownload).sendToTarget();
                }else{
                    handler.obtainMessage(Msg_FinishDownload).sendToTarget();
                }
                
            } catch (Exception e) {  
                e.printStackTrace();
                downloadFail(e.getMessage());

                try {
					if (in != null)
						in.close();
				} catch (Exception e2) {
				}
				try {
					if (fos != null)
						fos.close();
				} catch (Exception e2) {
				}  
            }   
        } else {
        	downloadFail(responseCode+"");
            Log.e("fax", "download fail,return:"+responseCode+",path: " + getDownloadUrl());  
        }  
    }
    private void downloadFail(String errorMsg){
    	downloadingList.remove(this);
    	Message m=handler.obtainMessage(Msg_DownloadError);
    	m.getData().putString(Msg_Date_FailMsg, errorMsg);
    	m.sendToTarget();
    }

    private boolean isPause() {
		return isPause;
	}
    public void pauseOrResume(){
    	if(isPause){
    		resume();
    	}else{
    		pause();
    	}
    }
    //停止下载   
    public void stop() {
    	isStop=true;
    	if(downloadThread!=null) downloadThread.interrupt();
    }  
    //暂停下载   
    public void pause() {  
    	isPause = true;  
        handler.obtainMessage(Msg_PauseDownload).sendToTarget();
    }  
    //继续下载   
    public void resume() {  
    	isPause = false;  
        handler.obtainMessage(Msg_ContinueDownload).sendToTarget();
        //恢复所有线程   
        synchronized (handler) {  
        	handler.notifyAll();  
        }
    }
    public boolean isDownloadFinish(){
    	return 
    			info.getFinalFile().exists()
//    			&&!info.getDlFile().exists()
    			;
    }
	private ArrayList<DownloadListener> downloadListeners=new ArrayList<Downloader.DownloadListener>();
	public void addDownloadListener(DownloadListener downloadListener) {
		downloadListeners.add(downloadListener);
	}
	public void removeDownloadListener(DownloadListener downloadListener){
		downloadListeners.remove(downloadListener);
	}
	public interface DownloadListener{
		public void onDownloadConnecting(Downloader downloader);
		public void onDownloadStart(Downloader downloader);
		public void onDownloading(Downloader downloader,long speed);
		public void onDownloadFinish(Downloader downloader);
		public void onDownloadFail(Downloader downloader,String errorMsg);
		public void onDownloadPause(Downloader downloader);
		public void onDownloadStop(Downloader downloader);
	}
	
}  