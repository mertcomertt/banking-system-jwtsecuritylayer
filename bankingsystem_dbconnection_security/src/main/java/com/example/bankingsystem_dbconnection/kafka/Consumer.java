package com.example.bankingsystem_dbconnection.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

	@KafkaListener(topics = "logs", groupId = "group")
	public void listen(@Payload String message) {
	System.out.println(message);
			  
	
	}
}
