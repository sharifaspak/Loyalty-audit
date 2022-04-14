package com.loyalty.marketplace.utils;

import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.domain.model.UploadedFileContentDomain;
import com.loyalty.marketplace.inbound.dto.BogoBulkUploadAsyncRequest;
import com.loyalty.marketplace.inbound.dto.BogoBulkUploadRequest;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileContent;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.service.MemManagementService;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftRequestDto;

@Component
public class MarketPlaceFileValidator extends MarketplaceValidator {
	private static final Logger LOG = LoggerFactory.getLogger(MarketPlaceFileValidator.class);
	
	@Autowired
	UploadedFileContentDomain uploadedFileContentDomain;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	MemManagementService memManagementService;

	public boolean validateDuplicateReferenceNumber(BogoBulkUploadAsyncRequest bogoBulkUploadAsyncRequest, UploadedFileContent savedUploadedFileContent) throws SubscriptionManagementException {
		LOG.info("Entering validateInputFileContent");
		boolean flag = true;
		 if(bogoBulkUploadAsyncRequest.getExistingExternalReferenceNumber()
				.contains(savedUploadedFileContent.getExternalReferenceNumber())
				|| bogoBulkUploadAsyncRequest.getDuplicateExternalReferenceNumber()
						.contains(savedUploadedFileContent.getExternalReferenceNumber())) {
			 uploadedFileContentDomain.saveFailedFileContent(savedUploadedFileContent, MarketPlaceCode.SUBSCRIPTION_FILE_CONTAINS_DUP_EXTERNALREFNUM);
			 flag = false;
		 }
		return flag; 
	}	
	
	public boolean validateAccountNumber(UploadedFileContent savedUploadedFileContent) throws SubscriptionManagementException {
		LOG.info("Entering validateAccountNumber");
		boolean flag = true;
		if (savedUploadedFileContent.getAccountNumber() == null
				|| savedUploadedFileContent.getAccountNumber().isEmpty()) {
			uploadedFileContentDomain.saveFailedFileContent(savedUploadedFileContent, MarketPlaceCode.SUBSCRIPTION_FILE_CONTAINS_EMPTY_ACCOUNT_NUMBER);
			flag = false;
		}
		return flag;
	}
	
	public boolean validateMembershipCode(Map<String, String> accountMemberMap, UploadedFileContent savedUploadedFileContent ) throws SubscriptionManagementException {
		LOG.info("Entering validateMembershipCode");
		boolean flag = false;
		if(accountMemberMap.containsKey(savedUploadedFileContent.getAccountNumber())) {
			if(null == savedUploadedFileContent.getMembershipCode() || savedUploadedFileContent.getMembershipCode().isEmpty()
					|| savedUploadedFileContent.getMembershipCode().equalsIgnoreCase(accountMemberMap.get(savedUploadedFileContent.getAccountNumber()))) { 
				LOG.info("MembershipCode passed in input inside if block:{}", savedUploadedFileContent.getMembershipCode());
				flag = true;
			} else {
				LOG.info("Inside validateMembershipCode, membership code does not match");
				flag = false;
				uploadedFileContentDomain.saveFailedFileContent(savedUploadedFileContent, MarketPlaceCode.SUBSCRIPTION_FILE_CONTAINS_INVALID_MEMBERSHIP_CODE);			
			} 
		} else {
			uploadedFileContentDomain.saveFailedFileContent(savedUploadedFileContent, MarketPlaceCode.MEMBER_NOT_ENROLLED);
			flag = false;
		}
		return flag;
	}

	public String validateAndFetchMembershipCode(UploadedFileContent savedUploadedFileContent, Headers headers) throws SubscriptionManagementException, MarketplaceException {
		LOG.info("Entering validateMembershipCode, account Number:{}", savedUploadedFileContent.getAccountNumber());
		String membershipCode = null;
		GetMemberResponse memberResponse = fetchServiceValues.getMemberDetails(savedUploadedFileContent.getAccountNumber(), null, headers);	
		LOG.info("Inside validateAndFetchMembershipCode memberResponse:{}", memberResponse);
		if(!ObjectUtils.isEmpty(memberResponse)) {
			if(null == savedUploadedFileContent.getMembershipCode() || savedUploadedFileContent.getMembershipCode().isEmpty()) { 
				LOG.info("MembershipCode passed in input inside if block:{}", savedUploadedFileContent.getMembershipCode());
				membershipCode = memberResponse.getMembershipCode();
			} else if(savedUploadedFileContent.getMembershipCode().equalsIgnoreCase(memberResponse.getMembershipCode())){
				LOG.info("MembershipCode fetched from MM and passed in input matches.");
				membershipCode = savedUploadedFileContent.getMembershipCode();
			} else {
				LOG.info("MembershipCode passed in input:{} and membershipCode fetched from MM are different:{}", savedUploadedFileContent.getMembershipCode(),
						memberResponse.getMembershipCode());
				uploadedFileContentDomain.saveFailedFileContent(savedUploadedFileContent, MarketPlaceCode.SUBSCRIPTION_FILE_CONTAINS_INVALID_MEMBERSHIP_CODE);			
			}
		}	
		return membershipCode;	
	}
	
	
	public PurchaseRequestDto populatePurchaseRequestDto(UploadedFileContent savedUploadedFileContent, String membershipCode,
			BogoBulkUploadRequest bogoBulkUploadRequest) {
		PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setAccountNumber(savedUploadedFileContent.getAccountNumber());
		purchaseRequestDto.setMembershipCode(membershipCode);
		purchaseRequestDto.setSelectedOption(MarketplaceConstants.FREE.getConstant());
		purchaseRequestDto.setSelectedPaymentItem("subscription");
		purchaseRequestDto.setSubscriptionMethod(MarketplaceConfigurationConstants.SUBSCRIPTION_METHOD_BOGO);
		purchaseRequestDto.setCouponQuantity(1);
		purchaseRequestDto.setSubscriptionCatalogId(bogoBulkUploadRequest.getSubscriptionCatalogId());
		purchaseRequestDto.setExtTransactionId(savedUploadedFileContent.getExternalReferenceNumber());
		purchaseRequestDto.setOfferId(savedUploadedFileContent.getSubscriptionCatalogId());
		
		return purchaseRequestDto;	
	}

	public WelcomeGiftRequestDto populateWelcomeGiftRequestDto(UploadedFileContent savedUploadedFileContent, BogoBulkUploadRequest bogoBulkUploadRequest) {
		
		WelcomeGiftRequestDto welcomeGiftRequestDto = new WelcomeGiftRequestDto();
		welcomeGiftRequestDto.setChannelId("ADMIN_PORTAL");
		welcomeGiftRequestDto.setAccountNumber(savedUploadedFileContent.getAccountNumber());
		welcomeGiftRequestDto.setMembershipCode(savedUploadedFileContent.getMembershipCode());
		welcomeGiftRequestDto.setSubscriptionCatalogId(bogoBulkUploadRequest.getSubscriptionCatalogId());
		welcomeGiftRequestDto.setBogoBulk(true);
		
		return welcomeGiftRequestDto;
	}
}
