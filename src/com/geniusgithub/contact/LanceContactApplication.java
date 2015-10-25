package com.geniusgithub.contact;

import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.geniusgithub.contact.contact.ContactManager;
import com.geniusgithub.contact.contact.ContactsHelper.OnContactsLoad;
import com.geniusgithub.contact.dialer.util.KeyboardTone;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public class LanceContactApplication extends Application {

	private static final CommonLog log = LogFactory.createLog(LanceContactApplication.class.getSimpleName());
	
	private static LanceContactApplication mInstance;
		
	private Handler mHandler;
	private boolean mEnterMain = false;
	
	public synchronized static LanceContactApplication getInstance(){
		return mInstance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		log.i("LanceApplication  onCreate!!!");
		mInstance = this;
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
			}
			
		};

		ContactManager.getInstance(this).startLoadContact();
		
		KeyboardTone.getInstance(this).init();
		
	}

	

}
