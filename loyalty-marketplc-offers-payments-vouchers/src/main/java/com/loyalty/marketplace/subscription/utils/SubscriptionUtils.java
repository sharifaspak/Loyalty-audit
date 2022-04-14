package com.loyalty.marketplace.subscription.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.domain.LinkedOffers;
import com.loyalty.marketplace.subscription.domain.MerchantsList;
import com.loyalty.marketplace.subscription.domain.PaymentMethods;
import com.loyalty.marketplace.subscription.domain.SubscriptionCatalogDomain;
import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
import com.loyalty.marketplace.subscription.domain.Title;
import com.loyalty.marketplace.subscription.helper.dto.RenewalValues;
import com.loyalty.marketplace.subscription.inbound.dto.CachedBenefits;
import com.loyalty.marketplace.subscription.inbound.dto.CachedCatalogBenefits;
import com.loyalty.marketplace.subscription.inbound.dto.CachedValues;
import com.loyalty.marketplace.subscription.inbound.dto.ManageRenewalRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.ManageSubscriptionRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.NotificationValues;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionInfoRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.ValidDates;
import com.loyalty.marketplace.subscription.outbound.database.entity.Cuisines;
import com.loyalty.marketplace.subscription.outbound.database.entity.PaymentMethodDetails;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPayment;
import com.loyalty.marketplace.subscription.outbound.database.repository.CuisinesRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepositoryHelper;
import com.loyalty.marketplace.subscription.outbound.dto.CuisinesResponseDto;
import com.loyalty.marketplace.subscription.service.SubscriptionService;
import com.loyalty.marketplace.utils.MarketplaceException;

@Component
public class SubscriptionUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionUtils.class);
	
	@Value("#{'${etisalat.cutomer.types}'.split(',')}")
	private List<String> etisalatCustomerTypes;
	
	//@Value("#{'${non.dcb.customerType}'.split(',')}")
    	private List<String> nonDCBCustomerType = Arrays.asList("ELIFE","VISITOR");
	
	@Autowired
	SubscriptionCatalogDomain subscriptionCatalogDomain;
	
	@Autowired
	SubscriptionDomain subscriptionDomain;
	
	@Autowired
	SubscriptionCatalogRepository subscriptionCatalogRepository;
	
	@Autowired
	SubscriptionRepositoryHelper subscriptionRepositoryHelper;
	
	@Autowired
	SubscriptionCatalogValidator subscriptionCatalogValidator;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	CuisinesRepository cuisinesRepository;
	
	public void initializeCachedValues(CachedValues cachedValues) {
		cachedValues.setPaymentRequired(true);
		cachedValues.setLifeTimeFree(false);
		cachedValues.setNonExistingMemberFree(false);
		cachedValues.setSubscriptionAction(null);
		cachedValues.setSubscribedCatalog(new SubscriptionCatalog());
		cachedValues.setSubscribedSubscription(new Subscription());
	}
	
	public static Date calculateEndDate(String startDate, int validityPeriod, ResultResponse resultResponse) throws SubscriptionManagementException {
		List<String> formatStrings = Arrays.asList("dd/MM/yyyy", "dd-MM-yyyy");
	    for (String formatString : formatStrings) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
	            Calendar c = Calendar.getInstance();
	            c.setTime(sdf.parse(startDate));
	            
	            c.add(Calendar.DAY_OF_MONTH, validityPeriod);
	    		String endDate = sdf.format(c.getTime());
	    		
	    		return SubscriptionCatalogValidator.validateDate(endDate, resultResponse);
	        }
	        catch (ParseException e) {
	        	throw new SubscriptionManagementException("SubscriptionCatalogValidator", "validateDate",
						e.getClass() + e.getMessage(), SubscriptionManagementCode.INVALID_DATE);
	        }
	    }
	    return null;
	}
	
	public String formatStringDate(String inputDateString) {
		List<String> formatStrings = Arrays.asList("E MMM dd HH:mm:ss Z yyyy", "yyyy-MM-dd HH:mm:ss.SSS'Z'", 
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
		for (String formatString : formatStrings) {
			try {
				DateFormat formatter = new SimpleDateFormat(formatString);
				Date date = (Date)formatter.parse(inputDateString);
				String renewalDate = new SimpleDateFormat("dd-MMM-yyyy").format(date);
				return renewalDate;
			} catch (ParseException e) {
				System.out.println("Try next format");
			}
		}
		return null;
	}
	
	public String convertDateToGstTimeZone(Date date) {
		try {
			LOG.info("Inside convertDateToGstTimeZone");
			date = (Utilities.setGstTimeZone(date, SubscriptionManagementConstants.SIMPLE_DATE_FORMAT.get()));
			SimpleDateFormat formatter = new SimpleDateFormat(SubscriptionManagementConstants.DATE_FORMAT_FOR_EMAIL.get());
			return formatter.format(date);
		} catch (ParseException e) {
			LOG.error("Error in SubscriptionUtils:covertedDateToGstTimeZone {} ",e);
		}
		return null;
	}
	
	public ValidDates calculateValidDates(PurchaseRequestDto purchaseRequestDto, CachedValues cachedValues, SubscriptionCatalog subscriptionCatalog, ResultResponse resultResponse) {	
		ValidDates validDates = new ValidDates();
		try {
			int validityPeriod = (null != purchaseRequestDto.getSubscriptionMethod() 
					&& purchaseRequestDto.getSubscriptionMethod().equalsIgnoreCase(SubscriptionManagementConstants.WELCOME_GIFT.get()))
					? purchaseRequestDto.getFreeSubscriptionDays()
							: subscriptionCatalog.getValidityPeriod();
			LOG.info("calculateValidDates :: validityPeriod : {}",validityPeriod);
			String startDate = null != purchaseRequestDto.getStartDate() ? purchaseRequestDto.getStartDate() : 
				new SimpleDateFormat(SubscriptionManagementConstants.SIMPLE_DATE_FORMAT.get()).format(new Date());
			validDates.setValidStartDate(SubscriptionCatalogValidator.validateDate(startDate, resultResponse));
											
			Date validEndDate = (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())
					|| cachedValues.isLifeTimeFree())
				? null : calculateEndDate(startDate, validityPeriod, resultResponse);
			validDates.setValidEndDate(validEndDate);
			
			LOG.info("calculateValidDates :: Valid Dates : {} :: {}",validDates.getValidStartDate(),validDates.getValidEndDate());
		} catch (SubscriptionManagementException sme) {
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "calculateValidDates",
					sme.getClass() + sme.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		return validDates;
	}
	
	public ValidDates calculateValidDatesforCoBrandedCard(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, ResultResponse resultResponse) {	
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
				? null : calculateEndDate(startDate, validityPeriod, resultResponse);
			validDates.setValidEndDate(validEndDate);
			LOG.info("calculateValidDates :: StartDate : {} :: EndDate : {}",validDates.getValidStartDate(),validDates.getValidEndDate());
		} catch (SubscriptionManagementException sme) {
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "calculateValidDates",
					sme.getClass() + sme.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		return validDates;
	}
	
	public Date calculateEndDateForCoBranded(PurchaseRequestDto purchaseRequestDto, Subscription subscription) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(subscription.getEndDate());
		LOG.info("Present End Date : {}",cal.getTime());
		if(purchaseRequestDto.getCardType().equalsIgnoreCase(SubscriptionManagementConstants.CARD_TYPE_PLATINUM.get())) {
			cal.add(Calendar.DAY_OF_MONTH, 365);
		} else if (purchaseRequestDto.getCardType().equalsIgnoreCase(SubscriptionManagementConstants.CARD_TYPE_SIGNATURE.get())) {
			cal.add(Calendar.DAY_OF_MONTH, 365);
		}
		LOG.info("Updated End Date : {}",cal.getTime());
		
		return cal.getTime();
		
	}
	
	public Date calculateEndDateForExtension(Date endDate, int validityPeriod) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		LOG.info("extendEndDateForSuscription :: Present End Date : {}",cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, validityPeriod);
		LOG.info("extendEndDateForSuscription :: Updated End Date : {}",cal.getTime());
		
		return cal.getTime();
		
	}
	
	public List<Title> categoryToTitle(List<Category> availableOfferCategoryList) {
		List<Title> titleList = new ArrayList<>();
		for(Category category : availableOfferCategoryList) {
			Title title = new Title();
			title.setId(category.getCategoryId());
			title.setEnglish(category.getCategoryName().getCategoryNameEn());
			title.setArabic(category.getCategoryName().getCategoryNameAr());
			titleList.add(title);
		}
		return titleList;
	}
	
	public boolean evaluateCancelCondition(ManageSubscriptionRequestDto manageSubscriptionRequestDto, Subscription subscription, Headers headers) {
		LOG.info("headers : ",headers);
		return (!ObjectUtils.isEmpty(manageSubscriptionRequestDto) && manageSubscriptionRequestDto.isAllowAction())				
				|| headers.getUserName().equalsIgnoreCase(SubscriptionManagementConstants.TIBCO_SYSTEM.get())
				|| headers.getUserName().equalsIgnoreCase(SubscriptionManagementConstants.CRM_JOB.get())
				|| (subscriptionCatalogValidator.fetchChargeabilityType(subscription.getSubscriptionCatalogId()).equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())
						&& headers.getChannelId().equalsIgnoreCase("RTF"))
				|| ((headers.getChannelId().equalsIgnoreCase("RTF") || headers.getChannelId().equalsIgnoreCase("CWOM")) 
						&& (subscription.getSubscriptionCatalogId().equalsIgnoreCase(SubscriptionManagementConstants.FOOD_FREE_ULTRA.get())))
				|| (headers.getUserName().equalsIgnoreCase(SubscriptionManagementConstants.ACCOUNT_CANCEL_EVENT.get())
						&& (subscriptionCatalogValidator.fetchChargeabilityType(subscription.getSubscriptionCatalogId()).equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())
							|| subscriptionCatalogValidator.fetchChargeabilityType(subscription.getSubscriptionCatalogId()).equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get()))
						&& !headers.getChannelId().equalsIgnoreCase(SubscriptionManagementConstants.ADMIN_PORTAL.get()))
						//Food subscription Drop2 Condition for handling cancellation  of autorenewables in case of cc to skip phonytunes call
				/*|| (subscription.getPaymentMethod().equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())
								&& !headers.getChannelId().equalsIgnoreCase(SubscriptionManagementConstants.ADMIN_PORTAL.get()))*/;
	}
	
	public List<PaymentMethods> validateRetrievePaymentMethods(List<String> selectedPaymentMethods, ResultResponse resultResponse) {
		List<PaymentMethods> paymentMethodsList = new ArrayList<>();
		List<PaymentMethod> validPaymentMethods = paymentMethodRepository.findByProgramCodeIgnoreCase("smiles");
		
		for(String selectedPaymentMethod : selectedPaymentMethods) {
			if(validPaymentMethods.stream().anyMatch(p -> p.getPaymentMethodId().equalsIgnoreCase(selectedPaymentMethod))) {
				PaymentMethods paymentMethods = new PaymentMethods();
				paymentMethods.setPaymentMethodId(selectedPaymentMethod);
				Optional<PaymentMethod> paymentMethod = validPaymentMethods.stream().filter(p -> p.getPaymentMethodId().equalsIgnoreCase(selectedPaymentMethod)).findFirst();
				if(paymentMethod.isPresent()) {
					paymentMethods.setDescription(paymentMethod.get().getDescription());
				}
			
				paymentMethodsList.add(paymentMethods);
			} else {
				resultResponse.addErrorAPIResponse(SubscriptionManagementCode.INVALID_PAYMENT_METHOD.getIntId(),
						SubscriptionManagementCode.INVALID_PAYMENT_METHOD.getMsg()+" : "+selectedPaymentMethod);
			}
		}
        return paymentMethodsList;
	}
	
	public boolean isEnterpriseAndCrmAddOnServiceDisabled(GetMemberResponse getMemberResponse, Headers headers) throws SubscriptionManagementException {
		List<String> customerType = etisalatCustomerTypes.stream().filter(getMemberResponse.getCustomerType()::contains).collect(Collectors.toList());
		LOG.info("isEnterpriseAndCrmAddOnServiceDisabled :: Customer Type : {}",customerType);
		return (null != customerType && customerType.contains(SubscriptionManagementConstants.ENTERPRISE_CUSTOMER_TYPE_MOBILE.get()) 
				&& subscriptionService.isCrmAddOnServiceDisabled(getMemberResponse.getAccountNumber(), headers));
	}
	
	public boolean hasAvailedFreeDuration(String accountNumber) {		
		return subscriptionRepositoryHelper.findFreeDurationForAccount(accountNumber) != null;
	}
	
	public boolean hasAvailedFreeDuration(String accountNumber, String subscriptionSegment) {		
		return subscriptionRepositoryHelper.findFreeDurationForAccountAndSegment(accountNumber, subscriptionSegment) != null;
	}
	
	public List<LinkedOffers> validateRetrieveLinkedOffers(List<String> linkedOffersRequest, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {
		
		List<LinkedOffers> availableOffers = subscriptionService.listOffersResponse(headers.getUserName(), headers.getToken(), resultResponse);
		
		LOG.info("availableOffers : {}", availableOffers);
		HashMap<String, LinkedOffers> linkedOfferMap = new HashMap<>();
		for (LinkedOffers availableOffer : availableOffers) {
			linkedOfferMap.put(availableOffer.getId(), availableOffer);
		}
		
		List<LinkedOffers> linkedOfferList = null;
		
		if(!CollectionUtils.isEmpty(linkedOffersRequest)) {
			
			linkedOfferList = new ArrayList<>();
			
			for(String linkedOfferRequestId : linkedOffersRequest) {
				LOG.info("linkedOfferRequestId : {}",linkedOfferRequestId);
				if(availableOffers.stream().map(LinkedOffers::getId).collect(Collectors.toList()).contains(linkedOfferRequestId)) {
					linkedOfferList.add(linkedOfferMap.get(linkedOfferRequestId));
				} else {
					resultResponse.addErrorAPIResponse(SubscriptionManagementCode.INVALID_OFFER.getIntId(),
		    				SubscriptionManagementCode.INVALID_OFFER.getMsg()+" "+linkedOfferRequestId);
				}
			}
		}
		LOG.info("linkedOfferList : {}", linkedOfferList);
        return linkedOfferList;
    }
	
	public CachedBenefits populateCachedBenefits(SubscriptionInfoRequestDto subscriptionInfoRequestDto, List<Subscription> memberSubscriptionList) throws SubscriptionManagementException {
		LOG.info("Enter populateCachedBenefits :: memberSubscriptionList : {}", memberSubscriptionList);
		CachedBenefits cachedBenefits = new CachedBenefits();		
		if(subscriptionInfoRequestDto.isIncludeBenefits() && (!memberSubscriptionList.isEmpty() && null != memberSubscriptionList)) {
			populateElifeBenefits(cachedBenefits, subscriptionInfoRequestDto, memberSubscriptionList);
			
			if(!cachedBenefits.isElifePrimary()) {
				populatePrimaryBenefits(cachedBenefits, subscriptionInfoRequestDto, memberSubscriptionList);
			}
											
			populateBogoBenefits(cachedBenefits, memberSubscriptionList);
			populateAccountBenefits(cachedBenefits, subscriptionInfoRequestDto, memberSubscriptionList);
		}
		LOG.info("Exit populateCachedBenefits : {}", cachedBenefits);
		return cachedBenefits;
	}
	
	public void populateElifeBenefits(CachedBenefits cachedBenefits, SubscriptionInfoRequestDto subscriptionInfoRequestDto, List<Subscription> memberSubscriptionList) throws SubscriptionManagementException {
		SubscriptionCatalog elifeCatalog = subscriptionCatalogDomain.findByPackageType(SubscriptionManagementConstants.PACKAGE_TYPE_ELIFE.get());
		if(null != elifeCatalog) {
			LOG.info("elifeCatalog Benefits : {}", elifeCatalog.getBenefits());
			cachedBenefits.setElifePrimary(memberSubscriptionList.stream().anyMatch(p -> p.getSubscriptionCatalogId().equalsIgnoreCase(elifeCatalog.getId())
					&& p.getAccountNumber().equals(subscriptionInfoRequestDto.getPrimaryAccount())));
			cachedBenefits.setElifeCatalogId(elifeCatalog.getId());
			
			List<CachedCatalogBenefits> elifeBenefits = new ArrayList<>();
			if(memberSubscriptionList.stream().anyMatch(p -> p.getSubscriptionCatalogId().equalsIgnoreCase(elifeCatalog.getId()))) {
				elifeBenefits.addAll(elifeCatalog.getBenefits().stream().map(p -> modelMapper.map(p, CachedCatalogBenefits.class)).collect(Collectors.toList()));
				cachedBenefits.setElifeBenefits(elifeBenefits);
			}			
			LOG.info("Elife Benefits : {}", cachedBenefits.getElifeBenefits());
		}		
	}
	
	public void populatePrimaryBenefits(CachedBenefits cachedBenefits, SubscriptionInfoRequestDto subscriptionInfoRequestDto, List<Subscription> memberSubscriptionList) throws SubscriptionManagementException {		
		List<Subscription> primarySubscription = memberSubscriptionList.stream().filter(p -> p.getAccountNumber().equals(subscriptionInfoRequestDto.getPrimaryAccount())).collect(Collectors.toList());
		if(!primarySubscription.isEmpty()) {
			List<String> primaryCatalogId = primarySubscription.stream().map(Subscription::getSubscriptionCatalogId).collect(Collectors.toList());
			List<SubscriptionCatalog> primaryCatalogList = subscriptionCatalogDomain.findSubscriptionCatalog(primaryCatalogId);
			List<CachedCatalogBenefits> primaryBenefits = new ArrayList<>();
			Map<String,String> catalogMap = new HashMap<String,String>();
			for(SubscriptionCatalog primaryCatalog : primaryCatalogList) {
				if(null != primaryCatalog.getBenefits() && !primaryCatalog.getBenefits().isEmpty()) {
					LOG.info("primaryCatalog Benefits : {}", primaryCatalog.getBenefits());
					primaryBenefits.addAll(primaryCatalog.getBenefits().stream().map(p -> modelMapper.map(p, CachedCatalogBenefits.class)).collect(Collectors.toList()));
					catalogMap.put(primaryCatalog.getSubscriptionSegment(), primaryCatalog.getId());
//					cachedBenefits.setPrimaryCatalogId(primaryCatalog.getId());
//					cachedBenefits.setPrimaryBenefits(primaryBenefits);
				}
			}
			cachedBenefits.setPrimaryCatalogIdMap(catalogMap);
			cachedBenefits.setPrimaryBenefits(primaryBenefits);
			
			LOG.info("Primary Benefits : {}", cachedBenefits.getPrimaryBenefits());
		}
	}
	
	public void populateBogoBenefits(CachedBenefits cachedBenefits, List<Subscription> memberSubscriptionList) throws SubscriptionManagementException {
		
		List<Subscription> distinctSubscriptionList = memberSubscriptionList.stream().filter(SubscriptionUtils.distinctByKey(Subscription::getSubscriptionCatalogId)).collect(Collectors.toList());
		if(distinctSubscriptionList != null && !distinctSubscriptionList.isEmpty()) {
			List<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogDomain
					.findSubscriptionCatalog(distinctSubscriptionList.stream().map(Subscription::getSubscriptionCatalogId).collect(Collectors.toList()));
			LOG.info("subscriptionCatalog List : {}", subscriptionCatalog);
			
			if(subscriptionCatalog != null && !subscriptionCatalog.isEmpty()) {
				subscriptionCatalog.removeIf(p -> p.getId().equals(cachedBenefits.getElifeCatalogId()) || 
						(!ObjectUtils.isEmpty(cachedBenefits.getPrimaryCatalogIdMap()) && cachedBenefits.getPrimaryCatalogIdMap().containsValue(p.getId())));
				List<CachedCatalogBenefits> bogoBenefits = new ArrayList<>();
				for(SubscriptionCatalog bogoCatalog : subscriptionCatalog) {
					LOG.info("bogoCatalog Benefits : {}", bogoCatalog.getBenefits());
					if(null != bogoCatalog.getBenefits()) {			
						bogoBenefits.addAll(bogoCatalog.getBenefits().stream().map(p -> modelMapper.map(p, CachedCatalogBenefits.class)).collect(Collectors.toList()));				
					}
				}
				cachedBenefits.setBogoBenefits(bogoBenefits);
				LOG.info("Bogo Benefits : {}", cachedBenefits.getBogoBenefits());
			}
		}		
	}
	
	public void populateAccountBenefits(CachedBenefits cachedBenefits,  SubscriptionInfoRequestDto subscriptionInfoRequestDto, List<Subscription> memberSubscriptionList) throws SubscriptionManagementException {
		LOG.info("Entering populateAccountBenefits");
		Map<String,List<CachedCatalogBenefits>> accountBenefits = new HashMap<>();
		List<Subscription> subscribedSubscriptionsForAccount = new ArrayList<>();
		
		List<SubscriptionCatalog> subscriptionCatalogList = subscriptionCatalogDomain
				.findSubscriptionCatalog(memberSubscriptionList.stream().map(Subscription::getSubscriptionCatalogId).collect(Collectors.toList()));
		LOG.info("subscriptionCatalogList : {}",subscriptionCatalogList);
		for(String accountNumber : subscriptionInfoRequestDto.getAccountNumber()) {
			List<CachedCatalogBenefits> benefits = new ArrayList<>();
			subscribedSubscriptionsForAccount = memberSubscriptionList.stream().filter(o -> o.getAccountNumber().equals(accountNumber)).collect(Collectors.toList());			
			LOG.info("subscribedSubscriptionsForAccount : {}",subscribedSubscriptionsForAccount);
			if(!ObjectUtils.isEmpty(subscribedSubscriptionsForAccount)) {
				List<String> accountSubscriptionCatalogId = subscribedSubscriptionsForAccount.stream().map(Subscription :: getSubscriptionCatalogId).collect(Collectors.toList());
				LOG.info("accountSubscriptionCatalogId : {}",accountSubscriptionCatalogId);
				if(!ObjectUtils.isEmpty(accountSubscriptionCatalogId) && !ObjectUtils.isEmpty(subscriptionCatalogList)) {
					for(SubscriptionCatalog subscriptionCatalog : subscriptionCatalogList) {
						if(accountSubscriptionCatalogId.contains(subscriptionCatalog.getId())) {
							benefits.addAll(subscriptionCatalog.getBenefits().stream()
									.map(p -> modelMapper.map(p, CachedCatalogBenefits.class)).collect(Collectors.toList()));
						}
					}
				}
			}
			accountBenefits.put(accountNumber, benefits);
			LOG.info("accountBenefits : {}",accountBenefits);
		}
		LOG.info("accountBenefits outside for loop: {}",accountBenefits);
		cachedBenefits.setAccountBenefits(accountBenefits);
		LOG.info("cachedBenefits.getAccountBenefits : {}",cachedBenefits.getAccountBenefits());
		LOG.info("Exiting populateAccountBenefits");
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	@Transactional(transactionManager = "mongoTransactionManager", propagation=Propagation.REQUIRES_NEW)
	public void subscriptionNotification(Headers headers, SubscriptionCatalog subscriptionCatalog, NotificationValues notificationValues, ResultResponse resultResponse) {
		try {
			if(notificationValues.isNotifyOneTimeSubscription()) {
				LOG.info("Inside NotifyOneTimeSubscription block");
				subscriptionService.notifyMemberBySMS(Arrays.asList(notificationValues.getAccountNumber()),notificationValues.getPromoCode(), headers.getExternalTransactionId());
			}
			
			if(notificationValues.isNotifyAutoRenewalSubscription()) {
				LOG.info("Inside NotifyAutoRenewalSubscription block");
				GetMemberResponse getMemberResponse = fetchServiceValues.getMemberDetails(notificationValues.getAccountNumber(), resultResponse, headers);
				if( null != getMemberResponse) {
                    notificationValues.setUiLanguage(getMemberResponse.getUiLanguage());
                } else {
                    notificationValues.setUiLanguage(SubscriptionManagementConstants.English);
                }
                //triggering SMS and Email for autorenewal subscription
                subscriptionService.notifyMemberBySMSForAutoRenewalSubscription(Arrays.asList(notificationValues.getAccountNumber()), headers.getExternalTransactionId(), subscriptionCatalog, notificationValues );
                if( null != getMemberResponse) {
                	subscriptionService.notifyMemberByEmailForAutoRenewalSubscription(notificationValues, headers.getExternalTransactionId(), getMemberResponse, subscriptionCatalog);
                }
			}
		} catch (MarketplaceException me) {
			exceptionLogService.saveExceptionsToExceptionLogs(me, headers.getExternalTransactionId(), notificationValues.getAccountNumber(), headers.getUserName());
			me.printStackTrace();
			LOG.error(SubscriptionManagementCode.EMAIL_ERROR.getMsg());
			resultResponse.addErrorAPIResponse(SubscriptionManagementCode.EMAIL_ERROR.getIntId(), SubscriptionManagementCode.EMAIL_ERROR.getMsg());
		} catch (Exception e) {
			exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), notificationValues.getAccountNumber(), headers.getUserName());
			e.printStackTrace();
			LOG.error(SubscriptionManagementCode.EMAIL_ERROR.getMsg());
			resultResponse.addErrorAPIResponse(SubscriptionManagementCode.EMAIL_ERROR.getIntId(), SubscriptionManagementCode.EMAIL_ERROR.getMsg());
		}
		LOG.info("Inside subscriptionNotification ExternalTransacrionId:{}",headers.getExternalTransactionId());

	}
	
	public boolean validateManadatoryFieldsForCC(ManageRenewalRequestDto renewalPaymentMethodRequestDto, ResultResponse resultResponse) {
		if(null != renewalPaymentMethodRequestDto.getEpgTransactionID() && !renewalPaymentMethodRequestDto.getEpgTransactionID().isEmpty() &&
				null != renewalPaymentMethodRequestDto.getMasterEPGTransactionId() && !renewalPaymentMethodRequestDto.getMasterEPGTransactionId().isEmpty() &&
				null != renewalPaymentMethodRequestDto.getCcDetails().getCardNumber() && !renewalPaymentMethodRequestDto.getCcDetails().getCardNumber().isEmpty() &&
				null != renewalPaymentMethodRequestDto.getCcDetails().getSubType() && !renewalPaymentMethodRequestDto.getCcDetails().getSubType().isEmpty()) {
			return true;
			
		} else {
			resultResponse.addErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_CCDETAILS_MISSING.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_CCDETAILS_MISSING.getMsg());
			return false;
		}
	}
	
	public boolean validatePaymentForNonDcb(String paymentMethod,  String customerType, ResultResponse result ) {
		LOG.info("nonDCBCustomerType : {}",nonDCBCustomerType);
		boolean valid = true;
		if((paymentMethod.equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_ATB.get())
				|| paymentMethod.equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_DCB_DFB.get()))
				&& nonDCBCustomerType.contains(customerType)) {
			result.addErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_INELIGIBLE_FOR_PAYMENT_METHOD.getIntId(),
					SubscriptionManagementCode.ACCOUNT_INELIGIBLE_FOR_PAYMENT_METHOD.getMsg());
			valid = false;
		}
		return valid;
	}
	
	public PurchaseRequestDto populatePurchaseRequestDto(SubscriptionCatalog subscriptionCatalog, Subscription subscription, SubscriptionPayment subscriptionPayment, Headers headers) {
		PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
		purchaseRequestDto.setSpentAmount(subscriptionCatalog.getCost());
		purchaseRequestDto.setMembershipCode(subscription.getMembershipCode());
		purchaseRequestDto.setAdditionalParams(subscription.getId());
		purchaseRequestDto.setAccountNumber(subscription.getAccountNumber());
		purchaseRequestDto.setSubscriptionCatalogId(subscription.getSubscriptionCatalogId());
		Optional<PaymentMethodDetails> paymentMethodDetails = subscriptionPayment.getPaymentMethodDetails().stream()
				.filter(p -> p.getStatus().equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())).findFirst();
		if(null != paymentMethodDetails && !ObjectUtils.isEmpty(paymentMethodDetails) 
				&& null != paymentMethodDetails.get().getCcDetails() && !ObjectUtils.isEmpty(paymentMethodDetails.get().getCcDetails())) {
			purchaseRequestDto.setMasterEPGTransactionId(paymentMethodDetails.get().getMasterEPGTransactionId());
			purchaseRequestDto.setCardNumber(paymentMethodDetails.get().getCcDetails().getCardNumber());
			purchaseRequestDto.setCardSubType(paymentMethodDetails.get().getCcDetails().getSubType());		
			purchaseRequestDto.setSelectedPaymentItem(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVITY_CODE.get());
			purchaseRequestDto.setPaymentType(SubscriptionManagementConstants.PAYMENT_TYPE.get());			
			LOG.info("purchaseRequestDto: {}", purchaseRequestDto);		
		}
		return purchaseRequestDto;
		
	}

	
	
	public RenewalValues populateRenewalValues(SubscriptionCatalog subscriptionCatalog, int duration) {
		
		RenewalValues  renewalValues = new RenewalValues();		
		Date nextRenewalDate;
		if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			if(duration != 0) {
				nextRenewalDate = Utilities.getDateFromSpecificDate(duration, new Date());
				nextRenewalDate = Utilities.setTimeInDate(nextRenewalDate, SubscriptionManagementConstants.UTC_DATE_TIME.get());
				renewalValues.setNextRenewalDate(nextRenewalDate); 
			} 
			else {
				nextRenewalDate = Utilities.getDateFromSpecificDate(subscriptionCatalog.getValidityPeriod(), new Date());
				nextRenewalDate = Utilities.setTimeInDate(nextRenewalDate, SubscriptionManagementConstants.UTC_DATE_TIME.get());
				renewalValues.setNextRenewalDate(nextRenewalDate);
				renewalValues.setLastChargedDate(new Date());
				renewalValues.setLastChargedAmount(subscriptionCatalog.getCost());
			}
		}
		return renewalValues;
	}
	
	public List<Subscription> validateSubscriptionExpiry(List<Subscription> subscriptionForAccountsList, Headers headers) throws SubscriptionManagementException {
		Date date = new Date();
		List<Subscription> cancelledSubscription = new ArrayList<>();
		if(null != subscriptionForAccountsList) {
			List<String> catalogIdList = subscriptionForAccountsList.stream().map(Subscription::getSubscriptionCatalogId).collect(Collectors.toList());
			Map<String,String> catalogChargeabilityMap = subscriptionCatalogDomain.fetchChargeabilityType(catalogIdList);
			LOG.info("catalogChargeabilityMap : {}", catalogChargeabilityMap);
			for(Subscription subscription :  subscriptionForAccountsList) {
				if(catalogChargeabilityMap.get(subscription.getSubscriptionCatalogId()).equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
					date = Utilities.setTimeInDate(date, SubscriptionManagementConstants.END_DATE_TIME_ONE_TIME.get());
					if(null != subscription.getEndDate() && subscription.getEndDate().compareTo(date)<=0) {
						LOG.info("compared dates value : {}",subscription.getEndDate().compareTo(date));
						LOG.info("Today Date : {}",date);
						LOG.info("EndDate : {}",subscription.getEndDate());
						cancelledSubscription.add(subscription);
						subscriptionDomain.updateSubscriptionStatus(subscription, SubscriptionManagementConstants.CANCELLED_STATUS.get(), headers);
					}	
				} else if(catalogChargeabilityMap.get(subscription.getSubscriptionCatalogId()).equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
					date = Utilities.setTimeInDate(date, SubscriptionManagementConstants.UTC_DATE_TIME.get());
					LOG.info("Status : {}",subscription.getStatus());
					if(null != subscription.getNextRenewalDate() && subscription.getNextRenewalDate().compareTo(date)<=0) {
						LOG.info("comparedates value : {}",subscription.getNextRenewalDate().compareTo(date));
						LOG.info("Today Date : {}",date);
						LOG.info("NextRenewalDate : {}",subscription.getNextRenewalDate());
						cancelledSubscription.add(subscription);
						subscriptionDomain.updateSubscriptionStatus(subscription, SubscriptionManagementConstants.PARKED_STATUS.get(), headers);
					}
				}
			}
		}
		LOG.info("cancelledSubscription : {}",cancelledSubscription);
		subscriptionForAccountsList.removeAll(cancelledSubscription);
		LOG.info("subscriptionForAccountsList after removing expired renewalsubscriptions: {}", subscriptionForAccountsList);
		return subscriptionForAccountsList;
	}

	public List<MerchantsList> validateRetrieveMerchants(List<String> selectedMerchantsList, ResultResponse resultResponse) {
		List<MerchantsList> merchantsList = new ArrayList<>();
		List<Merchant> validMerchants = merchantRepository.findByProgramIgnoreCase("smiles");
		
		for(String selectedMerchants : selectedMerchantsList) {
			if(validMerchants.stream().anyMatch(p -> p.getMerchantCode().equalsIgnoreCase(selectedMerchants))) {
				MerchantsList merchants = new MerchantsList();
				merchants.setMerchantCode(selectedMerchants);
				Optional<Merchant> merchant = validMerchants.stream().filter(p -> p.getMerchantCode().equalsIgnoreCase(selectedMerchants)).findFirst();
				if(merchant.isPresent()) {
					merchants.setMerchantNameEn(merchant.get().getMerchantName().getMerchantNameEn());
					merchants.setMerchantNameAr(merchant.get().getMerchantName().getMerchantNameAr());
				}
				merchantsList.add(merchants);
			} else {
				resultResponse.addErrorAPIResponse(SubscriptionManagementCode.INVALID_MERCHANT_CODE.getIntId(),
						SubscriptionManagementCode.INVALID_MERCHANT_CODE.getMsg()+" : "+selectedMerchants);
			}
		}
        return merchantsList;
	}
	
	public List<CuisinesResponseDto> validateRetrieveCuisines(List<String> selectedCuisinesList, ResultResponse resultResponse) {
		List<CuisinesResponseDto> cuisinesList = new ArrayList<>();
		List<Cuisines> validCuisines = cuisinesRepository.findAll();
		
		for(String selectedCuisines : selectedCuisinesList) {
			if(validCuisines.stream().anyMatch(p -> p.getCuisineId().equalsIgnoreCase(selectedCuisines))) {
				CuisinesResponseDto cuisines = new CuisinesResponseDto();
				cuisines.setCuisinesId(selectedCuisines);
				Optional<Cuisines> cuisine = validCuisines.stream().filter(p -> p.getCuisineId().equalsIgnoreCase(selectedCuisines)).findFirst();
				if(cuisine.isPresent()) {
					cuisines.setCuisineNameEn(cuisine.get().getCuisineName().getCuisineNameEn());
					cuisines.setCuisineNameAr(cuisine.get().getCuisineName().getCuisineNameAr());
				}
				cuisinesList.add(cuisines);
			} else {
				resultResponse.addErrorAPIResponse(SubscriptionManagementCode.INVALID_CUISINE_CODE.getIntId(),
						SubscriptionManagementCode.INVALID_CUISINE_CODE.getMsg()+" : "+selectedCuisines);
			}
		}
        return cuisinesList;
	}


	public boolean checkFutureDate(Date nextRenewalDate) {
//		String formatStrings = "yyyy-MM-dd HH:mm:ss.SSS'Z'";
		
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 0);
			// Current Date in format :: date Tue Feb 22 17:21:22 IST 2022
			Date currentDate = cal.getTime();  
//			String nextRenewalDateStringFormat = new SimpleDateFormat(formatStrings).format(nextRenewalDate);
			//Next Renewal Date in current date format
//			Date nextRenewalDateFormat = new SimpleDateFormat(formatStrings).parse(nextRenewalDateStringFormat);
			
			if(nextRenewalDate.after(currentDate))
				return true;
			
			} catch (Exception e) {
			System.out.println("Try next format");
			}
		return false;
	}
	
	public static boolean isTodayDate(Date nextRenewalDate, Date currentDate) {
        if (nextRenewalDate == null || currentDate == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(nextRenewalDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentDate);
        return isTodayDate(cal1, cal2);
    }
	
	public static boolean isTodayDate(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
	
	
	public int diffBetweenDates(Date date1) throws ParseException {
		/*Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		Format formatter = new SimpleDateFormat("dd/MMM/yyyy");
		String s1 = formatter.format(date);
		Date todayDate = ((DateFormat) formatter).parse(s1);
		todayDate.setHours(4); int leftOverDays = 0;
		leftOverDays = (int) ((date1.getTime() - todayDate.getTime())/ (1000*60*60*24));
		return leftOverDays; */ Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		String s1 = formatter.format(date);
		Date todayDate = formatter.parse(s1);
		String s2 = formatter.format(date1);
		Date tomorrowDate = formatter.parse(s2);
		int leftOverDays = 0;
		leftOverDays = (int) ((tomorrowDate.getTime() - todayDate.getTime())/ (1000*60*60*24));
		return leftOverDays;
	}


}