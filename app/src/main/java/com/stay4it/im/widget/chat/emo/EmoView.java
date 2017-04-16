package com.stay4it.im.widget.chat.emo;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stay4it.im.R;
import com.stay4it.im.untils.Trace;
import com.stay4it.im.widget.chat.emo.EmoGroupView.OnEmoGroupChangedListener;

/** 
 * @author Stay  
 * @version create time：Oct 15, 2014 8:05:01 PM 
 */
public class EmoView extends LinearLayout implements OnClickListener, OnPageChangeListener, OnEmoGroupChangedListener {

	private ViewPager mEmoViewPager;
	private TextView mEmoAddMoreBtn;
	private EmoGroupView mEmoGroupView;
	private EmoDotView mEmoDotView;
	private EmoPagerAdapter adapter;
	private OnEmoClickListener listener;
	private Context context;
	private int groupIndex;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public EmoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public EmoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public EmoView(Context context) {
		super(context);
		initializeView(context);
	}

	private void initializeView(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.widget_emo_container, this);
		mEmoDotView = (EmoDotView)findViewById(R.id.mEmoDotView);
		mEmoAddMoreBtn = (TextView)findViewById(R.id.mEmoAddMoreBtn);
		float density = getResources().getDisplayMetrics().density;
		mEmoAddMoreBtn.setOnClickListener(this);
		mEmoAddMoreBtn.setPadding((int)(density * 15), (int)(density * 5), (int)(density * 15), (int)(density * 5));
		mEmoAddMoreBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat_emo_add_more, 0, 0);
		mEmoAddMoreBtn.setBackgroundResource(R.drawable.selector_emo_group);
		mEmoAddMoreBtn.setGravity(Gravity.LEFT);
		mEmoViewPager = (ViewPager)findViewById(R.id.mEmoViewPager);
		adapter = new EmoPagerAdapter();
		mEmoViewPager.setAdapter(adapter);
		mEmoViewPager.setOnPageChangeListener(this);
		mEmoGroupView = (EmoGroupView)findViewById(R.id.mEmoGroupView);
		mEmoGroupView.initializeData(this,0);
	}
	
	public void initializeData(OnEmoClickListener listener){
		this.listener = listener;
		onPageSelected(0);
	}
	
	class EmoPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
//			the count should be sum(group pages)
			return EmoGroupManager.getInstance().getTotalPage();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			EmoPage page = EmoGroupManager.getInstance().getEmoPage(position);
			EmoGroup group = EmoGroupManager.getInstance().getEmoGroup(page.groupIndex);
			switch (group.type) {
			case normal:
				EmoNormalGrid normal = new EmoNormalGrid(context);
				normal.initializeData(group,page.innerIndex,listener);
				container.addView(normal);
				return normal;
			case custom:
				EmoCustomGrid custom = new EmoCustomGrid(context);
				custom.initializeData(group,page.innerIndex,listener);
				container.addView(custom);
				return custom;
			default:
				return null;
			}
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mEmoAddMoreBtn:
			listener.onEmoAddMoreClick();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int pageIndex) {
		Trace.d("onPageSelected" + pageIndex);
		EmoPage page = EmoGroupManager.getInstance().getEmoPage(pageIndex);
		mEmoDotView.notifyDataChanged(page.innerIndex,page.groupCount);
//		TODO also update group checked state
		if (page.groupIndex != groupIndex) {
			mEmoGroupView.notifyDataChanged(page.groupIndex);
			groupIndex = page.groupIndex;
		}
	}

	@Override
	public void onGroupChanged(int groupIndex) {
		Trace.d("onGroupChanged" + groupIndex);
		EmoGroup group = EmoGroupManager.getInstance().getEmoGroup(groupIndex);
		this.groupIndex = groupIndex;
		mEmoViewPager.setCurrentItem(group.startIndex);
		mEmoDotView.notifyDataChanged(0, group.page);
	}
	
	
	
}
