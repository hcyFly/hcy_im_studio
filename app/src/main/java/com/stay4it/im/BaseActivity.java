package com.stay4it.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stay4it.im.home.HomeActivity;
import com.stay4it.im.net.RequestManager;
import com.stay4it.im.untils.Constants;

/**
 * @author Stay
 * @version create time：Mar 15, 2015 8:24:15 PM
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(IMApplication.mAppState != -1){
            setContentView();
            initializeView();
            initializeData();
        }else {
            protectApp();
        }
    }

    protected void protectApp() {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra(Constants.KEY_PROTECT_APP, true);
        startActivity(intent);
        finish();
    }

    protected abstract void setContentView();

    protected abstract void initializeView();

    protected abstract void initializeData();

    @Override
    protected void onResume() {
        super.onResume();
        // TODO umeng
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestManager.getInstance().cancel(toString());
    }
}
