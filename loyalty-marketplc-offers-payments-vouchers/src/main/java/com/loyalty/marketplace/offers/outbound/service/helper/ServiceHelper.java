package com.loyalty.marketplace.offers.outbound.service.helper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;


/**
 * 
 * @author jaya.shukla
 *
 */
@Component
public class ServiceHelper {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceHelper.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("getTemplateBean")
	private RestTemplate getRestTemplate;

	@Autowired
	private RetryTemplate retryTemplate;
	
	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;

	/**
	 * 
	 * @param result
	 * @param classz
	 * @return input object converted to input class
	 */
	public Object convertToObject(Object result, Class<?> classz) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.convertValue(result, classz);
		} catch (Exception ex) {
			LOG.error(new MarketplaceException(this.getClass().toString(), "convertToObject",
					ex.getClass() + ex.getMessage(), OfferExceptionCodes.OBJECT_CONVERSION_EXCEPTION).printMessage());
		}

		return null;
	}

	/**
	 * 
	 * @param header
	 * @return header for rest call
	 */
	public HttpHeaders getHeader(Headers header) {

		HttpHeaders headers = new HttpHeaders();

		final List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		headers.setAccept(list);

		if (!ObjectUtils.isEmpty(header)) {

			headers.set(RequestMappingConstants.AUTHORIZATION, header.getAuthorization());
			headers.set(RequestMappingConstants.PROGRAM, header.getProgram());
			headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, header.getExternalTransactionId());
			headers.set(RequestMappingConstants.USER_NAME, header.getUserName());
			headers.set(RequestMappingConstants.SESSION_ID, header.getSessionId());
			headers.set(RequestMappingConstants.USER_PREV, header.getUserPrev());
			headers.set(RequestMappingConstants.CHANNEL_ID, header.getChannelId());
			headers.set(RequestMappingConstants.SYSTEM_ID, header.getSystemId());
			headers.set(RequestMappingConstants.SYSTEM_CREDENTIAL, header.getSystemPassword());
			headers.set(RequestMappingConstants.TOKEN, header.getToken());
			headers.set(RequestMappingConstants.TRANSACTION_ID, header.getTransactionId());
			headers.setContentType(MediaType.APPLICATION_JSON);

		}

		return headers;
	}

	/**
	 * 
	 * @param url
	 * @param header
	 * @return response for get rest call
	 */

	public CommonApiStatus getMethodGetObject(String url, Headers header, String service) {
		String log = Logs.logForServiceUrl(url);
		LOG.info(log);

		HttpEntity<?> headerEntity = new HttpEntity<>(getHeader(header));
		log = Logs.logForServiceHeaders(headerEntity);
		LOG.info(log);

		log = Logs.logBeforeHittingService(service);
		LOG.info(log);
		ResponseEntity<CommonApiStatus> response = retryCallForGetMethodGetObject(url, headerEntity, header);
		log = Logs.logAfterHittingService(service);
		LOG.info(log);

		log = Logs.logForServiceResponse(response.getBody());
		LOG.info(log);

		return response.getBody();
	}

	private ResponseEntity<CommonApiStatus> retryCallForGetMethodGetObject(String url, HttpEntity<?> headerEntity,
			Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForGetMethodGetObject method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						headerEntity.toString(), header.getUserName());
				return getRestTemplate.exchange(url, HttpMethod.GET, headerEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}

	public CommonApiStatus postMethodPostObject(String url, Headers header, String service) {
		String log = Logs.logForServiceUrl(url);
		LOG.info(log);

		HttpEntity<?> headerEntity = new HttpEntity<>(getHeader(header));
		log = Logs.logForServiceHeaders(headerEntity);
		LOG.info(log);

		log = Logs.logBeforeHittingService(service);
		LOG.info(log);
		ResponseEntity<CommonApiStatus> response = retryCallPostObject(url, headerEntity, header);
		log = Logs.logAfterHittingService(service);
		LOG.info(log);

		log = Logs.logForServiceResponse(response.getBody());
		LOG.info(log);

		return response.getBody();
	}

	/**
	 * 
	 * @param url
	 * @param header
	 * @return response for post rest call that involves retrieval
	 */

	public CommonApiStatus getMethodPostObjectForRetrieval(String url, Headers header, Object request, String service,
			ResultResponse resultResponse) {
		String log = Logs.logForServiceUrl(url);
		LOG.info(log);

		HttpEntity<?> requestEntity = new HttpEntity<>(request, getHeader(header));
		log = Logs.logForServiceRequest(requestEntity);
		LOG.info(log);

		log = Logs.logBeforeHittingService(service);
		LOG.info(log);

		ResponseEntity<CommonApiStatus> response = retryCallPostObject(url, requestEntity, header);
		log = Logs.logAfterHittingService(service);
		LOG.info(log);

		log = Logs.logForServiceResponse(response.getBody());
		LOG.info(log);

		return response.getBody();
//		ResponseEntity<CommonApiStatus> response = getRestTemplate.exchange(url,  HttpMethod.POST, requestEntity, CommonApiStatus.class);
//		log = Logs.logAfterHittingService(service);
//		LOG.info(log);
//
//		log = Logs.logForServiceResponse(response.getBody());
//		LOG.info(log);
//
//		return response.getBody();
	}

	private ResponseEntity<CommonApiStatus> retryCallPostObject(String url, HttpEntity<?> requestEntity,
			Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallPostObject method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				return getRestTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param header
	 * @return response for post rest call
	 */

	public CommonApiStatus getMethodPostObject(String url, Headers header, Object request, String service,
			ResultResponse resultResponse) {
		String log = Logs.logForServiceUrl(url);
		LOG.info(log);

		HttpEntity<?> requestEntity = new HttpEntity<>(request, getHeader(header));
		log = Logs.logForServiceRequest(requestEntity);
		LOG.info(log);

		log = Logs.logBeforeHittingService(service);
		LOG.info(log);
		ResponseEntity<CommonApiStatus> response = retryCallGetMethodPostObject(url, requestEntity, header);
		log = Logs.logAfterHittingService(service);
		LOG.info(log);

		log = Logs.logForServiceResponse(response.getBody());
		LOG.info(log);

		return response.getBody();
	}

	private ResponseEntity<CommonApiStatus> retryCallGetMethodPostObject(String url, HttpEntity<?> requestEntity,
			Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallGetMethodPostObject method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						requestEntity.toString(), header.getUserName());
				return restTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}
}