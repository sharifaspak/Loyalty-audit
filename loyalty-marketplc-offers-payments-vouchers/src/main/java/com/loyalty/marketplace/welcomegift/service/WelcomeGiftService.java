package com.loyalty.marketplace.welcomegift.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftDMRequest;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftDMResponseDto;

@Service
public class WelcomeGiftService {

	@Autowired
	private ServiceHelper serviceHelper;

	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	WelcomeGiftDMService welcomeGiftDMService;
 

	private static final Logger LOG = LoggerFactory.getLogger(WelcomeGiftService.class);

	/**
	 * @param accountNumber
	 * @param result
	 * @return
	 */

	public String getWelcomeGiftType(String accountNumber, String customerType, String channel, ResultResponse result, Headers header) {

		WelcomeGiftDMRequest welcomeGiftDMRequest = new WelcomeGiftDMRequest();
		welcomeGiftDMRequest.setAccountNumber(accountNumber);
		welcomeGiftDMRequest.setCustomerType(customerType);
		welcomeGiftDMRequest.setChannelId(channel);

		WelcomeGiftDMResponseDto decisionManagerResponseDto = welcomeGiftDMService.callDecisionManager(welcomeGiftDMRequest, result);
		
		if(decisionManagerResponseDto != null) {
			return decisionManagerResponseDto.getWelcomeGift();
		}
		return null;


	}

}
