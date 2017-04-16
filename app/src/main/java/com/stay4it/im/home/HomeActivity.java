package com.stay4it.im.home;

import android.content.Intent;

import com.stay4it.im.BaseActivity;
import com.stay4it.im.BaseFragment;
import com.stay4it.im.R;
import com.stay4it.im.entities.Tab;
import com.stay4it.im.push.IMPushManager;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.widget.TabIndicator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Mar 10, 2015 5:31:42 PM
 */
public class HomeActivity extends BaseActivity implements TabIndicator.OnTabClickListener {

	private TabIndicator mHomeIndicator;
	private ArrayList<Tab> tabs;
	private BaseFragment mCurrentFragment;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getBooleanExtra(Constants.KEY_FORCE_QUIT, false)) {
			finish();
		}
//		else if (intent.getBooleanExtra(Constants.KEY_PROTECT_APP, false)) {
//			protectApp();
//		}
	}

	@Override
	protected void protectApp() {
		Intent intent = new Intent(this, WelcomeActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_home);
	}

	@Override
	protected void initializeView() {
		mHomeIndicator = (TabIndicator) findViewById(R.id.mHomeIndicator);
		mHomeIndicator.setOnTabClickListener(this);
		tabs = new ArrayList<Tab>();
		tabs.add(new Tab(R.drawable.selector_tab_msg, R.string.mTabMsgLabel, ConversationFragment.class));
		tabs.add(new Tab(R.drawable.selector_tab_contact, R.string.mTabContactLabel, ContactFragment.class));
		tabs.add(new Tab(R.drawable.selector_tab_moments, R.string.mTabMomentsLabel, MomentsFragment.class));
		tabs.add(new Tab(R.drawable.selector_tab_profile, R.string.mTabProfileLabel, ProfileFragment.class));
		mHomeIndicator.initializeData(tabs);
		mHomeIndicator.setCurrentTab(0);
	}

	@Override
	protected void initializeData() {
		IMPushManager.getInstance(this).startPush();
	}

	@Override
	public void onTabClick(int index) {
		// TODO
		try {
			Constructor<? extends BaseFragment> con = tabs.get(index).getFragment().getConstructor();
			mCurrentFragment = (BaseFragment) con.newInstance();
			getSupportFragmentManager().beginTransaction().replace(R.id.mHomeFrame, mCurrentFragment).commitAllowingStateLoss();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
