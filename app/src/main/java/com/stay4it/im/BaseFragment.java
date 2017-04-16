package com.stay4it.im;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.stay4it.im.net.RequestManager;

/**
 * @author Stay
 * @version create time：Apr 12, 2015 11:08:50 AM
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.getInstance().cancel(toString());
    }
}
