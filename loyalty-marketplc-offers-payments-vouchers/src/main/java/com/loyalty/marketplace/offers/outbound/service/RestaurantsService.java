package com.loyalty.marketplace.offers.outbound.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.Restaurant;
import com.loyalty.marketplace.offers.outbound.dto.RestaurantDto;
import com.loyalty.marketplace.offers.outbound.service.dto.RestaurantResult;
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
@Service
public class RestaurantsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(RestaurantsService.class);
	private static final String SERVICE = "restaurants";
	private static final String FETCH_RESTAURANT_LIST = "fetchRestaurantList";
	private static final String API_KEY = "api-key";
	private static final String API_SECRET = "api-secret";
	
	@Value(OffersConfigurationConstants.RESTAURANTS_URI)
    private String restaurantUri;	
	
	@Value(OffersConfigurationConstants.RESTAURANTS_API_KEY)
	private String apiKey;

	@Value(OffersConfigurationConstants.RESTAURANTS_API_SECRET)
	private String apiSecret;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private RetryTemplate retryTemplate;
	
	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;
	
	/**
	 * 
	 * @param partnerCode
	 * @param partnerActivityDto
	 * @param header
	 * @param resultResponse
	 * @return list of retaurants
	 * @throws MarketplaceException
	 */
	public List<RestaurantDto> fetchRestaurantList(Headers header,
			ResultResponse resultResponse) throws MarketplaceException {

		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), FETCH_RESTAURANT_LIST);
		LOG.info(log);
		
		List<RestaurantDto> restaurantList = null;
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set(API_KEY, apiKey);
			headers.set(API_SECRET, apiSecret);
			log = Logs.logForServiceUrl(restaurantUri);
			LOG.info(log);

			HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
			log = Logs.logForServiceRequest(requestEntity);
			LOG.info(log);
			log = Logs.logBeforeHittingService(SERVICE);
			LOG.info(log);
			
			ResponseEntity<RestaurantResult> responseEntity = retryCallForFetchingRestaurantList(restaurantUri, requestEntity, header);
			log = Logs.logAfterHittingService(SERVICE);
			LOG.info(log);
			log = Logs.logForServiceResponse(responseEntity.getBody());
			LOG.info(log);
			
			if (!ObjectUtils.isEmpty(responseEntity)) {
				
				RestaurantResult result = responseEntity.getBody();
				
				if(!ObjectUtils.isEmpty(result)
				&& !CollectionUtils.isEmpty(result.getResults())) {
					
					restaurantList = new ArrayList<>(result.getResults().size());
					
					for(Restaurant res : result.getResults()) {
						
						restaurantList.add(new RestaurantDto(res.getRestaurantNameEn(),
								res.getRestaurantNameAr(), res.getType()));
					}
					
					
				} else {
					resultResponse.addErrorAPIResponse(Integer.valueOf(result.getError()),
							result.getMessage());
				}
			}	
			
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), FETCH_RESTAURANT_LIST,
					e.getClass() + e.getMessage(), OfferExceptionCodes.RESTAURANTS_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), FETCH_RESTAURANT_LIST,
					e.getClass() + e.getMessage(), OfferExceptionCodes.GENERIC_RUNTIME_EXCEPTION);
			
		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), FETCH_RESTAURANT_LIST);
		LOG.info(log);
		return restaurantList;
		 
	}
	
	/**
	 * 
	 * @param url
	 * @param headerEntity
	 * @param header
	 * @return list of restaurants
	 */
	private ResponseEntity<RestaurantResult> retryCallForFetchingRestaurantList(String url, HttpEntity<?> headerEntity,
			Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForFetchingRestaurantList method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						headerEntity.toString(), header.getUserName());
				return (ResponseEntity<RestaurantResult>) restTemplate.exchange(url, HttpMethod.GET, headerEntity, RestaurantResult.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}

}
