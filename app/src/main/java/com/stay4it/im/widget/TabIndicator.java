package com.stay4it.im.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.stay4it.im.entities.Tab;
import com.stay4it.im.untils.TextUtil;

import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Apr 11, 2015 8:44:21 PM
 */
public class TabIndicator extends LinearLayout implements OnClickListener {
    private int mTabSize;
    private int mTabIndex = -1;
    private OnTabClickListener listener;
    private final static int ID_PREFIX = 100000;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView();
    }


    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public TabIndicator(Context context) {
        super(context);
        initializeView();
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        this.listener = listener;
    }

    public void initializeData(ArrayList<Tab> tabs) {
        if (!TextUtil.isValidate(tabs)) {
            throw new IllegalArgumentException("the tabs should not be 0");
        }
        mTabSize = tabs.size();
        TabView tab;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.weight = 1;
        for (int i = 0; i < mTabSize; i++) {
            tab = new TabView(getContext());
            tab.setId(ID_PREFIX + i);
            tab.setOnClickListener(this);
            tab.initializeData(tabs.get(i));
            addView(tab, params);
        }
    }

    public void onDataChanged(int index, int number) {
        ((TabView) (findViewById(ID_PREFIX + index))).notifyDataChanged(number);
    }

    private void initializeView() {
        setOrientation(LinearLayout.HORIZONTAL);
    }


    @Override
    public void onClick(View v) {
        int index = v.getId() - ID_PREFIX;
        if (listener != null && mTabIndex != index) {
            listener.onTabClick(v.getId() - ID_PREFIX);
            v.setSelected(true);
            if (mTabIndex != -1) {
                View old = findViewById(ID_PREFIX + mTabIndex);
                old.setSelected(false);
            }
            mTabIndex = index;
        }
    }

    public interface OnTabClickListener {
        void onTabClick(int index);
    }

    public void setCurrentTab(int i) {
        if (i == mTabIndex) {
            return;
        }
        View view = findViewById(ID_PREFIX + i);
        onClick(view);
    }

}
