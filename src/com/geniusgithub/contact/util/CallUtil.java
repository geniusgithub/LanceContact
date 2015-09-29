/*
 * Copyright (C) 2015 The Android Open Source Project
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
import android.telecom.TelecomManager;
import android.text.TextUtils;

/**
 * Utilities related to calls that can be used by non system apps. These
 * use {@link Intent#ACTION_CALL} instead of ACTION_CALL_PRIVILEGED.
 *
 * The privileged version of this util exists inside Dialer.
 */
public class CallUtil {

    /**
     * Return an Intent for making a phone call. Scheme (e.g. tel, sip) will be determined
     * automatically.
     */
	private static Intent getCallIntent(String number) {
        return getCallIntent(getCallUri(number));
    }

    /**
     * Return an Intent for making a phone call. A given Uri will be used as is (without any
     * sanity check).
     */
    private static Intent getCallIntent(Uri uri) {
        return new Intent(Intent.ACTION_CALL, uri);
    }

 
    /**
     * Return Uri with an appropriate scheme, accepting both SIP and usual phone call
     * numbers.
     */
    private static Uri getCallUri(String number) {
        return Uri.fromParts("tel", number, null);
    }

    /**
     * Return an Intent for making a phone call. Scheme (e.g. tel, sip) will be determined
     * automatically.
     */
    public static Intent getCallIntent(Context context, String number, int solotID) {
        
        Uri uri = getCallUri(number);
        final Intent intent = getCallIntent(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
