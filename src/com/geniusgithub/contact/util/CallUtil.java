///*
// * Copyright (C) 2015 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License
// */
//
//package com.geniusgithub.contact.util;
//
//import java.util.List;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.telecom.PhoneAccount;
//import android.telecom.PhoneAccountHandle;
//import android.telecom.TelecomManager;
//import android.telecom.VideoProfile;
//
///**
// * Utilities related to calls that can be used by non system apps. These
// * use {@link Intent#ACTION_CALL} instead of ACTION_CALL_PRIVILEGED.
// *
// * The privileged version of this util exists inside Dialer.
// */
//public class CallUtil {
//
//    /**
//     * Return an Intent for making a phone call. Scheme (e.g. tel, sip) will be determined
//     * automatically.
//     */
//	private static Intent getCallIntent(String number) {
//        return getCallIntent(getCallUri(number));
//    }
//
//	 public static Intent getCallIntent(String number, PhoneAccountHandle accountHandle) {
//	        return getCallIntent(number, null, accountHandle);
//	    }
//
//	 
//    /**
//     * Return an Intent for making a phone call. A given Uri will be used as is (without any
//     * sanity check).
//     */
//    private static Intent getCallIntent(Uri uri) {
//        return new Intent(Intent.ACTION_CALL, uri);
//    }
//
// 
//    /**
//     * Return Uri with an appropriate scheme, accepting both SIP and usual phone call
//     * numbers.
//     */
//    private static Uri getCallUri(String number) {
//        return Uri.fromParts("tel", number, null);
//    }
//
//    /**
//     * Return an Intent for making a phone call. Scheme (e.g. tel, sip) will be determined
//     * automatically.
//     */
//    public static Intent getCallIntent(Context context, String number, int solotID) {
//        
//        Uri uri = getCallUri(number);
//        final Intent intent = getCallIntent(uri);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        return intent;
//    }
//    
//    /**
//     * A variant of {@link #getCallIntent(String, String, android.telecom.PhoneAccountHandle)} for
//     * starting a video call.
//     */
//    public static Intent getVideoCallIntent(String number, PhoneAccountHandle accountHandle) {
//        return getVideoCallIntent(number, null, accountHandle);
//    }
//    
//    /**
//     * A variant of {@link #getCallIntent(String, String, android.telecom.PhoneAccountHandle)} for
//     * starting a video call.
//     */
//    public static Intent getVideoCallIntent(
//            String number, String callOrigin, PhoneAccountHandle accountHandle) {
//        return getCallIntent(getCallUri(number), callOrigin, accountHandle,
//                VideoProfile.VideoState.BIDIRECTIONAL);
//    }
//
//    
//    private static boolean hasCapability(PhoneAccount phoneAccount, int capability) {
//        return (phoneAccount != null) &&
//                ((phoneAccount.getCapabilities() & capability) == capability);
//    }
//    
//    
//    public static boolean isVideoEnabled(Context context) {
//  
//        TelecomManager telecommMgr = (TelecomManager)
//                context.getSystemService(Context.TELECOM_SERVICE);
//        if (telecommMgr == null) {
//            return false;
//        }
//        List<PhoneAccountHandle> phoneAccountHandles = telecommMgr.getCallCapablePhoneAccounts();
//        for (PhoneAccountHandle handle : phoneAccountHandles) {
//            final PhoneAccount phoneAccount = telecommMgr.getPhoneAccount(handle);
//            if (hasCapability(phoneAccount, PhoneAccount.CAPABILITY_VIDEO_CALLING)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.geniusgithub.contact.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;


/**
 * Utilities related to calls.
 */
public class CallUtil {

    /**
     * Return an Intent for making a phone call. Scheme (e.g. tel, sip) will be determined
     * automatically.
     */
    public static Intent getCallIntent(String number) {
        return getCallIntent(number, null, null);
    }

    /**
     * Return an Intent for making a phone call. A given Uri will be used as is (without any
     * sanity check).
     */
    public static Intent getCallIntent(Uri uri) {
        return getCallIntent(uri, null, null);
    }

    /**
     * A variant of {@link #getCallIntent(String)} but also accept a call origin.
     * For more information about call origin, see comments in Phone package (PhoneApp).
     */
    public static Intent getCallIntent(String number, String callOrigin) {
        return getCallIntent(getCallUri(number), callOrigin, null);
    }

    /**
     * A variant of {@link #getCallIntent(String)} but also include {@code Account}.
     */
    public static Intent getCallIntent(String number, PhoneAccountHandle accountHandle) {
        return getCallIntent(number, null, accountHandle);
    }

    /**
     * A variant of {@link #getCallIntent(android.net.Uri)} but also include {@code Account}.
     */
    public static Intent getCallIntent(Uri uri, PhoneAccountHandle accountHandle) {
        return getCallIntent(uri, null, accountHandle);
    }

    /**
     * A variant of {@link #getCallIntent(String, String)} but also include {@code Account}.
     */
    public static Intent getCallIntent(
            String number, String callOrigin, PhoneAccountHandle accountHandle) {
        return getCallIntent(getCallUri(number), callOrigin, accountHandle);
    }

    /**
     * A variant of {@link #getCallIntent(android.net.Uri)} but also accept a call
     * origin and {@code Account}.
     * For more information about call origin, see comments in Phone package (PhoneApp).
     */
    public static Intent getCallIntent(
            Uri uri, String callOrigin, PhoneAccountHandle accountHandle) {
        return getCallIntent(uri, callOrigin, accountHandle,
                VideoProfile.VideoState.AUDIO_ONLY);
    }

    /**
     * A variant of {@link #getCallIntent(String, String)} for starting a video call.
     */
    public static Intent getVideoCallIntent(String number, String callOrigin) {
        return getCallIntent(getCallUri(number), callOrigin, null,
                VideoProfile.VideoState.BIDIRECTIONAL);
    }

    /**
     * A variant of {@link #getCallIntent(String, String, android.telecom.PhoneAccountHandle)} for
     * starting a video call.
     */
    public static Intent getVideoCallIntent(
            String number, String callOrigin, PhoneAccountHandle accountHandle) {
        return getCallIntent(getCallUri(number), callOrigin, accountHandle,
                VideoProfile.VideoState.BIDIRECTIONAL);
    }

    /**
     * A variant of {@link #getCallIntent(String, String, android.telecom.PhoneAccountHandle)} for
     * starting a video call.
     */
    public static Intent getVideoCallIntent(String number, PhoneAccountHandle accountHandle) {
        return getVideoCallIntent(number, null, accountHandle);
    }
    
    public static final String EXTRA_CALL_ORIGIN = "com.android.phone.CALL_ORIGIN";
    /**
     * A variant of {@link #getCallIntent(android.net.Uri)} but also accept a call
     * origin and {@code Account} and {@code VideoCallProfile} state.
     * For more information about call origin, see comments in Phone package (PhoneApp).
     */
    public static Intent getCallIntent(
            Uri uri, String callOrigin, PhoneAccountHandle accountHandle, int videoState) {
        //final Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED, uri);
        final Intent intent = new Intent(Intent.ACTION_CALL, uri);
        intent.putExtra(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, videoState);
        if (callOrigin != null) {
            intent.putExtra(EXTRA_CALL_ORIGIN, callOrigin);
        }
        if (accountHandle != null) {
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, accountHandle);
        }

        return intent;
    }

    /**
     * Return Uri with an appropriate scheme, accepting both SIP and usual phone call
     * numbers.
     */
    public static Uri getCallUri(String number) {
        if (PhoneNumberHelper.isUriNumber(number)) {
             return Uri.fromParts(PhoneAccount.SCHEME_SIP, number, null);
        }
        return Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null);
     }

    private static boolean hasCapability(PhoneAccount phoneAccount, int capability) {
        return (phoneAccount != null) &&
                ((phoneAccount.getCapabilities() & capability) == capability);
    }

    public static boolean isVideoEnabled(Context context) {
      
        if (true){
            return true;
            }
        TelecomManager telecommMgr = (TelecomManager)
                context.getSystemService(Context.TELECOM_SERVICE);
        if (telecommMgr == null) {
            return false;
        }
        List<PhoneAccountHandle> phoneAccountHandles = telecommMgr.getCallCapablePhoneAccounts();
        for (PhoneAccountHandle handle : phoneAccountHandles) {
            final PhoneAccount phoneAccount = telecommMgr.getPhoneAccount(handle);
            if (hasCapability(phoneAccount, PhoneAccount.CAPABILITY_VIDEO_CALLING)) {
                return true;
            }
        }
        return false;
    }
}

