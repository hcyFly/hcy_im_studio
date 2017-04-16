package com.stay4it.im.widget.chat.emo;
/** 
 * @author Stay  
 * @version create time：Oct 15, 2014 8:07:08 PM 
 */
public interface OnEmoClickListener {
	void onDeleteButtonClick();
	void onEmoAddMoreClick();
	void onNormalEmoClick(String emo, int resId);
	void onCustomEmoClick(String group_id, String emo_name);
}
