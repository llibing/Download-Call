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
 * ������ʾ������Ϣ
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
				new AlertDialog.Builder(context).setTitle("��ʾ")
				.setMessage("��⵽�ϴ������жϣ��������أ�")
				.setNegativeButton("ȡ��", null)
				.setPositiveButton("��������", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.continueDownload(downloadInfo);
//						showDownloadingDialog(downloader,context);
						showDownloadingDialog(downloader,context,null);
					}
				})
				.create().show();
		}else{
			new AlertDialog.Builder(context).setTitle("��ʾ")
				.setMessage("��ʼ������")
				.setNegativeButton("ȡ��", null)
				.setPositiveButton("��ʼ����", new DialogInterface.OnClickListener() {
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
	 * ��ʾ������Ϣ
	 * 
	 */
	public static void isNetworkConnectedShowDialog(final Context context){
		new AlertDialog.Builder(context).setTitle("               ����������ʾ")
		.setMessage("���粻���ã������Ƿ���������?")
		.setNegativeButton("ȡ��", null)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");  
				context.startActivity(intent);  
			}
		})
		.create().show();
		
	}
	
	
	
	
	public static void startDownloadAndShowDialog(final DownloadInfo downloadInfo,final Context context,final CompleteListener completeListener,final String dialogMessage){
		if(downloadInfo.getDlFile().exists()){
				new AlertDialog.Builder(context).setTitle("��ʾ")
				.setMessage("��⵽�ϴ������жϣ��������أ�")
				.setNegativeButton("ȡ��", null)
				.setNeutralButton("��������", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.continueDownload(downloadInfo);
//						showDownloadingDialog(downloader,context);
						showDownloadingDialog(downloader,context,completeListener);
					}
				})
				.setPositiveButton("��������", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final Downloader downloader=Downloader.restartDownload(downloadInfo);
						showDownloadingDialog(downloader,context,completeListener);
//						showDownloadingDialog(downloader,context);
					}
				})
				.create().show();
		}else{
			new AlertDialog.Builder(context).setTitle("��ʾ")
				.setMessage(dialogMessage)
				.setNegativeButton("ȡ��", null)
				.setPositiveButton("��ʼ����", new DialogInterface.OnClickListener() {
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
		pd.setTitle("��������"+downloadInfo.getFinalFile().getName());
		
		pd.setButton(Dialog.BUTTON_NEGATIVE, "ֹͣ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downloader.stop();
			}
		});
//		pd.setButton(Dialog.BUTTON_POSITIVE, "��̨����", new DialogInterface.OnClickListener() {
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
				pd.setMessage("��������...");
			}
			public void onDownloadStart(Downloader downloader) {
				pd.setTitle("��������("+MyUtil.SizeToString(downloader.getDownloadInfo().getFileSize())+")"
						+downloadInfo.getFinalFile().getName());
				pd.setMessage("��ʼ����...");
			}
			public void onDownloadFinish(Downloader downloader) {
				pd.setMessage("�������...");
				Toast.makeText(context, downloadInfo.getFinalFile().getName()+"�������",Toast.LENGTH_SHORT).show();
				pd.dismiss();
				
				if(completeListener!=null) completeListener.onComplete(downloadInfo.getFinalFile());
			}
			public void onDownloadFail(Downloader downloader, String errorMsg) {
				pd.dismiss();
//				Toast.makeText(context, downloadInfo.getFinalFile().getName()+"����ʧ��:\n"+errorMsg,Toast.LENGTH_SHORT).show();
				Toast.makeText(context, downloadInfo.getFinalFile().getName()+"����ʧ��,������������",Toast.LENGTH_LONG).show();
				
			}
			public void onDownloadPause(Downloader downloader) {
				pd.setMessage("(��ͣ��)"
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
		 * ������ɺ󣬶������ļ����д���,��ɺ����Ҫ����pd.dismiss();
		 * @param pd ������ʾ��progressDialog
		 */
		public void onComplete(final File downloadFile);
	}
}
