package com.stay4it.im.widget.chat.plugin;

import java.util.ArrayList;

import android.content.Context;
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
import android.widget.TextView;

import com.stay4it.im.widget.chat.ChatEditorView.OnPluginListener;

/** 
 * @author Stay  
 * @version create time：Oct 15, 2014 8:06:03 PM 
 */
public class PluginGrid extends GridView implements OnItemClickListener {

	private ArrayList<PluginEntity> entities;
	private int count;
	private Context context;
	private float density;
	private int square;
	private OnPluginListener listener;

	public PluginGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public PluginGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public PluginGrid(Context context) {
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

	public void initializeData(ArrayList<PluginEntity> entities, OnPluginListener listener) {
		this.count = 8;
		this.entities = entities;
		this.listener = listener;
		setAdapter(new EmoAdapter());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		listener.onPluginClick(entities.get(position).type);
	}
	
	class EmoAdapter extends BaseAdapter{

		private ViewHolder holder;

		@Override
		public int getCount() {
			return entities.size();
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
				layout.setOrientation(LinearLayout.VERTICAL);
				holder.mPluginImg = new ImageView(context);
				holder.mPluginImg.setLayoutParams(new LayoutParams(square,square));
				holder.mPluginLabel = new TextView(context);
				holder.mPluginLabel.setGravity(Gravity.CENTER);
				layout.addView(holder.mPluginImg);
				layout.addView(holder.mPluginLabel);
				convertView = layout;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			PluginEntity entity = entities.get(position);
			holder.mPluginImg.setImageResource(entity.pluginImgResId);
			holder.mPluginLabel.setText(entity.pluginNameResId);
			return convertView;
		}
	}
	
	static class ViewHolder {
		public TextView mPluginLabel;
		public ImageView mPluginImg;
	}
	
	
}
