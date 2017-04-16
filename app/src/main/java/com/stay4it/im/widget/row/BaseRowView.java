package com.stay4it.im.widget.row;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/** 
 * @author Stay  
 * @version create time：Oct 10, 2014 4:54:42 PM 
 */
public abstract class BaseRowView extends LinearLayout {
	

	public BaseRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BaseRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseRowView(Context context) {
		super(context);
	}
	
	public abstract void initializeData(BaseRowDescriptor descriptor,OnRowClickListener listener);
	
	public abstract void notifyDataChanged();
	
}
