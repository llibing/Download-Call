package com.fgc.autocall.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fgc.autocall.R;
import com.fgc.autocall.ui.component.ButtonTwoState;

public class ActivitySimCard extends BaseActivity{
	private static final String LOG_TAG = "ActivitySimCard";
	
	private RelativeLayout mLayoutTitleBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.a_sim_card);
		
		initView();
	}

	private void initView()
	{
		mLayoutTitleBar = (RelativeLayout)findViewById(R.id.title_bar);
		Button btnLeft = (Button)mLayoutTitleBar.findViewById(R.id.title_btn_left);
		btnLeft.setOnClickListener(mOnClickListener);
		((ButtonTwoState)mLayoutTitleBar.findViewById(R.id.title_btn_right)).setVisibility(View.GONE);
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
			}
		}
	};
}
