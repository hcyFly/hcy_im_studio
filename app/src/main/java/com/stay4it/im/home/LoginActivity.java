package com.stay4it.im.home;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.stay4it.im.BaseActivity;
import com.stay4it.im.IMApplication;
import com.stay4it.im.R;
import com.stay4it.im.entities.Profile;
import com.stay4it.im.net.AppException;
import com.stay4it.im.net.Request;
import com.stay4it.im.net.Request.RequestMethod;
import com.stay4it.im.net.Request.RequestTool;
import com.stay4it.im.net.callback.JsonCallback;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.PrefsAccessor;
import com.stay4it.im.untils.TextUtil;
import com.stay4it.im.untils.Trace;
import com.stay4it.im.untils.UrlHelper;
import com.stay4it.im.widget.EditorView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Stay
 * @version create time：Mar 15, 2015 8:26:44 PM
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditorView mLoginPwdEdt;
	private EditorView mLoginAccountEdt;
	private Button mLoginSubmitBtn;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void initializeView() {
		mLoginAccountEdt = (EditorView) findViewById(R.id.mLoginAccountEdt);
		mLoginPwdEdt = (EditorView) findViewById(R.id.mLoginPwdEdt);
	}

	@Override
	protected void initializeData() {
		mLoginAccountEdt.setText(PrefsAccessor.getInstance(this).getString(Constants.KEY_ACCOUNT));
		mLoginPwdEdt.setText(PrefsAccessor.getInstance(this).getString(Constants.KEY_PASSWORD));
		mLoginSubmitBtn = (Button) findViewById(R.id.mLoginSubmitBtn);
		mLoginSubmitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mLoginSubmitBtn:
			// TODO check valid
			String account = mLoginAccountEdt.getText();
			String pwd = mLoginPwdEdt.getText();
			if (TextUtil.isValidate(account, pwd)) {
				doLogin(account, pwd);
			}
			break;
		default:
			break;
		}
	}

	private void doLogin(final String account, final String pwd) {
		Request request = new Request(UrlHelper.loadLogin(), RequestMethod.POST, RequestTool.HTTPURLCONNECTION);
		JSONObject json = new JSONObject();
		try {
			json.put("account", account);
			json.put("password", pwd);
			json.put("clientId", "android");
			json.put("clientVersion", "1.0.0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		request.addHeader("content-type", "application/json");
		request.postContent = json.toString();

		request.setCallback(new JsonCallback<Profile>() {
			@Override
			public int retryCount() {
				return 3;
			}

			@Override
			public void onSuccess(Profile result) {
				Trace.d("login: " + result.toString());
				IMApplication.setProfile(result);
				IMApplication.mAppState = 1;
				PrefsAccessor.getInstance(LoginActivity.this).saveString(Constants.KEY_ACCOUNT, account);
				PrefsAccessor.getInstance(LoginActivity.this).saveString(Constants.KEY_PASSWORD, pwd);
				goHome();
			}

			@Override
			public void onFailure(AppException exception) {
				exception.printStackTrace();
			}
		});

		request.execute();
	}

	protected void goHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

}
