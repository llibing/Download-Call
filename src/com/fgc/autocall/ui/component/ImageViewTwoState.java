package com.fgc.autocall.ui.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/*
 * two state image view, auto switch background, default first click notify a true.
 * @author: FGC
 */
public class ImageViewTwoState extends ImageView{

	private static final String LOG_TAG = "ImageViewTwoState";
	
	public ImageViewTwoState(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ImageViewTwoState(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ImageViewTwoState(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	
	private Context mContext;
	private Drawable mBgPositive;
	private Drawable mBgNegative;
	private OnTwoStateSwitchListener mOnSwitchListener;
	private boolean mState = false;
	
	private void init(Context context)
	{
		mContext = context;
		mBgPositive = getDrawable();
		mBgNegative = getDrawable();
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
		public void onSwitch(boolean isPositive, int imgId);
	}
	
	public void setOnTwoStateSwitchListener(OnTwoStateSwitchListener listener)
	{
		mOnSwitchListener = listener;
	}
	
	public void setTwoStateDrawble(Drawable state1, Drawable state2)
	{
		mBgPositive = state1;
		mBgNegative = state2;
		setImageDrawable(mBgPositive);
	}
	
	public void setTwoStateDrawble(int stateSrcId1, int stateSrcId2)
	{
		mBgPositive = mContext.getResources().getDrawable(stateSrcId1);
		mBgNegative = mContext.getResources().getDrawable(stateSrcId2);
		setImageDrawable(mBgPositive);
	}
	
	public void setDefaultState(boolean isSelect)
	{
		mState = isSelect;
		if (mState)
		{
			setImageDrawable(mBgNegative);
		}
		else
		{
			setImageDrawable(mBgPositive);
		}
	}
	
	private void doSwitch()
	{
		if (mState == true)
		{
			mState = false;
			setImageDrawable(mBgPositive);
		}
		else
		{
			mState = true;
			setImageDrawable(mBgNegative);
		}
		
		if (mOnSwitchListener != null)
		{
			mOnSwitchListener.onSwitch(mState, getId());
		}
	}
}
