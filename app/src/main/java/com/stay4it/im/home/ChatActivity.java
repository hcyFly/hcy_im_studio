package com.stay4it.im.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stay4it.im.BaseActivity;
import com.stay4it.im.IMApplication;
import com.stay4it.im.R;
import com.stay4it.im.db.MessageController;
import com.stay4it.im.entities.Attachment;
import com.stay4it.im.entities.Message;
import com.stay4it.im.entities.Message.MessageType;
import com.stay4it.im.entities.Message.StatusType;
import com.stay4it.im.net.AppException;
import com.stay4it.im.net.Request;
import com.stay4it.im.net.Request.RequestMethod;
import com.stay4it.im.net.Request.RequestTool;
import com.stay4it.im.net.RequestManager;
import com.stay4it.im.net.callback.JsonArrayCallback;
import com.stay4it.im.push.IMPushManager;
import com.stay4it.im.push.PushWatcher;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.EmoParser;
import com.stay4it.im.untils.FileUtil;
import com.stay4it.im.untils.IDHelper;
import com.stay4it.im.untils.IOUtilities;
import com.stay4it.im.untils.TextUtil;
import com.stay4it.im.untils.TimeHelper;
import com.stay4it.im.untils.Trace;
import com.stay4it.im.untils.UrlHelper;
import com.stay4it.im.untils.Utils;
import com.stay4it.im.widget.chat.ChatEditorView;
import com.stay4it.im.widget.chat.plugin.PluginEntity;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Mar 16, 2015 3:30:22 PM
 */
public class ChatActivity extends BaseActivity implements OnClickListener, ChatEditorView.OnPluginListener {

	private ListView mChatLsv;
	private ChatAdapter adapter;
	private ArrayList<Message> mChatList;
	private String targetId;
	private String selfId;
	private String targetName;
	private long endTimestamp = 0;
	private PushWatcher watcher = new PushWatcher() {

		@Override
		public void onMessageReceived(Message message) {
			if (!message.getSenderId().equals(targetId)) {
				return;
			}
			if (mChatList == null) {
				mChatList = new ArrayList<Message>();
			}
			mChatList.add(message);
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onMessageUpdated(Message oldMessage, Message newMessage) {
			if (!oldMessage.getReceiverId().equals(targetId)) {
				return;
			}
			if (mChatList == null) {
				mChatList = new ArrayList<Message>();
			}
			int index = mChatList.indexOf(oldMessage);
			if (index == -1) {
				scrollToBottom();
				mChatList.add(oldMessage);
			} else {
				if (newMessage != null) {
					mChatList.remove(index);
					mChatList.add(index, newMessage);
				} else {
					mChatList.remove(index);
					mChatList.add(index, oldMessage);
				}
			}
			adapter.notifyDataSetChanged();
		}

	};
	private Button mChatLoadMoreBtn;
	private String selfName;
	private ChatEditorView mChatPluginView;
	private InputMethodManager mKeyboardManager;
	private Uri tempUri;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_chat);
	}

	@Override
	protected void initializeView() {
		mChatLsv = (ListView) findViewById(R.id.mChatLsv);
		mChatLoadMoreBtn = (Button) findViewById(R.id.mChatLoadMoreBtn);
		mChatPluginView = (ChatEditorView) findViewById(R.id.mChatEditorView);
		mKeyboardManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		mChatPluginView.initializeData(mKeyboardManager);
		mChatPluginView.setOnPluginListener(this);
		mChatLoadMoreBtn.setOnClickListener(this);
//		FIXME here for test
		mChatLoadMoreBtn.setVisibility(View.GONE);
	}

	@Override
	protected void initializeData() {
		IMApplication.getProfile().getAccess_token();
		targetId = getIntent().getStringExtra(Constants.KEY_TARGETID);
		targetName = getIntent().getStringExtra(Constants.KEY_TARGETNAME);
		setTitle(targetName);
		selfId = IMApplication.selfId;
		selfName = IMApplication.getProfile().getName();
		adapter = new ChatAdapter();
		mChatLsv.setAdapter(adapter);
		loadDataFromDB();
		loadDataListFromServer(Constants.REFRESH, endTimestamp);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Trace.d("onResume");
		IMPushManager.getInstance(getApplicationContext()).addObserver(watcher);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Trace.d("onPause");
		IMPushManager.getInstance(getApplicationContext()).removeObserver(watcher);
		MessageController.markAsRead(targetId, selfId);
	}

	private void loadDataFromDB() {
		mChatList = MessageController.queryAllByTimeAsc(targetId, selfId, Constants.REFRESH, 0);
		adapter.notifyDataSetChanged();
		scrollToBottom();
		if (mChatList != null && mChatList.size() > 0) {
			for (int i = mChatList.size() - 1; i >= 0; i--) {
				if (mChatList.get(i).isRead()) {
					endTimestamp = mChatList.get(i).getTimestamp();
					break;
				}
			}
			if (endTimestamp == 0l) {
				endTimestamp = MessageController.queryEndTimestamp(targetId, selfId);
			}
		}
	}

	private void loadDataListFromServer(final int state, final long timestamp) {
		Request request = new Request(UrlHelper.loadAllMsg(targetId, state, timestamp), RequestMethod.GET, RequestTool.HTTPURLCONNECTION);
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", IMApplication.getToken());
		request.setCallback(new JsonArrayCallback<Message>() {
			@Override
			public int retryCount() {
				return 3;
			}

			@Override
			public ArrayList<Message> preRequest() {
				if (state == Constants.LOADMORE) {
					ArrayList<Message> tmp = MessageController.queryAllByTimeAsc(targetId, selfId, state, timestamp);
					if (tmp != null && tmp.size() > 0) {
						return tmp;
					}
				}
				return super.preRequest();
			}

			@Override
			public ArrayList<Message> postRequest(ArrayList<Message> t) {
				MessageController.addOrUpdate(t);
				return MessageController.queryAllByTimeAsc(targetId, selfId, state, timestamp);
			}

			@Override
			public void onSuccess(ArrayList<Message> messages) {
				if (messages != null && messages.size() > 0) {
					if (mChatList == null) {
						mChatList = new ArrayList<Message>();
					}
					if (state == Constants.LOADMORE) {
						mChatList.addAll(0, messages);
						adapter.notifyDataSetChanged();
					} else {
						mChatList.clear();
						mChatList.addAll(messages);
						adapter.notifyDataSetChanged();
						scrollToBottom();
					}
				} else {
					if (state == Constants.LOADMORE) {
						mChatLoadMoreBtn.setEnabled(false);
					}
				}
			}

			@Override
			public void onFailure(AppException exception) {
				exception.printStackTrace();
			}
			
		}.cache(FileUtil.createTmpFile()));
//		request.execute();
		RequestManager.getInstance().execute(toString(), request);
	}
	
	private void scrollToBottom() {
		if (TextUtil.isValidate(mChatList)) {
			mHandler.sendEmptyMessageDelayed(0, 200);
		}
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			mChatLsv.setSelection(mChatList.size() - 1);
		}
	};

	class ChatAdapter extends BaseAdapter {

		private static final int MESSAGE_VIEW_TXT_TO = 0;
		private static final int MESSAGE_VIEW_TXT_FROM = 1;
		private static final int MESSAGE_VIEW_IMG_TO = 2;
		private static final int MESSAGE_VIEW_IMG_FROM = 3;
		private ViewHolder holder;

		@Override
		public int getCount() {
			return mChatList == null ? 0 : mChatList.size();
		}

		@Override
		public Object getItem(int position) {
			return mChatList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			Message message = mChatList.get(position);
			boolean isSelf = message.getSenderId().equals(IMApplication.selfId);
			switch (message.getType()) {
			case txt:
				return isSelf ? MESSAGE_VIEW_TXT_TO : MESSAGE_VIEW_TXT_FROM;
			case emo:
				return isSelf ? MESSAGE_VIEW_IMG_TO : MESSAGE_VIEW_IMG_FROM;
			case multimedia:
				return isSelf ? MESSAGE_VIEW_IMG_TO : MESSAGE_VIEW_IMG_FROM;
			}
			return -1;
		}

		@Override
		public int getViewTypeCount() {
			return 4;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Message message = mChatList.get(position);
			int type = getItemViewType(position);
			if (convertView == null || convertView.getTag() == null) {
				switch (type) {
				case MESSAGE_VIEW_TXT_TO:
					convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.activity_chat_text_out_item, null);
					holder = new TxtToHolder();
					break;
				case MESSAGE_VIEW_TXT_FROM:
					convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.activity_chat_text_in_item, null);
					holder = new TxtFromHolder();
					break;
				case MESSAGE_VIEW_IMG_TO:
					convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.activity_chat_img_out_item, null);
					holder = new ImgToHolder();
					break;
				case MESSAGE_VIEW_IMG_FROM:
					convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.activity_chat_img_in_item, null);
					holder = new ImgFromHolder();
					break;
				default:
					break;
				}
				holder.initializeView(convertView);
				convertView.setTag(holder);
			} else {
				switch (type) {
				case MESSAGE_VIEW_TXT_TO:
					holder = (TxtToHolder) convertView.getTag();
					break;
				case MESSAGE_VIEW_TXT_FROM:
					holder = (TxtFromHolder) convertView.getTag();
					break;
				case MESSAGE_VIEW_IMG_TO:
					holder = (ImgToHolder) convertView.getTag();
					break;
				case MESSAGE_VIEW_IMG_FROM:
					holder = (ImgFromHolder) convertView.getTag();
					break;
				default:
					break;
				}
			}
			holder.initializeData(message);
			return convertView;
		}

		abstract class ViewHolder {
			public abstract void initializeView(View convertView);

			public abstract void initializeData(Message message);
		}

		class TxtToHolder extends ViewHolder {

			private ImageView mChatOutAvatarImg;
			private TextView mChatOutMsgLabel;
			private Button mChatOutMsgResendBtn;
			private ProgressBar mChatOutMsgStatus;
			private TextView mChatTimeLabel;

			@Override
			public void initializeView(View convertView) {
				mChatOutAvatarImg = (ImageView) convertView.findViewById(R.id.mChatOutAvatarImg);
				mChatOutMsgLabel = (TextView) convertView.findViewById(R.id.mChatOutMsgLabel);
				mChatOutMsgResendBtn = (Button) convertView.findViewById(R.id.mChatOutMsgResendBtn);
				mChatOutMsgStatus = (ProgressBar) convertView.findViewById(R.id.mChatOutMsgStatus);
				mChatTimeLabel = (TextView) convertView.findViewById(R.id.mChatTimeLabel);
			}

			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void initializeData(final Message message) {
				mChatOutMsgStatus.setVisibility(View.GONE);
				if (message.getStatus() == StatusType.ing) {
					mChatOutMsgStatus.setVisibility(View.VISIBLE);
				} else if (message.getStatus() == StatusType.fail) {
					mChatOutMsgResendBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							IMPushManager.getInstance(ChatActivity.this).sendMessage(message);
						}
					});
				}

				mChatTimeLabel.setText(TimeHelper.getTimeRule3(message.getTimestamp()));
				if (message.getType() == MessageType.txt) {
					mChatOutMsgLabel.setText(EmoParser.parseEmo(getApplicationContext(), message.getContent()));
				} else {
					String[] emos = message.getContent().split(":");
					String path = FileUtil.getEmoPath(emos[0], emos[1]);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					mChatOutMsgLabel.setText(null);
					mChatOutMsgLabel.setBackground(new BitmapDrawable(bitmap));
				}
			}

		}

		class TxtFromHolder extends ViewHolder {

			private TextView mChatTimeLabel;
			private TextView mChatInMsgLabel;
			private ImageView mChatInAvatarImg;

			@Override
			public void initializeView(View convertView) {
				mChatInAvatarImg = (ImageView) convertView.findViewById(R.id.mChatInAvatarImg);
				mChatInMsgLabel = (TextView) convertView.findViewById(R.id.mChatInMsgLabel);
				mChatTimeLabel = (TextView) convertView.findViewById(R.id.mChatTimeLabel);
			}

			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void initializeData(Message message) {
				mChatTimeLabel.setText(TimeHelper.getTimeRule3(message.getTimestamp()));
				if (message.getType() == MessageType.txt) {
					mChatInMsgLabel.setText(EmoParser.parseEmo(getApplicationContext(), message.getContent()));
				} else {
					String[] emos = message.getContent().split(":");
					String path = FileUtil.getEmoPath(emos[0], emos[1]);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					mChatInMsgLabel.setBackground(new BitmapDrawable(bitmap));
				}
			}

		}

		class ImgToHolder extends ViewHolder {

			private ImageView mChatOutAvatarImg;
			private ImageView mChatOutMsgImg;
			private View mChatOutMsgStatus;
			private TextView mChatTimeLabel;
			private Button mChatOutMsgResendBtn;

			@Override
			public void initializeView(View convertView) {
				mChatOutAvatarImg = (ImageView) convertView.findViewById(R.id.mChatOutAvatarImg);
				mChatOutMsgImg = (ImageView) convertView.findViewById(R.id.mChatOutMsgImg);
				mChatOutMsgStatus = convertView.findViewById(R.id.mChatOutMsgStatus);
				mChatTimeLabel = (TextView) convertView.findViewById(R.id.mChatTimeLabel);
				mChatOutMsgResendBtn = (Button) convertView.findViewById(R.id.mChatOutMsgResendBtn);
			}

			@Override
			public void initializeData(final Message message) {
				mChatTimeLabel.setVisibility(View.VISIBLE);
				mChatTimeLabel.setText(TimeHelper.getTimeRule3(message.getTimestamp()));
				mChatOutMsgStatus.setVisibility(View.GONE);
				if (message.getStatus() == StatusType.ing) {
					mChatOutMsgStatus.setVisibility(View.VISIBLE);
				} else if (message.getStatus() == StatusType.fail) {
					mChatOutMsgResendBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							IMPushManager.getInstance(ChatActivity.this).sendMessage(message);
						}
					});
				}

				if (message.getType() == MessageType.emo) {
					String[] emos = message.getContent().split(":");
					String path = FileUtil.getEmoPath(emos[0], emos[1]);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					mChatOutMsgImg.setImageBitmap(bitmap);
				} else {
					Attachment attachment = message.getAttachments().get(0);
					if (TextUtil.isValidate(attachment.getFile_path())) {
						File file = new File(attachment.getFile_path());
						if (file.exists()) {
							Utils.showImage(attachment.getFile_path(), mChatOutMsgImg, false, true);
						}
					} else {
						Utils.showImage(attachment.getFile_url(), mChatOutMsgImg, false, true);
					}
				}
			}

		}

		class ImgFromHolder extends ViewHolder {

			private ImageView mChatInAvatarImg;
			private ImageView mChatInMsgImg;
			private TextView mChatTimeLabel;

			@Override
			public void initializeView(View convertView) {
				mChatInAvatarImg = (ImageView) convertView.findViewById(R.id.mChatInAvatarImg);
				mChatInMsgImg = (ImageView) convertView.findViewById(R.id.mChatInMsgImg);
				mChatTimeLabel = (TextView) convertView.findViewById(R.id.mChatTimeLabel);
			}

			@Override
			public void initializeData(Message message) {
				mChatTimeLabel.setVisibility(View.VISIBLE);
				mChatTimeLabel.setText(TimeHelper.getTimeRule3(message.getTimestamp()));
				if (message.getType() == MessageType.emo) {
					String[] emos = message.getContent().split(":");
					String path = FileUtil.getEmoPath(emos[0], emos[1]);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					mChatInMsgImg.setImageBitmap(bitmap);
				} else {
					Utils.showImage(message.getAttachments().get(0).getFile_url(), mChatInMsgImg, false, true);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mChatLoadMoreBtn:
			long timestamp = 0;
			if (mChatList != null && mChatList.size() > 0) {
				timestamp = mChatList.get(0).getTimestamp();
			}
			loadDataListFromServer(Constants.LOADMORE, timestamp);
			break;
		default:
			break;
		}
	}

	private void composeMessage(MessageType type, String content) {
		Message message = new Message();
		message.set_id(IDHelper.generateNew());
		message.setContent(content);
		message.setReceiver_name(targetName);
		message.setReceiverId(targetId);
		message.setSender_name(selfName);
		message.setSenderId(selfId);
		message.setContent_type(type);
		message.setTimestamp(System.currentTimeMillis());
		IMPushManager.getInstance(this).sendMessage(message);
	}

	@Override
	public void onSendMsg(CharSequence content) {
		composeMessage(MessageType.txt, content.toString());
	}

	@Override
	public void onSendEmo(String emo) {
		composeMessage(MessageType.emo, emo);
	}

	@Override
	public void onPluginClick(PluginEntity.PluginType plugin) {
		switch (plugin) {
		case Camera:
			Intent imageCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			tempUri = Uri.fromFile(new File(FileUtil.createTmpFile(System.currentTimeMillis() + ".jpg")));
			imageCapture.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
			startActivityForResult(imageCapture, Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			break;
		case Images:
			Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
			mediaChooser.setType("image/*");
			startActivityForResult(mediaChooser, Constants.PICK_FROM_FILE_ACTIVITY_REQUEST_CODE);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Trace.d("onActivityResult");
		IMPushManager.getInstance(getApplicationContext()).addObserver(watcher);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case Constants.PICK_FROM_FILE_ACTIVITY_REQUEST_CODE:
			handleImg(data.getData());
			break;
		case Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			handleImg(tempUri);
			break;
		}
	}

	private void handleImg(Uri uri) {
		Message message = new Message();
		message.set_id(IDHelper.generateNew());
		message.setTimestamp(System.currentTimeMillis());
		message.setContent_type(MessageType.multimedia);
		message.setReceiver_name(targetName);
		message.setReceiverId(targetId);
		// message.setReceiver_picture(mTargetPicture);
		message.setSenderId(selfId);
		message.setSender_name(selfName);
		message.setStatus(StatusType.ing);
		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
		String type = getContentResolver().getType(uri);
		String path = null;
		Attachment attachment = new Attachment();
		if (TextUtil.isValidate(type)) {
			path = FileUtil.getFilePathByUri(this, uri);
			attachment.setFile_type(type);
		} else {
			attachment.setFile_type("image/jpg");
			path = uri.getPath();
		}
		path = IOUtilities.copyAttachmentToPackage(path, type);
		attachment.setFile_path(path);
		attachment.setFile_name(path.substring(path.lastIndexOf("/") + 1));
		attachments.add(attachment);
		message.setAttachments(attachments);
		IMPushManager.getInstance(getApplicationContext()).sendMessage(message);
	}
}
