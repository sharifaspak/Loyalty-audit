package com.loyalty.marketplace.offers.inbound.restcontroller;

import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.gifting.helper.GiftingControllerHelper;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.domain.model.BirthdayInfoDomain;
import com.loyalty.marketplace.offers.domain.model.OfferCatalogDomain;
import com.loyalty.marketplace.offers.domain.model.OfferRatingDomain;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.domain.model.WishlistDomain;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.BirthdayInfoRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersFiltersRequest;
import com.loyalty.marketplace.offers.inbound.dto.EligibleOffersRequest;
import com.loyalty.marketplace.offers.inbound.dto.OfferCatalogDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferCountDto;
import com.loyalty.marketplace.offers.inbound.dto.OfferRatingDto;
import com.loyalty.marketplace.offers.inbound.dto.PromotionalGiftRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.RefundTransactionRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.TransactionRequestDto;
import com.loyalty.marketplace.offers.inbound.dto.WishlistRequestDto;
import com.loyalty.marketplace.offers.outbound.dto.BirthdayInfoResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.EligibleOffersResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogShortResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.PaymentMethodResponse;
import com.loyalty.marketplace.offers.outbound.dto.PromotionalGiftResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.RefundTransactionResponse;
import com.loyalty.marketplace.offers.outbound.dto.RestaurantResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.TransactionsResultResponse;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.OfferValidator;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.utils.Logs;

import io.swagger.annotations.Api;

/**
 * 
 * @author jaya.shukla
 *
 */
@RestController
@Api(value = RequestMappingConstants.MARKETPLACE)
@RequestMapping(RequestMappingConstants.MARKETPLACE_BASE)
public class OffersController{

	private static final Logger LOG = LoggerFactory.getLogger(OffersController.class);
	private static final String LIST_ALL_REFUND_TRANSACTIONS = "listAllRefundTransactions";
	
	@Autowired
	private OfferCatalogDomain offerCatalogDomain;
	
	@Autowired
    private OfferRatingDomain offerRatingDomain;
	
	@Autowired
	private WishlistDomain wishlistDomain;
	
	@Autowired
	private BirthdayInfoDomain birthdayInfoDomain;
	
	@Autowired
	private PurchaseDomain purchaseDomain;
	
	@Autowired
	private SubscriptionManagementController subscriptionManagementController;
	
	@Autowired
	private GiftingControllerHelper giftingControllerHelper;
	
	@Autowired
	private OffersHelper helper;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private ExceptionLogsService exceptionLogsService;
	
	@Value(OffersConfigurationConstants.IS_BATCH_TOGGLE)
	private boolean batchToggle;
		
	//Changes for loyalty as a service.
	@Value(OffersConfigurationConstants.DEFAULT_PROGRAM_CODE)
	private String defaultProgramCode;
	
	/**
	 * API to create a new offer
	 * @param offerCatalogRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return status of offer creation
	 */
	@PostMapping(value = OffersRequestMappingConstants.CREATE_OFFER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse createOffer(@RequestBody OfferCatalogDto offerCatalogRequest,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.CREATE_OFFER_METHOD.get());
		LOG.info(log);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		log = Logs.logForRequest(offerCatalogRequest);
		LOG.info(log);

		ResultResponse resultResponse = offerCatalogDomain.validateAndSaveOffer(offerCatalogRequest, 
										header);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.CREATE_OFFER_METHOD.get());
		LOG.info(log);
		
		if(Checks.checkSuccessResponse(resultResponse)) {
			offerCatalogDomain.getAndSaveEligibleOffers(header);
		}
		return resultResponse;

	}
		
	/**
	 * API to update/activate/deactivate an existing offer
	 * @param offerCatalogRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param role
	 * @param offerId
	 * @param userRole
	 * @return status for offer update
	 */
	@PostMapping(value = OffersRequestMappingConstants.UPDATE_OFFER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse updateOffer(@RequestBody OfferCatalogDto offerCatalogRequest,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@PathVariable(value = OffersRequestMappingConstants.OFFER_ID, required = true) String offerId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole){
		
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.UPDATE_SINGLE_OFFER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		log = Logs.logForRequest(offerCatalogRequest);
		LOG.info(log);
		OfferCountDto offerCount = new OfferCountDto();
		ResultResponse resultResponse = offerCatalogDomain.validateAndUpdateOffer(offerCatalogRequest, offerId, 
				header, userRole, offerCount);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.UPDATE_SINGLE_OFFER.get());
		LOG.info(log);
		
		if(Checks.checkSuccessResponse(resultResponse)) {
			offerCatalogDomain.getAndSaveEligibleOffers(header);
			offerCatalogDomain.updateOfferCountInMerchant(offerCatalogRequest,offerCount);
		}
		return resultResponse;
		
	}

	/**
	 * API to retrieve list of all offers for administrator
	 * @param page
	 * @param limit
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of filtered offers for administrator
	 */
	@GetMapping(value = OffersRequestMappingConstants.GET_OFFER_LIST_ADMINISRATOR, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OfferCatalogShortResultResponse listAdminOffers(
			@RequestParam(value = OffersRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = OffersRequestMappingConstants.LIMIT, required = false) Integer limit,
			@RequestParam(value = OffersRequestMappingConstants.OFFER_TYPE, required = false) String offerType,
			@RequestParam(value = OffersRequestMappingConstants.STATUS, required = false) String status,
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

		 String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ALL_OFFERS.get());
		 LOG.info(log);
		
		 //Changes for loyalty as a service.
		 if (null == program) program = defaultProgramCode;
		 
		 OfferCatalogShortResultResponse offerCatalogResultResponse = offerCatalogDomain.getAllOffersForAdministrator(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
	   				channelId, systemId, systemPassword, token, transactionId), page, limit, offerType, status);
		
		 log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ALL_OFFERS.get());
		 LOG.info(log);
		
		 return offerCatalogResultResponse;
	}
	
	/**
	 * API to retrieve list of all offers for a partner
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param partnerCode
	 * @return list of filtered offers for partner
	 */
	@GetMapping(value = OffersRequestMappingConstants.GET_OFFER_LIST_PARTNER, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OfferCatalogShortResultResponse listAllOffersForPartner(
			@RequestParam(value = OffersRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = OffersRequestMappingConstants.LIMIT, required = false) Integer limit,
			@RequestParam(value = OffersRequestMappingConstants.OFFER_TYPE, required = false) String offerType,
			@RequestParam(value = OffersRequestMappingConstants.STATUS, required = false) String status,
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
			@PathVariable String partnerCode) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_PARTNER_OFFER.get());
		LOG.info(log);
		
		log = Logs.logForVariable(OfferConstants.PARTNER_CODE_VARIABLE.get(), partnerCode);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		OfferCatalogShortResultResponse offerCatalogResultResponse = 
						offerCatalogDomain.getAllOffersForPartner(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				   				channelId, systemId, systemPassword, token, transactionId), partnerCode, page, limit, offerType, status);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ALL_OFFERS.get());
		LOG.info(log);
		
		return offerCatalogResultResponse;
	}

	/**
	 * API to retrieve list of all offers for a merchant
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param merchantCode
	 * @return list of filtered offers for merchant
	 */
	@GetMapping(value = OffersRequestMappingConstants.GET_OFFER_LIST_MERCHANT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OfferCatalogShortResultResponse listAllOffersForMerchant(
			@RequestParam(value = OffersRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = OffersRequestMappingConstants.LIMIT, required = false) Integer limit,
			@RequestParam(value = OffersRequestMappingConstants.OFFER_TYPE, required = false) String offerType,
			@RequestParam(value = OffersRequestMappingConstants.STATUS, required = false) String status,
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
			@PathVariable String merchantCode) {
		
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_MERCHANT_OFFER.get());
		LOG.info(log);
		
		log = Logs.logForVariable(OfferConstants.MERCHANT_CODE_VARIABLE.get(), merchantCode);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		OfferCatalogShortResultResponse offerCatalogResultResponse = offerCatalogDomain.getAllOffersForMerchant(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
   				channelId, systemId, systemPassword, token, transactionId),
				merchantCode, page, limit, offerType, status);
	
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_MERCHANT_OFFER.get());
		LOG.info(log);
		
		return offerCatalogResultResponse;
	}
	
	/**
	 * API to retrieve detail of a specific offer for administrator/ partner/ merchant
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param offerId
	 * @return detailed offer response for the offerId
	 */
	@GetMapping(value = OffersRequestMappingConstants.GET_OFFER_DETAIL_PORTAL, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OfferCatalogResultResponse getSpecificOfferDetails(
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
			@PathVariable String offerId) {
		
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_DETAIL_OFFER.get());
		LOG.info(log);
		
		log = Logs.logForVariable(OfferConstants.OFFER_ID_VARIABLE.get(), offerId);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		OfferCatalogResultResponse offerCatalogResultResponse = 
				offerCatalogDomain.getDetailedOfferPortal(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
		   				channelId, systemId, systemPassword, token, transactionId), offerId);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_DETAIL_OFFER.get());
		LOG.info(log);
		
		return offerCatalogResultResponse;
		
	}

	/**
	 * API to retrieve list of all eligible payments methods for a service and member
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param purchaseItem
	 * @param accountNumber
	 * @return list of eligible payment methods
	 */
	@GetMapping(value = OffersRequestMappingConstants.GET_ELIGIBLE_PAYMENT_METHOD, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PaymentMethodResponse listEligiblePaymentMethods(
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
			@PathVariable(value = OffersRequestMappingConstants.PURCHASE_ITEM,required = true) String purchaseItem,
			@RequestParam(value = OffersRequestMappingConstants.ACCOUNT_NUMBER,required = true) String accountNumber) {
		
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ELIGIBLE_PAYMENT_METHODS.get());
		LOG.info(log);
		
		log = Logs.logForVariable(OfferConstants.OFFER_ID_VARIABLE.get(), purchaseItem);
		LOG.info(log);
		
		log = Logs.logForVariable(OfferConstants.ACCOUNT_NUMBER_VARIABLE.get(), accountNumber);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		PaymentMethodResponse paymentMethodResponse = offerCatalogDomain.getEligiblePaymentMethods(purchaseItem, 
				accountNumber, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
		   				channelId, systemId, systemPassword, token, transactionId));
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ELIGIBLE_PAYMENT_METHODS.get());
		LOG.info(log);
		
	    return paymentMethodResponse;
	}
		
    /**
	 * API to retrieve list of all purchase transactions
	 * @param transactionRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of all purchase transactions
	 */
	@PostMapping(value = OffersRequestMappingConstants.GET_ALL_TRANSACTIONS, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TransactionsResultResponse listAllTransactions(@RequestBody TransactionRequestDto transactionRequest, 
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

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ALL_TRANSACTIONS.get());
		LOG.info(log);
		
		log = Logs.logForRequest(transactionRequest);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		 if (null == program) program = defaultProgramCode;
		
		TransactionsResultResponse  transactionsResultResponse = purchaseDomain.getAllPurchaseTransactionsWithVoucherDetails(transactionRequest, 
				new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
   				channelId, systemId, systemPassword, token, transactionId));
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_ALL_TRANSACTIONS.get());
		LOG.info(log);
		
		return transactionsResultResponse;
	}
	
	/**
	 * 
	 * @param offerType
	 * @return list of all merchants with offers configured of specific offertype
	 */
	@GetMapping(value = OffersRequestMappingConstants.OFFER_TYPE_DETAIL, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> listMerchantsWithOfferType(@PathVariable(value = OffersRequestMappingConstants.OFFER_TYPE, required = true) String offerType) {
		
		List<String> merchantIds=null;
		merchantIds= offerCatalogDomain.getAllMerchantCodesForOffer(offerType);
		return merchantIds;
		
	}
	
	/**
	 * API to rate any existing offer
	 * @param offerRatingRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param userPrev
	 * @param sessionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return status after rating the offer
	 */
	@PostMapping(value = OffersRequestMappingConstants.RATE_OFFER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse rateOffer(@RequestBody OfferRatingDto offerRatingRequest,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.RATE_OFFERS.get());
		LOG.info(log);
		
		log = Logs.logForRequest(offerRatingRequest);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		 if (null == program) program = defaultProgramCode;
		
		ResultResponse resultResponse = offerRatingDomain.validateAndSaveOfferRating(offerRatingRequest, new Headers(program, authorization, externalTransactionId, userName, sessionId,
				userPrev, channelId, systemId, systemPassword, token, transactionId));

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.RATE_OFFERS.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	/**
	 * API to send alerts to birthday accounts
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return status after sending birthday alerts
	 */
	@GetMapping(value = OffersRequestMappingConstants.BATCH_PROCESS_FOR_BIRTHDAY_OFFERS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse publishBirthdayGiftEvent(
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

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.PUBLISH_BIRTHDAY_GIFT_EVENT.get());
		LOG.info(log);
		
		ResultResponse resultResponse = null;
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		 
		if(batchToggle) {
		
			resultResponse = offerCatalogDomain.sendBirthdayGiftAlerts(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
			
		}
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.PUBLISH_BIRTHDAY_GIFT_EVENT.get());
		LOG.info(log);
		
		return resultResponse;
	}
	
	/**
	 * API to create/update wishlist for a member
	 * @param wishlistRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param accountNumber
	 * @return status after creating/updating wishlist for the member
	 */
	@PostMapping(value = OffersRequestMappingConstants.CREATE_UPDATE_WISHLIST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureWishlist(@RequestBody WishlistRequestDto wishlistRequest,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@PathVariable(value = OffersRequestMappingConstants.ACCOUNT_NUMBER, required = true) String accountNumber) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.CONFIGURE_WISHLIST_CONTROLLER.get());
		LOG.info(log);
		
		log = Logs.logForRequest(wishlistRequest);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = wishlistRequest.getAction().equalsIgnoreCase(OfferConstants.ADD_ACTION.get())
				? wishlistDomain.validateAndSaveOfferToWishlist(wishlistRequest, accountNumber, headers)
				: wishlistDomain.validateAndRemoveOfferFromWishlist(wishlistRequest, accountNumber, headers);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.CONFIGURE_WISHLIST_CONTROLLER.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	/**
	 * API to configure static birthday info
	 * @param birthayInfoRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return status after configuring birthday information
	 */
	@PostMapping(value = OffersRequestMappingConstants.CONFIGURE_BIRTHDAY_INFORMATION, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureBirthdayInfo(@RequestBody BirthdayInfoRequestDto birthayInfoRequest,
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

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.CONFIGURE_BIRTHDAY_INFO_CONTROLLER.get());
		LOG.info(log);
		
		log = Logs.logForRequest(birthayInfoRequest);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		ResultResponse resultResponse = birthdayInfoDomain.configureBirthdayInfo(birthayInfoRequest, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.CONFIGURE_BIRTHDAY_INFO_CONTROLLER.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	/**
	 * API to retrieve static birthday information for an account
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param accountNumber
	 * @return static birthday information for member
	 */
	@GetMapping(value = OffersRequestMappingConstants.RETRIEVE_BIRTHDAY_INFORMATION_ACCOUNT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BirthdayInfoResultResponse retrieveBirthdayInfoAccount(
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
			@PathVariable(value = OffersRequestMappingConstants.ACCOUNT_NUMBER, required = true) String accountNumber) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_BIRTHDAY_INFO_ACCOUNT_CONTROLLER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		BirthdayInfoResultResponse resultResponse = birthdayInfoDomain.getBirthdayInfoForAccount(accountNumber, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_BIRTHDAY_INFO_ACCOUNT_CONTROLLER.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	/**
	 * API to retrieve static birthday information for an admin
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param accountNumber
	 * @return static birthday information for administrator
	 */
	@GetMapping(value = OffersRequestMappingConstants.RETRIEVE_BIRTHDAY_INFORMATION_ADMIN, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BirthdayInfoResultResponse retrieveBirthdayInfoAdmin(
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

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_BIRTHDAY_INFO_ADMIN_CONTROLLER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		BirthdayInfoResultResponse resultResponse = birthdayInfoDomain.getBirthdayInfoForAdmin(new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_BIRTHDAY_INFO_ADMIN_CONTROLLER.get());
		LOG.info(log);
		
		return resultResponse;

	}
	
	/**
	 * API to configure all eligible offers
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of all eligible offers
	 */
	@GetMapping(value = OffersRequestMappingConstants.CONFIGURE_ELIGIBLE_OFFER, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultResponse configureEligibleOffers(
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

    	String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.CONFIGURE_ELIGIBLE_OFFER.get());
		LOG.info(log);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		offerCatalogDomain.getAndSaveEligibleOffers(new Headers(program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
        
		Responses.setResponse(resultResponse, OfferErrorCodes.ELIGIBLE_OFFERS_CONFIGURATION_FAILED, OfferSuccessCodes.ELIGIBLE_OFFERS_CONFIGURATION_SUCCESSFULL);

        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.CONFIGURE_ELIGIBLE_OFFER.get());
		LOG.info(log);
        
        return resultResponse;
    
    }
	
	/**
	 * API to retrieve current list of eligible offers in singleton class
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of eligible offers currently in singleton class
	 */
	@GetMapping(value = OffersRequestMappingConstants.RETRIEVE_SINGLETON_OFFERS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EligibleOffersResultResponse getEligibleOffersInSingleton(
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

    	String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_SINGLETON_OFFER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		EligibleOffersResultResponse resultResponse = helper.getEligibleOfferListInContext(new Headers(program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
        
        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_SINGLETON_OFFER.get());
		LOG.info(log);
        
        return resultResponse;
    
    }
	
	/**
	 * API to reset all offer and gifting counters
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 */
	@PostMapping(value = OffersRequestMappingConstants.RESET_ALL_COUNTER, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse resetAllCounters(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
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
	    
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.RESET_ALL_OFFER_COUNTERS_METHOD.get());
		LOG.info(log);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			if(batchToggle) {
				
				Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
		   				channelId, systemId, systemPassword, token, transactionId);
			    
				helper.resetCounters(resultResponse);
			    giftingControllerHelper.resetGiftingCounters(headers, resultResponse);
			    
			}
		} catch(Exception e) {
			e.printStackTrace();
			exceptionLogsService.saveExceptionsToExceptionLogs(e, externalTransactionId, null, userName);
			resultResponse.addErrorAPIResponse(OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					OfferErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(OfferErrorCodes.RESET_ALL_COUNTER_FAILURE.getId(),
					OfferErrorCodes.RESET_ALL_COUNTER_FAILURE.getMsg());
		}
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.RESET_ALL_OFFER_COUNTERS_METHOD.get());
		LOG.info(log);
		
		return resultResponse;
		
	}
	
	/**
	 * API to purchase a marketplace item
	 * @param purchaseRequestDto
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return purchase response
	 */
	@PostMapping(value = OffersRequestMappingConstants.PURCHASE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PurchaseResultResponse createPurchaseDetails(@RequestBody PurchaseRequestDto purchaseRequestDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = true) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
         
		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.MAKE_PURCHASE_CONTROLLER.get());
		LOG.info(log);
		
		log = Logs.logForRequest(purchaseRequestDto);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		PurchaseResultResponse purchaseResultResponse = new PurchaseResultResponse(externalTransactionId);
		
		if (!OfferValidator.validateDto(purchaseRequestDto, validator, purchaseResultResponse)) {
			
			purchaseResultResponse.setResult(OfferErrorCodes.PURCHASE_ITEM_FAILED.getId(), OfferErrorCodes.PURCHASE_ITEM_FAILED.getMsg());
			return purchaseResultResponse;
		}
		
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
   				channelId, systemId, systemPassword, token, transactionId);	
		String paymentTransactionId = purchaseRequestDto.getExtTransactionId();
				
		purchaseResultResponse =(purchaseRequestDto.getSelectedPaymentItem().equalsIgnoreCase(OffersConfigurationConstants.subscriptionItem))
                ? subscriptionManagementController.createSubscription(purchaseRequestDto, headers)
                : purchaseDomain.validateAndSavePurchaseHistory(purchaseRequestDto,
                   purchaseResultResponse, headers);	
        purchaseRequestDto.setExtTransactionId(paymentTransactionId);        
        purchaseDomain.checkAndPerformRefund(purchaseResultResponse, purchaseRequestDto, headers);        
        
        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.MAKE_PURCHASE_CONTROLLER.get());
		LOG.info(log);
        
		return purchaseResultResponse;

	}
	
	/**
	 * API to return all eligible offers
	 * @param eligibleOffersParameters
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of all eligible offers
	 */
	@PostMapping(value = OffersRequestMappingConstants.GET_OFFER_LIST_MEMBER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OfferCatalogResultResponse listEligibleOffersForMembers(@RequestBody EligibleOffersFiltersRequest eligibleOffersParameters,
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

    	String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_MEMBER_ELIGIBLE_OFFER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		eligibleOffersParameters = ObjectUtils.isEmpty(eligibleOffersParameters)
                ? new EligibleOffersFiltersRequest()
                : eligibleOffersParameters;
		
		log = Logs.logForRequest(eligibleOffersParameters);
		LOG.info(log);
    	
		OfferCatalogResultResponse offerCatalogResultResponse = offerCatalogDomain.listEligibleOffers(eligibleOffersParameters, new Headers(program, authorization, 
				externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));

        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_MEMBER_ELIGIBLE_OFFER.get());
		LOG.info(log);
        
        return offerCatalogResultResponse;
    
    }
	
	/**
	 * API to retrieve list of all active and eligible offers from wishlist
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param accountNumber
	 * @return list of eligible offers from member's wishlist
	 */
	@GetMapping(value = OffersRequestMappingConstants.GET_FROM_WISHLIST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OfferCatalogResultResponse getFromWishlist(
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@PathVariable(value = OffersRequestMappingConstants.ACCOUNT_NUMBER, required = true) String accountNumber) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.GET_WISHLIST_CONTROLLER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		OfferCatalogResultResponse wishlistResponse = wishlistDomain.getActiveOffersFromWishlist(accountNumber, new Headers(program, authorization, externalTransactionId, 
				userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.GET_WISHLIST_CONTROLLER.get());
		LOG.info(log);
		
		return wishlistResponse;

	}

	/**
	 * API to retrieve list of all birthday offers
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param accountNumber
	 * @return list of eligible birthday offers
	 */
	@GetMapping(value = OffersRequestMappingConstants.GET_MEMBER_BIRTHDAY_GIFTS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OfferCatalogResultResponse listMemberBirthdayGiftOffers(
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestParam(value = OffersRequestMappingConstants.ACCOUNT_NUMBER, required = true) String accountNumber) {

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.BIRTHDAY_OFFER_METHOD.get());
		LOG.info(log);
		
		log = Logs.logForVariable(OfferConstants.ACCOUNT_NUMBER_VARIABLE.get(), accountNumber);
		LOG.info(log);
		
		log = Logs.logForVariable(OfferConstants.CHANNEL_ID_VARIABLE.get(), channelId);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		OfferCatalogResultResponse offerCatalogResultResponse = offerCatalogDomain
				.getAllEligibleBirthdayOffers(accountNumber, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, 
						systemId, systemPassword, token, transactionId));

		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.BIRTHDAY_OFFER_METHOD.get());
		LOG.info(log);
			
		return offerCatalogResultResponse;
		
	}
	
	/**
     * API to retrieve details of an eligible offer for a member
     * @param eligibleOffersParameters
     * @param program
     * @param authorization
     * @param externalTransactionId
     * @param userName
     * @param sessionId
     * @param userPrev
     * @param channelId
     * @param systemId
     * @param systemPassword
     * @param token
     * @param transactionId
     * @param offerId
     * @return detailed eligible offer
     */
    @PostMapping(value = OffersRequestMappingConstants.GET_OFFER_DETAIL_MEMBER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OfferCatalogResultResponse getDetailedOfferForMember(@RequestBody EligibleOffersRequest eligibleOffersParameters, 
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
            @RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
            @PathVariable(value = OffersRequestMappingConstants.OFFER_ID, required = true) String offerId) {

    	String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_DETAIL_ELIGIBLE_OFFER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		eligibleOffersParameters = ObjectUtils.isEmpty(eligibleOffersParameters)
                ? new EligibleOffersRequest()
                : eligibleOffersParameters;
		
		log = Logs.logForRequest(eligibleOffersParameters);
		LOG.info(log);
    	
    	OfferCatalogResultResponse  offerCatalogResultResponse = 
                offerCatalogDomain.listSpecificOfferForMember(eligibleOffersParameters.getAccountNumber(), new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
           				channelId, systemId, systemPassword, token, transactionId), offerId);
        
        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.LIST_DETAIL_ELIGIBLE_OFFER.get());
		LOG.info(log);
        
        return offerCatalogResultResponse;
    }
    
    @PostMapping(value = OffersRequestMappingConstants.PROMOTIONAL_GIFT, consumes = MediaType.ALL_VALUE)
	public PromotionalGiftResultResponse sendPromotionalGift(
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
			@RequestBody PromotionalGiftRequestDto promotionalGiftRequest) {

    	String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.PROMOTIONAL_GIFT_CONTROLLER.get());
		LOG.info(log);
		
		log = Logs.logForRequest(promotionalGiftRequest);
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		PromotionalGiftResultResponse promotionalGiftResultResponse = offerCatalogDomain.givePromotionalGift(promotionalGiftRequest, new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
   				channelId, systemId, systemPassword, token, transactionId));	
		
        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.PROMOTIONAL_GIFT_CONTROLLER.get());
		LOG.info(log);
        
		return promotionalGiftResultResponse;

	}
    


    /***
	 * 
	 * @param transactionRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of all refund transactions
	 */
	@PostMapping(value = OffersRequestMappingConstants.GET_REFUND_TRANSACTIONS, consumes = MediaType.APPLICATION_JSON_VALUE)
	public RefundTransactionResponse listAllRefundTransactions(@RequestBody RefundTransactionRequestDto transactionRequest, 
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

		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), LIST_ALL_REFUND_TRANSACTIONS);
		LOG.info(log);
		

		log = Logs.logForRequest(transactionRequest);
		LOG.info(log);
		

		//Changes for loyalty as a service.
		 if (null == program) program = defaultProgramCode;
		 
		 RefundTransactionResponse  transactionsResultResponse = purchaseDomain.getAllRefundTransactions(transactionRequest, 
				new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
   				channelId, systemId, systemPassword, token, transactionId));
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), LIST_ALL_REFUND_TRANSACTIONS);
		LOG.info(log);
		
		return transactionsResultResponse;
	}
	
	@PostMapping(value = OffersRequestMappingConstants.OFFER_CATALOG_BULK_UPDATE, consumes = MediaType.ALL_VALUE)
	public ResultResponse updateOfferCatalog(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

    		String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.OFFER_CATALOG_BULK_UPDATE.get());
		LOG.info(log);
		

		if (StringUtils.isEmpty(program)) program = defaultProgramCode;
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		resultResponse = offerCatalogDomain.processOfferCatalogContent(OfferConstants.OFFER_CATALOG_BULK_UPDATE.get(), headers, resultResponse);
		
		log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.OFFER_CATALOG_BULK_UPDATE.get());
		LOG.info(log);
		
		return resultResponse;
		
	}
	
	@GetMapping(value = OffersRequestMappingConstants.RETRIEVE_RESTAURANTS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantResultResponse listAllOfferRestaurants(
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

    	String log = Logs.logForEnteringControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_SINGLETON_OFFER.get());
		LOG.info(log);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		RestaurantResultResponse resultResponse = offerCatalogDomain.getAllRestaurants(new Headers(program,
				authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId));
        
        log = Logs.logForLeavingControllerMethod(this.getClass().getSimpleName(), OfferConstants.RETRIEVE_SINGLETON_OFFER.get());
		LOG.info(log);
        
        return resultResponse;
    
    }

   	
}
