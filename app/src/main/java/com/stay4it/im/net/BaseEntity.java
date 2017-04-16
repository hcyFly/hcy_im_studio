package com.stay4it.im.net;

import com.google.gson.stream.JsonReader;


/** 
 * @author Stay  
 * @version create time：Sep 16, 2014 12:47:44 PM 
 */
public abstract class BaseEntity {
	public abstract void readFromJson(JsonReader reader) throws AppException;
}
