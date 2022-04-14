package com.loyalty.marketplace.subscription.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.PaymentMethods;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.promocode.domain.PromoCodeDomain;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApply;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApplyAndValidate;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.domain.SubscriptionCatalogDomain;
import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
import com.loyalty.marketplace.subscription.inbound.dto.AccountSubscription;
import com.loyalty.marketplace.subscription.inbound.dto.CachedValues;
import com.loyalty.marketplace.subscription.inbound.dto.ManageRenewalRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.ManageSubscriptionRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.ValidDates;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepositoryHelper;
import com.loyalty.marketplace.subscription.service.SubscriptionService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MarketplaceValidator;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftRequestDto;

@Component
public class SubscriptionValidator extends MarketplaceValidator {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionValidator.class);
	
	@Autowired
	SubscriptionCatalogDomain subscriptionCatalogDomain;

	@Autowired
	SubscriptionDomain subscriptionDomain;

	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Autowired
	SubscriptionRepositoryHelper subscriptionRepositoryHelper;
	
	@Autowired
	PromoCodeDomain promoCodeDomain;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	SubscriptionUtils subscriptionUtils;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Value("${non.existing.member}")
    private String nonExisting;
	
	@Value("#{'${etisalat.cutomer.types}'.split(',')}")
	private List<String> etisalatCustomerTypes;
	
	@Value("#{'${dcp.payment.methods}'.split(',')}")
	private List<String> dcpPaymentMethods;
	
	private List<String> cancelExtendChannelList = Arrays.asList("RTF",SubscriptionManagementConstants.ADMIN_PORTAL.get(),SubscriptionManagementConstants.CHANNEL_ID_SAPP.get());
	private List<String> prepaidLifeTimeBogoList = Arrays.asList("OF_9999_BOGO_PRE");
		
	public SubscriptionCatalog validateSubscriptionCatalog(PurchaseRequestDto purchaseRequestDto, CachedValues cachedValues, Headers headers, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {	
		LOG.info("validateSubscriptionCatalog :: findByIdAndStatus");
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogDomain.findByIdAndStatus(purchaseRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
		if (!subscriptionCatalog.isPresent()) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getMsg());
			return null;
		} else {
			LOG.info("validateSubscriptionCatalog : {}",subscriptionCatalog.get());
			if(null != subscriptionCatalog.get().getEligibleChannels() && !SubscriptionCatalogValidator.checkEligibleCatalogByChannel(headers.getChannelId(), subscriptionCatalog.get().getEligibleChannels())) {
				cachedValues.setChannelEligible(false);
			}
			
			if(subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()) 
					&& purchaseRequestDto.getSelectedOption().equalsIgnoreCase(MarketplaceConstants.FULLCREDITCARD.getConstant())
					&& ObjectUtils.isEmpty(purchaseRequestDto.getMasterEPGTransactionId())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.NO_MASTER_EPG_TRANSACTION_ID_FOUND.getIntId(),
							SubscriptionManagementCode.NO_MASTER_EPG_TRANSACTION_ID_FOUND.getMsg());
				return null;
			}
			
			validatePaymentForSubscription(purchaseRequestDto, subscriptionCatalog, cachedValues);		
			if(cachedValues.isPaymentRequired()
					&& !SubscriptionCatalogValidator.checkEligiblePaymentMethods(purchaseRequestDto.getSelectedOption(), subscriptionCatalog.get().getPaymentMethods(), resultResponse)) {
				return null;
			}
				
			return subscriptionCatalog.get();
		}
	}
	
	public SubscriptionCatalog validateOneTimeSubscriptionCatalog(PurchaseRequestDto purchaseRequestDto, CachedValues cachedValues, String paymentType, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("validateOneTimeSubscriptionCatalog :: findByIdAndChargeabilityType");
		Optional<SubscriptionCatalog> oneTimeSubscriptionCatalog = subscriptionCatalogDomain.findByIdAndChargeabilityType(purchaseRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get());
		if(!oneTimeSubscriptionCatalog.isPresent()) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getMsg());
			return null;
		} else {
			LOG.info("validateFreeOneTimeSubscriptionCatalog : {}",oneTimeSubscriptionCatalog.get());
			if(!oneTimeSubscriptionCatalog.get().getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIBTIONCATALOG_NOT_ACTIVE.getIntId(),
						SubscriptionManagementCode.SUBSCRIBTIONCATALOG_NOT_ACTIVE.getMsg());
				return null;
			} else {
				if(oneTimeSubscriptionCatalog.get().getCost() <= 0 && oneTimeSubscriptionCatalog.get().getPointsValue() <= 0) {
					cachedValues.setPaymentRequired(false);
					if(oneTimeSubscriptionCatalog.get().getValidityPeriod() <= 0) {
						cachedValues.setLifeTimeFree(true);
					}
				} else {
					if(!SubscriptionCatalogValidator.checkEligiblePaymentMethods(purchaseRequestDto.getSelectedOption(), oneTimeSubscriptionCatalog.get().getPaymentMethods(), resultResponse)) {
						return null;
					}
				} 
			}
		}			
		return oneTimeSubscriptionCatalog.get();
	}
	
	private void validatePaymentForSubscription(PurchaseRequestDto purchaseRequestDto, Optional<SubscriptionCatalog> subscriptionCatalog, CachedValues cachedValues) {
		LOG.info("Inside validatePaymentForSubscription");		
		if(null != purchaseRequestDto.getSelectedOption() && (purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get()) ||
				purchaseRequestDto.getSelectedOption().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_PREPAID.get()))) {
			cachedValues.setPaymentRequired(false);
		}
			
		if(subscriptionCatalog.isPresent() && subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
			cachedValues.setLifeTimeFree(true);
			cachedValues.setPaymentRequired(false);
		}
		if(subscriptionCatalog.isPresent() && subscriptionCatalog.get().getCost() <= 0 && subscriptionCatalog.get().getPointsValue() <= 0) {
			cachedValues.setPaymentRequired(false);				
			if(subscriptionCatalog.get().getValidityPeriod() <= 0) {
				cachedValues.setLifeTimeFree(true);
			}
		}
		LOG.info("validatePaymentForSubscription :: isPaymentRequired : {}, isLifeTimeFree : {}",cachedValues.isPaymentRequired(), cachedValues.isLifeTimeFree());
	}
	
	public SubscriptionCatalog validateOneTimeSubscriptionCatalog(PurchaseRequestDto purchaseRequestDto, CachedValues cachedValues, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("validateOneTimeSubscriptionCatalog :: findByIdAndChargeabilityType");
		Optional<SubscriptionCatalog> oneTimeSubscriptionCatalog = subscriptionCatalogDomain.findByIdAndChargeabilityType(purchaseRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get());
		if(!oneTimeSubscriptionCatalog.isPresent()) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getMsg());
			return null;
		} else {
			LOG.info("validateFreeOneTimeSubscriptionCatalog : {}",oneTimeSubscriptionCatalog.get());
			if(!oneTimeSubscriptionCatalog.get().getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIBTIONCATALOG_NOT_ACTIVE.getIntId(),
						SubscriptionManagementCode.SUBSCRIBTIONCATALOG_NOT_ACTIVE.getMsg());
				return null;
			}
			if(oneTimeSubscriptionCatalog.get().getCost() <= 0 && oneTimeSubscriptionCatalog.get().getPointsValue() <= 0) {
				cachedValues.setPaymentRequired(false);
			}
			if(oneTimeSubscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
				cachedValues.setLifeTimeFree(true);
				cachedValues.setPaymentRequired(false);
			} 
			if(cachedValues.isPaymentRequired() 
					&& !SubscriptionCatalogValidator.checkEligiblePaymentMethods(purchaseRequestDto.getSelectedOption(), oneTimeSubscriptionCatalog.get().getPaymentMethods(), resultResponse)) {
				return null;
			}		
		}			
		return oneTimeSubscriptionCatalog.get();
	}
	
	public SubscriptionCatalog validateAndfetchOneTimeFreeSubscriptionCatalogForAccount(PurchaseRequestDto purchaseRequestDto, 
			WelcomeGiftRequestDto welcomeGiftRequestDto, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("fetchOneTimeFreeSubscriptionCatalogList :: findByIdAndChargeabilityType");
		SubscriptionCatalog subscriptionCatalog = new SubscriptionCatalog();
		List<SubscriptionCatalog> oneTimeSubscriptionCatalogList = subscriptionCatalogDomain.findByChargeabilityTypeAndStatus(
				SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
		if(!oneTimeSubscriptionCatalogList.isEmpty()) {
			subscriptionCatalog = fetchOneTimeWelcomeGiftSubscriptionCatalogForAccount(purchaseRequestDto, welcomeGiftRequestDto, oneTimeSubscriptionCatalogList, resultResponse);
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_FREE_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_FREE_NOT_FOUND.getMsg());			
		}			
		return subscriptionCatalog;
	}
	
	public SubscriptionCatalog fetchOneTimeWelcomeGiftSubscriptionCatalogForAccount(PurchaseRequestDto purchaseRequestDto, WelcomeGiftRequestDto welcomeGiftRequestDto, 
			List<SubscriptionCatalog> oneTimeSubscriptionCatalogList, PurchaseResultResponse resultResponse) {
		List<SubscriptionCatalog> oneTimeSubscriptionCatalog = new ArrayList<>();
		if(purchaseRequestDto.getSubscriptionMethod().equalsIgnoreCase(SubscriptionManagementConstants.WELCOME_GIFT.get())) {
			for(SubscriptionCatalog subscriptionCatalog : oneTimeSubscriptionCatalogList) {
				if(subscriptionCatalog.getCost() == 0 || subscriptionCatalog.getPointsValue() == 0 
						&& validateAccountForWelcomeGiftSubscription(purchaseRequestDto, welcomeGiftRequestDto, subscriptionCatalog, resultResponse)) {
					LOG.info("fetchOneTimeWelcomeGiftSubscriptionCatalogForAccount :: subscriptionCatalog : {}",subscriptionCatalog);
					oneTimeSubscriptionCatalog.add(subscriptionCatalog);
				}
			}
			LOG.info("fetchOneTimeWelcomeGiftSubscriptionCatalogForAccount : {}", oneTimeSubscriptionCatalog);
			if(oneTimeSubscriptionCatalog.isEmpty()) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_FREE_NOT_FOUND.getIntId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_FREE_NOT_FOUND.getMsg());
				return null;
			} else if(oneTimeSubscriptionCatalog.size() > 1) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.MULTIPLE_FREE_SUBSCRIPTION_FOUND.getIntId(),
						SubscriptionManagementCode.MULTIPLE_FREE_SUBSCRIPTION_FOUND.getMsg());
				return null;
			}
			return oneTimeSubscriptionCatalog.get(0);
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_METHOD_NOT_APPLICABLE.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTION_METHOD_NOT_APPLICABLE.getMsg()+" Accepted Methods : [welcome]");
			return null;
		}
		
	}
	
	
	public boolean validateSubscriptionAndApplyPromoCode(PurchaseRequestDto purchaseRequestDto, CachedValues cachedValues, Headers headers, 
			SubscriptionCatalog subscriptionCatalog, PurchaseResultResponse resultResponse) throws SubscriptionManagementException, ParseException {
		LOG.info("validateSubscriptionAndApplyPromoCode :: findBySubscriptionCatalogIdAndAccountNumber");			
		AccountSubscription accountSubscription = validateAndPopulateAccountSubscription(purchaseRequestDto.getAccountNumber(), resultResponse);
		
		if(null != accountSubscription) {
			if(accountSubscription.getParkedSubscription().stream()
					.anyMatch(p -> p.getSubscriptionSegment().equalsIgnoreCase(subscriptionCatalog.getSubscriptionSegment()))) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_PARKED.getIntId(),
						SubscriptionManagementCode.ACCOUNT_PARKED.getMsg());
				return false;
			}
			
			if(accountSubscription.getSubscribedSubscription().stream()
					.anyMatch(p -> p.getSubscriptionSegment().equalsIgnoreCase(subscriptionCatalog.getSubscriptionSegment()))) {
				
				Optional<Subscription> subscribedSubscription = accountSubscription.getSubscribedSubscription().stream()
						.filter(p -> p.getSubscriptionSegment().equalsIgnoreCase(subscriptionCatalog.getSubscriptionSegment()) && 
								p.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())).findFirst();
				if(subscribedSubscription.isPresent()) {
					SubscriptionCatalog subscribedCatalog = subscriptionCatalogDomain.findSubscriptionCatalog(subscribedSubscription.get().getSubscriptionCatalogId());
					if(cancelExtendChannelList.contains(headers.getChannelId()) && null != subscribedCatalog) {
						LOG.info("validateSubscriptionAndApplyPromoCode :: subscriptionCatalogId : {} :: subscribedCatalogId : {}", subscriptionCatalog.getId(), subscribedSubscription.get().getSubscriptionCatalogId());
						if(!validateSubscriptionForCancelExtendChannels(subscriptionCatalog, subscribedCatalog, cachedValues, resultResponse, subscribedSubscription, headers)) {
							return false;
						}
						cachedValues.setSubscribedCatalog(subscribedCatalog);
						cachedValues.setSubscribedSubscription(subscribedSubscription.get());
					} else {
						resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_EXISTS.getIntId(),
								SubscriptionManagementCode.SUBSCRIPTION_EXISTS.getMsg());
						return false;
					}
				}
				
			}	
		}
		
		LOG.info("validateSubscriptionAndApplyPromoCode :: applyPromocode : {}", subscriptionCatalog);
		LOG.info("AccountNumber before calling applyPromoCode : {}",purchaseRequestDto.getAccountNumber());
		if(!applyPromocode(purchaseRequestDto, subscriptionCatalog, cachedValues, headers, resultResponse)) {
			return false;
		}
		if (cachedValues.isPaymentRequired() && MarketplaceConstants.FULLPOINTS.getConstant().equalsIgnoreCase(purchaseRequestDto.getSelectedOption()) 
				&& subscriptionCatalog.getPointsValue() > 0	&& null != purchaseRequestDto.getSpentPoints() && subscriptionCatalog.getPointsValue() > purchaseRequestDto.getSpentPoints()) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INSUFFICIENT_POINTS.getIntId(),
						SubscriptionManagementCode.INSUFFICIENT_POINTS.getMsg());
			return false;
		} 
		
		return true;
	}
	
	public boolean validateSubscription(List<Subscription> subscriptionList, PurchaseRequestDto purchaseRequestDto, PurchaseResultResponse resultResponse) {
		for(Subscription subscription : subscriptionList) {
			if (subscription.getSubscriptionCatalogId().equalsIgnoreCase(purchaseRequestDto.getSubscriptionCatalogId()) 
					&& subscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_EXISTS.getIntId(),
						SubscriptionManagementCode.SUBSCRIPTION_EXISTS.getMsg());
				return false;
			} else if (subscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_SUBSCRIBED.getIntId(),
						SubscriptionManagementCode.ACCOUNT_SUBSCRIBED.getMsg());
				return false;		
			} else if(subscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_PARKED.getIntId(),
						SubscriptionManagementCode.ACCOUNT_PARKED.getMsg());
				return false;
			} 
		}
		return true;
	}
	
	public Subscription validateSubscription(List<Subscription> accountSubscriptionList, PurchaseResultResponse resultResponse) {
		LOG.info("validateSubscription :: subscriptionList : {}", accountSubscriptionList);
		if(!accountSubscriptionList.isEmpty()) {
			if(accountSubscriptionList.stream().anyMatch(o -> o.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get()))) {
				Optional<Subscription> subscription = accountSubscriptionList.stream().filter(o -> o.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())).findFirst();
				if(subscription.isPresent()) {
					resultResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_PARKED.getIntId(),
							SubscriptionManagementCode.ACCOUNT_PARKED.getMsg());
					return subscription.get();
				}
			} else if(accountSubscriptionList.stream().anyMatch(o -> o.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get()))) {
				Optional<Subscription> subscription = accountSubscriptionList.stream().filter(o -> o.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())).findFirst();
				if(subscription.isPresent()) {
					return subscription.get();
				}
			}
		}
		return null;
	}
	
	public AccountSubscription validateAndPopulateAccountSubscription(String accountNumber, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("populateSubscribedSubscription :: accountNumber : {}", accountNumber);
		List<Subscription> subscriptionList = subscriptionDomain.findByAccountNumber(accountNumber);
		LOG.info("validateAndPopulateAccountSubscription :: subscriptionList : {}", subscriptionList);
		
		AccountSubscription accountSubscription = null;
		if(!subscriptionList.isEmpty()) {
			accountSubscription = new AccountSubscription();
			accountSubscription.setParkedSubscription(subscriptionList.stream()
					.filter(o -> o.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())).collect(Collectors.toList()));
			
			accountSubscription.setSubscribedSubscription(subscriptionList.stream()
					.filter(o -> o.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())).collect(Collectors.toList()));
			
		}
		return accountSubscription;
	}
	
	public boolean validateSubscriptionForCancelExtendChannels(SubscriptionCatalog subscriptionCatalog, SubscriptionCatalog subscribedCatalog, CachedValues cachedValues, PurchaseResultResponse resultResponse, Optional<Subscription> subscribedSubscription, Headers headers) throws ParseException {
		LOG.info("Inside validateSubscriptionForCancelExtendChannels");
		if(subscribedCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
			if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
				cachedValues.setExtendValidityPeriod(true);
				cachedValues.setSubscriptionAction(SubscriptionManagementConstants.CANCEL_ONE_EXTEND_ONE.get());
				return true;
			} else if (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.ONE_TIME_SUBSCRIBED.getIntId(),
						SubscriptionManagementCode.ONE_TIME_SUBSCRIBED.getMsg());
				return false;
			} else if (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
				if(prepaidLifeTimeBogoList.contains(subscriptionCatalog.getId())) {
					return false;
				} else {
					cachedValues.setSubscriptionAction(SubscriptionManagementConstants.CANCEL_ONE_SUBSCRIBE_LIFE.get());
					return true;
				}
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getIntId(),
						SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getMsg());
				return false;
			}					
		} else if(subscribedCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
				//Cancel existing auto and subscribe to one-time
//				int leftOverDays = (!ObjectUtils.isEmpty(headers) && !ObjectUtils.isEmpty(headers.getChannelId()) 
//						&& Arrays.asList(SubscriptionManagementConstants.CHANNEL_ID_SAPP.get(),SubscriptionManagementConstants.ADMIN_PORTAL.get()).contains(headers.getChannelId())) 
//						? subscriptionUtils.diffBetweenDates(subscribedSubscription.get().getNextRenewalDate()) : 0;
//				LOG.info("Inside validateSubscriptionForCancelExtendChannels - LeftOverDays :: {}", leftOverDays);
				cachedValues.setSubscriptionAction(SubscriptionManagementConstants.CANCEL_AUTO_SUBSCRIBE_ONE.get());
//				cachedValues.setLeftOverDays(leftOverDays);
				cachedValues.setCoolingPeriod(true);
				return true;
			} else if (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.AUTO_RENEWABLE_SUBSCRIBED.getIntId(),
						SubscriptionManagementCode.AUTO_RENEWABLE_SUBSCRIBED.getMsg());
				return false;
			} else if (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
				//Cancel existing auto and subscribe to the lifetime pack
				if(prepaidLifeTimeBogoList.contains(subscriptionCatalog.getId())) {
					return false;
				} else {
					cachedValues.setSubscriptionAction(SubscriptionManagementConstants.CANCEL_AUTO_SUBSCRIBE_LIFE.get());
					return true;
				}
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getIntId(),
						SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getMsg());
				return false;
			}					
		} else if(subscribedCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getIntId(),
					SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getMsg());
			return false;
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getIntId(),
					SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getMsg());
			return false;
		}
	}
	
	private boolean applyPromocode(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, CachedValues cachedValues, Headers headers, PurchaseResultResponse resultResponse) {
		purchaseRequestDto.setFreeSubscriptionDays(0);
		if(null != purchaseRequestDto.getPromoCode() && !purchaseRequestDto.getPromoCode().isEmpty() && !purchaseRequestDto.getPromoCode().equals("")) {
			LOG.info("applyPromocode :: calling applyPromoCode");
			LOG.info("AccountNumber inside applyPromocode : {}",purchaseRequestDto.getAccountNumber());
			PromoCodeApplyAndValidate promoCodeApply = promoCodeDomain.applyPromoCode(purchaseRequestDto.getPromoCode(), purchaseRequestDto.getSubscriptionCatalogId(), null, subscriptionCatalog.getCost(), 
					subscriptionCatalog.getPointsValue(), purchaseRequestDto.getAccountNumber(), 0, headers, null, resultResponse);
			if(promoCodeApply.getPromoCodeApply().getStatus() == 0) {
				LOG.info("applyPromocode :: Updated Values From applyPromoCode :: points Value : {} :: cost : {} :: promo duration : {}",promoCodeApply.getPromoCodeApply().getPointsValue(), promoCodeApply.getPromoCodeApply().getCost(), promoCodeApply.getPromoCodeApply().getDuration());
				cachedValues.setPromoGift(true);
				cachedValues.setPaymentRequired(!(promoCodeApply.getPromoCodeApply().getPointsValue() <= 0 && Double.valueOf(promoCodeApply.getPromoCodeApply().getCost()) <= 0));
				LOG.info("applyPromocode :: cachedValues.isPaymentRequired : {}",cachedValues.isPaymentRequired());
				subscriptionCatalog.setPointsValue(promoCodeApply.getPromoCodeApply().getPointsValue());
				subscriptionCatalog.setCost(promoCodeApply.getPromoCodeApply().getCost());
				if(null != promoCodeApply.getPromoCodeApply().getPromoType() && !promoCodeApply.getPromoCodeApply().getPromoType().isEmpty() 
						&& promoCodeApply.getPromoCodeApply().getPromoType().equalsIgnoreCase(SubscriptionManagementConstants.FREE_DURATION_PROMO_TYPE.get())
						&& !subscriptionUtils.hasAvailedFreeDuration(purchaseRequestDto.getAccountNumber(), subscriptionCatalog.getSubscriptionSegment())) {
					purchaseRequestDto.setFreeSubscriptionDays(promoCodeApply.getPromoCodeApply().getDuration()-subscriptionCatalog.getFreeDuration());
				} else {
					purchaseRequestDto.setFreeSubscriptionDays(promoCodeApply.getPromoCodeApply().getDuration());
				}
				promoCodeDomain.burnSubsPromoVoucher(purchaseRequestDto, headers);
			} else {
				LOG.info("applyPromocode :: Remove invalid promocode : {}",purchaseRequestDto.getPromoCode());
				purchaseRequestDto.setPromoCode(null);
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_PROMO_CODE.getIntId(),
						SubscriptionManagementCode.INVALID_PROMO_CODE.getMsg());
				return false;
			}
		} 
		return true;
	}
	
	
	public GetMemberResponseDto validateAccountForSubscription(PurchaseRequestDto purchaseRequestDto, Headers headers, SubscriptionCatalog subscriptionCatalog, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {
		try {
			LOG.info("validateAccountForSubscription :: calling getMemberDetailsForPayment");
			GetMemberResponseDto getMemberResponseDto = fetchServiceValues.getMemberDetailsForPayment(purchaseRequestDto.getAccountNumber(), ProcessValues.getIncludeMemberDetailsForPayment(), headers, resultResponse);
			LOG.info("validateAccountForSubscription :: getMemberResponseDto : {}", getMemberResponseDto);
			LOG.info("validateAccountForSubscription :: calling getMemberInfo");
			GetMemberResponse getMemberResponse = ProcessValues.getMemberInfo(getMemberResponseDto, purchaseRequestDto.getAccountNumber());
			LOG.info("validateAccountForSubscription :: getMemberResponse : {}", getMemberResponse);
			
			if (null != getMemberResponse) {
				
				LOG.info("validate invalid Member");
				if(null != purchaseRequestDto.getMembershipCode() && !purchaseRequestDto.getMembershipCode().isEmpty() && !getMemberResponse.getMembershipCode().equals(purchaseRequestDto.getMembershipCode())) {
					LOG.info("validateAccountForSubscription : invalid Member");
					resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_MEMBERSHIP_CODE.getIntId(),
						SubscriptionManagementCode.INVALID_MEMBERSHIP_CODE.getMsg());
					return null;
				}
				
				LOG.info("validateAccountForSubscription :: getMembershipCode : {}",getMemberResponse.getMembershipCode());
				purchaseRequestDto.setMembershipCode(getMemberResponse.getMembershipCode());
				
				LOG.info("validate invalid CustomerSegment");
				if(!SubscriptionCatalogValidator.validateCustomerSegment(subscriptionCatalog.getCustomerSegments(), getMemberResponse.getCustomerType(), resultResponse)) {
					LOG.info("validateAccountForSubscription : invalid CustomerSegment");
					return null;
				}
				
				LOG.info("validate invalid EtisalatCustomerType");
				if(!validateEtisalatCustomerType(getMemberResponse, subscriptionCatalog, purchaseRequestDto, headers, resultResponse)) {
					
					LOG.info("validateAccountForSubscription : invalidEtisalatCustomerType ");
					return null;
				}
				
				LOG.info("validate invalid PaymentMethod");
				if(!SubscriptionCatalogValidator.checkEligiblePaymentMethodsForAccount(purchaseRequestDto.getSelectedOption(), getMemberResponse.getCustomerType().get(0), getMemberResponse.getEligiblePaymentMethod(), resultResponse)) {
					
					LOG.info("validateAccountForSubscription : invalidPaymentMethod ");
					return null;
				} 
				
				
				LOG.info("validateAccountForSubscription :hasAvailedFreeDuration");			
				if (subscriptionUtils.hasAvailedFreeDuration(purchaseRequestDto.getAccountNumber(), subscriptionCatalog.getSubscriptionSegment())) {
					subscriptionCatalog.setFreeDuration(0);
				}
				
				purchaseRequestDto.setAdditionalParams(getMemberResponse.getCustomerType().get(0));
				return getMemberResponseDto;
			} else {
				LOG.info("validateAccountForSubscription :: MembershipCode : nonExisting");
				purchaseRequestDto.setMembershipCode(nonExisting);
			}
		} catch (MarketplaceException e) {
			
			exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), headers.getUserName());
			e.printStackTrace();
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "validateAccountForSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return null;
	}
	
	public boolean validateAccountForWelcomeGiftSubscription(PurchaseRequestDto purchaseRequestDto, WelcomeGiftRequestDto welcomeGiftRequestDto,
			SubscriptionCatalog subscriptionCatalog, PurchaseResultResponse resultResponse) {
		try {		
			if(null != welcomeGiftRequestDto.getMembershipCode()) {
				LOG.info("validateAccountForWelcomeGiftSubscription : invalid Member");
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_MEMBERSHIP_CODE.getIntId(),
					SubscriptionManagementCode.INVALID_MEMBERSHIP_CODE.getMsg());
				return false;
			}
			if(!SubscriptionCatalogValidator.validateCustomerSegment(subscriptionCatalog.getCustomerSegments(), welcomeGiftRequestDto.getCustomerType(), resultResponse)) {
				LOG.info("validateAccountForWelcomeGiftSubscription : invalid CustomerSegment");
				return false;
			}
			
			LOG.info("validateAccountForWelcomeGiftSubscription :: getMembershipCode : {}",welcomeGiftRequestDto.getMembershipCode());
			purchaseRequestDto.setMembershipCode(welcomeGiftRequestDto.getMembershipCode());
			LOG.info("validateAccountForWelcomeGiftSubscription :hasAvailedFreeDuration");
			if (subscriptionUtils.hasAvailedFreeDuration(purchaseRequestDto.getAccountNumber())) {
				subscriptionCatalog.setFreeDuration(0);
			}
			
			return true;

		} catch (Exception e) {
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "validateAccountForSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		return false;
	}
	
	public boolean validateEtisalatCustomerType(GetMemberResponse getMemberResponse, SubscriptionCatalog subscriptionCatalog, 
			PurchaseRequestDto purchaseRequestDto, Headers headers, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("validateEtisalatCustomerType");
		List<String> customerType = etisalatCustomerTypes.stream().filter(getMemberResponse.getCustomerType()::contains).collect(Collectors.toList());
		LOG.info("validateEtisalatCustomerType :: Customer Type : {}",customerType);
		// Relaxed below validation for non etislat customer if puchase is using full cc
		if(customerType.isEmpty() && subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()) && 
				!(purchaseRequestDto.getSelectedOption().equalsIgnoreCase(MarketplaceConstants.FULLCREDITCARD.getConstant()))) {
			LOG.info("validateEtisalatCustomerType : invalid chargeability");
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.NON_ETISALAT_AUTO_EXCEPTION.getIntId(),
					SubscriptionManagementCode.NON_ETISALAT_AUTO_EXCEPTION.getMsg());
			return false;		
		} else if(customerType.contains(SubscriptionManagementConstants.ENTERPRISE_CUSTOMER_TYPE_MOBILE.get())) {
			if(subscriptionService.isCrmAddOnServiceDisabled(purchaseRequestDto.getAccountNumber(), headers)
				&& dcpPaymentMethods.contains(purchaseRequestDto.getSelectedOption().toLowerCase())) {
				
				LOG.info("validateEtisalatCustomerType : crm exception");
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.CRM_DISABLED_EXCEPTION.getIntId(),
					SubscriptionManagementCode.CRM_DISABLED_EXCEPTION.getMsg());
				return false;	
			}
			LOG.info("Enterprise Customer type DCB Check");
			if(!(getMemberResponse.getEligiblePaymentMethod().stream().
					anyMatch(p -> p.getDescription().equals(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
							&& p.getDescription().equals(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())))) {
				String numberType = getMemberResponse.getNumberType();
				if(null != numberType && getMemberResponse.getNumberType().equalsIgnoreCase(SubscriptionManagementConstants.NUMBER_TYPE_PREPAID.get())) {
					List<PaymentMethods> eligiblePaymentMethods = getMemberResponse.getEligiblePaymentMethod();
					LOG.info("eligiblePaymentMethods : {}",eligiblePaymentMethods);
					PaymentMethods paymentMethod = new PaymentMethods();
					paymentMethod.setPaymentMethodId("4");					
					paymentMethod.setDescription(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get());
					eligiblePaymentMethods.add(paymentMethod);
					getMemberResponse.setEligiblePaymentMethod(eligiblePaymentMethods);
					LOG.info("modified eligiblePaymentMethods : {}",getMemberResponse.getEligiblePaymentMethod());
				}
				if(null != numberType && getMemberResponse.getNumberType().equalsIgnoreCase(SubscriptionManagementConstants.NUMBER_TYPE_POSTPAID.get())) {
					List<PaymentMethods> eligiblePaymentMethods = getMemberResponse.getEligiblePaymentMethod();
					LOG.info("eligiblePaymentMethods : {}",eligiblePaymentMethods);
					PaymentMethods paymentMethod = new PaymentMethods();
					paymentMethod.setPaymentMethodId("3");					
					paymentMethod.setDescription(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get());
					eligiblePaymentMethods.add(paymentMethod);
					getMemberResponse.setEligiblePaymentMethod(eligiblePaymentMethods);
					LOG.info("modified eligiblePaymentMethods : {}",getMemberResponse.getEligiblePaymentMethod());
				}
				
			}
			
		} 
		return true;
	}
		
	
		
	public ValidDates calculateValidDates(PurchaseRequestDto purchaseRequestDto, CachedValues cachedValues, SubscriptionCatalog subscriptionCatalog, Headers headers, ResultResponse resultResponse) {	
		ValidDates validDates = new ValidDates();
		LOG.info("calculateValidDates :: cachedValues : {}",cachedValues);
		try {
			if(cachedValues.isExtendValidityPeriod()) {
				LOG.info("calculateValidDates :: extendValidityPeriod : {}",subscriptionCatalog.getValidityPeriod());
				
					validDates.setValidStartDate(cachedValues.getSubscribedSubscription().getStartDate());
					validDates.setValidEndDate(calculateExtendedEndDate(cachedValues.getSubscribedSubscription().getEndDate(), subscriptionCatalog.getValidityPeriod()));
					
			} else if(cachedValues.isCoolingPeriod() && cachedValues.getSubscriptionAction().equalsIgnoreCase(SubscriptionManagementConstants.CANCEL_AUTO_SUBSCRIBE_ONE.get()) && cachedValues.getSubscribedCatalog().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()) && !ObjectUtils.isEmpty(headers) && !ObjectUtils.isEmpty(headers.getChannelId()) 
					&& Arrays.asList(SubscriptionManagementConstants.CHANNEL_ID_SAPP.get(),SubscriptionManagementConstants.ADMIN_PORTAL.get()).contains(headers.getChannelId())) {
				String startDate = !StringUtils.isEmpty(purchaseRequestDto.getStartDate())
						? purchaseRequestDto.getStartDate() : new SimpleDateFormat(SubscriptionManagementConstants.SIMPLE_DATE_FORMAT.get()).format(new Date());
				validDates.setValidStartDate(SubscriptionCatalogValidator.validateDate(startDate, resultResponse));	
				validDates.setValidEndDate(calculateExtendedEndDate(cachedValues.getSubscribedSubscription().getNextRenewalDate(), subscriptionCatalog.getValidityPeriod()));
			}  else {
				LOG.info("calculateValidDates :: validityPeriod : {}",subscriptionCatalog.getValidityPeriod());	
				String startDate = !StringUtils.isEmpty(purchaseRequestDto.getStartDate())
						? purchaseRequestDto.getStartDate() : new SimpleDateFormat(SubscriptionManagementConstants.SIMPLE_DATE_FORMAT.get()).format(new Date());
				//validDates.setValidStartDate(new Date());
				validDates.setValidStartDate(SubscriptionCatalogValidator.validateDate(startDate, resultResponse));	
				
				Date validEndDate = (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())
						|| subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())
						|| cachedValues.isLifeTimeFree())
					? null : SubscriptionCatalogValidator.calculateEndDate(startDate, subscriptionCatalog.getValidityPeriod(), resultResponse);
				validDates.setValidEndDate(validEndDate);			
			}
			
			LOG.info("calculateValidDates :: Valid Dates : {} :: {}",validDates.getValidStartDate(),validDates.getValidEndDate());
		} catch (SubscriptionManagementException sme) {
			
			exceptionLogService.saveExceptionsToExceptionLogs(sme, purchaseRequestDto.getExtTransactionId(), purchaseRequestDto.getAccountNumber(), null);
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "calculateValidDates",
					sme.getClass() + sme.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		return validDates;
	}
	
	public ValidDates calculateValidDatesforExtendedPartner(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, ResultResponse resultResponse) {	
		ValidDates validDates = new ValidDates();
		int validityPeriod = 0;
		try {
			if(null != purchaseRequestDto.getCardType() && purchaseRequestDto.getCardType().equalsIgnoreCase(SubscriptionManagementConstants.CARD_TYPE_PLATINUM.get())) {
				validityPeriod = 365;
			} else if (null != purchaseRequestDto.getCardType() && purchaseRequestDto.getCardType().equalsIgnoreCase(SubscriptionManagementConstants.CARD_TYPE_SIGNATURE.get())) {
				validityPeriod = 365;
			} else {
				validityPeriod = subscriptionCatalog.getValidityPeriod();
			}
						
			LOG.info("calculateValidDates :: validityPeriod : {}",validityPeriod);
			String startDate = null != purchaseRequestDto.getStartDate() ? purchaseRequestDto.getStartDate() : 
				new SimpleDateFormat(SubscriptionManagementConstants.SIMPLE_DATE_FORMAT.get()).format(new Date());
			validDates.setValidStartDate(SubscriptionCatalogValidator.validateDate(startDate, resultResponse));
			
			Date validEndDate = ((null != purchaseRequestDto.getSubscriptionMethod()
					&& purchaseRequestDto.getSubscriptionMethod().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_METHOD_FREE.get()))
					|| subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()))
				? null : SubscriptionCatalogValidator.calculateEndDate(startDate, validityPeriod, resultResponse);
			validDates.setValidEndDate(validEndDate);
			LOG.info("calculateValidDates :: StartDate : {} :: EndDate : {}",validDates.getValidStartDate(),validDates.getValidEndDate());
		} catch (SubscriptionManagementException sme) {
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "calculateValidDates",
					sme.getClass() + sme.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		return validDates;
	}
	
	public Date calculateExtendedEndDate(Date endDate, int extendValidityPeriod) {
		LOG.info("endDate : {}",endDate);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		LOG.info("Present End Date : {}",cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, extendValidityPeriod);
		LOG.info("Updated End Date : {}",cal.getTime());
		
		return cal.getTime();
		
	}
		
	public List<Subscription> validateAutoSubscription(List<Subscription> subscriptionList, ResultResponse resultResponse) throws SubscriptionManagementException {
		List<Subscription> autoSubscriptionList = new ArrayList<>();
		
		if (!subscriptionList.isEmpty()) {
			LOG.info("validateAutoSubscription :: subscriptionList : {}", subscriptionList);					
			for (Subscription subscription : subscriptionList) {
				if(subscriptionCatalogDomain.findByIdAndChargeabilityType(subscription.getSubscriptionCatalogId(), 
						SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()).isPresent()) {
					autoSubscriptionList.add(subscription);						
				}
			}
			if(autoSubscriptionList.isEmpty()) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIBTION_AUTO_NOT_FOUND.getIntId(),
						SubscriptionManagementCode.SUBSCRIBTION_AUTO_NOT_FOUND.getMsg());
			}
		}
		return autoSubscriptionList;
	}
		
	public List<Subscription> validateSubscriptionExists(ManageSubscriptionRequestDto manageSubscriptionRequestDto, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		List<Subscription> subscriptionList = new ArrayList<>();
		if(null != manageSubscriptionRequestDto.getSubscriptionId() && !manageSubscriptionRequestDto.getSubscriptionId().isEmpty()) {
			
			Optional<Subscription> subscription = subscriptionDomain.findById(manageSubscriptionRequestDto.getSubscriptionId());
			if(subscription.isPresent()) {
				subscriptionList.add(subscription.get());
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
				SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
			}
			
		} else if (null != manageSubscriptionRequestDto.getAccountNumber() && !manageSubscriptionRequestDto.getAccountNumber().isEmpty()) {
			List<Subscription> subscriptionListForAccount = subscriptionDomain.findByAccountNumber(manageSubscriptionRequestDto.getAccountNumber());
			if(null != manageSubscriptionRequestDto.getPackageId() && !manageSubscriptionRequestDto.getPackageId().isEmpty()) {
				subscriptionListForAccount = subscriptionListForAccount.stream().filter(p -> null != p.getPhoneyTunesPackageId() && p.getPhoneyTunesPackageId()
						.equalsIgnoreCase(manageSubscriptionRequestDto.getPackageId())).collect(Collectors.toList());
			} else {
				subscriptionListForAccount = subscriptionListForAccount.stream().filter(p -> p.getSubscriptionSegment()
						.equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())).collect(Collectors.toList());
			}
			
			for(Subscription subscription : subscriptionListForAccount) {
				subscriptionList.add(subscription);
			}
			if(subscriptionList.isEmpty()) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
						SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
			}
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_PARAMETERS.getIntId(),
					SubscriptionManagementCode.INVALID_PARAMETERS.getMsg()+" : No SubscriptionId or Account Number found in Request");
		}
		LOG.info("ExternalTransactionId : {}",headers.getExternalTransactionId());
		return subscriptionList;
	}
	
	
	public List<Subscription> validateSubscriptionIdExists(
			ManageRenewalRequestDto renewalPaymentMethodRequestDto, Headers headers,
			ResultResponse resultResponse) throws SubscriptionManagementException {
		List<Subscription> subscriptionList = new ArrayList<>();
		if(null != renewalPaymentMethodRequestDto.getSubscriptionId() && !renewalPaymentMethodRequestDto.getSubscriptionId().isEmpty()) {
			
			Optional<Subscription> subscription = subscriptionDomain.findById(renewalPaymentMethodRequestDto.getSubscriptionId());
			if(subscription.isPresent()) {
				subscriptionList.add(subscription.get());
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
				SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
			}
			
		}
		return subscriptionList;
	}
	
//	public List<LinkedOfferDto>configureLinkedOffers(SubscriptionCatalogRequestDto subscriptionCatalogRequestDto, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {		
//		List<LinkedOfferDto> linkedOffers = new ArrayList<>();
//		if(null != subscriptionCatalogRequestDto.getLinkedOfferId()) {
//			LOG.info("Linked Offers : {}",subscriptionCatalogRequestDto.getLinkedOfferId());
//			List<LinkedOfferDto> availableOffers = subscriptionService.listOffersResponse(headers.getUserName(), headers.getToken(), resultResponse);
//			linkedOffers = SubscriptionCatalogValidator.validateAndSetLinkedOffer(availableOffers, subscriptionCatalogRequestDto.getLinkedOfferId(), resultResponse);
//		}
//		return linkedOffers;
//	}
	
	public ValidDates configureValidDates(SubscriptionCatalogRequestDto subscriptionCatalogRequestDto, ResultResponse resultResponse) throws SubscriptionManagementException{
		ValidDates validDates = new ValidDates();
		
		String startDate = null != subscriptionCatalogRequestDto.getStartDate() ? subscriptionCatalogRequestDto.getStartDate() : 
			new SimpleDateFormat(SubscriptionManagementConstants.SIMPLE_DATE_FORMAT.get()).format(new Date());
		
		validDates.setValidStartDate(SubscriptionCatalogValidator.validateDate(startDate, resultResponse));				
		validDates.setValidEndDate(SubscriptionCatalogValidator.validateDate(subscriptionCatalogRequestDto.getEndDate(), resultResponse));
		if(validDates.getValidStartDate().after(validDates.getValidEndDate())) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_START_DATE.getIntId(),
					SubscriptionManagementCode.INVALID_START_DATE.getMsg());
			return null;
		}
		
		return validDates;
	}
	
	public boolean validateChannelForPromoGift(CachedValues cachedValues, PurchaseResultResponse resultResponse) {
		if(!cachedValues.isChannelEligible() && !cachedValues.isPromoGift()) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INELIGIBLE_CHANNEL.getIntId(),
					SubscriptionManagementCode.INELIGIBLE_CHANNEL.getMsg());
			return false;
		}
		return true;
	}

	
	
}

