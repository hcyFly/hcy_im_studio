package com.stay4it.im.net;

import com.stay4it.im.net.callback.ICallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stay
 * @version create time：Sep 15, 2014 11:44:26 AM
 */
public class Request {
	public enum RequestMethod {
		GET, POST, DELETE, PUT
	}
	public enum RequestTool {
		HTTPCLIENT, HTTPURLCONNECTION
	}

	public RequestMethod method;
	public String url;
	public Map<String, String> headers;
	public ICallback callback;
	public IRequestListener listener;
	private RequestTask task;
	public RequestTool tool;
	public String postContent;
	public OnUploadProgressChangedListener uploadListener;
	public OnUploadProgressChangedListener tmpUploadLister;
	
	public Request(String url, RequestMethod method) {
		this.url = url;
		this.method = method;
		this.tool = RequestTool.HTTPCLIENT;
	}
	
	public Request(String url, RequestMethod method, RequestTool tool) {
		this.url = url;
		this.method = method;
		this.tool = tool;
	}

	public Request(String url) {
		this.url = url;
		this.method = RequestMethod.GET;
		this.tool = RequestTool.HTTPURLCONNECTION;
	}

	public void setCallback(ICallback callback) {
		this.callback = callback;
	}

	public void addHeader(String key, String value) {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put(key, value);
	}
	
	public void setRequestListener(IRequestListener listener){
		this.listener = listener;
	}

	public void execute() {
		task = new RequestTask(this);
		task.execute();
//		task.executeOnExecutor(exec, params);
	}

	public void cancel(boolean force) {
		if (force && task != null) {
			task.cancel(true);
		}
		if (callback != null) {
			callback.cancel(force);
		}
	}

	public void setUploadListener(OnUploadProgressChangedListener onUploadProgressChangedListener) {
		this.uploadListener = onUploadProgressChangedListener;
	}

}
