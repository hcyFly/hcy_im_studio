package com.stay4it.im.widget.row;
/** 
 * @author Stay  
 * @version create time：Oct 10, 2014 3:39:42 PM 
 */
public class MomentsRowDescriptor extends BaseRowDescriptor{
	public int iconResId;
	public String label;
	public String latestUrl;
	
	public MomentsRowDescriptor(int iconResId, String label,String latestUrl, RowActionEnum action){
		this.iconResId = iconResId;
		this.label = label;
		this.action = action;
		this.latestUrl = latestUrl;
	}

}
