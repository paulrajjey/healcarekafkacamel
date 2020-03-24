package com.redhat.controller;

import java.util.HashMap;
import java.util.Map;

public class NotificationHandler {
	
	private static NotificationHandler notificationHandler;
	
	private Map<String, NotificationData> notifications ;
	private Map<String, String> msgNotifications ;
	
	private NotificationHandler() {
		
		notifications = new HashMap<String, NotificationData>();	
		msgNotifications = new HashMap<String, String>();
		
	}
	public static NotificationHandler newNotificationHandler() {
		if (notificationHandler ==null) {
			notificationHandler = new NotificationHandler();
		}
		return notificationHandler;
		
	}
	public Map<String, NotificationData> getNotifications() {
		return notifications;
	}
	public Map<String, String> getMsgNotifications() {
		return msgNotifications;
	}
	
	

	
}
