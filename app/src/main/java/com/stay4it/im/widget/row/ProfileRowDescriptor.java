package com.stay4it.im.widget.row;
/** 
 * @author Stay  
 * @version create time：Oct 10, 2014 4:50:50 PM 
 */
public class ProfileRowDescriptor extends BaseRowDescriptor{
	public String imgUrl;
	public String label;
	public String detailLabel;
	
	public ProfileRowDescriptor(String imgUrl, String label, String detailLabel,RowActionEnum action) {
		this.imgUrl = imgUrl;
		this.label = label;
		this.detailLabel = detailLabel;
		this.action = action;
	}
	
	
}
