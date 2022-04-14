package com.loyalty.marketplace.outbound.jms.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.jms.events.dto.JmsReprocessDto;
import com.loyalty.marketplace.utils.MarketplaceException;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Marketplace")
@RequestMapping("/marketplace") 
public class JmsController {
	
//	@Autowired 
//	private JmsEvents jmsEvent;
//	
//	@Autowired
//	private JmsRepository jmsRepository;
//	
	
	@Autowired
	private JMSReprocessHelper jMSReprocessHelper;
	
	private static final Logger LOG = LoggerFactory.getLogger(JmsController.class);
	
//	@PostMapping(value = "/jmsEvent/{correlationId}", consumes = MediaType.ALL_VALUE)
//	public void reprocessJmsEvent(@PathVariable(value = JmsHeaders.CORRELATION_ID, required = true) String correlationId)  {
//		jmsEvent.reprocessJmsEvent(correlationId);
//		
//	}
//	
//	@PostMapping(value = "/jmsEvent/reprocess", consumes = MediaType.ALL_VALUE)
//	public ResultResponse reprocessJmsEvent(
//			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
//			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
//			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
//			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
//			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
//			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
//			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
//			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
//			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
//			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
//			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
//			@RequestBody JmsReprocessDto jmsReprocessDto) {
//
//		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
//		
//		try {
//
//			List<JmsMessages> jmsMessageList = new ArrayList<>();
//			if (null != jmsReprocessDto.getCorrelationIds() || !jmsReprocessDto.getCorrelationIds().isEmpty()) {
//				jmsMessageList = jmsRepository.findByCorrelationIdList(jmsReprocessDto.getCorrelationIds());
//			} else {
//				jmsRepository.findAll();
//			}
//
//			if (CollectionUtils.isNotEmpty(jmsMessageList)) jmsMessageList.forEach(j -> jmsEvent.reprocessEvent(j));
//
//			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_SUCCESS.getId(),
//					MarketPlaceCode.JMS_REPROCESS_SUCCESS.getMsg());
//			
//		} catch (Exception e) {
//			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
//					e.getMessage());
//			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_FAILED.getId(),
//					MarketPlaceCode.JMS_REPROCESS_FAILED.getMsg());
//			LOG.info(new MarketplaceException(this.getClass().toString(), "reprocessJmsEvent",
//					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
//		}
//		
//		return resultResponse;
//
//	}
	
	@PostMapping(value = RequestMappingConstants.REPROCESS_EVENTS_ALL, consumes = MediaType.ALL_VALUE)
	public ResultResponse reprocessAllJmsEvent(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info("inside reprocessAllJmsEvent method of class {}", this.getClass().getName());
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		try {

			jMSReprocessHelper.reprocessEvent();
			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_SUCCESS.getId(),
					MarketPlaceCode.JMS_REPROCESS_SUCCESS.getMsg());
			
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_FAILED.getId(),
					MarketPlaceCode.JMS_REPROCESS_FAILED.getMsg());
			LOG.info(new MarketplaceException(this.getClass().toString(), "reprocessJmsEvent",
					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;

	}
	
	@PostMapping(value = RequestMappingConstants.REPROCESS_EVENTS_LIST, consumes = MediaType.ALL_VALUE)
	public ResultResponse reprocessJmsEventList(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestBody JmsReprocessDto jmsReprocessDto) {

		LOG.info("inside reprocessJmsEventList method of class {}", this.getClass().getName());
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		try {

			jMSReprocessHelper.reprocessEvent(jmsReprocessDto.getCorrelationIds());
			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_SUCCESS.getId(),
					MarketPlaceCode.JMS_REPROCESS_SUCCESS.getMsg());
			
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_FAILED.getId(),
					MarketPlaceCode.JMS_REPROCESS_FAILED.getMsg());
			LOG.info(new MarketplaceException(this.getClass().toString(), "reprocessJmsEvent",
					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;

	}
	
	@PostMapping(value = RequestMappingConstants.REPROCESS_EVENTS, consumes = MediaType.ALL_VALUE)
	public ResultResponse reprocessJmsEvent(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@PathVariable(required = true) String correlationId) {

		LOG.info("inside reprocessJmsEvent method of class {}", this.getClass().getName());
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		try {

			jMSReprocessHelper.reprocessEvent(correlationId);
			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_SUCCESS.getId(),
					MarketPlaceCode.JMS_REPROCESS_SUCCESS.getMsg());
			
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(MarketPlaceCode.JMS_REPROCESS_FAILED.getId(),
					MarketPlaceCode.JMS_REPROCESS_FAILED.getMsg());
			LOG.info(new MarketplaceException(this.getClass().toString(), "reprocessJmsEvent",
					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;

	}
}
