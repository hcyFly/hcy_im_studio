package com.stay4it.im.widget.row;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/** 
 * @author Stay  
 * @version create time：Oct 10, 2014 4:32:38 PM 
 */
public class ContainerView extends ScrollView {

	private ArrayList<GroupDescriptor> descriptors;
	private OnRowClickListener listener;
	private LinearLayout mRootView;

	public ContainerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	public ContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public ContainerView(Context context) {
		super(context);
		initializeView();
	}
	
	private void initializeView() {
		mRootView = new LinearLayout(getContext());
		mRootView.setOrientation(LinearLayout.VERTICAL);
		float density = getResources().getDisplayMetrics().density;
		mRootView.setPadding(0, 0, 0, (int)(20*density));
		addView(mRootView);
	}
	
	public void initializeData(ArrayList<GroupDescriptor> descriptors,OnRowClickListener listener){
		this.descriptors = descriptors;
		this.listener = listener;
	}

	public void notifyDataChanged() {
		if (descriptors != null && descriptors.size() > 0) {
			GroupView group = null;
			float density = getContext().getResources().getDisplayMetrics().density;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.topMargin = (int)(20*density);
			for (GroupDescriptor descriptor : descriptors) {
				group = new GroupView(getContext());
				group.initializeData(descriptor, listener);
				group.notifyDataChanged();
				mRootView.addView(group,params);
			}
			setVisibility(View.VISIBLE);
		}else {
			setVisibility(View.GONE);
		}
	}
	
}
