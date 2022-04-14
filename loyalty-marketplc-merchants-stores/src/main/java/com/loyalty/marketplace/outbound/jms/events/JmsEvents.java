package com.loyalty.marketplace.outbound.jms.events;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.constants.JMSConstants;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.outbound.jms.events.database.JmsMessages;
import com.loyalty.marketplace.outbound.jms.events.database.JmsRepository;
import com.loyalty.marketplace.utils.MarketplaceException;

@Component
@RefreshScope
public class JmsEvents {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private JmsRepository jmsRepository;
	
	@Value("${marketplace.replyQueue}")
	private String replyQueue;
	
	private static final Logger LOG = LoggerFactory.getLogger(JmsEvents.class);
	
	public void publishJmsEvent(Object event, String queueName, String replyQueueName,String correlationID) {
        LOG.info("inside publishJmsEvent method of class JmsEvents");
        JmsMessages jmsMessageSave = persistJmsMessage(event, queueName, replyQueueName, correlationID, JMSConstants.PUBLISHED.toString(), "published");
        try {
            publishEvent(event, queueName, replyQueueName, correlationID);   
        } catch (Exception e) {
            LOG.info("Exception in publishJmsEvent method of class JmsEvents: {}",e.getMessage());
            jmsMessageSave.setStatus(JMSConstants.PUBLISH_FAILED.toString());
            jmsMessageSave.setStatusDetail(e.getMessage());
            jmsMessageSave.setUpdatedDate(new Date());
            jmsRepository.save(jmsMessageSave);
        }       
    }
   
    private void publishEvent(Object event, String queueName, String replyQueueName, String correlationId) throws MarketplaceException{
        LOG.info("inside publishEvent method of class JmsEvents publising event {} to queue {} correlationId={}",
                event, queueName, correlationId);
        try {
            this.jmsTemplate.setPubSubDomain(false);
            this.jmsTemplate.convertAndSend(queueName, event , createMessage(correlationId, replyQueueName));
        } catch (Exception e) {
            LOG.error(
                    "inside publishEvent method of class EventHandler  got exception while send message to queueName={} and exception={}",
                    queueName, e);
            throw new MarketplaceException(this.getClass().toString(), "publishEvent",
					e.getClass() + e.getMessage(), MarketPlaceCode.PUBLISH_JMS_EVENT_EXCEPTION);
        }
         LOG.info("Message sent to {} ",queueName);
    }
    
    public void publishReplyEvents(Object event, String queueName, String correlationId){
        LOG.info("inside publishEvent method of class EventHandler publising event {} to queue {} correlationId={}",
                event, queueName, correlationId);
        try {
            this.jmsTemplate.setPubSubDomain(false);
            this.jmsTemplate.convertAndSend(queueName, event, createMessage(correlationId, "notify-Marketplace"));
        }catch (Exception e) {
            LOG.error(
                    "inside publishEvent method of class EventHandler  got exception while send message to queueName={} and exception={}",
                    queueName, e.getMessage());
            JmsMessages jmsMessage = jmsRepository.findByCorrelationId(correlationId);
            jmsMessage.setStatus(JMSConstants.REPLY_FAILED.toString());
            jmsMessage.setUpdatedDate(new Date());
            jmsRepository.save(jmsMessage);
        }
    }
    
    private JmsMessages persistJmsMessage(Object event, String queueName, String replyQueueName, String correlationID,
            String status, String cause) {
    	JmsMessages jmsMessage = new JmsMessages();
        jmsMessage.setData(event);
        jmsMessage.setReplyQueueName(replyQueueName);
        jmsMessage.setCorrelationId(correlationID);
        jmsMessage.setQueueName(queueName);
        jmsMessage.setCreatedDate(new Date());
        jmsMessage.setStatus(status);
        return jmsRepository.save(jmsMessage);
    } 
	
	private MessagePostProcessor createMessage(String correlationId, String queueName) {
		LOG.debug("inside createMessage method of class EventHandler correlationId={} queueName={}", correlationId,
				queueName);
		return new MessagePostProcessor() {
			public Message postProcessMessage(Message message) throws JMSException {
				message.setJMSCorrelationID(correlationId);
				if(null!=queueName) {
				message.setJMSReplyTo(new Queue() {
					@Override
					public String getQueueName() throws JMSException {
						return queueName;
					}
				});
				}
				return message;
			}
		};
		
	}
	
	public void publishReprocessEvent(Object event, String queueName, String correlationId, String status) {
		LOG.info(
				"inside publishReprocessEvent method of class EventHandler publising event {} to queue {} correlationId={}",
				event, queueName, correlationId);
		boolean flag = true;
		if (String.valueOf(JMSConstants.REPLY_FAILED).equalsIgnoreCase(status)) {
			flag = false;
		}

		try {
			this.jmsTemplate.setPubSubDomain(false);
			this.jmsTemplate.convertAndSend(queueName, event,
					createMessage(correlationId, replyQueue, flag));
		} catch (Exception e) {
			LOG.error(
					"inside publishReprocessEvent method of class EventHandler  got exception while send message to queueName={} and exception ",
					queueName, e);
			flag = true;
		}
		if (!flag) {
			boolean existsByCorrelationId = jmsRepository.existsByCorrelationId(correlationId);
			if (existsByCorrelationId)
				jmsRepository.deleteByCorrelationId(correlationId);
		}
	}
	
	private MessagePostProcessor createMessage(String correlationId, String queueName, boolean hasReplyTo) {
		LOG.info("inside createMessage method of class EventHandler correlationId={} queueName={}", correlationId,
				queueName);
		return new MessagePostProcessor() {
			public Message postProcessMessage(Message message) throws JMSException {
				message.setJMSCorrelationID(correlationId);
				if (hasReplyTo) {
					message.setJMSReplyTo(new Queue() {
						@Override
						public String getQueueName() throws JMSException {
							return queueName;
						}
					});
				}
				return message;
			}
		};

	}
}
