package com.stay4it.im.untils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/** 
 * @author Stay  
 * @version create time：Sep 15, 2014 3:50:57 PM 
 */
public class JsonParser {
	private static Gson gson = new Gson();
	
	
	public static <T> T deserializeFromJson(String json, Class<T> clz){
		return gson.fromJson(json, clz);
	}
	
	public static <T> T deserializeFromJson(String json, Type type){
		return gson.fromJson(json, type);
	}
	
	
	public static String serializeToJson(Object object){
		return gson.toJson(object);
	}
}
