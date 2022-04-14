package com.loyalty.marketplace.outbound.jms.events;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.loyalty.marketplace.constants.JMSConstants;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.outbound.jms.events.database.JmsMessages;
import com.loyalty.marketplace.outbound.jms.events.database.JmsRepository;
import com.loyalty.marketplace.utils.MarketplaceException;

@Component
public class JMSReprocessHelper {

	private static final Logger LOG = LoggerFactory.getLogger(JMSReprocessHelper.class);

	@Autowired
	private JmsEvents jmsEvents;

	@Autowired
	private JmsRepository jmsErrorRepository;

	@Autowired
	private MongoOperations mongoOperations;

	private static final List<String> jsmConstantList = Arrays.asList(String.valueOf(JMSConstants.PUBLISH_FAILED),
			String.valueOf(JMSConstants.FAILED), String.valueOf(JMSConstants.REPLY_FAILED));

	public void reprocessEvent() {
		LOG.info("inside reprocessEvents method of class JMSReprocessHelper and  correlationIds");
		List<JmsMessages> jmsMessages = mongoOperations.find(query(where("Status").in(jsmConstantList)),
				JmsMessages.class);
		if (!CollectionUtils.isEmpty(jmsMessages)) {
			for (JmsMessages jmsMessage : jmsMessages) {
				String status = jmsMessage.getStatus();
				if (jsmConstantList.contains(status)) {
					jmsEvents.publishReprocessEvent(jmsMessage.getData(), jmsMessage.getQueueName(),
							jmsMessage.getCorrelationId(), status);
				}
			}
		}
	}

	public void reprocessEvent(List<String> correlationIds) throws MarketplaceException {
		LOG.info("inside reprocessEvents method of class JMSReprocessHelper and  correlationIds and correlationIds {} ",
				correlationIds);
		for (String correlationId : correlationIds) {
			try {
				reprocessEvent(correlationId);
			} catch (Exception e) {
				LOG.info("inside reprocessEvents method of class JMSReprocessHelper and got exception ", e);
			}
		}
	}

	public void reprocessEvent(String correlationId) throws MarketplaceException {
		LOG.info("inside reprocessEvents method of class JMSReprocessHelper and correlationId {} ", correlationId);
		JmsMessages jmsErrorOptional = jmsErrorRepository.findByCorrelationId(correlationId);
		if (null != jmsErrorOptional) {
			JmsMessages jMSMessages = jmsErrorOptional;
			String status = jMSMessages.getStatus();
			if (jsmConstantList.contains(status)) {
				jmsEvents.publishReprocessEvent(jMSMessages.getData(), jMSMessages.getQueueName(),
						jMSMessages.getCorrelationId(), status);
			} else {
//				throw new UserManagementException(ErrorCodes.INAVLID_CORRELATION_ID.getCode(),
//						invalidCorrelationIds + status, null);
				
				throw new MarketplaceException(this.getClass().toString(), "reprocessEvent",
						"Error in reprocessing event", MarketPlaceCode.PUBLISH_JMS_EVENT_EXCEPTION);
			}
		}
	}
}
