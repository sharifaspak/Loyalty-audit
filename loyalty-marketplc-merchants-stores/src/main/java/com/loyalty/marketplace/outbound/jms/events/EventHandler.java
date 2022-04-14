package com.loyalty.marketplace.outbound.jms.events;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.merchants.outbound.service.dto.EmailRequestDto;

@Component
@EnableJms
@RefreshScope
public class EventHandler {
	
	@Autowired
	private JmsEvents jmsEvent;
	
	@Value("${email.queue}")
	private String emailQueue;

	@Value("${marketplace.replyQueue}")
	private String replyQueue;
	
	public void publishEmail(EmailRequestDto email) {
		
		jmsEvent.publishJmsEvent(email, emailQueue, replyQueue, UUID.randomUUID().toString());
			
	}
	
}
