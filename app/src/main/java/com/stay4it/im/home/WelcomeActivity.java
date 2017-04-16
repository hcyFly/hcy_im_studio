package com.stay4it.im.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.stay4it.im.BaseActivity;
import com.stay4it.im.IMApplication;
import com.stay4it.im.R;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.PrefsAccessor;
import com.stay4it.im.untils.TextUtil;

/** 
 * @author Stay  
 * @version create time：Apr 8, 2015 2:47:04 PM 
 */
public class WelcomeActivity extends BaseActivity implements OnClickListener {
	private Button select_login_btn;
	private Button select_register_btn;
	private View select_lv;
	private static final int ACTION_LOGIN = 0;
	private static final int ACTION_HOME = 1;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent();
			switch (msg.what) {
			case ACTION_LOGIN:
				intent.setClass(WelcomeActivity.this, LoginActivity.class);
				break;
			case ACTION_HOME:
				intent.setClass(WelcomeActivity.this, HomeActivity.class);
				break;
			default:
				break;
			}
			startActivity(intent);
			finish();
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		IMApplication.mAppState = 0;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_welcome);
	}

	@Override
	protected void initializeView() {
		select_lv = findViewById(R.id.select_lv);
		select_login_btn = (Button)findViewById(R.id.select_login_btn);
		select_register_btn = (Button)findViewById(R.id.select_register_btn);
		select_login_btn.setOnClickListener(this);
		select_register_btn.setOnClickListener(this);
	}

	@Override
	protected void initializeData() {
		String account = PrefsAccessor.getInstance(this).getString(Constants.KEY_ACCOUNT);
		String pwd = PrefsAccessor.getInstance(this).getString(Constants.KEY_PASSWORD);
		if (TextUtil.isValidate(account,pwd)) {
			IMApplication.initializeProfile();
			if (IMApplication.getProfile() == null) {
				mHandler.sendEmptyMessageDelayed(ACTION_LOGIN, 2000);
			}else {
				IMApplication.mAppState = 1;
				mHandler.sendEmptyMessageDelayed(ACTION_HOME, 2000);
			}
		}else if(TextUtil.isValidate(account)){
			mHandler.sendEmptyMessageDelayed(ACTION_LOGIN, 2000);
		}else {
			select_lv.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_login_btn:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		case R.id.select_register_btn:
			
			break;
		default:
			break;
		}
	}

}
