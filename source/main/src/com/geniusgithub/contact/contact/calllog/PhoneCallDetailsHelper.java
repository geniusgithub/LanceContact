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

import java.util.ArrayList;

import android.content.res.Resources;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.geniusgithub.contact.R;
import com.geniusgithub.contact.util.DialerUtils;
import com.geniusgithub.contact.util.PhoneNumberHelper;
import com.google.android.collect.Lists;

/**
 * Helper class to fill in the views in {@link PhoneCallDetailsViews}.
 */
public class PhoneCallDetailsHelper {
    /** The maximum number of icons will be shown to represent the call types in a group. */
    private static final int MAX_CALL_TYPE_ICONS = 3;

    private final Resources mResources;
    /** The injected current time in milliseconds since the epoch. Used only by tests. */
    private Long mCurrentTimeMillisForTest;
    // Helper classes.
    private final PhoneNumberDisplayHelper mPhoneNumberHelper;
    private final PhoneNumberUtilsWrapper mPhoneNumberUtilsWrapper;

    /**
     * List of items to be concatenated together for accessibility descriptions
     */
    private ArrayList<CharSequence> mDescriptionItems = Lists.newArrayList();

    /**
     * Creates a new instance of the helper.
     * <p>
     * Generally you should have a single instance of this helper in any context.
     *
     * @param resources used to look up strings
     */
    public PhoneCallDetailsHelper(Resources resources, CallTypeHelper callTypeHelper,
            PhoneNumberUtilsWrapper phoneUtils) {
        mResources = resources;
        mPhoneNumberUtilsWrapper = phoneUtils;
        mPhoneNumberHelper = new PhoneNumberDisplayHelper(mPhoneNumberUtilsWrapper, resources);
    }

    /** Fills the call details views with content. */
    public void setPhoneCallDetails(PhoneCallDetailsViews views, PhoneCallDetails details) {
        // Display up to a given number of icons.
        views.callTypeIcons.clear();
        int count = details.callTypes.length;
        boolean isVoicemail = false;
        for (int index = 0; index < count && index < MAX_CALL_TYPE_ICONS; ++index) {
            views.callTypeIcons.add(details.callTypes[index]);
            if (index == 0) {
                isVoicemail = details.callTypes[index] == Calls.VOICEMAIL_TYPE;
            }
        }

        // Show the video icon if the call had video enabled.
        views.callTypeIcons.setShowVideo(
                (details.features & Calls.FEATURES_VIDEO) == Calls.FEATURES_VIDEO);
        views.callTypeIcons.requestLayout();
        views.callTypeIcons.setVisibility(View.VISIBLE);

        // Show the total call count only if there are more than the maximum number of icons.
        final Integer callCount;
        if (count > MAX_CALL_TYPE_ICONS) {
            callCount = count;
        } else {
            callCount = null;
        }

        CharSequence callLocationAndDate = getCallLocationAndDate(details);

        // Set the call count, location and date.
        setCallCountAndDate(views, callCount, callLocationAndDate);

        // set the account icon if it exists
        if (details.accountIcon != null) {
            views.callAccountIcon.setVisibility(View.VISIBLE);
            views.callAccountIcon.setImageDrawable(details.accountIcon);
        } else {
            views.callAccountIcon.setVisibility(View.GONE);
        }

        final CharSequence nameText;
        final CharSequence displayNumber =
            mPhoneNumberHelper.getDisplayNumber(details.accountId, details.number,
                    details.numberPresentation, details.formattedNumber);
        if (TextUtils.isEmpty(details.name)) {
            nameText = displayNumber;
            // We have a real phone number as "nameView" so make it always LTR
            views.nameView.setTextDirection(View.TEXT_DIRECTION_LTR);
        } else {
            nameText = details.name;
        }

        views.nameView.setText(nameText);

        if (isVoicemail && !TextUtils.isEmpty(details.transcription)) {
            views.voicemailTranscriptionView.setText(details.transcription);
            views.voicemailTranscriptionView.setVisibility(View.VISIBLE);
        } else {
            views.voicemailTranscriptionView.setText(null);
            views.voicemailTranscriptionView.setVisibility(View.GONE);
        }
    }

    /**
     * Builds a string containing the call location and date.
     *
     * @param details The call details.
     * @return The call location and date string.
     */
    private CharSequence getCallLocationAndDate(PhoneCallDetails details) {
        mDescriptionItems.clear();

        // Get type of call (ie mobile, home, etc) if known, or the caller's location.
        CharSequence callTypeOrLocation = getCallTypeOrLocation(details);

        // Only add the call type or location if its not empty.  It will be empty for unknown
        // callers.
        if (!TextUtils.isEmpty(callTypeOrLocation)) {
            mDescriptionItems.add(callTypeOrLocation);
        }
        // The date of this call, relative to the current time.
        mDescriptionItems.add(getCallDate(details));

        // Create a comma separated list from the call type or location, and call date.
        return DialerUtils.join(mResources, mDescriptionItems);
    }

    /**
     * For a call, if there is an associated contact for the caller, return the known call type
     * (e.g. mobile, home, work).  If there is no associated contact, attempt to use the caller's
     * location if known.
     * @param details Call details to use.
     * @return Type of call (mobile/home) if known, or the location of the caller (if known).
     */
    public CharSequence getCallTypeOrLocation(PhoneCallDetails details) {
        CharSequence numberFormattedLabel = null;
        // Only show a label if the number is shown and it is not a SIP address.
        if (!TextUtils.isEmpty(details.number)
                && !PhoneNumberHelper.isUriNumber(details.number.toString())
                && !mPhoneNumberUtilsWrapper.isVoicemailNumber(details.accountId, details.number)) {

            if (details.numberLabel == ContactInfo.GEOCODE_AS_LABEL) {
                numberFormattedLabel = details.geocode;
            } else {
                numberFormattedLabel = Phone.getTypeLabel(mResources, details.numberType,
                        details.numberLabel);
            }
        }

        if (!TextUtils.isEmpty(details.name) && TextUtils.isEmpty(numberFormattedLabel)) {
            numberFormattedLabel = mPhoneNumberHelper.getDisplayNumber(details.accountId,
                    details.number, details.numberPresentation, details.formattedNumber);
        }
        return numberFormattedLabel;
    }

    /**
     * Get the call date/time of the call, relative to the current time.
     * e.g. 3 minutes ago
     * @param details Call details to use.
     * @return String representing when the call occurred.
     */
    public CharSequence getCallDate(PhoneCallDetails details) {
        return DateUtils.getRelativeTimeSpanString(details.date,
                getCurrentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    /** Sets the text of the header view for the details page of a phone call. */
    public void setCallDetailsHeader(TextView nameView, PhoneCallDetails details) {
        final CharSequence nameText;
        final CharSequence displayNumber =
            mPhoneNumberHelper.getDisplayNumber(details.accountId, details.number,
                        details.numberPresentation,
                        mResources.getString(R.string.recentCalls_addToContact));
        if (TextUtils.isEmpty(details.name)) {
            nameText = displayNumber;
        } else {
            nameText = details.name;
        }

        nameView.setText(nameText);
    }


    public void setCurrentTimeForTest(long currentTimeMillis) {
        mCurrentTimeMillisForTest = currentTimeMillis;
    }

    /**
     * Returns the current time in milliseconds since the epoch.
     * <p>
     * It can be injected in tests using {@link #setCurrentTimeForTest(long)}.
     */
    private long getCurrentTimeMillis() {
        if (mCurrentTimeMillisForTest == null) {
            return System.currentTimeMillis();
        } else {
            return mCurrentTimeMillisForTest;
        }
    }

    /** Sets the call count and date. */
    private void setCallCountAndDate(PhoneCallDetailsViews views, Integer callCount,
            CharSequence dateText) {
        // Combine the count (if present) and the date.
        final CharSequence text;
        if (callCount != null) {
            text = mResources.getString(
                    R.string.call_log_item_count_and_date, callCount.intValue(), dateText);
        } else {
            text = dateText;
        }

        views.callLocationAndDate.setText(text);
    }
}