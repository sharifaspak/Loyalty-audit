package com.loyalty.marketplace.subscription.inbound.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Validator;
import javax.ws.rs.QueryParam;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.offers.promocode.domain.PromoCodeDomain;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PartnerPromocodeRequest;
import com.loyalty.marketplace.offers.promocode.inbound.dto.ValidatePromoCodeRequest;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoCode;
import com.loyalty.marketplace.offers.promocode.outbound.database.repository.PromoCodeRepository;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PartnerPromoCodeDetails;
import com.loyalty.marketplace.offers.promocode.outbound.dto.ValidPromoCodeDetails;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApplyAndValidate;
import com.loyalty.marketplace.payment.inbound.restcontroller.MarketplaceController;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.constants.SubscriptionRequestMappingConstants;
import com.loyalty.marketplace.subscription.domain.SubscriptionCatalogDomain;
import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
import com.loyalty.marketplace.subscription.domain.SubscriptionPromotionDomain;
import com.loyalty.marketplace.subscription.inbound.dto.GiftApiRequest;
import com.loyalty.marketplace.subscription.inbound.dto.ManageRenewalRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.ManageSubscriptionRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogInputDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionInfoRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionPromotionRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionReportRequestDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionWithInterestRequestDto;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepositoryHelper;
import com.loyalty.marketplace.subscription.outbound.dto.CuisinesResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.GiftApiResponse;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionCatalogResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionInfoResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionReportResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResponseDto;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionResultResponse;
import com.loyalty.marketplace.subscription.outbound.dto.SubscriptionWithInterestResultResponse;
import com.loyalty.marketplace.subscription.service.SubscriptionService;
import com.loyalty.marketplace.subscription.utils.SubscriptionCatalogValidator;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.subscription.utils.SubscriptionValidator;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.MarketplaceValidator;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftRequestDto;

import io.swagger.annotations.Api;

@Api(value = "marketplace")
@RestController
@RequestMapping("/marketplace")
@RefreshScope
public class SubscriptionManagementController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionManagementController.class);
	
	@Value("${centos.server.address}")
    private String serverAddress;
	
	@Value("#{'${subscription.smsalert.destination.numbers}'.split(',')}")
    private List<String> alertdestinationNumbers;
	
	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	SubscriptionCatalogDomain subscriptionCatalogDomain;
	
	@Autowired
	SubscriptionCatalogRepository subscriptionCatalogRepository;
	
	@Autowired
	SubscriptionPromotionDomain subscriptionPromotionDomain;
	
	@Autowired
	SubscriptionPromotionDomain subscriptionPromotionRepository;
	
	@Autowired
	SubscriptionDomain subscriptionDomain;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Autowired
	MemberManagementService membermngmtService;
	
	@Autowired
	Validator validator;
	
	@Autowired
	SubscriptionManagementControllerHelper subscriptionManagementControllerHelper;
		
	@Autowired
	MarketplaceController marketplaceController;
	
	@Autowired
	PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
    PaymentService paymentService;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	ProgramManagement programManagement;
	
	@Autowired
	PurchaseDomain purchaseDomain;
	
	@Autowired
	PromoCodeDomain promoCodeDomain;

	@Autowired
	SubscriptionRepositoryHelper subscriptionRepositoryHelper;
	
	@Autowired
	SubscriptionValidator subscriptionValidator;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	PromoCodeRepository promoCodeRepository; 
	
	@PostMapping(value = "/subscriptionCatalog", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureSubscriptionCatalog(@RequestBody SubscriptionCatalogInputDto subscriptionCatalogInputDto,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter configureSubscriptionCatalog : Request :: {}", subscriptionCatalogInputDto);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = subscriptionManagementControllerHelper.validateAndCreateSubscriptionCatalogRequestFromInput(subscriptionCatalogInputDto, resultResponse, headers);
		LOG.info("Converted Request :: {}", subscriptionCatalogRequestDto);
		
		if (!MarketplaceValidator.validateDto(subscriptionCatalogRequestDto, validator, resultResponse)
				|| !SubscriptionCatalogValidator.validateChargeabilityType(subscriptionCatalogRequestDto.getChargeabilityType(), resultResponse)
				|| !SubscriptionCatalogValidator.validateStatus(subscriptionCatalogRequestDto.getStatus(), resultResponse)
				|| !SubscriptionCatalogValidator.validateMandatoryHeaders(userName, resultResponse) 
				|| !SubscriptionCatalogValidator.validateSubscriptionSegment(subscriptionCatalogRequestDto.getSubscriptionSegment(), resultResponse)
			    || !subscriptionManagementControllerHelper.checkUniqueSubscriptionCatalogId(subscriptionCatalogRequestDto.getId(), resultResponse)) {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
			return resultResponse;
		}
				
		try {
			program = programManagement.getProgramCode(headers.getProgram());
			headers.setProgram(program);
			subscriptionManagementControllerHelper.validateAndSaveSubscriptionCatalog(subscriptionCatalogRequestDto, Optional.empty(), SubscriptionManagementConstants.SUBSCRIPTION_CATALOG_INSERT.get(),SubscriptionRequestMappingConstants.CONFIGURE_SUBSCRIPTION_CATALOG, headers, resultResponse);
		} catch (SubscriptionManagementException sme) {
			
			sme.printStackTrace();
			resultResponse.addErrorAPIResponse(sme.getErrorCodeInt(), sme.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
		} catch (MarketplaceException me) {
			me.printStackTrace();
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
		} catch(Exception e) {
			e.printStackTrace();
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "createSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		} 
		LOG.info("Exit configureSubscriptionCatalog");
		return resultResponse;
	}
	
	
	@PostMapping(value = "/subscriptionCatalog/{subscriptionCatalogId}",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse updateSubscriptionCatalog(@RequestBody SubscriptionCatalogInputDto subscriptionCatalogInputDto,
			@PathVariable String subscriptionCatalogId,
			@RequestHeader(value = SubscriptionRequestMappingConstants.ACTION, required = true) String action,
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
		
		LOG.info("Enter configureSubscriptionCatalog : Request :: {}", subscriptionCatalogInputDto);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		SubscriptionCatalogRequestDto subscriptionCatalogRequestDto = subscriptionManagementControllerHelper.validateAndCreateSubscriptionCatalogRequestFromInput(subscriptionCatalogInputDto, resultResponse, headers);
		
		LOG.info("Converted Request :: {}", subscriptionCatalogRequestDto);
		if (!MarketplaceValidator.validateDto(subscriptionCatalogRequestDto, validator, resultResponse)
				|| !SubscriptionCatalogValidator.validateChargeabilityType(subscriptionCatalogRequestDto.getChargeabilityType(), resultResponse)
				|| !SubscriptionCatalogValidator.validateStatus(subscriptionCatalogRequestDto.getStatus(), resultResponse)
				|| !SubscriptionCatalogValidator.validateMandatoryHeaders(userName, resultResponse)) {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
			return resultResponse;
		}
				
		try {
			program = programManagement.getProgramCode(headers.getProgram());
			headers.setProgram(program);
			subscriptionManagementControllerHelper.validateAndUpdateSubscriptionCatalog(subscriptionCatalogRequestDto, subscriptionCatalogId, action, SubscriptionRequestMappingConstants.UPDATE_SUBSCRIPTION_CATALOG, headers, resultResponse);
		} catch (SubscriptionManagementException sme) {
			sme.printStackTrace();
			resultResponse.addErrorAPIResponse(sme.getErrorCodeInt(), sme.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getMsg());
			LOG.error(sme.printMessage());
		} catch (MarketplaceException me) {
			me.printStackTrace();
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getMsg());
			LOG.error(me.printMessage());
		} catch(Exception e) {
			e.printStackTrace();
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "updateSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Exit updateSubscriptionCatalog");
		return resultResponse;
	}
	
	
	@GetMapping(value = "/subscriptionCatalog", consumes = MediaType.ALL_VALUE) 
	public SubscriptionCatalogResultResponse listSubscriptionCatalog(
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter listSubscriptionCatalog");
		SubscriptionCatalogResultResponse subscriptionCatalogResultResponse = new SubscriptionCatalogResultResponse(externalTransactionId);		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		if (!SubscriptionCatalogValidator.validateMandatoryHeaders(userName, subscriptionCatalogResultResponse)) {
			subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
			return subscriptionCatalogResultResponse;
		}
		
		try {
			programManagement.getProgramCode(program);
			//List<SubscriptionCatalog> availableSubscriptionCatalog = subscriptionCatalogRepository.findAll();
			
			//Loyalty as a service.
			if (null == program) program = defaultProgramCode;
			List<SubscriptionCatalog> availableSubscriptionCatalog = subscriptionCatalogRepository.findByProgramCodeIgnoreCase(program);
			
			if (!availableSubscriptionCatalog.isEmpty()) {
				List<SubscriptionCatalogResponseDto> subscriptionCatalogResponseList = new ArrayList<>();
				
				for (SubscriptionCatalog subscriptionCatalog : availableSubscriptionCatalog) {
					SubscriptionCatalogResponseDto subscriptionCatalogResponseDto = modelMapper.map(subscriptionCatalog, SubscriptionCatalogResponseDto.class);
					subscriptionCatalogResponseList.add(subscriptionCatalogResponseDto);
				}	
				subscriptionCatalogResultResponse.setSubscriptionCatalog(subscriptionCatalogResponseList);	
				subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY.getMsg());
			} else {
				subscriptionCatalogResultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getIntId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getMsg());
				subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
			}
		} catch (MarketplaceException me) {
			subscriptionCatalogResultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
			LOG.error(me.printMessage());
		} catch (Exception e) {
			subscriptionCatalogResultResponse.addErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
		}
		
		LOG.info("Exit listSubscriptionCatalog");  
		return subscriptionCatalogResultResponse; 
	}
	
	@GetMapping(value = "/subscriptionCatalog/{subscriptionCatalogId}", consumes = MediaType.APPLICATION_JSON_VALUE) 
	public SubscriptionCatalogResultResponse getSubscriptionCatalog(
			@PathVariable String subscriptionCatalogId,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter getSubscriptionCatalog");
		SubscriptionCatalogResultResponse subscriptionCatalogResultResponse = new SubscriptionCatalogResultResponse(externalTransactionId);		
		if (!SubscriptionCatalogValidator.validateMandatoryHeaders(userName, subscriptionCatalogResultResponse)) {
			subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
			return subscriptionCatalogResultResponse;
		}
		
		try {
			programManagement.getProgramCode(program);
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			List<SubscriptionCatalogResponseDto> subscriptionCatalogResponseList = new ArrayList<>();
			Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
			
			if (subscriptionCatalog.isPresent()) {
				SubscriptionCatalogResponseDto subscriptionCatalogResponseDto = modelMapper.map(subscriptionCatalog.get(), SubscriptionCatalogResponseDto.class);
				subscriptionCatalogResponseList.add(subscriptionCatalogResponseDto);
			
				subscriptionCatalogResultResponse.setSubscriptionCatalog(subscriptionCatalogResponseList);	
				subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY.getMsg());
			} else {
				subscriptionCatalogResultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getIntId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_NOT_FOUND.getMsg());
				subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
			}
		} catch (MarketplaceException me) {
			subscriptionCatalogResultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
		} catch (Exception e) {
			subscriptionCatalogResultResponse.addErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			subscriptionCatalogResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "listSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
			
		}
		
		LOG.info("Exit getSubscriptionCatalog");   
		return subscriptionCatalogResultResponse; 
	}


	public PurchaseResultResponse createSubscription(PurchaseRequestDto purchaseRequestDto, Headers headers) {
		LOG.info("Enter createSubscription :: Request : {} Headers : {}", purchaseRequestDto, headers);
		PurchaseResultResponse resultResponse = new PurchaseResultResponse(headers.getExternalTransactionId());
		if (!MarketplaceValidator.validateDto(purchaseRequestDto, validator, resultResponse)
				|| !SubscriptionCatalogValidator.validateMandatoryHeaders(headers.getUserName(), resultResponse)
				|| !SubscriptionCatalogValidator.validateSpentCriteriaForPaymentMethod(purchaseRequestDto.getSelectedOption(),purchaseRequestDto.getSpentAmount(),purchaseRequestDto.getSpentPoints(),resultResponse)) {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			return resultResponse;
		}
		
		try {
			String program = programManagement.getProgramCode(headers.getProgram());
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			headers.setProgram(program);
			if(ObjectUtils.isEmpty(purchaseRequestDto.getSpentPoints())) {
                purchaseRequestDto.setSpentPoints(0);
            }
            if(ObjectUtils.isEmpty(purchaseRequestDto.getSpentAmount())) {
                purchaseRequestDto.setSpentAmount(0.0);
            }
			subscriptionManagementControllerHelper.validateAndSaveSubscriptionPurchase(purchaseRequestDto, headers, resultResponse);
			
		} catch(Exception e) {
			
			exceptionLogService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), headers.getUserName());
			e.printStackTrace();
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "createSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		purchaseDomain.addErrorToPurchaseHistory(resultResponse, purchaseRequestDto, headers);
		LOG.info("Exit createSubscription");
		return resultResponse;
		
	}
	
	public PurchaseResultResponse createWelcomeGiftSubscription(PurchaseRequestDto purchaseRequestDto, WelcomeGiftRequestDto welcomeGiftRequestDto, Headers headers) {
		LOG.info("Enter createWelcomeGiftSubscription : Request :: {}", purchaseRequestDto);
		PurchaseResultResponse purchaseResultResponse = new PurchaseResultResponse(headers.getExternalTransactionId());
		if (!MarketplaceValidator.validateDto(purchaseRequestDto, validator, purchaseResultResponse)
				|| !SubscriptionCatalogValidator.validateMandatoryHeaders(headers.getUserName(), purchaseResultResponse)) {
			purchaseResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			return null;
		}
		
		try {
			if(ObjectUtils.isEmpty(purchaseRequestDto.getSpentPoints())) {
                purchaseRequestDto.setSpentPoints(0);
            }
            if(ObjectUtils.isEmpty(purchaseRequestDto.getSpentAmount())) {
                purchaseRequestDto.setSpentAmount(0.0);
            }
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			String subscriptionCatalogID = subscriptionManagementControllerHelper.validateWelcomeGiftSubscription(purchaseRequestDto, welcomeGiftRequestDto, headers, purchaseResultResponse);			
			if(null != subscriptionCatalogID) purchaseResultResponse.setResult("SusbscriptionCatalogId",subscriptionCatalogID);
	
		} catch(Exception e) {
			e.printStackTrace();
			purchaseResultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			purchaseResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "createWelcomeGiftSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Exit createWelcomeGiftSubscription :: PurchaseResultResponse : {}", purchaseResultResponse);
		purchaseResultResponse.getResult().getDescription();
		return purchaseResultResponse;
	}
		

	@PostMapping(value = "/manageSubscription",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse manageSubscription(@RequestBody ManageSubscriptionRequestDto manageSubscriptionRequestDto,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter manageSubscription :: Request : {} :: UserName : {} :: ExternalTransactionId : {}", manageSubscriptionRequestDto, userName, externalTransactionId);
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
						
		if (!MarketplaceValidator.validateDto(manageSubscriptionRequestDto, validator, resultResponse)
				|| !SubscriptionCatalogValidator.validateMandatoryHeaders(userName, resultResponse)) {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
			return resultResponse;
		}
		
		try {
			programManagement.getProgramCode(program);
			subscriptionManagementControllerHelper.cancelParkActivateSubscription(manageSubscriptionRequestDto, headers, resultResponse);	
			
		} catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
		} catch(Exception e) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "manageSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Exit manageSubscriptionCatalog");
		return resultResponse;
	}
		
	@PostMapping(value = SubscriptionRequestMappingConstants.MANAGE_RENEWAL,consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse manageRenewal(@RequestBody ManageRenewalRequestDto manageRenewalRequestDto,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter renewalPaymentMethod :: Request : {} :: UserName : {} :: ExternalTransactionId : {}", manageRenewalRequestDto, userName, externalTransactionId);
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
						
		if (!MarketplaceValidator.validateDto(manageRenewalRequestDto, validator, resultResponse)
				|| !SubscriptionCatalogValidator.validateMandatoryHeaders(userName, resultResponse)) {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
			return resultResponse;
		}
		
		
		try {
			programManagement.getProgramCode(program);
			subscriptionManagementControllerHelper.manageDataForSubscriptionRenewal(manageRenewalRequestDto, headers, resultResponse);	
			
		} catch (MarketplaceException me) {
			me.printStackTrace();
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
		} catch(Exception e) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "subscriptionPayment",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Exit subscriptionPayment");
		return resultResponse;
	}
	
	@GetMapping(value = "/subscriptions", consumes = MediaType.APPLICATION_JSON_VALUE) 
	public SubscriptionResultResponse listSubscriptions(
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
			@QueryParam(value = SubscriptionRequestMappingConstants.ACCOUNT_NUMBER) String accountNumber) {
		
		LOG.info("Enter listSubscriptions");
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		SubscriptionResultResponse subscriptionResultResponse = new SubscriptionResultResponse(externalTransactionId);
						
		if (!SubscriptionCatalogValidator.validateMandatoryChannelId(headers.getChannelId(), subscriptionResultResponse)) {
			subscriptionResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED.getMsg());
			return subscriptionResultResponse;
		}
					
		try {
			program = programManagement.getProgramCode(headers.getProgram());
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			headers.setProgram(program);
			subscriptionManagementControllerHelper.validateAndfetchSubscription(accountNumber, headers, subscriptionResultResponse);
		} catch (MarketplaceException me) {
			subscriptionResultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			subscriptionResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			LOG.error(me.printMessage());
		} catch (Exception e) {
			subscriptionResultResponse.addErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			subscriptionResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "listSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
			
		}
		
		LOG.info("listSubscription response parameters : {}", subscriptionResultResponse);  
		return subscriptionResultResponse; 
	}
	
	
	@PostMapping(value = "/subscriptionReport", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) 
	public SubscriptionReportResultResponse subscriptionReport(@RequestBody SubscriptionReportRequestDto subscriptionReportRequestDto,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter SubscriptionReport");
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		SubscriptionReportResultResponse subscriptionReportResultResponse = new SubscriptionReportResultResponse(externalTransactionId);
						
		if (!SubscriptionCatalogValidator.validateMandatoryChannelId(headers.getChannelId(), subscriptionReportResultResponse)) {
			subscriptionReportResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			return subscriptionReportResultResponse;
		}
					
		try {
			program = programManagement.getProgramCode(headers.getProgram());
			headers.setProgram(program);
			List<Subscription> subscriptionReport = subscriptionRepositoryHelper.filterSubscriptionReport(subscriptionReportRequestDto);
			
			if (!subscriptionReport.isEmpty()) {
				List<SubscriptionResponseDto> subscriptionReportResponseList = new ArrayList<>();
				
				for (Subscription subscriptionReportResponse : subscriptionReport) {
					SubscriptionResponseDto subscriptionResponseDto = modelMapper.map(subscriptionReportResponse, SubscriptionResponseDto.class);
					subscriptionReportResponseList.add(subscriptionResponseDto);
				}	
				subscriptionReportResultResponse.setSubscription(subscriptionReportResponseList);	
				subscriptionReportResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTED_SUCCESSFULLY.getMsg());
			} else {
				subscriptionReportResultResponse.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getIntId(),
						SubscriptionManagementCode.SUBSCRIPTION_NOT_FOUND.getMsg());
				subscriptionReportResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			}
		} catch (MarketplaceException me) {
			subscriptionReportResultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			subscriptionReportResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			LOG.error(me.printMessage());
		} catch (Exception e) {
			subscriptionReportResultResponse.addErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			subscriptionReportResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "subscriptionReport",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
			
		}
		
		LOG.info("Exit SubscriptionReport");  
		return subscriptionReportResultResponse; 
	}
	
	@PostMapping(value = "/subscriptionRenewal",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse subscriptionRenewal(
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
			@RequestParam(value = SubscriptionRequestMappingConstants.RENEW_DATE, required = false) Date renewDate) {
		
		LOG.info("Enter subscriptionRenewal");
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
						
		try {
			programManagement.getProgramCode(program);
			
			if(renewDate == null) {
				renewDate = new Date();
			}
			
			List<SubscriptionCatalog> autoRenewalSubscriptionCatalog = subscriptionCatalogDomain.findByChargeabilityType(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get());

			subscriptionManagementControllerHelper.changeStatusForCoolingPeriod(autoRenewalSubscriptionCatalog, renewDate, headers);
			subscriptionManagementControllerHelper.chargeForSubscriptionRenewal(autoRenewalSubscriptionCatalog, renewDate, headers, resultResponse);
			subscriptionManagementControllerHelper.sendSubscriptionRenewalReport(headers);
			
		} catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
		} catch(Exception e) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "subscriptionPayment",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Exit subscriptionRenewal");
		return resultResponse;
	}

	
	
	@GetMapping(value = "/isSubscribed/{accountNumber}") 
	public boolean isSubscribed (
			@PathVariable String accountNumber,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter isSubscribed");
		SubscriptionResultResponse subscriptionResultResponse = new SubscriptionResultResponse(externalTransactionId);
					
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			if(null != accountNumber) {
				LOG.info("Inside isSubscribed :: accountNumber {}",accountNumber);
				Optional<Subscription> subscribedSubscription = subscriptionDomain.findByAccountNumberAndStatus(accountNumber);
				LOG.info("subscribedSubscription{}",subscribedSubscription);
				if(subscribedSubscription.isPresent()) {
					return true;
				}
			}
		} catch (Exception e) {
			subscriptionResultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			subscriptionResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "isSubscribed",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());			
		}
		LOG.info("Exit isSubscribed");
		return false;
	}
	
	@PostMapping(value = "/isSubscribedWithInterest") 
	public SubscriptionWithInterestResultResponse isSubscribedWithInterest (
			@RequestBody SubscriptionWithInterestRequestDto subscriptionWithInterestRequestDto,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter isSubscribedWithInterest");
		SubscriptionWithInterestResultResponse subscriptionWithInterestResultResponse = new SubscriptionWithInterestResultResponse(externalTransactionId);							
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			subscriptionManagementControllerHelper.fetchSubscriptionWithInterest(subscriptionWithInterestRequestDto, subscriptionWithInterestResultResponse);
		} catch (Exception e) {
			subscriptionWithInterestResultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			subscriptionWithInterestResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "isSubscribedWithInterest",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());			
		}
		LOG.info("Exit isSubscribedWithInterest");
		return subscriptionWithInterestResultResponse;
	}
	
	@PostMapping(value = "/subscriptionInfo") 
	public SubscriptionInfoResultResponse subscriptionInfo (
			@RequestBody SubscriptionInfoRequestDto subscriptionInfoRequestDto,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter subscriptionInfo");
		SubscriptionInfoResultResponse subscriptionInfoResultResponse = new SubscriptionInfoResultResponse(externalTransactionId);							
		try {
			Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
			subscriptionManagementControllerHelper.populateSubscriptionInfo(subscriptionInfoRequestDto, subscriptionInfoResultResponse, headers);
		} catch (Exception e) {
			subscriptionInfoResultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			subscriptionInfoResultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "subscriptionInfo",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());			
		}
		LOG.info("Exit subscriptionInfo");
		return subscriptionInfoResultResponse;
	}
	
	
	@PostMapping(value = "/subscriptionPromotion",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse createTimedPromotions(@RequestBody SubscriptionPromotionRequestDto subscriptionPromotionRequestDto,
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
		
		LOG.info("createTimedPromotions request parameters : {}", subscriptionPromotionRequestDto);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		if (!MarketplaceValidator.validateDto(subscriptionPromotionRequestDto, validator, resultResponse)
				|| !SubscriptionCatalogValidator.validateMandatoryHeaders(userName, resultResponse)) {
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getMsg());
			return resultResponse;
		}
				
		try {
			program = programManagement.getProgramCode(program);
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			Date validStartDate = null;
			if(null != subscriptionPromotionRequestDto.getStartDate()) {
				validStartDate = SubscriptionCatalogValidator.validateDate(subscriptionPromotionRequestDto.getStartDate(), resultResponse);
			}			
			Date validEndDate = SubscriptionCatalogValidator.validateDate(subscriptionPromotionRequestDto.getEndDate(), resultResponse);
							
			if(null != validStartDate && validStartDate.after(validEndDate)) {
				resultResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_START_DATE.getIntId(),
						SubscriptionManagementCode.INVALID_START_DATE.getMsg());
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getId(),
						SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED.getMsg());
				return resultResponse;
			}
					
			Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionPromotionRequestDto.getSubscriptionCatalogId());
			if (subscriptionCatalog.isPresent()) {
				if(subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())
						&& subscriptionPromotionRequestDto.getDiscountMonths() <= 0) {
					resultResponse.setErrorAPIResponse(SubscriptionManagementCode.MANDATORY_DISCOUNT_MONTHS.getIntId(),
							SubscriptionManagementCode.MANDATORY_DISCOUNT_MONTHS.getMsg());
					resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getId(),
							SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getMsg());
					return resultResponse;
				}
				subscriptionPromotionDomain.saveSubscriptionPromotion(new SubscriptionPromotionDomain.SubscriptionPromotionBuilder(
						program, subscriptionPromotionRequestDto.getSubscriptionCatalogId(),subscriptionPromotionRequestDto.getValidity(), validStartDate, validEndDate, 
						subscriptionPromotionRequestDto.getDiscountPercentage(), subscriptionPromotionRequestDto.getFlatRate(),
						subscriptionCatalog.get().getChargeabilityType(), subscriptionPromotionRequestDto.getDiscountMonths(), new Date(), userName, new Date(), userName).build());
				resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATED.getId(), SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATED.getMsg());
				resultResponse.setSuccessAPIResponse();
			}	
			
		} catch (SubscriptionManagementException sme) {
			resultResponse.addErrorAPIResponse(sme.getErrorCodeInt(), sme.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getMsg());
			LOG.error(sme.printMessage());
		} catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getMsg());
			LOG.error(me.printMessage());
		} catch(Exception e) {
			resultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "createSubscriptionPromotion",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED).printMessage());
		} 
		LOG.info("createSubscriptionPromotion response parameters : {}", resultResponse);
		return resultResponse;
	}
	
	
	@PostMapping(value = "/subscriptionCatalog/partner/promoCode",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PartnerPromoCodeDetails generatePromoCode(@RequestBody PartnerPromocodeRequest promoCodeRequestDto,
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
		
		LOG.info("Enter generatePromoCode :: Request : {}", promoCodeRequestDto);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		PartnerPromoCodeDetails partnerPromoCodeDetails = new PartnerPromoCodeDetails(externalTransactionId);
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		try {
			partnerPromoCodeDetails = promoCodeDomain.createPartnerPromoCodeInBulk(promoCodeRequestDto, resultResponse, program, userName, header);
		} catch(Exception e) {
			partnerPromoCodeDetails.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			partnerPromoCodeDetails.setResult(SubscriptionManagementCode.GENERATE_PROMOCODE_FAILED.getId(),
					SubscriptionManagementCode.GENERATE_PROMOCODE_FAILED.getMsg());
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "generatePromoCode",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERATE_PROMOCODE_FAILED).printMessage());
		} 
		LOG.info("Exit generatePromoCode");	
		return partnerPromoCodeDetails;
		
	}
	
	@PostMapping(value = "/cancelExpiredSubscriptions",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void cancelExpiredSubscriptions(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter cancelExpiredSubscriptions");
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			subscriptionManagementControllerHelper.cancelExpiredOneTimeSubscriptions();
		} catch (Exception e) {
			LOG.error(new SubscriptionManagementException(this.getClass().toString(), "cancelExpiredSubscriptions",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}		
		
		LOG.info("Exit cancelExpiredSubscriptions");			
	}
	
	
	
	@PostMapping(value = "/resetSubscriptionCatalog", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void resetSubscriptionCatalog() {
	    
	    subscriptionCatalogRepository.deleteAll();
	    
	}

	@PostMapping(value = "/resetSubscription", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void resetSubscription() {
	    
	    subscriptionRepository.deleteAll();
	    
	}
	
	@PostMapping(value = "/notifyMemberForSubscription",consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void generatePromoCode(
			@RequestHeader(value = "promoCode", required = false) String promoCode,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		subscriptionService.notifyMemberBySMS(alertdestinationNumbers, promoCode, transactionId);
	}
	
	@GetMapping(value = "/cuisines") 
	public CuisinesResultResponse getCuisines (
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Enter getCuisines");
		CuisinesResultResponse cuisinesResultResponse = new CuisinesResultResponse(externalTransactionId);		
		try {
			if (null == program) program = defaultProgramCode;
			subscriptionManagementControllerHelper.fetchCuisines(cuisinesResultResponse);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Exception occured in getCuisines()");
			cuisinesResultResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			cuisinesResultResponse.setResult(SubscriptionManagementCode.CUISINES_LISTING_FAILED.getId(),
					SubscriptionManagementCode.CUISINES_LISTING_FAILED.getMsg());		
		}
		LOG.info("Exit getCuisines");
		return cuisinesResultResponse;
	}
	
	@PostMapping(value = "/validatePromoAndProvideGift") 
	public GiftApiResponse giftAPI(
			@RequestBody GiftApiRequest validatePromoCodeRequest,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		
		LOG.info("Entering /validatePromoAndProvideGift API");
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		GiftApiResponse giftApiResponse = new GiftApiResponse(externalTransactionId);
		PromoCodeApplyAndValidate resultResponse = new PromoCodeApplyAndValidate();
		try {
			if (null == program) program = defaultProgramCode;
			if(ObjectUtils.isEmpty(validatePromoCodeRequest.getAccountNumber()) || (ObjectUtils.isEmpty(validatePromoCodeRequest.getPromoCodeDetails()) && ObjectUtils.isEmpty(validatePromoCodeRequest.getSubscriptionCatalogId()))) {
				giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.INVALID_PARAMETERS.getIntId(), SubscriptionManagementCode.INVALID_PARAMETERS.getMsg());
				return giftApiResponse;
			}
			Boolean isBinBased = !ObjectUtils.isEmpty(validatePromoCodeRequest.getSubscriptionCatalogId()) ? true : false;
			
			giftApiResponse = subscriptionManagementControllerHelper.populateEligibleGiftDetails(validatePromoCodeRequest, isBinBased, resultResponse, giftApiResponse, headers);
				
		} catch (Exception e) {
			e.printStackTrace();
			giftApiResponse.setErrorAPIResponse(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());	
		}
		LOG.info("Exit /validatePromoAndProvideGift API");
		return giftApiResponse;
	}
	
}
