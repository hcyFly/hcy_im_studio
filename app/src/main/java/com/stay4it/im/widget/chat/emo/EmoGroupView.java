package com.stay4it.im.widget.chat.emo;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.stay4it.im.R;
import com.stay4it.im.untils.Trace;

/**
 * @author Stay
 * @version create time：Oct 15, 2014 8:06:42 PM
 */
public class EmoGroupView extends HorizontalScrollView implements OnCheckedChangeListener, OnClickListener {

	private RadioGroup mRadioGroup;
	private Context context;
	private OnEmoGroupChangedListener listener;
	private int totalGroupCount;

	public EmoGroupView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public EmoGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public EmoGroupView(Context context) {
		super(context);
		initializeView(context);
	}

	private void initializeView(Context context) {
		this.context = context;
		mRadioGroup = new RadioGroup(context);
		mRadioGroup.setOnCheckedChangeListener(this);
		mRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
		addView(mRadioGroup);
	}

	public void initializeData(OnEmoGroupChangedListener listener,int defaultCheckedIndex) {
		this.listener = listener;
		totalGroupCount = EmoGroupManager.getInstance().getTotalGroupCount();
		RadioButton radio = null;
		float density = getResources().getDisplayMetrics().density;
		int resId = 0;
		for (int i = 0; i < totalGroupCount; i++) {
			radio = new RadioButton(context);
			radio.setPadding((int)(density * 15), (int)(density * 5), (int)(density * 15), (int)(density * 5));
			resId = EmoGroupManager.getInstance().getEmoGroup(i).resId;
			radio.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
			radio.setButtonDrawable(new BitmapDrawable());
			radio.setBackgroundResource(R.drawable.selector_emo_group);
			radio.setId(i);
			radio.setGravity(Gravity.LEFT);
			if (i == defaultCheckedIndex) {
				radio.setChecked(true);
			}
			radio.setOnClickListener(this);
			mRadioGroup.addView(radio);
		}
	}

	public void notifyDataChanged(int groupIndex) {
		((RadioButton) mRadioGroup.getChildAt(groupIndex)).setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// listener.onGroupChanged(checkedId);
	}

	interface OnEmoGroupChangedListener {
		void onGroupChanged(int position);
	}

	@Override
	public void onClick(View v) {
		Trace.d("onCheckedChanged:" + v.getId());
		listener.onGroupChanged(v.getId());
	}

}
