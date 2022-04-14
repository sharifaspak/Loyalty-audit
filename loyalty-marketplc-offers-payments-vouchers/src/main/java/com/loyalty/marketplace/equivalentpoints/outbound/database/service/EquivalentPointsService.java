package com.loyalty.marketplace.equivalentpoints.outbound.database.service;

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
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.event.HeaderDto;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus;

import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;

@Component
public class EquivalentPointsService {

	private static final Logger LOG = LoggerFactory.getLogger(EquivalentPointsService.class);

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

	@SuppressWarnings("unchecked")
	public ResponseEntity<CommonApiStatus> buildWebClient(final String uri, final Object data,
			final HttpMethod methodType, Class<?> classz, HeaderDto headerDto) {
		// Add the Jackson message converter
		// restTemplate.getMessageConverters().add(new
		// MappingJackson2HttpMessageConverter());

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (headerDto != null) {
			headers.set(RequestMappingConstants.USER_NAME, headerDto.getUserName());
			headers.set(RequestMappingConstants.PROGRAM, headerDto.getProgram());
			headers.set(RequestMappingConstants.CHANNEL_ID, headerDto.getChannelId());
			headers.set(RequestMappingConstants.TOKEN, headerDto.getToken());
			headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, headerDto.getExternalTransactionId());
		} else {
			headers.set(RequestMappingConstants.USER_NAME, "Admin");
			headers.set(RequestMappingConstants.PROGRAM, "Smiles");
			headers.set(RequestMappingConstants.CHANNEL_ID, "Loyalty");
		}
		final List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		headers.setAccept(list);
		HttpEntity<?> entity = new HttpEntity<>(data, headers);
		LOG.info("inside buildWebClient method of class {} with member activity  HttpEntity {} ",
				this.getClass().getName(), entity);
		try {
			return retryTemplate.execute(context -> {
				LOG.info("inside Retry block using retryTemplate of buildWebClient method of class {}",
						this.getClass().getName());
				retryLogsService.saveRestRetrytoRetryLogs(uri.toString(), headerDto.getExternalTransactionId(),
						entity.toString(), headerDto.getUserName());
				return (ResponseEntity<CommonApiStatus>) getRestTemplate.exchange(uri, methodType, entity, classz);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, headerDto.getExternalTransactionId(), null,
					headerDto.getUserName(), uri.toString());
		}
		return null;
	}

}
