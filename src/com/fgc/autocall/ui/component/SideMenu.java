package com.fgc.autocall.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.fgc.autocall.R;
import com.fgc.autocall.Tools.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import net.simonvt.menudrawer.MenuDrawer;

public class SideMenu {
	private static final String LOG_TAG = "SideMenu";
	
    public static final int MENU_INDEX_SETTING = 0;
    public static final int MENU_INDEX_EXPORT_CALL_RECORD = 1;
    public static final int MENU_INDEX_CHECK_SIM_CARD_INFO = 2;
    public static final int MENU_INDEX_ABOUT_US = 3;
	  
	private Activity mActivity;
	private MenuDrawer mMenuDrawer;
	private ListView mMenuListView;
	private MenuAdapter mMenuAdapter;
	private OnMenuItemClickObserver mOnMenuItemClickObserver;
	
	public SideMenu(Activity activity)
	{
		mActivity = activity;
		mMenuDrawer = MenuDrawer.attach(mActivity);
	}
	
	/*
	 * must be use in the last of the main activity initView.
	 */
	public void init()
	{
		initView();
	}
	
	public MenuDrawer getMenuDrawer()
	{
		return mMenuDrawer;
	}
	
	public interface OnMenuItemClickObserver
	{
		public void onClickMenuItem(int position);
	}
	
	public void setOnMenuItemClickObserver(OnMenuItemClickObserver observer)
	{
		mOnMenuItemClickObserver = observer;
	}
	
	private void initView()
	{
		View menuView = LayoutInflater.from(mActivity).inflate(R.layout.left_menu, null);
		mMenuDrawer.setMenuView(menuView);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setMenuSize(Tools.getScreenWidth(mActivity)*2/3+40);
		mMenuDrawer.setDropShadowSize(Tools.dip2px(mActivity, 10));
		
		
		Resources resource = mActivity.getResources();
		
		
    	List<MenuItem> items = new ArrayList<MenuItem>();
        items.add(MENU_INDEX_SETTING, new MenuItem(resource.getString(R.string.left_menu_setting),
        												true,
        												true,
        												R.drawable.left_menu_setting));
    	items.add(MENU_INDEX_EXPORT_CALL_RECORD, new MenuItem(resource.getString(R.string.left_menu_export),
												 true, 
												 false,
												 R.drawable.left_menu_export));
        items.add(MENU_INDEX_CHECK_SIM_CARD_INFO, new MenuItem(resource.getString(R.string.left_menu_simcard),
												false,
												true,
												R.drawable.left_menu_simcard));
        items.add(MENU_INDEX_ABOUT_US, new MenuItem(resource.getString(R.string.left_menu_about),
        										 true,
        										 true,
        										 R.drawable.left_menu_about_us));
         
    	mMenuListView = (ListView)menuView.findViewById(R.id.list_view_menu);
    	mMenuAdapter = new MenuAdapter(items);
    	mMenuListView.setAdapter(mMenuAdapter);
    	mMenuListView.setOnItemClickListener(mItemClickListener);
	}
	
	public void open()
	{
		mMenuDrawer.openMenu();
	}
	
	public void close()
	{
        mMenuDrawer.closeMenu();
	}
	
    private class MenuItem {

    	boolean mIsGroupStart;
    	boolean mIsGroupEnd;
    	int mIconResId;
        String mTitle;

        public MenuItem(String title, boolean isGroupStart, boolean isGroupEnd, int iconResId) 
        {
            mTitle = title;
            mIsGroupStart = isGroupStart;
            mIsGroupEnd = isGroupEnd;
        	mIconResId = iconResId;
        }
    }
    
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	
//        	Log.i(LOG_TAG, "menu position: " + position);
            
            switch(position){
	            case MENU_INDEX_SETTING:{
	            	Log.i(LOG_TAG, "MENU_INDEX_SETTING");
	            	break;
	            }
	            case MENU_INDEX_EXPORT_CALL_RECORD:
	            {
	            	Log.i(LOG_TAG, "MENU_INDEX_EXPORT_CALL_RECORD");
	            	break;
	            }
	            case MENU_INDEX_CHECK_SIM_CARD_INFO:
	            {
	            	Log.i(LOG_TAG, "MENU_INDEX_CHECK_SIM_CARD_INFO");
	            	break;
	            }
	            case MENU_INDEX_ABOUT_US:{
	            	Log.i(LOG_TAG, "MENU_INDEX_ABOUT_US");
	            	break;
	            }
            }
            
            if (mOnMenuItemClickObserver != null)
            {
            	mOnMenuItemClickObserver.onClickMenuItem(position);
            }
            
        }
    };
	
    private class MenuAdapter extends BaseAdapter {

        private List<MenuItem> mItems;

        MenuAdapter(List<MenuItem> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof MenuItem ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItem(position) instanceof MenuItem;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
        	if (position<0 || position>=getCount())
        	{
        		return null;
        	}
        	
        	LinearLayout itemView = null;
            MenuItem item = (MenuItem)getItem(position);

            if (convertView == null)
            {
            	itemView = (LinearLayout)LayoutInflater.from(mActivity).inflate(R.layout.left_menu_item_title, parent, false);
            }
            else
            {
            	itemView = (LinearLayout)convertView;
            }
            
            LinearLayout groupSeperator = (LinearLayout)itemView.findViewById(R.id.layout_group_seperator);
            if (item.mIsGroupStart)
            {
            	groupSeperator.setVisibility(View.VISIBLE);
            }
            else
            {
            	groupSeperator.setVisibility(View.GONE);
            }
            LinearLayout endSeperator = (LinearLayout)itemView.findViewById(R.id.layout_end_seperator);
            if (item.mIsGroupEnd)
            {
//            	Log.i(LOG_TAG, "hide end pos: " + position);
            	endSeperator.setVisibility(View.GONE);
            }
            else
            {
            	endSeperator.setVisibility(View.VISIBLE);
            }
            LinearLayout lastSeperator = (LinearLayout)itemView.findViewById(R.id.layout_last_seperator);
            if (position == getCount()-1)
            {
//            	Log.i(LOG_TAG, "show last pos: " + position);
            	lastSeperator.setVisibility(View.VISIBLE);
            }
            else
            {
            	lastSeperator.setVisibility(View.GONE);
            }
            ImageView imageIcon = (ImageView)itemView.findViewById(R.id.image_icon);
            TextView textTitle = (TextView)itemView.findViewById(R.id.text_title);
            
            imageIcon.setImageResource(item.mIconResId);
            textTitle.setText((item.mTitle));

//            itemView.setTag(R.id.mdActiveViewPosition, position);

            return itemView;
        }
    }
	
}
