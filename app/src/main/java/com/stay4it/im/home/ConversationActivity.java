package com.stay4it.im.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stay4it.im.BaseActivity;
import com.stay4it.im.IMApplication;
import com.stay4it.im.R;
import com.stay4it.im.db.ConversationController;
import com.stay4it.im.db.MessageController;
import com.stay4it.im.entities.Conversation;
import com.stay4it.im.entities.Message;
import com.stay4it.im.net.AppException;
import com.stay4it.im.net.Request;
import com.stay4it.im.net.Request.RequestMethod;
import com.stay4it.im.net.Request.RequestTool;
import com.stay4it.im.net.callback.JsonCallback;
import com.stay4it.im.push.IMPushManager;
import com.stay4it.im.push.PushWatcher;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.UrlHelper;

import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Mar 16, 2015 3:30:22 PM
 */
public class ConversationActivity extends BaseActivity implements OnItemClickListener {

	private ListView mConversationLsv;
	private ConversationAdapter adapter;
	private ArrayList<Conversation> mConversationList;
	private PushWatcher watcher = new PushWatcher(){

		@Override
		public void onMessageReceived(Message message) {
			Conversation conversation = message.copyTo();
			mConversationList.remove(conversation);
			mConversationList.add(0,conversation);
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onMessageUpdated(Message oldMessage, Message newMessage) {
			Conversation conversation = oldMessage.copyTo();
			mConversationList.remove(conversation);
			if (newMessage != null) {
				mConversationList.add(0,newMessage.copyTo());
			}else {
				mConversationList.add(conversation);
			}
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_conversation);
	}

	@Override
	protected void initializeView() {
		mConversationLsv = (ListView) findViewById(R.id.mConversationLsv);
		mConversationLsv.setOnItemClickListener(this);
	}

	@Override
	protected void initializeData() {
		IMPushManager.getInstance(this).startPush();
		adapter = new ConversationAdapter();
		mConversationLsv.setAdapter(adapter);
		loadDataFromDB();
		loadDataListFromServer();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IMPushManager.getInstance(getApplicationContext()).addObserver(watcher);
		loadDataFromDB();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		IMPushManager.getInstance(getApplicationContext()).removeObserver(watcher);
	}

	private void loadDataFromDB() {
		mConversationList = ConversationController.queryAllByTimeDesc();
		adapter.notifyDataSetChanged();
	}

	private void loadDataListFromServer() {
		Request request = new Request(UrlHelper.loadConversation(), RequestMethod.GET, RequestTool.HTTPURLCONNECTION);
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", IMApplication.getToken());
		request.setCallback(new JsonCallback<ArrayList<Message>>() {
			@Override
			public ArrayList<Message> postRequest(ArrayList<Message> messages) {
				for (Message message : messages) {
					MessageController.addOrUpdate(message);
				}
				return messages;
			}

			@Override
			public void onSuccess(ArrayList<Message> messages) {
				if (messages != null && messages.size() > 0) {
					loadDataFromDB();
				}
			}

			@Override
			public void onFailure(AppException exception) {
				exception.printStackTrace();
			}
		});
		request.execute();
	}

	class ConversationAdapter extends BaseAdapter {

		private ViewHolder mViewHolder;

		@Override
		public int getCount() {
			return mConversationList == null ? 0 : mConversationList.size();
		}

		@Override
		public Object getItem(int position) {
			return mConversationList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null || convertView.getTag() == null) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(ConversationActivity.this).inflate(R.layout.activity_conversation_item, null);
				mViewHolder.mConversationAvatarImg = (ImageView) convertView.findViewById(R.id.mConversationAvatarImg);
				mViewHolder.mConversationNumTip = (TextView) convertView.findViewById(R.id.mConversationNumTip);
				mViewHolder.mConversationTimestampLabel = (TextView) convertView.findViewById(R.id.mConversationTimestampLabel);
				mViewHolder.mConversationUsernameLabel = (TextView) convertView.findViewById(R.id.mConversationUsernameLabel);
				mViewHolder.mConversationContentLabel = (TextView) convertView.findViewById(R.id.mConversationContentLabel);
				mViewHolder.mConversationStatusLabel = (TextView) convertView.findViewById(R.id.mConversationStatusLabel);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			Conversation conversation = mConversationList.get(position);
			if (conversation.getUnreadNum() == 0) {
				mViewHolder.mConversationNumTip.setVisibility(View.INVISIBLE);
			}else {
				mViewHolder.mConversationNumTip.setVisibility(View.VISIBLE);
				mViewHolder.mConversationNumTip.setText(String.valueOf(conversation.getUnreadNum()));
			}
			mViewHolder.mConversationUsernameLabel.setText(conversation.getTargetName());
			mViewHolder.mConversationContentLabel.setText(conversation.getContent());
			return convertView;
		}

	}

	static class ViewHolder {
		TextView mConversationStatusLabel;
		TextView mConversationContentLabel;
		TextView mConversationUsernameLabel;
		TextView mConversationTimestampLabel;
		TextView mConversationNumTip;
		ImageView mConversationAvatarImg;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, ChatActivity.class);
		Conversation conversation = mConversationList.get(position);
		intent.putExtra(Constants.KEY_TARGETID, conversation.getTargetId());
		intent.putExtra(Constants.KEY_TARGETNAME, conversation.getTargetName());
		startActivity(intent);
	}

}
