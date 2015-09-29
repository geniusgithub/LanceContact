package com.geniusgithub.contact.dialer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.geniusgithub.contact.R;
import com.geniusgithub.contact.base.BaseFragment;
import com.geniusgithub.contact.util.AnimUtils;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public class DialpadFragment extends BaseFragment{

	private static final CommonLog log = LogFactory.createLog(DialpadFragment.class.getSimpleName());
	private View mFragmentContainer;
	private ViewGroup mDialpadContainer;
	private DialpadView mDigitDialpad;						

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
			hideDialpad(context);
		}else{
			showDialpad(context);
		}
	}
	
	public void showDialpad(Context context){
	//	mDigitDialpad.animateShow();
		mDialpadContainer.startAnimation(mSlideIn);
	}
	
	public void hideDialpad(Context context){

		mDialpadContainer.startAnimation(mSlideOut);
	       
	}
	


}
