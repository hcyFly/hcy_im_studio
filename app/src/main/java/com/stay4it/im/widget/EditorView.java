package com.stay4it.im.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stay4it.im.R;

/**
 * @author Stay
 * @version create time：Apr 8, 2015 8:43:01 PM
 */
public class EditorView extends RelativeLayout implements OnClickListener {

	private TextView mWidgetEditorInfoLabel;
	private EditText mWidgetEditorEdt;
	private Button mWdigetEditorClearBtn;

	public EditorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context, attrs);
	}

	public EditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context, attrs);
	}

	public EditorView(Context context) {
		super(context);
		initializeView(context, null);
	}

	private void initializeView(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.widget_login_editor, this);
		mWidgetEditorInfoLabel = (TextView) findViewById(R.id.mWidgetEditorInfoLabel);
		mWidgetEditorEdt = (EditText) findViewById(R.id.mWidgetEditorEdt);
		mWdigetEditorClearBtn = (Button) findViewById(R.id.mWdigetEditorClearBtn);
		mWdigetEditorClearBtn.setOnClickListener(this);
		parseAttr(context, attrs);
		mWidgetEditorEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					mWdigetEditorClearBtn.setVisibility(View.VISIBLE);
				} else {
					mWdigetEditorClearBtn.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void parseAttr(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.editor);
		if (a.hasValue(R.styleable.editor_hint)) {
			mWidgetEditorEdt.setHint(a.getString(R.styleable.editor_hint));
		}
		if (a.hasValue(R.styleable.editor_android_inputType)) {
			mWidgetEditorEdt.setInputType(a.getInt(R.styleable.editor_android_inputType, InputType.TYPE_CLASS_TEXT));
		}
		if (a.hasValue(R.styleable.editor_android_gravity)) {
			mWidgetEditorEdt.setGravity(a.getInt(R.styleable.editor_android_gravity, Gravity.LEFT));
		}
		if (a.hasValue(R.styleable.editor_android_minEms)) {
			mWidgetEditorEdt.setMinEms(a.getInt(R.styleable.editor_android_minEms, -1));
		}
		if (a.hasValue(R.styleable.editor_android_maxEms)) {
			mWidgetEditorEdt.setMaxEms(a.getInt(R.styleable.editor_android_maxEms, Integer.MAX_VALUE));
		}
		if (a.hasValue(R.styleable.editor_text)) {
			mWidgetEditorInfoLabel.setText(a.getString(R.styleable.editor_text));
		}
		if (a.hasValue(R.styleable.editor_android_singleLine)) {
			mWidgetEditorEdt.setSingleLine(a.getBoolean(R.styleable.editor_android_singleLine, true));
		}
		a.recycle();
	}

	public String getText() {
		return mWidgetEditorEdt.getText().toString();
	}

	public void setText(String content) {
		mWidgetEditorEdt.setText(content);
	}

	@Override
	public void onClick(View v) {
		mWidgetEditorEdt.setText(null);
	}

}
