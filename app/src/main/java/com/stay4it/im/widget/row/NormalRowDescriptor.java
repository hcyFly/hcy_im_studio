package com.stay4it.im.widget.row;
/** 
 * @author Stay  
 * @version create time：Oct 10, 2014 3:39:42 PM 
 */
public class NormalRowDescriptor extends BaseRowDescriptor{
	public int iconResId;
	public String label;
	
	public NormalRowDescriptor(int iconResId, String label, RowActionEnum action){
		this.iconResId = iconResId;
		this.label = label;
		this.action = action;
	}

}
