package com.stay4it.im.widget.chat.plugin;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stay4it.im.R;
import com.stay4it.im.widget.chat.ChatEditorView.OnPluginListener;
import com.stay4it.im.widget.chat.emo.EmoDotView;
import com.stay4it.im.widget.chat.plugin.PluginEntity.PluginType;

import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Oct 15, 2014 8:05:01 PM
 */
public class PluginView extends LinearLayout implements OnPageChangeListener {

	private ViewPager mEmoViewPager;
	private EmoDotView mEmoDotView;
	private EmoPagerAdapter adapter;
	private Context context;
	private OnPluginListener listener;
	private int pageCount = 1;
	private int pageIndex;
	private ArrayList<PluginEntity> entities;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public PluginView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public PluginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public PluginView(Context context) {
		super(context);
		initializeView(context);
	}

	private void initializeView(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.widget_plugin_container, this);
		mEmoDotView = (EmoDotView) findViewById(R.id.mEmoDotView);
		mEmoViewPager = (ViewPager) findViewById(R.id.mEmoViewPager);
		adapter = new EmoPagerAdapter();
		mEmoViewPager.setAdapter(adapter);
		mEmoViewPager.setOnPageChangeListener(this);
	}

	public void initializeData(OnPluginListener listener) {
		this.listener = listener;
		entities = new ArrayList<PluginEntity>();
		entities.add(new PluginEntity(R.string.mChatPluginImagesLabel,R.drawable.ic_chat_plugin_images,PluginType.Images));
		entities.add(new PluginEntity(R.string.mChatPluginCameraLabel,R.drawable.ic_chat_plugin_camera,PluginType.Camera));
	}

	class EmoPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageCount;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			PluginGrid custom = new PluginGrid(context);
			custom.initializeData(entities, listener);
			container.addView(custom);
			return custom;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
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
		mEmoDotView.notifyDataChanged(pageIndex, pageCount);
		this.pageIndex = pageIndex;
	}

}
