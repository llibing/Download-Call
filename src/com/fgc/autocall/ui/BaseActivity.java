package com.fgc.autocall.ui;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fgc.autocall.R;
import com.fgc.autocall.Tools.Tools;
import com.fgc.autocall.app.ClientApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

public abstract class BaseActivity extends Activity{
	static private  String LOG_TAG = "BaseActivity";
    
	protected ClientApp mApp;
	protected Handler mBaseHandler = null;
    private boolean mFinishAppDoubleCheckFlag;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG, "onCreate");
		
		mBaseHandler = new BaseHandler(this);
		mApp = (ClientApp)this.getApplication();
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(LOG_TAG, "onNewIntent");
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
		if (null != mBaseHandler) {
			mBaseHandler.removeCallbacksAndMessages(null);
		}
		mBaseHandler = null;
		super.onDestroy();
	}
	
	protected boolean ifProcessBackKeyPress(){
		return false;
	}
	
	protected boolean ifFinishAppByBackKeyPress(){
		return false;
	}
	
	@Override
	 public boolean dispatchKeyEvent(KeyEvent event) { 
		
		if ((event.getAction() == KeyEvent.ACTION_DOWN &&  event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
			Log.d(LOG_TAG, "dispatchKeyEvent KEYCODE_BACK ...  ");
			if(true ==  ifProcessBackKeyPress())
				return true;
			
			//finish activity or application
			if(ifFinishAppByBackKeyPress() == true){
				if(mFinishAppDoubleCheckFlag == true){
					mApp.finishApp();
					finish();
					return true;
				}else{
					mFinishAppDoubleCheckFlag = true;
					mBaseHandler.postDelayed(new Runnable(){//remove the double check flag by timeout.
						@Override
						public void run() {
							mFinishAppDoubleCheckFlag = false;
						}
						
					}, 1500);
					Tools.ToastShow(this, R.string.double_click_quit_app, false);
					return true;
				}

			}else{
				finish();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);  
	 }
	
	protected boolean handleMessage(Message msg) {
		return true;
	}
	
}
