package com.stay4it.im.widget.row;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.stay4it.im.R;

import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Oct 10, 2014 3:37:07 PM
 */
public class GroupView extends LinearLayout {

	private ArrayList<BaseRowDescriptor> descriptors;
	private OnRowClickListener listener;
	private String title;

	public GroupView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	public GroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public GroupView(Context context) {
		super(context);
		initializeView();
	}

	private void initializeView() {
		setOrientation(VERTICAL);
		setBackgroundResource(android.R.color.transparent);
	}

	public void initializeData(GroupDescriptor descriptor, OnRowClickListener listener) {
		this.descriptors = descriptor.descriptors;
		this.title = descriptor.title;
		this.listener = listener;
	}

	public void notifyDataChanged() {
		if (descriptors != null && descriptors.size() > 0) {
			BaseRowView row = null;
			View line = null;
			float density = getContext().getResources().getDisplayMetrics().density;
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
			params.leftMargin = (int) (10 * density);
			params.rightMargin = (int) (10 * density);
			BaseRowDescriptor descriptor = null;
			int lineColor = getResources().getColor(R.color.widgets_general_row_line);
			for (int i = 0; i < descriptors.size(); i++) {
				descriptor = descriptors.get(i);
				if (descriptor instanceof NormalRowDescriptor) {
					row = new NormalRowView(getContext());
				} else if (descriptor instanceof ProfileRowDescriptor) {
					row = new ProfileRowView(getContext());
				} else if (descriptor instanceof MomentsRowDescriptor) {
					row = new MomentsRowView(getContext());
				}
				row.initializeData(descriptor, listener);
				row.notifyDataChanged();
				addView(row);
				if (i != descriptors.size() - 1) {
					line = new View(getContext());
					line.setBackgroundColor(lineColor);
					// line.setBackgroundResource(R.color.widgets_general_row_line);
					addView(line, params);
				}
			}
			setBackgroundColor(Color.WHITE);
		} else {
			setVisibility(View.GONE);
		}
	}

}
