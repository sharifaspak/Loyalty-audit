package com.loyalty.marketplace.offers.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.decision.manager.inbound.dto.CustomerSegmentDMRequestDto;
import com.loyalty.marketplace.offers.decision.manager.inbound.dto.MemberDetails;
import com.loyalty.marketplace.offers.decision.manager.inbound.dto.PromotionalGiftDMRequestDto;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.LifetimeSavingsHelperDto;
import com.loyalty.marketplace.offers.inbound.dto.ListValuesDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.inbound.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.CustomerTypeDto;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.EventTypeCatalogueDTO;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityConfigDto;
import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityDto;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityName;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityTypes;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.EventTypeCatalogueDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.EventTypeCatalogueName;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.PartnerActivityCode;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.RateType;
import com.loyalty.marketplace.offers.memberactivity.outbound.dto.ProgramActivityWithIdDto;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.service.dto.IncludeMemberDetails;
import com.loyalty.marketplace.offers.points.bank.inbound.dto.LifeTimeSavingsRequestDto;
import com.loyalty.marketplace.offers.points.bank.inbound.dto.LifeTimeSavingsVouchersEvent;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.dto.PushNotificationRequestDto;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.payment.inbound.dto.PaymentAdditionalRequest;
import com.loyalty.marketplace.voucher.inbound.dto.ListVoucherRequest;

/**
 * 
 * @author jaya.shukla
 *
 */
public class Requests {
	
	Requests(){
		
	}
	
	/**
	 * 
	 * @param type
	 * @param code
	 * @param codeDescriptionEnglish
	 * @param codeDescriptionArabic
	 * @param programActivityDto
	 * @param value
	 * @return request object for creating partner activity in MA
	 */
	public static PartnerActivityDto getPartnerActivityRequest(String type, String code, String codeDescriptionEnglish, String codeDescriptionArabic, ProgramActivityWithIdDto programActivityDto, Double value, ListValuesDto customerTypes) {
		
		PartnerActivityCode activityCode = new PartnerActivityCode();
		ActivityDescription activityCodeDescription = new ActivityDescription();
		activityCodeDescription.setEnglish(codeDescriptionEnglish);
		activityCodeDescription.setArabic(codeDescriptionArabic);
		activityCode.setCode(code);
		activityCode.setDescription(activityCodeDescription);
				
		ActivityTypes activityType = new ActivityTypes();
		ActivityDescription activityDescription = new ActivityDescription();
		ActivityName activityName = new ActivityName();
		
		RateType rateType = new RateType(); 
		
		EventTypeCatalogueDTO activityTypeCatalogue = new EventTypeCatalogueDTO();
		EventTypeCatalogueName activityTypeCatalogueName = new EventTypeCatalogueName();
		activityTypeCatalogueName.setEnglish(OfferConstants.ACTIVITY_CATALOGUE_PARTNER_NAME_EN.get());	
		activityTypeCatalogueName.setArabic(OfferConstants.ACTIVITY_CATALOGUE_PARTNER_NAME_AR.get());
		EventTypeCatalogueDescription activityTypeCatalogueDescription = new EventTypeCatalogueDescription();
		activityTypeCatalogueDescription.setEnglish(OfferConstants.ACTIVITY_CATALOGUE_PARTNER_DESCRIPTION_EN.get());	
		activityTypeCatalogueDescription.setArabic(OfferConstants.ACTIVITY_CATALOGUE_PARTNER_DESCRIPTION_AR.get());
		activityTypeCatalogue.setName(activityTypeCatalogueName );
		activityTypeCatalogue.setDescription(activityTypeCatalogueDescription);
		
		if(type.equalsIgnoreCase(OfferConstants.ACTIVITY_ACCRUAL.get())) {
			
			activityName.setEnglish(OfferConstants.ACTIVITY_TYPE_NAME_EARNING_EN.get()); 
			rateType.setName(OfferConstants.RATE_TYPE_NAME_ACCRUAL.get());
		    rateType.setDescription(OfferConstants.RATE_TYPE_DESCRIPTION_ACCRUAL.get());
						
		} else if(type.equalsIgnoreCase(OfferConstants.ACTIVITY_REDEMPTION.get())) {
			
			activityName.setEnglish(OfferConstants.ACTIVITY_TYPE_NAME_REEDEMING_EN.get()); 
			rateType.setName(OfferConstants.RATE_TYPE_NAME_REDEMPTION.get());
 			rateType.setDescription(OfferConstants.RATE_TYPE_DESCRIPTION_REDEMPTION.get());
			
		}
		
		activityType.setName(activityName);
		activityType.setDescription(activityDescription);
		
		PartnerActivityDto partnerActivityDto = new PartnerActivityDto();
		partnerActivityDto.setLoyaltyActivity(programActivityDto);
		partnerActivityDto.setActivityCode(activityCode);
		partnerActivityDto.setActivityType(activityType);
		partnerActivityDto.setRateType(rateType);
		partnerActivityDto.setTierPointsFlag(false);
		partnerActivityDto.setBaseRate(value);
		partnerActivityDto.setStartDate(new	Date());
		partnerActivityDto.setEndDate(null);
		partnerActivityDto.setActivityTypeCatalogue(activityTypeCatalogue );
		partnerActivityDto.setPointsExpiryPeriod(24L);
		partnerActivityDto.setCustomerType(getCustomerTypeListForPartnerActivity(customerTypes));
				
		return partnerActivityDto;
	}
	
	/**
	 * 
	 * @param memberDetails
	 * @param purchaseRecords
	 * @param resultResponse 
	 * @return request object for connecting to DM
	 */
	public static CustomerSegmentDMRequestDto getDecisionManagerRequestDto(List<GetMemberResponse> memberInfoList, List<PurchaseHistory> purchaseRecords){
		
		CustomerSegmentDMRequestDto decisionManagerRequestDto = null; 
		
		if(CollectionUtils.isNotEmpty(memberInfoList)) {
			
			decisionManagerRequestDto = new CustomerSegmentDMRequestDto();
			List<MemberDetails> memberDetailsList = new ArrayList<>(memberInfoList.size());
			
			for(GetMemberResponse memberInfo : memberInfoList) {
				
				MemberDetails memberDetails = new MemberDetails();
				memberDetails.setAccountNumber(memberInfo.getAccountNumber());
				memberDetails.setMembershipCode(memberInfo.getMembershipCode());
				memberDetails.setCustomerType(memberInfo.getCustomerType());
				memberDetails.setTierLevelName(memberInfo.getTierLevelName());
				memberDetails.setSubscribed(memberInfo.isSubscribed());
				memberDetails.setHasCobranded(!CollectionUtils.isEmpty(memberInfo.getCobrandedCardDetails()));
				memberDetails.setCoBrand(ProcessValues.getCoBrandedCards(memberInfo.getCobrandedCardDetails()));
				memberDetails.setActiveCoBranded(Checks.checkActiveCobranded(memberInfo.getCobrandedCardDetails()));
				memberDetails.setBillPaymentAmount(ProcessValues.getBillPaymentRechargeAmount(purchaseRecords, OffersConfigurationConstants.billPaymentItem));
				memberDetails.setRechargeAmount(ProcessValues.getBillPaymentRechargeAmount(purchaseRecords, OffersConfigurationConstants.rechargeItem));
			    memberDetailsList.add(memberDetails);
			}
			decisionManagerRequestDto.setMemberDetailsList(memberDetailsList);
			
		}
		
		return decisionManagerRequestDto;
	}
	
//	/**
//	 * 
//	 * @param headers
//	 * @param accountNumber
//	 * @param membershipCode
//	 * @param estSavings
//	 * @param offerTypeId
//	 * @param quantity
//	 * @param amount
//	 * @return request object for sending lifetime savings to points bank
//	 */
//    public static LifeTimeSavingsVouchersEvent createLifetimeSavingsVoucherEvent(Headers headers, String accountNumber, String membershipCode, Double estSavings, String purchaseItem, Integer quantity, Double amount) {
//		
//		LifeTimeSavingsVouchersEvent lifeTimeSavingsVouchersEvent = new LifeTimeSavingsVouchersEvent();
//		
//		lifeTimeSavingsVouchersEvent.setAccountNumber(accountNumber);
//		lifeTimeSavingsVouchersEvent.setMembershipCode(membershipCode);
//		lifeTimeSavingsVouchersEvent.setHasEstimatedSavings(!ObjectUtils.isEmpty(estSavings));
//		
//		if(lifeTimeSavingsVouchersEvent.isHasEstimatedSavings()) {
//			
//			lifeTimeSavingsVouchersEvent.setEstimatedSavings(estSavings);
//		}
//		
//		lifeTimeSavingsVouchersEvent.setQuantity(quantity);
//		lifeTimeSavingsVouchersEvent.setSpendValue(amount);
//		lifeTimeSavingsVouchersEvent.setType(ProcessValues.getLifetimeSavingsType(purchaseItem));
//		
//		if(!ObjectUtils.isEmpty(headers.getUserName())) {
//			lifeTimeSavingsVouchersEvent.setUserName(headers.getUserName());
//		}
//		
//		if(!ObjectUtils.isEmpty(headers.getExternalTransactionId())) {
//			lifeTimeSavingsVouchersEvent.setExternalTransactionId(headers.getExternalTransactionId());
//		}
//		
//		
//		
//		return lifeTimeSavingsVouchersEvent;
//		
//	}
    
    /**
	 * 
	 * @param headers
	 * @param accountNumber
	 * @param membershipCode
	 * @param estSavings
	 * @param offerTypeId
	 * @param quantity
	 * @param amount
	 * @return request object for sending lifetime savings to points bank
	 */
    public static LifeTimeSavingsVouchersEvent createLifetimeSavingsVoucherEvent(Headers headers, PurchaseRequestDto purchaseRequest, LifetimeSavingsHelperDto lifetimeSavingsHelperDto) {
		
		LifeTimeSavingsVouchersEvent lifeTimeSavingsVouchersEvent = new LifeTimeSavingsVouchersEvent();
		
		if(!ObjectUtils.isEmpty(purchaseRequest)) {
			lifeTimeSavingsVouchersEvent.setAccountNumber(purchaseRequest.getAccountNumber());
			lifeTimeSavingsVouchersEvent.setMembershipCode(purchaseRequest.getMembershipCode());
			lifeTimeSavingsVouchersEvent.setQuantity(purchaseRequest.getCouponQuantity());
			lifeTimeSavingsVouchersEvent.setType(ProcessValues.getLifetimeSavingsType(purchaseRequest.getSelectedPaymentItem()));
		}
		
		if(!ObjectUtils.isEmpty(headers)) {
			
			if(!ObjectUtils.isEmpty(headers.getUserName())) {
				lifeTimeSavingsVouchersEvent.setUserName(headers.getUserName());
			}
			
			if(!ObjectUtils.isEmpty(headers.getExternalTransactionId())) {
				lifeTimeSavingsVouchersEvent.setExternalTransactionId(headers.getExternalTransactionId());
			}
		}
		
		if(!ObjectUtils.isEmpty(lifetimeSavingsHelperDto)) {
			lifeTimeSavingsVouchersEvent.setSubscriptionStatus(lifetimeSavingsHelperDto.isSubscriptionStatus());
			lifeTimeSavingsVouchersEvent.setMerchantName(lifetimeSavingsHelperDto.getMerchantName());
			
			if(lifeTimeSavingsVouchersEvent.getType().equals(OfferConstants.DISCOUNT_VOUCHER_LIFETIME_SAVINGS.get())
		    && lifeTimeSavingsVouchersEvent.isSubscriptionStatus()) {
				lifeTimeSavingsVouchersEvent.setSubscriptionWaiveOff(lifetimeSavingsHelperDto.getSubscriptionWaiveOff());
			} 
			lifeTimeSavingsVouchersEvent.setSpendValue(lifetimeSavingsHelperDto.getSavings());
			lifeTimeSavingsVouchersEvent.setPointsTransactionId(lifetimeSavingsHelperDto.getPointsTransactionId());
		}

		return lifeTimeSavingsVouchersEvent;
		
	}
    
    /**
     * 
     * @param businessIds
     * @return request object for getting voucher details 
     */
    public static ListVoucherRequest getVoucherRequestDto(List<String> businessIds) {
    	
    	ListVoucherRequest voucherRequestDto = new ListVoucherRequest();
    	voucherRequestDto.setBusinessIds(businessIds);
    	return voucherRequestDto;
    	
    }

    /**
     * 
     * @param accountNumber
     * @param includeMemberDetails
     * @return request object for fetching member details from MM
     */
	public static MemberDetailRequestDto getMemberRequestDto(String accountNumber,
			IncludeMemberDetails includeMemberDetails) {
		
		MemberDetailRequestDto memberRequestDto = new MemberDetailRequestDto();
		memberRequestDto.setAccountNumber(accountNumber);
		memberRequestDto.setIncludeEligibilityMatrix(includeMemberDetails.isIncludeEligibilityMatrix());
		memberRequestDto.setIncludeEligiblePaymentMethods(includeMemberDetails.isIncludePaymentMethods());
		memberRequestDto.setCallMemberActivity(includeMemberDetails.isIncludeMemberActivityInfo());
		memberRequestDto.setCallSubscription(includeMemberDetails.isIncludeSubscriptionInfo());
		memberRequestDto.setCallPointsBank(includeMemberDetails.isIncludeSubscriptionInfo());
		memberRequestDto.setCallCustomerInterest(includeMemberDetails.isIncludeCustomerInterestInfo());
		memberRequestDto.setIncludeLinkedAccount(includeMemberDetails.isIncludeLinkedAccount());
		memberRequestDto.setIncludeReferralBonusAccount(includeMemberDetails.isIncludeReferralBonusAccount());
		return memberRequestDto;
	}

	/**
	 * 
	 * @param headers
	 * @param activityCode
	 * @param uuid
	 * @param isPaymentRequired
	 * @return additional request object for payment service
	 */
	public static PaymentAdditionalRequest getPaymentAdditionalRequest(Headers headers, String activityCode,
			String uuid, boolean isPaymentRequired) {
		
		PaymentAdditionalRequest paymentAdditionalRequest = new PaymentAdditionalRequest();
		paymentAdditionalRequest.setActivityCode(activityCode);
		paymentAdditionalRequest.setUuid(uuid);
		paymentAdditionalRequest.setChannelId(headers.getChannelId());
		paymentAdditionalRequest.setHeader(headers);
		paymentAdditionalRequest.setPaymentRequired(isPaymentRequired);
		return paymentAdditionalRequest;
	}

	/**
	 * 
	 * @param accountNumberList
	 * @param name
	 * @return request object for push notification for non subscribed user on purchasing discount voucher
	 */
	public static PushNotificationRequestDto getPushNotificationRequestForBogoSubscription(String accountNumber, String membershipCode, String firstName, String transactionId, String uiLanguage) {
		
		PushNotificationRequestDto requestDto = new PushNotificationRequestDto();
		requestDto.setNotificationId(OffersConfigurationConstants.BOGO_SUBSCRIPTION_NOTIFICATION_ID);
		requestDto.setNotificationCode(OffersConfigurationConstants.BOGO_SUBSCRIPTION_NOTIFICATION_CODE);
		requestDto.setAccountNumber(accountNumber);
		requestDto.setMembershipCode(membershipCode);
		requestDto.setTransactionId(transactionId);
		requestDto.setLanguage(ProcessValues.getNotificationLanguage(uiLanguage));
		
		Map<String, String> additionalParam = new HashMap<>(1);
		additionalParam.put(OfferConstants.FIRST_NAME_PUSH_NOTIFICATION.get(), firstName);
		requestDto.setAdditionalParameters(additionalParam);
		
		return requestDto;
	}

	/**
	 * 
	 * @param birthdayAccountsDto
	 * @return request object for SMS for birthday alert
	 */
	public static SMSRequestDto createSMSRequestForBirthdayAlert(BirthdayAccountsDto birthdayAccountsDto, Headers headers) {
		
		SMSRequestDto smsRequestDto = new SMSRequestDto();

		smsRequestDto.setLanguage(ProcessValues.getNotificationLanguage(birthdayAccountsDto.getUiLanguage()));
		smsRequestDto.setTransactionId(headers.getExternalTransactionId());
		smsRequestDto.setTemplateId(OffersConfigurationConstants.BIRTHDAY_SMS_TEMPLATE_ID);
		smsRequestDto.setNotificationId(OffersConfigurationConstants.BIRTHDAY_SMS_NOTIFICATION_ID);
		smsRequestDto.setNotificationCode(OffersConfigurationConstants.BIRTHDAY_SMS_NOTIFICATION_CODE);
		smsRequestDto.setMembershipCode(birthdayAccountsDto.getMembershipCode());

		Map<String, String> additionalParam = new HashMap<>();
		additionalParam.put(OfferConstants.FIRST_NAME_SMS.get(), birthdayAccountsDto.getFirstName());
		smsRequestDto.setAdditionalParameters(additionalParam);

		List<String> contacts = new ArrayList<>();
		contacts.add(birthdayAccountsDto.getAccountNumber());
		smsRequestDto.setDestinationNumber(contacts);
		
		return smsRequestDto;
	}

	/**
	 * 
	 * @param birthdayAccountsDto
	 * @return request object for email for birthday alert
	 */
	public static EmailRequestDto createEmailRequestForBirthdayAlert(BirthdayAccountsDto birthdayAccountsDto) {
		
		EmailRequestDto emailDto = new EmailRequestDto();

		emailDto.setLanguage(birthdayAccountsDto.getUiLanguage());
		emailDto.setTransactionId("");
		emailDto.setTemplateId("");
		emailDto.setNotificationId("");
		emailDto.setNotificationCode("");
		emailDto.setAccountNumber(birthdayAccountsDto.getAccountNumber());
		emailDto.setEmailId(birthdayAccountsDto.getEmail());

		Map<String, String> additionalParam = new HashMap<>();
		emailDto.setAdditionalParameters(additionalParam);
		
		return emailDto;
	}

	/**
	 * 
	 * @param birthdayAccountsDto
	 * @return request object for push notification for birthday alert
	 */
	public static PushNotificationRequestDto createPushNotificationRequestForBirthdayAlert(
			BirthdayAccountsDto birthdayAccountsDto, String transactionId) {
		
		PushNotificationRequestDto requestDto = new PushNotificationRequestDto();
		requestDto.setNotificationId(OffersConfigurationConstants.NOTIFICATION_TEMPLATE_ID);
		requestDto.setNotificationCode(OffersConfigurationConstants.PUSH_NOTIFICATION_CODE);
		requestDto.setAccountNumber(birthdayAccountsDto.getAccountNumber());
		requestDto.setMembershipCode(birthdayAccountsDto.getMembershipCode());
		requestDto.setTransactionId(transactionId);
		requestDto.setLanguage(ProcessValues.getNotificationLanguage(birthdayAccountsDto.getUiLanguage()));
		
		Map<String, String> additionalParam = new HashMap<>(1);
		additionalParam.put(OfferConstants.FIRST_NAME_PUSH_NOTIFICATION.get(), birthdayAccountsDto.getFirstName());
		requestDto.setAdditionalParameters(additionalParam);
		
		return requestDto;
		
	}
	
	/**
     * 
     * @param earnMultiplier
	 * @param listValuesDto 
     * @return request object for updating activity code
     */
    public static PartnerActivityConfigDto getUpdatePartnerActivityRequest(Double earnMultiplier, ListValuesDto customerTypes) {
		
		PartnerActivityConfigDto partnerActivityDto = new PartnerActivityConfigDto();
		partnerActivityDto.setBaseRate(earnMultiplier);
		partnerActivityDto.setCustomerType(getCustomerTypeListForPartnerActivity(customerTypes));
		return partnerActivityDto;
	}

    /**
     * 
     * @param customerTypes
     * @return list of customers to be set for partnerActivity
     */
    private static List<CustomerTypeDto> getCustomerTypeListForPartnerActivity(ListValuesDto customerTypes) {
		
    	boolean customerTypePresent = !ObjectUtils.isEmpty(customerTypes)
				&& !CollectionUtils.isEmpty(customerTypes.getEligibleTypes());
		List<CustomerTypeDto> customerTypeList = customerTypePresent
				? new ArrayList<>(customerTypes.getEligibleTypes().size())
				: null;
		
		if(customerTypePresent) {
			customerTypes.getEligibleTypes().forEach(c->customerTypeList.add(new CustomerTypeDto(c)));
		}
		
		return customerTypeList;
	}

	/**
     * 
     * @param channelId
     * @param promotionalId
     * @return request object for promotional gift for DM
     */
	public static PromotionalGiftDMRequestDto getPromotionalGiftRequest(String channelId, String promotionalGiftId) {
		
		PromotionalGiftDMRequestDto promotionalGiftDMRequestDto = new PromotionalGiftDMRequestDto();
		promotionalGiftDMRequestDto.setChannelId(channelId);
		promotionalGiftDMRequestDto.setPromotionalGiftId(promotionalGiftId);
		return promotionalGiftDMRequestDto;
	}

	/**
	 * 
	 * @param accountNumber
	 * @return lifetimes avings retrieval request
	 */
	public static LifeTimeSavingsRequestDto getlifetimeSavingsRequest(String accountNumber) {
		LifeTimeSavingsRequestDto lifeTimeSavingsRequestDto = new LifeTimeSavingsRequestDto();
		lifeTimeSavingsRequestDto.setAccountNumber(accountNumber);
		return lifeTimeSavingsRequestDto;
	}
    
	

}
