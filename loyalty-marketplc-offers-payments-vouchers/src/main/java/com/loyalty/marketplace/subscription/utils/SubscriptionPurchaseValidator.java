package com.loyalty.marketplace.subscription.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.gifting.outbound.database.entity.Gifts;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftRepository;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.helper.dto.PurchaseExtraAttributes;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResponseDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.domain.CCDetailsDomain;
import com.loyalty.marketplace.subscription.domain.PaymentMethodDetailsDomain;
import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
import com.loyalty.marketplace.subscription.domain.SubscriptionPaymentDomain;
import com.loyalty.marketplace.subscription.helper.dto.RenewalValues;
import com.loyalty.marketplace.subscription.inbound.dto.CachedValues;
import com.loyalty.marketplace.subscription.inbound.dto.NotificationValues;
import com.loyalty.marketplace.subscription.inbound.dto.ValidDates;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPayment;
import com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto.PhoneyTunesRequest;
import com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto.PhoneyTunesResponse;
import com.loyalty.marketplace.subscription.service.SubscriptionService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MarketplaceValidator;

import lombok.AccessLevel;
import lombok.Getter;

@Component
public class SubscriptionPurchaseValidator extends MarketplaceValidator {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPurchaseValidator.class);
	
	@Value("${non.existing.member}")
    private String nonExisting;
	
	@Autowired
	SubscriptionDomain subscriptionDomain;
	
	@Autowired
	SubscriptionPaymentDomain subscriptionPaymentDomain;
	
	@Autowired
	PurchaseDomain purchaseDomain;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	SubscriptionCatalogValidator subscriptionCatalogValidator;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	SubscriptionUtils subscriptionUtils;
	
	@Autowired
	PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
    PaymentService paymentService;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	GiftRepository giftRepository;
	
	public Subscription validatePurchaseSubscription(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, GetMemberResponseDto getMemberResponseDto, 
			ValidDates validDates, CachedValues cachedValues, Headers headers, PurchaseResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException {
		LOG.info("Enter validatePurchaseSubscription");
		Subscription savedSubscribtion = new Subscription();
		NotificationValues notificationValues = new NotificationValues(false, false, purchaseRequestDto.getAccountNumber(),purchaseRequestDto.getPromoCode(),purchaseRequestDto.getSelectedOption(),purchaseRequestDto.getCardNumber(),purchaseRequestDto.getSpentAmount(),null,null,null, null);
		if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
			if (purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
		    		|| purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())) {
				int duration = subscriptionCatalog.getValidityPeriod()+subscriptionCatalog.getFreeDuration();
				LOG.info("purchaseSubscription :: subscriptionCatalog.getFreeDuration() : {} :: subscriptionCatalog.getValidityPeriod() : {} :: duration : {}", subscriptionCatalog.getFreeDuration(), subscriptionCatalog.getValidityPeriod(), duration);				
				savedSubscribtion = purchaseSubscriptionWithPhoneyTunes(duration, SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get(), subscriptionCatalog.getCost().toString(), notificationValues, purchaseRequestDto, subscriptionCatalog,
						validDates, headers, resultResponse);
		    } else {
		    	LOG.info("purchaseSubscription :: Saving One-Time Subscription of MembershipCode : {}", purchaseRequestDto.getMembershipCode());
		    	savedSubscribtion = purchaseSubscriptionWithPayment(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, cachedValues, validDates, headers, resultResponse);
		    }
			
		} else if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			LOG.info("purchaseSubscription :: getPhoneyTunesResponse");
			int duration = subscriptionCatalog.getFreeDuration() + purchaseRequestDto.getFreeSubscriptionDays();
			LOG.info("purchaseSubscription :: subscriptionCatalog.getFreeDuration() : {} :: purchaseRequestDto.getFreeSubscriptionDays() : {} :: duration : {}", subscriptionCatalog.getFreeDuration(), purchaseRequestDto.getFreeSubscriptionDays(), duration);			
			notificationValues.setNotifyAutoRenewalSubscription(true);
			notificationValues.setActionType(SubscriptionManagementConstants.ACTION_TYPE_ACTIVATED.get());
			notificationValues.setChargebilityType(SubscriptionManagementConstants.NEW_SUBSCRIPTION_EVENT);
			if(!ObjectUtils.isEmpty(getMemberResponseDto) && !CollectionUtils.isEmpty(getMemberResponseDto.getAccountsInfo())) {
				notificationValues.setUiLanguage(getMemberResponseDto.getAccountsInfo().get(0).getUilanguage());
			}
			if (purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
		    		|| purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())) {
				savedSubscribtion = purchaseSubscriptionWithPhoneyTunes(duration, SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get(), subscriptionCatalog.getCost().toString(), notificationValues, purchaseRequestDto, subscriptionCatalog,
						validDates, headers, resultResponse);

			} else if (purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())){
				LOG.info("purchaseSubscription :: Saving Auto-Renewal Subscription using credit card of MembershipCode : {}", purchaseRequestDto.getMembershipCode());
		    	savedSubscribtion = purchaseSubscriptionWithCard(duration, purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, cachedValues, validDates, notificationValues, headers, resultResponse);
		    	if(null!=savedSubscribtion && duration == 0) {
					paymentService.callMiscPaymentPostingForAutoRenewalSubscriptions(purchaseRequestDto, MarketplaceConstants.NEW_AUTORENEWAL_SUBSCRIPTION.getConstant(), headers);
				}
//			} else if (purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_METHOD_FREE.get())) {
//				LOG.info("purchaseSubscription :: Saving Auto-Renewal Subscription with free payment method : {}", purchaseRequestDto.getMembershipCode());
//				cachedValues.setPaymentRequired(false);
//		    	savedSubscribtion = purchaseSubscriptionWithPayment(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, cachedValues, validDates, headers, resultResponse);
//				
			} else {
				resultResponse.setResult(SubscriptionManagementCode.INELIGIBLE_PAYMENT_METHOD.getId(),
						SubscriptionManagementCode.INELIGIBLE_PAYMENT_METHOD.getMsg());
			}
		} else if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
			LOG.info("purchaseSubscription :: Saving Life-Time Subscription of MembershipCode : {}", purchaseRequestDto.getMembershipCode());
			savedSubscribtion = purchaseSubscriptionWithPayment(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, cachedValues, validDates, headers, resultResponse);
		} else {
			resultResponse.setResult(SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getId(),
					SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getMsg());
		}
		LOG.info("Exit validatePurchaseSubscription");
		return savedSubscribtion;
	}
	
	private Subscription purchaseSubscriptionWithPhoneyTunes(int duration, String chargeabilityType, String cost, NotificationValues notificationValues, PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog,
			ValidDates validDates, Headers headers, PurchaseResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException {
		LOG.info("purchaseSubscriptionWithPhoneyTunes");
		PurchaseExtraAttributes purchaseExtraAttributes = new PurchaseExtraAttributes(subscriptionCatalog.getSubscriptionSegment());
		PurchaseDomain purchaseDomainToSave = DomainConfiguration.getPurchaseDomain(null, purchaseRequestDto, null, null, headers, purchaseExtraAttributes);		    
		PurchaseHistory savedHistory = purchaseDomain.saveUpdatePurchaseDetails(purchaseDomainToSave, null, OfferConstants.INSERT_ACTION.get(),headers);
		LOG.info("purchaseSubscriptionWithPhoneyTunes :: savedHistory : {}", savedHistory);
		PhoneyTunesRequest phoneyTunesRequest = new PhoneyTunesRequest(purchaseRequestDto.getAccountNumber(), duration, 
				purchaseRequestDto.getSelectedOption(), chargeabilityType, subscriptionCatalogValidator.fetchPhoneyTunesPackageId(subscriptionCatalog), cost, headers.getExternalTransactionId());
		PhoneyTunesResponse phoneyTunesResponse = subscriptionService.getPhoneyTunesResponse(phoneyTunesRequest, resultResponse, headers.getExternalTransactionId());
		if(null != phoneyTunesResponse) {
			if(phoneyTunesResponse.getAckMessage().getStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())
					&& phoneyTunesResponse.getResponse().toUpperCase().contains(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
				LOG.info("purchaseSubscriptionWithPhoneyTunes :: Saving {} Subscription of MembershipCode : {}", chargeabilityType, purchaseRequestDto.getMembershipCode());
										
				String transactionId = phoneyTunesResponse.getTransactionID();			
				Subscription savedSubscription = saveSubscriptionAndPurchaseHistory(SubscriptionManagementConstants.PAYMENT_SERVICE_PHONEY_TUNES.get(), transactionId, validDates, 
					headers, purchaseRequestDto, savedHistory, subscriptionCatalog, null, phoneyTunesResponse, resultResponse, duration);
				notificationValues.setNextRenewalDate(null != savedSubscription.getNextRenewalDate() ? savedSubscription.getNextRenewalDate().toString() : null);
				subscriptionUtils.subscriptionNotification(headers, subscriptionCatalog, notificationValues,resultResponse);
				
				return savedSubscription;
			} else {		 			 			
	 			saveSubscriptionFailureInPurchaseHistory(SubscriptionManagementConstants.PAYMENT_SERVICE_PHONEY_TUNES.get(), headers, purchaseRequestDto, savedHistory, null, phoneyTunesResponse, resultResponse, subscriptionCatalog);
	 		}
		} else {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			
		}
		return null;
	}
	
	
	private Subscription purchaseSubscriptionWithPayment(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, GetMemberResponseDto getMemberResponseDto,
			CachedValues cachedValues, ValidDates validDates, Headers headers, PurchaseResultResponse resultResponse) {	
		LOG.info("purchaseSubscriptionWithPayment");
		PaymentResponse paymentResponse = new PaymentResponse();
		try {
			PurchaseExtraAttributes purchaseExtraAttributes = new PurchaseExtraAttributes(subscriptionCatalog.getSubscriptionSegment());
			PurchaseDomain purchaseDomainToSave = DomainConfiguration.getPurchaseDomain(null, purchaseRequestDto, null, null, headers, purchaseExtraAttributes);		    
			PurchaseHistory savedHistory = purchaseDomain.saveUpdatePurchaseDetails(purchaseDomainToSave, null, OfferConstants.INSERT_ACTION.get(),headers);
			
			LOG.info("purchaseSubscriptionWithPayment :: savedHistory : {}",savedHistory);				    		    		
			if(!cachedValues.isPaymentRequired()) {
				if (null != purchaseRequestDto.getMembershipCode() && purchaseRequestDto.getMembershipCode().equalsIgnoreCase(nonExisting)) {
	    			cachedValues.setNonExistingMemberFree(true);
	    		}
				paymentResponse.setPaymentStatus(MarketplaceConstants.SUCCESS.getConstant());
			} else {
				paymentResponse = subscriptionService.getPaymentResponse(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, headers, cachedValues.isPaymentRequired(), 
	    				!ObjectUtils.isEmpty(savedHistory)? savedHistory.getId() : null, resultResponse);
			}
						
			if(null != paymentResponse && paymentResponse.getPaymentStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
				String transactionId = paymentResponse.getExtRefNo() != null ? paymentResponse.getExtRefNo() : paymentResponse.getEpgTransactionId();
				
				return saveSubscriptionAndPurchaseHistory(SubscriptionManagementConstants.PAYMENT_SERVICE_LOYALTY.get(), transactionId, validDates, 
						headers, purchaseRequestDto, savedHistory, subscriptionCatalog, paymentResponse, null, resultResponse, 0);
			} else {		 			
	 			saveSubscriptionFailureInPurchaseHistory(SubscriptionManagementConstants.PAYMENT_SERVICE_LOYALTY.get(), headers, purchaseRequestDto, savedHistory, paymentResponse, null, resultResponse, subscriptionCatalog);
	 		}
		} catch (SubscriptionManagementException sme) {

			exceptionLogService.saveExceptionsToExceptionLogs(sme, headers.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), headers.getUserName());
			resultResponse.addErrorAPIResponse(sme.getErrorCodeInt(), sme.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			LOG.error(sme.printMessage());
		} catch (MarketplaceException me) {
			exceptionLogService.saveExceptionsToExceptionLogs(me, headers.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), headers.getUserName());
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			LOG.error(me.printMessage());
		}
		return null;
	}
	
	private Subscription purchaseSubscriptionWithCard(int duration, PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, GetMemberResponseDto getMemberResponseDto,
			CachedValues cachedValues, ValidDates validDates, NotificationValues notificationValues, Headers headers, PurchaseResultResponse resultResponse) {	
		LOG.info("purchaseSubscriptionWithCard");
		PaymentResponse paymentResponse = new PaymentResponse();
		try {
			PurchaseExtraAttributes purchaseExtraAttributes = new PurchaseExtraAttributes(subscriptionCatalog.getSubscriptionSegment());
			PurchaseDomain purchaseDomainToSave = DomainConfiguration.getPurchaseDomain(null, purchaseRequestDto, null, null, headers, purchaseExtraAttributes);		    
			PurchaseHistory savedHistory = purchaseDomain.saveUpdatePurchaseDetails(purchaseDomainToSave, null, OfferConstants.INSERT_ACTION.get(),headers);
			
			LOG.info("purchaseSubscriptionWithCard :: savedHistory : {}",savedHistory);				    		    		
			if(!cachedValues.isPaymentRequired()) {
				if (null != purchaseRequestDto.getMembershipCode() && purchaseRequestDto.getMembershipCode().equalsIgnoreCase(nonExisting)) {
	    			cachedValues.setNonExistingMemberFree(true);
	    		}
				paymentResponse.setPaymentStatus(MarketplaceConstants.SUCCESS.getConstant());
			}
						
			if(null != paymentResponse && paymentResponse.getPaymentStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
				String transactionId = paymentResponse.getExtRefNo() != null ? paymentResponse.getExtRefNo() : paymentResponse.getEpgTransactionId();
				
				Subscription savedSubscription = saveSubscriptionAndPurchaseHistory(SubscriptionManagementConstants.PAYMENT_SERVICE_LOYALTY.get(), transactionId, validDates, 
						headers, purchaseRequestDto, savedHistory, subscriptionCatalog, paymentResponse, null, resultResponse, duration);
				//fix to prevent failure in case of subscription using promocode
				notificationValues.setNextRenewalDate(null != savedSubscription.getNextRenewalDate() ? savedSubscription.getNextRenewalDate().toString() : null);
				subscriptionUtils.subscriptionNotification(headers, subscriptionCatalog, notificationValues,resultResponse);
				
				return savedSubscription;
			} else {		 			
	 			saveSubscriptionFailureInPurchaseHistory(SubscriptionManagementConstants.PAYMENT_SERVICE_LOYALTY.get(), headers, purchaseRequestDto, savedHistory, paymentResponse, null, resultResponse, subscriptionCatalog);
	 		}
		} catch (SubscriptionManagementException sme) {
			exceptionLogService.saveExceptionsToExceptionLogs(sme, headers.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), headers.getUserName());
			resultResponse.addErrorAPIResponse(sme.getErrorCodeInt(), sme.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			LOG.error(sme.printMessage());
		} catch (MarketplaceException me) {
			exceptionLogService.saveExceptionsToExceptionLogs(me, headers.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), headers.getUserName());
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			LOG.error(me.printMessage());
		}
		return null;
	}
	
	private Subscription saveSubscriptionAndPurchaseHistory(String paymentService, String transactionId, ValidDates validDates, Headers headers, PurchaseRequestDto purchaseRequestDto, PurchaseHistory savedHistory,
			SubscriptionCatalog subscriptionCatalog, PaymentResponse paymentResponse, PhoneyTunesResponse phoneyTunesResponse, PurchaseResultResponse resultResponse, int duration) throws SubscriptionManagementException, MarketplaceException {		
		LOG.info("Enter saveSubscriptionAndPurchaseHistory");
		PurchaseResponseDto purchaseResponseDto = new PurchaseResponseDto();
		RenewalValues renewalValues  = subscriptionUtils.populateRenewalValues(subscriptionCatalog, duration);
		
		Subscription savedSubscription = subscriptionDomain.saveSubscription(new SubscriptionDomain.SubscriptionBuilder(headers.getProgram(), subscriptionCatalog.getId(),
				purchaseRequestDto.getAccountNumber(), purchaseRequestDto.getMembershipCode(), purchaseRequestDto.getPromoCode(), subscriptionCatalog.getCost(), 
				subscriptionCatalog.getPointsValue(), subscriptionCatalog.getFreeDuration(), subscriptionCatalog.getValidityPeriod(), validDates.getValidStartDate(), validDates.getValidEndDate(), 
				SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(), purchaseRequestDto.getSelectedOption(), purchaseRequestDto.getSubscriptionMethod(), subscriptionCatalog.getSubscriptionSegment(),
				headers.getChannelId(),null, null, subscriptionCatalogValidator.fetchPhoneyTunesPackageId(subscriptionCatalog), transactionId, renewalValues.getLastChargedAmount(), renewalValues.getLastChargedDate(), 
				renewalValues.getNextRenewalDate(), new Date(), headers.getUserName(), new Date(), headers.getUserName()).build());			
		
		
		List<PaymentMethodDetailsDomain> paymentMethodDetailsList = saveSubscriptionPaymentMethodDetails(headers, purchaseRequestDto, savedSubscription);
		LOG.info("paymentMethodDetailsList {}", paymentMethodDetailsList);
			
		purchaseRequestDto.setSubscriptionCatalogId(savedSubscription.getId());
		purchaseResponseDto.setSubscriptionId(savedSubscription.getId());
		purchaseResponseDto.setSubscriptionEndDate(savedSubscription.getEndDate());
		if(checkFreeDurationEligibilty(duration)) {
			purchaseRequestDto.setSpentAmount(0.0);
		}
		
		LOG.info("saveSubscriptionAndPurchaseHistory :: savedHistory : {}", savedHistory);
		
		PurchaseExtraAttributes purchaseExtraAttributes = new PurchaseExtraAttributes(subscriptionCatalog.getSubscriptionSegment());
		PurchaseDomain purchaseDomainToSave = 
				paymentService.equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_SERVICE_PHONEY_TUNES.get()) 
						? DomainConfiguration.getPurchaseDomainForPhoneyTunes(null, purchaseRequestDto, phoneyTunesResponse, savedHistory, headers, purchaseExtraAttributes)
						: DomainConfiguration.getPurchaseDomain(null, purchaseRequestDto, paymentResponse, savedHistory, headers, purchaseExtraAttributes);
		LOG.info("saveSubscriptionAndPurchaseHistory :: purchaseDomainToSave : {}", purchaseDomainToSave);
		PurchaseHistory purchaseHistory = purchaseDomain.saveUpdatePurchaseDetails(purchaseDomainToSave, savedHistory, OfferConstants.UPDATE_ACTION.get(),headers);	    
		purchaseResponseDto.setTransactionNo(purchaseHistory.getId());
	    purchaseResponseDto.setPaymentStatus(paymentService.equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_SERVICE_PHONEY_TUNES.get()) 
				? phoneyTunesResponse.getAckMessage().getStatus()
				: paymentResponse.getPaymentStatus());
	    	    
	    resultResponse.setPurchaseResponseDto(purchaseResponseDto);
	    resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATED.getId(), SubscriptionManagementCode.SUBSCRIPTION_CREATED.getMsg());
		resultResponse.setSuccessAPIResponse();
		LOG.info("Exit saveSubscriptionAndPurchaseHistory");
		return savedSubscription;
	}
	
	private List<PaymentMethodDetailsDomain> saveSubscriptionPaymentMethodDetails(Headers headers,
			PurchaseRequestDto purchaseRequestDto, Subscription savedSubscription) throws SubscriptionManagementException {
		List<PaymentMethodDetailsDomain> paymentMethodDetailsList = new ArrayList<>();
		
		CCDetailsDomain ccDetails = new CCDetailsDomain();
			ccDetails.setCardNumber(purchaseRequestDto.getCardNumber());
			ccDetails.setSubtype(purchaseRequestDto.getCardSubType());
		
		PaymentMethodDetailsDomain paymentMetDet = new PaymentMethodDetailsDomain();
			paymentMetDet.setPaymentMethodId(paymentMethodRepository.findByDescription(purchaseRequestDto.getSelectedOption()).getPaymentMethodId());
			paymentMetDet.setPaymentMethod(purchaseRequestDto.getSelectedOption());
			paymentMetDet.setMasterEPGTransactionId(purchaseRequestDto.getMasterEPGTransactionId());
			paymentMetDet.setEpgTransactionID(purchaseRequestDto.getEpgTransactionId());
			paymentMetDet.setCcDetailsdomain(ccDetails);
			paymentMetDet.setStatus(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
			//paymentMetDet.setInactiveDate(new Date());
		
			paymentMethodDetailsList.add(paymentMetDet);
			
		SubscriptionPayment savedSubscriptionPayment = subscriptionPaymentDomain.saveSubscriptionPayment(new SubscriptionPaymentDomain.SubscriptionPaymentBuilder(null, savedSubscription.getId(), paymentMethodDetailsList , new Date(), headers.getUserName(), new Date(), headers.getUserName()).build());
		LOG.info("SavedSubscriptionPayment:{}", savedSubscriptionPayment);
		return paymentMethodDetailsList;
	}

	private void saveSubscriptionFailureInPurchaseHistory(String paymentService, Headers headers, PurchaseRequestDto purchaseRequestDto, PurchaseHistory savedHistory, 
			PaymentResponse paymentResponse, PhoneyTunesResponse phoneyTunesResponse, PurchaseResultResponse resultResponse, SubscriptionCatalog subscriptionCatalog) throws MarketplaceException {
		LOG.info("Enter saveSubscriptionFailureInPurchaseHistory ::savedHistory : {}", savedHistory);
		PurchaseResponseDto purchaseResponseDto = new PurchaseResponseDto();
		
		PurchaseExtraAttributes purchaseExtraAttributes = new PurchaseExtraAttributes(subscriptionCatalog.getSubscriptionSegment());
		PurchaseDomain purchaseDomainToSave = 
				paymentService.equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_SERVICE_PHONEY_TUNES.get())
					? DomainConfiguration.getPurchaseDomainForPhoneyTunes(null, purchaseRequestDto, phoneyTunesResponse, savedHistory, headers, purchaseExtraAttributes)
					: DomainConfiguration.getPurchaseDomain(null, purchaseRequestDto, paymentResponse, savedHistory, headers, purchaseExtraAttributes);
		LOG.info("saveSubscriptionFailureInPurchaseHistory :: purchaseDomainToSave : {}", purchaseDomainToSave);
		PurchaseHistory purchaseHistory = purchaseDomain.saveUpdatePurchaseDetails(purchaseDomainToSave, savedHistory, OfferConstants.UPDATE_ACTION.get(),headers);	 			
		purchaseResponseDto.setTransactionNo(purchaseHistory.getId());
		purchaseResponseDto.setPaymentStatus(null != paymentResponse ? paymentResponse.getPaymentStatus() : MarketplaceConstants.STATUS_FAILED.getConstant());
		
		resultResponse.setPurchaseResponseDto(purchaseResponseDto);
		resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(), 
				SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
		LOG.info("Exit saveSubscriptionFailureInPurchaseHistory");
	}
	
	
	public boolean cancelPhoneyTunesSubscription(Subscription subscription, String chargeabilityType, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("cancelPhoneyTunesSubscription : {}", subscription.getId());
		PhoneyTunesRequest phoneyTunesRequest = new PhoneyTunesRequest(subscription.getAccountNumber(), 0, 
				SubscriptionManagementConstants.CANCEL_SUBSCRIPTION.get(), chargeabilityType, subscription.getPhoneyTunesPackageId(), subscriptionCatalogValidator.fetchPhoneyTunesPackageId(chargeabilityType, subscription.getSubscriptionSegment()), headers.getExternalTransactionId());
		PhoneyTunesResponse phoneyTunesResponse = subscriptionService.getPhoneyTunesResponse(phoneyTunesRequest, resultResponse, headers.getExternalTransactionId());
		return (null != phoneyTunesResponse
				&& phoneyTunesResponse.getAckMessage().getStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())
					&& phoneyTunesResponse.getResponse().toUpperCase().contains(MarketplaceConstants.STATUS_SUCCESS.getConstant()));
	}
	
	
	public boolean cancelExtendSubscriptionForChannels(CachedValues cachedValues, Headers headers) throws SubscriptionManagementException {
		LOG.info("cancelExtendSubscriptionForChannels : {}", cachedValues.getSubscriptionAction());
		if(cachedValues.getSubscriptionAction().equalsIgnoreCase(SubscriptionManagementConstants.CANCEL_ONE_EXTEND_ONE.get())
				|| cachedValues.getSubscriptionAction().equalsIgnoreCase(SubscriptionManagementConstants.CANCEL_ONE_SUBSCRIBE_LIFE.get())) {
			
			subscriptionDomain.cancelSubscription(null, cachedValues.getSubscribedSubscription(), cachedValues.getSubscriptionAction(), headers);
			return true;
		} 
		if (cachedValues.getSubscriptionAction().equalsIgnoreCase(SubscriptionManagementConstants.CANCEL_AUTO_SUBSCRIBE_ONE.get())
				|| cachedValues.getSubscriptionAction().equalsIgnoreCase(SubscriptionManagementConstants.CANCEL_AUTO_SUBSCRIBE_LIFE.get())) {
			
			ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
			//to skip phoneytunes call if AutoRenewable Subscription is in coolingPeriod by checking EndDate value
			if(!ObjectUtils.isEmpty(cachedValues.getSubscribedSubscription().getEndDate())
					||  (((cachedValues.getSubscribedSubscription().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
							|| cachedValues.getSubscribedSubscription().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get()))
							&& cancelPhoneyTunesSubscription(cachedValues.getSubscribedSubscription(), SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get(), headers, resultResponse))
						|| (cachedValues.getSubscribedSubscription().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())
							&& !headers.getChannelId().equalsIgnoreCase(SubscriptionManagementConstants.ADMIN_PORTAL.get())))) {
				subscriptionDomain.cancelSubscription(null, cachedValues.getSubscribedSubscription(), cachedValues.getSubscriptionAction(), headers);
				return true;
			}
		}
		return false;
	}
	
	public boolean checkFreeDurationEligibilty(int duration) {
		
		if(duration != 0) {
			return true;
		} else {
			return false;
		}
	
	}
	

	@Async
	public void addFreeOffer(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, Headers header) {
		LOG.info("Entering freeOffer Method");
//		Gifts giftsForCatalog = giftRepository.findByGifts_GiftDetails_SubscriptionCatalogId(subscriptionCatalog.getId());
//		LOG.info("giftsForCatalog : {}",giftsForCatalog);
		Gifts giftsForCatalog = giftRepository.findBySubscriptionCatalogIdAndIsActive(subscriptionCatalog.getId(),true);
		LOG.info("giftsForCatalog1 : {}",giftsForCatalog);
		if(!ObjectUtils.isEmpty(giftsForCatalog)) {
			PurchaseRequestDto offerPurchaseRequestDto = new PurchaseRequestDto();
			List<OfferGiftValues> offerGiftValues = giftsForCatalog.getOfferValues();
			for(OfferGiftValues offerGift : offerGiftValues) {
				PurchaseResultResponse purchaseResultResponse = new PurchaseResultResponse(header.getExternalTransactionId());
				offerPurchaseRequestDto.setUiLanguage(purchaseRequestDto.getUiLanguage());
				offerPurchaseRequestDto.setExtTransactionId(header.getExternalTransactionId());
				offerPurchaseRequestDto.setAuthorizationCode(purchaseRequestDto.getAuthorizationCode());
				offerPurchaseRequestDto.setAccountNumber(purchaseRequestDto.getAccountNumber());
				offerPurchaseRequestDto.setOfferId(offerGift.getOfferId());
				offerPurchaseRequestDto.setSelectedOption(SubscriptionManagementConstants.PAYMENT_METHOD_POINTS.get());
				offerPurchaseRequestDto.setSpentAmount(SubscriptionManagementConstants.ZERO_DOUBLE);
				offerPurchaseRequestDto.setSpentPoints(SubscriptionManagementConstants.ZERO_INTEGER);
				offerPurchaseRequestDto.setCouponQuantity(offerGift.getCouponQuantity());
				offerPurchaseRequestDto.setPaymentType(SubscriptionManagementConstants.PAYMENT_METHOD_POINTS.get());
				if(Checks.checkIsCashVoucher(offerGift.getOfferType())) {
					offerPurchaseRequestDto.setVoucherDenomination(offerGift.getDenomination());
				}
				if(Checks.checkIsDealVoucher(offerGift.getOfferType())) {
					offerPurchaseRequestDto.setSubOfferId(offerGift.getSubOfferId());	
				}
				offerPurchaseRequestDto.setSelectedPaymentItem(ProcessValues.getPurchaseItemFromOfferType(offerGift.getOfferType()));
				LOG.info("offerPurchaseRequestDto : {}",offerPurchaseRequestDto);
				purchaseResultResponse = purchaseDomain.validateAndSavePurchaseHistory(offerPurchaseRequestDto, purchaseResultResponse, header);
				LOG.info("purchaseResultResponse : {}",purchaseResultResponse);
			}
		}
		LOG.info("Exiting freeOffer Method");
	}
}
