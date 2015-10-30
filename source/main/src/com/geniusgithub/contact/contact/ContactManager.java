package com.geniusgithub.contact.contact;

import android.content.Context;
import android.os.Handler;

import com.geniusgithub.contact.contact.ContactsHelper.OnContactsLoad;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public class ContactManager implements OnContactsLoad{

	private static final CommonLog log = LogFactory.createLog();
	private static ContactManager mInstance;

	private Context mContext;
	private Handler mHandler;
	
	private ContactsHelper mContactsHelper;
	
	public static synchronized ContactManager getInstance(Context context) {
		if (mInstance == null){
			mInstance  = new ContactManager(context);
		
		}
		return mInstance;
	}
	
	private ContactManager(Context context){
		mContext = context;
		init();
	}
	
	private void init(){
		mContactsHelper = ContactsHelper.getInstance();
		mContactsHelper.setOnContactsLoad(this);
	}
	
	public boolean startLoadContact(){
		
		boolean ret = mContactsHelper.startLoadContacts();
		log.i("startLoadContact ret = " + ret);
		return ret;
	}
	

	@Override
	public void onContactsLoadSuccess() {
		log.i("onContactsLoadSuccess");
	}

	@Override
	public void onContactsLoadFailed() {
		log.i("onContactsLoadFailed");
	}
	
	
	public void parseT9InputSearchContacts(String search) {
		log.i("parseT9InputSearchContacts search = " + search);
		mContactsHelper.parseT9InputSearchContacts(search);
	}
	
	
}
