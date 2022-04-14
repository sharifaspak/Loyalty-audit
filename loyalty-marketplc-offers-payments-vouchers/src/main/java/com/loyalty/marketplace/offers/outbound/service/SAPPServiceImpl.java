package com.loyalty.marketplace.offers.outbound.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.offers.outbound.service.dto.SAPPRequest;
import com.loyalty.marketplace.offers.outbound.service.dto.cacheOffersResponse;
import com.loyalty.marketplace.utils.Logs;

@Service
public class SAPPServiceImpl {
	
	private static final Logger LOG = LoggerFactory.getLogger(SAPPServiceImpl.class);

	@Autowired
	@Qualifier("customRestTemplateBean")
	private RestTemplate customRestTemplate;
	
	public SAPPServiceImpl(RestTemplate customRestTemplate,JmsTemplate jmsTemplate) {
		super();
		this.customRestTemplate = customRestTemplate;
		
	}
	
	@Value("${smiles.app.uri}")
	private String sappUri;
	
	@Value("${smiles.app.username}")
	private String userName;
	
	@Value("${smiles.app.password}")
	private String password;
	
	
	/**
	 * This method used for refreshing cache details.
	 * 
	 * @param userName
	 * @param password
	 * @param ListDeviceDetails
	 * @return {@link cacheOffersResponse}
	 * @throws RestServicesException
	 */
	@Async
	public void refreshEligibleOffersCache() {
		
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), "refreshEligibleOffersCache");
		LOG.info(log);
		
		SAPPRequest sappRequest = new SAPPRequest();
		sappRequest.setUserName(userName);
		sappRequest.setPassword(password);
         
		log = Logs.logForServiceUrl(sappUri);
		LOG.info(log);
				
		log = Logs.logBeforeHittingService("Refresh Cache");
		LOG.info(log);
		
        ResponseEntity<cacheOffersResponse> responseJsonString = customRestTemplate.postForEntity(sappUri,
        		sappRequest, cacheOffersResponse.class);  
        
        log = Logs.logAfterHittingService("Refresh Cache");
		LOG.info(log);
        
		log = Logs.logForServiceResponse(responseJsonString.getBody());
		LOG.info(log);
        
//        if (null != responseJsonString) {
//        	cacheOffersResponse responseJson = responseJsonString.getBody();
//			String responseJsonObject = ObjectMapperUtil.INSTANCE.convertToJSon(sappRequest);
//		}
        
        log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), "refreshEligibleOffersCache");
		LOG.info(log);
	}

}
