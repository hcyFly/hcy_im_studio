package com.stay4it.im.push;

import com.stay4it.im.db.MessageController;
import com.stay4it.im.entities.Message;
import com.stay4it.im.untils.Trace;

import java.util.Observable;

/** 
 * @author Stay  
 * @version create time：Mar 11, 2015 10:28:10 AM 
 */
public class PushChanger extends Observable {
	private static PushChanger mInstance;
	
	public static PushChanger getInstance(){
		if (mInstance == null) {
			mInstance = new PushChanger();
		}
		return mInstance;
	}
	
	public void notifyChanged(int percent, Message message){
		Trace.d("send image message: progress changed:" + percent + "%");
		setChanged();
		notifyObservers(message);
	}
	
	public void notifyChanged(Message oldMessage, Message newMessage) {
		if (newMessage != null) {
			MessageController.delete(oldMessage);
			MessageController.addOrUpdate(newMessage);
		}else {
			MessageController.addOrUpdate(oldMessage);
		}
		Message[] messages = new Message[2];
		messages[0] = oldMessage;
		messages[1] = newMessage;
		setChanged();
		notifyObservers(messages);
	}
	
	public void notifyChanged(Message message) {
		MessageController.addOrUpdate(message);
		setChanged();
		notifyObservers(message);
	}
	
	
}
