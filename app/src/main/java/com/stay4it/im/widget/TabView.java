package com.stay4it.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stay4it.im.R;
import com.stay4it.im.entities.Tab;

/** 
 * @author Stay  
 * @version create time：Apr 11, 2015 8:44:52 PM 
 */
public class TabView extends RelativeLayout {

	private TextView mTabInfoLabel;
	private ImageView mTabIconImg;

	public TabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public TabView(Context context) {
		super(context);
		initializeView();
	}

	private void initializeView() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_tab, this);
		mTabIconImg = (ImageView)findViewById(R.id.mTabIconImg);
		mTabInfoLabel = (TextView)findViewById(R.id.mTabInfoLabel);
	}

	public void initializeData(Tab tab) {
		mTabIconImg.setImageResource(tab.getIconResId());
		mTabInfoLabel.setText(tab.getInfoResId());
	}

	public void notifyDataChanged(int number) {
		
	}
	
}
