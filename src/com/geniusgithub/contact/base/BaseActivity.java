package com.geniusgithub.contact.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public abstract class BaseActivity extends Activity{

	private BaseActivityBinder mBaseActivityBinder;
	
	public BaseActivity() {
		super();
		mBaseActivityBinder = new BaseActivityBinder(this);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(mBaseActivityBinder != null) mBaseActivityBinder.onCreate();
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		if(mBaseActivityBinder != null) mBaseActivityBinder.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if(mBaseActivityBinder != null) mBaseActivityBinder.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mBaseActivityBinder != null) mBaseActivityBinder.onDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if(mBaseActivityBinder != null) mBaseActivityBinder.onConfigurationChanged(newConfig);
	}
	
	public BaseActivityBinder getActivityBinder() {
		return mBaseActivityBinder;
	}
	
	
}
