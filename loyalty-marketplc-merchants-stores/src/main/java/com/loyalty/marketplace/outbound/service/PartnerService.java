package com.loyalty.marketplace.outbound.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.utils.MarketplaceException;

@Service
public class PartnerService {

	@Value("${partnerManagement.uri}")
    private String partnerUri;
	@Value("${partnerManagement.getPartner.path}")
	private String partnerPath;
	@Value("${partnerManagement.getPartnerAccrualOrBoth.path}")
	private String partnerTypeAccrualBothPath;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	public boolean getPartnerDetails(String partnerCode, String token, String externalTransactionId) throws MarketplaceException {

		String url = partnerUri + partnerPath + partnerCode;
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set(RequestMappingConstants.TOKEN, token);
			headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, externalTransactionId);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			return getRestTemplate.exchange(url,HttpMethod.GET, entity, Boolean.class).getBody();
		} catch(RestClientException rce) {
			throw new MarketplaceException(this.getClass().toString(), "getPartnerDetails",
					rce.getClass() + rce.getMessage(),
					MerchantCodes.ERROR_FETCH_PARTERCODE);
		} catch(NullPointerException npe) {
			throw new MarketplaceException(this.getClass().toString(), "getPartnerDetails",
					npe.getClass() + npe.getMessage(),
					MerchantCodes.ERROR_FETCH_PARTERCODE);
		}

	}
	
	public List<String> getPartnerCodesByPartnerType(String token, String externalTransactionId) throws MarketplaceException {
		String url = partnerUri + partnerTypeAccrualBothPath;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set(RequestMappingConstants.TOKEN, token);
			headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, externalTransactionId);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			List<String> body = getRestTemplate.exchange(url,HttpMethod.GET, entity, List.class).getBody();
			return body;
		} catch (Exception e) {
			throw new MarketplaceException(this.getClass().toString(), "getPartnerType", e.getClass() + e.getMessage(),
					MarketPlaceCode.PARTNER_CHECK_REST_CLIENT_EXCEPTION);
		}
	}

	
}
