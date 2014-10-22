package com.fgc.autocall.ui;

import com.fgc.autocall.R;
import com.fgc.autocall.R.layout;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ActivityStartup extends BaseActivity {

	private static final String LOG_TAG = "ActivityStartup";
	
	static final private int HANDLER_WHAT_ID_INIT_APP = 1;
	static final private int HANDLER_WHAT_ID_GOTO_MAIN_VIEW = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.a_startup);
		
		ImageView coverView = (ImageView)findViewById(R.id.cover);
		coverView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				mBaseHandler.removeMessages(HANDLER_WHAT_ID_GOTO_MAIN_VIEW);
				Log.i(LOG_TAG, "directly goto next view ");
				gotoMainView();
			}
			
		});
		mApp.initApp();
	}
	
	protected boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HANDLER_WHAT_ID_INIT_APP: {
			mApp.initApp();
			mBaseHandler.sendEmptyMessageDelayed(HANDLER_WHAT_ID_GOTO_MAIN_VIEW, 2000);
			break;
		}
		case HANDLER_WHAT_ID_GOTO_MAIN_VIEW: {
			gotoMainView();
			break;
		}
		default:
			break;
		}
		return true;
	}
	

	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(LOG_TAG, "onNewIntent");
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Log.i(LOG_TAG, "onStart");
		mBaseHandler.sendEmptyMessageDelayed(HANDLER_WHAT_ID_INIT_APP, 200);
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.i(LOG_TAG, "onRestart");
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(LOG_TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(LOG_TAG, "onPause");
		
	}

	@Override
	protected void onStop() {
		Log.i(LOG_TAG, "onStop");

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i(LOG_TAG, "onDestroy");
		
		super.onDestroy();
	}

	
	private void gotoMainView(){
		Log.i(LOG_TAG, "gotoMainView");
		Intent intent = new Intent(this, ActivityMain.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected boolean ifProcessBackKeyPress(){
		Log.i(LOG_TAG, "click back key !");
		return true;
	}

}
