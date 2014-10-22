package com.fgc.autocall.ui.component;

import com.fgc.autocall.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListViewRoundCorner extends ListView {
	public ListViewRoundCorner(Context context) {
		super(context);
	}

	public ListViewRoundCorner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/****
	 * ���ش����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			int itemnum = pointToPosition(x, y);
			if (itemnum == AdapterView.INVALID_POSITION)
				break;
			else {
//				if (itemnum == 0) {
//					if (itemnum == (getAdapter().getCount() - 1)) {
//						// ֻ��һ��
//						setSelector(R.drawable.list_item_all_round);
//					} else {
//						// ��һ��
//						setSelector(R.drawable.list_item_top_round);
//					}
//				} else if (itemnum == (getAdapter().getCount() - 1))
//					// ���һ��
//					setSelector(R.drawable.list_item_bottom_round);
//				else {
//					// �м���
//					setSelector(R.drawable.list_item_center_round);
//				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			
			break;
		}
		return super.onTouchEvent(ev);
	}
}