package com.loyalty.marketplace.offers.outbound.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CustomerTypeListResult;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.outbound.service.dto.IncludeMemberDetails;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.Requests;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;


/**
 * 
 * @author jaya.shukla
 *
 */
@Service
@SuppressWarnings({OffersConfigurationConstants.UNCHECKED})
public class MemberManagementService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MemberManagementService.class);
	
	@Value(OffersConfigurationConstants.MEMBER_MANAGEMENT_URI)
    private String memberManagementUri;	
	
	@Value(OffersConfigurationConstants.GET_MEMBER_PATH)
    private String memberPath;
		
	@Value(OffersConfigurationConstants.ALL_CUSTOMER_TYPE_PATH)
    private String allCusomerTypePath;
	
	@Value(OffersConfigurationConstants.GET_BIRTHDAY_DETAILS_PATH)
	private String getBirthdayDetailPath;
	
	@Autowired
	private ServiceHelper serviceHelper;
		
	/***
	 * 
	 * @param accountNumber
	 * @param header 
	 * @param includeEligibilityMatrix
	 * @param includeEligiblePaymentMethods
	 * @param resultResponse
	 * @return member response to get full member details including eligibility matrix
	 * @throws MarketplaceException
	 */
	public GetMemberResponseDto  getMemberDetails(String accountNumber, IncludeMemberDetails includeMemberDetails, Headers header, ResultResponse resultResponse) throws MarketplaceException {	
		
		
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_MEMBER_FULL_DETAILS_METHOD.get());
		LOG.info(log);
		String url = memberManagementUri + memberPath;
		GetMemberResponseDto memberDetails = null;
		
		try {
			
			CommonApiStatus commonApiStatus = serviceHelper.getMethodPostObjectForRetrieval(url, header, Requests.getMemberRequestDto(accountNumber, includeMemberDetails), OfferConstants.MEMBER_MANAGEMENT_SERVICE.get(), resultResponse);
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus), OfferErrorCodes.MEMBER_MANAGEMENT_RESPONSE_NOT_RECEIVED, resultResponse)
			&& Checks.checkValidResponseWithNoErrors(resultResponse, commonApiStatus.getApiStatus())) {
				memberDetails = extractMemberDetails(commonApiStatus, resultResponse);	
			} 
			
		} catch (Exception e) {			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_MEMBER_FULL_DETAILS_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_MEMBER_DETAILS_RUNTIME_EXCEPTION);		
		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_MEMBER_FULL_DETAILS_METHOD.get());
		LOG.info(log);
        return memberDetails;
	}
	
	/***
	 * 
	 * @param commonApiStatus
	 * @param resultResponse
	 * @param accountNumber
	 * @return extract member details from received hash map
	 */
	private GetMemberResponseDto extractMemberDetails(CommonApiStatus commonApiStatus, ResultResponse resultResponse) {
		
		GetMemberResponseDto getMemberResponse = null;
		LinkedHashMap<String, GetMemberResponseDto> resultLinkedHashMap = (LinkedHashMap<String, GetMemberResponseDto>) commonApiStatus
				.getResult();
		
		if(!ObjectUtils.isEmpty(resultLinkedHashMap)) {
			
			Object result = resultLinkedHashMap.get(OfferConstants.MEMBER_HASH_MAP_NAME.get());
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(result), OfferErrorCodes.MEMBER_NOT_AVAILABLE, resultResponse)) {
				getMemberResponse =  (GetMemberResponseDto) serviceHelper.convertToObject(result, GetMemberResponseDto.class);
				Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(getMemberResponse), OfferErrorCodes.MEMBER_NOT_AVAILABLE, resultResponse);
			} 
			
		}
		return getMemberResponse;
	}

	/**
	 * 
	 * @param resultResponse
	 * @return list of all customer types with parent types fetched from member management microservice
	 * @throws MarketplaceException
	 */
	public List<ParentChlidCustomer> getAllCustomerTypesWithParent(ResultResponse resultResponse, Headers header) throws MarketplaceException {
	
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_ALL_CUSTOMER_TYPES_METHOD.get());
		LOG.info(log);
		String url = memberManagementUri + allCusomerTypePath;
		List<ParentChlidCustomer> customerTypeList = null;
		
		try {
			
			CommonApiStatus commonApiStatus = serviceHelper.getMethodGetObject(url, header, OfferConstants.MEMBER_MANAGEMENT_SERVICE.get());
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus), OfferErrorCodes.MEMBER_MANAGEMENT_RESPONSE_NOT_RECEIVED, resultResponse)
			&& Checks.checkValidResponseWithNoErrors(resultResponse, commonApiStatus.getApiStatus())) {
						
				CustomerTypeListResult customerTypeListResult = (CustomerTypeListResult) serviceHelper.convertToObject(commonApiStatus.getResult(),
						CustomerTypeListResult.class);	
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(customerTypeListResult), OfferErrorCodes.CUSTOMER_TYPE_LIST_EMPTY, resultResponse)) {
					
					customerTypeList =  customerTypeListResult.getCustomerTypeList();
				}
						
			}
			
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_ALL_CUSTOMER_TYPES_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_CUSTOMER_TYPES_REST_CLIENT_EXCEPTION);
			
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_ALL_CUSTOMER_TYPES_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_CUSTOMER_TYPES_RUNTIME_EXCEPTION);
			
		}
	
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_ALL_CUSTOMER_TYPES_METHOD.get());
		LOG.info(log);
		return  customerTypeList;
		
	}
	
	/**
	 * 
	 * @return
	 * @throws MarketplaceException
	 */
	public List<BirthdayAccountsDto> getBirthdayAccountDetails(String noOfdaysToadd, Headers header) throws MarketplaceException {

		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_BIRTHDAY_ACCOUNT_DETAILS.get());
		LOG.info(log);
		String url = memberManagementUri + getBirthdayDetailPath;
        BirthdayAccountsResponse birthdayAccountsResponse = null;
		List<BirthdayAccountsDto> listBirthdayAccountsDto = null;

		try {
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam(OffersRequestMappingConstants.DAYS_TO_ADD, noOfdaysToadd);
			
			CommonApiStatus commonApiStatus = serviceHelper.getMethodGetObject(builder.toUriString(), header, OfferConstants.MEMBER_MANAGEMENT_SERVICE.get());
			
			if (commonApiStatus != null) {
				ApiStatus apiStatus = commonApiStatus.getApiStatus();
				Object result = commonApiStatus.getResult();
				if (apiStatus.getStatusCode() == 0) {
					birthdayAccountsResponse = (BirthdayAccountsResponse) serviceHelper
							.convertToObject(result, BirthdayAccountsResponse.class);
					
					if (birthdayAccountsResponse != null && CollectionUtils.isNotEmpty(birthdayAccountsResponse.getAccountList())) {
						
						listBirthdayAccountsDto = birthdayAccountsResponse.getAccountList();
						
					}
				}
			}

		} catch (RestClientException e) {
			throw new MarketplaceException(this.getClass().toString(),
					OfferConstants.GET_BIRTHDAY_ACCOUNT_DETAILS.get(), e.getClass() + e.getMessage(),
					OfferExceptionCodes.GET_BIRTHDAY_DETAILS_REST_CLIENT_EXCEPTION);
		} catch (Exception e) {
			throw new MarketplaceException(this.getClass().toString(),
					OfferConstants.GET_BIRTHDAY_ACCOUNT_DETAILS.get(), e.getClass() + e.getMessage(),
					OfferExceptionCodes.GET_BIRTHDAY_DETAILS_RUNTIME_EXCEPTION);

		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_BIRTHDAY_ACCOUNT_DETAILS.get());
		LOG.info(log);
		return listBirthdayAccountsDto;
	}
	
	
	@Async(OffersConfigurationConstants.THREAD_POOL_TASK_EXECUTOR)
	public void refreshCrmInfo(String accountNumber, Headers headers) {
		LOG.info("Entering refreshCrmInfo : {}", headers.getExternalTransactionId());

		String url = memberManagementUri + "/refreshCrmInfo/" + accountNumber;
		CommonApiStatus commonApiStatus = serviceHelper.postMethodPostObject(url, headers, OfferConstants.MEMBER_MANAGEMENT_SERVICE.get());
		
		if (commonApiStatus != null) {
			ApiStatus apiStatus = commonApiStatus.getApiStatus();
			if (apiStatus.getStatusCode() == 0) {
				LOG.info("RefreshCrmInfo Response Message : {}", apiStatus.getMessage());
			}
		} 
		
		LOG.info("Exiting refreshCrmInfo : {}", headers.getExternalTransactionId());
	}
	
}