package com.stay4it.im.push;

import java.util.Observable;
import java.util.Observer;

import com.stay4it.im.entities.Message;

/** 
 * @author Stay  
 * @version create time：Mar 11, 2015 10:28:26 AM 
 */
public class PushWatcher implements Observer {

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof Message) {
			onMessageReceived((Message)data);
		}else if(data instanceof Message[]){
			onMessageUpdated(((Message[])data)[0], ((Message[])data)[1]);
		}
	}
	
	public void onMessageReceived(Message message){
		
	}
	
	public void onMessageUpdated(Message oldMessage, Message newMessage){
		
	}

}
