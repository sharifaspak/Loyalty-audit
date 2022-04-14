package com.loyalty.marketplace.audit;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.constants.JMSConstants;
import com.loyalty.marketplace.outbound.jms.events.JmsEvents;
import com.loyalty.marketplace.outbound.jms.events.database.JmsMessages;
import com.loyalty.marketplace.outbound.jms.events.database.JmsRepository;

@Component
@EnableJms
public class AuditEvents {
	
	@Autowired
	private JmsRepository jmsRepository;
	
	@Autowired
	private JmsEvents jmsEvent;
	
	@Value("${audit.queue}")
	private String queue;
	
	@Value("${audit.replyQueue}")
	private String replyQueue;

	private static final Logger LOG = LoggerFactory.getLogger(AuditEvents.class);

	public void publishAuditEvent(Audit audit) {
	
		jmsEvent.publishJmsEvent(audit, queue, replyQueue, UUID.randomUUID().toString());
			
	}
	
	@JmsListener(destination = "MerchantsStores-reply")
    public void receiveEvent(String event, @Header(JmsHeaders.MESSAGE_ID) String messageId,
            @Header(JmsHeaders.CORRELATION_ID) String correlationId) {
        LOG.info("inside receiveEvent method of class EventHandler receiving  event={}  messageId={} correlationId={}",
                event, messageId, correlationId);
        try {
            if (event instanceof String) {
                boolean existsByCorrelationId = jmsRepository.existsByCorrelationId(correlationId);
                if (existsByCorrelationId) {
                    if(JMSConstants.SUCCESS.toString().equalsIgnoreCase(event)) {
                    	jmsRepository.deleteByCorrelationId(correlationId);
                    }else {
                        updateJmsFailed(correlationId, event);
                    }
                }
            }
            LOG.info("inside receiveEvent method of class EventHandler successfully processed event ");
        } catch (Exception e) {
            LOG.error("inside receiveEvent method of class EventHandler got exception while processing the event error={}", e);
            updateJmsFailed(correlationId, event);
        }
    }
   
    private void updateJmsFailed(String correlationId, String errorMessage) {
        JmsMessages jmsError = jmsRepository.findByCorrelationId(correlationId);
        jmsError.setStatus(JMSConstants.FAILED.toString());
        jmsRepository.save(jmsError);
    }
	
	

}
