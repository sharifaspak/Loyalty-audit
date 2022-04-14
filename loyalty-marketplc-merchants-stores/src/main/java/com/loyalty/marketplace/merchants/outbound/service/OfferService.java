package com.loyalty.marketplace.merchants.outbound.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantOffers;
import com.loyalty.marketplace.merchants.outbound.service.dto.Headers;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.stores.inbound.restcontroller.StoreController;
import com.loyalty.marketplace.utils.MarketplaceException;

@Service
public class OfferService {

	private static final Logger LOG = LoggerFactory.getLogger(OfferService.class);
	
	@Value("${marketplace.uri}")
	private String uri;

	@Value("${marketplace.merchant.path}")
	private String merchantPath;

	@Value("${marketplace.merchant.offers.path}")
	private String merchantofferPath;

	@Value("${marketplace.merchant.offers.offerCatalogs}")
	private String offerCatalogs;

	@Value("${marketplace.merchant.offers.offerType}")
	private String offerType;
	
	@Value("${marketplace.offers.update.eligibleOffers}")
	private String updateEligibleOfferUrl;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	ObjectMapper mapper = new ObjectMapper();

	public List<MerchantOffers> getOfferDetails(String merchantCode,String token) throws IOException {

		List<MerchantOffers> listMerchnantOffers = new ArrayList();

		String url = uri + merchantPath + merchantCode + merchantofferPath;
		

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(RequestMappingConstants.TOKEN, token);
		HttpEntity<Object> request=new HttpEntity<>(headers);
		String jsonStr = getRestTemplate.exchange(url,HttpMethod.GET, request, String.class).getBody();
		Map<String, List<?>> map = mapper.readValue(jsonStr, new TypeReference<HashMap>() {
		});
		Set<Entry<String, List<?>>> keys = map.entrySet();
		for (Entry<String, List<?>> key : keys) {
			if (key.getKey().equalsIgnoreCase(offerCatalogs)) {
				List<?> offerCatalogsList = key.getValue();
				for (int i = 0; i < offerCatalogsList.size(); i++) {
					listMerchnantOffers.add(mapper.convertValue(mapper.convertValue(key.getValue().get(i), Map.class),
							MerchantOffers.class));
				}

			}
		}
		return listMerchnantOffers;
	}

	public List<String> getMerchantsForOfferType(String offer,String token) {
		List<String> listMerchantIds = new ArrayList<>();
		String url = uri + merchantofferPath + offerType + offer;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(RequestMappingConstants.TOKEN, token);
		HttpEntity<Object> request=new HttpEntity<>(headers);
		String[] body = getRestTemplate.exchange(url,HttpMethod.GET, request, String[].class).getBody();
		if (body != null) {
			listMerchantIds = Arrays.asList(body);
		}
		return listMerchantIds;
	}

	@Async("threadPoolTaskExecutor")
	public void updateEligibleOffers(Headers headers) {
		
		LOG.info("Entering OfferService - updateEligibleOffers");
		
		try {

			//restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			HttpEntity<?> headerEntity = new HttpEntity<>(getHeader(headers));
			restTemplate.exchange(updateEligibleOfferUrl,  HttpMethod.GET, headerEntity, ResultResponse.class);
			
		} catch (RestClientException e) {
			
			LOG.error(new MarketplaceException(this.getClass().toString(), "updateEligibleOffers",
					e.getClass() + e.getMessage(), MerchantCodes.UPDATE_ELIGIBLE_OFFERS_FAILED).printMessage());
			
		} catch (Exception e) {
			
			LOG.error(new MarketplaceException(this.getClass().toString(), "updateEligibleOffers",
					e.getClass() + e.getMessage(), MerchantCodes.UPDATE_ELIGIBLE_OFFERS_FAILED).printMessage());
			
		}
		
		LOG.info("Leaving OfferService - updateEligibleOffers");
		
	}
	
	public HttpHeaders getHeader(Headers header) {

		HttpHeaders headers = new HttpHeaders();

		final List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		headers.setAccept(list);

		if(!ObjectUtils.isEmpty(header)){

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
}
