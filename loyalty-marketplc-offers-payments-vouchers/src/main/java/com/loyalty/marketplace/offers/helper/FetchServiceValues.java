package com.loyalty.marketplace.offers.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.offers.decision.manager.inbound.dto.CustomerSegmentDMRequestDto;
import com.loyalty.marketplace.offers.decision.manager.inbound.dto.PromotionalGiftDMRequestDto;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CustomerSegmentResult;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.PromotionalGiftResult;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityConfigDto;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;
import com.loyalty.marketplace.offers.outbound.dto.RestaurantDto;
import com.loyalty.marketplace.offers.outbound.dto.RestaurantResultResponse;
import com.loyalty.marketplace.offers.outbound.service.DecisionManagerService;
import com.loyalty.marketplace.offers.outbound.service.MemberActivityService;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.offers.outbound.service.PartnerService;
import com.loyalty.marketplace.offers.outbound.service.RestaurantsService;
import com.loyalty.marketplace.offers.outbound.service.dto.IncludeMemberDetails;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;

/**
 * 
 * @author jaya.shukla
 *
 */
@Component
public class FetchServiceValues {
	
	@Autowired
	MemberManagementService memberManagementService;
	
	@Autowired
	MemberActivityService memberActivityService;
	
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	DecisionManagerService decisionManagerService;
	
	@Autowired
	RestaurantsService restaurantsService;
	
	/**
	 * 
	 * @param resultResponse
	 * @param header
	 * @return retrieved customer type list from member management service with their parent
	 * customer type
	 * @throws MarketplaceException
	 */
	public List<ParentChlidCustomer> getAllCustomerTypes(ResultResponse resultResponse, Headers header) 
			throws MarketplaceException{
		
	    return memberManagementService.getAllCustomerTypesWithParent(resultResponse, header);
		
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param resultResponse
	 * @param header
	 * @return retrieved member details based on acount number from member management
	 * service combined in one object
	 * @throws MarketplaceException
	 */
	public GetMemberResponse getMemberDetails(String accountNumber, ResultResponse resultResponse, Headers header) 
			throws MarketplaceException {
		
		IncludeMemberDetails includeMemberDetails = ProcessValues.getIncludeMemberDetailsForPayment();
		includeMemberDetails.setIncludeReferralBonusAccount(false);
		GetMemberResponseDto memberResponseDto = memberManagementService.getMemberDetails(accountNumber, includeMemberDetails, header, resultResponse);
		return ProcessValues.getMemberInfo(memberResponseDto, accountNumber);	
	}
	
	/**
	 * 
	 * @param partnerCode
	 * @param header
	 * @return received status from partner management service whether partner with partner 
	 * code exists or not in Partner Management
	 * @throws MarketplaceException
	 */
	public boolean checkPartnerExists(String partnerCode, Headers header) throws MarketplaceException {
		
		return partnerService.checkPartnerExists(partnerCode, header);
	}
	
	
	/**
	 *  
	 * @param decisionManagerRequestDto
	 * @param resultResponse
	 * @return the result of check in DM for customer segment of member
	 * @throws MarketplaceException
	 */
	public CustomerSegmentResult checkCustomerSegment(CustomerSegmentDMRequestDto decisionManagerRequestDto, 
			Headers header, ResultResponse resultResponse) throws MarketplaceException {
       
		return decisionManagerService.checkCustomerSegment(decisionManagerRequestDto, header, resultResponse);
	}
	
	/**
	 * 
	 * @param promotionalGiftDto
	 * @param header
	 * @param resultResponse
	 * @return the result of check in DM for promotional gift
	 * @throws MarketplaceException
	 */
	public PromotionalGiftResult getPromotionalGift(PromotionalGiftDMRequestDto req, Headers header,
			ResultResponse resultResponse) throws MarketplaceException {
		
		return decisionManagerService.getPromotionalGift(req, header, resultResponse);
	}
	
	/**
	 * 
	 * @param partnerCode
	 * @param partnerActivityDto
	 * @param resultResponse
	 * @return activity id after creation of partner activity in Member Activity
	 * @throws MarketplaceException
	 */
	public String partnerActivity(String partnerCode, PartnerActivityDto partnerActivityDto,
			ResultResponse resultResponse, Headers header) throws MarketplaceException {
		
		return memberActivityService.createPartnerActivity(partnerCode, partnerActivityDto, header, resultResponse);
		
	}
	
	/**
	 * 
	 * @param partnerCode
	 * @param partnerActivityDto
	 * @param resultResponse
	 * @return value indicating insertion of activity code in member activity service
	 * @throws MarketplaceException
	 */
	public boolean updatePartnerActivity(String partnerCode, String activityId, PartnerActivityConfigDto partnerActivityDto,
			ResultResponse resultResponse, Headers header) throws MarketplaceException {
		
		return memberActivityService.updatePartnerActivity(partnerCode, activityId, partnerActivityDto, header, resultResponse);
		
	}
	
	/**
	 * 
	 * @param header
	 * @param resultResponse
	 * @return list of all program activity from member activity service
	 * @throws MarketplaceException
	 */
	public List<ProgramActivityWithIdDto> getAllProgramActivity(Headers header, ResultResponse resultResponse) throws MarketplaceException {

		return memberActivityService.getProgramActivityList(header, resultResponse);
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param includeMemberDetails
	 * @param header
	 * @param resultResponse
	 * @return details of a member with specific account number from Member Management service
	 * @throws MarketplaceException
	 */
	public GetMemberResponseDto getMemberDetailsForPayment(String accountNumber, IncludeMemberDetails includeMemberDetails, Headers header, ResultResponse resultResponse) throws MarketplaceException {
		
		return memberManagementService.getMemberDetails(accountNumber, includeMemberDetails, header, resultResponse);
	}

	/**
	 * 
	 * @return details of all accounts with birthday today
	 * @throws MarketplaceException 
	 */
	public List<BirthdayAccountsDto> getBirthdayAccountDetails(String days, Headers header) throws MarketplaceException {
		
		return memberManagementService.getBirthdayAccountDetails(days, header);
	}
	
	
	/**
	 * Refreshes CRM Information
	 * @param accountNumber
	 * @param header
	 */
	public void refreshCrmInfo(String accountNumber, Headers header) {
		
		memberManagementService.refreshCrmInfo(accountNumber, header);
	}

	/***
	 * 
	 * @param headers
	 * @param restaurantResultResponse
	 * @return list of all restaurants
 	 * @throws MarketplaceException
	 */
	public List<RestaurantDto> fetchRestaurantList(Headers headers, RestaurantResultResponse restaurantResultResponse) throws MarketplaceException {
		
		return restaurantsService.fetchRestaurantList(headers, restaurantResultResponse);
	}
	
	
	
}
