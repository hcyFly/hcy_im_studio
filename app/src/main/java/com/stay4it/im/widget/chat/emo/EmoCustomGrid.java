package com.stay4it.im.widget.chat.emo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stay4it.im.untils.FileUtil;

import java.util.ArrayList;

/** 
 * @author Stay  
 * @version create time：Oct 15, 2014 8:06:03 PM 
 */
public class EmoCustomGrid extends GridView implements OnItemClickListener {

	private ArrayList<EmoEntity> entities;
	private EmoGroup group;
	private int count;
	private Context context;
	private OnEmoClickListener listener;
	private int innerPageIndex;
	private float density;
	private int square;

	public EmoCustomGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public EmoCustomGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public EmoCustomGrid(Context context) {
		super(context);
		initializeView(context);
	}

	private void initializeView(Context context) {
		this.context = context;
		density = context.getResources().getDisplayMetrics().density;
		setNumColumns(4);
		setVerticalSpacing((int) (density * 15));
		square = (int)(density * 50);
		setOnItemClickListener(this);
		setGravity(Gravity.CENTER);
		setSelector(new BitmapDrawable());
	}

	public void initializeData(EmoGroup group, int position, OnEmoClickListener listener) {
		this.group = group;
		this.count = 8;
		this.listener = listener;
		this.innerPageIndex = position;
		entities = EmoUtil.getEmoEntities(group);
		setAdapter(new EmoAdapter());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		listener.onCustomEmoClick(group.name, entities.get(innerPageIndex * 8 + position).emo_name);
	}
	
	class EmoAdapter extends BaseAdapter{

		private ViewHolder holder;

		@Override
		public int getCount() {
			return 8;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				LinearLayout layout = new LinearLayout(context);
				layout.setGravity(Gravity.CENTER);
				holder.emoticon = new ImageView(context);
				holder.emoticon.setLayoutParams(new LayoutParams(square,square));
				layout.addView(holder.emoticon);
				convertView = layout;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String path = FileUtil.getEmoPath(group.name, entities.get(innerPageIndex * 8 + position).emo_name);
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			holder.emoticon.setImageBitmap(bitmap);
			return convertView;
		}
	}
	
	static class ViewHolder {
		public ImageView emoticon;
	}
	
	
}
