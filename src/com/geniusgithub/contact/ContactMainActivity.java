package com.geniusgithub.contact;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import com.geniusgithub.contact.base.BaseActivity;
import com.geniusgithub.contact.base.BaseActivityBinder;
import com.geniusgithub.contact.base.BaseFragment;
import com.geniusgithub.contact.base.ITabInterface;
import com.geniusgithub.contact.dialer.DialpadFragment;
import com.geniusgithub.contact.main.CallLogFragment;
import com.geniusgithub.contact.main.ContactFragment;
import com.geniusgithub.contact.main.PersionFragment;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public class ContactMainActivity extends BaseActivity{

	private static final CommonLog log = LogFactory.createLog(ContactMainActivity.class.getSimpleName());
	
	private final static String TAB_DIALPAD_FRAGMENT = "TAB_DIALPAD_FRAGMENT";
	private final static String TAB_CALLLOG_FRAGMENT = "TAB_CALLLOG_FRAGMENT";
	private final static String TAB_CONTACT_FRAGMENT = "TAB_CONTACT_FRAGMENT";
	private final static String TAB_PERSION_FRAGMENT = "TAB_PERSION_FRAGMENT";
	
	private FragmentManager mFragmentManager;
    private TabManager mTabManager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_layout);
		
		initView();
		log.i("onCreate");
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		log.i("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		log.i("onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		log.i("onDestroy");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		log.i("onConfigurationChanged");
	}

	@Override
	public BaseActivityBinder getActivityBinder() {
		return super.getActivityBinder();
	}


	private void initView(){


		mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		BaseFragment mDialpadFragment = new DialpadFragment(LanceContactApplication.getInstance());	
		fragmentTransaction.add(R.id.fragment_container, mDialpadFragment, TAB_DIALPAD_FRAGMENT);
		BaseFragment mCalllogFragment = new CallLogFragment(LanceContactApplication.getInstance());	
		fragmentTransaction.add(R.id.fragment_container, mCalllogFragment, TAB_CALLLOG_FRAGMENT);
		BaseFragment mContactFragment = new ContactFragment(LanceContactApplication.getInstance());	
		fragmentTransaction.add(R.id.fragment_container, mContactFragment, TAB_CONTACT_FRAGMENT);
		BaseFragment mPersionFragment = new PersionFragment(LanceContactApplication.getInstance());	
		fragmentTransaction.add(R.id.fragment_container, mPersionFragment, TAB_PERSION_FRAGMENT);
		fragmentTransaction.hide(mDialpadFragment);
		fragmentTransaction.hide(mCalllogFragment);
		fragmentTransaction.hide(mContactFragment);
		fragmentTransaction.hide(mPersionFragment);
		fragmentTransaction.commitAllowingStateLoss();

		TabViewContainer tabViewContainer = (TabViewContainer) findViewById(R.id.tabview_container);
		
		mTabManager = new TabManager(this, tabViewContainer);
		
		mTabManager.attchActivity(this);
		mTabManager.bindFragment(mDialpadFragment, 0);
		mTabManager.bindFragment(mCalllogFragment, 1);
		mTabManager.bindFragment(mContactFragment, 2);
		mTabManager.bindFragment(mPersionFragment, 3);
		
		mTabManager.setTab(0);
		
	}
	

}
