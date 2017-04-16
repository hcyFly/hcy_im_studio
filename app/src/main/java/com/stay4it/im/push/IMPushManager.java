package com.stay4it.im.push;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stay4it.im.R;
import com.stay4it.im.entities.Message;
import com.stay4it.im.home.WelcomeActivity;
import com.stay4it.im.untils.Constants;
import com.stay4it.im.untils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Stay
 * @version create time：Mar 11, 2015 10:29:12 AM
 */
public class IMPushManager {
	private static IMPushManager mInstance;
	public Context context;
	private Gson gson = new Gson();

	public IMPushManager(Context context) {
		this.context = context;
	}

	public static IMPushManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new IMPushManager(context);
		}
		return mInstance;
	}

	public void messageUpdated(Message oldMessage, Message newMessage) {
		PushChanger.getInstance().notifyChanged(oldMessage, newMessage);
	}
	
	public void messageUpdated(int percent,Message message) {
		PushChanger.getInstance().notifyChanged(percent, message);
	}
	
	

	public void handlePush(String content) {
		try {
			JSONObject json = new JSONObject(content);
			JSONObject msg = json.optJSONObject("message_content");
			if (msg != null) {
				Message message = gson.fromJson(msg.toString(), Message.class);
				PushChanger.getInstance().notifyChanged(message);
				if (!isAppInForeground()) {
					startNotification(message.getSender_name() + ":" + message.getContent());
				}
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Message message) {
		message.setStatus(Message.StatusType.ing);
		messageUpdated(message, null);
		Intent service = new Intent(context, IMPushService.class);
		service.putExtra(Constants.KEY_MESSAGE, message);
		context.startService(service);
	}

	public void addObserver(PushWatcher watcher) {
		PushChanger.getInstance().addObserver(watcher);
	}

	public void removeObserver(PushWatcher watcher) {
		PushChanger.getInstance().deleteObserver(watcher);
	}

	public void removeObservers() {
		PushChanger.getInstance().deleteObservers();
	}

	public void startPush() {
		PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(context, "api_key"));
//		 PushManager.setTags(this, mCurrentUser.getPushTags());
	}
	
	public void stopPush(){
		PushManager.stopWork(context);
	}
	
	public boolean isAppInForeground() {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Application.ACTIVITY_SERVICE);
		PowerManager manager = (PowerManager) context.getSystemService(Activity.POWER_SERVICE);
		RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);
		ComponentName topComponent = info.topActivity;
		if (info.numRunning > 0 && manager.isScreenOn() && topComponent != null && topComponent.getPackageName().equals("com.iteacher.android")) {
			return true;
		}
		return false;
	}

	public void startNotification(String message) {
		Intent intent = new Intent(context, WelcomeActivity.class);
		startNotification(message, intent);
	}
	
	public void startNotification(String message, Intent intent) {
		NotificationManager mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification notification = new Notification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.icon = R.drawable.whatsnew_logo1;
		notification.when = System.currentTimeMillis();
		
		notification.tickerText = message;
//		notification.setLatestEventInfo(context, context.getString(R.string.app_name), message, pendingIntent);
		mNotifMan.notify(Constants.KEY_NOTIFICATION_ID, notification);
	}

}
