package com.geniusgithub.contact.dialer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.geniusgithub.contact.R;
import com.geniusgithub.contact.base.BaseFragment;
import com.geniusgithub.contact.dialer.DialpadView.IDigitKeyListener;
import com.geniusgithub.contact.dialer.DialpadView.KeyDigit;
import com.geniusgithub.contact.util.AnimUtils;
import com.geniusgithub.contact.util.CallUtil;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;
import com.geniusgithub.contact.util.PhoneNumberFormatter;

public class DialpadFragment extends BaseFragment implements TextWatcher, IDigitKeyListener, OnClickListener, OnLongClickListener{

	private static final CommonLog log = LogFactory.createLog(DialpadFragment.class.getSimpleName());
	private View mFragmentContainer;
	private ViewGroup mDialpadContainer;
	private DialpadView mDigitDialpad;						
	private EditText mDigitsEditText;		
	  
	private ImageButton mSingCardBtn;
	private ImageView mDeleteBtn;
	private ImageView mAddContactBtn;
	  
    private Animation mSlideIn;
    private Animation mSlideOut;
    
    
    
	
    public DialpadFragment(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mContext = activity;

		log.i("onAttach");
	}

	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		  	mSlideIn = AnimationUtils.loadAnimation(mContext, R.anim.dialpad_slide_in_bottom);
	        mSlideOut = AnimationUtils.loadAnimation(mContext, R.anim.dialpad_slide_out_bottom);
	        mSlideIn.setInterpolator(AnimUtils.EASE_IN);
	        mSlideOut.setInterpolator(AnimUtils.EASE_OUT);
	        mSlideIn.setAnimationListener(new AnimationListener() {
	            
	            @Override
	            public void onAnimationStart(Animation animation) {
	            	
	            }
	            
	            @Override
	            public void onAnimationRepeat(Animation animation) {
	                
	            }
	            
	            @Override
	            public void onAnimationEnd(Animation animation) {
	                mDialpadContainer.setVisibility(View.VISIBLE);
	                
	            }
	        });
	        mSlideOut.setAnimationListener(new AnimationListener() {
	            
	            @Override
	            public void onAnimationStart(Animation animation) {
	               
	            }
	            
	            @Override
	            public void onAnimationRepeat(Animation animation) {
	              
	            }
	            
	            @Override
	            public void onAnimationEnd(Animation animation) {
	                mDialpadContainer.setVisibility(View.GONE);
	                
	            }
	        });
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mFragmentContainer = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.dialpad_fragment, container, false); 
		mFragmentContainer.buildLayer();
		
	    return mFragmentContainer;
	}
	
   @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {    
	   mDialpadContainer = (ViewGroup) mFragmentContainer.findViewById(R.id.dialpad_container);
	   mDigitDialpad = (DialpadView) mFragmentContainer.findViewById(R.id.dialpad_view);
	   mDigitDialpad.setKeyListener(this);
	   
	   mDigitsEditText = mDigitDialpad.getDigits();	
       mDigitsEditText.addTextChangedListener(this);
       mDigitsEditText.setKeyListener(DialerKeyListener.getInstance());
       mDigitsEditText.setElegantTextHeight(false);
       mDigitsEditText.setCursorVisible(true);
       PhoneNumberFormatter.setPhoneNumberFormattingTextWatcher(getActivity(), mDigitsEditText);
       
       mSingCardBtn = (ImageButton) mFragmentContainer.findViewById(R.id.dialpad_single_card);
       mSingCardBtn.setOnClickListener(this);
       
       mDeleteBtn = (ImageView) mFragmentContainer.findViewById(R.id.deleteButton);
       mDeleteBtn.setOnClickListener(this);
       mDeleteBtn.setOnLongClickListener(this);
       
       mAddContactBtn = (ImageView) mFragmentContainer.findViewById(R.id.addContactButton);
       mAddContactBtn.setOnClickListener(this);
   }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		log.i("onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void toggleDialpad(Context context){
		
		if (mDialpadContainer.isShown()){
			showDialpad(false);
		}else{
			showDialpad(true);
		}
	}
	
	public void showDialpad(boolean show){

		if (show){
			mDialpadContainer.startAnimation(mSlideIn);
		}else{
			mDialpadContainer.startAnimation(mSlideOut);
		}
	
	}
	
	public void showBottomButton(boolean show){
		if (show){
			mDeleteBtn.setVisibility(View.VISIBLE);
			mAddContactBtn.setVisibility(View.VISIBLE);
		}else{
			mDeleteBtn.setVisibility(View.GONE);
			mAddContactBtn.setVisibility(View.GONE);
		}
	}
	
    private boolean isDigitsEmpty() {
        return mDigitsEditText.length() == 0;
    }
	  
	private void cleanDigits() {
	        mDigitsEditText.getText().clear();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {

		mDigitDialpad.showDigitContainer(!isDigitsEmpty());
		showBottomButton(!isDigitsEmpty());
	
	}

	@Override
	public void onKeyPress(KeyDigit key) {
		int keyCode = getKeyCode(key);

		if (keyCode != -1){
			KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
		    mDigitsEditText.onKeyDown(keyCode, event);
		}

	}
	
	public int getKeyCode(KeyDigit key){
		int keyCode = -1;
		switch(key){
		 	case KEY_ONE:
		 		keyCode = KeyEvent.KEYCODE_1;
		     	break;
		     case KEY_TWO:
		    	 keyCode = KeyEvent.KEYCODE_2;
		     	break;
		     case KEY_THREE:
		    	 keyCode = KeyEvent.KEYCODE_3;
		     	break;
		     case KEY_FOUR:
		    	 keyCode = KeyEvent.KEYCODE_4;
		     	break;
		     case KEY_FIVE:
		    	 keyCode = KeyEvent.KEYCODE_5;
		     	break;
		     case KEY_SIX:
		    	 keyCode = KeyEvent.KEYCODE_6;
		     	break;
		     case KEY_SEVEN:
		    	 keyCode = KeyEvent.KEYCODE_7;
		     	break;
		     case KEY_EIGHT:
		    	 keyCode = KeyEvent.KEYCODE_8;
		     	break;
		     case KEY_NIGHT:
		    	 keyCode = KeyEvent.KEYCODE_9;
		     	break;
		     case KEY_START:
		    	 keyCode = KeyEvent.KEYCODE_STAR;
		    	 break;
		     case KEY_ZERO:
		    	 keyCode = KeyEvent.KEYCODE_0;  	 
		    	 break;
		     case KEY_POUND:
		    	 keyCode = KeyEvent.KEYCODE_POUND;
		    	 break;
//		     case KEY_DELETE:
//		    	 keyCode = KeyEvent.KEYCODE_DEL;
		     default:
		    	 break;
		}

		return keyCode;
	}

	@Override
	public void onKeyClick(KeyDigit key) {
		int keyCode = getKeyCode(key);
	
		if (keyCode != -1){
			if (keyCode == KeyEvent.KEYCODE_DEL){
				KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
			    mDigitsEditText.onKeyDown(keyCode, event);
			}
		}
	}

	@Override
	public void onKeyLongClick(KeyDigit key) {
		int keyCode = getKeyCode(key);

		if (keyCode != -1){
			if (keyCode == KeyEvent.KEYCODE_DEL){
				cleanDigits();
			}
		}
	}
	

	
	
	private void handleCallEvent(Context context){
		  String number = mDigitsEditText.getText().toString();
          
		  if (TextUtils.isEmpty(number)){
			  return ;
		  }

		  makeCall(context, number, -1);
          cleanDigits();
	}
	
    public static void makeCall(Context context, String number, int slotID) {

        Intent intent = CallUtil.getCallIntent(context, number, slotID);
        context.startActivity(intent);

    }



	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.dialpad_single_card:
			handleCallEvent(mContext);
			break;
		case R.id.deleteButton:
			log.d("mDigitsEditText onKeyDown");
			mDigitsEditText.onKeyDown(KeyEvent.KEYCODE_DEL, new KeyEvent(KeyEvent.ACTION_DOWN,  KeyEvent.KEYCODE_DEL));
			break;
		case R.id.addContactButton:
			
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
			case R.id.deleteButton:
				cleanDigits();
				break;
		}
		
		return false;
	}

}
