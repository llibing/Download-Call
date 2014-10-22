package com.fgc.autocall.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fgc.autocall.R;
import com.fgc.autocall.app.component.PhoneContactManager;
import com.fgc.autocall.app.component.StorageManagerFgc;
import com.fgc.autocall.constant.Constans;
import com.fgc.autocall.ui.component.ButtonTwoState;

public class ActivityExportRecord extends BaseActivity{
	private static final String LOG_TAG = "ActivityExportRecord";
	
	private RelativeLayout mLayoutTitleBar;
	private TextView mTextExportNotice;
	private Button mBtnExport;
	
	private PhoneContactManager mPhoneContactManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.a_export_record);
		
		mPhoneContactManager = new PhoneContactManager(this);
		
		initView();
	}

	private void initView()
	{
		mLayoutTitleBar = (RelativeLayout)findViewById(R.id.title_bar);
		Button btnLeft = (Button)mLayoutTitleBar.findViewById(R.id.title_btn_left);
		btnLeft.setOnClickListener(mOnClickListener);
		((ButtonTwoState)mLayoutTitleBar.findViewById(R.id.title_btn_right)).setVisibility(View.GONE);
		
		mTextExportNotice = (TextView)findViewById(R.id.text_export_notice);
		mBtnExport = (Button)findViewById(R.id.btn_export);
		mBtnExport.setOnClickListener(mOnClickListener);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i(LOG_TAG, "onClick");
			
			switch (v.getId())
			{
			case R.id.title_btn_left:
				Log.i(LOG_TAG, "click title_btn_left");
				finish();
				break;
			case R.id.btn_export:
				Log.i(LOG_TAG, "click btn_export");
				String exporting = ActivityExportRecord.this.getResources().getString(R.string.exporting_notice);
				if (exporting.equals(mTextExportNotice.getText().toString()))
				{
					Log.w(LOG_TAG, "exporting !");
					return;
				}
						
				mTextExportNotice.setText(exporting);
				String exportPath = new StorageManagerFgc(ActivityExportRecord.this).getExternalStoragePath();
				exportPath = exportPath + "/" + Constans.FileName.EXPORT_CONTACT_FILE_NAME;
				Log.i(LOG_TAG, "export path: " + exportPath);
				new WorkThread(exportPath).start();
				break;
			}
		}
	};
	
	
	private final int HANDLE_WHAT_EXPORT_SUCCESS = 0;
	@Override
	protected boolean handleMessage(android.os.Message msg)
	{
		mTextExportNotice.setText(ActivityExportRecord.this.getResources().getString(R.string.exported_notice));
		return true;
	}
	
	private class WorkThread extends Thread
	{
		private String mFileFullPath;
		public WorkThread(String path)
		{
			mFileFullPath = path;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mPhoneContactManager.exportContactsToFile(mFileFullPath);
			mBaseHandler.sendEmptyMessage(HANDLE_WHAT_EXPORT_SUCCESS);
		}
	}
	
}
