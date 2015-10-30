package com.geniusgithub.contact.util;


public class LogFactory {
	private static final String TAG = "LanceContact";
	//private static CommonLog log = null;

	public static CommonLog createLog() {
		CommonLog log = null;
		if (log == null) {
    		log = new CommonLog();
		}
		
		log.setTag(TAG);
		return log;
	}
	
	public static CommonLog createLog(String tag) {
		CommonLog log = null;
		if (log == null) {
			log = new CommonLog();
		}
		
		if (tag == null || tag.length() < 1) {
    		log.setTag(TAG);
		} else {
			log.setTag(tag);
		}
		return log;
	}
}