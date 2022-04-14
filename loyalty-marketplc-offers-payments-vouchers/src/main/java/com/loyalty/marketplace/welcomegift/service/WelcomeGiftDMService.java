package com.loyalty.marketplace.welcomegift.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Backoff;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.ApiError;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.promocode.inbound.dto.DecisionManagerResponseDto;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoCodeDMRequest;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherController;
import com.loyalty.marketplace.welcomegift.constants.WelcomeGiftCodes;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftDMRequest;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftDMResponseDto;

@Service
public class WelcomeGiftDMService {

	@Value("${decisionManager.uri}")
	private String decisionManagerUri;	

	@Value("${decisionManager.welcomeGift.rule}")
	private String giftTypeCheck;

	@Autowired
	private ServiceHelper serviceHelper;

	@Autowired
	FetchServiceValues fetchServiceValues;

	private static int count = 0 ;

	private static final Logger LOG = LoggerFactory.getLogger(WelcomeGiftDMService.class);

	/**
	 * @param accountNumber
	 * @param result
	 * @return
	 */

	@Retryable(value = {Throwable.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	public WelcomeGiftDMResponseDto callDecisionManager(WelcomeGiftDMRequest welcomeGiftDMRequest, ResultResponse resultResponse) {
		
		count++;

		LOG.info("RetryableMethod callDecisionManager~~~~~attempt: "+ count);

		String url = decisionManagerUri + giftTypeCheck;
		RestTemplate restTemplate = new RestTemplate();
		WelcomeGiftDMResponseDto decisionManagerResponseDTO = null;

		LOG.info("Welcome Gift Decision Manager Call URL : " + url);

//		try {

			HttpEntity<WelcomeGiftDMRequest> requestEntity = new HttpEntity<>(welcomeGiftDMRequest);

			LOG.info("requestEntity: "+ requestEntity);

			/////------------attempt ends

			ResponseEntity<CommonApiStatus> response = restTemplate.exchange(url, HttpMethod.POST, 
					requestEntity, CommonApiStatus.class);

			LOG.info("response: "+ response);

			CommonApiStatus commonApiStatus = response.getBody();
			LOG.info("commonApiStatus: "+ commonApiStatus);


			if(null!=commonApiStatus) {

				ApiStatus apiStatus = commonApiStatus.getApiStatus();

				LOG.info("apiStatus: "+ apiStatus);

				if (apiStatus.getStatusCode() == 0) {
					LOG.info("apiStatus.getStatusCode(): "+ apiStatus.getStatusCode());

					decisionManagerResponseDTO = (WelcomeGiftDMResponseDto) serviceHelper.convertToObject(
							response.getBody().getResult(), WelcomeGiftDMResponseDto.class);
					LOG.info("decisionManagerResponseDTO: "+ decisionManagerResponseDTO);


				} else {
					resultResponse.setErrorAPIResponse(apiStatus.getStatusCode(), apiStatus.getOverallStatus());
					LOG.info("resultResponse: "+ resultResponse.getApiStatus().getErrors());
				}
			} 
//			else {
//
//				resultResponse.addErrorAPIResponse(OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getIntId(), 
//						OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getMsg());	
//			}	
//
//		} 
//		catch (RestClientException e) {
//
//			e.printStackTrace();
//			//LOG.error("{}", e.getMessage());
//		}
//		catch (Exception e) {
//
//			e.printStackTrace();
//			//LOG.error("{}", e.getMessage());
//		}

		LOG.info("inside callDecisionManager for WelcomeFGift, decisionManagerResult {}", decisionManagerResponseDTO);
		return decisionManagerResponseDTO;

		// TODO Auto-generated method stub

	}

	

	@Recover
	private WelcomeGiftDMResponseDto recoverDM(Throwable t, WelcomeGiftDMRequest welcomeGiftDMRequest, ResultResponse resultResponse) {

		LOG.info("####################### Falling back to DM recover method ##############################");

		LOG.info("DM Retry failure -- " + t.getClass().getName());

		LOG.info("WelcomeGiftDMRequest: "+ welcomeGiftDMRequest);

		resultResponse.addErrorAPIResponse(OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getIntId(),OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getMsg());

		LOG.info("DM Recover resultResponse: "+ resultResponse.getApiStatus().getErrors());
		
		return null;
	}
 

//
//		@Recover
//	private String recover(Throwable t, String accountNumber, ResultResponse result, Headers header) {
//
//		LOG.info("####################### Falling back to DM recover method ################################# ");
//
//		LOG.info("DM Retry failure -- " + t.getClass().getName());
//
//		LOG.info("accountNumber: "+ accountNumber);
//
//		LOG.info("header: "+ header);
//
////		if() {
//
//		result.addErrorAPIResponse(OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getIntId(),OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getMsg());
//
////		} else {
////			result.addErrorAPIResponse(OfferErrorCodes.MEMBER_MANAGEMENT_RESPONSE_NOT_RECEIVED.getIntId(),OfferErrorCodes.MEMBER_MANAGEMENT_RESPONSE_NOT_RECEIVED.getMsg());
////		}
//
//		LOG.info("Recover result: "+ result.getApiStatus().getErrors());
//
//		//return null;
//
//		return "recovered";
//	}
//	 

	//	@Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	//	public CommonApiStatus dmRestCall(String url, RestTemplate restTemplate,
	//			HttpEntity<WelcomeGiftDMRequest> requestEntity) {
	//		
	//		
	//
	//		return restTemplate.exchange(url, HttpMethod.POST, 
	//				requestEntity, CommonApiStatus.class).getBody();
	//	}


	//	@Recover
	//	private CommonApiStatus recover(Throwable t) {
	//
	//		LOG.info("####################### Falling back to recover method ################################# ");
	//
	//		LOG.info("Retry failure -- " + t.getClass().getName());
	//
	//		List<ApiError> errors = new ArrayList<>();
	//		ApiError error = new ApiError(OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getIntId(), OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getMsg());
	//		errors.add(error);
	//		ApiStatus apiStatus = new ApiStatus(OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getMsg(), "ERROR", 1, errors);
	//
	//		return new CommonApiStatus(apiStatus, null);
	//	}

}
