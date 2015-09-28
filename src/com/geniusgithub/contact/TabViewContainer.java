package com.geniusgithub.contact;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniusgithub.contact.R;
import com.geniusgithub.contact.base.ITabInterface;

public class TabViewContainer extends ViewGroup implements OnClickListener, ITabInterface{


	
	private final static int CHILD_COUNT = 4;
	private final String TAG = "TabViewContainer";
    private Context mContext;
    
	private View mRootView;
	
	private View[] mTabViews;
	private ImageView[] mImageViews;
	private TextView[] mTextViews;
	
	private ITabInterface mInterface;
	private int mCurrentTabPosition = -1;
   
    public TabViewContainer(Context context) {
        this(context, null);
    }

    public TabViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

   
        mContext = context;
        initWidget(context);
        
    }
    


    private void initWidget(Context context){

        mRootView =  LayoutInflater.from(mContext).inflate(R.layout.tabview_container, null, false);  
    	
    	mTabViews = new View[CHILD_COUNT];
    	mTabViews[0] = mRootView.findViewById(R.id.re_weixin);
    	mTabViews[1] = mRootView.findViewById(R.id.re_contact_list);
    	mTabViews[2] = mRootView.findViewById(R.id.re_find);
    	mTabViews[3] = mRootView.findViewById(R.id.re_profile);
    	
    	mImageViews = new ImageView[CHILD_COUNT];
    	mImageViews[0] = (ImageView) mRootView.findViewById(R.id.ib_weixin);
     	mImageViews[1] = (ImageView) mRootView.findViewById(R.id.ib_contact_list);
     	mImageViews[2] = (ImageView) mRootView.findViewById(R.id.ib_find);
     	mImageViews[3] = (ImageView) mRootView.findViewById(R.id.ib_profile);
     	
     	mTextViews = new TextView[CHILD_COUNT];
     	mTextViews[0] = (TextView) mRootView.findViewById(R.id.tv_weixin);
    	mTextViews[1] = (TextView) mRootView.findViewById(R.id.tv_contact_list);
    	mTextViews[2] = (TextView) mRootView.findViewById(R.id.tv_find);
    	mTextViews[3] = (TextView) mRootView.findViewById(R.id.tv_profile);
    	
    	addView(mRootView); 
    	
    	for (View  view : mTabViews) {
			view.setOnClickListener(this);
		}
    	
  
    }
    
    public void setTabListener(ITabInterface object){
    	mInterface = object;
    }
	 
	  
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measuredWidth = getDefaultSize(0, widthMeasureSpec);
        int measuredHeight = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
        
        int width = getChildMeasureSpec(widthMeasureSpec, 0, measuredWidth);
        int height = getChildMeasureSpec(heightMeasureSpec, 0, measuredHeight);
        
        mRootView.measure(width, height);

    }

	@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
	
        int width = right - left;
        int height = bottom - top ;

        mRootView.layout(0, 0, width, height);
	}

	@Override
	public void onClick(View v) {
	
		switch(v.getId()){
			case R.id.re_weixin:
				setTab(0);
				break;
			case R.id.re_contact_list:
				setTab(1);
				break;
			case R.id.re_find:
				setTab(2);
				break;
			case R.id.re_profile:
				setTab(3);
				break;
		}
	}

	@Override
	public void setTab(int position) {
		if (position >= 0 && position < CHILD_COUNT && mCurrentTabPosition != position){
	
			mImageViews[position].setSelected(true);
			mTextViews[position].setSelected(true);
			if (mCurrentTabPosition != -1){
				mImageViews[mCurrentTabPosition].setSelected(false);
				mTextViews[mCurrentTabPosition].setSelected(false);
			}

			mCurrentTabPosition = position;
			onTabSelect(mCurrentTabPosition);
		}
	}

	@Override
	public void onTabSelect(int position) {
		if (mInterface != null){
			mInterface.onTabSelect(position);
		}
	}

	@Override
	public int getCurrentTabPosition(Context context) {
		return mCurrentTabPosition;
	}
}
