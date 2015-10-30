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

import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Static methods related to Geo.
 */
public class GeoUtil {

    /**
     * Returns the country code of the country the user is currently in. Before calling this
     * method, make sure that {@link CountryDetector#initialize(Context)} has already been called
     * in {@link Application#onCreate()}.
     * @return The ISO 3166-1 two letters country code of the country the user
     *         is in.
     */
    public static String getCurrentCountryIso(Context context) {
    	return "CN";
   // 	return getLocaleBasedCountryIso();
    }


    private static String getLocaleBasedCountryIso() {

        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale != null) {
            return defaultLocale.getCountry();
        }
        return null;
    }
}
