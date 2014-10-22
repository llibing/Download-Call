package com.fgc.autocall.ui;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public class BaseHandler extends Handler {
	private WeakReference<BaseActivity> mWeakAc = null;
	
	public BaseHandler(BaseActivity ac) {
		mWeakAc = new WeakReference<BaseActivity>(ac);
	}

	@Override
	public void handleMessage(Message msg) {
		BaseActivity ac = mWeakAc.get();
		if (null != ac) {
			if(true == ac.handleMessage(msg))
				return;
		}
		
		super.dispatchMessage(msg);
	}

}
