package com.geniusgithub.contact.base;

import android.app.Activity;
import android.content.res.Configuration;


public class BaseActivityBinder implements IBaseInterface{

	private final Activity mActivity;
    
	public BaseActivityBinder(Activity activity) {
		mActivity = activity;
	}
	  
    public void onCreate() {
    	if(mActivity == null) return;
		
    }
    
    public void onResume() {
    	if(mActivity == null) return;    	    	
    }
    
    public void onPause() {
    	if(mActivity == null) return;    		

    }
    
    public void onDestroy() {
    	if(mActivity == null) return;
    }
    

    public void onConfigurationChanged(Configuration newConfig) {
    	if(mActivity == null) return;
    }

}
