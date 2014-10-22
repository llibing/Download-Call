package com.fgc.autocall.Tools;

import java.io.File;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

/**
 * 辅助显示下载信息
 * @author linfaxin
 */
public class DownloaderShower {
	public static void startDownloadAndShowDialog(final DownloadInfo downloadInfo,final Context context){
		if(downloadInfo.getDlFile().exists()){
			if(Downloader.getDownloader(downloadInfo)!=null){
				final Downloader downloader=Downloader.continueDownload(downloadInfo);
				showDownloadingDialog(downloader,context,null);
//				showDownloadingDialog(downloader,context);
			}else 
				new AlertDialog.Builder(context).setTitle("提示")
				.setMessage("检测到上次下载中断，继续下载？")
				.setNegativeButton("取消", null)
				.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.continueDownload(downloadInfo);
//						showDownloadingDialog(downloader,context);
						showDownloadingDialog(downloader,context,null);
					}
				})
				.create().show();
		}else{
			new AlertDialog.Builder(context).setTitle("提示")
				.setMessage("开始下载吗？")
				.setNegativeButton("取消", null)
				.setPositiveButton("开始下载", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.continueDownload(downloadInfo);
						showDownloadingDialog(downloader,context,null);
//						showDownloadingDialog(downloader,context);
					}
				})
				.create().show();
			
		}
	}
	
	/**
	 * 显示网络信息
	 * 
	 */
	public static void isNetworkConnectedShowDialog(final Context context){
		new AlertDialog.Builder(context).setTitle("               数据连接提示")
		.setMessage("网络不可用，请检测是否连接网络?")
		.setNegativeButton("取消", null)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");  
				context.startActivity(intent);  
			}
		})
		.create().show();
		
	}
	
	
	
	
	public static void startDownloadAndShowDialog(final DownloadInfo downloadInfo,final Context context,final CompleteListener completeListener,final String dialogMessage){
		if(downloadInfo.getDlFile().exists()){
				new AlertDialog.Builder(context).setTitle("提示")
				.setMessage("检测到上次下载中断，继续下载？")
				.setNegativeButton("取消", null)
				.setNeutralButton("继续下载", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.continueDownload(downloadInfo);
//						showDownloadingDialog(downloader,context);
						showDownloadingDialog(downloader,context,completeListener);
					}
				})
				.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.restartDownload(downloadInfo);
						showDownloadingDialog(downloader,context,completeListener);
//						showDownloadingDialog(downloader,context);
					}
				})
				.create().show();
		}else{
			new AlertDialog.Builder(context).setTitle("提示")
				.setMessage(dialogMessage)
				.setNegativeButton("取消", null)
				.setPositiveButton("开始下载", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.continueDownload(downloadInfo);
//						showDownloadingDialog(downloader,context);
						showDownloadingDialog(downloader,context,completeListener);
					}
				})
				.create().show();
			
		}
	}
	public static void showDownloadingDialog(final Downloader downloader,final Context context,final CompleteListener completeListener){
//		public static void showDownloadingDialog(final Downloader downloader,final Context context){
		final DownloadInfo downloadInfo=downloader.getDownloadInfo();
		
		final ProgressDialog pd=new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		pd.setMax(100);
		pd.setMessage("");
		pd.setTitle("正在下载"+downloadInfo.getFinalFile().getName());
		
		pd.setButton(Dialog.BUTTON_NEGATIVE, "停止", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downloader.stop();
			}
		});
//		pd.setButton(Dialog.BUTTON_POSITIVE, "后台下载", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				pd.dismiss();
//			}
//		});
		pd.show();
		
		final Downloader.DownloadListener downloadListener=new Downloader.DownloadListener() {
			@Override
			public void onDownloading(Downloader downloader,long speed) {
					pd.setProgress(downloader.getDownloadPercent());
					pd.setMessage("("+MyUtil.SizeToString(speed)+"/s)"
					+MyUtil.SizeToString(downloader.getDownloadInfo().getDone())+"/"
							+MyUtil.SizeToString(downloader.getDownloadInfo().getFileSize()));
				
			}
			
			public void onDownloadConnecting(Downloader downloader) {
				pd.setMessage("正在连接...");
			}
			public void onDownloadStart(Downloader downloader) {
				pd.setTitle("正在下载("+MyUtil.SizeToString(downloader.getDownloadInfo().getFileSize())+")"
						+downloadInfo.getFinalFile().getName());
				pd.setMessage("开始下载...");
			}
			public void onDownloadFinish(Downloader downloader) {
				pd.setMessage("下载完成...");
				Toast.makeText(context, downloadInfo.getFinalFile().getName()+"下载完成",Toast.LENGTH_SHORT).show();
				pd.dismiss();
				
				if(completeListener!=null) completeListener.onComplete(downloadInfo.getFinalFile());
			}
			public void onDownloadFail(Downloader downloader, String errorMsg) {
				pd.dismiss();
//				Toast.makeText(context, downloadInfo.getFinalFile().getName()+"下载失败:\n"+errorMsg,Toast.LENGTH_SHORT).show();
				Toast.makeText(context, downloadInfo.getFinalFile().getName()+"下载失败,请检测网络连接",Toast.LENGTH_LONG).show();
				
			}
			public void onDownloadPause(Downloader downloader) {
				pd.setMessage("(暂停中)"
						+MyUtil.SizeToString(downloader.getDownloadInfo().getDone())+"/"
								+MyUtil.SizeToString(downloader.getDownloadInfo().getFileSize()));
			}
			public void onDownloadStop(Downloader downloader) {
				pd.dismiss();
			}
		};
		downloader.addDownloadListener(downloadListener);

		pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				downloader.removeDownloadListener(downloadListener);
			}
		});
	}
	
	public interface CompleteListener{
		/**
		 * 下载完成后，对下载文件进行处理,完成后最后要调用pd.dismiss();
		 * @param pd 正在显示的progressDialog
		 */
		public void onComplete(final File downloadFile);
	}
}
