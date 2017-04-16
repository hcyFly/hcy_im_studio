package com.stay4it.im.widget.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.stay4it.im.R;
import com.stay4it.im.widget.chat.emo.EmoView;
import com.stay4it.im.widget.chat.emo.OnEmoClickListener;
import com.stay4it.im.widget.chat.plugin.PluginEntity.PluginType;
import com.stay4it.im.widget.chat.plugin.PluginView;

/** 
 * @author Stay  
 * @version create time：Apr 17, 2015 11:31:18 AM 
 */
public class ChatEditorView extends RelativeLayout implements OnClickListener, OnEmoClickListener, TextWatcher {

	private OnPluginListener listener;
	private EditText mChatEdt;
	private Button mChatSendBtn;
	private EmoView mChatEmoView;
	private ImageButton mChatEmoBtn;
	private InputMethodManager mKeyboardManager;
	private int mEmoSize;
	private PluginView mChatPluginView;
	private ImageButton mChatPluginBtn;

	public ChatEditorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	public ChatEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public ChatEditorView(Context context) {
		super(context);
		initializeView();
	}
	
	
	private void initializeView() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_chat_editor_pannel, this);
		mChatEdt = (EditText)findViewById(R.id.mChatEdt);
		mChatEdt.addTextChangedListener(this);
		mChatEmoBtn = (ImageButton) findViewById(R.id.mChatEmoBtn);
		mChatEmoBtn.setOnClickListener(this);
		mChatEmoBtn.setBackgroundResource(R.drawable.ic_chat_editor_emo);
		mChatPluginBtn = (ImageButton) findViewById(R.id.mChatPluginBtn);
		mChatPluginBtn.setOnClickListener(this);
		mChatSendBtn = (Button)findViewById(R.id.mChatSendBtn);
		mChatSendBtn.setOnClickListener(this);
		mChatEmoView = (EmoView)findViewById(R.id.mChatEmoView);
		mChatEmoView.initializeData(this);
		mChatPluginView = (PluginView)findViewById(R.id.mChatPluginView);
		float density = getResources().getDisplayMetrics().density;
		mEmoSize = (int)(25*density);
	}
	
	public void initializeData(InputMethodManager manager){
		this.mKeyboardManager = manager;
	}


	public interface OnPluginListener{
		void onSendMsg(CharSequence content);
		void onSendEmo(String content);
		void onPluginClick(PluginType plugin);
	}

	public void setOnPluginListener(OnPluginListener listener) {
		this.listener = listener;
		mChatPluginView.initializeData(listener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mChatSendBtn:
			if (mChatEdt.getText().toString() != null) {
				listener.onSendMsg(mChatEdt.getText());
				mChatEdt.setText(null);
				mKeyboardManager.hideSoftInputFromWindow(mChatEdt.getWindowToken(), 0);
			}
			break;
		case R.id.mChatEmoBtn:
			if (mChatEmoView.getVisibility() == View.GONE) {
				mKeyboardManager.hideSoftInputFromWindow(mChatEdt.getWindowToken(), 0);
				mChatEmoView.setVisibility(View.VISIBLE);
				mChatPluginView.setVisibility(View.GONE);
				mChatEmoBtn.setBackgroundResource(R.drawable.ic_chat_editor_emo_h);
			} else {
				mChatEmoView.setVisibility(View.GONE);
				mChatEmoBtn.setBackgroundResource(R.drawable.ic_chat_editor_emo);
			}
			break;
		case R.id.mChatPluginBtn:
			if (mChatPluginView.getVisibility() == View.GONE) {
				mKeyboardManager.hideSoftInputFromWindow(mChatEdt.getWindowToken(), 0);
				mChatEmoView.setVisibility(View.GONE);
				mChatPluginView.setVisibility(View.VISIBLE);
				mChatEmoBtn.setBackgroundResource(R.drawable.ic_chat_editor_emo);
			}else {
				mChatPluginView.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onDeleteButtonClick() {
		mChatEdt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
		mChatEdt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
	}

	@Override
	public void onEmoAddMoreClick() {
//		TODO jump to emoticon store
	}

	@Override
	public void onNormalEmoClick(String emo, int resId) {
		Editable editable = mChatEdt.getText();
		int index = mChatEdt.getSelectionEnd();
		emo = "["+emo+"]";
		SpannableStringBuilder builder = new SpannableStringBuilder(emo);
		Drawable d = getResources().getDrawable(resId);
		d.setBounds(0, 0, mEmoSize, mEmoSize);
		ImageSpan span = new ImageSpan(d);
		builder.setSpan(span, 0, emo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (index < mChatEdt.length()) {
			editable.insert(index, builder);
		}else {
			editable.append(builder);
		}
		mChatEdt.setSelection(index + emo.length());
	}

	@Override
	public void onCustomEmoClick(String group_id, String emo_name) {
		listener.onSendEmo(group_id + ":" + emo_name);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() > 0) {
			mChatSendBtn.setVisibility(View.VISIBLE);
			mChatPluginBtn.setVisibility(View.GONE);
		}else {
			mChatPluginBtn.setVisibility(View.VISIBLE);
			mChatSendBtn.setVisibility(View.GONE);
		}
	}
	
}
