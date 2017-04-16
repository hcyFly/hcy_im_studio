package com.stay4it.im.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Stay
 * @version create time：Oct 8, 2015 3:26:51 PM
 */
public class SpreadActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView contentLabel = new TextView(this);
		Intent intent = getIntent();
		if (intent != null) {
			contentLabel.setText(intent.toString()+"\n"+intent.getDataString());
		}
		
		setContentView(contentLabel);
	}

}
