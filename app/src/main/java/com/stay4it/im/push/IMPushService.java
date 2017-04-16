package com.stay4it.im.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.stay4it.im.IMApplication;
import com.stay4it.im.entities.Message;
import com.stay4it.im.entities.Message.StatusType;
import com.stay4it.im.net.AppException;
import com.stay4it.im.net.AppException.ExceptionStatus;
import com.stay4it.im.net.OnUploadProgressChangedListener;
import com.stay4it.im.net.Request;
import com.stay4it.im.net.Request.RequestMethod;
import com.stay4it.im.net.Request.RequestTool;
import com.stay4it.im.net.UploadUtil;
import com.stay4it.im.net.callback.JsonCallback;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.IOUtilities;
import com.stay4it.im.untils.Trace;
import com.stay4it.im.untils.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Stay
 * @version create time：Mar 11, 2015 10:38:03 AM
 */
public class IMPushService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message message = (Message) intent.getSerializableExtra(Constants.KEY_MESSAGE);
		switch (message.getType()) {
		case txt:
		case emo:
			sendPlainMsg(message);
			break;
		case multimedia:
			sendMediaMsg(message);
			break;

		default:
			break;
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void sendPlainMsg(final Message oldMessage) {
		// receiverType: String, receiverId: String, receiverName: String,
		// receiverPicture: Option[String], content: String
		Request request = new Request(UrlHelper.loadSendMsg(), RequestMethod.PUT, RequestTool.HTTPURLCONNECTION);
		request.addHeader("content-type", "application/json");
		request.addHeader("Authorization", IMApplication.getToken());
		try {
			JSONObject json = new JSONObject();
			json.put("receiverType", "single");
			json.put("receiverId", oldMessage.getReceiverId());
			json.put("receiverName", oldMessage.getReceiver_name());
			json.put("content", oldMessage.getContent());
			json.put("contentType", oldMessage.getContent_type());
			request.postContent = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		request.setCallback(new JsonCallback<Message>() {

			@Override
			public void onSuccess(Message newMessage) {
				if (newMessage != null) {
					Trace.d("message send success:" + newMessage.toString());
					newMessage.setStatus(StatusType.done);
					IMPushManager.getInstance(getApplicationContext()).messageUpdated(oldMessage, newMessage);
				} else {
					oldMessage.setStatus(StatusType.fail);
					IMPushManager.getInstance(getApplicationContext()).messageUpdated(oldMessage, null);
				}
			}

			@Override
			public void onFailure(AppException exception) {
				exception.printStackTrace();
				oldMessage.setStatus(StatusType.fail);
				IMPushManager.getInstance(getApplicationContext()).messageUpdated(oldMessage, null);
			}
		});
		request.execute();
	}

	private void sendMediaMsg(final Message oldMessage) {
		Request request = new Request(UrlHelper.loadSendMediaMsg(), RequestMethod.PUT, RequestTool.HTTPURLCONNECTION);
		request.addHeader("Authorization", IMApplication.getToken());
		request.addHeader("Connection", "Keep-Alive");
		request.addHeader("Charset", "UTF-8");
		request.addHeader("Content-Type", "multipart/form-data;boundary=7d4a6d158c9");
		request.setUploadListener(new OnUploadProgressChangedListener() {

			@Override
			public void onProgressUpdate(int curPos, int contentLength) {
				oldMessage.setPercent((int) (curPos * 100l / contentLength));
				IMPushManager.getInstance(getApplicationContext()).messageUpdated(oldMessage.getPercent(), oldMessage);
			}
		});
		request.setCallback(new JsonCallback<Message>() {
			@Override
			public boolean onCustomOutput(OutputStream out, OnUploadProgressChangedListener listener) throws AppException {
				try {
					JSONObject json = new JSONObject();
					json.put("receiverType", "single");
					json.put("receiverId", oldMessage.getReceiverId());
					json.put("receiverName", oldMessage.getReceiver_name());
					json.put("contentType", oldMessage.getContent_type().name());
					json.put("content", oldMessage.getContent());
					String postContent = json.toString();
					UploadUtil.upload(out, postContent, oldMessage.getAttachments(), listener);

				} catch (JSONException e) {
					throw new AppException(ExceptionStatus.ParseJsonException, "upload post content json error");
				}
				return false;
			}
			
			@Override
			public Message postRequest(Message newMessage) {
				String from = oldMessage.getAttachments().get(0).getFile_path();
				String url = newMessage.getAttachments().get(0).getFile_url();
				url = UrlHelper.loadImg(url, false);
				String dest = ImageLoader.getInstance().getDiscCache().get(url).getAbsolutePath();
				try {
					IOUtilities.copyFile(from, dest);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return newMessage;
			}

			@Override
			public void onSuccess(Message newMessage) {
				if (newMessage != null) {
					Trace.d("message send success:" + newMessage.toString());
					newMessage.setStatus(StatusType.done);
					IMPushManager.getInstance(getApplicationContext()).messageUpdated(oldMessage, newMessage);
				} else {
					oldMessage.setStatus(StatusType.fail);
					IMPushManager.getInstance(getApplicationContext()).messageUpdated(oldMessage, null);
				}
			}

			@Override
			public void onFailure(AppException exception) {
				exception.printStackTrace();
				oldMessage.setStatus(StatusType.fail);
				IMPushManager.getInstance(getApplicationContext()).messageUpdated(oldMessage, null);
			}
		});
		request.execute();
	}

}
