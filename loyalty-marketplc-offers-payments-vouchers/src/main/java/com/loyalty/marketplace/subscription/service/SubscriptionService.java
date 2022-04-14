package com.loyalty.marketplace.subscription.service;

import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.RAW_TYPE;
import static com.loyalty.marketplace.offers.constants.OffersConfigurationConstants.UNCHECKED;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.inbound.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.inbound.dto.PaymentAdditionalRequest;
import com.loyalty.marketplace.payment.outbound.database.service.EPGIntegrationService;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.payment.utils.EPGResponse;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.crm.Accounts;
import com.loyalty.marketplace.subscription.crm.ResponseObj;
import com.loyalty.marketplace.subscription.crm.Subscriptions;
import com.loyalty.marketplace.subscription.dm.dto.CustomerListDMRequestDto;
import com.loyalty.marketplace.subscription.dm.dto.CustomerListDMResponse;
import com.loyalty.marketplace.subscription.dm.dto.CustomerListDMResult;
import com.loyalty.marketplace.subscription.domain.LinkedOffers;
import com.loyalty.marketplace.subscription.domain.SubscriptionCatalogDomain;
import com.loyalty.marketplace.subscription.inbound.dto.NotificationValues;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto.PhoneyTunesRequest;
import com.loyalty.marketplace.subscription.phoneyTunes.inbound.dto.PhoneyTunesResponse;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.subscription.utils.SubscriptionUtils;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;
import com.loyalty.marketplace.utils.Logs;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.CommonApiStatus;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Service
@SuppressWarnings({RAW_TYPE, UNCHECKED})
public class SubscriptionService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	@Value("${offers.uri}")
    private String offersUri;	
	
	@Value("${memberManagement.uri}")
	private String memberManagementUri;
	
	@Value(OffersConfigurationConstants.ELIGIBILITY_MATRIX_PATH)
    private String eligibilityMatrixPath;
	
	@Value("${memberManagement.getCrmCall.path}")
	private String getCrmCall;
	
	@Value("${decisionManager.uri}")
	private String dmUri;
	
	@Value("${payment.tibco.username}")
	private String tibcoUsername;
	
	@Value("${payment.tibco.password}")
	private String tibcoPassword;

	@Value("${payment.rbt.uri}")
	private String rbtUri;
	
//	@Value("${descisionManager.etisalat.customer}")
	private String etisalatCustomerUri;
	
	@Value("${phoneyTunes.subscription.uri}")
	private String phoneyTunesSubscription;
	
	@Value("${phoneyTunes.unSubscription.uri}")
	private String phoneyTunesUnSubscription;
	
	@Value("${phoneyTunes.userName}")
	private String phoneyTunesUsername;
	
	@Value("${phoneyTunes.password}")
	private String phoneyTunesPassword;
	
	@Value("${phoneyTunes.lifestyle.auto.renewal.packageId}")
	private String phoneyTunesLifeStyleAutoPackId;
	
	@Value("${phoneyTunes.lifestyle.one.time.packageId}")
	private String phoneyTunesLifeStyleOnePackId;
	
	@Value("${phoneyTunes.food.auto.renewal.packageId}")
	private String phoneyTunesFoodAutoPackId;
	
	@Value("${phoneyTunes.food.one.time.packageId}")
	private String phoneyTunesFoodOnePackId;
	
	@Value("${phoneyTunes.requested.system}")
	private String phoneyTunesRequestedSystem;
	
	@Value("${email.additional.parameter.manageSub}")
	private String manageSub;
	
	@Value("${email.additional.parameter.orderDetails1.lifstyle}")
	private String orderDetails1Lifestyle;
	
	@Value("${email.additional.parameter.orderDetails2.lifstyle}")
	private String orderDetails2Lifestyle;
	
	@Value("${email.additional.parameter.orderDetails3.lifstyle}")
	private String orderDetails3Lifestyle;
	
	@Value("${email.additional.parameter.orderDetails1.food}")
	private String orderDetails1food;
	
	@Value("${email.additional.parameter.orderDetails2.food}")
	private String orderDetails2food;
	
	@Value("${email.additional.parameter.orderDetails3.food}")
	private String orderDetails3food;
	
	@Value("${email.additional.parameter.TNC}")
	private String termsAndConditions;
	
	@Value("${email.additional.parameter.greetingLifestyle1}")
	private String greetingLifestyle1;
	
	@Value("${email.additional.parameter.greetingFood1}")
	private String greetingFood1;
	
	@Value("${email.additional.parameter.pack.lifeStyle}")
	private String lifestylePack;
	
	@Value("${email.additional.parameter.pack.food}")
	private String foodPack;
	
	@Value("${email.additional.parameter.paymentMethod.addToBill}")
	private String addToBillPaymentMethod;
	
	@Value("${email.additional.parameter.paymentMethod.deductFromBalance}")
	private String deductFromBalPaymentMethod;
	
	@Value("${email.additional.parameter.paymentMethod.fullCreditCard}")
	private String fullCCPaymentMethod;
	
	@Value("${email.additional.parameter.paymentIcon.addToBill}")
	private String addToBillPaymentIcon;
	
	@Value("${email.additional.parameter.paymentIcon.deductFromBalance}")
	private String deductFromBalPaymentIcon;
	
	@Value("${email.additional.parameter.paymentIcon.fullCreditCard}")
	private String fullCCPaymentIcon;
	
	@Value("${sms.additional.parameter.lifestyle.packname.english}")
	private String lifestylePackageNameEnglish;
	
	@Value("${sms.additional.parameter.food.packname.english}")
	private String foodPackageNameEnglish;
	
	@Value("${sms.additional.parameter.food.benefit.english}")
	private String foodBenefitInEnglish;
	
	@Value("${sms.additional.parameter.lifestyle.benefit.english}")
	private String lifestyleBenefitInEnglish;
	
	@Value("${sms.additional.parameter.onetimeSubscription.status.english}")
	private String oneTimeSubsStatusInEnglish;
	
	@Value("${sms.additional.parameter.autorenewalSubscription.status.english}")
	private String autoRenewalSubsStatusInEnglish;
	
	@Value("${sms.additional.parameter.food.price.english}")
	private String foodPriceInEnglish;
	
	@Value("${sms.additional.parameter.lifestyle.price.english}")
	private String lifestylePriceInEnglish;
	
	@Autowired
	private EventHandler eventHandler;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	@Autowired
    private ServiceHelper serviceHelper;
	
	@Autowired
    PaymentService paymentService;
	
	@Autowired
	EPGIntegrationService epgIntegrationService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	SubscriptionCatalogDomain subscriptionCatalogDomain;
	
	@Autowired
	SubscriptionUtils subscriptionUtils;
	
	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	public List<LinkedOffers> listOffersResponse(String userName, String token, ResultResponse result) throws SubscriptionManagementException {
		LOG.info("Enter listOffersResponse");
		List<LinkedOffers> availableOffers = new ArrayList<>();
		try {
			HttpHeaders headers = new HttpHeaders();
            headers.set(RequestMappingConstants.TOKEN, token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            String jsonStr = retryCallForListOffersResponse(offersUri, entity) ;
            
			Map map = mapper.readValue(jsonStr, new TypeReference<HashMap>(){});
			Set<Entry> keys = map.entrySet();
			for (Entry key : keys) {
				if (((String) key.getKey()).equalsIgnoreCase("offerList") && null != key.getValue()) {
					List offerCatalogs = (ArrayList) key.getValue();
					for(int i=0; i<offerCatalogs.size(); i++) {
						LinkedOffers offerTitle = new LinkedOffers();	
						LinkedHashMap offerCatalog = (LinkedHashMap) offerCatalogs.get(i);
						Set<Entry> offerCatalogKeyValues =  offerCatalog.entrySet();
						for(Entry offerCatalogKeyValue:offerCatalogKeyValues) {		
							if(offerCatalogKeyValue.getKey().toString().equalsIgnoreCase("offerId") && null != offerCatalogKeyValue.getValue()) {
								offerTitle.setId(offerCatalogKeyValue.getValue().toString());
							}
							if(offerCatalogKeyValue.getKey().toString().equalsIgnoreCase("offerTitleEn") && null != offerCatalogKeyValue.getValue()) {
								offerTitle.setEnglish(offerCatalogKeyValue.getValue().toString());
							}
							if(offerCatalogKeyValue.getKey().toString().equalsIgnoreCase("offerTitleAr") && null != offerCatalogKeyValue.getValue()) {
								offerTitle.setArabic(offerCatalogKeyValue.getValue().toString());
							}
//							offerTitle.setId((offerCatalogKeyValue.getKey().toString().equalsIgnoreCase("offerId") && null != offerCatalogKeyValue.getValue()) ?
//									offerCatalogKeyValue.getValue().toString() : null);
//							offerTitle.setEnglish((offerCatalogKeyValue.getKey().toString().equalsIgnoreCase("offerTitleEn") && null != offerCatalogKeyValue.getValue()) ?
//									offerCatalogKeyValue.getValue().toString() : null);
//							offerTitle.setArabic((offerCatalogKeyValue.getKey().toString().equalsIgnoreCase("offerTitleAr") && null != offerCatalogKeyValue.getValue()) ?
//									offerCatalogKeyValue.getValue().toString() : null);					
						}
						availableOffers.add(offerTitle);	
					}	
				}	    	
		    }
		} catch (RestClientException e) {
			throw new SubscriptionManagementException("SubscriptionService", "listOffersResponse",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GET_OFFERS_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {			
			throw new SubscriptionManagementException("SubscriptionService", "listOffersResponse",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);			
		}
		LOG.info("Enter listOffersResponse");
		return availableOffers;
	}
	
	
	/***
	 * 
	 * @param entity 
	 * @param offersUri 
	 * @return
	 */
	private String retryCallForListOffersResponse(String offersUri, HttpEntity<?> entity) {
		try {
		LOG.info("inside Retry block using retryTemplate of retryCallForListOffersResponse method of class {}", this.getClass().getName());
		return retryTemplate.execute(context ->{
			retryLogsService.saveRestRetrytoRetryLogs(offersUri.toString(), null,
					entity.toString(), null);
			return getRestTemplate.exchange(offersUri, HttpMethod.GET, entity, String.class).getBody();
		});	
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, null, null,
					null, offersUri.toString());
		}
		return null;
	}
	
	public boolean isEtisalat (List customerType, ResultResponse resultResponse) throws SubscriptionManagementException {
		
		final String uri  = dmUri + etisalatCustomerUri;
		LOG.info("GET etisalat customer URL : {} ", uri);
			try {		
				CustomerListDMRequestDto customerListDMRequestDto = new CustomerListDMRequestDto();
				customerListDMRequestDto.setCustomerList(customerType);
				String jsonStr = retryCallForIsEtisalat( uri,  customerListDMRequestDto);
				
				Gson gson = new Gson();
				CustomerListDMResponse response = gson.fromJson(jsonStr, CustomerListDMResponse.class);
				LOG.info("CustomerListDMResponse : {}", response);
				
				for (CustomerListDMResult result : response.getResult()) {
					if(result.isEtisalatCustomer()) {
						return true;
					}
				}
			} catch (RestClientException e) {
				throw new SubscriptionManagementException("SubscriptionService", "isEtisalat",
						e.getClass() + e.getMessage(), SubscriptionManagementCode.GET_CRMCALL_REST_CLIENT_EXCEPTION);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			return false;
	}
	
	/***
	 * 
	 * @param customerListDMRequestDto 
	 * @param uri 
	 * @return
	 */
	private String retryCallForIsEtisalat(String uri, CustomerListDMRequestDto customerListDMRequestDto) {
		try {
		LOG.info("inside Retry block using retryTemplate of retryCallForIsEtisalat method of class {}", this.getClass().getName());
		return retryTemplate.execute(context ->{
			retryLogsService.saveRestRetrytoRetryLogs(uri.toString(), null,
					customerListDMRequestDto.toString(), null);
			return restTemplate.postForObject(uri, customerListDMRequestDto, String.class);
		});	
	} catch (Exception e) {
		exceptionLogsService.saveExceptionsToExceptionLogs(e, null, null,
				null, uri.toString());
	}
	return null;
		
	}
	
	public boolean isCrmAddOnServiceDisabled (String accountNumber, Headers headers) throws SubscriptionManagementException {
		
		final String uri = memberManagementUri + getCrmCall + "?accountNumber=" + accountNumber;
		LOG.info("CRM Add On Service URL : {} ", uri);
		boolean flag = false;
		try {
			
			HttpEntity<MemberDetailRequestDto> requestEntity = new HttpEntity<>(serviceHelper.getHeader(headers));
			ResponseEntity<CommonApiStatus> response = retryCallForCrm(uri, requestEntity, headers);
			CommonApiStatus commonApiStatus = response.getBody();
			LOG.info("commonApiStatus : {}", commonApiStatus);
						
			ResponseObj result = (ResponseObj) serviceHelper.convertToObject(commonApiStatus.getResult(),
					ResponseObj.class);
						
            if(!ObjectUtils.isEmpty(result) 
            && !ObjectUtils.isEmpty(result.getResult())
            && !CollectionUtils.isEmpty(result.getResult().getAccounts())) {
            	
            	for (Accounts accounts : result.getResult().getAccounts()) {
					for(Subscriptions subscriptions : accounts.getSubscriptions()) {
						if (subscriptions.getProduct().getProductCode1().equalsIgnoreCase(SubscriptionManagementConstants.CRM_THIRD_PARTY_PRODUCTCODE.get())) {
							LOG.info("Subscription ID : {}", subscriptions.getSubscriptionId());
							flag = true;
						}
					}
				}
            }
            
		} catch (RestClientException e) {
			throw new SubscriptionManagementException("SubscriptionService", "isCrmAddOnServiceDisabled",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GET_CRMCALL_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "isCrmAddOnServiceDisabled", e.getMessage(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
			
		}
		return flag;
	}
		
	/***
	 * 
	 * @param requestEntity 
	 * @param uri 
	 * @return
	 */
	private ResponseEntity<CommonApiStatus> retryCallForCrm(String uri, HttpEntity<MemberDetailRequestDto> requestEntity, Headers header) {
		try {
		LOG.info("inside Retry block using retryTemplate of retryCallForCrm method of class {}", this.getClass().getName());
		return retryTemplate.execute(context ->{
			retryLogsService.saveRestRetrytoRetryLogs(uri.toString(), header.getExternalTransactionId(),
					requestEntity.toString(), header.getUserName());
			return getRestTemplate.exchange(uri, HttpMethod.GET, requestEntity, CommonApiStatus.class);
		});	
	} catch (Exception e) {
		exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
				header.getUserName(), uri.toString());
	}
	return null;
		
	}
	
	/**
	 * 
	 * @param resultResponse
	 * @return list of all customer types fetched from member management microservice
	 * @throws MarketplaceException
	 * @throws IOException
	 */
	public List<String> getAvailableCustomerType(String token, ResultResponse resultResponse) throws MarketplaceException, IOException {
		String log = Logs.logsForEnteringServiceMethod(this.getClass().getName(), OfferConstants.GET_AVAILABLE_CUSTOMER_TYPE_METHOD.get());
		LOG.info(log);
		String url = memberManagementUri + eligibilityMatrixPath;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(RequestMappingConstants.TOKEN, token);

		HttpEntity<Object> request=new HttpEntity<>(headers);
		
		List<String> customerType = new ArrayList<>();
		
		try {
			
			String jsonStr = retryCallForGetAvailableCustomerType(url, request);
			log = Logs.logForServiceResponse(jsonStr);
			LOG.info(log);
						
			Map map = mapper.readValue(jsonStr, new TypeReference<HashMap>(){});
			Set<Entry> keys = map.entrySet();
			for (Entry key : keys) {
				if (((String) key.getKey()).equalsIgnoreCase(OfferConstants.RESULT_KEY.get()) && null != key.getValue()) {
					LinkedHashMap viewEligibilityResult = (LinkedHashMap) key.getValue();
					Set<Entry> eligibilityResultkeyValues =  viewEligibilityResult.entrySet();
					for(Entry eligibilityResultkeyValue:eligibilityResultkeyValues) {
						if (eligibilityResultkeyValue.getKey().toString().equalsIgnoreCase(OfferConstants.VIEW_ELIGIBILITY_RESULT.get()) && null != eligibilityResultkeyValue.getValue()) {							
							LinkedHashMap viewEligibility = (LinkedHashMap) eligibilityResultkeyValue.getValue();
							Set<Entry> eligibilitykeyValues =  viewEligibility.entrySet();
							for(Entry eligibilitykeyValue:eligibilitykeyValues) {
								if (eligibilitykeyValue.getKey().toString().equalsIgnoreCase(OfferConstants.ELIGIBILITY.get()) && null != eligibilitykeyValue.getValue()) {
									List eligibilityList = (ArrayList)eligibilitykeyValue.getValue();
									for(Object eligibilityObj : eligibilityList) {
										LinkedHashMap eligibility = (LinkedHashMap) eligibilityObj;
										Set<Entry> eligibilityObjKeyValues =  eligibility.entrySet();
										for(Entry eligibilityObjKeyValue:eligibilityObjKeyValues) {
											if (eligibilityObjKeyValue.getKey().toString().equalsIgnoreCase(OfferConstants.CUSTOMER_TYPE.get()) && null != eligibilityObjKeyValue.getValue()) {
												customerType.add(eligibilityObjKeyValue.getValue().toString());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (RestClientException e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_AVAILABLE_CUSTOMER_TYPE_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_AVAILABLE_CUSTOMER_TYPES_REST_CLIENT_EXCEPTION);
			
		} catch (Exception e) {
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.GET_AVAILABLE_CUSTOMER_TYPE_METHOD.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_AVAILABLE_CUSTOMER_TYPES_RUNTIME_EXCEPTION);
			
		}
		
		log = Logs.logForServiceResponse(customerType);
		LOG.info(log);
		log = Logs.logsForLeavingServiceMethod(this.getClass().getName(), OfferConstants.GET_AVAILABLE_CUSTOMER_TYPE_METHOD.get());
		LOG.info(log);
		return customerType;
	}
	
	
	/***
	 * 
	 * @param customerListDMRequestDto 
	 * @param uri 
	 * @return
	 */
	private String retryCallForGetAvailableCustomerType(String url, HttpEntity<Object> request) {
		try {
		LOG.info("inside Retry block using retryTemplate of retryCallForGetAvailableCustomerType method of class {}", this.getClass().getName());
		return retryTemplate.execute(context ->{
			retryLogsService.saveRestRetrytoRetryLogs(url.toString(), null,
					request.toString(), null);
			return getRestTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
		});	
	} catch (Exception e) {
		exceptionLogsService.saveExceptionsToExceptionLogs(e, null, null,
				null, url.toString());
	}
	return null;
		
	}
	
	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		LOG.info("Requestand response saved : {}",callLog);
		jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
	}
	
	public void logServiceRequest(String jsonReq, String responseString, long start, long end, String externalTransactionId) {
		ServiceCallLogsDto serviceCallLogsDto = new ServiceCallLogsDto();
        serviceCallLogsDto.setCreatedDate(new Date());
        serviceCallLogsDto.setAction("PhoneyTunes");
        serviceCallLogsDto.setTransactionId(externalTransactionId);
        serviceCallLogsDto.setServiceType("Outbound");
        serviceCallLogsDto.setRequest(jsonReq);
        serviceCallLogsDto.setResponse(responseString);
        serviceCallLogsDto.setCreatedUser(""); 
        serviceCallLogsDto.setResponseTime((end-start));
        
        saveRequestResponse(serviceCallLogsDto);
	}
	
	public PhoneyTunesResponse getPhoneyTunesResponse(PhoneyTunesRequest phoneyTunesRequest, ResultResponse resultResponse, String externalTransactionId) throws SubscriptionManagementException {
		LOG.info("phoneyTunesSubscription accountNumber : {} with freeDuration : {} and cost : {}", phoneyTunesRequest.getAccountNumber(), 
				phoneyTunesRequest.getFreeDuration(), phoneyTunesRequest.getPackagePrice());
		phoneyTunesRequest.setAccountNumber(phoneyTunesRequest.getAccountNumber().replaceFirst("^0+(?!$)", "971"));
				
//		String packageId = phoneyTunesRequest.getChargeabilityType()
//				.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()) ? phoneyTunesLifeStyleAutoPackId : phoneyTunesLifeStyleOnePackId;
		
		String queryParam = "?usr="+phoneyTunesUsername+"&pwd="+phoneyTunesPassword+"&msisdn="+phoneyTunesRequest.getAccountNumber()+"&packageId="+phoneyTunesRequest.getPackageId();
		if(null != phoneyTunesRequest.getPackagePrice() && phoneyTunesRequest.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
			Double costAedToFils = Double.parseDouble(phoneyTunesRequest.getPackagePrice()) * 100;
			phoneyTunesRequest.setPackagePrice(String.format("%.0f", costAedToFils));
			queryParam = queryParam +"&packagePrice="+phoneyTunesRequest.getPackagePrice();
		}
		if(phoneyTunesRequest.getFreeDuration() != 0) {
			queryParam = queryParam +"&duration="+phoneyTunesRequest.getFreeDuration();
		}
				
		MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
		headers.add("X-TIB-TransactionID", phoneyTunesRequest.getExternalTransactionId());
	    headers.add("X-TIB-RequestedSystem", phoneyTunesRequestedSystem);
		
        try {
        	long start = System.currentTimeMillis();
        	Gson g = new Gson();       
            Client client = Client.create();
		    client.addFilter(new HTTPBasicAuthFilter("loyalty", "loyalty"));
		    final String phoneyTunesUrl = phoneyTunesRequest.getAction().equalsIgnoreCase(SubscriptionManagementConstants.CANCEL_SUBSCRIPTION.get()) 
		    		? phoneyTunesUnSubscription+queryParam : phoneyTunesSubscription+queryParam;
		    		    
		    LOG.info("PhoneyTunes Request URL : {} :: {}", phoneyTunesRequest.getAction(), phoneyTunesUrl);
		    WebResource.Builder builder = client.resource(phoneyTunesUrl).accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).type(javax.ws.rs.core.MediaType.APPLICATION_JSON);
		    for (String key : headers.keySet()) {
		    	builder = builder.header(key, headers.getFirst(key));
		    }
		    ClientResponse response = builder.post(ClientResponse.class);
			String responseString = response.getEntity(String.class);
			LOG.info("PhoneyTunes ResponseString : {}", responseString);
			PhoneyTunesResponse responseBody = g.fromJson(responseString, PhoneyTunesResponse.class);
			LOG.info("PhoneyTunes ResponseBody : {}", responseBody);
			long end = System.currentTimeMillis();
			logServiceRequest(phoneyTunesUrl, responseString, start, end, externalTransactionId);
			
			if(null != responseBody) {
				if(responseBody.getAckMessage().getStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
					LOG.info("Success Status");
					if(responseBody.getResponse().toUpperCase().contains(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
						LOG.info("Success Response : {}", responseBody);
						return responseBody;
					} else {
						LOG.info("Failure Response : {}", responseBody.getResponse());
						resultResponse.setErrorAPIResponse(SubscriptionManagementCode.PHONY_TUNES_ERROR.getIntId(),
								SubscriptionManagementCode.PHONY_TUNES_ERROR.getMsg() + " : " +responseBody.getResponse());
						return responseBody;
					}										
				} else {
					LOG.info("Failure Status : {}", responseBody);
					resultResponse.setErrorAPIResponse(SubscriptionManagementCode.PHONY_TUNES_ERROR.getIntId(),
							SubscriptionManagementCode.PHONY_TUNES_ERROR.getMsg() + " : " +responseBody.getAckMessage().getErrorCode() 
							+ " | " + responseBody.getAckMessage().getErrorType() + " | " + responseBody.getAckMessage().getErrorDescription());
					return responseBody;
				}
			} else {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.PHONY_TUNES_CONNECTION_ERROR.getIntId(),
	 					SubscriptionManagementCode.PHONY_TUNES_CONNECTION_ERROR.getMsg());
			}
			return null;
        } catch (Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "getPhoneyTunesResponse", e.getMessage(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}

    }
	
	public PaymentResponse getPaymentResponse(PurchaseRequestDto purchaseRequestDto, SubscriptionCatalog subscriptionCatalog, 
			GetMemberResponseDto getMemberResponseDto, Headers headers, boolean isPaymentRequired, String uuid, ResultResponse resultResponse) throws SubscriptionManagementException {
		LOG.info("getPaymentResponse :: isPaymentRequired : {}",isPaymentRequired);
		PaymentAdditionalRequest paymentAdditionalRequest = new PaymentAdditionalRequest();
		paymentAdditionalRequest.setChannelId(headers.getChannelId());
		paymentAdditionalRequest.setActivityCode(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVITY_CODE.get());
		paymentAdditionalRequest.setUuid(uuid);
		paymentAdditionalRequest.setPaymentRequired(isPaymentRequired);
		paymentAdditionalRequest.setHeader(headers);
		
		try {
			LOG.info("Calling Payment Service : {}", purchaseRequestDto);
			LOG.info("getPaymentResponse :: paymentAdditionalRequest.isPaymentRequired : {}",paymentAdditionalRequest.isPaymentRequired());
			purchaseRequestDto.setExtTransactionId(headers.getExternalTransactionId());
			PaymentResponse paymentResponse = paymentService.paymentAndProvisioning(purchaseRequestDto, getMemberResponseDto, null, subscriptionCatalog, paymentAdditionalRequest);			 		
	 		LOG.info("Response from Payment Service : {}", paymentResponse);
	 		
	 		if(null != paymentResponse) {
	 			if(paymentResponse.getPaymentStatus().equalsIgnoreCase(MarketplaceConstants.STATUS_SUCCESS.getConstant())) {
		 			return paymentResponse;
	 			} else {
					resultResponse.setErrorAPIResponse(paymentResponse.getErrorCode(),
							paymentResponse.getFailedreason() + " : " + " Error from PaymentService");
					return paymentResponse;
				} 				
	 		} else {
	 			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.PAYMENT_FAILED.getIntId(),
	 					SubscriptionManagementCode.PAYMENT_FAILED.getMsg());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SubscriptionManagementException(this.getClass().toString(), "getPaymentResponse", e.getMessage(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}
	}
	
	public boolean epgTransaction(PurchaseRequestDto purchaseRequestDto, Headers headers, Date nextRenewalDate, ResultResponse resultResponse) throws Exception {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogDomain.findByIdAndStatus(purchaseRequestDto.getSubscriptionCatalogId(), SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get());
		if (!subscriptionCatalog.isPresent()) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getIntId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getMsg());
			return false;
		}
		EPGResponse epgResponse = epgIntegrationService.callEPGPayerNotPresentPayment(purchaseRequestDto, headers, resultResponse);
		
		String authorizationCode = epgResponse.getTransaction().getApprovalCode();
		String cardToken = epgResponse.getTransaction().getCardToken();
		String epgTransactionId = epgResponse.getTransaction().getTransactionID();
		
		if(resultResponse.getApiStatus().getStatusCode() == 0) {
			purchaseRequestDto.setEpgTransactionId(epgTransactionId);
			purchaseRequestDto.setAuthorizationCode(authorizationCode);
			purchaseRequestDto.setCardToken(cardToken);
			LOG.info("MISC CALL");
			
			paymentService.callMiscPaymentPostingForAutoRenewalSubscriptions(purchaseRequestDto, MarketplaceConstants.RENEW_AUTORENEWAL_SUBSCRIPTION.getConstant(), headers);
			
			//Email and SMS on autorenewal using card
			NotificationValues notificationValues = new NotificationValues(false, false, purchaseRequestDto.getAccountNumber(),purchaseRequestDto.getPromoCode(),MarketplaceConstants.FULLCREDITCARD.getConstant(),purchaseRequestDto.getCardNumber(),purchaseRequestDto.getSpentAmount(),SubscriptionManagementConstants.RENEWED_EVENT, SubscriptionManagementConstants.ACTION_TYPE_RENEWED.get(), nextRenewalDate.toString(), null);
			notificationValues.setNotifyAutoRenewalSubscription(true);
			subscriptionUtils.subscriptionNotification(headers, subscriptionCatalog.get(), notificationValues,resultResponse);
			
			return true;
		} else {
			return false;
		}
		

	}
	
	public void notifyMemberBySMS(List<String> destinationNumbers, String promoCode, String externalTransactionId) {
		Map<String, String> additionalParameters = new HashMap<>();
		String templateId = promoCode != null ? "2095" : "2064";
		String notificationId = promoCode != null ? "105" : "22";
				
		SMSRequestDto smsEvent = new SMSRequestDto(externalTransactionId, templateId, notificationId,
                "00", null, additionalParameters, "en", destinationNumbers);
        eventHandler.publishSms(smsEvent);
	}
	
	public void notifyMemberBySMSForAutoRenewalSubscription(List<String> destinationNumbers, String externalTransactionId, SubscriptionCatalog subscriptionCatalog, NotificationValues notificationValues) {
		Map<String, String> additionalParameters = new HashMap<>();
		String packageName = null;
		String benefits = null;
		String status = null;
		String price = null;
		String templateId = null;
		String notificationId = null;
		String renewalDate = subscriptionUtils.formatStringDate(notificationValues.getNextRenewalDate());
				
		templateId = SubscriptionManagementConstants.SMS_TEMPLATEID_AUTORENEWAL;
		notificationId = SubscriptionManagementConstants.SMS_NOTIFICATIONID_AUTORENEWAL;
		LOG.info("uiLanguage: {}", notificationValues.getUiLanguage());
		if((SubscriptionManagementConstants.ENGLISH_LANGUAGE.get().equalsIgnoreCase(notificationValues.getUiLanguage())) 
				|| (SubscriptionManagementConstants.English.equalsIgnoreCase(notificationValues.getUiLanguage()))) {
			LOG.info("Inside English dynamic SMS params for autorenewal subscription");
			if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
				packageName = lifestylePackageNameEnglish;
			} else if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
				packageName = foodPackageNameEnglish;
			} else {
				packageName = "";
			}
			if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
				benefits = lifestyleBenefitInEnglish;
			} else if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
				benefits = foodBenefitInEnglish;
			} else {
				benefits = "";
			}
			if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
				price = lifestylePriceInEnglish;
			} else if (subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
				price = foodPriceInEnglish;
			} else {
				price = "";
			}
			if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
				status = oneTimeSubsStatusInEnglish;
			} else if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
				status = autoRenewalSubsStatusInEnglish;
			} else {
				status = "";
			}
			additionalParameters.put(SubscriptionManagementConstants.PACKAGE_NAME, packageName);
			additionalParameters.put(SubscriptionManagementConstants.ACTION_TYPE, notificationValues.getActionType());
			additionalParameters.put(SubscriptionManagementConstants.BENEFITS, benefits);
			additionalParameters.put(SubscriptionManagementConstants.Status, status);
			additionalParameters.put(SubscriptionManagementConstants.DATE, renewalDate);
			additionalParameters.put(SubscriptionManagementConstants.PRICE, price);
			SMSRequestDto smsEvent = new SMSRequestDto(externalTransactionId, templateId, notificationId,
			"00", null, additionalParameters, "en", destinationNumbers);
			eventHandler.publishSms(smsEvent);
		}
		if((SubscriptionManagementConstants.ARABIC_LANGUAGE.get().equalsIgnoreCase(notificationValues.getUiLanguage()))
			|| (SubscriptionManagementConstants.Arabic.equalsIgnoreCase(notificationValues.getUiLanguage()))) {
				LOG.info("Inside Arabic dynamic SMS params for autorenewal subscription");
				if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
					packageName = SubscriptionManagementConstants.LIFESTYLE_PACKNAME_ARABIC;
				} else if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
					packageName = SubscriptionManagementConstants.FOOD_PACKNAME_ARABIC;
				} else {
					packageName = "";
				}
				if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
					benefits = SubscriptionManagementConstants.LIFESTYLE_BENEFIT_ARABIC;
				} else if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
					benefits = SubscriptionManagementConstants.FOOD_BENEFIT_ARABIC;
				} else {
					benefits = "";
				}
				if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
					price = SubscriptionManagementConstants.LIFESTYLE_PRICE_ARABIC;
				} else if (subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
					price = SubscriptionManagementConstants.FOOD_PRICE_ARABIC;
				} else {
					price = "";
				}
				if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
					status = SubscriptionManagementConstants.ONETIME_SUBSCRIPTION_STATUS_ARABIC;
				} else if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
					status = SubscriptionManagementConstants.AUTORENEWAL_SUBSCRIPTION_STATUS_ARABIC;
				} else {
					status = "";
				}
				String actionType = null;
				if(notificationValues.getActionType().equalsIgnoreCase(SubscriptionManagementConstants.ACTION_TYPE_ACTIVATED.get())) {
					actionType = SubscriptionManagementConstants.ACTIONTYPE_ACTIVATED_ARABIC;
				} else if(notificationValues.getActionType().equalsIgnoreCase(SubscriptionManagementConstants.ACTION_TYPE_RENEWED.get())) {
					actionType = SubscriptionManagementConstants.ACTIONTYPE_RENEWED_ARABIC;
				}
				additionalParameters.put(SubscriptionManagementConstants.PACKAGE_NAME, packageName);
				additionalParameters.put(SubscriptionManagementConstants.ACTION_TYPE,actionType);
				additionalParameters.put(SubscriptionManagementConstants.BENEFITS, benefits);
				additionalParameters.put(SubscriptionManagementConstants.Status, status);
				additionalParameters.put(SubscriptionManagementConstants.DATE, renewalDate);
				additionalParameters.put(SubscriptionManagementConstants.PRICE, price);
				SMSRequestDto smsEvent = new SMSRequestDto(externalTransactionId, templateId, notificationId,
				"00", null, additionalParameters, "ar", destinationNumbers);
				eventHandler.publishSms(smsEvent);
			}
	
	 }
	
	public void notifyMemberByEmailForAutoRenewalSubscription(NotificationValues notificationValues, String externalTransactionId, GetMemberResponse getMemberResponse, SubscriptionCatalog subscriptionCatalog) {
		LOG.info("Inside SubscriptionService.notifyMemberByEmailForAutoRenewalSubscription()");
		LOG.info("notificationValues : {}",notificationValues);
		EmailRequestDto emailRequestDto = new EmailRequestDto();
		double vat = subscriptionCatalog.getCost() * 0.05;
		double subTotal = subscriptionCatalog.getCost() - vat;
		LOG.info("SubscriptionSegment coming from subscriptionCatalog: {}",subscriptionCatalog.getSubscriptionSegment());
		LOG.info("SubscriptionSegment coming from subscriptionCatalog: {}",subscriptionCatalog.getSubscriptionSegment().toString());
	
		String pack = null;
		String greeting1 = null;
		String packType = null;
		String paymentMethod= null;
		String paymentIcon = null;
		String TNC = termsAndConditions.replace("offerId", subscriptionCatalog.getId());
		if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
			pack = lifestylePack;
		} else if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
			pack = foodPack;
		} else {
			pack = "";
		}
		if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
			greeting1 = greetingLifestyle1;
		} else if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
			greeting1 = greetingFood1;
		} else {
			greeting1 = "";
		}
		if (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			packType = "Auto-renewal";
		} else if (subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
			packType = "One time";
		} else {
			packType = "";
		}
		if (MarketplaceConstants.ADDTOBILL.getConstant().equalsIgnoreCase(notificationValues.getPaymentType())) {
			paymentMethod = addToBillPaymentMethod;
			paymentIcon = addToBillPaymentIcon;
		} else if (MarketplaceConstants.DEDUCTFROMBALANCE.getConstant().equalsIgnoreCase(notificationValues.getPaymentType())) {
			paymentMethod = deductFromBalPaymentMethod;
			paymentIcon = deductFromBalPaymentIcon;
		} else if (MarketplaceConstants.FULLCREDITCARD.getConstant().equalsIgnoreCase(notificationValues.getPaymentType())) {
			paymentMethod = fullCCPaymentMethod;
			paymentIcon = fullCCPaymentIcon;
		} else {
			paymentMethod = "";
			paymentIcon = "";
		}
		Map<String, String> additionalParameters = new HashMap<>();
		additionalParameters.put(SubscriptionManagementConstants.FIRST_NAME, getMemberResponse.getFirstName());
		additionalParameters.put(SubscriptionManagementConstants.EVENT, notificationValues.getChargebilityType());
		additionalParameters.put(SubscriptionManagementConstants.PACK, pack);
		additionalParameters.put(SubscriptionManagementConstants.PACK_TYPE, packType);
		additionalParameters.put(SubscriptionManagementConstants.AMOUNT_PAID, subscriptionCatalog.getCost().toString());
		additionalParameters.put(SubscriptionManagementConstants.PAYMENT_METHOD, paymentMethod);
		additionalParameters.put(SubscriptionManagementConstants.PAYMENT_ICON, paymentIcon);
		additionalParameters.put(SubscriptionManagementConstants.CC_DETAILS, notificationValues.getCardNumber());
		additionalParameters.put(SubscriptionManagementConstants.SMILES_POINTS, notificationValues.getSpentPoints().toString());
		additionalParameters.put(SubscriptionManagementConstants.SUB_TOTAL, String.valueOf(subTotal));
		additionalParameters.put(SubscriptionManagementConstants.VAT, String.valueOf(vat));
		additionalParameters.put(SubscriptionManagementConstants.TOTAL, subscriptionCatalog.getCost().toString());
		if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get())) {
			additionalParameters.put(SubscriptionManagementConstants.ORDER_DETAILS1, orderDetails1Lifestyle);
			additionalParameters.put(SubscriptionManagementConstants.ORDER_DETAILS2, orderDetails2Lifestyle);
			additionalParameters.put(SubscriptionManagementConstants.ORDER_DETAILS3, orderDetails3Lifestyle);
		}
		if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get())) {
			additionalParameters.put(SubscriptionManagementConstants.ORDER_DETAILS1, orderDetails1food);
			additionalParameters.put(SubscriptionManagementConstants.ORDER_DETAILS2, orderDetails2food);
			additionalParameters.put(SubscriptionManagementConstants.ORDER_DETAILS3, orderDetails3food);
		}
		additionalParameters.put(SubscriptionManagementConstants.TNC, TNC);
		additionalParameters.put(SubscriptionManagementConstants.MANAGE_SUB, manageSub);
		additionalParameters.put(SubscriptionManagementConstants.GREETING1, greeting1);
		additionalParameters.put(SubscriptionManagementConstants.DATE, subscriptionUtils.convertDateToGstTimeZone(new Date()));
		String templateId = SubscriptionManagementConstants.EMAIL_TEMPLATEID_AUTORENEWAL;
		String notificationId = SubscriptionManagementConstants.EMAIL_NOTIFICATION_AUTORENEWAL;
		emailRequestDto.setAccountNumber(notificationValues.getAccountNumber());
		emailRequestDto.setAdditionalParameters(additionalParameters);
		emailRequestDto.setEmailId(getMemberResponse.getEmail());
		emailRequestDto.setLanguage("en");
		emailRequestDto.setTemplateId(templateId);
		emailRequestDto.setNotificationId(notificationId);
		emailRequestDto.setTransactionId(externalTransactionId);
		emailRequestDto.setNotificationCode("00");
		LOG.info("Inside SubscriptionService.notifyMemberByEmailForAutoRenewalSubscription() additionalParamenters = {}", additionalParameters.toString());
		LOG.info("Inside SubscriptionService.notifyMemberByEmailForAutoRenewalSubscription() RequestDto = {}", emailRequestDto.toString());
		eventHandler.publishEmail(emailRequestDto);
	}
}

