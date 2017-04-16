package com.stay4it.im.net;

import java.util.ArrayList;
import java.util.HashMap;

/** 
 * @author Stay  
 * @version create time：Sep 15, 2014 9:32:09 PM 
 */
public class RequestManager {
	private static RequestManager mInstance;
	private HashMap<String, ArrayList<Request>> mRequestCache = null;
	private RequestManager(){
		mRequestCache = new HashMap<String, ArrayList<Request>>();
	}
	
	public static RequestManager getInstance(){
		if (mInstance == null) {
			mInstance = new RequestManager();
		}
		return mInstance;
	}
	
	public void cancel(String key){
		ArrayList<Request> requests = mRequestCache.remove(key);
		if (requests != null && requests.size() > 0) {
			while (requests.size() > 0) {
				requests.remove(0).cancel(true);
			}
		}
	}
	
	public void cancelAll(){
		
		
	}
	
	public void execute(String key,Request request){
		ArrayList<Request> requests = mRequestCache.get(key);
		if (requests == null) {
			requests = new ArrayList<Request>();
			mRequestCache.put(key, requests);
		}
		requests.add(request);
		request.execute();
	}
	
	
	
}
