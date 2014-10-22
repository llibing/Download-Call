package com.fgc.autocall.ui.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/*
 * two state button, auto switch background, default first click notify a false.
 * @author: FGC
 */
public class ButtonTwoState extends Button{

	private static final String LOG_TAG = "ButtonTwoState";
	
	public ButtonTwoState(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ButtonTwoState(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ButtonTwoState(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	
	private Context mContext;
	private Drawable mBgPositive;
	private Drawable mBgNegative;
	private OnTwoStateSwitchListener mOnSwitchListener;
	private boolean mState = true;
	
	private void init(Context context)
	{
		mContext = context;
		mBgPositive = getBackground();
		mBgNegative = getBackground();
		setOnClickListener(mOnClickListenenr);
	}
	
	private OnClickListener mOnClickListenenr = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			doSwitch();
		}
	};
	
	public interface OnTwoStateSwitchListener
	{
		public void onSwitch(boolean isPositive, int btnId);
	}
	
	public void setOnTwoStateSwitchListener(OnTwoStateSwitchListener listener)
	{
		mOnSwitchListener = listener;
	}
	
	public void setTwoStateDrawble(Drawable state1, Drawable state2)
	{
		mBgPositive = state1;
		mBgNegative = state2;
		setBackgroundDrawable(mBgPositive);
	}
	
	public void setTwoStateDrawble(int stateSrcId1, int stateSrcId2)
	{
		mBgPositive = mContext.getResources().getDrawable(stateSrcId1);
		mBgNegative = mContext.getResources().getDrawable(stateSrcId2);
		setBackgroundDrawable(mBgPositive);
	}
	
	private void doSwitch()
	{
		if (mState == true)
		{
			mState = false;
			setBackgroundDrawable(mBgNegative);
		}
		else
		{
			mState = true;
			setBackgroundDrawable(mBgPositive);
		}
		
		if (mOnSwitchListener != null)
		{
			mOnSwitchListener.onSwitch(mState, getId());
		}
	}
}
