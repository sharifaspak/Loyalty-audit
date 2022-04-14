package com.loyalty.marketplace.offers.inbound.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.gifting.helper.GiftingControllerHelper;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersListConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.domain.model.OfferCatalogDomain;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.AmountPoints;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.ExceptionInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.member.management.outbound.dto.BirthdayAccountsDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ParentChlidCustomer;
import com.loyalty.marketplace.offers.outbound.database.repository.AccountOfferCounterRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.OfferValidator;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.CFMService;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.dto.CFMRequestDto;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementControllerHelper;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;

import io.swagger.annotations.Api;

/**
 * 
 * @author jaya.shukla
 *
 */
@RestController
@Api(value = RequestMappingConstants.MARKETPLACE)
@RequestMapping(RequestMappingConstants.MARKETPLACE_BASE)
public class TestingServicesController {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestingServicesController.class);
	
	@Autowired
	FetchServiceValues fetchServiceValues;

	@Autowired
	PurchaseRepository purchaseRepository;
	
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Autowired
	OffersHelper helper;
	
	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	OfferCatalogDomain offerCatalogDomain;
	
	@Autowired
	AccountOfferCounterRepository accountOfferCounterRepository;
	
	@Autowired
	SubscriptionManagementControllerHelper subscriptionManagementControllerHelper;
	
	@Autowired
	CFMService cmfservice;

	@Autowired
	ExceptionLogsService exceptionLogService;
	

	@Value(OffersConfigurationConstants.IS_BATCH_TOGGLE)
	private boolean batchToggle;
		
	//Changes for loyalty as a service.
	@Value(OffersConfigurationConstants.DEFAULT_PROGRAM_CODE)
	private String defaultProgramCode;
	

	@Autowired
	private GiftingControllerHelper giftingControllerHelper;
	
	@GetMapping(value = "/getCustomerTypes", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParentChlidCustomer> getAllCustomerTypes(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) throws MarketplaceException{
       
		ResultResponse resultResponse = new ResultResponse(OfferConstants.EMPTY_CHARACTER.get()); 
		return fetchServiceValues.getAllCustomerTypes(resultResponse, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
        
    }
		
	@GetMapping(value = "/memberDetails", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  GetMemberResponse getMemberDetails(
    		@RequestHeader String accountNumber,
    		@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) throws MarketplaceException{
       
		ResultResponse resultResponse = new ResultResponse(OfferConstants.EMPTY_CHARACTER.get());
		return fetchServiceValues.getMemberDetails(accountNumber, resultResponse, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId));
    }
	
	@GetMapping(value = "/conversionRateList", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  List<ConversionRate> checkConversionRateList(
    		@RequestHeader(required = true) String partnerCode,
    		@RequestHeader(required = true) String item,
    		@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId){
       
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		return helper.fetchConversionRateList(partnerCode, item, channelId, resultResponse);
		 
	}
	
	@GetMapping(value = "/pointsEq", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  String getEquivalentPoint(
    		@RequestHeader(required = true) String partnerCode,
    		@RequestHeader(required = true) String item,
    		@RequestHeader(required = true) Double amount,
    		@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId){
       
	      ResultResponse resultResponse = new ResultResponse(externalTransactionId);
	      List<ConversionRate> conversionRateList = helper.fetchConversionRateList(partnerCode, item, channelId, resultResponse);
	      
	      return "Equivalent Points : "+ ProcessValues.getEquivalentPoints(conversionRateList, amount);
		
		
	}
	
	@GetMapping(value = "/amountEq", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEquivalentAmount(
    		@RequestHeader(required = true) String partnerCode,
    		@RequestHeader(required = true) String item,
    		@RequestHeader(required = true) Integer points,
    		@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId){
       
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		List<ConversionRate> conversionRateList = helper.fetchConversionRateList(partnerCode, item, channelId, resultResponse);
	    
		return "Equivalent Amount : " + ProcessValues.getEquivalentAmount(conversionRateList, points, resultResponse);
		
	}
	
	@GetMapping(value = "/goldEq", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AmountPoints getEquivalentAmount(
    		@RequestHeader(required = true) Double balance,
    		@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId){
       
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		return helper.getGoldCertificateAmountOrPoints(balance, channelId, resultResponse); 
		
	}
	
	@GetMapping(value = "/constantValues", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getConstantValues(
    		@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId){
       
		List<String> list = new ArrayList<>(1);
		list.add("Cinema Rule = " + OffersConfigurationConstants.cinemaRule);
		list.add("Standard Global Limit = " + OffersConfigurationConstants.standardGlobalLimit);
		list.add("Standard Account Limit = " + OffersConfigurationConstants.standardAccountLimit);
		list.add("Standard Member Limit = " + OffersConfigurationConstants.standardMemberLimit);
		list.add("Subscriber Global Limit = " + OffersConfigurationConstants.subscriberGlobalLimit);
		list.add("Subscriber Account Limit = " + OffersConfigurationConstants.subscriberAccountLimit);
		list.add("Subscriber Member Limit = " + OffersConfigurationConstants.subscriberMemberLimit);
		list.add("Special Global Limit = " + OffersConfigurationConstants.specialGlobalLimit);
		list.add("Special Account Limit = " + OffersConfigurationConstants.specialAccountLimit);
		list.add("Coms Channel = " + OffersConfigurationConstants.coms);
    	list.add("Rtf Channel = " + OffersConfigurationConstants.rtf);
    	list.add("Emcais Channel = " + OffersConfigurationConstants.emcais);
    	list.add("Rbt Channel = " + OffersConfigurationConstants.rbt);
    	list.add("PhonyTunes Channel = " + OffersConfigurationConstants.phonyTunes);
    	list.add("Portal Web = " + OffersConfigurationConstants.sweb);
		list.add("Portal App = "+ OffersConfigurationConstants.sapp);
		list.add("Purchase Item Discount Voucher = " + OffersConfigurationConstants.discountVoucherItem);
		list.add("Purchase Item Cash Voucher = "+ OffersConfigurationConstants.cashVoucherItem);
		list.add("Purchase Item Deal Voucher = "+ OffersConfigurationConstants.dealVoucherItem);
		list.add("Purchase Item Add on = "+ OffersConfigurationConstants.addOnItem);
		list.add("Purchase Item Bill payment = "+ OffersConfigurationConstants.billPaymentItem);
		list.add("Purchase Item Recharge = "+ OffersConfigurationConstants.rechargeItem);
		list.add("Purchase Item Subscription = "+ OffersConfigurationConstants.subscriptionItem);
		list.add("Purchase Item Gold Certificate = "+ OffersConfigurationConstants.goldCertificateItem);
		list.add("Non eligible Segment = "+ OffersConfigurationConstants.nonEligibleCustomerSegment);
		list.add("Standard Segment = "+ OffersConfigurationConstants.standardCustomerSegment);
		list.add("Subscriber Segment = "+ OffersConfigurationConstants.subscriberCustomerSegment);
		list.add("Special Segment = "+ OffersConfigurationConstants.specialCustomerSegment);
		list.addAll(OffersListConstants.CUSTOMER_SEGMENTS);
		list.add("Offer Type Discount Voucher ="+ OffersConfigurationConstants.discountOfferType);
		list.add("Offer Type Cash Voucher ="+ OffersConfigurationConstants.cashOfferType);
		list.add("Offer Type Deal Voucher ="+ OffersConfigurationConstants.dealOfferType);
		list.add("Offer Type Etisalat Add On ="+ OffersConfigurationConstants.addOnOfferType);
		list.add("Offer Type Gold Certificate ="+ OffersConfigurationConstants.goldCertificateOfferType);
		list.add("Offer Type Telcom ="+ OffersConfigurationConstants.telecomOfferType);
		list.add("Offer Type Other ="+ OffersConfigurationConstants.otherOfferType);
		list.add("Offer Type Lifestyle Offer ="+ OffersConfigurationConstants.lifestyleOfferType);
		list.add("Offer Type WelcomeGift ="+ OffersConfigurationConstants.welcomeGiftOfferType);
		list.add("Language English ="+ OffersConfigurationConstants.languageEn);
		list.add("Language Arabic ="+ OffersConfigurationConstants.languageAr);
		list.add("Birthday Notification Id ="+ OffersConfigurationConstants.birthdayNotificationTemplateId);
		list.add("Birthday Notification Code ="+ OffersConfigurationConstants.birthdayNotificationCode);
		list.add("Birthday Sms Template Id ="+ OffersConfigurationConstants.birthdaySmsTemplateId);
		list.add("Birthday Sms Notification Id ="+ OffersConfigurationConstants.birthdaySmsNotificationId);
		list.add("Birthday Sms Notification Code ="+ OffersConfigurationConstants.birthdaySmsNotificationCode);
		list.add("Bogo Subscription Notification Id ="+ OffersConfigurationConstants.bogoSubscriptionNotificationId);
		list.add("Bogo Subscription Notification Code ="+ OffersConfigurationConstants.bogoSubscriptionNotificationCode);
		list.add("Discount Voucher Product Item ="+ OffersConfigurationConstants.discountVoucherProductItem);
		list.add("Cash Voucher Product Item ="+ OffersConfigurationConstants.cashVoucherProductItem);
		list.add("Deal Voucher Product Item ="+ OffersConfigurationConstants.dealVoucherProductItem);
		list.add("Add On Product Item ="+ OffersConfigurationConstants.addOnProductItem);
		list.add("Bill Payment Product Item ="+ OffersConfigurationConstants.billPaymentProductItem);
		list.add("Recharge Product Item ="+ OffersConfigurationConstants.rechargeProductItem);
		list.add("Subscription Product Item ="+ OffersConfigurationConstants.subscriptionProductItem);
		list.add("Gold Certificate Product Item ="+ OffersConfigurationConstants.goldCertificateProductItem);
		list.add("Point Gifting Product Item ="+ OffersConfigurationConstants.pointGiftingProductItem);
		list.add("Point Conversion Partner Code ="+ OffersConfigurationConstants.pointConversionPartnerCode);
		list.add("Gold Certificate Conversion Rate ="+ OffersConfigurationConstants.goldCertificateConversionRate);
		list.add("Payment Method Full Credit Card ="+ OffersConfigurationConstants.fullCreditCard);
		list.add("Payment Method Full Points ="+ OffersConfigurationConstants.fullPoints);
		list.add("Payment Partial Payment ="+ OffersConfigurationConstants.partialCardPoints);
		list.add("Payment Method Add To Bill ="+ OffersConfigurationConstants.addToBill);
		list.add("Payment Method Deduct From Balance ="+ OffersConfigurationConstants.deductFromBalance);
		list.addAll(OffersListConstants.OFFER_ID_PURCHASE_LIST);
		return list; 
		
	}
	
	@PostMapping(value = "/listEligibleOffers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OfferCatalogResultResponse listEligibleOffers(@RequestBody EligibleOffersFiltersRequest eligibleOffersParameters,
            @RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
            @RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
            @RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
            @RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
            @RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
            @RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
            @RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = true) String channelId,
            @RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
            @RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
            @RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
            @RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId){

    	String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ALL_ELIGIBLE_OFFER.get());
		LOG.info(log);
		
		eligibleOffersParameters = ObjectUtils.isEmpty(eligibleOffersParameters)
                ? new EligibleOffersFiltersRequest()
                : eligibleOffersParameters;
		
		log = Logs.logForRequest(eligibleOffersParameters);
		LOG.info(log);
    	
		OfferCatalogResultResponse offerCatalogResultResponse = getEligibleOfferList(eligibleOffersParameters, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
		   				channelId, systemId, systemPassword, token, transactionId));

        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ALL_ELIGIBLE_OFFER.get());
		LOG.info(log);
        
        return offerCatalogResultResponse;
    
    }
	
	/**
	 * Domain method to get eligible offers list for method
	 * @param eligibleOffersRequest
	 * @param headers
	 * @return list of eligible offers
	 */
	public OfferCatalogResultResponse getEligibleOfferList(EligibleOffersFiltersRequest eligibleOffersRequest, Headers headers) {
		
		OfferCatalogResultResponse offerCatalogResultResponse = new OfferCatalogResultResponse(headers.getExternalTransactionId());
		boolean isMember = !StringUtils.isEmpty(eligibleOffersRequest.getAccountNumber());
		
		OfferErrorCodes errorResult = isMember  
				? OfferErrorCodes.LISTING_OFFERS_MEMBER_FAILED
				: OfferErrorCodes.LISTING_OFFERS_ELIGIBLE_FAILED;
		
		OfferSuccessCodes successResult = isMember 
				? OfferSuccessCodes.OFFERS_LISTED_FOR_MEMBER_SUCCESSFULLY
				: OfferSuccessCodes.ELIGIBLE_OFFERS_LISTED_SUCCESSFULLY;
		
		try {
			
			if(OfferValidator.validateEligibleOfferFilterRequest(eligibleOffersRequest, offerCatalogResultResponse)) {
				
				EligibilityInfo eligibilityInfo = new EligibilityInfo();
				ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, headers, null, isMember);
				
				if(isMember) {
					
					eligibilityInfo.setAccountNumber(eligibleOffersRequest.getAccountNumber());
					helper.setAllAccountDetails(eligibilityInfo, offerCatalogResultResponse);
					
				}
				
				if(Checks.checkNoErrors(offerCatalogResultResponse)) {
					
					eligibilityInfo.setOfferList(repositoryHelper.findAllActiveEligibleOffers(headers.getChannelId()));	
					
					if(Checks.checkNoErrors(offerCatalogResultResponse)		
					&& ProcessValues.filterEligibleOffers(eligibilityInfo, eligibleOffersRequest, offerCatalogResultResponse)) {
					
					  helper.setCounterDetails(eligibilityInfo);
				      offerCatalogResultResponse.setTotalRecordCount(eligibilityInfo.getOfferList().size());
				      //offerCatalogResultResponse.setOfferCatalogs(helper.getEligibleOfferList(eligibilityInfo, offerCatalogResultResponse, true));
				      ProcessValues.sortEligibleOffers(offerCatalogResultResponse, eligibleOffersRequest);
					  ProcessValues.applyPagination(eligibleOffersRequest, offerCatalogResultResponse);
					  LOG.info("Filtered offers : {}", offerCatalogResultResponse.getOfferCatalogs().stream()
								.map(OfferCatalogResultResponseDto::getOfferId)
								.collect(Collectors.toList()));
					}
					
				}
				  
			}
			
			
		} catch (MarketplaceException e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_OFFERS_LIST.get(), null, e, errorResult, null,
					errorResult,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), null, headers.getUserName())));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Responses.setResponseAfterException(offerCatalogResultResponse, 
					new ExceptionInfo(this.getClass().toString(), 
					OfferConstants.GET_ELIGIBLE_OFFERS_LIST.get(), e, null, errorResult,
					OfferExceptionCodes.GET_LIST_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION, null,
					exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), null, headers.getUserName())));
			
		}
		
		Responses.setResponse(offerCatalogResultResponse, errorResult, successResult);
		return offerCatalogResultResponse;
	}
	
	
	@GetMapping(value = "/birthdayAccounts", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BirthdayAccountsDto> getBirthdayAccounts(
    		@RequestHeader(required = true) Integer days,
    		@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) throws MarketplaceException{
       
		  LOG.info("Inside getBirthdayAccounts :: getBirthdayAccounts");
		  return repositoryHelper.getBirthdayAccounts(days);
		
		
	}
	
	@GetMapping(value = "/renewalReport")
    public String sendRenewalReport(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId){
       
		    subscriptionManagementControllerHelper.sendSubscriptionRenewalReport(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
		    return "Processed";
	}
	
	 @PostMapping(value = "/testCMF", consumes = MediaType.ALL_VALUE)
		public void testCMFIntegration(
				@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
				@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
				@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
				@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
				@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
				@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
				@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
				@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
				@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
				@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
				@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
				@RequestBody CFMRequestDto cmfRequestDto) {

			LOG.info("inside testCMF test API");
			
			try {
				cmfservice.cfmPosting(cmfRequestDto, externalTransactionId, "");
			} catch (MarketplaceException e) {
				LOG.error("exception occured while calling CMF : {}", e.getMessage());
				e.printStackTrace();
			}

		}
	 

			
}

