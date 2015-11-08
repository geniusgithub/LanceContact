package com.geniusgithub.contact;

import android.database.Cursor;

import com.geniusgithub.contact.contact.calllog.CallLogQuery;

public class CallLogDebug {

	public static  String getDisplayString(Cursor cursor){
		if (cursor == null){
			return null;
		}
		
	    final String number = cursor.getString(CallLogQuery.NUMBER);
   //     final int numberPresentation = cursor.getInt(CallLogQuery.NUMBER_PRESENTATION);
        final int callType = cursor.getInt(CallLogQuery.CALL_TYPE);
  //      final String countryIso = cursor.getString(CallLogQuery.COUNTRY_ISO);
        final String accId = cursor.getString(CallLogQuery.ACCOUNT_ID);
	    final String currentAccountComponentName = cursor.getString( CallLogQuery.ACCOUNT_COMPONENT_NAME); 
	      
	      
	    StringBuffer stringBuffer = new StringBuffer();
	    stringBuffer.append("number = " + number + 
	    				"\ncallType = " + callType + 
	    				"\naccId = " + accId + 
	    				"\ncurrentAccountComponentName = "+ currentAccountComponentName);
 
	    return stringBuffer.toString();
	    				
	}
}
