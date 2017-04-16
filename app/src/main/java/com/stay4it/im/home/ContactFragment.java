package com.stay4it.im.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stay4it.im.BaseFragment;

/** 
 * @author Stay  
 * @version create time：Apr 12, 2015 11:09:17 AM 
 */
public class ContactFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TextView textView = new TextView(getActivity().getApplicationContext());
		textView.setText("Contact");
		return textView;
	}
}
