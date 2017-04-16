package com.stay4it.im.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.stay4it.im.BaseFragment;
import com.stay4it.im.IMApplication;
import com.stay4it.im.R;
import com.stay4it.im.db.ConversationController;
import com.stay4it.im.db.MessageController;
import com.stay4it.im.entities.Conversation;
import com.stay4it.im.entities.Message;
import com.stay4it.im.entities.Message.MessageType;
import com.stay4it.im.net.AppException;
import com.stay4it.im.net.Request;
import com.stay4it.im.net.Request.RequestMethod;
import com.stay4it.im.net.Request.RequestTool;
import com.stay4it.im.net.RequestManager;
import com.stay4it.im.net.callback.JsonCallback;
import com.stay4it.im.push.IMPushManager;
import com.stay4it.im.push.PushWatcher;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.EmoParser;
import com.stay4it.im.untils.RefreshManager;
import com.stay4it.im.untils.TextUtil;
import com.stay4it.im.untils.TimeHelper;
import com.stay4it.im.untils.Trace;
import com.stay4it.im.untils.UrlHelper;

import java.util.ArrayList;

/** 
 * @author Stay  
 * @version create time：Apr 12, 2015 11:09:17 AM 
 */
public class ConversationFragment extends BaseFragment implements OnItemClickListener {
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Trace.d("onActivityCreated");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Trace.d("onAttach");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Trace.d("onDetach");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Trace.d("onResume");
		IMPushManager.getInstance(getActivity().getApplicationContext()).addObserver(watcher);
		loadDataFromDB();
		if (RefreshManager.getInstance().shouldRefresh(getClass())) {
			loadDataListFromServer();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Trace.d("onPause");
		IMPushManager.getInstance(getActivity().getApplicationContext()).removeObserver(watcher);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Trace.d("onStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Trace.d("onStop");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Trace.d("onCreateView");
		View view = inflater.inflate(R.layout.fragment_conversation, null);
		mConversationLsv = (ListView)view.findViewById(R.id.mConversationLsv);
		adapter = new ConversationAdapter();
		mConversationLsv.setAdapter(adapter);
		mConversationLsv.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onDestroyView() {
		Trace.d("onDestroyView");
		super.onDestroyView();
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
				if (TextUtil.isValidate(messages)) {
					for (Message message : messages) {
						MessageController.addOrUpdate(message);
					}
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
//		request.execute();
		RequestManager.getInstance().execute(toString(), request);
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_conversation_item, null);
				mViewHolder.mConversationAvatarImg = (ImageView) convertView.findViewById(R.id.mConversationAvatarImg);
				mViewHolder.mConversationNumTip = (TextView) convertView.findViewById(R.id.mConversationNumTip);
				mViewHolder.mConversationTimestampLabel = (TextView) convertView.findViewById(R.id.mConversationTimestampLabel);
				mViewHolder.mConversationUsernameLabel = (TextView) convertView.findViewById(R.id.mConversationUsernameLabel);
				mViewHolder.mConversationContentLabel = (TextView) convertView.findViewById(R.id.mConversationContentLabel);
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
			if (conversation.getType() == MessageType.txt) {
				mViewHolder.mConversationContentLabel.setText(EmoParser.parseEmo(getActivity(), conversation.getContent()));
			}else if(conversation.getType() == MessageType.emo){
				mViewHolder.mConversationContentLabel.setText(R.string.mChatEmoMessageLabel);
			}else {
				mViewHolder.mConversationContentLabel.setText(R.string.mChatImageMessageLabel);
			}
			mViewHolder.mConversationTimestampLabel.setText(TimeHelper.getTimeRule3(conversation.getTimestamp()));
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
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		Conversation conversation = mConversationList.get(position);
		intent.putExtra(Constants.KEY_TARGETID, conversation.getTargetId());
		intent.putExtra(Constants.KEY_TARGETNAME, conversation.getTargetName());
		startActivity(intent);
	}
	
}
