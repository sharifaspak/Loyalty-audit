package com.loyalty.marketplace.offers.outbound.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityConfigDto;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ListPartnerActivityDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ListProgramActivityDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.PartnerActivityResponseDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.utils.Checks;
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
public class MemberActivityService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MemberActivityService.class);
	
	@Value(OffersConfigurationConstants.MEMBER_ACTIVITY_URI)
    private String memberActivityUri;	
	
	@Value(OffersConfigurationConstants.PARTNER_ACTIVITY)
    private String partnerActivity;
	
	@Value(OffersConfigurationConstants.PROGRAM_ACTIVITY_LIST)
    private String programActivityList;
	
	@Autowired
	private ServiceHelper serviceHelper;
	
	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * 
	 * @param partnerCode
	 * @param partnerActivityDto
	 * @param resultResponse
	 * @return Status of activity code creation in member management micro service
	 * @throws MarketplaceException
	 */
	public String createPartnerActivity(String partnerCode, PartnerActivityDto partnerActivityDto, Headers header,
			ResultResponse resultResponse) throws MarketplaceException {

		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), OfferConstants.CREATE_PARTNER_ACTIVITY_METHOD.get());
		LOG.info(log);
		String url = memberActivityUri + OfferConstants.FORWARD_SLASH.get() + partnerCode + partnerActivity;
		String activityId = null;
		
		try {
			
			CommonApiStatus commonApiStatus = serviceHelper.getMethodPostObject(url, header, partnerActivityDto, OfferConstants.MEMBER_ACTIVITY_SERVICE.get(), resultResponse);
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus), OfferErrorCodes.MEMBER_ACTIVITY_RESPONSE_NOT_RECEIVED, resultResponse)
			&& Checks.checkValidResponseWithNoErrorsForActivityCode(resultResponse, commonApiStatus.getApiStatus())) {
				
				if(!Checks.checkActivityCodeExists(resultResponse)) {
				
					PartnerActivityResponseDto partnerActivityResponse = (PartnerActivityResponseDto) serviceHelper.convertToObject(commonApiStatus.getResult(), PartnerActivityResponseDto.class);
					activityId = !ObjectUtils.isEmpty(partnerActivityResponse) && !StringUtils.isEmpty(partnerActivityResponse.getActivityId()) ? partnerActivityResponse.getActivityId() : null;
				
				} else {
				
					Responses.removeAllErrors(resultResponse);
					commonApiStatus = serviceHelper.getMethodGetObject(url, header, OfferConstants.MEMBER_ACTIVITY_SERVICE.get());
					
					if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus), OfferErrorCodes.MEMBER_ACTIVITY_RESPONSE_NOT_RECEIVED, resultResponse)
					&& Checks.checkValidResponseWithNoErrors(resultResponse, commonApiStatus.getApiStatus())) { 
					
						ListPartnerActivityDto partnerActivivtyList = (ListPartnerActivityDto) serviceHelper.convertToObject(commonApiStatus.getResult(), ListPartnerActivityDto.class);
						
						if(!ObjectUtils.isEmpty(partnerActivivtyList) 
						&& !CollectionUtils.isEmpty(partnerActivivtyList.getListPartnerActivity())) {
							
							PartnerActivityDto activity = partnerActivivtyList.getListPartnerActivity().stream()
									.filter(a->a.getActivityCode().getCode().equals(partnerActivityDto.getActivityCode().getCode())).findAny().orElse(null);  
							activityId = !ObjectUtils.isEmpty(activity)
									&& !ObjectUtils.isEmpty(activity.getLoyaltyActivity())
									? activity.getLoyaltyActivity().getActivityId()
									: null;
						}
						
					}
					
				}
			}	
			
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CREATE_PARTNER_ACTIVITY_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_PARTNER_ACTIVITY_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CREATE_PARTNER_ACTIVITY_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_PARTNER_ACTIVITY_RUNTIME_EXCEPTION);
			
		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), OfferConstants.CREATE_PARTNER_ACTIVITY_METHOD.get());
		LOG.info(log);
		return activityId;
		 
	}

	
	/**
	 * 
	 * @param partnerCode
	 * @param partnerActivityDto
	 * @param resultResponse
	 * @return Status of activity code creation in member management micro service
	 * @throws MarketplaceException
	 */
	public boolean updatePartnerActivity(String partnerCode, String activityId, PartnerActivityConfigDto partnerActivityDto, Headers header,
			ResultResponse resultResponse) throws MarketplaceException {

		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), OfferConstants.UPDATE_PARTNER_ACTIVITY_METHOD.get());
		LOG.info(log);
		String url = memberActivityUri + OfferConstants.FORWARD_SLASH.get() + partnerCode + partnerActivity + OfferConstants.FORWARD_SLASH.get() + activityId;
		
		try {
			
			CommonApiStatus commonApiStatus = serviceHelper.getMethodPostObject(url, header, partnerActivityDto, OfferConstants.MEMBER_ACTIVITY_SERVICE.get(), resultResponse);
			Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus), OfferErrorCodes.MEMBER_ACTIVITY_RESPONSE_NOT_RECEIVED, resultResponse);
			Checks.checkValidResponseWithNoErrors(resultResponse, commonApiStatus.getApiStatus());
						
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_PARTNER_ACTIVITY_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.UPDATE_PARTNER_ACTIVITY_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_PARTNER_ACTIVITY_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.UPDATE_PARTNER_ACTIVITY_RUNTIME_EXCEPTION);
			
		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), OfferConstants.UPDATE_PARTNER_ACTIVITY_METHOD.get());
		LOG.info(log);
		return Checks.checkNoErrors(resultResponse);
		 
	}
	
	/**
	 * 
	 * @param header
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 * @throws MarketplaceException
	 */
    public List<ProgramActivityWithIdDto> getProgramActivityList(Headers header, ResultResponse resultResponse) throws MarketplaceException {
		
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_PROGRAM_ACTIVITY_LIST_METHOD.get());
		LOG.info(log);
		String url = memberActivityUri + programActivityList ;
		List<ProgramActivityWithIdDto> listprogramActivities = null;
		
		try {
			
			CommonApiStatus commonApiStatus = serviceHelper.getMethodGetObject(url, header, OfferConstants.MEMBER_ACTIVITY_SERVICE.get());
			
			if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(commonApiStatus), OfferErrorCodes.MEMBER_ACTIVITY_RESPONSE_NOT_RECEIVED, resultResponse)
			&& Checks.checkValidResponseWithNoErrors(resultResponse, commonApiStatus.getApiStatus())) {
				
				ListProgramActivityDto listProgramActivityDto = (ListProgramActivityDto) serviceHelper.convertToObject(commonApiStatus.getResult(), ListProgramActivityDto.class);
				
				listprogramActivities = !ObjectUtils.isEmpty(listProgramActivityDto)
						? listProgramActivityDto.getListProgramActivity()
						: null;
				
			}
			
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_PROGRAM_ACTIVITY_LIST_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_PROGRAM_ACTIVITY_LIST_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_PROGRAM_ACTIVITY_LIST_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_PROGRAM_ACTIVITY_LIST_RUNTIME_EXCEPTION);
			
		}
		
		log = Logs.logsForLeavingServiceMethod(this.getClass().getSimpleName(), OfferConstants.GET_PROGRAM_ACTIVITY_LIST_METHOD.get());
		LOG.info(log);
		return listprogramActivities;
		
	}
	    
}
