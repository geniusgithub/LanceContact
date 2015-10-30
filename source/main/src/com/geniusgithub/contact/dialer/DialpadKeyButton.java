package com.geniusgithub.contact.dialer;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;


public class DialpadKeyButton extends LinearLayout {
    /** Timeout before switching to long-click accessibility mode. */
    private static final int LONG_HOVER_TIMEOUT = ViewConfiguration.getLongPressTimeout() * 2;

    /** Accessibility manager instance used to check touch exploration state. */
    private AccessibilityManager mAccessibilityManager;

    /** Bounds used to filter HOVER_EXIT events. */
    private Rect mHoverBounds = new Rect();

    /** Whether this view is currently in the long-hover state. */
    private boolean mLongHovered;

    /** Alternate content description for long-hover state. */
    private CharSequence mLongHoverContentDesc;

    /** Backup of standard content description. Used for accessibility. */
    private CharSequence mBackupContentDesc;

    /** Backup of clickable property. Used for accessibility. */
    private boolean mWasClickable;

    /** Backup of long-clickable property. Used for accessibility. */
    private boolean mWasLongClickable;

    /** Runnable used to trigger long-click mode for accessibility. */
    private Runnable mLongHoverRunnable;
    
    private Context mContext;

    public interface OnPressedListener {
        public void onPressed(View view, boolean pressed);
    }

    private OnPressedListener mOnPressedListener;

    public void setOnPressedListener(OnPressedListener onPressedListener) {
        mOnPressedListener = onPressedListener;
    }

    public DialpadKeyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initForAccessibility(context);
    }

    public DialpadKeyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initForAccessibility(context);
    }

    private void initForAccessibility(Context context) {
        mAccessibilityManager = (AccessibilityManager) context.getSystemService(
                Context.ACCESSIBILITY_SERVICE);
    }

    public void setLongHoverContentDescription(CharSequence contentDescription) {
        mLongHoverContentDesc = contentDescription;

        if (mLongHovered) {
            super.setContentDescription(mLongHoverContentDesc);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        super.onLayout(changed, l, t, r, b);
//        Drawable tileBackground = getTileBackground();
//        if (tileBackground instanceof RippleDrawable) {
//            setRipple((RippleDrawable) tileBackground);
//        }
    }
//    private RippleDrawable mRipple;
//    private void setRipple(RippleDrawable tileBackground) {
//        mRipple = tileBackground;
//        if (getWidth() != 0) {
//            updateRippleSize(getMeasuredWidth(), getMeasuredHeight());
//        }
//    }
//    
//    private void updateRippleSize(int width, int height) {
//       
//        // center the touch feedback on the center of the icon, and dial it down a bit
//        final int cx = width / 2;
//        final int cy = height / 2;
//        final int rad = cy;
//        mRipple.setHotspotBounds(cx-rad,cy-rad, cx+rad, cy+rad);
//        setBackground(mRipple);
//        
//    }
//    
//    private Drawable getTileBackground() {
//        final int[] attrs = new int[] { android.R.attr.selectableItemBackgroundBorderless };
//        final TypedArray ta = mContext.obtainStyledAttributes(attrs);
//        final Drawable d = ta.getDrawable(0);
//        ta.recycle();
//        return d;
//    }
    
    @Override
    public void setContentDescription(CharSequence contentDescription) {
        if (mLongHovered) {
            mBackupContentDesc = contentDescription;
        } else {
            super.setContentDescription(contentDescription);
        }
    }
   
    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
       if (mOnPressedListener != null) {
            mOnPressedListener.onPressed(this, pressed);
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHoverBounds.left = getPaddingLeft();
        mHoverBounds.right = w - getPaddingRight();
        mHoverBounds.top = getPaddingTop();
        mHoverBounds.bottom = h - getPaddingBottom();
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if (action == AccessibilityNodeInfo.ACTION_CLICK) {
            simulateClickForAccessibility();
            return true;
        }

        return super.performAccessibilityAction(action, arguments);
    }

    @Override
    public boolean onHoverEvent(MotionEvent event) {
        // When touch exploration is turned on, lifting a finger while inside
        // the button's hover target bounds should perform a click action.
        if (mAccessibilityManager.isEnabled()
                && mAccessibilityManager.isTouchExplorationEnabled()) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    // Lift-to-type temporarily disables double-tap activation.
                    mWasClickable = isClickable();
                    mWasLongClickable = isLongClickable();
                    if (mWasLongClickable && mLongHoverContentDesc != null) {
                        if (mLongHoverRunnable == null) {
                            mLongHoverRunnable = new Runnable() {
                                @Override
                                public void run() {
  
                                    setLongHovered(true);
                                    announceForAccessibility(mLongHoverContentDesc);
                                }
                            };
                        }
                        postDelayed(mLongHoverRunnable, LONG_HOVER_TIMEOUT);
                    }

                    setClickable(false);
                    setLongClickable(false);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    if (mHoverBounds.contains((int) event.getX(), (int) event.getY())) {
                        if (mLongHovered) {
                            performLongClick();
                        } else {
                            simulateClickForAccessibility();
                        }
                    }

                    cancelLongHover();
                    setClickable(mWasClickable);
                    setLongClickable(mWasLongClickable);
                    break;
            }
        }

        return super.onHoverEvent(event);
    }

    /**
     * When accessibility is on, simulate press and release to preserve the
     * semantic meaning of performClick(). Required for Braille support.
     */
    private void simulateClickForAccessibility() {
        // Checking the press state prevents double activation.
        if (isPressed()) {
            return;
        }

        setPressed(true);

        // Stay consistent with performClick() by sending the event after
        // setting the pressed state but before performing the action.
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);

        setPressed(false);
    }

    private void setLongHovered(boolean enabled) {
        if (mLongHovered != enabled) {
            mLongHovered = enabled;

            // Switch between normal and alternate description, if available.
            if (enabled) {
                mBackupContentDesc = getContentDescription();
                super.setContentDescription(mLongHoverContentDesc);
            } else {
                super.setContentDescription(mBackupContentDesc);
            }
        }
    }

    private void cancelLongHover() {
        if (mLongHoverRunnable != null) {
            removeCallbacks(mLongHoverRunnable);
        }
        setLongHovered(false);
    }
}
