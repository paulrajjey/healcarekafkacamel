package com.redhat.config;

import org.codehaus.jackson.map.ObjectMapper;

import com.redhat.model.Notification;


public class KafkaOutputBean {

	public void doWork(String body) {
		System.out.println("KafkaBody result >>>>> " + this.retrieve(body));
	}

	public Notification retrieve(String body) {
		Notification message;
		try {
			message = new ObjectMapper().readValue(body, Notification.class);
		} catch (Exception e) {
			message = new Notification();
		}
		return message;
	}
}
