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
public class NormalRowView extends BaseRowView implements OnClickListener {

	private ImageView mWidgetRowActionImg;
	private TextView mWidgetRowLabel;
	private ImageView mWidgetRowIconImg;
	private OnRowClickListener listener;
	private NormalRowDescriptor descriptor;

	public NormalRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	public NormalRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public NormalRowView(Context context) {
		super(context);
		initializeView();
	}

	private void initializeView() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_general_row, this);
		mWidgetRowLabel = (TextView) findViewById(R.id.mWidgetRowLabel);
		mWidgetRowIconImg = (ImageView) findViewById(R.id.mWidgetRowIconImg);
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onRowClick(descriptor.action);
		}
	}

	public void initializeData(BaseRowDescriptor descriptor, OnRowClickListener listener) {
		this.descriptor = (NormalRowDescriptor) descriptor;
		this.listener = listener;
	}

	public void notifyDataChanged() {
		if (descriptor != null) {
			mWidgetRowIconImg.setBackgroundResource(descriptor.iconResId);
			mWidgetRowLabel.setText(descriptor.label);
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
