package com.loyalty.marketplace.outbound.events.handler;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.constants.JMSConstants;
import com.loyalty.marketplace.offers.points.bank.inbound.dto.LifeTimeSavingsVouchersEvent;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.dto.PushNotificationRequestDto;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.eventobject.AccountCancelEvent;
import com.loyalty.marketplace.outbound.events.eventobject.AccountChangeEvent;
import com.loyalty.marketplace.outbound.events.eventobject.EnrollEvent;
import com.loyalty.marketplace.outbound.events.eventobject.Event;
import com.loyalty.marketplace.outbound.events.eventobject.MemberMergeEvent;
import com.loyalty.marketplace.outbound.events.helper.EventHelper;
import com.loyalty.marketplace.outbound.jms.events.JmsEvents;
import com.loyalty.marketplace.outbound.jms.events.database.JmsMessages;
import com.loyalty.marketplace.outbound.jms.events.database.JmsRepository;
import com.loyalty.marketplace.payment.inbound.dto.ATGEnrollmentDTO;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;

@Component
@EnableJms
@RefreshScope
public class EventHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(EventHandler.class);
	
	@Autowired
	EventHelper eventHelper;
	
	@Autowired
	private JmsEvents jmsEvent;
	
	@Autowired
	private JmsMessages jmsMessage;
	
	@Autowired
	private JmsRepository jmsRepository;
	
	@Value("${marketplace.listenerQueue}")
	private String listenerQueue;
	
	@Value("${sms.queue}")
	private String smsQueue;
	
	@Value("${alert.sms.queue}")
	private String alertSmsQueue;
	
	@Value("${notification.queue}")
	private String pushNotificationQueue;
	
	@Value("${email.queue}")
	private String emailQueue;
	
	@Value("${points.bank.queue}")
	private String pointsBankQueue;
	
	@Value("${marketplace.replyQueue}")
	private String replyQueue;
	
	@Value("${service.call.log.queue}")
	private String serviceCallLogQueue;
	
	public void publishSms(SMSRequestDto sms) {
		
		jmsEvent.publishJmsEvent(sms, smsQueue, replyQueue, UUID.randomUUID().toString());
			
	}
	
	public void publishAlertSms(SMSRequestDto sms) {
		
		jmsEvent.publishJmsEvent(sms, alertSmsQueue, replyQueue, UUID.randomUUID().toString());
			
	}
	
	public void publishEmail(EmailRequestDto email) {
		
		jmsEvent.publishJmsEvent(email, emailQueue, replyQueue, UUID.randomUUID().toString());
			
	}
	
	public void publishPushNotification(PushNotificationRequestDto pushNotification) {
		
		jmsEvent.publishJmsEvent(pushNotification, pushNotificationQueue, replyQueue, UUID.randomUUID().toString());
			
	}
	
	public void publishLifetimeSavings(LifeTimeSavingsVouchersEvent lifeTimeSavingsDetails) {
		
		if(!ObjectUtils.isEmpty(lifeTimeSavingsDetails)
		&& !ObjectUtils.isEmpty(lifeTimeSavingsDetails.getSpendValue())) {
			
			jmsEvent.publishJmsEvent(lifeTimeSavingsDetails, pointsBankQueue, replyQueue, UUID.randomUUID().toString());
		}
	}
	
	public void publishEnrollment(ATGEnrollmentDTO enrollment) {
		
		jmsEvent.publishJmsEvent(enrollment, "atgEnrollmentQueue", replyQueue, UUID.randomUUID().toString());
			
	}
	
	public void publishServiceCallLogs(ServiceCallLogsDto serviceCallLog) {
		
		jmsEvent.publishJMSEventWithoutReply(serviceCallLog, serviceCallLogQueue);
			
	}
	
	@JmsListener(destination = "${marketplace.listenerQueue}")
	public void receiveEvent(final Event event, 
			@Header(JmsHeaders.MESSAGE_ID) String messageId,
			@Header(JmsHeaders.CORRELATION_ID) String correlationId, 
			@Header(JmsHeaders.DESTINATION) String destination,
			@Header(JmsHeaders.REPLY_TO) String replyTo) {
		
		LOG.info("inside receiveEvent method of class EventHandler receiving  event={}  messageId={} correlationId={} form destination={} replyTo={}",
				event, messageId, correlationId, destination, replyTo);
		
		jmsRepository
		.save(new JmsMessages(null, correlationId, null, null, replyTo, String.valueOf(JMSConstants.CONSUMED), null, null, new Date(), null, null));
		String message = String.valueOf(JMSConstants.SUCCESS);
		try {
			
			
			if (event instanceof AccountChangeEvent) {
			
				AccountChangeEvent accountChangeEvent = (AccountChangeEvent) event;
				eventHelper.updateAccountNumbers(accountChangeEvent);
							
			} else if (event instanceof EnrollEvent) {
				
				EnrollEvent enrollEvent = (EnrollEvent) event;
				eventHelper.updateSubscriptionWithMember(enrollEvent);
								
			} else if (event instanceof AccountCancelEvent) {
				
				AccountCancelEvent cancelEvent = (AccountCancelEvent) event;
				eventHelper.cancelSubscriptionForAccount(cancelEvent);
								
			} else if (event instanceof MemberMergeEvent) {
				
				MemberMergeEvent memberMergeEvent = (MemberMergeEvent) event;
				eventHelper.updateAccountWithNewMembership(memberMergeEvent);
								
			} else {
				LOG.info("inside receiveEvent method of class EventHandler an unknown  event is received");
				jmsEvent.publishJmsEvent("Unknow event", replyTo, replyQueue, correlationId);
				return;
			}
			
			
			LOG.info("inside receiveEvent method of class EventHandler successfully processed event ");
		
		} catch (Exception e) {
			
			LOG.error("inside receiveEvent method of class={}  got exception while processing the event error={}", e);
			jmsMessage.setStatus(JMSConstants.FAILED.toString());
			jmsMessage.setStatusDetail(e.getMessage());
			jmsMessage.setData(event);
			jmsMessage.setReplyQueueName(replyTo);
			jmsMessage.setCorrelationId(correlationId);
			jmsMessage.setQueueName(listenerQueue);
			jmsMessage.setCreatedDate(new Date());
			jmsRepository.save(jmsMessage);
			
			message = e.getLocalizedMessage();
			
		}		
		jmsEvent.publishReplyEvents(message, replyTo, correlationId); 
	}
	
	@JmsListener(destination = "${marketplace.replyQueue}")
    public void receiveReplyEvent(String event, @Header(JmsHeaders.MESSAGE_ID) String messageId,
            @Header(JmsHeaders.CORRELATION_ID) String correlationId) {
        LOG.info("inside receiveReplyEvent method of class EventHandler receiving  event={}  messageId={} correlationId={}",
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
            LOG.info("inside receiveReplyEvent method of class EventHandler successfully processed event ");
        } catch (Exception e) {
            LOG.error("inside receiveReplyEvent method of class EventHandler got exception while processing the event error={}", e);
            updateJmsFailed(correlationId, event);
        }
    }
   
    private void updateJmsFailed(String correlationId, String errorMessage) {
        JmsMessages jmsError = jmsRepository.findByCorrelationId(correlationId);
        jmsError.setStatus(JMSConstants.FAILED.toString());
        jmsRepository.save(jmsError);
    }
	
	@JmsListener(destination = "marketplaceRollbackQueue")
	public void receiveRollbackEvent(final String pointsTransactionId, 
			@Header(JmsHeaders.MESSAGE_ID) String messageId,
			@Header(JmsHeaders.CORRELATION_ID) String correlationId, 
			@Header(JmsHeaders.DESTINATION) String destination,
			@Header(JmsHeaders.REPLY_TO) String replyTo) {
		
		LOG.info("inside receiveRollbackEvent method of class EventHandler receiving  event={}  messageId={} correlationId={} form destination={} replyTo={}",
				pointsTransactionId, messageId, correlationId, destination, replyTo);
		
		jmsRepository
		.save(new JmsMessages(null, correlationId, null, null, replyTo, String.valueOf(JMSConstants.CONSUMED), null, null, new Date(), null, null));
		String message = String.valueOf(JMSConstants.SUCCESS);
		try {
			eventHelper.rollbackNonVoucher(pointsTransactionId);
			LOG.info("inside receiveRollbackEvent method of class EventHandler successfully processed event ");
		
		} catch (Exception e) {
			
			LOG.error("inside receiveRollbackEvent method of class={}  got exception while processing the event error={}", e);
			jmsMessage.setStatus(JMSConstants.FAILED.toString());
			jmsMessage.setStatusDetail(e.getMessage());
			jmsMessage.setData(pointsTransactionId);
			jmsMessage.setReplyQueueName(replyTo);
			jmsMessage.setCorrelationId(correlationId);
			jmsMessage.setQueueName("marketplaceRollbackQueue");
			jmsMessage.setCreatedDate(new Date());
			jmsRepository.save(jmsMessage);
			
			message = e.getLocalizedMessage();
			
		}		
		jmsEvent.publishReplyEvents(message, replyTo, correlationId); 
	}
	
}
