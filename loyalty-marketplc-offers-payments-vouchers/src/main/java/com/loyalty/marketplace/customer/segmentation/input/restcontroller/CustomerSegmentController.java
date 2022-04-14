//package com.loyalty.marketplace.customer.segmentation.input.restcontroller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.loyalty.marketplace.constants.RequestMappingConstants;
//import com.loyalty.marketplace.customer.segmentation.domain.CustomerSegmentDomain;
//import com.loyalty.marketplace.customer.segmentation.helper.dto.CustomerSegmentHeaders;
//import com.loyalty.marketplace.customer.segmentation.input.dto.CustomerSegmentRequestDto;
//import com.loyalty.marketplace.offers.constants.OfferConstants;
//import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
//import com.loyalty.marketplace.offers.utils.Logs;
//import com.loyalty.marketplace.outbound.dto.ResultResponse;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@Api(value = OffersRequestMappingConstants.MARKETPLACE)
//@RequestMapping(OffersRequestMappingConstants.MARKETPLACE_BASE)
//public class CustomerSegmentController{
//	
//	private static final Logger LOG = LoggerFactory.getLogger(CustomerSegmentController.class);
//	
//	@Autowired
//	CustomerSegmentDomain customerSegmentDomain;
//
//	/**
//	 * 
//	 * @param offerCatalogRequest
//	 * @param program
//	 * @param authorization
//	 * @param externalTransactionId
//	 * @param userName
//	 * @param sessionId
//	 * @param userPrev
//	 * @param channelId
//	 * @param systemId
//	 * @param systemPassword
//	 * @param token
//	 * @param transactionId
//	 * @return
//	 */
//	@PostMapping(value = OffersRequestMappingConstants.CREATE_CUSTOMER_SEGMENT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResultResponse createCustomerSegment(@RequestBody CustomerSegmentRequestDto customerSegmentRequest,
//			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
//			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
//			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
//			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
//			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
//			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
//			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
//			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
//			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
//			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
//			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
//
//		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.CREATE_CUSTOMER_SEGMENT_METHOD.get());
//		LOG.info(log);
//		
//		log = Logs.logForRequest(customerSegmentRequest);
//		LOG.info(log);
//
//		ResultResponse resultResponse = customerSegmentDomain.validateAndSaveCustomerSegment(customerSegmentRequest, 
//										new CustomerSegmentHeaders(program, authorization, externalTransactionId, userName, sessionId, userPrev,
//										channelId, systemId, systemPassword, token, transactionId));
//		
//		log = Logs.logForResponse(resultResponse);
//		LOG.info(log);
//		
//		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.CREATE_CUSTOMER_SEGMENT_METHOD.get());
//		LOG.info(log);
//		
//		return resultResponse;
//
//	}
//	
//		
//}
