package com.stay4it.im.widget.row;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.stay4it.im.R;


/**
 * @author Stay
 * @version create time：Oct 10, 2014 12:25:34 PM
 */
public class MomentsRowView extends BaseRowView implements OnClickListener {

	private ImageView mWidgetRowLatestImg;
	private TextView mWidgetRowLabel;
	private ImageView mWidgetRowIconImg;
	private OnRowClickListener listener;
	private MomentsRowDescriptor descriptor;

	public MomentsRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	public MomentsRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public MomentsRowView(Context context) {
		super(context);
		initializeView();
	}

	private void initializeView() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_moments_row, this);
		mWidgetRowIconImg = (ImageView) findViewById(R.id.mWidgetRowIconImg);
		mWidgetRowLabel = (TextView) findViewById(R.id.mWidgetRowLabel);
		mWidgetRowLatestImg = (ImageView) findViewById(R.id.mWidgetRowLatestImg);
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onRowClick(descriptor.action);
		}
	}

	public void initializeData(BaseRowDescriptor descriptor, OnRowClickListener listener) {
		this.descriptor = (MomentsRowDescriptor) descriptor;
		this.listener = listener;
	}

	public void notifyDataChanged() {
		if (descriptor != null) {
			mWidgetRowIconImg.setBackgroundResource(descriptor.iconResId);
			mWidgetRowLabel.setText(descriptor.label);
			if (descriptor.latestUrl != null) {
				mWidgetRowLatestImg.setBackgroundResource(R.drawable.mini_avatar);
			}
			if (descriptor.action != null) {
				setOnClickListener(this);
				setBackgroundResource(R.drawable.selector_general_row);
			} else {
				setBackgroundColor(Color.WHITE);
			}
		} else {
			setVisibility(View.GONE);
		}
	}

}
