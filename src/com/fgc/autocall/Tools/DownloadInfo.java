package com.fgc.autocall.Tools;

import java.io.File;
import java.net.URL;

public class DownloadInfo{
	
	String downloadUrl;
	File finalFile;
	File dlFile;
	long filesize;
	
	public DownloadInfo(File finalFile,String downloadUrl){
		this(finalFile, downloadUrl, -1);
	}
	
	public DownloadInfo(File finalFile,String downloadUrl,long filesize){
		this.downloadUrl=downloadUrl;
		this.finalFile=finalFile;
		dlFile=new File(finalFile.getPath()+".dl");
		this.filesize=filesize;
	}
	
	void initFleSize() throws Exception{
		filesize=new URL(downloadUrl).openConnection().getContentLength();
	}
	
	public File getDlFile() {
		return dlFile;
	}

	public long getFileSize() {
		return filesize;
	}

	public long getDone() {
		if(getDlFile()!=null){
			return getDlFile().length();
		}
		return 0;
	}

	public File getFinalFile() {
		return finalFile;
	}

	public void saveInfo() {
		// TODO Auto-generated method stub
		
	}
	
}
