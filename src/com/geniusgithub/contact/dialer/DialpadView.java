/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.geniusgithub.contact.dialer;

import java.util.Locale;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewPropertyAnimator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geniusgithub.contact.R;
import com.geniusgithub.contact.dialer.DialpadKeyButton.OnPressedListener;
import com.geniusgithub.contact.util.AnimUtils;

/**
 * View that displays a twelve-key phone dialpad.
 */
public class DialpadView extends LinearLayout implements OnClickListener, OnPressedListener, OnLongClickListener{
	
	public static enum KeyDigit{
		KEY_EMPTY,
		KEY_ONE,
		KEY_TWO,
		KEY_THREE,
		KEY_FOUR,
		KEY_FIVE,
		KEY_SIX,
		KEY_SEVEN,
		KEY_EIGHT,
		KEY_NIGHT,
		KEY_ZERO,
		KEY_START,
		KEY_POUND,
		KEY_DELETE
	}
	public static interface IDigitKeyListener{
		public void onKeyPress(KeyDigit key);
		public void onKeyClick(KeyDigit key);
		public void onKeyLongClick(KeyDigit key);
	}
	
    private static final String TAG = DialpadView.class.getSimpleName();

    private static final double DELAY_MULTIPLIER = 0.66;
    private static final double DURATION_MULTIPLIER = 0.8;

    /**
     * {@code True} if the dialpad is in landscape orientation.
     */
    private final boolean mIsLandscape;

    /**
     * {@code True} if the dialpad is showing in a right-to-left locale.
     */
    private final boolean mIsRtl;

    private EditText mDigits;
    private ImageButton mDelete;
//    private View mOverflowMenuButton;
    private ColorStateList mRippleColor;

    private boolean mCanDigitsBeEdited;

    private final int[] mButtonIds = new int[] {R.id.zero, R.id.one, R.id.two, R.id.three,
            R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.star,
            R.id.pound};

    // For animation.
    private static final int KEY_FRAME_DURATION = 33;

    private int mTranslateDistance;
    
    private IDigitKeyListener mDigitKeyListener;
    

    public DialpadView(Context context) {
        this(context, null);
    }

    public DialpadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialpadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Dialpad);
        mRippleColor = a.getColorStateList(R.styleable.Dialpad_dialpad_key_button_touch_tint);
        a.recycle();

        mTranslateDistance = getResources().getDimensionPixelSize(
                R.dimen.dialpad_key_button_translate_y);

        mIsLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
        mIsRtl = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) ==
                View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    protected void onFinishInflate() {
        setupKeypad();
        mDigits = (EditText) findViewById(R.id.et_inputdigits);
        mDelete = (ImageButton) findViewById(R.id.deleteButton);
        
        mDelete.setOnClickListener(this);
        mDelete.setOnLongClickListener(this);
//        mOverflowMenuButton = findViewById(R.id.dialpad_overflow);
    }

    private void setupKeypad() {
        final int[] numberIds = new int[] {R.string.dialpad_0_number, R.string.dialpad_1_number,
                R.string.dialpad_2_number, R.string.dialpad_3_number, R.string.dialpad_4_number,
                R.string.dialpad_5_number, R.string.dialpad_6_number, R.string.dialpad_7_number,
                R.string.dialpad_8_number, R.string.dialpad_9_number, R.string.dialpad_star_number,
                R.string.dialpad_pound_number};

        final int[] letterIds = new int[] {R.string.dialpad_0_letters, R.string.dialpad_1_letters,
                R.string.dialpad_2_letters, R.string.dialpad_3_letters, R.string.dialpad_4_letters,
                R.string.dialpad_5_letters, R.string.dialpad_6_letters, R.string.dialpad_7_letters,
                R.string.dialpad_8_letters, R.string.dialpad_9_letters,
                R.string.dialpad_star_letters, R.string.dialpad_pound_letters};

        final Resources resources = getContext().getResources();
        DialpadKeyButton dialpadKey;
        TextView numberView;
        TextView lettersView;

        for (int i = 0; i < mButtonIds.length; i++) {
            dialpadKey = (DialpadKeyButton) findViewById(mButtonIds[i]);
           // dialpadKey.setOnClickListener(this);
            dialpadKey.setOnLongClickListener(this);
            dialpadKey.setOnPressedListener(this);
            numberView = (TextView) dialpadKey.findViewById(R.id.dialpad_key_number);
            numberView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            lettersView = (TextView) dialpadKey.findViewById(R.id.dialpad_key_letters);
            final String numberString = resources.getString(numberIds[i]);
            final RippleDrawable rippleBackground =
                    (RippleDrawable) getContext().getDrawable(R.drawable.btn_dialpad_key);
            if (mRippleColor != null) {
                rippleBackground.setColor(mRippleColor);
            }
   
          //  rippleBackground.setMaxRadius(resources.getDimensionPixelSize(R.dimen.dialer_ripple_radius));
            numberView.setText(numberString);
            numberView.setElegantTextHeight(false);
            dialpadKey.setContentDescription(numberString);
            dialpadKey.setBackground(rippleBackground);

            if (lettersView != null) {
                lettersView.setText(resources.getString(letterIds[i]));
            }
        }

        final DialpadKeyButton one = (DialpadKeyButton) findViewById(R.id.one);
        final DialpadKeyButton zero = (DialpadKeyButton) findViewById(R.id.zero);


    }
    
    public void setKeyListener(IDigitKeyListener listener){
    	mDigitKeyListener = listener;
    }
 
//    public void setShowVoicemailButton(boolean show) {
//        View view = findViewById(R.id.dialpad_key_voicemail);
//        if (view != null) {
//            view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        }
//    }

    /**
     * Whether or not the digits above the dialer can be edited.
     *
     * @param canBeEdited If true, the backspace button will be shown and the digits EditText
     *         will be configured to allow text manipulation.
     */
    public void setCanDigitsBeEdited(boolean canBeEdited) {
        View deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(canBeEdited ? View.VISIBLE : View.GONE);
//        View overflowMenuButton = findViewById(R.id.dialpad_overflow);
//        overflowMenuButton.setVisibility(canBeEdited ? View.VISIBLE : View.GONE);

        EditText digits = (EditText) findViewById(R.id.et_inputdigits);
        digits.setClickable(canBeEdited);
        digits.setLongClickable(canBeEdited);
        digits.setFocusableInTouchMode(canBeEdited);
        digits.setCursorVisible(false);

        mCanDigitsBeEdited = canBeEdited;
    }

    public boolean canDigitsBeEdited() {
        return mCanDigitsBeEdited;
    }

    /**
     * Always returns true for onHoverEvent callbacks, to fix problems with accessibility due to
     * the dialpad overlaying other fragments.
     */
    @Override
    public boolean onHoverEvent(MotionEvent event) {
        return true;
    }

    public void animateShow() {
        // This is a hack; without this, the setTranslationY is delayed in being applied, and the
        // numbers appear at their original position (0) momentarily before animating.
        final AnimatorListenerAdapter showListener = new AnimatorListenerAdapter() {};

        for (int i = 0; i < mButtonIds.length; i++) {
            int delay = (int)(getKeyButtonAnimationDelay(mButtonIds[i]) * DELAY_MULTIPLIER);
            int duration =
                    (int)(getKeyButtonAnimationDuration(mButtonIds[i]) * DURATION_MULTIPLIER);
            final DialpadKeyButton dialpadKey = (DialpadKeyButton) findViewById(mButtonIds[i]);

            ViewPropertyAnimator animator = dialpadKey.animate();
            if (mIsLandscape) {
                // Landscape orientation requires translation along the X axis.
                // For RTL locales, ensure we translate negative on the X axis.
                dialpadKey.setTranslationX((mIsRtl ? -1 : 1) * mTranslateDistance);
                animator.translationX(0);
            } else {
                // Portrait orientation requires translation along the Y axis.
                dialpadKey.setTranslationY(mTranslateDistance);
                animator.translationY(0);
            }
            animator.setInterpolator(AnimUtils.EASE_OUT_EASE_IN)
                    .setStartDelay(delay)
                    .setDuration(duration)
                    .setListener(showListener)
                    .start();
        }
    }

    public EditText getDigits() {
        return mDigits;
    }

    public ImageButton getDeleteButton() {
        return mDelete;
    }

//    public View getOverflowMenuButton() {
//        return mOverflowMenuButton;
//    }

    /**
     * Get the animation delay for the buttons, taking into account whether the dialpad is in
     * landscape left-to-right, landscape right-to-left, or portrait.
     *
     * @param buttonId The button ID.
     * @return The animation delay.
     */
    private int getKeyButtonAnimationDelay(int buttonId) {
        if (mIsLandscape) {
            if (mIsRtl) {
                switch (buttonId) {
                    case R.id.three: return KEY_FRAME_DURATION * 1;
                    case R.id.six: return KEY_FRAME_DURATION * 2;
                    case R.id.nine: return KEY_FRAME_DURATION * 3;
                    case R.id.pound: return KEY_FRAME_DURATION * 4;
                    case R.id.two: return KEY_FRAME_DURATION * 5;
                    case R.id.five: return KEY_FRAME_DURATION * 6;
                    case R.id.eight: return KEY_FRAME_DURATION * 7;
                    case R.id.zero: return KEY_FRAME_DURATION * 8;
                    case R.id.one: return KEY_FRAME_DURATION * 9;
                    case R.id.four: return KEY_FRAME_DURATION * 10;
                    case R.id.seven:
                    case R.id.star:
                        return KEY_FRAME_DURATION * 11;
                }
            } else {
                switch (buttonId) {
                    case R.id.one: return KEY_FRAME_DURATION * 1;
                    case R.id.four: return KEY_FRAME_DURATION * 2;
                    case R.id.seven: return KEY_FRAME_DURATION * 3;
                    case R.id.star: return KEY_FRAME_DURATION * 4;
                    case R.id.two: return KEY_FRAME_DURATION * 5;
                    case R.id.five: return KEY_FRAME_DURATION * 6;
                    case R.id.eight: return KEY_FRAME_DURATION * 7;
                    case R.id.zero: return KEY_FRAME_DURATION * 8;
                    case R.id.three: return KEY_FRAME_DURATION * 9;
                    case R.id.six: return KEY_FRAME_DURATION * 10;
                    case R.id.nine:
                    case R.id.pound:
                        return KEY_FRAME_DURATION * 11;
                }
            }
        } else {
            switch (buttonId) {
                case R.id.one: return KEY_FRAME_DURATION * 1;
                case R.id.two: return KEY_FRAME_DURATION * 2;
                case R.id.three: return KEY_FRAME_DURATION * 3;
                case R.id.four: return KEY_FRAME_DURATION * 4;
                case R.id.five: return KEY_FRAME_DURATION * 5;
                case R.id.six: return KEY_FRAME_DURATION * 6;
                case R.id.seven: return KEY_FRAME_DURATION * 7;
                case R.id.eight: return KEY_FRAME_DURATION * 8;
                case R.id.nine: return KEY_FRAME_DURATION * 9;
                case R.id.star: return KEY_FRAME_DURATION * 10;
                case R.id.zero:
                case R.id.pound:
                    return KEY_FRAME_DURATION * 11;
            }
        }

        Log.wtf(TAG, "Attempted to get animation delay for invalid key button id.");
        return 0;
    }

    /**
     * Get the button animation duration, taking into account whether the dialpad is in landscape
     * left-to-right, landscape right-to-left, or portrait.
     *
     * @param buttonId The button ID.
     * @return The animation duration.
     */
    private int getKeyButtonAnimationDuration(int buttonId) {
        if (mIsLandscape) {
            if (mIsRtl) {
                switch (buttonId) {
                    case R.id.one:
                    case R.id.four:
                    case R.id.seven:
                    case R.id.star:
                        return KEY_FRAME_DURATION * 8;
                    case R.id.two:
                    case R.id.five:
                    case R.id.eight:
                    case R.id.zero:
                        return KEY_FRAME_DURATION * 9;
                    case R.id.three:
                    case R.id.six:
                    case R.id.nine:
                    case R.id.pound:
                        return KEY_FRAME_DURATION * 10;
                }
            } else {
                switch (buttonId) {
                    case R.id.one:
                    case R.id.four:
                    case R.id.seven:
                    case R.id.star:
                        return KEY_FRAME_DURATION * 10;
                    case R.id.two:
                    case R.id.five:
                    case R.id.eight:
                    case R.id.zero:
                        return KEY_FRAME_DURATION * 9;
                    case R.id.three:
                    case R.id.six:
                    case R.id.nine:
                    case R.id.pound:
                        return KEY_FRAME_DURATION * 8;
                }
            }
        } else {
            switch (buttonId) {
                case R.id.one:
                case R.id.two:
                case R.id.three:
                case R.id.four:
                case R.id.five:
                case R.id.six:
                    return KEY_FRAME_DURATION * 10;
                case R.id.seven:
                case R.id.eight:
                case R.id.nine:
                    return KEY_FRAME_DURATION * 9;
                case R.id.star:
                case R.id.zero:
                case R.id.pound:
                    return KEY_FRAME_DURATION * 8;
            }
        }

        Log.wtf(TAG, "Attempted to get animation duration for invalid key button id.");
        return 0;
    }

	@Override
	public void onClick(View view) {
		KeyDigit value = getKey(view);	
		
		if (mDigitKeyListener != null){
			mDigitKeyListener.onKeyClick(value);
		}
	}

	@Override
	public void onPressed(View view, boolean pressed) {
		if (!pressed){
			return ;
		}
		KeyDigit value = getKey(view);	
		
		if (mDigitKeyListener != null){
			mDigitKeyListener.onKeyPress(value);
		}
	}
	
	@Override
	public boolean onLongClick(View view) {
		KeyDigit value = getKey(view);
		if (mDigitKeyListener != null){
			mDigitKeyListener.onKeyLongClick(value);
		}
		return false;
	}
	
	private KeyDigit getKey(View view){
		KeyDigit value = KeyDigit.KEY_EMPTY;
		switch(view.getId()){
	        case R.id.one:
	        	value = KeyDigit.KEY_ONE;
	        	break;
	        case R.id.two:
	          	value = KeyDigit.KEY_TWO;
	        	break;
	        case R.id.three:
	          	value = KeyDigit.KEY_THREE;
	        	break;
	        case R.id.four:
	          	value = KeyDigit.KEY_FOUR;
	        	break;
	        case R.id.five:
	          	value = KeyDigit.KEY_FIVE;
	        	break;
	        case R.id.six:
	          	value = KeyDigit.KEY_SIX;
	        	break;
	        case R.id.seven:
	          	value = KeyDigit.KEY_SEVEN;
	        	break;
	        case R.id.eight:
	          	value = KeyDigit.KEY_EIGHT;
	        	break;
	        case R.id.nine:
	          	value = KeyDigit.KEY_NIGHT;
	        	break;
	        case R.id.star:
	          	value = KeyDigit.KEY_START;
	        	break;
	        case R.id.zero:
	           	value = KeyDigit.KEY_ZERO;
	        	break;
	        case R.id.pound:
	           	value = KeyDigit.KEY_POUND;
	        	break;
	        case R.id.deleteButton:
	        	value = KeyDigit.KEY_DELETE;
	        	break;
			}
		return value;
	}


}
