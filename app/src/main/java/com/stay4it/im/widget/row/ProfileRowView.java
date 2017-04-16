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
public class ProfileRowView extends BaseRowView implements OnClickListener {

	private ImageView mWidgetRowActionImg;
	private OnRowClickListener listener;
	private ProfileRowDescriptor descriptor;
	private ImageView mWidgetRowIconImg;
	private TextView mWidgetRowLabel;
	private TextView mWidgetRowDetailLabel;

	public ProfileRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	public ProfileRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public ProfileRowView(Context context) {
		super(context);
		initializeView();
	}

	private void initializeView() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_profile_row, this);
		mWidgetRowIconImg = (ImageView) findViewById(R.id.mWidgetRowIconImg);
		mWidgetRowLabel = (TextView)findViewById(R.id.mWidgetRowLabel);
		mWidgetRowDetailLabel = (TextView)findViewById(R.id.mWidgetRowDetailLabel);
	}


	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onRowClick(descriptor.action);
		}
	}

	public void initializeData(BaseRowDescriptor descriptor,OnRowClickListener listener) {
		this.descriptor = (ProfileRowDescriptor)descriptor;
		this.listener = listener;
	}

	public void notifyDataChanged() {
		if (descriptor != null) {
			mWidgetRowIconImg.setBackgroundResource(R.drawable.mini_avatar);
			mWidgetRowLabel.setText(descriptor.label);
			mWidgetRowDetailLabel.setText(descriptor.detailLabel);
			if (descriptor.action != null) {
				setOnClickListener(this);
				setBackgroundResource(R.drawable.selector_general_row);
			} else {
				setBackgroundColor(Color.WHITE);
			}
			setVisibility(View.VISIBLE);
		}else {
			setVisibility(View.GONE);
		}
	}
}
