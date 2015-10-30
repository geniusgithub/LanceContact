package com.geniusgithub.contact;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import com.geniusgithub.contact.base.BaseFragment;
import com.geniusgithub.contact.base.ITabInterface;
import com.geniusgithub.contact.contact.ContactManager;
import com.geniusgithub.contact.dialer.DialpadFragment;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public class TabManager implements ITabInterface {

	protected  final CommonLog log = LogFactory.createLog(TabManager.class.getSimpleName());
	private final static int CHILD_COUNT = 4;
	
	private Context mContext;
	private TabViewContainer mTabViewContainer;
	
	private Activity mActivity;
    private BaseFragment mCurrentFragment;
    private Map<Integer, BaseFragment> mFrMap;

	
	public  TabManager(Context context, TabViewContainer container){
		mContext = context;
		mTabViewContainer = container;
		mTabViewContainer.setTabListener(this);

		mFrMap = new HashMap<>();
	}

	public void attchActivity(Activity activity){
		mActivity = activity;
		
		log.i("attchActivity mActivity = " + mActivity);
		
	}
	
	public void onDetachActivity(){
		log.i("onDetachActivity");
		mActivity = null;
		mFrMap.clear();
		mCurrentFragment = null;
	}

	@Override
	public void onTabSelect(int position) {
		
		log.i("onTabSelect pos = " + position);
		if (mActivity != null){
			FragmentManager fragmentManager = mActivity.getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			
			if (mCurrentFragment != null){
				fragmentTransaction.hide(mCurrentFragment);
			}
			
			mCurrentFragment = findFragment(position);
			if (mCurrentFragment != null){
				fragmentTransaction.show(mCurrentFragment);
				log.i("show fragment = " + mCurrentFragment);
			}else{
				log.e("can't find fragment for pos:" + position);
			}
			
			fragmentTransaction.commitAllowingStateLoss();
		}
		
	}


	@Override
	public void setTab(int position) {
		mTabViewContainer.setTab(position);
	}


	@Override
	public int getCurrentTabPosition(Context context) {
		return mTabViewContainer.getCurrentTabPosition(context);
	}


	public BaseFragment bindFragment(BaseFragment fragment, int position){
		return mFrMap.put(position, fragment);
	}

	public BaseFragment findFragment(int position){
		return mFrMap.get(position);
	}
	
	public void clearFragment(){
		mFrMap.clear();
		mCurrentFragment = null;
	}

	@Override
	public void onTabClick(int position) {

		if (position == 0){
			BaseFragment fragment = findFragment(position);
			if (fragment != null){
				if (fragment instanceof DialpadFragment){
					((DialpadFragment) fragment).toggleDialpad(mContext);
				}
			}
		}
	
	
	}
}
