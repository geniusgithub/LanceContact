package com.geniusgithub.contact.main;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geniusgithub.contact.R;
import com.geniusgithub.contact.R.layout;
import com.geniusgithub.contact.base.BaseFragment;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public class PersionFragment extends BaseFragment{

	private static final CommonLog log = LogFactory.createLog(PersionFragment.class.getSimpleName());
	

	private View mFragmentContainer;
    public PersionFragment(Context context) {
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mFragmentContainer = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.person_fragment, container, false); 
		mFragmentContainer.buildLayer();
	    return mFragmentContainer;
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

	public  String getClassName(Context context){
		return PersionFragment.class.getSimpleName();
	}



	@Override
	public void onTabSelectedStatusChanged(int status) {
		log.i("onTabSelectedStatusChanged status = " + status);

        if (BaseFragment.TAB_SELECTED == status) {
     
        } else {

        }
	}

}
