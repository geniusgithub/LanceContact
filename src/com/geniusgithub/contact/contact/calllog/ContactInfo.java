/*
 * Copyright (C) 2011 The Android Open Source Project
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
 * limitations under the License.
 */

package com.geniusgithub.contact.contact.calllog;

import android.net.Uri;
import android.text.TextUtils;

import com.geniusgithub.contact.util.UriUtils;

/**
 * Information for a contact as needed by the Call Log.
 */
public class ContactInfo {
    public Uri lookupUri;
    public String lookupKey;
    public String name;
    public int type;
    public String label;
    public String number;
    public String formattedNumber;
    public String normalizedNumber;
    /** The photo for the contact, if available. */
    public long photoId;
    /** The high-res photo for the contact, if available. */
    public Uri photoUri;
    public boolean isBadData;
    public String objectId;

    public static ContactInfo EMPTY = new ContactInfo();

    public static String GEOCODE_AS_LABEL = "";

    public int sourceType = 0;

    @Override
    public int hashCode() {
        // Uses only name and contactUri to determine hashcode.
        // This should be sufficient to have a reasonable distribution of hash codes.
        // Moreover, there should be no two people with the same lookupUri.
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lookupUri == null) ? 0 : lookupUri.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ContactInfo other = (ContactInfo) obj;
        if (!UriUtils.areEqual(lookupUri, other.lookupUri)) return false;
        if (!TextUtils.equals(name, other.name)) return false;
        if (type != other.type) return false;
        if (!TextUtils.equals(label, other.label)) return false;
        if (!TextUtils.equals(number, other.number)) return false;
        if (!TextUtils.equals(formattedNumber, other.formattedNumber)) return false;
        if (!TextUtils.equals(normalizedNumber, other.normalizedNumber)) return false;
        if (photoId != other.photoId) return false;
        if (!UriUtils.areEqual(photoUri, other.photoUri)) return false;
        if (!TextUtils.equals(objectId, other.objectId)) return false;
        return true;
    }

    @Override
    public String toString() {
    	StringBuffer sbBuffer = new StringBuffer();
        return sbBuffer.append("lookupUri="+ lookupUri).append("\nname="+ name).append(
                "\ntype="+ type).append("\nlabel="+label).append("\nnumber="+ number).append("\nformattedNumber="+
                formattedNumber).append("\nnormalizedNumber="+ normalizedNumber).append("\nphotoId="+ photoId)
                .append("\nphotoUri="+ photoUri).append("\nobjectId="+ objectId).toString();
    }
}
