package com.loyalty.marketplace.subscription.inbound.controller;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.drools.core.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.helper.MarketplaceRepositoryHelper;
import com.loyalty.marketplace.interest.domain.model.InterestDomain;
import com.loyalty.marketplace.interest.outbound.dto.CategoriesInterestDto;
import com.loyalty.marketplace.interest.outbound.entity.CustomerInterestEntity;
import com.loyalty.marketplace.interest.outbound.entity.InterestEntity;
import com.loyalty.marketplace.interest.repository.CategotyRepository;
import com.loyalty.marketplace.interest.repository.CustomerInterest;
import com.loyalty.marketplace.interest.repository.InterestRepository;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.service.PointsBankService;
import com.loyalty.marketplace.offers.points.bank.outbound.dto.LifeTimeSavingsDetails;
import com.loyalty.marketplace.offers.promocode.constants.codes.PromoCodeErrorCodes;
import com.loyalty.marketplace.offers.promocode.domain.PromoCodeDomain;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoCode;
import com.loyalty.marketplace.offers.promocode.outbound.database.repository.PromoCodeRepository;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoDetails;
import com.loyalty.marketplace.offers.promocode.outbound.dto.ValidPromoCodeDetails;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApply;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApplyAndValidate;
import com.loyalty.marketplace.payment.outbound.database.entity.EPGTransaction;
import com.loyalty.marketplace.payment.outbound.database.repository.EPGTransactionRepository;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.constants.SubscriptionRequestMappingConstants;
import com.loyalty.marketplace.subscription.domain.Benefits;
import com.loyalty.marketplace.subscription.domain.LinkedOffers;
import com.loyalty.marketplace.subscription.domain.MerchantsList;
import com.loyalty.marketplace.subscription.domain.PaymentMethods;
import com.loyalty.marketplace.subscription.domain.SubscriptionCatalogDomain;
import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
import com.loyalty.marketplace.subscription.helper.dto.RenewalValues;
import com.loyalty.marketplace.subscription.inbound.dto.CachedBenefits;
import com.loyalty.marketplace.subscription.inbound.dto.CachedValues;
import com.loyalty.marketplace.subscription.inbound.dto.EligibleCategories;
import com.loyalty.marketplace.subscription.inbound.dto.GiftApiRequest;
import com.loyalty.marketplace.subscription.inbound.dto.ManageRenewalRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.ManageSubscriptionRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.NotificationValues;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionBenefits;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionBenefitsRequest;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogInputDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionInfoRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionWithInterestRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.ValidDates;
import com.loyalty.marketplace.subscription.outbound.database.entity.CCDetails;
import com.loyalty.marketplace.subscription.outbound.database.entity.Cuisines;
import com.loyalty.marketplace.subscription.outbound.database.entity.PaymentMethodDetails;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPayment;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPaymentMethod;
import com.loyalty.marketplace.subscription.outbound.database.repository.CuisinesRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionPaymentRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepositoryHelper;
import com.loyalty.marketplace.subscription.outbound.dto.BenefitsResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.Cost;
import com.loyalty.marketplace.subscription.outbound.dto.CuisinesResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.CuisinesResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.GiftApiResponse;
import com.loyalty.marketplace.subscription.outbound.dto.GiftDetails;
import com.loyalty.marketplace.subscription.outbound.dto.PointsValue;
import com.loyalty.marketplace.subscription.outbound.dto.SubscribedSegmentResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionInfoResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionInfoResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionPaymentResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionRenewalResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionWithInterestResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionWithInterestResultResponse;
import com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto.PhoneyTunesRequest;
import com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto.PhoneyTunesResponse;
import com.loyalty.marketplace.subscription.service.SubscriptionService;
import com.loyalty.marketplace.subscription.utils.SubscriptionCatalogValidator;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.subscription.utils.SubscriptionPurchaseValidator;
import com.loyalty.marketplace.subscription.utils.SubscriptionUtils;
import com.loyalty.marketplace.subscription.utils.SubscriptionValidator;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftRequestDto;
import com.mongodb.client.result.UpdateResult;
import com.opencsv.CSVWriter;

@RefreshScope
@Component
public class SubscriptionManagementControllerHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionManagementControllerHelper.class);
	
	@Value("#{'${non.member.bogo.channelIds}'.split(',')}")
	private List<String> nonMemberChannelIdList;
	
	@Value("#{'${extend.partner.bogo.channelIds}'.split(',')}")
	private List<String> extendedPartnerChannelIdList;
	
	@Value("#{'${bogo.bulk.channelIds}'.split(',')}")
	private List<String> bogoBulkChannelIds;
	
	@Value("${non.existing.member}")
    	private String nonExisting;
	
	@Value("#{'${non.dcb.customerType}'.split(',')}")
    	private List<String> nonDCBCustomerType;
	
	@Value("#{${cardtype.catalog.mapping}}")
	private Map<String, String> cardCatalogMap;
	
	//@Value("${subscription.park.limit.days}")
	private int subscriptionParkLimitDays = 7;
    
	@Value("#{'${renewal.report.email.list}'.split(',')}")
	private List<String> renewalReportEmailList;
	
	@Value("${renewal.report.upload.location}")
	private String renewalReportSavingLocation;
    
        @Value("${renewal.report.email.template.id}")
	private String renewalReportEmailTemplateId;
    
        @Value("${renewal.report.email.notification.id}")
	private String renewalReportEmailNotificationId;
    
        @Autowired
	EventHandler eventHandler;
	
    	@Autowired
	PaymentService paymentService;
	
	@Autowired
	SubscriptionCatalogDomain subscriptionCatalogDomain;
	
	@Autowired
	SubscriptionDomain subscriptionDomain;
	
	@Autowired
	SubscriptionCatalogRepository subscriptionCatalogRepository;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Autowired
	PurchaseDomain purchaseDomain;
	
	@Autowired
	SubscriptionValidator subscriptionValidator;
	
	@Autowired
	SubscriptionCatalogValidator subscriptionCatalogValidator;
	
	@Autowired
	SubscriptionPurchaseValidator subscriptionPurchaseValidator;
	
	@Autowired
	SubscriptionUtils subscriptionUtils;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	PurchaseRepository purchaseRepository;
	
	@Autowired
	InterestRepository interestRepository;
	
	@Autowired
	CustomerInterest customerInterestRepository;
	
	@Autowired
	CategotyRepository categotyRepository;
	
	@Autowired
	InterestDomain interestDomain;
	
	@Autowired
	OffersHelper offersHelper;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	SubscriptionRepositoryHelper subscriptionRepositoryHelper;
	
	@Autowired
	SubscriptionPaymentRepository subscriptionPaymentRepository;
	
	@Autowired
	MarketplaceRepositoryHelper marketplaceRepositoryHelper;
	
	@Autowired
	EPGTransactionRepository epgTransactionRepository;
	
	@Autowired
	PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	CuisinesRepository cuisinesRepository;
	
	@Autowired
	@Qualifier("customRestTemplateBean")
	private RestTemplate restTemplate;
	
	@Autowired
	PromoCodeRepository promoCodeRepository; 
	
	@Autowired
	PromoCodeDomain promoCodeDomain;
	
	@Autowired
    PointsBankService pointsBankService;
	
	public void validateAndSaveSubscriptionCatalog(SubscriptionCatalogRequestDto subscriptionCatalogRequestDto, Optional<SubscriptionCatalog> subscriptionCatalog, 
			String action, String changeType, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException, IOException {
		LOG.info("validateAndSaveSubscriptionCatalog : {}",action);
		List<PaymentMethods> paymentMethods = subscriptionUtils.validateRetrievePaymentMethods(subscriptionCatalogRequestDto.getPaymentMethods(), resultResponse);
		List<LinkedOffers> linkedOffers = subscriptionUtils.validateRetrieveLinkedOffers(subscriptionCatalogRequestDto.getLinkedOffers(), headers, resultResponse);
		
		List<MerchantsList> merchantsList = new ArrayList<>();
		List<CuisinesResponseDto> cuisinesList = new ArrayList<>();
		if(!ObjectUtils.isEmpty(subscriptionCatalogRequestDto.getMerchantsList())) {
			merchantsList = subscriptionUtils.validateRetrieveMerchants(subscriptionCatalogRequestDto.getMerchantsList(),resultResponse);
		}
		if(!ObjectUtils.isEmpty(subscriptionCatalogRequestDto.getCuisinesList())) {
			cuisinesList = subscriptionUtils.validateRetrieveCuisines(subscriptionCatalogRequestDto.getCuisinesList(),resultResponse);
		}
		ValidDates validDates = subscriptionValidator.configureValidDates(subscriptionCatalogRequestDto, resultResponse);
		if(null != validDates) {										
			List<String> availableCustomerType = subscriptionService.getAvailableCustomerType(headers.getToken(), resultResponse);
			if(!availableCustomerType.containsAll(subscriptionCatalogRequestDto.getCustomerSegments())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_CUSTOMER_SEGMENT.getIntId(),
						SubscriptionManagementCode.INVALID_CUSTOMER_SEGMENT.getMsg());
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
			} else {
				List<Benefits> benefits = subscriptionCatalogValidator.configureCatalogBenefits(subscriptionCatalogRequestDto, headers, resultResponse);
				LOG.info("paymentMethods : {}",paymentMethods);
				LOG.info("linkedOffers : {}",linkedOffers);
				LOG.info("availableCustomerType : {}",availableCustomerType);
				LOG.info("catalogBenefits : {}",benefits);
				
				if(action.equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_INSERT.get())) {
					saveSubscriptionCatalog(subscriptionCatalog, subscriptionCatalogRequestDto, validDates, paymentMethods, linkedOffers, benefits, changeType, merchantsList, cuisinesList, headers, resultResponse);
				} else if(action.equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_UPDATE.get())) {
					updateSubscriptionCatalog(subscriptionCatalog, subscriptionCatalogRequestDto, validDates, paymentMethods, linkedOffers, benefits, changeType, merchantsList, cuisinesList, headers, resultResponse);
				}				
			}		
		} else {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
		}
		
	}
	
	public void saveSubscriptionCatalog(Optional<SubscriptionCatalog> subscriptionCatalog, SubscriptionCatalogRequestDto subscriptionCatalogRequestDto, ValidDates validDates, 
			List<PaymentMethods> paymentMethods, List<LinkedOffers> linkedOffers, List<Benefits> benefits, String changeType, List<MerchantsList> merchantsList, List<CuisinesResponseDto> cuisinesList, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		if (subscriptionCatalog.isPresent()) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_EXISTS.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_EXISTS.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
		} else {
			LOG.info("Saving Subscription Catalog : {}",subscriptionCatalogRequestDto);
			subscriptionCatalogDomain.saveSubscriptionCatalog(new SubscriptionCatalogDomain.SubscriptionCatalogBuilder(subscriptionCatalogRequestDto.getId(),
					subscriptionCatalogRequestDto.getSubscriptionTitle(), subscriptionCatalogRequestDto.getSubscriptionDescription(), subscriptionCatalogRequestDto.getTermsAndConditions(),
					subscriptionCatalogRequestDto.getSubscriptionTitleHeader(), subscriptionCatalogRequestDto.getSubscriptionSubTitle(), subscriptionCatalogRequestDto.getSubscribedOfferTitle(),
					subscriptionCatalogRequestDto.getSubscriptionMarketTitleLine(),subscriptionCatalogRequestDto.getSubscriptionMarketDescLine(), subscriptionCatalogRequestDto.getImageUrl(), merchantsList, cuisinesList, subscriptionCatalogRequestDto.getCost(), 
					subscriptionCatalogRequestDto.getPointsValue(), subscriptionCatalogRequestDto.getFreeDuration(), subscriptionCatalogRequestDto.getValidityPeriod(), 
					validDates.getValidStartDate(), validDates.getValidEndDate(),subscriptionCatalogRequestDto.getStatus(), subscriptionCatalogRequestDto.getChargeabilityType(), 
					paymentMethods, subscriptionCatalogRequestDto.getCustomerSegments(), subscriptionCatalogRequestDto.getEligibleChannels(), linkedOffers, null, benefits,
					subscriptionCatalogRequestDto.getPackageType(), subscriptionCatalogRequestDto.getSubscriptionSegment(),	new Date(), headers.getUserName(), new Date(), headers.getUserName(), headers.getProgram().toLowerCase()).build(),changeType,headers);
			
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATED.getId(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATED.getMsg());
			resultResponse.setSuccessAPIResponse();
		}	
	}
	
	public void updateSubscriptionCatalog(Optional<SubscriptionCatalog> subscriptionCatalog, SubscriptionCatalogRequestDto subscriptionCatalogRequestDto, ValidDates validDates, 
			List<PaymentMethods> paymentMethods, List<LinkedOffers> linkedOffers, List<Benefits> benefits, String changeType, List<MerchantsList> merchantsList, List<CuisinesResponseDto> cuisinesList, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("updateSubscriptionCatalog : subscriptionCatalog : {}", subscriptionCatalog);
		if(subscriptionCatalog.isPresent()) {
			LOG.info("Updating Subscription Catalog : {}",subscriptionCatalogRequestDto);
			subscriptionCatalogDomain.updateSubscriptionCatalog(new SubscriptionCatalogDomain.SubscriptionCatalogBuilder(subscriptionCatalog.get().getId(),
					subscriptionCatalogRequestDto.getSubscriptionTitle(), subscriptionCatalogRequestDto.getSubscriptionDescription(), subscriptionCatalogRequestDto.getTermsAndConditions(),
					subscriptionCatalogRequestDto.getSubscriptionTitleHeader(), subscriptionCatalogRequestDto.getSubscriptionSubTitle(), subscriptionCatalogRequestDto.getSubscribedOfferTitle(),
					subscriptionCatalogRequestDto.getSubscriptionMarketTitleLine(),subscriptionCatalogRequestDto.getSubscriptionMarketDescLine(), subscriptionCatalogRequestDto.getImageUrl(), merchantsList, cuisinesList, subscriptionCatalogRequestDto.getCost(), 
					subscriptionCatalogRequestDto.getPointsValue(), subscriptionCatalogRequestDto.getFreeDuration(), subscriptionCatalogRequestDto.getValidityPeriod(), 
					validDates.getValidStartDate(), validDates.getValidEndDate(),subscriptionCatalogRequestDto.getStatus(), subscriptionCatalogRequestDto.getChargeabilityType(), 
					paymentMethods, subscriptionCatalogRequestDto.getCustomerSegments(), subscriptionCatalogRequestDto.getEligibleChannels(), linkedOffers, null, benefits, subscriptionCatalogRequestDto.getPackageType(),
					subscriptionCatalogRequestDto.getSubscriptionSegment(), subscriptionCatalog.get().getCreatedDate(), subscriptionCatalog.get().getCreatedUser(), new Date(), headers.getUserName(), headers.getProgram().toLowerCase()).build(), subscriptionCatalog.get(), changeType, headers);
			
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATED.getId(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATED.getMsg());
			resultResponse.setSuccessAPIResponse();
		}	
	}
	
	public void validateAndUpdateSubscriptionCatalog (SubscriptionCatalogRequestDto subscriptionCatalogRequestDto, String subscriptionCatalogId, 
			String action, String changeType, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException, IOException {
		LOG.info("validateAndUpdateSubscriptionCatalog");
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		if (!subscriptionCatalog.isPresent()) {
			resultResponse.addErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getMsg());
		} else {
			if(action.equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_UPDATE.get())) {
				validateAndSaveSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalog, SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_UPDATE.get(), changeType, headers, resultResponse);
			} else if(action.equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_DELETE.get())) {
				subscriptionCatalogDomain.deleteSubscriptionCatalog(subscriptionCatalog, changeType, headers);			
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_DELETED.getId(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_DELETED.getMsg());
				resultResponse.setSuccessAPIResponse();				
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_ACTION.getIntId(), SubscriptionManagementCode.INVALID_ACTION.getMsg());
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getMsg());
			}
		}		
	}
	
	@Transactional(transactionManager = "mongoTransactionManager", rollbackForClassName = { "java.lang.Exception" },propagation=Propagation.REQUIRED)
	public void validateAndSaveSubscriptionPurchase(PurchaseRequestDto purchaseRequestDto, Headers headers, PurchaseResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException, ParseException {
		
		LOG.info("validateAndSaveSubscriptionPurchase");		
		CachedValues cachedValues = new CachedValues(true, false, false, true);
		SubscriptionCatalog subscriptionCatalog = subscriptionValidator.validateSubscriptionCatalog(purchaseRequestDto, cachedValues, headers, resultResponse);
		if(null != subscriptionCatalog) {
			if(subscriptionValidator.validateSubscriptionAndApplyPromoCode(purchaseRequestDto, cachedValues, headers, subscriptionCatalog, resultResponse)
					&& subscriptionValidator.validateChannelForPromoGift(cachedValues,resultResponse)) {
				ValidDates validDates = subscriptionValidator.calculateValidDates(purchaseRequestDto, cachedValues, subscriptionCatalog, headers, resultResponse);	
				GetMemberResponseDto getMemberResponseDto = subscriptionValidator.validateAccountForSubscription(purchaseRequestDto, headers, subscriptionCatalog, resultResponse);
				
				LOG.info("validateAndSaveSubscriptionPurchase :: cachedValues.getSubscriptionAction : {}",cachedValues.getSubscriptionAction());
				LOG.info("validateAndSaveSubscriptionPurchase :: getMemberResponseDto : {}",getMemberResponseDto);				
				LOG.info("validateAndSaveSubscriptionPurchase :: cachedValues.isPaymentRequired : {}",cachedValues.isPaymentRequired());
				LOG.info("validateAndSaveSubscriptionPurchase :: nonMemberChannelIdList : {}",nonMemberChannelIdList);
				
				if(null != getMemberResponseDto || nonMemberChannelIdList.contains(headers.getChannelId())) {
						if(null == cachedValues.getSubscriptionAction()
								|| (null != cachedValues.getSubscriptionAction() 
								&& subscriptionPurchaseValidator.cancelExtendSubscriptionForChannels(cachedValues, headers))) {
						Subscription subscription = subscriptionPurchaseValidator.validatePurchaseSubscription(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, validDates, cachedValues, headers, resultResponse);						
						savePurchaseRecordLifeTimeSavings(purchaseRequestDto, subscriptionCatalog, cachedValues, headers, resultResponse);
						LOG.info("cachedValues.isIsfreeOffer : {}",cachedValues.isIsfreeOffer());
						if(!ObjectUtils.isEmpty(subscription) && subscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {
							subscriptionPurchaseValidator.addFreeOffer(purchaseRequestDto,subscriptionCatalog,headers);
						}
					} else {
						resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
								SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
					}					
				} else {
					resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
							SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
				}				
			} else {
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			}
		} else {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
		}	
	}
	
	public void savePurchaseRecordLifeTimeSavings(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, CachedValues cachedValues, Headers headers, ResultResponse resultResponse) {
		if(cachedValues.isNonExistingMemberFree()) {
			LOG.info("validateAndSaveSubscriptionPurchase :: NonExistingMemberFree");
			Responses.removeAllErrors(resultResponse);
			//Add to Purchase History Table
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATED.getId(), SubscriptionManagementCode.SUBSCRIPTION_CREATED.getMsg());
			resultResponse.setSuccessAPIResponse();
		}
		else if(Checks.checkNoErrors(resultResponse)) {
			LOG.info("validateAndSaveSubscriptionPurchase :: ExistingMember");
			purchaseRequestDto.setCouponQuantity(1);
		}
	}
	
	
	public String validateWelcomeGiftSubscription(PurchaseRequestDto purchaseRequestDto, WelcomeGiftRequestDto welcomeGiftRequestDto, 
			Headers headers, PurchaseResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException {
		LOG.info("Inside validateWelcomeGiftSubscription");
		String subscriptionCatalogId = null;
		if(!StringUtils.isEmpty(welcomeGiftRequestDto.getSubscriptionCatalogId())) {
			LOG.info("SubscriptionCatalogId : {}", welcomeGiftRequestDto.getSubscriptionCatalogId());
			subscriptionCatalogId = welcomeGiftRequestDto.getSubscriptionCatalogId();
		} else if((!StringUtils.isEmpty(welcomeGiftRequestDto.getGiftId()) && welcomeGiftRequestDto.getGiftType().equalsIgnoreCase("Subscription"))) {
			LOG.info("GiftType : {}, SubscriptionCatalogId : {}", welcomeGiftRequestDto.getGiftType(), welcomeGiftRequestDto.getGiftId());
			subscriptionCatalogId = welcomeGiftRequestDto.getGiftId();
		} else if(cardCatalogMap.containsKey(welcomeGiftRequestDto.getCardType())) {
			LOG.info("CardType : {}, SubscriptionCatalogId : {}", welcomeGiftRequestDto.getCardType(), cardCatalogMap.get(welcomeGiftRequestDto.getCardType()));
			subscriptionCatalogId = cardCatalogMap.get(welcomeGiftRequestDto.getCardType());
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_CONFIGURED.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_CONFIGURED.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			return null;
		}
			
		SubscriptionCatalog subscriptionCatalog = subscriptionCatalogDomain.findSubscriptionCatalog(subscriptionCatalogId);
		if(null != subscriptionCatalog) {
			GetMemberResponseDto getMemberResponseDto = subscriptionValidator.validateAccountForSubscription(purchaseRequestDto, headers, subscriptionCatalog, resultResponse);
			LOG.info("memberResponseDto : {}", getMemberResponseDto);
			LOG.info("Headers : {}", headers);
			if(null != getMemberResponseDto) {
				if(extendedPartnerChannelIdList.contains(headers.getChannelId().toUpperCase())
						|| (welcomeGiftRequestDto.isBogoBulk() && bogoBulkChannelIds.contains(welcomeGiftRequestDto.getChannelId().toUpperCase()))) {
					return validateAndSaveExtendedPartnerWelcomeGiftSubscription(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, headers, resultResponse);
				} else {
					return validateAndSendWelcomeGiftSubscription(purchaseRequestDto, subscriptionCatalog, resultResponse);
				}
			}
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getIntId(),
					SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
		}
		return null;
	}

		
	public String validateAndSaveExtendedPartnerWelcomeGiftSubscription(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, 
			GetMemberResponseDto getMemberResponseDto, Headers headers, PurchaseResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException {
		LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription");
		CachedValues cachedValues = new CachedValues(false, false, false, true);
		//Optional<Subscription> existingSubscription = subscriptionDomain.findByAccountNumberAndStatus(purchaseRequestDto.getAccountNumber(), SubscriptionManagementConstants.SUBSCRIBED_STATUS.get());
		Optional<Subscription> existingSubscription = subscriptionDomain.findSubscriptionForAccountWithStatus(Arrays.asList(purchaseRequestDto.getAccountNumber()),subscriptionCatalog.getSubscriptionSegment());
		LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription :: existingSubscription : {}",existingSubscription);
		if(existingSubscription.isPresent()) {
			LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription :: existingSubscription present");
			SubscriptionCatalog subscribedSubscriptionCatalog = subscriptionCatalogDomain.findSubscriptionCatalog(existingSubscription.get().getSubscriptionCatalogId());
			if(subscribedSubscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {			
				LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription :: Cancel Auto and Save One-Time Subscription for MembershipCode : {}", purchaseRequestDto.getMembershipCode());								
				validateAndCancelSubscription(null, Arrays.asList(existingSubscription.get()), headers, resultResponse);				
				ValidDates validDates = subscriptionValidator.calculateValidDates(purchaseRequestDto, cachedValues, subscriptionCatalog, headers, resultResponse);
				LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription :: Saving One-Time Subscription of MembershipCode : {}", purchaseRequestDto.getMembershipCode());						
				Subscription savedSubscription = resultResponse.getResult().getResponse().equalsIgnoreCase(SubscriptionManagementCode.SUBSCRIPTION_CANCELLED.getId()) 
						? subscriptionPurchaseValidator.validatePurchaseSubscription(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, validDates, cachedValues, headers, resultResponse) : null;
				return null != savedSubscription ? savedSubscription.getId(): null;
				
			} else if(subscribedSubscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {			
				
				LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription :: Cancel and Extend One-Time Subscription for MembershipCode : {}", purchaseRequestDto.getMembershipCode());								
				cachedValues.setSubscribedSubscription(existingSubscription.get());
				cachedValues.setExtendValidityPeriod(true);
				subscriptionDomain.cancelSubscription(null, existingSubscription.get(),cachedValues.getSubscriptionAction(), headers);
				ValidDates validDates = subscriptionValidator.calculateValidDates(purchaseRequestDto, cachedValues, subscriptionCatalog, headers, resultResponse);
				Subscription savedSubscription = subscriptionPurchaseValidator.validatePurchaseSubscription(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, validDates, cachedValues, headers, resultResponse);
				
				return null != savedSubscription ? savedSubscription.getId(): null;
				
			}  else if(subscribedSubscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getIntId(),
						SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getMsg());
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			}
		} else {
			LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription :: existingSubscription not present");
			ValidDates validDates = subscriptionValidator.calculateValidDates(purchaseRequestDto, cachedValues, subscriptionCatalog, headers, resultResponse);
			LOG.info("validateAndSaveExtendedPartnerWelcomeGiftSubscription :: Saving One-Time Subscription of MembershipCode : {}", purchaseRequestDto.getMembershipCode());						
			Subscription savedSubscription = subscriptionPurchaseValidator.validatePurchaseSubscription(purchaseRequestDto, subscriptionCatalog, getMemberResponseDto, validDates, cachedValues, headers, resultResponse);
			return null != savedSubscription ? savedSubscription.getId(): null;
		}	
		return null;		
	}
	
	public String validateAndSendWelcomeGiftSubscription(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, PurchaseResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("validateAndSendWelcomeGiftSubscription");					
		List<Subscription> subscription = subscriptionDomain.findByAccountNumber(purchaseRequestDto.getAccountNumber());
		if(subscription.isEmpty()) {
			LOG.info("Subscription Catalog Id : {}",subscriptionCatalog.getId());
			resultResponse.setSuccessAPIResponse();
			return subscriptionCatalog.getId();				
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_WELCOME_GIFT.getIntId(),
					SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_WELCOME_GIFT.getMsg());
		}
		return null;
	}
	
	
	public void validateAndfetchSubscription (String accountNumber, Headers headers, SubscriptionResultResponse subscriptionResultResponse) throws SubscriptionManagementException, MarketplaceException {
		LOG.info("validateAndfetchSubscription");
		GetMemberResponse getMemberResponse = fetchServiceValues.getMemberDetails(accountNumber, subscriptionResultResponse, headers);
		LOG.info("getMemberResponse : {}", getMemberResponse);
		if (null != getMemberResponse) {
			List<Subscription> subscribedSubscriptionList = populateSubscribedSubscription(accountNumber, subscriptionResultResponse);
			
			populateSubscriptionCatalogForSubscription(subscribedSubscriptionList, getMemberResponse, headers, subscriptionResultResponse);	
			//Added to populate lifetime savings details in response
			populateSavingsDetails(accountNumber, headers, subscriptionResultResponse);
			if(getMemberResponse.isPrimaryAccount()) {
				populatePassiveForElife(accountNumber, getMemberResponse, subscriptionResultResponse);
			}	
		}
	}
	
	private void populateSavingsDetails(String accountNumber, Headers headers, SubscriptionResultResponse subscriptionResultResponse) throws MarketplaceException {
		
		LifeTimeSavingsDetails lifeTimeSavingsDetails = pointsBankService.getLifetimeSavingsDetails(accountNumber, headers, subscriptionResultResponse);
		
		if(!ObjectUtils.isEmpty(lifeTimeSavingsDetails)
		&& !ObjectUtils.isEmpty(lifeTimeSavingsDetails.getOverallSavings())) {
			
			subscriptionResultResponse.setBogoSavings(lifeTimeSavingsDetails.getOverallSavings().getBogoSavings());
			subscriptionResultResponse.setFoodSavings(lifeTimeSavingsDetails.getOverallSavings().getFoodSavings());
		}
		
		Responses.removeAllErrors(subscriptionResultResponse);
	
	}
	
	public List<Subscription> populateSubscribedSubscription(String accountNumber, SubscriptionResultResponse subscriptionResultResponse) throws SubscriptionManagementException {
		List<Subscription> subscribedSubscriptionList = subscriptionDomain.findByAccountNumberAndStatusIn(accountNumber,Arrays.asList(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(),SubscriptionManagementConstants.PARKED_STATUS.get()));
		LOG.info("populateSubscribedSubscription :: subscribedSubscriptionList : {}", subscribedSubscriptionList);
		if(!subscribedSubscriptionList.isEmpty()) {
			List<SubscriptionResponseDto> subscriptionResponseList = new ArrayList<>();
			List<SubscriptionPayment> subscriptionPaymentList = subscriptionPaymentRepository.findBySubscriptionIdInAndPaymentMethodDetails_Status(
					subscribedSubscriptionList.stream().map(Subscription :: getId).collect(Collectors.toList()),SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
			
			for(Subscription subscribedSubscription : subscribedSubscriptionList) {
				SubscriptionResponseDto subscriptionResponseDto = modelMapper.map(subscribedSubscription, SubscriptionResponseDto.class);
				if(null != subscribedSubscriptionList && !subscribedSubscriptionList.isEmpty()) {
					List<SubscriptionPayment> subscriptionPayment = subscriptionPaymentList.stream().filter(s -> s.getSubscriptionId().equalsIgnoreCase(subscribedSubscription.getId())).collect(Collectors.toList());	
					LOG.info("Record from SubscriptionPayment collection: {}", subscriptionPayment);
					if(null != subscriptionPayment && !subscriptionPayment.isEmpty()) {
						Optional<PaymentMethodDetails> paymentMethodDetails = subscriptionPayment.stream().flatMap(a -> a.getPaymentMethodDetails().stream()
                                .filter(b ->b.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())))
                                .findFirst();
						LOG.info("paymentMethodDetails in populateSubscribedSubscription:{}",paymentMethodDetails);
						SubscriptionPaymentResponseDto subscriptionPaymentResponseDto = new SubscriptionPaymentResponseDto();				
						subscriptionPaymentResponseDto.setPaymentMethod(paymentMethodDetails.get().getPaymentMethod());
						subscriptionPaymentResponseDto.setPaymentMethodId(paymentMethodDetails.get().getPaymentMethodId());
						if(paymentMethodDetails.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get()) 
								&& null != paymentMethodDetails.get().getCcDetails()) {
							if(null != paymentMethodDetails.get().getCcDetails().getCardNumber()) 
								subscriptionPaymentResponseDto.setCardNumber(paymentMethodDetails.get().getCcDetails().getCardNumber());
							if(null != paymentMethodDetails.get().getCcDetails().getSubType()) 
								subscriptionPaymentResponseDto.setSubType(paymentMethodDetails.get().getCcDetails().getSubType());
						}
						subscriptionResponseDto.setPaymentDetails(subscriptionPaymentResponseDto);
						if(extendedPartnerChannelIdList.contains(subscribedSubscription.getSubscriptionChannel())) {
							subscriptionResponseDto.setCobrandFlag(true);
						}
					}
				}
				
				subscriptionResponseList.add(subscriptionResponseDto);
				

			}
			LOG.info("populateSubscribedSubscription :: subscriptionResponseList : {}", subscriptionResponseList);
			subscriptionResultResponse.setSubscription(subscriptionResponseList);
			subscriptionResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getMsg());
		} else {
			subscriptionResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
		}
		return subscribedSubscriptionList;
	}
	
	public void populateSubscriptionCatalogForSubscription(List<Subscription> subscribedSubscriptionList, GetMemberResponse getMemberResponse, Headers headers, 
			SubscriptionResultResponse subscriptionResultResponse) throws SubscriptionManagementException {
		List<SubscriptionCatalog> catalog = subscriptionCatalogRepository.findAll();
		LOG.info("catalog : {}", catalog);
		if(!catalog.isEmpty()) {
			List<SubscriptionCatalogResponseDto> subscribedSubscriptionCatalogResponseList = new ArrayList<>();
			List<SubscriptionCatalogResponseDto> eligibleSubscriptionCatalogResponseList = new ArrayList<>();
			for(SubscriptionCatalog subscriptionCatalogResponse : catalog) {
				SubscriptionCatalogResponseDto subscriptionCatalogResponseDto = modelMapper.map(subscriptionCatalogResponse, SubscriptionCatalogResponseDto.class);
				LOG.info("subscriptionCatalogResponseDto : {}", subscriptionCatalogResponseDto);				
				if(!subscribedSubscriptionList.isEmpty() && subscribedSubscriptionList.stream().anyMatch(p -> p.getSubscriptionCatalogId().equalsIgnoreCase(subscriptionCatalogResponse.getId()))) {
					populateSubscribedSubscriptionCatalog(subscriptionCatalogResponse, getMemberResponse, headers, subscribedSubscriptionCatalogResponseList, subscriptionCatalogResponseDto);
				} else {
					populateEligibleSubscriptionCatalog(subscriptionCatalogResponse, getMemberResponse, headers, eligibleSubscriptionCatalogResponseList, subscriptionCatalogResponseDto,false);
				}
			}
			if(!eligibleSubscriptionCatalogResponseList.isEmpty()) {
				LOG.info("eligibleSubscriptionCatalogResponseList : {}", eligibleSubscriptionCatalogResponseList);
				if(eligibleSubscriptionCatalogResponseList.stream().anyMatch(e -> e.getSubscriptionSegment().equalsIgnoreCase("food"))) {
					eligibleSubscriptionCatalogResponseList=Stream.concat(
							eligibleSubscriptionCatalogResponseList.stream().filter(e -> e.getSubscriptionSegment().equals("food")), 
							eligibleSubscriptionCatalogResponseList.stream().filter(e -> !e.getSubscriptionSegment().equals("food"))).collect(Collectors.toList());
				}
				
				if(headers.getChannelId().equalsIgnoreCase("SAPP")) {
					if(subscribedSubscriptionCatalogResponseList.stream().anyMatch(e -> e.getSubscriptionSegment().equalsIgnoreCase("lifestyle"))) {
						eligibleSubscriptionCatalogResponseList = eligibleSubscriptionCatalogResponseList.stream().filter(m -> !m.getSubscriptionSegment().equals("lifestyle")).collect(Collectors.toList());
					}
					 	
					if(subscribedSubscriptionCatalogResponseList.stream().anyMatch(e -> e.getSubscriptionSegment().equalsIgnoreCase("food"))) {
						eligibleSubscriptionCatalogResponseList = eligibleSubscriptionCatalogResponseList.stream().filter(m -> !m.getSubscriptionSegment().equals("food")).collect(Collectors.toList());
					}
				}
				
				subscriptionResultResponse.setEligibleSubscriptionCatalog(eligibleSubscriptionCatalogResponseList);					
			}
			if(!subscribedSubscriptionCatalogResponseList.isEmpty()) {
				LOG.info("subscribedSubscriptionCatalogResponseList : {}", subscribedSubscriptionCatalogResponseList);
				subscriptionResultResponse.setSubscribedSubscriptionCatalog(subscribedSubscriptionCatalogResponseList);
			}
		} else {
			subscriptionResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
		}	
	}
	
	public void populateSubscribedSubscriptionCatalog(SubscriptionCatalog subscriptionCatalogResponse, GetMemberResponse getMemberResponse, Headers headers,
			List<SubscriptionCatalogResponseDto> subscribedSubscriptionCatalogResponseList, SubscriptionCatalogResponseDto subscriptionCatalogResponseDto) throws SubscriptionManagementException {
		if(subscriptionUtils.hasAvailedFreeDuration(getMemberResponse.getAccountNumber())) {
			LOG.info("populateEligibleSubscriptionCatalog : hasAvailedFreeDuration");
			subscriptionCatalogResponseDto.setFreeDuration(0);
		}
		List<PaymentMethodDto> eligiblePaymentMethods = populateEligiblePaymentMethods(subscriptionCatalogResponse, getMemberResponse, headers);
		if(null != eligiblePaymentMethods) {
			subscriptionCatalogResponseDto.setPaymentMethods(eligiblePaymentMethods);
		}
		LOG.info("subscriptionCatalogResponse.getId() : {}", subscriptionCatalogResponse.getId());
		subscribedSubscriptionCatalogResponseList.add(subscriptionCatalogResponseDto);
	
		
	}
	
	public void populateEligibleSubscriptionCatalog(SubscriptionCatalog subscriptionCatalogResponse, GetMemberResponse getMemberResponse, Headers headers,
			List<SubscriptionCatalogResponseDto> eligibleSubscriptionCatalogResponseList, SubscriptionCatalogResponseDto subscriptionCatalogResponseDto, Boolean isPromoGift) throws SubscriptionManagementException {
		
		if(subscriptionCatalogResponse.getStatus() == null || subscriptionCatalogResponse.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())
				&& (isPromoGift || SubscriptionCatalogValidator.checkEligibleCatalogByChannel(headers.getChannelId(), subscriptionCatalogResponse.getEligibleChannels()))
				&& CollectionUtils.containsAny(getMemberResponse.getCustomerType(), subscriptionCatalogResponse.getCustomerSegments())
				&& (null != subscriptionCatalogResponse.getPaymentMethods() && null != getMemberResponse.getEligiblePaymentMethod()
					&& SubscriptionCatalogValidator.validatePaymentMethod(subscriptionCatalogResponse.getPaymentMethods(), getMemberResponse.getEligiblePaymentMethod()))) {
			
			if (subscriptionUtils.hasAvailedFreeDuration(getMemberResponse.getAccountNumber(), subscriptionCatalogResponse.getSubscriptionSegment())) {
				LOG.info("populateEligibleSubscriptionCatalog : hasAvailedFreeDuration");
				subscriptionCatalogResponseDto.setFreeDuration(0);
			}
			
			List<PaymentMethodDto> eligiblePaymentMethods = populateEligiblePaymentMethods(subscriptionCatalogResponse, getMemberResponse, headers);
			if(null != eligiblePaymentMethods) {
				subscriptionCatalogResponseDto.setPaymentMethods(eligiblePaymentMethods);
				
				LOG.info("eligibleCatalogResponse.getId() : {}", subscriptionCatalogResponse.getId());
				eligibleSubscriptionCatalogResponseList.add(subscriptionCatalogResponseDto);
			}
		}
	}
	
	public SubscriptionResponseDto populatePassiveSubscription(Subscription elifeSubscription, List<SubscriptionPayment> subscriptionPaymentList) throws SubscriptionManagementException {
		LOG.info("populatePassiveSubscription :: elifeSubscription : {}", elifeSubscription);
		SubscriptionResponseDto subscriptionResponseDto = modelMapper.map(elifeSubscription, SubscriptionResponseDto.class);
		if(!ObjectUtils.isEmpty(elifeSubscription)) {
			List<SubscriptionPayment> subscriptionPayment = subscriptionPaymentList.stream().filter(s -> s.getSubscriptionId().equalsIgnoreCase(elifeSubscription.getId())).collect(Collectors.toList());	
			LOG.info("Record from SubscriptionPayment collection: {}", subscriptionPayment);
			if(null != subscriptionPayment && !subscriptionPayment.isEmpty()) {
				Optional<PaymentMethodDetails> paymentMethodDetails = subscriptionPayment.stream().flatMap(a -> a.getPaymentMethodDetails().stream()
						.filter(b ->b.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())))
						.findFirst();
				LOG.info("paymentMethodDetails in populatePassiveSubscription:{}",paymentMethodDetails);
				SubscriptionPaymentResponseDto subscriptionPaymentResponseDto = new SubscriptionPaymentResponseDto();				
				subscriptionPaymentResponseDto.setPaymentMethod(paymentMethodDetails.get().getPaymentMethod());
				subscriptionPaymentResponseDto.setPaymentMethodId(paymentMethodDetails.get().getPaymentMethodId());
				if(paymentMethodDetails.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())) {
					if(null != paymentMethodDetails.get().getCcDetails().getCardNumber()) 
						subscriptionPaymentResponseDto.setCardNumber(paymentMethodDetails.get().getCcDetails().getCardNumber());
					if(null != paymentMethodDetails.get().getCcDetails().getSubType()) 
						subscriptionPaymentResponseDto.setSubType(paymentMethodDetails.get().getCcDetails().getSubType());
				}
				subscriptionResponseDto.setPaymentDetails(subscriptionPaymentResponseDto);
			}
		}
		LOG.info("populatePassiveSubscription :: subscriptionResponseDto : {}", subscriptionResponseDto);
		return subscriptionResponseDto;
	}
	
	public void populatePassiveForElife(String accountNumber, GetMemberResponse getMemberResponse, SubscriptionResultResponse subscriptionResultResponse) throws SubscriptionManagementException {
		LOG.info("populatePassiveForElife");
		List<SubscriptionResponseDto> subscriptionResponseList = new ArrayList<>();
		SubscriptionCatalog elifeSubscriptionCatalog = subscriptionCatalogDomain.findByPackageType(SubscriptionManagementConstants.PACKAGE_TYPE_ELIFE.get());
		List<Subscription> elifeSubscriptionList = subscriptionDomain.findByMembershipCodeAndSubscriptionCatalogIdAndStatusIn(getMemberResponse.getMembershipCode(),elifeSubscriptionCatalog.getId(),Arrays.asList(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(),SubscriptionManagementConstants.PARKED_STATUS.get()));
		if(!ObjectUtils.isEmpty(elifeSubscriptionList)) {
			List<SubscriptionPayment> subscriptionPaymentList = subscriptionPaymentRepository.findBySubscriptionIdInAndPaymentMethodDetails_Status(
					elifeSubscriptionList.stream().map(Subscription :: getId).collect(Collectors.toList()),SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
			for(Subscription elifeSubscription : elifeSubscriptionList) {
				if(!(elifeSubscription.getAccountNumber().equalsIgnoreCase(accountNumber))) {
					subscriptionResponseList.add(populatePassiveSubscription(elifeSubscription, subscriptionPaymentList));
				}
			}
			
		}
		if(!ObjectUtils.isEmpty(subscriptionResponseList)) {
			subscriptionResultResponse.setPassiveSubscription(subscriptionResponseList);
			SubscriptionCatalogResponseDto subscriptionCatalog = modelMapper.map(elifeSubscriptionCatalog, SubscriptionCatalogResponseDto.class);
			List<SubscriptionCatalogResponseDto> subscriptionCatalogResponseDto = new ArrayList<>();
			subscriptionCatalogResponseDto.add(subscriptionCatalog);
			subscriptionResultResponse.setPassiveSubscriptionCatalog(subscriptionCatalogResponseDto);
		}
	}
	
	public List<PaymentMethodDto> populateEligiblePaymentMethods(SubscriptionCatalog subscriptionCatalogResponse, GetMemberResponse getMemberResponse, Headers headers) throws SubscriptionManagementException {
		List<SubscriptionPaymentMethod> commonPaymentMethods= SubscriptionCatalogValidator.populateCommonPaymentMethods(subscriptionCatalogResponse.getPaymentMethods(), 
				getMemberResponse.getEligiblePaymentMethod());	
				
		List<PaymentMethodDto> commonPaymentMethodDtoList = (null != commonPaymentMethods && !commonPaymentMethods.isEmpty()) 
				? commonPaymentMethods.stream().map(commonPaymentMethod -> modelMapper.map(commonPaymentMethod, PaymentMethodDto.class)).collect(Collectors.toList())
				: null;		
		
		LOG.info("commonPaymentMethodDtoList : {}",commonPaymentMethodDtoList);
		List<String> customerTypeList = getMemberResponse.getCustomerType();
		if(null != customerTypeList && customerTypeList.contains(SubscriptionManagementConstants.ENTERPRISE_CUSTOMER_TYPE_MOBILE.get())) {
			if(!(getMemberResponse.getEligiblePaymentMethod().stream().
					anyMatch(p -> p.getDescription().equals(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
							&& p.getDescription().equals(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())))) {
			
				String numberType = getMemberResponse.getNumberType();
				if(null != numberType && getMemberResponse.getNumberType().equalsIgnoreCase(SubscriptionManagementConstants.NUMBER_TYPE_PREPAID.get())) {
					PaymentMethodDto paymentMethod = new PaymentMethodDto();
					paymentMethod.setPaymentMethodId("4");					
					paymentMethod.setDescription(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get());
					commonPaymentMethodDtoList.add(paymentMethod);
				}
				if(null != numberType && getMemberResponse.getNumberType().equalsIgnoreCase(SubscriptionManagementConstants.NUMBER_TYPE_POSTPAID.get())) {
					PaymentMethodDto paymentMethod = new PaymentMethodDto();
					paymentMethod.setPaymentMethodId("3");					
					paymentMethod.setDescription(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get());
					commonPaymentMethodDtoList.add(paymentMethod);
				}
			}
		}
		
		LOG.info("commonPaymentMethodDtoList : {}",commonPaymentMethodDtoList);
		if(null != commonPaymentMethodDtoList && subscriptionUtils.isEnterpriseAndCrmAddOnServiceDisabled(getMemberResponse, headers)) {
			LOG.info("populateEligibleSubscriptionCatalog : isEnterpriseAndCrmAddOnServiceDisabled");			
			for (Iterator<PaymentMethodDto> iterator = commonPaymentMethodDtoList.iterator(); iterator.hasNext();) {
				PaymentMethodDto paymentMethodDto= iterator.next();
			    if (paymentMethodDto.getDescription().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
			    		|| paymentMethodDto.getDescription().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())) {
			       iterator.remove();
			    }
			}														
		}
		
		LOG.info("commonPaymentMethodDtoList : {}",commonPaymentMethodDtoList);
		if(null != commonPaymentMethodDtoList && subscriptionCatalogResponse.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			for (Iterator<PaymentMethodDto> iterator = commonPaymentMethodDtoList.iterator(); iterator.hasNext();) {
				PaymentMethodDto paymentMethodDto= iterator.next();
			    if (!paymentMethodDto.getDescription().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
			    		&& !paymentMethodDto.getDescription().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())
			    		&& !paymentMethodDto.getDescription().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())) {
			       iterator.remove();
			    }
			}												
		}
		LOG.info("commonPaymentMethodDtoList : {}",commonPaymentMethodDtoList);
		if(null != commonPaymentMethodDtoList && nonDCBCustomerType.contains(getMemberResponse.getCustomerType().get(0))) {
			for (Iterator<PaymentMethodDto> iterator = commonPaymentMethodDtoList.iterator(); iterator.hasNext();) {
				PaymentMethodDto paymentMethodDto= iterator.next();
				if (!paymentMethodDto.getDescription().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())) {
					iterator.remove();
				}
			}
		}
		return commonPaymentMethodDtoList;
		
	}
	
	
	public void cancelParkActivateSubscription(ManageSubscriptionRequestDto manageSubscriptionRequestDto, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException {
		List<Subscription> subscriptionList = subscriptionValidator.validateSubscriptionExists(manageSubscriptionRequestDto, headers, resultResponse);
		SubscriptionCatalog subscriptionCatalog = new SubscriptionCatalog();
		NotificationValues notificationValues = null;
		if (!subscriptionList.isEmpty()) {
			LOG.info("cancelParkActivateSubscription :: SubscriptionList : {} :: ExternalTransactionId : {}", subscriptionList, headers.getExternalTransactionId());
			
			if(manageSubscriptionRequestDto.getAction().equalsIgnoreCase(SubscriptionManagementConstants.CANCEL_SUBSCRIPTION.get())) {
				if(!ObjectUtils.isEmpty(manageSubscriptionRequestDto) && StringUtils.isEmpty(manageSubscriptionRequestDto.getCancellationReason())) {
					manageSubscriptionRequestDto.setCancellationReason(SubscriptionManagementConstants.REASON_NOT_APPLICABLE.get());
				}
				subscriptionCatalog = validateAndCancelSubscription(manageSubscriptionRequestDto, subscriptionList, headers, resultResponse);	
			} else if(manageSubscriptionRequestDto.getAction().equalsIgnoreCase(SubscriptionManagementConstants.PARK_SUBSCRIPTION.get())) {
				List<Subscription> autoSubscriptionList = subscriptionValidator.validateAutoSubscription(subscriptionList, resultResponse);
				subscriptionCatalog = parkSubscription(autoSubscriptionList, headers, resultResponse);
			} else if(manageSubscriptionRequestDto.getAction().equalsIgnoreCase(SubscriptionManagementConstants.ACTIVATE_SUBSCRIPTION.get())) {
				List<Subscription> autoSubscriptionList = subscriptionValidator.validateAutoSubscription(subscriptionList, resultResponse);
				subscriptionCatalog = activateSubscription(autoSubscriptionList, headers, manageSubscriptionRequestDto, resultResponse);
				notificationValues = new NotificationValues(false, true, subscriptionList.get(0).getAccountNumber(),null,autoSubscriptionList.get(0).getPaymentMethod(),null,0.0, SubscriptionManagementConstants.RENEWED_EVENT, SubscriptionManagementConstants.ACTION_TYPE_RENEWED.get(), manageSubscriptionRequestDto.getNextRenewalDate(), null);
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_ACTION.getIntId(),
						SubscriptionManagementCode.INVALID_ACTION.getMsg());
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
			}
			if(null != subscriptionCatalog && subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())
					&& manageSubscriptionRequestDto.getAction().equalsIgnoreCase(SubscriptionManagementConstants.ACTIVATE_SUBSCRIPTION.get())) {
				subscriptionUtils.subscriptionNotification(headers, subscriptionCatalog, notificationValues, resultResponse);
			}
			
		} else {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		}
		
	}
	
	
	public void manageDataForSubscriptionRenewal(ManageRenewalRequestDto manageRenewalRequestDto, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException, MarketplaceException {
        Subscription existingSubscription = subscriptionDomain.findById(manageRenewalRequestDto.getSubscriptionId()).get();
        if(null != existingSubscription) {
        SubscriptionPayment activeSubscriptionPayment = subscriptionPaymentRepository.findBySubscriptionId(manageRenewalRequestDto.getSubscriptionId());
        if(null != activeSubscriptionPayment) {
        Optional<PaymentMethodDetails> paymentMethodDetails = activeSubscriptionPayment.getPaymentMethodDetails().stream()
        		.filter(p ->p.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())).findFirst();
        
        SubscriptionCatalog subscriptionCatalog =  subscriptionCatalogValidator.fetchSubscriptionCatalogById(existingSubscription.getSubscriptionCatalogId());
        
        if (paymentMethodDetails.isPresent() && null != subscriptionCatalog) {   
            LOG.info("Entering manageDataForSubscriptionRenewal() : SubscriptionPaymentRequestDto :: {} : ExternalTransactionId :: {}", manageRenewalRequestDto, headers.getExternalTransactionId());          
            if(manageRenewalRequestDto.getRenewPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())) {        	
            	if(subscriptionUtils.validateManadatoryFieldsForCC(manageRenewalRequestDto, resultResponse)) {
            		if(paymentMethodDetails.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())
            				|| existingSubscription.getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())) {
            			//exit if same card details
            			if(manageRenewalRequestDto.getMasterEPGTransactionId().equalsIgnoreCase(paymentMethodDetails.get().getMasterEPGTransactionId())){
            				resultResponse.addErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_CCDETAILS_ALREADY_EXISTS.getIntId(),
                                    SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_CCDETAILS_ALREADY_EXISTS.getMsg());           				
            				return;
            			} 
            		}           		
            		if (paymentMethodDetails.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
            				|| existingSubscription.getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
            				|| paymentMethodDetails.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())
            				|| existingSubscription.getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())) {
            			//call cancel api in phoney tunes
            			LOG.info("Cancelling Phonytunes subscription when requested payment method is: {} and existing payment method is:{}",
            					manageRenewalRequestDto.getRenewPaymentMethod(), existingSubscription.getPaymentMethod());
            			if(!subscriptionPurchaseValidator.cancelPhoneyTunesSubscription(existingSubscription, subscriptionCatalog.getChargeabilityType(), headers, resultResponse))
            				return ;//exit if above is failure response
            			
            		}           		
            		renewPaymentMethodToCard(manageRenewalRequestDto, activeSubscriptionPayment, existingSubscription, subscriptionCatalog,
        					paymentMethodDetails, headers, resultResponse);
        			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getId(), 
        					SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getMsg());
        			resultResponse.setSuccessAPIResponse();            		            		
            	}  
            } else if(manageRenewalRequestDto.getRenewPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())) {
                if(paymentMethodDetails.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
                		|| activeSubscriptionPayment.getPaymentMethodDetails().get(0).getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())) {
                    resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_METHOD_ALREADY_EXISTS.getIntId(),
                            SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_METHOD_ALREADY_EXISTS.getMsg());
                    resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
        					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
                    return;
                }                
                renewPaymentMethodToDCB(manageRenewalRequestDto, activeSubscriptionPayment, existingSubscription, subscriptionCatalog, paymentMethodDetails, 
                		headers, resultResponse);              
            } else if(manageRenewalRequestDto.getRenewPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())) {
                if(paymentMethodDetails.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())) {
                    resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_METHOD_ALREADY_EXISTS.getIntId(),
                            SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_METHOD_ALREADY_EXISTS.getMsg());
                    resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
        					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
                    return;
                }                
                renewPaymentMethodToDCB(manageRenewalRequestDto, activeSubscriptionPayment, existingSubscription, subscriptionCatalog, paymentMethodDetails, 
                		headers, resultResponse);  			
            } else { 
            	sendErrorForInvalidPaymentMethod(resultResponse);
        }       
    }
        } else {
    	resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
                SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
    }
        } else {
        	resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
                    SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
        }
}
	
	public void renewPaymentMethodToCard(ManageRenewalRequestDto manageRenewalRequestDto, SubscriptionPayment activeSubscriptionPayment, Subscription existingSubscription, 
			SubscriptionCatalog subscriptionCatalog, Optional<PaymentMethodDetails> paymentMethodDetailsOfSuccessStatus, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		
		LOG.info("Entering renewPaymentMethodToCard:");
		if(existingSubscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {
			LOG.info("Inside renewPaymentMethodToCard where subscription status is:{} and existing payment method is :{}",
					existingSubscription.getStatus(), paymentMethodDetailsOfSuccessStatus.get().getPaymentMethod());
											
			setStatusInactiveForExistingPaymentMethod(activeSubscriptionPayment, paymentMethodDetailsOfSuccessStatus, headers);
			renewSubscriptionPaymentMethod(manageRenewalRequestDto, activeSubscriptionPayment,  headers);
			callAuditForSubscriptionPaymentUpdate(activeSubscriptionPayment, headers);
			updateExistingSubscription(existingSubscription, subscriptionCatalog, manageRenewalRequestDto, findRemainingFreeDuration(existingSubscription), headers);
			callAuditForSubscriptionUpdate(existingSubscription, headers);					
		} else if(existingSubscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())) {
			
			LOG.info("Inside renewPaymentMethodToCard where subscription status is:{} and existing payment method is :{}",
					existingSubscription.getStatus(), paymentMethodDetailsOfSuccessStatus.get().getPaymentMethod());
			
			setStatusInactiveForExistingPaymentMethod(activeSubscriptionPayment, paymentMethodDetailsOfSuccessStatus, headers);		
			renewSubscriptionPaymentMethod(manageRenewalRequestDto, activeSubscriptionPayment,  headers);
			callAuditForSubscriptionPaymentUpdate(activeSubscriptionPayment, headers);
			PurchaseRequestDto purchaseRequestDto = subscriptionUtils.populatePurchaseRequestDto(subscriptionCatalog, existingSubscription, activeSubscriptionPayment, headers);
			purchaseRequestDto.setAuthorizationCode(manageRenewalRequestDto.getCcDetails().getAuthorizationCode());
			purchaseRequestDto.setCardToken(manageRenewalRequestDto.getCcDetails().getCardToken());
			purchaseRequestDto.setEpgTransactionId(manageRenewalRequestDto.getEpgTransactionID());
			paymentService.callMiscPaymentPostingForAutoRenewalSubscriptions(purchaseRequestDto, MarketplaceConstants.NEW_AUTORENEWAL_SUBSCRIPTION.getConstant(), headers);
			updateExistingSubscription(existingSubscription, subscriptionCatalog, manageRenewalRequestDto, 0, headers);
			callAuditForSubscriptionUpdate(existingSubscription, headers);				
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_INVALID_SUBSCRIPTION_STATUS.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_INVALID_SUBSCRIPTION_STATUS.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());			
		}
	}
	
	
	private void callAuditForSubscriptionPaymentUpdate(SubscriptionPayment activeSubscriptionPayment, Headers headers) {
		Gson gsonForPay = new Gson();
		SubscriptionPayment originalSubscriptionPayment = gsonForPay.fromJson(gsonForPay.toJson(activeSubscriptionPayment), SubscriptionPayment.class);		
		auditService.updateDataAudit(SubscriptionManagementConstants.SUBSCRIPTION_PAYMENT, activeSubscriptionPayment, 
				SubscriptionRequestMappingConstants.MANAGE_RENEWAL, originalSubscriptionPayment, 
				headers.getExternalTransactionId(), headers.getUserName());		
					
		LOG.info("after updateDataAudit call");
	}

	private void callAuditForSubscriptionUpdate(Subscription existingSubscription, Headers headers) {		
		Gson gsonForSub = new Gson();
		Subscription originalSubscription = gsonForSub.fromJson(gsonForSub.toJson(existingSubscription), Subscription.class);
		auditService.updateDataAudit(SubscriptionManagementConstants.SUBSCRIPTION, existingSubscription, 
				SubscriptionRequestMappingConstants.MANAGE_RENEWAL, originalSubscription, 
				headers.getExternalTransactionId(), headers.getUserName());		
					
		LOG.info("after updateDataAudit call for Subscription update");
	}

	public void setStatusInactiveForExistingPaymentMethod(SubscriptionPayment activeSubscriptionPayment, Optional<PaymentMethodDetails> paymentMethodDetailsOfSuccessStatus, 
			Headers headers) throws SubscriptionManagementException {
		try {	
			LOG.info("Entering setStatusAndInactiveDateForExistingPaymentMethod: Username: {}", headers.getUserName());		
			if(CollectionUtils.isEmpty(activeSubscriptionPayment.getPaymentMethodDetails())) {
				activeSubscriptionPayment.setPaymentMethodDetails(new ArrayList<>());
			}
			
			paymentMethodDetailsOfSuccessStatus.get().setStatus(SubscriptionManagementConstants.SUBSCRIPTION_INACTIVE_STATUS.get());
			paymentMethodDetailsOfSuccessStatus.get().setInactiveDate(new Date());	
			
			activeSubscriptionPayment.setUpdatedDate(new Date());
			activeSubscriptionPayment.setUpdatedUser(headers.getUserName());
			LOG.info("Exit setStatusInactiveForExistingPaymentMethod");
			//subscriptionPaymentRepository.save(activeSubscriptionPayment);	
		} catch (Exception e) {			
			e.printStackTrace();
			LOG.info("Exception occured while updating subscription payment.");
			throw new SubscriptionManagementException(this.getClass().toString(), "setStatusAndInactiveDateForExistingPaymentMethod",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED);		
		} 
	}

	public void renewSubscriptionPaymentMethod(ManageRenewalRequestDto manageRenewalRequestDto,
			SubscriptionPayment activeSubscriptionPayment, Headers headers) throws SubscriptionManagementException {
			try {	
				LOG.info("Entering renewSubscriptionPaymentMethd: UserName: {}", headers.getUserName());		
				if(CollectionUtils.isEmpty(activeSubscriptionPayment.getPaymentMethodDetails())) {
					activeSubscriptionPayment.setPaymentMethodDetails(new ArrayList<>());;
				}
				PaymentMethodDetails paymentMethodDetails = new PaymentMethodDetails();
				if(manageRenewalRequestDto.getRenewPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())) {
					CCDetails creditCardDtls = new CCDetails();
					creditCardDtls.setCardNumber(manageRenewalRequestDto.getCcDetails().getCardNumber());
					creditCardDtls.setSubType(manageRenewalRequestDto.getCcDetails().getSubType());
					
					paymentMethodDetails.setMasterEPGTransactionId(manageRenewalRequestDto.getMasterEPGTransactionId());
					paymentMethodDetails.setCcDetails(creditCardDtls);
					paymentMethodDetails.setEpgTransactionID(manageRenewalRequestDto.getEpgTransactionID());
				}
				paymentMethodDetails.setStatus(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
				paymentMethodDetails.setPaymentMethod(manageRenewalRequestDto.getRenewPaymentMethod());
				paymentMethodDetails.setPaymentMethodId(paymentMethodRepository.findByDescription(manageRenewalRequestDto.getRenewPaymentMethod()).getPaymentMethodId());
					
				activeSubscriptionPayment.getPaymentMethodDetails().add(paymentMethodDetails);
				activeSubscriptionPayment.setUpdatedDate(new Date());
				activeSubscriptionPayment.setUpdatedUser(headers.getUserName());
				subscriptionPaymentRepository.save(activeSubscriptionPayment);
				LOG.info("Exit renewSubscriptionPaymentMethod");	
			} catch (Exception e) {	
				e.printStackTrace();
				LOG.info("Exception occured while renewing subscription payment method.");
				throw new SubscriptionManagementException(this.getClass().toString(), "renewSubscriptionPaymentMethd",
						e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED);			
			}			
		}
	
	public void updateExistingSubscription(Subscription existingSubscription, SubscriptionCatalog subscriptionCatalog, ManageRenewalRequestDto manageRenewalRequestDto,
			int duration, Headers headers) throws SubscriptionManagementException {
		try {	
			LOG.info("updateExistingSubscription: UserName: {}", headers.getUserName());	
			RenewalValues renewalValues  = subscriptionUtils.populateRenewalValues(subscriptionCatalog, duration);
			
			if(existingSubscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())) {
				existingSubscription.setStatus(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get());
			}
			
			existingSubscription.setPaymentMethod(manageRenewalRequestDto.getRenewPaymentMethod());
			existingSubscription.setNextRenewalDate(renewalValues.getNextRenewalDate());
			existingSubscription.setLastChargedAmount(renewalValues.getLastChargedAmount());
			existingSubscription.setLastChargedDate(renewalValues.getLastChargedDate());			
			existingSubscription.setUpdatedDate(new Date());
			existingSubscription.setUpdatedUser(headers.getUserName());
			subscriptionRepository.save(existingSubscription);
			LOG.info("Exit updateExistingSubscription");	
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Exception occured while updating subscription collection.");
			throw new SubscriptionManagementException(this.getClass().toString(), "updateExistingSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED);
		
		} 
		
	}
	
	public void sendErrorForInvalidPaymentMethod(ResultResponse resultResponse) {
		resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_INVALID_PAYMENT_METHOD.getIntId(),
				SubscriptionManagementCode.SUBSCRIPTION_INVALID_PAYMENT_METHOD.getMsg());
		resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
				SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());		
	}

	public void renewPaymentMethodToDCB(ManageRenewalRequestDto manageRenewalRequestDto, SubscriptionPayment activeSubscriptionPayment, 
			Subscription existingSubscription, SubscriptionCatalog subscriptionCatalog, Optional<PaymentMethodDetails> paymentMethodDetailsOfSuccessStatus, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		
		LOG.info("Entering renewPaymentMethodToDCB:");
		if(existingSubscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {
			
			LOG.info("Inside renewPaymentMethodToDCB where current status of subscription : {},existing payment method : {}, renewing payment method to : {}",
					existingSubscription.getStatus(), paymentMethodDetailsOfSuccessStatus.get().getPaymentMethod(), manageRenewalRequestDto.getRenewPaymentMethod());	
			
			int remainingFreeDuration =  findRemainingFreeDuration(existingSubscription);
			
			if((paymentMethodDetailsOfSuccessStatus.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
					&& manageRenewalRequestDto.getRenewPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get()))
					|| (paymentMethodDetailsOfSuccessStatus.get().getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get())
					&& manageRenewalRequestDto.getRenewPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get()))) {
				
				setStatusInactiveForExistingPaymentMethod(activeSubscriptionPayment, paymentMethodDetailsOfSuccessStatus, headers);
				renewSubscriptionPaymentMethod(manageRenewalRequestDto, activeSubscriptionPayment, headers);
				callAuditForSubscriptionPaymentUpdate(activeSubscriptionPayment, headers);
				updateExistingSubscription(existingSubscription, subscriptionCatalog, manageRenewalRequestDto, remainingFreeDuration, headers);	
				callAuditForSubscriptionUpdate(existingSubscription, headers);
				
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getId(), 
    					SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getMsg());
    			resultResponse.setSuccessAPIResponse();
			} else {									
			PhoneyTunesRequest phoneyTunesRequest = new PhoneyTunesRequest(existingSubscription.getAccountNumber(), remainingFreeDuration, 
					SubscriptionManagementConstants.ACTIVATE_SUBSCRIPTION.get(), subscriptionCatalog.getChargeabilityType(), existingSubscription.getPhoneyTunesPackageId(), 
					subscriptionCatalogValidator.fetchPhoneyTunesPackageId(subscriptionCatalog.getChargeabilityType(), existingSubscription.getSubscriptionSegment()),
					headers.getExternalTransactionId());
			PhoneyTunesResponse phoneyTunesResponse = subscriptionService.getPhoneyTunesResponse(phoneyTunesRequest, resultResponse, headers.getExternalTransactionId());
			
			if(null != phoneyTunesResponse
					&& phoneyTunesResponse.getAckMessage().getStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())
						&& phoneyTunesResponse.getResponse().toUpperCase().contains(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
				setStatusInactiveForExistingPaymentMethod(activeSubscriptionPayment, paymentMethodDetailsOfSuccessStatus, headers);
				renewSubscriptionPaymentMethod(manageRenewalRequestDto, activeSubscriptionPayment, headers);
				callAuditForSubscriptionPaymentUpdate(activeSubscriptionPayment, headers);
				updateExistingSubscription(existingSubscription, subscriptionCatalog, manageRenewalRequestDto, remainingFreeDuration, headers);	
				callAuditForSubscriptionUpdate(existingSubscription, headers);
			
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getId(), 
    					SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getMsg());
    			resultResponse.setSuccessAPIResponse();
			} else {
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
			}
		}
					
		} else if (existingSubscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())) {
								
			LOG.info("Inside renewPaymentMethodToDCB where current status of subscription : {},existing payment method : {}, renewing payment method to : {}",
					existingSubscription.getStatus(), paymentMethodDetailsOfSuccessStatus.get().getPaymentMethod(), manageRenewalRequestDto.getRenewPaymentMethod());			
				
			PhoneyTunesRequest phoneyTunesRequest = new PhoneyTunesRequest(existingSubscription.getAccountNumber(), 0, 
					SubscriptionManagementConstants.ACTIVATE_SUBSCRIPTION.get(), subscriptionCatalog.getChargeabilityType(), existingSubscription.getPhoneyTunesPackageId(), 
					subscriptionCatalogValidator.fetchPhoneyTunesPackageId(subscriptionCatalog.getChargeabilityType(), existingSubscription.getSubscriptionSegment()),
					headers.getExternalTransactionId());
			PhoneyTunesResponse phoneyTunesResponse = subscriptionService.getPhoneyTunesResponse(phoneyTunesRequest, resultResponse, headers.getExternalTransactionId());
			
			if(null != phoneyTunesResponse
					&& phoneyTunesResponse.getAckMessage().getStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())
						&& phoneyTunesResponse.getResponse().toUpperCase().contains(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
				setStatusInactiveForExistingPaymentMethod(activeSubscriptionPayment, paymentMethodDetailsOfSuccessStatus, headers);
				renewSubscriptionPaymentMethod(manageRenewalRequestDto, activeSubscriptionPayment, headers);
				callAuditForSubscriptionPaymentUpdate(activeSubscriptionPayment, headers);
				updateExistingSubscription(existingSubscription, subscriptionCatalog, manageRenewalRequestDto, 0, headers);	
				callAuditForSubscriptionUpdate(existingSubscription, headers);
				
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getId(), 
    					SubscriptionManagementCode.SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS.getMsg());
    			resultResponse.setSuccessAPIResponse();
			} else {
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
			}
			
		} else {
			sendErrorForInvalidPaymentMethod(resultResponse);
		}
	}
	
	public int findRemainingFreeDuration(Subscription existingSubscription) {
		
		int remainingFreeDuration  = (int) ((existingSubscription.getNextRenewalDate().getTime() - new Date().getTime())/ 1000/60/60/24);
		LOG.info("Remaining Free Duration:"+ remainingFreeDuration);
		return remainingFreeDuration;
	}

	public SubscriptionCatalog validateAndCancelSubscription(ManageSubscriptionRequestDto manageSubscriptionRequestDto, List<Subscription> subscriptionList, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		List<Subscription> subscription = subscriptionList.stream()
				.filter(s -> SubscriptionManagementConstants.SUBSCRIBED_STATUS.get().equalsIgnoreCase(s.getStatus()) 
						|| SubscriptionManagementConstants.PARKED_STATUS.get().equalsIgnoreCase(s.getStatus()))
				.collect(Collectors.toList());		
		if(subscription.size() == 1) {
			LOG.info("validateAndCancelSubscription :: username : {}", headers.getUserName());
			if(subscriptionUtils.evaluateCancelCondition(manageSubscriptionRequestDto, subscription.get(0), headers)) {
				LOG.info("validateAndCancelSubscription : {}", subscription.get(0).getId());
				subscriptionDomain.cancelSubscription(manageSubscriptionRequestDto, subscription.get(0), null, headers);				
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CANCELLED.getId(), SubscriptionManagementCode.SUBSCRIPTION_CANCELLED.getMsg());
				resultResponse.setSuccessAPIResponse();
				
			} else if(subscriptionCatalogValidator.fetchChargeabilityType(subscription.get(0).getSubscriptionCatalogId())
					.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()) 
					&& (((subscription.get(0).getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
							|| subscription.get(0).getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get()))
							&& subscriptionPurchaseValidator.cancelPhoneyTunesSubscription(subscription.get(0), SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get(), headers, resultResponse))
						|| (subscription.get(0).getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())
							&& !headers.getChannelId().equalsIgnoreCase(SubscriptionManagementConstants.ADMIN_PORTAL.get())))) {
				
					LOG.info("validateAndCancelSubscription :: Cancel Auto-Renewal Subscription");
					subscriptionDomain.cancelSubscription(manageSubscriptionRequestDto, subscription.get(0), null, headers);				
					resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CANCELLED.getId(), SubscriptionManagementCode.SUBSCRIPTION_CANCELLED.getMsg());
					resultResponse.setSuccessAPIResponse();	
					return subscriptionCatalogValidator.fetchSubscriptionCatalogById(subscription.get(0).getSubscriptionCatalogId());
				
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_CANCEL_FAILED.getIntId(),
						SubscriptionManagementCode.SUBSCRIPTION_CANCEL_FAILED.getMsg());
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
			}
			
		} else if (subscription.isEmpty()){
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.MULTIPLE_SUBSCRIPTION_FOUND.getIntId(),
					SubscriptionManagementCode.MULTIPLE_SUBSCRIPTION_FOUND.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		}
		LOG.info("Inside ValidateAndCancelSubscription ExternalTransactionId:{}",headers.getExternalTransactionId());
		return null;
	}
	
	
	
	public void cancelExpiredOneTimeSubscriptions() throws SubscriptionManagementException {
		List<SubscriptionCatalog> oneTimeSubscriptionCatalog = subscriptionCatalogDomain.findByChargeabilityType(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get());
		List<String> oneTimeSubscriptionCatalogIdList = oneTimeSubscriptionCatalog
	            .stream()
	            .map(SubscriptionCatalog::getId)
	            .collect(Collectors.toList());
		LOG.info("oneTimeSubscriptionCatalogIdList : {}",oneTimeSubscriptionCatalogIdList);
		if(null != oneTimeSubscriptionCatalogIdList) {
			UpdateResult updateResult = subscriptionRepositoryHelper.cancelExpiredSubscriptions(oneTimeSubscriptionCatalogIdList, SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(), new Date());
			LOG.info("UpdateResult : {}", updateResult);
		}
	}
	
	public void chargeForSubscriptionRenewal(List<SubscriptionCatalog> autoRenewalSubscriptionCatalog, Date date, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		
//		List<SubscriptionCatalog> autoRenewalSubscriptionCatalog = subscriptionCatalogDomain.findByChargeabilityType(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get());
		if(null != autoRenewalSubscriptionCatalog && !autoRenewalSubscriptionCatalog.isEmpty()) {
			date = Utilities.setTimeInDate(date, SubscriptionManagementConstants.END_DATE_TIME.get());
			LOG.info("RenewDate : {}", date);
			List<Subscription> renewSubscriptionList = subscriptionRepositoryHelper.findSubscriptionToRenew(autoRenewalSubscriptionCatalog.stream().map(SubscriptionCatalog::getId).collect(Collectors.toList()), date);
			LOG.info("Renew Subscription List : {}", renewSubscriptionList);
			
			if(null != renewSubscriptionList && !renewSubscriptionList.isEmpty()) {
				List<SubscriptionPayment> subscriptionPaymentList = subscriptionPaymentRepository.findBySubscriptionIdInAndPaymentMethodDetails_Status(renewSubscriptionList.stream().map(Subscription::getId).collect(Collectors.toList()), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
				LOG.info("Subscription Payment List : {}", subscriptionPaymentList);
				
				if(null != subscriptionPaymentList && !subscriptionPaymentList.isEmpty()) {
					List<Subscription> renewSubscribedList = renewSubscriptionList.stream()
						.filter(s -> SubscriptionManagementConstants.SUBSCRIBED_STATUS.get().equalsIgnoreCase(s.getStatus()))
						.collect(Collectors.toList());
					
					List<Subscription> renewParkedList = renewSubscriptionList.stream()
							.filter(s ->  SubscriptionManagementConstants.PARKED_STATUS.get().equalsIgnoreCase(s.getStatus()))
							.collect(Collectors.toList());
					
					LOG.info("Renew Subscribed List : {}", renewSubscribedList);
					LOG.info("Renew Parked List : {}", renewParkedList);
					
					
					if(null != renewSubscribedList && !renewSubscribedList.isEmpty()) {
						renewSubscribedSubscriptions(autoRenewalSubscriptionCatalog, renewSubscribedList, subscriptionPaymentList, headers, resultResponse);
					}
					
					if(null != renewParkedList && !renewParkedList.isEmpty()) {
						renewParkedSubscriptions(autoRenewalSubscriptionCatalog, renewParkedList, subscriptionPaymentList, headers, resultResponse);
					}
				}	
				 
			}		
			
		} 
	}
	
	public void renewSubscribedSubscriptions(List<SubscriptionCatalog> autoRenewalSubscriptionCatalog, List<Subscription> renewSubscribedList, List<SubscriptionPayment> subscriptionPaymentList, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {		
		for(Subscription subscribedSubscription : renewSubscribedList) {				
			LOG.info("EPG Call");
			SubscriptionCatalog subscriptionCatalog = autoRenewalSubscriptionCatalog.stream().filter(p -> p.getId().equalsIgnoreCase(subscribedSubscription.getSubscriptionCatalogId())).findFirst().get();
			PurchaseRequestDto purchaseRequestDto = subscriptionUtils.populatePurchaseRequestDto(subscriptionCatalog, subscribedSubscription, subscriptionPaymentList.stream().filter(s -> subscribedSubscription.getId().equals(s.getSubscriptionId())).findFirst().get(), headers);
//			EPGResponse epgResponse = new EPGResponse();
			try {
				if(subscriptionService.epgTransaction(purchaseRequestDto, headers, Utilities.getDateFromSpecificDate(subscribedSubscription.getValidityPeriod(), new Date()), resultResponse)) {
					subscriptionDomain.updateSubscriptionForCharging(subscribedSubscription, subscriptionCatalog.getCost(),
							new Date(), Utilities.getDateFromSpecificDate(subscribedSubscription.getValidityPeriod(), new Date()), 
							SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(), headers);
					
//					String response = epgResponse.getTransaction().getResponseDescription();
//					PurchaseExtraAttributes purchaseExtraAttributes = new PurchaseExtraAttributes(subscriptionCatalog.getSubscriptionSegment(),response,null);
//					PurchaseDomain purchaseDomainToSave = DomainConfiguration.getPurchaseDomain(null, purchaseRequestDto, null, null, headers, purchaseExtraAttributes);		    
//					purchaseDomain.saveUpdatePurchaseDetails(purchaseDomainToSave, null, OfferConstants.INSERT_ACTION.get(),headers);

				} else {
					subscriptionDomain.updateSubscriptionStatus(subscribedSubscription, SubscriptionManagementConstants.PARKED_STATUS.get(), headers);
				}
			} catch (Exception e) {
				LOG.info("EPG Failed for renew subscribed subscription : {}",e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void renewParkedSubscriptions(List<SubscriptionCatalog> autoRenewalSubscriptionCatalog, List<Subscription> renewParkedList, List<SubscriptionPayment> subscriptionPaymentList, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		
		
        for(Subscription parkedSubscription : renewParkedList) {	
        	LOG.info("NextRenewalDate : {}, Date in Time : {}",parkedSubscription.getNextRenewalDate(),parkedSubscription.getNextRenewalDate().getTime());
        	Date currentDate = Utilities.setTimeInDate(new Date(), SubscriptionManagementConstants.FROM_DATE_TIME.get());
        	Date renewalDate = Utilities.setTimeInDate(parkedSubscription.getNextRenewalDate(), SubscriptionManagementConstants.FROM_DATE_TIME.get());
            long duration = currentDate.getTime() - renewalDate.getTime();
            long parkDays = TimeUnit.MILLISECONDS.toDays(duration);
           
        	LOG.info("parkDays : {}",parkDays);
			if(parkDays <= subscriptionParkLimitDays) {
				LOG.info("EPG Call");
				SubscriptionCatalog subscriptionCatalog = autoRenewalSubscriptionCatalog.stream().filter(p -> p.getId().equalsIgnoreCase(parkedSubscription.getSubscriptionCatalogId())).findFirst().get();
				PurchaseRequestDto purchaseRequestDto = subscriptionUtils.populatePurchaseRequestDto(subscriptionCatalog, parkedSubscription, subscriptionPaymentList.stream().filter(s -> parkedSubscription.getId().equals(s.getSubscriptionId())).findFirst().get(), headers);
//				EPGResponse epgResponse = new EPGResponse();
				try {
					if(subscriptionService.epgTransaction(purchaseRequestDto, headers, Utilities.getDateFromSpecificDate(parkedSubscription.getValidityPeriod(), new Date()), resultResponse)) {
						subscriptionDomain.updateSubscriptionForCharging(parkedSubscription, subscriptionCatalog.getCost(), 
								new Date(), Utilities.getDateFromSpecificDate(parkedSubscription.getValidityPeriod(), new Date()), 
								SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(), headers);
//						PurchaseExtraAttributes purchaseExtraAttributes = new PurchaseExtraAttributes(subscriptionCatalog.getSubscriptionSegment(),null,null);
//						PurchaseDomain purchaseDomainToSave = DomainConfiguration.getPurchaseDomain(null, purchaseRequestDto, null, null, headers, purchaseExtraAttributes);		    
//						PurchaseHistory savedHistory = purchaseDomain.saveUpdatePurchaseDetails(purchaseDomainToSave, null, OfferConstants.INSERT_ACTION.get(),headers);
					} else {						
						LOG.info("EPG Failed to Renew Parked Subscription : {}",parkedSubscription.getId());
						LOG.info("RenewalDate : {} , CurrentDate : {}",renewalDate, currentDate);
						if(parkDays == 7) {
							subscriptionDomain.updateSubscriptionStatus(parkedSubscription, SubscriptionManagementConstants.CANCELLED_STATUS.get(), headers);
						} else {
							subscriptionDomain.updateSubscriptionStatus(parkedSubscription, SubscriptionManagementConstants.PARKED_STATUS.get(), headers);
						}
					}
				} catch (Exception e) {
					LOG.info("EPG Failed for renew park subscription : {}",e.getMessage());					
					e.printStackTrace();
				}					
			} else {
				subscriptionDomain.updateSubscriptionStatus(parkedSubscription, SubscriptionManagementConstants.CANCELLED_STATUS.get(), headers);					
			}
			
		}			
	}
	
	public SubscriptionCatalog parkSubscription(List<Subscription> autoSubscriptionList, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		List<Subscription> subscriptionList = new ArrayList<>();
		for(Subscription subscription : autoSubscriptionList) {
			if(subscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {
				subscriptionList.add(subscription);
			}
		}
		if(subscriptionList.size() == 1) {
			subscriptionDomain.updateSubscriptionStatus(subscriptionList.get(0), SubscriptionManagementConstants.PARKED_STATUS.get(), headers);				
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PARKED.getId(), SubscriptionManagementCode.SUBSCRIPTION_PARKED.getMsg());
			resultResponse.setSuccessAPIResponse();	
			return subscriptionCatalogValidator.fetchSubscriptionCatalogById(subscriptionList.get(0).getSubscriptionCatalogId());

		} else if (subscriptionList.isEmpty()){
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.MULTIPLE_SUBSCRIPTION_FOUND.getIntId(),
					SubscriptionManagementCode.MULTIPLE_SUBSCRIPTION_FOUND.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		}
		return null;
	}
	
	public SubscriptionCatalog activateSubscription(List<Subscription> autoSubscriptionList, Headers headers, ManageSubscriptionRequestDto manageSubscriptionRequestDto, ResultResponse resultResponse) throws SubscriptionManagementException {		
		List<Subscription> subscriptionList = new ArrayList<>();
		Date lastChargedDate =  SubscriptionCatalogValidator.validateDate(manageSubscriptionRequestDto.getLastChargedDate(), resultResponse);
		Date nextRenewalDate = SubscriptionCatalogValidator.validateDate(manageSubscriptionRequestDto.getNextRenewalDate(), resultResponse);
		
		for(Subscription subscription : autoSubscriptionList) {
			if(subscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())
					|| subscription.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {
				subscriptionList.add(subscription);
			}
		}
		if(subscriptionList.size() == 1) {
			if(subscriptionList.get(0).getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())) {
				subscriptionDomain.updateSubscriptionForCharging(subscriptionList.get(0), manageSubscriptionRequestDto.getLastChargedAmount(), lastChargedDate, nextRenewalDate, 
						SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(), headers);				
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_ACTIVATED.getId(), SubscriptionManagementCode.SUBSCRIPTION_ACTIVATED.getMsg());
				resultResponse.setSuccessAPIResponse();	
				return subscriptionCatalogValidator.fetchSubscriptionCatalogById(subscriptionList.get(0).getSubscriptionCatalogId());
			} else if(subscriptionList.get(0).getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get())) {			
				subscriptionDomain.updateSubscriptionForCharging(subscriptionList.get(0), manageSubscriptionRequestDto.getLastChargedAmount(), lastChargedDate, nextRenewalDate,
						SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(), headers);
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATED.getId(), SubscriptionManagementCode.SUBSCRIPTION_UPDATED.getMsg());
				resultResponse.setSuccessAPIResponse();
				return subscriptionCatalogValidator.fetchSubscriptionCatalogById(subscriptionList.get(0).getSubscriptionCatalogId());
			}			
		} else if (subscriptionList.isEmpty()){
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		} else {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.MULTIPLE_SUBSCRIPTION_FOUND.getIntId(),
					SubscriptionManagementCode.MULTIPLE_SUBSCRIPTION_FOUND.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		}
		return null;
	}
	
	public void fetchSubscriptionWithInterest(SubscriptionWithInterestRequestDto subscriptionWithInterestRequestDto, 
			SubscriptionWithInterestResultResponse subscriptionWithInterestResultResponse) {
		LOG.info("fetchSubscriptionWithInterest");
		List<SubscriptionWithInterestResponseDto> subscriptionWithInterest = new ArrayList<>();
		
		if(null != subscriptionWithInterestRequestDto && !subscriptionWithInterestRequestDto.getAccountNumber().isEmpty()) {
			LOG.info("subscriptionWithInterestRequestDto.getAccountNumber() {}",subscriptionWithInterestRequestDto.getAccountNumber());
			
			List<Subscription> subscriptionForAccountsList = subscriptionWithInterestRequestDto.isCallSubscription()
					? subscriptionRepositoryHelper.findSubscriptionForAccountList(subscriptionWithInterestRequestDto.getAccountNumber()) : null;
			LOG.info("Subscription For Accounts List : {}",subscriptionForAccountsList);
			List<InterestEntity> interestEntityList = subscriptionWithInterestRequestDto.isIncludeAccountInterest()
					? interestRepository.findAll() : null;
			LOG.info("Interest Entity List : {}",interestEntityList);
			
			for(String accountNumber : subscriptionWithInterestRequestDto.getAccountNumber()) {
				SubscriptionWithInterestResponseDto subscriptionWithInterestResponseDto = new SubscriptionWithInterestResponseDto();
				
				LOG.info("Request Account Number : {}", accountNumber);								
				subscriptionWithInterestResponseDto.setAccountNumber(accountNumber);
				if(subscriptionWithInterestRequestDto.isCallSubscription()) {
					subscriptionWithInterestResponseDto.setSubscriptionStatus(callSubscriptionForAccount(subscriptionWithInterestRequestDto, subscriptionForAccountsList, accountNumber));
				}
				if(subscriptionWithInterestRequestDto.isIncludeAccountInterest()) {
					subscriptionWithInterestResponseDto.setInterestList(callInterestForAccount(interestEntityList, accountNumber));
				}
								
				subscriptionWithInterest.add(subscriptionWithInterestResponseDto);
			}
			subscriptionWithInterestResultResponse.setSubscriptionWithInterest(subscriptionWithInterest);
			subscriptionWithInterestResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getId(), SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getMsg());
			subscriptionWithInterestResultResponse.setSuccessAPIResponse();							
		}
	}
	
	public void populateSubscriptionInfo(SubscriptionInfoRequestDto subscriptionInfoRequestDto, SubscriptionInfoResultResponse subscriptionInfoResultResponse, Headers headers) throws SubscriptionManagementException {
		LOG.info("Enter populateSubscriptionInfo");
		
		if(null != subscriptionInfoRequestDto && !subscriptionInfoRequestDto.getAccountNumber().isEmpty()) {
			LOG.info("subscriptionInfoRequestDto.getAccountNumber() {}",subscriptionInfoRequestDto.getAccountNumber());
			
			List<Subscription> subscriptionForAccountsList = subscriptionInfoRequestDto.isIncludeSubscribed()
					? subscriptionRepositoryHelper.findSubscriptionForAccountList(subscriptionInfoRequestDto.getAccountNumber()) : null;
			LOG.info("Subscription For Accounts List : {}",subscriptionForAccountsList);
			
			subscriptionForAccountsList = subscriptionUtils.validateSubscriptionExpiry(subscriptionForAccountsList, headers);
			
			List<Subscription> memberSubscriptionList = subscriptionInfoRequestDto.isIncludeBenefits()
					? subscriptionRepository.findByMembershipCodeAndStatus(subscriptionInfoRequestDto.getMembershipCode(), SubscriptionManagementConstants.SUBSCRIBED_STATUS.get()) : null;
			LOG.info("Subscription For Member List : {}",memberSubscriptionList);
					
			List<InterestEntity> interestEntityList = subscriptionInfoRequestDto.isIncludeAccountInterest()
					? interestRepository.findAll() : null;
			LOG.info("Interest Entity List : {}",interestEntityList);
			
			List<SubscriptionInfoResponseDto> subscriptionInfoResponseDto = populateSubscriptionInfoForAccount(subscriptionInfoRequestDto, subscriptionForAccountsList, memberSubscriptionList, interestEntityList);
			
			subscriptionInfoResultResponse.setSubscriptionInfo(subscriptionInfoResponseDto);
			subscriptionInfoResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getId(), SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getMsg());
			subscriptionInfoResultResponse.setSuccessAPIResponse();							
		}
	}
	
	private List<SubscriptionInfoResponseDto> populateSubscriptionInfoForAccount(SubscriptionInfoRequestDto subscriptionInfoRequest, 
			List<Subscription> subscriptionForAccountsList, List<Subscription> memberSubscriptionList, List<InterestEntity> interestEntityList) throws SubscriptionManagementException {
		
		List<SubscriptionInfoResponseDto> subscriptionInfoResponse = new ArrayList<>();
		CachedBenefits cachedbenefits = subscriptionUtils.populateCachedBenefits(subscriptionInfoRequest, memberSubscriptionList);
		
		for(String accountNumber : subscriptionInfoRequest.getAccountNumber()) {
			SubscriptionInfoResponseDto subscriptionInfo = new SubscriptionInfoResponseDto();
			
			List<Subscription> subscribedSubscriptionsForAccount = new ArrayList<>();
			LOG.info("Request Account Number : {}", accountNumber);								
			subscriptionInfo.setAccountNumber(accountNumber);
			if(subscriptionInfoRequest.isIncludeSubscribed()) {
				SubscribedSegmentResponseDto subscribedSegmentResponse = new SubscribedSegmentResponseDto();
					
				subscribedSubscriptionsForAccount = subscriptionForAccountsList.stream().filter(o -> o.getAccountNumber().equals(accountNumber)).collect(Collectors.toList());			
				List<String> subscriptionSegments = subscriptionCatalogValidator.fetchSubscriptionSegment(
						subscribedSubscriptionsForAccount.stream().map(Subscription :: getSubscriptionCatalogId).collect(Collectors.toList()));
				
				subscribedSegmentResponse.setLifestyle(null != subscriptionSegments 
						? subscriptionSegments.contains(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get()) : Boolean.FALSE);
				subscribedSegmentResponse.setFood(null != subscriptionSegments 
						? subscriptionSegments.contains(SubscriptionManagementConstants.SEGMENT_FOOD.get()) : Boolean.FALSE);
				subscribedSegmentResponse.setCombo(null != subscriptionSegments 
						? subscriptionSegments.contains(SubscriptionManagementConstants.SEGMENT_COMBO.get()) : Boolean.FALSE);
				subscriptionInfo.setSubscribed(subscribedSegmentResponse);
			}
			
			if(subscriptionInfoRequest.isIncludeBenefits()) {
				List<BenefitsResponseDto> benefitsResponse = populateBenefits(accountNumber, cachedbenefits, subscriptionInfoRequest, subscriptionInfo, memberSubscriptionList, subscribedSubscriptionsForAccount);
				LOG.info("benefitsResponse :{}",benefitsResponse);
				List<BenefitsResponseDto> mergedBenefitsResponse =
		                new ArrayList<>(benefitsResponse.stream()
		                		.collect(Collectors.toMap(BenefitsResponseDto.GroupOfferType::new, p -> p, BenefitsResponseDto::mergeOfferType)).values());
				LOG.info("mergedBenefitsResponse :{}",mergedBenefitsResponse);
				subscriptionInfo.setBenefits(mergedBenefitsResponse);
			}
			
			if(subscriptionInfoRequest.isIncludeAccountInterest()) {
				subscriptionInfo.setInterestList(callInterestForAccount(interestEntityList, accountNumber));
			}
							
			subscriptionInfoResponse.add(subscriptionInfo);
		}
		return subscriptionInfoResponse;
	}
	
	public List<BenefitsResponseDto> populateBenefits(String accountNumber, CachedBenefits cachedBenefits, SubscriptionInfoRequestDto subscriptionInfoRequestDto, 
			SubscriptionInfoResponseDto subscriptionBenefits, List<Subscription> memberSubscriptionList, List<Subscription> subscribedSubscriptionsForAccount) {
		LOG.info("Enter populateBenefits");
		LOG.info("Elife Benefits : {}", cachedBenefits.getElifeBenefits());
		LOG.info("Primary Benefits : {}", cachedBenefits.getPrimaryBenefits());
		LOG.info("Bogo Benefits : {}", cachedBenefits.getBogoBenefits());
		
		List<BenefitsResponseDto> benefitsResponseDtoList = new ArrayList<>();
				
		if(accountNumber.equals(subscriptionInfoRequestDto.getPrimaryAccount())) {
			LOG.info("populateBenefits : Primary Account");			
			if(subscriptionBenefits.getSubscribed().isLifestyle() || subscriptionBenefits.getSubscribed().isFood()) {
				LOG.info("populateBenefits : subscribed");				
				if(memberSubscriptionList.stream().anyMatch(p -> p.getAccountNumber().equals(accountNumber) && p.getSubscriptionCatalogId().equals(cachedBenefits.getElifeCatalogId()))) {
					if(null != cachedBenefits.getElifeBenefits()) {					
						benefitsResponseDtoList.addAll(cachedBenefits.getElifeBenefits().stream()
								.map(p -> modelMapper.map(p, BenefitsResponseDto.class)).collect(Collectors.toList()));
					}
					
					if(null != cachedBenefits.getBogoBenefits()) {
						benefitsResponseDtoList.addAll(cachedBenefits.getBogoBenefits().stream()
								.map(p -> modelMapper.map(p, BenefitsResponseDto.class)).collect(Collectors.toList()));
					}
														
				} else {
					if(null != cachedBenefits.getElifeBenefits()) {
						benefitsResponseDtoList.addAll(cachedBenefits.getElifeBenefits().stream()
								.map(p -> modelMapper.map(p, BenefitsResponseDto.class)).collect(Collectors.toList()));
					}
					
					if(null != cachedBenefits.getPrimaryBenefits()) {
						benefitsResponseDtoList.addAll(cachedBenefits.getPrimaryBenefits().stream()
								.map(p -> modelMapper.map(p, BenefitsResponseDto.class)).collect(Collectors.toList()));
					}					
				}
			} else {
				if(null != cachedBenefits.getElifeBenefits()) {
					benefitsResponseDtoList.addAll(cachedBenefits.getElifeBenefits().stream()
							.map(p -> modelMapper.map(p, BenefitsResponseDto.class)).collect(Collectors.toList()));
				}
			}
		} else {
			LOG.info("populateBenefits : Non Primary Account");
			if(subscriptionBenefits.getSubscribed().isLifestyle() || subscriptionBenefits.getSubscribed().isFood()) {
				if(null != cachedBenefits.getAccountBenefits() && null !=  cachedBenefits.getAccountBenefits().get(accountNumber)) {
					benefitsResponseDtoList.addAll(cachedBenefits.getAccountBenefits().get(accountNumber).stream()
							.map(p -> modelMapper.map(p, BenefitsResponseDto.class)).collect(Collectors.toList()));
					LOG.info("cachedBenefits.getAccountBenefits().get(accountNumber) : {}", cachedBenefits.getAccountBenefits().get(accountNumber));
				}
				LOG.info("Secondary benefitsResponseDtoList : {}",benefitsResponseDtoList);	
									 
			}
		}
		LOG.info("Exit populateBenefits");
		return benefitsResponseDtoList;
				
	}
	
	public String callSubscriptionForAccount(SubscriptionWithInterestRequestDto subscriptionWithInterestRequestDto, List<Subscription> subscriptionForAccountsList, String accountNumber) {
		if(subscriptionWithInterestRequestDto.isCallSubscription() && null != subscriptionForAccountsList 
				&& subscriptionForAccountsList.stream().anyMatch(o -> o.getAccountNumber().equals(accountNumber))) {
			Optional<Subscription> subscription = subscriptionForAccountsList.stream().filter(o -> o.getAccountNumber().equals(accountNumber)).findFirst();
			if(subscription.isPresent()) {
				LOG.info("Subscription Status : {}", subscription.get().getStatus());
				return "true";
			}					
		}
		return "false";
	}
	
	public List<CategoriesInterestDto> callInterestForAccount(List<InterestEntity> interestEntityList, String accountNumber) {
		List<CategoriesInterestDto> categoriesInterestList = new ArrayList<>();
		if(null != interestEntityList) {
			List<Category> categoryEntity = categotyRepository.findAll();
			
			Optional<CustomerInterestEntity> customerInterestEntity = customerInterestRepository.findByAccountNumber(accountNumber);
			for (InterestEntity interestEntity : interestEntityList) {
				if(!categoryEntity.isEmpty() && categoryEntity.stream().anyMatch(c->c.getCategoryId().equalsIgnoreCase(interestEntity.getCategoryId().getCategoryId()))
						&& categoryEntity.stream().anyMatch(c->c.getCategoryId().equalsIgnoreCase(interestEntity.getSubCategoryId().getCategoryId()))) {
					
					CategoriesInterestDto categoriesInterest = interestDomain.populate(interestEntity,customerInterestEntity,
							categoryEntity.stream().filter(c->c.getCategoryId().equalsIgnoreCase(interestEntity.getCategoryId().getCategoryId())).findAny(),
							categoryEntity.stream().filter(c->c.getCategoryId().equalsIgnoreCase(interestEntity.getSubCategoryId().getCategoryId())).findAny());
					
					categoriesInterestList.add(categoriesInterest);
				}
			}
			LOG.info("Categories Interest List : {}", categoriesInterestList);
		}
		return categoriesInterestList;
	}

	/**
	 * 
	 * @param subscriptionCatalogInputDto
	 * @param resultResponse
	 * @param headers 
	 * @return input request for creation/updating subscription catalog 
	 */
	public SubscriptionCatalogRequestDto validateAndCreateSubscriptionCatalogRequestFromInput(
			SubscriptionCatalogInputDto subscriptionCatalogInputDto, ResultResponse resultResponse, Headers headers) {
		
		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = modelMapper.map(subscriptionCatalogInputDto, SubscriptionCatalogRequestDto.class);
		
		if(!ObjectUtils.isEmpty(subscriptionCatalogRequestDto)
		&& !CollectionUtils.isEmpty(subscriptionCatalogInputDto.getSubscriptionBenefits())) {
			
			subscriptionCatalogRequestDto.setSubscriptionBenefits(new ArrayList<>(subscriptionCatalogInputDto.getSubscriptionBenefits().size()));
			List<String> subCategoryRequestList = getSubCategoryListFromSubscriptionBenefitInput(subscriptionCatalogInputDto.getSubscriptionBenefits());
			List<Category> subCategoryList =  !CollectionUtils.isEmpty(subCategoryRequestList)
					? marketplaceRepositoryHelper.findCategoryByIdListAndProgramCode(subCategoryRequestList, headers.getProgram())
					: null;
			
			LOG.info("subCategoryList : {}", subCategoryList);		
					
			for(SubscriptionBenefitsRequest benefit : subscriptionCatalogInputDto.getSubscriptionBenefits()) {
				
				LOG.info("offer type : {}", benefit.getEligibleOfferTypeId());
				LOG.info("category list : {}", benefit.getEligibleOfferCategoryIds());
				LOG.info("subcategory list : {}", benefit.getEligibleOfferSubCategoryIds());
				SubscriptionBenefits subscriptionBenefit = new SubscriptionBenefits();
				subscriptionBenefit.setEligibleOfferTypeId(benefit.getEligibleOfferTypeId());
				setCategoryAndSubCategoryInSubscriptionCatalogRequest(subscriptionBenefit, benefit, subCategoryList);
				LOG.info("subscriptionBenefit : {}", subscriptionBenefit);
				subscriptionCatalogRequestDto.getSubscriptionBenefits().add(subscriptionBenefit);
				
			}
		
		}
		
		return subscriptionCatalogRequestDto;
	}

	/**
	 * 
	 * @param subscriptionBenefit
	 * @param benefit
	 * @param subCategoryList 
	 */
	private void setCategoryAndSubCategoryInSubscriptionCatalogRequest(SubscriptionBenefits subscriptionBenefit,
			SubscriptionBenefitsRequest benefit, List<Category> subCategoryList) {
		
		if(!CollectionUtils.isEmpty(benefit.getEligibleOfferCategoryIds())) {
			
			subscriptionBenefit.setEligibleOfferCategory(new ArrayList<>(benefit.getEligibleOfferCategoryIds().size()));
			
			for(String category : benefit.getEligibleOfferCategoryIds()) {
				
				LOG.info("category : {}", category);
				EligibleCategories catSubCat = new EligibleCategories();
				catSubCat.setEligibleOfferCategoryId(category);
				
				if(!CollectionUtils.isEmpty(benefit.getEligibleOfferSubCategoryIds())
				&& !CollectionUtils.isEmpty(subCategoryList)) {
					
					catSubCat.setEligibleOfferSubCategoryId(subCategoryList.stream()
							.filter(s->CollectionUtils.containsAny(benefit.getEligibleOfferSubCategoryIds(), Arrays.asList(s.getCategoryId()))
								 && !ObjectUtils.isEmpty(s.getParentCategory())
								 && !StringUtils.isEmpty(s.getParentCategory().getCategoryId())
								 && s.getParentCategory().getCategoryId().equals(category))
							.map(Category::getCategoryId)
							.collect(Collectors.toList()));
					
				}
				
				subscriptionBenefit.getEligibleOfferCategory().add(catSubCat);
			}
			
		}
		
	}

	/**
	 * 
	 * @param subscriptionBenefits
	 * @return list of all subcategories in the request
	 */
	private List<String> getSubCategoryListFromSubscriptionBenefitInput(
			List<SubscriptionBenefitsRequest> subscriptionBenefits) {
		
		List<String> subCategoryList = null;
		
		if(!CollectionUtils.isEmpty(subscriptionBenefits)) {
			
			subCategoryList = new ArrayList<>(1);
							 
			for(SubscriptionBenefitsRequest benefit : subscriptionBenefits) {
				
				if(!CollectionUtils.isEmpty(benefit.getEligibleOfferSubCategoryIds())) {
					
					subCategoryList.addAll(benefit.getEligibleOfferSubCategoryIds());
				}
				
			}
			
		}
		
		return subCategoryList;
	}
	
	/**
	 * 
	 * @param id
	 * @param resultResponse 
	 * @return status to indicate subscription catalog request has unique id 
	 */
	public boolean checkUniqueSubscriptionCatalogId(String id, ResultResponse resultResponse) {
		
		boolean checkStatus = true;
	
		if(!StringUtils.isEmpty(id)) {
			
			checkStatus = !subscriptionRepositoryHelper.checkUniqueSubscriptionCatalog(id);
			
			if(!checkStatus) {
				
				resultResponse.addErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_CATALOG_ID_DUPLICATE.getIntId(), SubscriptionManagementCode.SUBSCRIPTION_CATALOG_ID_DUPLICATE.getMsg());
				
			}
			
		}
		
		return checkStatus;
	}
	
	/**
	 * 
	 * @param headers
	 * @param subscriptionRenewalResultResponse 
	 * @return list of subscription renewal
	 */
	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_RENEWAL_REPORT)
	public void sendSubscriptionRenewalReport(Headers headers) {
		
		LOG.info("======================= Entering sendSubscriptionRenewalReport ========================");
		List<SubscriptionRenewalResponseDto> renewalList = null;
		List<EPGTransaction> epgTransactionList = epgTransactionRepository.findByExternalTransactionId(headers.getExternalTransactionId());
		
		if(!CollectionUtils.isEmpty(epgTransactionList)) {
			
			List<String> subscriptionIdList = epgTransactionList.stream().map(EPGTransaction::getSubscriptionId)
					.collect(Collectors.toList());
			List<Subscription> subscriptionList = subscriptionRepositoryHelper.findSubscriptionListForIdList(subscriptionIdList); 
			renewalList = new ArrayList<>(epgTransactionList.size());
			SubscriptionRenewalResponseDto renewalResponseDto = null;
				
			for(EPGTransaction epgTransaction:epgTransactionList) {
					
				renewalResponseDto = new SubscriptionRenewalResponseDto();
				renewalResponseDto.setSubscriptionId(epgTransaction.getSubscriptionId());
				renewalResponseDto.setAccountNumber(epgTransaction.getAccountNumber());
				renewalResponseDto.setAmount(Double.valueOf(epgTransaction.getAmount()));
				renewalResponseDto.setSubscriptionCatalogId(epgTransaction.getSubscriptionCatalogId());
				renewalResponseDto.setEpgTransactionId(epgTransaction.getEpgTransactionId());
				renewalResponseDto.setMasterEpgTransactionId(epgTransaction.getMasterEPGTransactionId());
				renewalResponseDto.setEpgTransactionStatus(epgTransaction.getEpgStatus());
				renewalResponseDto.setEpgResponse(epgTransaction.getResponseClassDescription());
				renewalResponseDto.setExternalTransactionId(epgTransaction.getExternalTransactionId());
				if(!CollectionUtils.isEmpty(subscriptionList)) {
					
					Subscription subscriptionCurrent = subscriptionList.stream().filter(s->s.getId().equals(epgTransaction.getSubscriptionId()))
							.findFirst().orElse(null);
					
					if(null!=subscriptionCurrent) {
						renewalResponseDto.setSubscriptionStatus(subscriptionCurrent.getStatus());
					}
					
				}
				renewalList.add(renewalResponseDto);
				
			}
			LOG.info("Subscription Renewal list fetched successfully");
		}
		
		if(!CollectionUtils.isEmpty(renewalList)) {
			
			prepareFileAndSendEmail(renewalList);
		}
		
		LOG.info("======================= Exiting sendSubscriptionRenewalReport ========================");
	}

	/**
	 * 
	 * @param subscriptionRenewalList
	 * @return location of renewal file saved
	 * @throws FileNotFoundException 
	 */
	private void prepareFileAndSendEmail(List<SubscriptionRenewalResponseDto> subscriptionRenewalList)  {
		
		List<String[]> dataLines = new ArrayList<>(subscriptionRenewalList.size());
		String header[] = {
				SubscriptionManagementConstants.EXTERNAL_TRANSACTION_ID.get(),
				SubscriptionManagementConstants.ACCOUNT_NUMBER.get(),
				SubscriptionManagementConstants.SUBSCRIPTION_ID.get(),
				SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_ID.get(),
				SubscriptionManagementConstants.SUBSCRIPTION_STATUS.get(),
				SubscriptionManagementConstants.AMOUNT.get(), 
				SubscriptionManagementConstants.MASTER_EPG_TRANSCATION_ID.get(),
				SubscriptionManagementConstants.EPG_TRANSACTION_ID.get(),
				SubscriptionManagementConstants.EPG_STATUS.get(),
				SubscriptionManagementConstants.EPG_RESPONSE.get()};
		dataLines.add(header);
		subscriptionRenewalList.forEach(renewalResponse->
			dataLines.add(new String[] {
					renewalResponse.getExternalTransactionId(),
					renewalResponse.getAccountNumber(),
					renewalResponse.getSubscriptionId(),
					renewalResponse.getSubscriptionCatalogId(),
					renewalResponse.getSubscriptionStatus(),
					renewalResponse.getAmount().toString(),
					renewalResponse.getMasterEpgTransactionId(),
					renewalResponse.getEpgTransactionId(),
					renewalResponse.getEpgTransactionStatus(),
					renewalResponse.getEpgResponse()})
		);	
		
		//Writing renewal report to csv file and saving it to physical location
		String fileName = SubscriptionManagementConstants.RENEWAL_FILE_PREFIX.get()+Utilities.getDateInStringFormat(null, SubscriptionManagementConstants.TRANSACTIONS_DATE_FORMAT.get())+SubscriptionManagementConstants.CSV_EXTENSION.get();
		String location = renewalReportSavingLocation + fileName;
		
		try (CSVWriter writer = new CSVWriter(new FileWriter(location))){
			dataLines.forEach(d-> writer.writeNext(d));
			writer.flush();
		    LOG.info("Subscription renewal report file save to physical location");
		    emailRenewalReport(location, fileName);
		} catch (Exception e) {
			
			e.printStackTrace();
			LOG.error("Error occured in saving the file for subscription renewal : {} ", e.getMessage());
		} 
	     
	    
	}
	
	/**
	 * 
	 * @param fileLocation
	 */
	private void emailRenewalReport(String fileLocation, String fileName) {
		
		if(!StringUtils.isEmpty(fileLocation)
		&& !CollectionUtils.isEmpty(renewalReportEmailList)) {
			
			for(String email : renewalReportEmailList) {
				
				EmailRequestDto emailRequestDto = new EmailRequestDto();
				emailRequestDto.setEmailId(email);
				emailRequestDto.setTemplateId(renewalReportEmailTemplateId);
				emailRequestDto.setNotificationId(renewalReportEmailNotificationId);
				emailRequestDto.setLanguage(SubscriptionManagementConstants.ENGLISH_LANGUAGE.get());
				
				final Map<String, String> additionalParameters = new HashMap<>();
				additionalParameters.put(SubscriptionManagementConstants.FILE_PATH.get(), fileLocation);
				additionalParameters.put(SubscriptionManagementConstants.FILE_NAME.get(), fileName);
				additionalParameters.put(SubscriptionManagementConstants.REPORT_NAME.get(), SubscriptionManagementConstants.SUBSCRIPTION_RENEWAL_REPORT.get());
				additionalParameters.put(SubscriptionManagementConstants.FILE_TYPE.get(), SubscriptionManagementConstants.CSV.get());
				emailRequestDto.setAdditionalParameters(additionalParameters);
				
				eventHandler.publishEmail(emailRequestDto);
				
			}
			
			LOG.info("Sent emails for subscription renewal report");
		}
		
	}
	
	public void fetchCuisines(CuisinesResultResponse cuisinesResultResponse) throws SubscriptionManagementException{
		
		try {
			List<Cuisines> cuisinesList = cuisinesRepository.findAll();
			if(ObjectUtils.isEmpty(cuisinesList)) {
				cuisinesResultResponse.setErrorAPIResponse(SubscriptionManagementCode.CUISINES_LIST_EMPTY.getIntId(),
						SubscriptionManagementCode.CUISINES_LIST_EMPTY.getMsg());
				cuisinesResultResponse.setResult(SubscriptionManagementCode.CUISINES_LISTING_FAILED.getId(),
						SubscriptionManagementCode.CUISINES_LISTING_FAILED.getMsg());
			} else {
				List<CuisinesResponseDto> cuisinesResponseDtoList = new ArrayList<>();
				for(Cuisines cuisines : cuisinesList) {
					CuisinesResponseDto cuisinesResponseDto = new CuisinesResponseDto();
					cuisinesResponseDto.setCuisinesId(cuisines.getCuisineId());
					cuisinesResponseDto.setCuisineNameEn(cuisines.getCuisineName().getCuisineNameEn());
					cuisinesResponseDto.setCuisineNameAr(cuisines.getCuisineName().getCuisineNameAr());
					cuisinesResponseDtoList.add(cuisinesResponseDto);
				}
				cuisinesResultResponse.setCuisines(cuisinesResponseDtoList);
				cuisinesResultResponse.setResult(SubscriptionManagementCode.CUISINES_LISTED_SUCCESSFULLY.getId(),
						SubscriptionManagementCode.CUISINES_LISTED_SUCCESSFULLY.getMsg());
			}
		} catch (Exception e) {	
			e.printStackTrace();
			LOG.info("Exception occured in fetchCuisines()");
			throw new SubscriptionManagementException(this.getClass().toString(), "fetchCuisines",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}			
	}
	
	public void changeStatusForCoolingPeriod(List<SubscriptionCatalog> autoRenewalSubscriptionCatalog, Date renewDate, Headers headers) throws SubscriptionManagementException {
//		List<SubscriptionCatalog> autoRenewalSubscriptionCatalog = subscriptionCatalogDomain.findByChargeabilityType(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get());
		if(null != autoRenewalSubscriptionCatalog && !autoRenewalSubscriptionCatalog.isEmpty()) {
			renewDate = Utilities.setTimeInDate(renewDate, SubscriptionManagementConstants.END_DATE_TIME.get());
			LOG.info("renewDate : {}",renewDate);
			List<Subscription> renewSubscriptionList = subscriptionRepositoryHelper.findSubscriptionsListForAutoRenewal(autoRenewalSubscriptionCatalog.stream().map(SubscriptionCatalog::getId).collect(Collectors.toList()), renewDate);
			if(!CollectionUtils.isEmpty(renewSubscriptionList)) {
				
				for(Subscription subscribedSubscription : renewSubscriptionList) {
					LOG.info("AccountNumber : {}, NextRenewalDate : {}",subscribedSubscription.getAccountNumber(),subscribedSubscription.getNextRenewalDate());
					if((SubscriptionUtils.isTodayDate(subscribedSubscription.getNextRenewalDate(), new Date())) && ObjectUtils.isNotEmpty(subscribedSubscription.getEndDate())) {
						LOG.info("CancellingSubscription for AccountNumber : {}, NextRenewalDate : {}",subscribedSubscription.getAccountNumber(),subscribedSubscription.getNextRenewalDate());
						subscribedSubscription.setStatus(SubscriptionManagementConstants.CANCELLED_STATUS.get());
						subscribedSubscription.setUpdatedDate(new Date());
						subscribedSubscription.setUpdatedUser(headers.getUserName());
					}
					subscriptionRepository.save(subscribedSubscription);	
				}
			}
		
		}		
		
	}
	
	public GiftApiResponse populateEligibleGiftDetails(GiftApiRequest validatePromoCodeRequest, Boolean isBinBased, PromoCodeApplyAndValidate resultResponse,
			GiftApiResponse giftApiResponse, Headers headers) throws SubscriptionManagementException, MarketplaceException {
		
		List<PromoCode> promoCodeEntityList = new ArrayList<>();
		Optional<SubscriptionCatalog> subscriptionCatalog = Optional.empty();
		Optional<SubscriptionCatalog> subscribedSubscriptionCatalog = Optional.empty();
		Optional<Subscription> subscription = Optional.empty();
		
		GetMemberResponse getMemberResponse = fetchServiceValues.getMemberDetails(validatePromoCodeRequest.getAccountNumber().get(0), giftApiResponse, headers);
		LOG.info("getMemberResponse : {}", getMemberResponse);
		if(null==getMemberResponse) {
			giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.MEMBER_DETAILS_NOT_FOUND.getIntId() ,SubscriptionManagementCode.MEMBER_DETAILS_NOT_FOUND.getMsg());
			return giftApiResponse;
		}
		
		if(!isBinBased) {
			promoCodeEntityList = promoCodeRepository.findByPromoCode(validatePromoCodeRequest.getPromoCodeDetails().getPromoCode());
			if(ObjectUtils.isEmpty(promoCodeEntityList)) {
				giftApiResponse.setErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_DETAILS_NOT_FOUND.getIntId(),
						PromoCodeErrorCodes.PROMOCODE_DETAILS_NOT_FOUND.getMsg());
				return giftApiResponse;
			}
			if(!ObjectUtils.isEmpty(promoCodeEntityList) && !ObjectUtils.isEmpty(promoCodeEntityList.get(0)) && !ObjectUtils.isEmpty(promoCodeEntityList.get(0).getSubcscriptions()) && !ObjectUtils.isEmpty(promoCodeEntityList.get(0).getSubcscriptions().get(0))) {
				subscriptionCatalog = subscriptionCatalogDomain.findByIdAndStatus(promoCodeEntityList.get(0).getSubcscriptions().get(0),SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
			}
			if(!ObjectUtils.isEmpty(promoCodeEntityList) && !ObjectUtils.isEmpty(promoCodeEntityList.get(0)) && !ObjectUtils.isEmpty(promoCodeEntityList.get(0).getPromoCode()) && subscriptionCatalog.isPresent()) {
				resultResponse =  promoCodeDomain.applyPromoCode(promoCodeEntityList.get(0).getPromoCode(), subscriptionCatalog.get().getId(), null, subscriptionCatalog.get().getCost(), 
						subscriptionCatalog.get().getPointsValue(), validatePromoCodeRequest.getAccountNumber().get(0), 0, headers, null, giftApiResponse);
			}
		} else {
			subscriptionCatalog = subscriptionCatalogDomain.findByIdAndStatus(validatePromoCodeRequest.getSubscriptionCatalogId() ,SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
		}
		
		if(subscriptionCatalog.isPresent()) {
			subscription = subscriptionRepository.findByAccountNumberAndStatusInAndSubscriptionSegment(
					validatePromoCodeRequest.getAccountNumber().get(0),
					Arrays.asList(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(),SubscriptionManagementConstants.PARKED_STATUS.get()),
					subscriptionCatalog.get().getSubscriptionSegment());
		}
		
		if(subscription.isPresent()) {
			subscribedSubscriptionCatalog = subscriptionCatalogDomain.findByIdAndStatus(subscription.get().getSubscriptionCatalogId() ,SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
		}
		
		
		if(subscription.isPresent() && subscription.get().getStatus().equalsIgnoreCase(SubscriptionManagementConstants.PARKED_STATUS.get())) {
			
			giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_PARKED.getIntId(),
					SubscriptionManagementCode.ACCOUNT_PARKED.getMsg());
			return giftApiResponse;
			
		} else if(subscriptionCatalog.isPresent() && subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			if((!isBinBased && promoCodeEntityList.get(0).getPromoType().getType().equalsIgnoreCase("Free Duration")) || isBinBased) {
				if(!subscription.isPresent()) {
					giftApiResponse = populateCatalogDetails(validatePromoCodeRequest.getAccountNumber().get(0) , subscriptionCatalog.get(), 
								promoCodeEntityList, isBinBased, resultResponse, giftApiResponse, getMemberResponse, headers);
				} else if(subscribedSubscriptionCatalog.isPresent() && subscribedSubscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
					giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.REJECT_AUTO_WHEN_ONE_TIME_SUBSCRIBED.getIntId(),SubscriptionManagementCode.REJECT_AUTO_WHEN_ONE_TIME_SUBSCRIBED.getMsg()+subscription.get().getEndDate());
					return giftApiResponse;
				} else if(subscribedSubscriptionCatalog.isPresent() && subscribedSubscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
					giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getIntId(),SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getMsg());
					return giftApiResponse;	
				} else {
					giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_EXISTS.getIntId(),SubscriptionManagementCode.SUBSCRIPTION_EXISTS.getMsg());
					return giftApiResponse;
				}
			} else {
				giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.AUTORENEWABLE_ELIGIBLE_PROMOTYPE.getIntId(), SubscriptionManagementCode.AUTORENEWABLE_ELIGIBLE_PROMOTYPE.getMsg());
				return giftApiResponse;
			}
			
		} else if(subscriptionCatalog.isPresent() && subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
			if(!subscription.isPresent() || (subscription.isPresent() && subscribedSubscriptionCatalog.isPresent()
					&& !subscribedSubscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(
							SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get()))) {
				giftApiResponse = populateCatalogDetails(validatePromoCodeRequest.getAccountNumber().get(0) , subscriptionCatalog.get(), 
							promoCodeEntityList, isBinBased, resultResponse, giftApiResponse, getMemberResponse, headers);
			} else {
				giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getIntId(),SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getMsg());
				return giftApiResponse;
			}
			
		} else if(subscriptionCatalog.isPresent() && subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
			if((!isBinBased && !promoCodeEntityList.get(0).getPromoType().getType().equalsIgnoreCase("Free Duration")) || isBinBased) {
				if(!subscription.isPresent() || (subscription.isPresent() && subscribedSubscriptionCatalog.isPresent()
						&& !subscribedSubscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get()))) {

					giftApiResponse = populateCatalogDetails(validatePromoCodeRequest.getAccountNumber().get(0), subscriptionCatalog.get(),
								promoCodeEntityList, isBinBased, resultResponse, giftApiResponse, getMemberResponse, headers);
						
				} else {
					giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getIntId(),SubscriptionManagementCode.LIFE_TIME_SUBSCRIBED.getMsg());
					return giftApiResponse;
				}
			} else {
				giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.ONETIME_NOT_ELIGIBLE_PROMO_TYPE.getIntId(),SubscriptionManagementCode.ONETIME_NOT_ELIGIBLE_PROMO_TYPE.getMsg());
				return giftApiResponse;
			}
		}
		return giftApiResponse;
	}
	
	public GiftApiResponse populateCatalogDetails(String accountNumber, SubscriptionCatalog subscriptionCatalog, List<PromoCode> promoCodeEntityList, Boolean isBinBased,
			PromoCodeApplyAndValidate resultResponse, GiftApiResponse giftApiResponse, GetMemberResponse getMemberResponse, Headers headers) throws MarketplaceException, SubscriptionManagementException {
		LOG.info("Entering populateCatalogDetails");
		List<GiftDetails> giftDetailsList = new ArrayList<>();
		GiftDetails giftDetails = new GiftDetails();
		List<SubscriptionCatalogResponseDto> subscriptionCatalogDetailsList = new ArrayList<>();
		SubscriptionCatalogResponseDto subscriptionCatalogDetails = new SubscriptionCatalogResponseDto();
		PromoCodeApply promoCodeApply = resultResponse.getPromoCodeApply();
		ValidPromoCodeDetails validPromoCodeDetails = resultResponse.getValidatePromoCodeResponse();
		
		PointsValue pointsValue = new PointsValue();
		pointsValue.setActual(subscriptionCatalog.getPointsValue());
		pointsValue.setDiscounted(subscriptionCatalog.getPointsValue());
		Cost cost = new Cost();
		cost.setActual(subscriptionCatalog.getCost());
		cost.setDiscounted(subscriptionCatalog.getCost());
		
		if(!isBinBased && promoCodeApply.getStatus() == 0) {
			pointsValue.setDiscounted(promoCodeApply.getPointsValue());
			cost.setDiscounted(promoCodeApply.getCost());
		}
		
		LOG.info("subscriptionCatalog : {}",subscriptionCatalog);
		subscriptionCatalogDetails = modelMapper.map(subscriptionCatalog, SubscriptionCatalogResponseDto.class);
		subscriptionCatalogDetails.setLoyaltyPointsValue(pointsValue);	
		subscriptionCatalogDetails.setLoyaltyCost(cost);
		subscriptionCatalogDetails.setCost(null);
		subscriptionCatalogDetails.setPointsValue(null);
		LOG.info("subscriptionCatalogDetails : {}",subscriptionCatalogDetails);
//		populateEligibleSubscriptionCatalog(subscriptionCatalog, getMemberResponse, headers, subscriptionCatalogDetailsList, 
//				subscriptionCatalogDetails, validPromoCodeDetails.getApiStatus().getStatusCode() == 0 ? true : false);
//		
//		LOG.info("subscriptionCatalogDetailsList : {}", subscriptionCatalogDetailsList);
//		if(ObjectUtils.isEmpty(subscriptionCatalogDetailsList)) {
//			giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_GIFT.getIntId(),SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_GIFT.getMsg());
//			return giftApiResponse;
//		}
		
//		giftDetails.setSubscriptionCatalog(subscriptionCatalogDetailsList);		
//		giftDetailsList.add(giftDetails);
		
		
		if(isBinBased) {
			populateEligibleSubscriptionCatalog(subscriptionCatalog, getMemberResponse, headers, subscriptionCatalogDetailsList, 
					subscriptionCatalogDetails, true);
			LOG.info("subscriptionCatalogDetailsList : {}", subscriptionCatalogDetailsList);
			if(ObjectUtils.isEmpty(subscriptionCatalogDetailsList)) {
				giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_GIFT.getIntId(),SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_GIFT.getMsg());
				return giftApiResponse;
			}
			giftDetails.setSubscriptionCatalog(subscriptionCatalogDetailsList);		
			giftDetailsList.add(giftDetails);
			giftApiResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY.getId(),SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY.getMsg());
			giftApiResponse.setGiftDetails(giftDetailsList);
		} else {
			
			if(!ObjectUtils.isEmpty(validPromoCodeDetails) && !ObjectUtils.isEmpty(validPromoCodeDetails.getApiStatus()) && null!=validPromoCodeDetails.getApiStatus().getStatusCode() && validPromoCodeDetails.getApiStatus().getStatusCode() == 0) {
				populateEligibleSubscriptionCatalog(subscriptionCatalog, getMemberResponse, headers, subscriptionCatalogDetailsList, 
						subscriptionCatalogDetails, validPromoCodeDetails.getApiStatus().getStatusCode() == 0 ? true : false);
				LOG.info("subscriptionCatalogDetailsList : {}", subscriptionCatalogDetailsList);
				if(ObjectUtils.isEmpty(subscriptionCatalogDetailsList)) {
					giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_GIFT.getIntId(),SubscriptionManagementCode.ACCOUNT_NOT_ELIGIBLE_FOR_GIFT.getMsg());
					return giftApiResponse;
				}
				giftDetails.setSubscriptionCatalog(subscriptionCatalogDetailsList);		
				giftDetailsList.add(giftDetails);
				giftApiResponse.setResult(SubscriptionManagementCode.GIFT_DETAILS_FETCHED_SUCCESSFULLY.getId(),SubscriptionManagementCode.GIFT_DETAILS_FETCHED_SUCCESSFULLY.getMsg());
				PromoDetails promoDetails = validPromoCodeDetails.getPromoDetails();
				promoDetails.setPromoTypeDescription(promoCodeEntityList.get(0).getPromoType().getType());;
				giftApiResponse.setPromoDetails(promoDetails);
				giftApiResponse.setGiftDetails(giftDetailsList);		
			} else {
				giftApiResponse.setErrorAPIResponse(validPromoCodeDetails.getApiStatus().getErrors().get(0).getCode(),validPromoCodeDetails.getApiStatus().getErrors().get(0).getMessage());
			}
		}
		LOG.info("Exiting populateCatalogDetails");	
		return giftApiResponse;
		
		
	}
	
}



