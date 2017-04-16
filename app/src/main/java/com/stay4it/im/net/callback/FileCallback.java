package com.stay4it.im.net.callback;
/** 
 * @author Stay  
 * @version create time：Sep 15, 2014 12:40:04 PM 
 */
public abstract class FileCallback extends AbstractCallback<String> {
	@Override
	public String bindData(String content) {
		return content;
	}
}
