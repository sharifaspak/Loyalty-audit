package com.loyalty.marketplace.offers.outbound.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.loyalty.marketplace.image.outbound.dto.ImageDataResponse;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.decision.manager.inbound.dto.CustomerSegmentDMRequestDto;
import com.loyalty.marketplace.offers.decision.manager.inbound.dto.PromotionalGiftDMRequestDto;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.ApiError;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CustomerSegmentResponse;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CustomerSegmentResult;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.PromotionalGiftResult;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.RuleResult;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.outbound.dto.ComTemplateResponse;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

/**
 * 
 * @author jaya.shukla
 *
 */
@Service
public class DecisionManagerService {

	@Value(OffersConfigurationConstants.DECISION_MANAGER_URI)
	private String decisionManagerUri;

	@Value(OffersConfigurationConstants.CUSTOMER_SEGMENT_CHECK_PATH)
	private String customerSegmentCheck;

	@Value(OffersConfigurationConstants.GET_PROMOTIONAL_GIFT_PATH)
	private String getPromotionalGift;

	@Value("integration.url.getTemplateDetails")
	private String getTemplateDetailsUrl;

	@Autowired
	private ServiceHelper serviceHelper;

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

	private static final Logger LOG = LoggerFactory.getLogger(DecisionManagerService.class);

	/**
	 * 
	 * @param offerEligibilityDto
	 * @param resultResponse
	 * @return list of customer segment for the list of members
	 * @throws MarketplaceException
	 */
	public CustomerSegmentResult checkCustomerSegment(CustomerSegmentDMRequestDto decisionManagerDto, Headers header,
			ResultResponse resultResponse) throws MarketplaceException {
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(),
				OfferConstants.CUSTOMER_CHECK_METHOD.get());
		LOG.info(log);
		String url = decisionManagerUri + customerSegmentCheck;
		CustomerSegmentResult decisionManagerResult = null;

		try {

			log = Logs.logForServiceUrl(url);
			LOG.info(log);
			HttpEntity<CustomerSegmentDMRequestDto> requestEntity = new HttpEntity<>(decisionManagerDto,
					serviceHelper.getHeader(header));
			log = Logs.logForServiceRequest(requestEntity);
			LOG.info(log);
			log = Logs.logBeforeHittingService(OfferConstants.DECISION_MANAGER_SERVICE.get());
			LOG.info(log);
			ResponseEntity<CommonApiStatus> response = retryCallForCheckCustomerSegment(url, requestEntity, header);
			log = Logs.logAfterHittingService(OfferConstants.DECISION_MANAGER_SERVICE.get());
			LOG.info(log);
			CommonApiStatus commonApiStatus = response.getBody();
			log = Logs.logForServiceResponse(commonApiStatus);
			LOG.info(log);

			if (Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus),
					OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED, resultResponse)) {

				ApiStatus apiStatus = commonApiStatus.getApiStatus();

				if (apiStatus.getStatusCode() == 0) {

					decisionManagerResult = getCustomerSegmentResult(commonApiStatus);

				} else {

					if (!CollectionUtils.isEmpty(apiStatus.getErrors())) {

						for (ApiError apiError : apiStatus.getErrors()) {
							resultResponse.addErrorAPIResponse(apiError.getCode(), apiError.getMessage());
						}

					}

				}

			}

		} catch (RestClientException e) {

			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CUSTOMER_CHECK_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.CUSTOMER_SEGMENT_REST_CLIENT_EXCEPTION);

		} catch (Exception e) {

			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CUSTOMER_CHECK_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.CUSTOMER_SEGMENT_CHECK_RUNTIME_EXCEPTION);

		}

		log = Logs.logForServiceResponse(decisionManagerResult);
		LOG.info(log);
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(),
				OfferConstants.CUSTOMER_CHECK_METHOD.get());
		LOG.info(log);
		return decisionManagerResult;
	}

	private ResponseEntity<CommonApiStatus> retryCallForCheckCustomerSegment(String url,
			HttpEntity<CustomerSegmentDMRequestDto> requestEntity, Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForCheckCustomerSegment method of class {}",
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

	/***
	 * 
	 * @param commonApiStatus
	 * @return the list of rule check result
	 * 
	 */
	private CustomerSegmentResult getCustomerSegmentResult(CommonApiStatus commonApiStatus) {

		CustomerSegmentResult decisionManagerResult = null;
		Object result = commonApiStatus.getResult();

		if (!ObjectUtils.isEmpty(result)) {

			CustomerSegmentResponse dmResponse = (CustomerSegmentResponse) serviceHelper.convertToObject(result,
					CustomerSegmentResponse.class);

			if (!ObjectUtils.isEmpty(dmResponse) && !CollectionUtils.isEmpty(dmResponse.getMemberDetails())) {

				List<RuleResult> memberDetails = new ArrayList<>(dmResponse.getMemberDetails().size());

				for (Object ruleObject : dmResponse.getMemberDetails()) {

					RuleResult ruleResult = (RuleResult) serviceHelper.convertToObject(ruleObject, RuleResult.class);
					memberDetails.add(ruleResult);

				}

				if (!CollectionUtils.isEmpty(memberDetails)) {

					decisionManagerResult = new CustomerSegmentResult();
					decisionManagerResult.setRulesResult(memberDetails);

				}
			}

		}

		return decisionManagerResult;

	}

	/**
	 * 
	 * @param promotionalGiftDto
	 * @param header
	 * @param resultResponse
	 * @return promotional gift from DM
	 * @throws MarketplaceException
	 */
	public PromotionalGiftResult getPromotionalGift(PromotionalGiftDMRequestDto req, Headers header,
			ResultResponse resultResponse) throws MarketplaceException {
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(),
				OfferConstants.GET_PROMOTIONAL_GIFT_METHOD.get());
		LOG.info(log);
		String url = decisionManagerUri + getPromotionalGift;
		PromotionalGiftResult promotionalGiftResult = null;

		try {

			log = Logs.logForServiceUrl(url);
			LOG.info(log);
			HttpEntity<PromotionalGiftDMRequestDto> requestEntity = new HttpEntity<>(req,
					serviceHelper.getHeader(header));
			log = Logs.logForServiceRequest(requestEntity);
			LOG.info(log);
			log = Logs.logBeforeHittingService(OfferConstants.DECISION_MANAGER_SERVICE.get());
			LOG.info(log);
			ResponseEntity<CommonApiStatus> response = retryCallForGetPromotionalGift(url, requestEntity, header);
			log = Logs.logAfterHittingService(OfferConstants.DECISION_MANAGER_SERVICE.get());
			LOG.info(log);
			CommonApiStatus commonApiStatus = response.getBody();
			log = Logs.logForServiceResponse(commonApiStatus);
			LOG.info(log);

			if (Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus),
					OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED, resultResponse)) {

				ApiStatus apiStatus = commonApiStatus.getApiStatus();

				if (apiStatus.getStatusCode() == 0) {

					promotionalGiftResult = (PromotionalGiftResult) serviceHelper
							.convertToObject(commonApiStatus.getResult(), PromotionalGiftResult.class);

					if (!ObjectUtils.isEmpty(promotionalGiftResult) && !promotionalGiftResult.isStatus()) {

						Responses.setResponseWithMessageAfterConditionCheck(
								!ObjectUtils.isEmpty(promotionalGiftResult) && promotionalGiftResult.isStatus(),
								OfferErrorCodes.PROMOTIONAL_GIFT_DM_CHECK_FAILED, promotionalGiftResult.getReason(),
								resultResponse);

					} else if (ObjectUtils.isEmpty(promotionalGiftResult)) {

						Responses.setErrorResponse(resultResponse, OfferErrorCodes.PROMOTIONAL_GIFT_DM_CHECK_FAILED);

					}

				} else {

					if (!CollectionUtils.isEmpty(apiStatus.getErrors())) {

						for (ApiError apiError : apiStatus.getErrors()) {
							resultResponse.addErrorAPIResponse(apiError.getCode(), apiError.getMessage());
						}

					}

				}

			}

		} catch (RestClientException e) {

			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_PROMOTIONAL_GIFT_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.PROMOTIONAL_GIFT_REST_CLIENT_EXCEPTION);

		} catch (Exception e) {

			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_PROMOTIONAL_GIFT_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.PROMOTIONAL_GIFT_CHECK_RUNTIME_EXCEPTION);

		}

		log = Logs.logForServiceResponse(promotionalGiftResult);
		LOG.info(log);
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(),
				OfferConstants.GET_PROMOTIONAL_GIFT_METHOD.get());
		LOG.info(log);
		return promotionalGiftResult;
	}

	private ResponseEntity<CommonApiStatus> retryCallForGetPromotionalGift(String url,
			HttpEntity<PromotionalGiftDMRequestDto> requestEntity, Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForGetPromotionalGift method of class {}",
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
	 * @param templateId
	 * @return
	 * @throws VoucherManagementException
	 */
	public ComTemplateResponse getTemplateDetails(String templateId) throws VoucherManagementException {
		// ?comTemplateId=1996
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getTemplateDetailsUrl)
				.queryParam("comTemplateId", templateId);
		URI finalUrl = builder.build().toUri();
		LOG.info("Request Object Voucher service class getTemplateDetails in put url is {}", finalUrl);
		try {
			ComTemplateResponse templateObj = retryCallForGetTemplateDetails(finalUrl);
			LOG.info("Response Object Voucher service class getComTemplateList {}", templateObj);
			return templateObj;
		} catch (Exception e) {
			LOG.info("Exception in Voucher service class getTemplateDetails");
			throw new VoucherManagementException(VoucherManagementCode.VOUCHER_GIFT_TEMPLATE_ERROR);
		}
	}

	private ComTemplateResponse retryCallForGetTemplateDetails(URI url) {

		LOG.info("inside Retry block using retryTemplate of retryCallForGetTemplateDetails method of class {}",
				this.getClass().getName());
		return retryTemplate.execute(context -> {
			return restTemplate.getForObject(url, ComTemplateResponse.class);
		});
	}

}
