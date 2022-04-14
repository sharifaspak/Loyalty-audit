package com.loyalty.marketplace.banners.outbound.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.utils.MarketplaceException;

@Component
public class BannerServiceHelper {

	private static final Logger LOG = LoggerFactory.getLogger(BannerServiceHelper.class);
	private static final String USER_NAME = "username";
	
	@Value("${sapp.custom.header}")
	public String customHeaderSAPP;

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
	 * @param isTokenRequired
	 * @param token
	 * @return header for rest call
	 */
	public HttpHeaders getBannerHeaders(Headers header, boolean isTokenRequired, String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("CUSTOM_HEADER", customHeaderSAPP);
		
		if(!ObjectUtils.isEmpty(header)){

			headers.set(USER_NAME, header.getUserName());
			headers.set(RequestMappingConstants.TRANSACTION_ID, header.getExternalTransactionId());
			
		}
		
		if(isTokenRequired) {
			headers.set(RequestMappingConstants.TOKEN, token);
		}

		return headers;
	}

}