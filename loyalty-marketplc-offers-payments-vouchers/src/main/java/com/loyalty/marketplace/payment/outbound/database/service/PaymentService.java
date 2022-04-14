package com.loyalty.marketplace.payment.outbound.database.service;

import java.io.StringReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.equivalentpoints.domain.EquivalentPointsDomain;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.EquivalentPointsDto;
import com.loyalty.marketplace.equivalentpoints.inbound.restcontroller.EquivalentPointsController;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ListRedemptionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.PartnerResultResponse;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionRate;
import com.loyalty.marketplace.equivalentpoints.utils.EquivalentPointsException;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.inbound.dto.UpdateWelcomeGiftReceivedFlagRequest;
import com.loyalty.marketplace.offers.member.management.outbound.dto.AccountInfoDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiError;
import com.loyalty.marketplace.offers.member.management.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.WelcomeGift;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.SubOffer;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoCodeDMRequest;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.FilterValues;
import com.loyalty.marketplace.offers.utils.Predicates;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.constants.MarketplaceCode;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.inbound.dto.ATGEnrollmentDTO;
import com.loyalty.marketplace.payment.inbound.dto.MemberActivityPaymentDto;
import com.loyalty.marketplace.payment.inbound.dto.PaymentAdditionalRequest;
import com.loyalty.marketplace.payment.inbound.dto.RollbackAccrualOrRedemptionDto;
import com.loyalty.marketplace.payment.inbound.dto.PaymentAdditionalRequest;
import com.loyalty.marketplace.payment.inbound.dto.ATGEnrollmentDTO;
import com.loyalty.marketplace.payment.outbound.database.entity.CreditCardTransaction;
import com.loyalty.marketplace.payment.outbound.database.entity.PaymentValues;
import com.loyalty.marketplace.payment.outbound.database.entity.ThirdPartyCallLog;
import com.loyalty.marketplace.payment.outbound.database.repository.SmilesPaymentRepository;
import com.loyalty.marketplace.payment.outbound.database.repository.ThirdPartyCallLogRepository;
import com.loyalty.marketplace.payment.outbound.dto.MemberActivityResponse;
import com.loyalty.marketplace.payment.outbound.dto.PaymentGatewayResponseResult;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.payment.outbound.dto.UpdateReferralBonusDto;
import com.loyalty.marketplace.payment.utils.GetPaymentRequest;
import com.loyalty.marketplace.payment.utils.GetPaymentResponse;
import com.loyalty.marketplace.payment.utils.SOAPConnector;
import com.loyalty.marketplace.payment.utils.Utils;
import com.loyalty.marketplace.service.CFMService;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.dto.CFMRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherController;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherCancelRequestDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherRequestDto;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResult;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResultResponse;

import ae.billingadapter.etisalat._1.ChargeImmediatelyResponse;
import ae.etisalat.middleware.bwtemplate.webservicename.GenericPaymentService;
import ae.etisalat.middleware.bwtemplate.webservicename.GenericPaymentServiceServiceagent;
import ae.etisalat.middleware.bwtemplate.webservicename.PaymentFault;
import ae.etisalat.middleware.genericpaymentservice.adjustmentpostingrequest.AdjustmentPostingRequest;
import ae.etisalat.middleware.genericpaymentservice.adjustmentpostingrequest.AdjustmentPostingRequest.DataHeader;
import ae.etisalat.middleware.genericpaymentservice.adjustmentpostingresponse.AdjustmentPostingResponse;
import ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest.MiscPaymentRequest;
import ae.etisalat.middleware.genericpaymentservice.miscpaymentresponse.MiscPaymentResponse;
import ae.etisalat.middleware.genericpaymentservice.paymentpostingrequest.PaymentPostingRequest;
import ae.etisalat.middleware.genericpaymentservice.paymentpostingresponse.PaymentPostingResponse;
import ae.etisalat.middleware.serviceordermgmt_cud.ServiceOrderMgmtCUD;
import ae.etisalat.middleware.serviceordermgmt_cud.ServiceOrderMgmtCUD_Service;
import ae.etisalat.middleware.serviceordermgmt_cud.subscribeservicesrequest.SubscribeServicesRequest;
import ae.etisalat.middleware.serviceordermgmt_cud.subscribeservicesresponse.SubscribeServicesResponse;
import ae.etisalat.middleware.sharedresources.common.applicationheader.ApplicationHeader;
import ae.etisalat.middleware.sharedresources.common.common.AdditionalInfo;
import lombok.AccessLevel;
import lombok.Getter;

@RefreshScope
@Service
public class PaymentService {

	private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);
	// private static final String CHANNEL = "WEB";
	private static final long PARENT_ORDER_NUMBER = -1;
	private static final long ORDER_ITEM_NUMBER = 1;
	private static final String OFFER_CODE = "offer_code";
	private static final String USER_ID_ATTR = "user_id";
	private static final String USER_ID_VAL = "WEBATM";
	private static final String IS_DEDUCTION_REQUIRED_ATTR = "isDeductionRequired";
	private static final String TRANSACTION_TYPE_ATTR = "transactiontype";
	private static final String TRANSACTION_TYPE_VAL = "Card";
	private static final String AMOUNT_ATTR = "amount";
	private static final String PAYMENT_REF_NUM_ATTR = "paymentrfrncnmb";
	private static final String RESELLER_CODE_ATTR = "resellercode";
	private static final String CASHIER_ID_ATTR = "cashier_id";
	private static final String CASHIER_ID_VAL = "WAPGTWAY";
	private static final String AUTHORIZATION_CODE_ATTR = "authorizationcode";
	private static final String CARD_NUMBER_ATTR = "cardnumber";
	private static final String INSTALLMENT_FLAG_ATTR = "installamentflag";
	private static final String PAYMENT_MODE_ATTR = "paymentmode";
	private static final String PAYMENT_MODE_VAL = "loyalty";
	private static final String CHANNEL_TYPE_ATTR = "channeltype";
	private static final String CHANNEL_TYPE_VAL = "LOYALTY";
	private static final String CARD_TYPE_ATTR = "cardtype";
	private static final String CARD_SUB_TYPE_ATTR = "cardsubtype";
	private static final String CARD_SUB_TYPE_VAL = "MASTER";
	private static final String CARD_EXPIRY_DATE_ATTR = "cardexpirydate";
	public static final String P_TYPE = "Smiles Redemption";
	public static final String P_STAGE = "COMS Provisioning";
	private static final String PREPAID_ACCOUNT = "PRE";
	private static final String POSTPAID_ACCOUNT = "POS";
	private static final String IS_DEDUCT_FROM_BALANCE = "isDeductFromBalance";
	private static final String IS_FULL_POINTS = "isFullPoints";
	private static final String REQUESTEDSYSTEM = "Loyalty";
	private static final String ADJUSTMNT_REASON = "ADJ-MISCEL";
	private static final String CHARGE_TYPE_IMM = "IMM";
	private static final String CHARGE_TYPE_OFF = "1OFF";
	private static final String CHARGE_CODE = "B27";
	private static final String TRANS_TYPE = "D";
	private static final String POSTPAID = "POSTPAID";
	public static final String paymentModeCode = "MOBILEAPP";
	public static final String IsCancelled = "N";
	public static final String cashierName = "WAPGTWAY";
	public static final String ptsBnkCd = "7101";
	public static final String ccBnkCd = "7102";
	public static final String loyaltyPymTyp = "Loyalty";
	public static final String cardPymTyp = "Card";

	public static final String currency = "AED";
	public static final String channelType = "E";
	public static final String colltnRgnCd = "DX";
	public static final String CREDIT = "Credit";
	public static final String PaymentAccepted = "Y";
	public static final String BulkPayment = "N";
	public static final String PaymentProcessed = "N";
	public static final String SMSNotification = "N";
	public static final String EmailNotification = "N";
	public static final String RegionCode = "DX";
	public static final String TopupCompleted = "N";
	public static final Double ConversionRate = 100.0;
	public static final String PaymentCategory = "MobileNonMobile";
	public static final String CHARGE_SUCCESS = "TIB-000";
	public static final String CCPaymentType = "Card";
	protected static final String USER_NAME = "loyalty";
	protected static final String PASSWORD = "loyalty";
	public static final String CONTENT_NAME = "ETISALAT Loyalty Test";
	static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	static final int DIRHAM_TO_FILL_AMOUNT = 100;
	@Value(OffersConfigurationConstants.MEMBER_MANAGEMENT_URI)
	private String memberManagementUri;

	@Value(OffersConfigurationConstants.UPDATE_REFFERAL_BONUS_PATH)
	private String updateReferralBonusPath;
	
	@Value(OffersConfigurationConstants.PAYMENT_REFERRALBONUS_ENABLE)
	private String referralBonusenable;

	@Autowired
	SmilesPaymentRepository paymentRepository;
	
	@Autowired
	BillingAdapterService billingAdapterService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	ThirdPartyCallLogRepository callLogRepository;

	@Autowired
	MemberManagementService memberManagementService;
	
	@Autowired
	EquivalentPointsController equivalentPointsController;
	

	@Getter(AccessLevel.NONE)
	@Autowired
	PurchaseRepository purchaseRepository;

	@Autowired
	private ServiceHelper serviceHelper;

	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	private EventHandler eventHandler;
	
	@Autowired
	private VoucherController voucherController;
	
	@Autowired
	CFMService cfmService;
	
	@Autowired
	ExceptionLogsService exceptionLogService;

	@Autowired
	private RetryTemplate retryTemplate;
	
	@Autowired
	private EquivalentPointsDomain equivalentPointsDomain;
	
	@Autowired
	private AsynchronousService asyncService;
	
	@Value("${memberActivity.uri}")
	private String memberActivityUri;

	@Value("${memberActivity.reserveOrCommit.path}")
	private String reserveOrCommit;
	
	@Value("${points.bank.uri}")
	private String pointbankUri;

	@Value("${pointbank.rollback.path}")
	private String rollbackApi;
	
	@Value("${memberActivity.payment.path}")
	private String referralAccrual;

	@Value("${marketplace.uri}")
	private String marketplaceUri;

	@Value("${marketplace.generateVoucher.path}")
	private String generatevoucher;

	@Value("${payment.tibco.uri}")
	private String tibcoUri;

	@Value("${payment.tibco.username}")
	private String tibcoUsername;

	@Value("${payment.tibco.password}")
	private String tibcoPassword;

	@Value("${rtf.uri}")
	private String rtfUri;

	@Value("${payment.rbt.uri}")
	private String rbtUri;

	@Value("${memberManagement.updateWelcomeGfitFlag.path}")
	private String updateWelcomeGiftFlag;
	
	private SOAPConnector soapConnector;

	public Long callReservePointsAPI() {
		return null;
	}

	public GetPaymentResponse setPaymentGatewayParams(String selectedPaymentItem, String accountNumber, String amount,
			String pointsValue, String selectedOption, String offerId, String channelId, String promoCode,
			String language, Integer denomination, long transactionId, String atgUserName, String offerTitle,
			String token, Integer count, String subOfferId) {

		GetPaymentResponse response = new GetPaymentResponse();

		Math.round(Utils.getIfNotNull(amount)
				+ (Utils.getIfNotNull(pointsValue) * Utils.pointsToAEDRate(Utils.getIntIfNotNull(pointsValue))));

		return response;

	}

	public PaymentGatewayResponseResult paymentRegistration(GetPaymentRequest request) throws Exception {
		GetPaymentResponse response = (GetPaymentResponse) soapConnector.callWebService("", request);
		PaymentGatewayResponseResult responseResult = new PaymentGatewayResponseResult();
		if (response != null) {
			responseResult.setResponseCode(response.getResponseCode());
			responseResult.setResponseDescription(response.getResponseDescription());
			responseResult.setResponseParameters(response.getResponseParameters());
			responseResult.setUniqueId(response.getUniqueId());
			responseResult.setPaymentPortal(response.getPaymentPortal());
			responseResult.setTransactionId(response.getTransactionId());
			responseResult.setAmount(response.getAmount());
			responseResult.setApprovalCode(response.getApprovalCode());
			responseResult.setBalance(response.getBalance());
			responseResult.setAccountNumber(response.getAccountNumber());
			responseResult.setAccountToken(response.getAccountToken());
			responseResult.setFees(response.getFees());
			responseResult.setOrderID(response.getOrderID());
			responseResult.setAccountExpiry(response.getAccountExpiry());
			responseResult.setPaymentURL(response.getPaymentURL());
			responseResult.setCardToken(response.getCardToken());
			responseResult.setCardNumber(response.getCardNumber());
			responseResult.setCardBrand(response.getCardBrand());
			responseResult.setRecurrenceID(response.getRecurrenceID());
		}
		return responseResult;
	}

	public String getParentOfferBySuboffer(String offerId) {

		return null;
	}

	public PaymentResponse paymentAndProvisioning(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			OfferCatalog offerCatalog, SubscriptionCatalog subscription, PaymentAdditionalRequest paymentAdditionalRequest) {
		 String channelId = paymentAdditionalRequest.getChannelId(); 
		 String activityCode = paymentAdditionalRequest.getActivityCode();
		String uuid = paymentAdditionalRequest.getUuid();
		SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		AccountInfoDto requestedAccountInfo = new AccountInfoDto();
		boolean isPaymentRequired = paymentAdditionalRequest.isPaymentRequired();
		String reservePointsTxn = null;
		String referenceNmbr = null;
		double calculatedValue = 0.00;
		double rate = 0.00;
		List<String> voucherCodes = new ArrayList<String>();
		MemberActivityResponse memberActivityResponse = null;
		PaymentResponse paymentResponse = new PaymentResponse();
		CommonApiStatus adjustmentTibcoResponse = new CommonApiStatus(null);
		CommonApiStatus miscTibcoResponse = new CommonApiStatus(null);
		CommonApiStatus paymentPostingResponse = new CommonApiStatus(null);
		CommonApiStatus rbtResponse = new CommonApiStatus(null);
		ChargeImmediatelyResponse billingAdapterResponse = new ChargeImmediatelyResponse();
		
		String offerTitleEn = (null != offerCatalog && null != offerCatalog.getOffer() && 
				null != offerCatalog.getOffer().getOfferTitle()) ? offerCatalog.getOffer().getOfferTitle().getOfferTitleEn() : null;
		String subCategoryName = (null != offerCatalog && null != offerCatalog.getSubCategory() && null != offerCatalog.getSubCategory().getCategoryName()) ?
				offerCatalog.getSubCategory().getCategoryName().getCategoryNameEn() : null;
		String partnerCode = (null != offerCatalog && null != offerCatalog.getPartnerCode()) ? offerCatalog.getPartnerCode() : null;
		CFMRequestDto cmfRequestDto = new CFMRequestDto(paymentReq.getAccountNumber(), paymentReq.getSelectedOption(), offerTitleEn,
				memberDetails.getAccountsInfo().get(0).getDob(), memberDetails.getAccountsInfo().get(0).getNationality(),
				memberDetails.getAccountsInfo().get(0).getLanguage(), memberDetails.getAccountsInfo().get(0).getEmail(),
				memberDetails.getAccountsInfo().get(0).getNumberType(), null, subCategoryName);

		LOG.info("Payment Required : --------------------" + isPaymentRequired);
		try {

			if (null == memberDetails) {
				paymentResponse.setErrorCode(30300);
				paymentResponse.setPaymentStatus("Failed");
				paymentResponse.setFailedreason("Invalid Account Number");
				return paymentResponse;
			}

			String accountId = null;
			for (AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
				if (accountInfo.getAccountNumber().equalsIgnoreCase(paymentReq.getAccountNumber())) {
					requestedAccountInfo = accountInfo;
					accountId = accountInfo.getAccountId();
				}
			}
			if (null == accountId) {
				paymentResponse.setErrorCode(30301);
				paymentResponse.setPaymentStatus("Failed");
				paymentResponse.setFailedreason("AccountId not present for this Account Number");
				return paymentResponse;
			}
			Double spentAmount = 0.0;
			Integer spentPoint = 0;
			if(paymentReq.getSpentAmount() != null) {
				spentAmount = paymentReq.getSpentAmount();
			}
			if(paymentReq.getSpentPoints() != null) {
				spentPoint = paymentReq.getSpentPoints();
			}
			if ((MarketplaceConstants.FULLCREDITCARD.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
					|| (MarketplaceConstants.PARTIALPOINTSCC.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
					|| (MarketplaceConstants.APPLEPAY.getConstant().equalsIgnoreCase(paymentReq.getSelectedOption()))
					|| (MarketplaceConstants.SAMSUNGPAY.getConstant().equalsIgnoreCase(paymentReq.getSelectedOption()))) {
				LOG.info("Inside Credit card save");
				PaymentValues paymentValues = new PaymentValues();
				paymentValues.setCost(spentAmount);
				paymentValues.setPointsValue(spentPoint);
				CreditCardTransaction ccTrans = new CreditCardTransaction();
				ccTrans.setOrderId(randomAlphaNumeric(16));
				referenceNmbr = ccTrans.getOrderId();
				ccTrans.setAccountNumber(paymentReq.getAccountNumber());
				ccTrans.setApprovalCode(paymentReq.getAuthorizationCode());
				ccTrans.setAtgUsername(paymentReq.getAtgUserName());
				ccTrans.setCcNumber(paymentReq.getCardNumber());
				ccTrans.setCcExpDate(paymentReq.getCardExpiryDate());
				ccTrans.setCcToken(paymentReq.getCardToken());
				if(paymentAdditionalRequest.getUuid() != null) {
					ccTrans.setCcTxnId(paymentAdditionalRequest.getUuid());
				} else{
					ccTrans.setCcTxnId(paymentReq.getExtTransactionId());
				}
				ccTrans.setChannelId(channelId);
				ccTrans.setEpgTransactionId(paymentReq.getEpgTransactionId());
				ccTrans.setLanguage(paymentReq.getUiLanguage());
				ccTrans.setOfferId(paymentReq.getOfferId());
				ccTrans.setPaymentItem(paymentReq.getSelectedPaymentItem());
				ccTrans.setTypeOfPayment(paymentReq.getSelectedOption());
				ccTrans.setPaymentValues(paymentValues);
				ccTrans.setCreatedDate(new Date());
				ccTrans.setDateLastUpdated(new Date());
				ccTrans.setCreatedUser(paymentAdditionalRequest.getHeader().getUserName());
				ccTrans.setUpdatedUser(paymentAdditionalRequest.getHeader().getUserName());
				CreditCardTransaction ccTransSave = paymentRepository.save(ccTrans);

				LOG.info("Credit Card Transaction Saved : " + ccTransSave.toString());
			} else if ((MarketplaceConstants.FULLPOINTS.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
					&& isPaymentRequired) {
				long externalTransactionNumber = (long) (Math.random() * ((546546546 - 346546546) + 1)) + 346546546;
				MemberActivityPaymentDto memberActivity = new MemberActivityPaymentDto();

				memberActivity.setMembershipCode("");
				memberActivity.setAccountNumber(paymentReq.getAccountNumber());
				memberActivity.setActivityCode(activityCode);
				if(paymentReq.getSelectedPaymentItem().equalsIgnoreCase("Subscription")) {
					memberActivity.setOfferId(paymentReq.getSubscriptionCatalogId());
				} else {
					memberActivity.setOfferId(paymentReq.getOfferId());
				}
				memberActivity.setSubscriptionId(paymentReq.getSubscriptionCatalogId());
				memberActivity.setEventDate(eventDateFormat.format(new Date()));
				if (null != offerCatalog) {
					memberActivity.setPartnerCode(offerCatalog.getPartnerCode());
				} else {
					memberActivity.setPartnerCode(OfferConstants.PURCHASE_PARTNER_CODE.get());
				}
				if (paymentReq.getExtTransactionId() == null) {
					memberActivity.setExternalReferenceNumber("" + externalTransactionNumber);
				} else {
					memberActivity.setExternalReferenceNumber(paymentReq.getExtTransactionId());
				}
				
				if(null != offerCatalog && offerCatalog.isProratedBundle())
				{
					
					Date d=new Date();
					SimpleDateFormat s= new SimpleDateFormat("dd-MM-yyyy");
					String str=s.format(d);
					
					String startDateArr[] = str.split("-");
					int monthno= calculatemonth(startDateArr);
					memberActivity.setPointsValue((spentPoint*(monthno-Integer.parseInt(startDateArr[0])))/30);
				}
				else
				{
				memberActivity.setPointsValue(spentPoint);
				}
				memberActivity.setRedemptionType("Reserve");
				memberActivity.setReservationTime(1500);
				memberActivity.setUnitTime("Seconds");
				memberActivity.setMemberResponse(memberDetails);
				if ((MarketplaceConstants.DEALVOUCHER.getConstant())
						.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
						|| (MarketplaceConstants.CASHVOUCHER.getConstant())
								.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
						|| (MarketplaceConstants.DISCOUNTVOUCHER.getConstant())
								.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
					memberActivity.setVoucherId(uuid);
				}
				LOG.info("---------------Calling Member Activity Reserve API---------------");
				String jsonReq = new Gson().toJson(memberActivity);
				LOG.info("Reserve Request Object " + jsonReq);
				com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus response = redemptionReq(
						memberActivity, paymentAdditionalRequest.getHeader());
				if (response.getApiStatus().getStatusCode() == 0) {
					memberActivityResponse = (MemberActivityResponse) serviceHelper
							.convertToObject(response.getResult(), MemberActivityResponse.class);
					reservePointsTxn = memberActivityResponse.getTransactionRefId();
					LOG.info("Reserve Transaction Id : " + reservePointsTxn);
				} else {
					paymentResponse.setErrorCode(30302);
					paymentResponse.setPaymentStatus("Failed");
					paymentResponse.setFailedreason(response.getApiStatus().getMessage());
					return paymentResponse;
				}
			}

			

			if ((MarketplaceConstants.DEALVOUCHER.getConstant()).equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
					|| (MarketplaceConstants.CASHVOUCHER.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
					|| (MarketplaceConstants.DISCOUNTVOUCHER.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
					|| (MarketplaceConstants.SUBSCRIPTION.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
					|| (MarketplaceConstants.GOLDCERTIFICATE.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
					|| (MarketplaceConstants.GOLDGIFT.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
					|| (MarketplaceConstants.GOLDPOINTS.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
				LOG.info("Inside Voucher block");
				if ((MarketplaceConstants.ADDTOBILL.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
						&& !(MarketplaceConstants.SUBSCRIPTION.getConstant())
								.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
					try {
						adjustmentTibcoResponse = adjustmentPostingTIBCO(paymentReq.getAccountNumber(),
								Long.parseLong(accountId), spentAmount, paymentReq.getExtTransactionId(), 0);
					} catch (PaymentFault e) {
						LOG.error("Tibco adjusment posting Failed " + e.getMessage());
					} catch (MarketplaceException e) {
						LOG.error("Tibco adjusment posting Failed " + e.getMessage());
					}
					if (adjustmentTibcoResponse != null && adjustmentTibcoResponse.getStatusCode() != null
							&& adjustmentTibcoResponse.getStatusCode() == 0) {
						LOG.info("Adjustment Passed");

						VoucherResponse voucherResponse = generateVoucher(offerCatalog, paymentReq, uuid, paymentAdditionalRequest.getHeader());
						voucherCodes = new ArrayList<String>();
						if (voucherResponse != null && voucherResponse.getApiStatus() != null
								&& voucherResponse.getApiStatus().getStatusCode() == 0
								&& voucherResponse.getVoucherResult() != null
								&& !voucherResponse.getVoucherResult().isEmpty()) {
							for (VoucherResult result : voucherResponse.getVoucherResult()) {
								voucherCodes.add(result.getVoucherCode());
							}
							paymentResponse.setVoucherCode(voucherCodes);
						} else {
							paymentResponse.setErrorCode(30306);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse
									.setFailedreason(voucherResponse.getApiStatus().getErrors().get(0).getMessage());
							return paymentResponse;
						}
						firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
								memberDetails.getMemberInfo().getMembershipCode(), paymentAdditionalRequest.getHeader(), memberDetails);
						referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
						paymentResponse.setPaymentStatus("Success");
						paymentResponse.setTransactionNo(adjustmentTibcoResponse.getExternalTransactionId());
						
						//Calling CFM
						doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
						
						return paymentResponse;
					} else {
						paymentResponse.setErrorCode(30304);
						paymentResponse.setPaymentStatus("Failed");
						paymentResponse
								.setFailedreason("Adjusment Posting Failed for " + paymentReq.getAccountNumber());
						return paymentResponse;
					}
				} else if ((MarketplaceConstants.DEDUCTFROMBALANCE.getConstant())
						.equalsIgnoreCase(paymentReq.getSelectedOption())
						&& !(MarketplaceConstants.SUBSCRIPTION.getConstant())
								.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
					try {
							billingAdapterResponse = billingAdapterService.billingAdapterCall(paymentReq.getAccountNumber(),
								((int) ((double) (spentAmount* DIRHAM_TO_FILL_AMOUNT))), paymentReq.getExtTransactionId());
						if (billingAdapterResponse != null) {
							if (billingAdapterResponse.getStatusCode() == 0) {
								// call Voucher Management

								VoucherResponse voucherResponse = generateVoucher(offerCatalog, paymentReq, uuid, paymentAdditionalRequest.getHeader());
								voucherCodes = new ArrayList<String>();
								if (voucherResponse != null && voucherResponse.getApiStatus() != null
										&& voucherResponse.getApiStatus().getStatusCode() == 0
										&& voucherResponse.getVoucherResult() != null
										&& !voucherResponse.getVoucherResult().isEmpty()) {
									for (VoucherResult result2 : voucherResponse.getVoucherResult()) {
										voucherCodes.add(result2.getVoucherCode());
									}
									paymentResponse.setVoucherCode(voucherCodes);
								} else {
									paymentResponse.setErrorCode(30306);
									paymentResponse.setPaymentStatus("Failed");
									paymentResponse
											.setFailedreason(voucherResponse.getApiStatus().getErrors().get(0).getMessage());
									return paymentResponse;
								}
								firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
										memberDetails.getMemberInfo().getMembershipCode(), paymentAdditionalRequest.getHeader(), memberDetails);
								referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
								paymentResponse.setPaymentStatus("Success");
								paymentResponse.setTransactionNo(
										billingAdapterResponse.getAdditionalResponseList().get(0).getValue());
								
								//Calling CFM
								doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
								
								return paymentResponse;
							} else if (billingAdapterResponse.getStatusCode() == 3) {
								LOG.info("Billing Adapter Failed because of Insufficient Balance");
								paymentResponse.setErrorCode(30305);
								paymentResponse.setPaymentStatus("Failed");
								paymentResponse.setFailedreason("Insufficient Balance for deductFromBalance for "
										+ paymentReq.getAccountNumber());
								return paymentResponse;
							} else{
								LOG.info("Billing Adapter Failed");
								paymentResponse.setErrorCode(30305);
								paymentResponse.setPaymentStatus("Failed");
								paymentResponse.setFailedreason("BillingAdapter Failed for "
										+ paymentReq.getAccountNumber());
								return paymentResponse;
							}
						}
					} catch (NumberFormatException e) {
						LOG.error("Billing Adapter Failed" + e.getMessage());
						LOG.info("Billing Adapter Failed");
						paymentResponse.setErrorCode(30305);
						paymentResponse.setPaymentStatus("Failed");
						paymentResponse.setFailedreason("BillingAdapter Failed for "
										+ paymentReq.getAccountNumber());
								return paymentResponse;
					} catch (MarketplaceException e) {
						LOG.error("Billing Adapter Failed" + e.getMessage());
						LOG.info("Billing Adapter Failed");
						paymentResponse.setErrorCode(30305);
						paymentResponse.setPaymentStatus("Failed");
						paymentResponse.setFailedreason("BillingAdapter Failed for "
							+ paymentReq.getAccountNumber());
						return paymentResponse;
					}
				} else {
					if (!(MarketplaceConstants.SUBSCRIPTION.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
							&& !(MarketplaceConstants.GOLDCERTIFICATE.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedPaymentItem()) 
							&& !(MarketplaceConstants.GOLDGIFT.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
							&& !(MarketplaceConstants.GOLDPOINTS.getConstant())
											.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
						// call Voucher Management
						VoucherResponse voucherResponse = generateVoucher(offerCatalog, paymentReq, uuid, paymentAdditionalRequest.getHeader());
						voucherCodes = new ArrayList<String>();
						if (voucherResponse != null && voucherResponse.getApiStatus() != null
								&& voucherResponse.getApiStatus().getStatusCode() == 0
								&& voucherResponse.getVoucherResult() != null
								&& !voucherResponse.getVoucherResult().isEmpty()) {
							for (VoucherResult result1 : voucherResponse.getVoucherResult()) {
								voucherCodes.add(result1.getVoucherCode());
							}
							paymentResponse.setVoucherCode(voucherCodes);
						} else {
							paymentResponse.setErrorCode(30306);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse
									.setFailedreason(voucherResponse.getApiStatus().getErrors().get(0).getMessage());
							if((MarketplaceConstants.FULLPOINTS.getConstant())
								.equalsIgnoreCase(paymentReq.getSelectedOption())) {
									manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
								} else {
									paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
								}
							return paymentResponse;
						}
					}
					boolean callMisc = false;
					if ((MarketplaceConstants.DEALVOUCHER.getConstant()).equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
							|| (MarketplaceConstants.CASHVOUCHER.getConstant())
								.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
							|| ((MarketplaceConstants.DISCOUNTVOUCHER.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem()) && ((MarketplaceConstants.FULLCREDITCARD.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedOption()) || (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
							||(MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())))) {
								callMisc = true;
							}
						String paidTotalAmount = spentAmount.toString();
						String paidTotalAmountPoint = "";
						if (!(MarketplaceConstants.FULLCREDITCARD.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
								&& !(MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
								&& !(MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
								&& isPaymentRequired && callMisc) {
							EquivalentPointsDto eqPointDto = new EquivalentPointsDto();
							eqPointDto.setAccountNumber(paymentReq.getAccountNumber());
							eqPointDto.setPoint(Double.valueOf(spentPoint));
							eqPointDto.setChannel(paymentAdditionalRequest.getHeader().getChannelId());
							eqPointDto.setOperationType("redeeming");
							if((MarketplaceConstants.DEALVOUCHER.getConstant()).equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
								eqPointDto.setOfferType("dealVoucher");
							}
							if((MarketplaceConstants.CASHVOUCHER.getConstant()).equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
								eqPointDto.setOfferType("voucher");
							}
							if((MarketplaceConstants.DISCOUNTVOUCHER.getConstant()).equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
								eqPointDto.setOfferType("discount");
							}
							eqPointDto.setPartnerCode("ES");
							LOG.info("---Calling EquivalentPoint point----");
							Headers header = paymentAdditionalRequest.getHeader();
							ResponseEntity<com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus> response = equivalentPointsController
									.equivalentPoints(eqPointDto, header.getUserName(), header.getSessionId(),
											header.getUserPrev(), header.getChannelId(), header.getSystemId(),
											header.getSystemPassword(), header.getToken(), header.getExternalTransactionId(),
											header.getProgram());
							com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus commonApiStatus = response
									.getBody();
							List<ListRedemptionRate> listRedemptionRate = null;

							if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
								listRedemptionRate = (List<ListRedemptionRate>) commonApiStatus.getResult();
							}
							RedemptionRate redemptionRate = new RedemptionRate();
							
							if (listRedemptionRate != null && !listRedemptionRate.isEmpty()) {
								redemptionRate = listRedemptionRate.get(0).getRedemptionCalculatedValue();
							}
							if (redemptionRate != null) {
								rate = redemptionRate.getRate();
								calculatedValue = redemptionRate.getEquivalentPoint();
							}
							LOG.info("Conversion Rate from equivalentpoint : " + rate);
							paidTotalAmountPoint = "" + calculatedValue;
							LOG.info("Calculated Value : " + calculatedValue);
							LOG.info("PaidTotalAmountPoint Value : " + paidTotalAmountPoint);

						}
						
						String paymentType = null;
						if((MarketplaceConstants.FULLPOINTS.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())) {
							paymentType = "Loyalty";
						} else {
							paymentType = "Card";
						}
						String cardNbr = paymentReq.getCardNumber();
						String cardSubType = paymentReq.getCardSubType();
						String cardToken = paymentReq.getCardToken();
						String authorizationCode = paymentReq.getAuthorizationCode();
						String membershipCode = paymentReq.getMembershipCode();
						String loyaltyPoints = spentPoint.toString();
						boolean miscPaymentSuccess = false;
						boolean pointsMisc = true;
						// String referenceNumber = paymentResponse.getVoucherCode().get(0);
						try {
							if (!(MarketplaceConstants.SUBSCRIPTION.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
									&& !(MarketplaceConstants.GOLDCERTIFICATE.getConstant())
											.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
									&& !(MarketplaceConstants.GOLDGIFT.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
									&& !(MarketplaceConstants.GOLDPOINTS.getConstant())
											.equalsIgnoreCase(paymentReq.getSelectedPaymentItem()) && callMisc && isPaymentRequired) {
									String referenceNumber = paymentResponse.getVoucherCode().get(0);
									LOG.info("Voucher Code reference :" + referenceNumber);
									if (((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())
									|| (MarketplaceConstants.PARTIALPOINTSCC.getConstant())
											.equalsIgnoreCase(paymentReq.getSelectedOption())) && pointsMisc && isPaymentRequired) {
								paymentType = "Loyalty";
								miscTibcoResponse = miscPaymentPostingTIBCO(paidTotalAmountPoint, paymentType,
										referenceNumber, null, null, null, null,
										membershipCode, loyaltyPoints, paymentReq.getExtTransactionId(), null, null, paymentAdditionalRequest.getHeader(), 0);
							if (miscTibcoResponse != null) {
								miscPaymentSuccess = true;
								LOG.info("TIBCO Misc Payment Posting Response Status for referenceNumber "
										+ referenceNumber + "is :" + miscTibcoResponse.getMessage());
							} else {
								miscPaymentSuccess = false;
								pointsMisc = true;
							}
							}
							if (((MarketplaceConstants.FULLCREDITCARD.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())
									|| (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
									|| (MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
									|| (MarketplaceConstants.PARTIALPOINTSCC.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()))
									&& pointsMisc && isPaymentRequired) {
								paymentType = "Card";
								miscTibcoResponse = miscPaymentPostingTIBCO(paidTotalAmount, paymentType,
										referenceNumber, cardNbr, cardSubType, cardToken, authorizationCode,
										membershipCode, null, paymentReq.getExtTransactionId(), null, paymentReq.getEpgTransactionId(), paymentAdditionalRequest.getHeader(), 0);
							if (miscTibcoResponse != null) {
								miscPaymentSuccess = true;
								LOG.info("TIBCO Misc Payment Posting Response Status for referenceNumber "
										+ referenceNumber + "is :" + miscTibcoResponse.getMessage());
							} else {
								miscPaymentSuccess = false;
							}
							}
							} else {
								miscPaymentSuccess = true;
							}
							// call Commit API
							if (miscPaymentSuccess) {
								if ((MarketplaceConstants.CASHVOUCHER.getConstant())
										.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
									asyncService.accrualForCashVoucherPurchase(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, 
											null != paymentReq.getSpentAmount() ? paymentReq.getSpentAmount() : 0.0,
											null != paymentReq.getSpentPoints() ? paymentReq.getSpentPoints() : 0);
								}
								firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
										memberDetails.getMemberInfo().getMembershipCode(), paymentAdditionalRequest.getHeader(), memberDetails);
								referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
								if(!(MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
								paymentResponse.setPaymentStatus("Success");
								paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
								paymentResponse.setTransactionNo(miscTibcoResponse.getExternalTransactionId());
								// SMS changes by Aspak for partialPointCC trx

								if ((MarketplaceConstants.PARTIALPOINTSCC.getConstant())
										.equalsIgnoreCase(paymentReq.getSelectedOption())) {
									MerchantName merchantName = new MerchantName();
									double offerCost = 0.0;
									if (offerCatalog != null && offerCatalog.getMerchant() != null
											&& offerCatalog.getMerchant().getMerchantName() != null) {
										merchantName = offerCatalog.getMerchant().getMerchantName();
									}
									if (offerCatalog != null && offerCatalog.getOfferValues() != null
											&& offerCatalog.getOfferValues().getCost() != null) {
										offerCost = offerCatalog.getOfferValues().getCost();
									}
									LOG.info("---Calling sendSms  ----");
									sendSms(paymentReq, memberDetails, memberActivityResponse, activityCode,
											voucherCodes, merchantName, calculatedValue, offerCost);
									LOG.info("---Calling sendSms end ----");
								}

								// Calling CFM
								doCFMAsyncCall(cmfRequestDto, paymentReq.getExtTransactionId(), paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);

								return paymentResponse;
							}
							} else {
								blacklistVouchers(voucherCodes, "Voucher purchase payment process failed", uuid, paymentAdditionalRequest.getHeader());
								if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									} else {
										paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
									}
								paymentResponse.setErrorCode(30307);
								paymentResponse.setPaymentStatus("Failed");
								if(!miscTibcoResponse.getErrors().isEmpty() && miscTibcoResponse.getErrors().get(0).getMessage() != null) {
									paymentResponse.setFailedreason(miscTibcoResponse.getErrors().get(0).getMessage());
								}else {
									paymentResponse.setFailedreason("TIBCO Misc Payment Failed");
								}
								return paymentResponse;
							}
						} catch (MarketplaceException e) {
							LOG.error("Misc Payment Posting Failed " + e.getMessage());
							blacklistVouchers(voucherCodes, "Voucher purchase payment process failed", uuid, paymentAdditionalRequest.getHeader());
							if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									} else {
										paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
									}
							paymentResponse.setErrorCode(30308);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setFailedreason("TIBCO Misc Payment Failed");
							return paymentResponse;
						}
					}
			} else if ((MarketplaceConstants.BILLPAYMENT.getConstant())
					.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
					|| (MarketplaceConstants.RECHARGES.getConstant())
							.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
              boolean usePreferredNumber = false;
				if(paymentReq.getPreferredNumber() != null) {
					boolean validPreferredNumber = false;
					boolean validAccountNumber = false;
					for(AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
						if(paymentReq.getPreferredNumber().equals(accountInfo.getAccountNumber())){
							validPreferredNumber = true;
							accountId = accountInfo.getAccountId();
							break;
						}
					}
					if(!validPreferredNumber) {
						ATGEnrollmentDTO enrollment = new ATGEnrollmentDTO();
						enrollment.setAccountNumber(paymentReq.getPreferredNumber());
						enrollment.setAtgUserName(paymentReq.getAtgUserName());
						enrollment.setChannel(paymentAdditionalRequest.getHeader().getChannelId());
						enrollment.setToken(paymentAdditionalRequest.getHeader().getToken());
						enrollment.setExternalTransactionId(paymentAdditionalRequest.getHeader().getExternalTransactionId());
						eventHandler.publishEnrollment(enrollment);
					}
					for(AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
						if(paymentReq.getAccountNumber().equals(accountInfo.getAccountNumber()) && accountInfo.isPrimary()){
							validAccountNumber = true;
							break;
						}
					}
					if(!validAccountNumber) {
						paymentResponse.setErrorCode(30317);
						paymentResponse.setPaymentStatus("Failed");
						paymentResponse.setFailedreason("Account Number is not Primary");
						return paymentResponse;
					}
					usePreferredNumber = true;
				}
				if ((!(MarketplaceConstants.FULLCREDITCARD.getConstant())
						.equalsIgnoreCase(paymentReq.getSelectedOption())
						||!(MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
						|| !(MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()))&& isPaymentRequired) {
					EquivalentPointsDto eqPointDto = new EquivalentPointsDto();
					eqPointDto.setAccountNumber(paymentReq.getAccountNumber());
					eqPointDto.setPoint(Double.valueOf(spentPoint));
					eqPointDto.setChannel(paymentAdditionalRequest.getHeader().getChannelId());
					eqPointDto.setOperationType("redeeming");
					eqPointDto.setOfferType("billRecharge");
					eqPointDto.setPartnerCode("ES");
					LOG.info("---Calling EquivalentPoint point----");
					Headers header = paymentAdditionalRequest.getHeader();
					ResponseEntity<com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus> response = equivalentPointsController
							.equivalentPoints(eqPointDto, header.getUserName(), header.getSessionId(),
									header.getUserPrev(), header.getChannelId(), header.getSystemId(),
									header.getSystemPassword(), header.getToken(), header.getExternalTransactionId(),
									header.getProgram());
					com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus commonApiStatus = response
							.getBody();
					List<ListRedemptionRate> listRedemptionRate = null;

					if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
						listRedemptionRate = (List<ListRedemptionRate>) commonApiStatus.getResult();
					}
					RedemptionRate redemptionRate = new RedemptionRate();
					
					if (listRedemptionRate != null && !listRedemptionRate.isEmpty()) {
						redemptionRate = listRedemptionRate.get(0).getRedemptionCalculatedValue();
					}
					if (redemptionRate != null) {
						rate = redemptionRate.getRate();
						calculatedValue = redemptionRate.getEquivalentPoint();
					}
					LOG.info("Conversion Rate from equivalentpoint : " + rate);
				}
				String paidTotalAmount = spentAmount.toString();

				String accountNmbr = null;
				if(usePreferredNumber) {
					accountNmbr = paymentReq.getPreferredNumber();
				} else {
					accountNmbr = paymentReq.getAccountNumber();
				}
				String transactionType = null;
				if (("billPayment").equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
					transactionType = "BILPAY";
				} else {
					transactionType = "RECHARGES";
				}
				String cardNmbr = paymentReq.getCardNumber();
				String cardSubType = paymentReq.getCardSubType();
				String cardToken = paymentReq.getCardToken();
				String authorizationCode = paymentReq.getAuthorizationCode();
				String cardExpiryDt = paymentReq.getCardExpiryDate();
				String paidAmount = spentAmount.toString();
				String membershipCode = paymentReq.getMembershipCode();
				String epgTransactionId = paymentReq.getEpgTransactionId();
				String paidTotalAmountPoint = "" + (calculatedValue);
				String paidAmountPoint = "" + (calculatedValue);
				String loyaltyPoints = spentPoint.toString();
				String referenceNumber = "140909457236551";
				if ((MarketplaceConstants.FULLCREDITCARD.getConstant())
						.equalsIgnoreCase(paymentReq.getSelectedOption())
						|| (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
						|| (MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())) {
					String paymentType = "Card";
					try {
						paymentPostingResponse = paymentPostingTibco(paidTotalAmount, paymentType,
								Long.parseLong(accountId), accountNmbr, transactionType, referenceNumber, cardNmbr,
								cardToken, cardExpiryDt, authorizationCode, paidAmount, epgTransactionId, cardSubType,
								membershipCode, null, paymentReq.getExtTransactionId(), rate, paymentAdditionalRequest.getHeader(), 0);
						if (paymentPostingResponse != null && paymentPostingResponse.getStatusCode().equals(0)) {
							firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
									memberDetails.getMemberInfo().getMembershipCode(), paymentAdditionalRequest.getHeader(), memberDetails);
							referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
							paymentResponse.setPaymentStatus("Success");
							paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
							paymentResponse.setTransactionNo(paymentPostingResponse.getExternalTransactionId());
							
							//Calling CFM
							doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
							
							return paymentResponse;
						} else {
							paymentResponse.setErrorCode(30309);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
							if(!paymentPostingResponse.getErrors().isEmpty() && paymentPostingResponse.getErrors().get(0).getMessage() != null) {
								paymentResponse
								.setFailedreason(paymentPostingResponse.getErrors().get(0).getMessage());
							}else {
								paymentResponse
								.setFailedreason("Credit Card Payment Posting Failed for Bill Payment or Recharge");
							}
							return paymentResponse;
						}
					} catch (MarketplaceException e) {
						LOG.error("TIBCO Payment Posting Failed : " + e.getMessage());
						paymentResponse.setErrorCode(30310);
						paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
						paymentResponse.setPaymentStatus("Failed");
						paymentResponse
								.setFailedreason("Credit Card Payment Posting Error for Bill Payment or Recharge for :" + e.getMessage());
						return paymentResponse;
					}
				} else if ((MarketplaceConstants.FULLPOINTS.getConstant())
						.equalsIgnoreCase(paymentReq.getSelectedOption())) {
					String paymentType = "Loyalty";
					try {
						paymentPostingResponse = paymentPostingTibco(paidTotalAmountPoint, paymentType,
								Long.parseLong(accountId), accountNmbr, transactionType, referenceNumber, null, null,
								null, null, paidAmountPoint, null, null, membershipCode, loyaltyPoints, paymentReq.getExtTransactionId(),rate, paymentAdditionalRequest.getHeader(), 0);
						if (paymentPostingResponse != null && paymentPostingResponse.getStatusCode().equals(0)) {
							// commit point call
							paymentResponse.setPaymentStatus("Success");
							paymentResponse.setTransactionNo(paymentPostingResponse.getExternalTransactionId());
							
						} else {
							if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									}
							paymentResponse.setErrorCode(30311);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse
									.setFailedreason("Point Payment Posting Failed for Bill Payment or Recharge");
							return paymentResponse;
						}
					} catch (MarketplaceException e) {
						LOG.error("TIBCO Payment Posting Failed : " + e.getMessage());
						if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									}
						paymentResponse.setErrorCode(30312);
						paymentResponse.setPaymentStatus("Failed");
						paymentResponse.setFailedreason("Point Payment Posting Error for Bill Payment or Recharge");
						return paymentResponse;
					}
				} else if ((MarketplaceConstants.PARTIALPOINTSCC.getConstant())
						.equalsIgnoreCase(paymentReq.getSelectedOption())) {
					String paymentType = "Card";
					String tibcoTxn ="";
					try {
						paymentPostingResponse = paymentPostingTibco(paidTotalAmount, paymentType,
								Long.parseLong(accountId), accountNmbr, transactionType, referenceNumber, cardNmbr,
								cardToken, cardExpiryDt, authorizationCode, paidAmount, epgTransactionId, cardSubType,
								membershipCode, null, paymentReq.getExtTransactionId(), rate, paymentAdditionalRequest.getHeader(), 0);
						if (paymentPostingResponse != null && paymentPostingResponse.getStatusCode().equals(0)) {
							tibcoTxn = paymentPostingResponse.getExternalTransactionId();
							paymentResponse.setPaymentStatus("Success");
							paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
							paymentResponse.setTransactionNo(tibcoTxn);
						} else {
							paymentResponse.setErrorCode(30313);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
							paymentResponse
									.setFailedreason("Credit Card Payment Posting Failed for Bill Payment or Recharge");
							return paymentResponse;
						}
					} catch (MarketplaceException e) {
						LOG.error("TIBCO Payment Posting Failed : " + e.getMessage());
						paymentResponse.setErrorCode(30313);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
							paymentResponse
									.setFailedreason("Credit Card Payment Posting Failed for Bill Payment or Recharge");
							return paymentResponse;
					}
					paymentType = "Loyalty";
					try {
						paymentPostingResponse = paymentPostingTibco(paidTotalAmountPoint, paymentType,
								Long.parseLong(accountId), accountNmbr, transactionType, referenceNumber, null, null,
								null, null, paidAmountPoint, null, null, membershipCode, loyaltyPoints, paymentReq.getExtTransactionId(), rate, paymentAdditionalRequest.getHeader(), 0);
						if (paymentPostingResponse != null && paymentPostingResponse.getStatusCode().equals(0)) {
							tibcoTxn = tibcoTxn + "," + paymentPostingResponse.getExternalTransactionId();
							firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
									memberDetails.getMemberInfo().getMembershipCode(), paymentAdditionalRequest.getHeader(), memberDetails);
							referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
							paymentResponse.setPaymentStatus("Success");
							paymentResponse.setTransactionNo(tibcoTxn);
							if ((MarketplaceConstants.PARTIALPOINTSCC.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
								MerchantName merchantName = new MerchantName();
								double offerCost = 0.0;
								if (offerCatalog != null && offerCatalog.getMerchant() != null
										&& offerCatalog.getMerchant().getMerchantName() != null) {
									merchantName = offerCatalog.getMerchant().getMerchantName();
								}
								if (offerCatalog != null && offerCatalog.getOfferValues() != null
										&& offerCatalog.getOfferValues().getCost() != null) {
									offerCost = offerCatalog.getOfferValues().getCost();
								}
								LOG.info("---Calling sendSms  ----");
								sendSms(paymentReq, memberDetails, memberActivityResponse, activityCode,
										voucherCodes, merchantName, calculatedValue, offerCost);
								LOG.info("---Calling sendSms end ----");
							}
							//Calling CFM
							doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
							
							return paymentResponse;
						} else {
							paymentResponse.setErrorCode(30314);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse
									.setFailedreason("Point Payment Posting Failed for Bill Payment or Recharge");
							return paymentResponse;
						}
					} catch (MarketplaceException e) {
						LOG.error("TIBCO Payment Posting Failed : " + e.getMessage());
						paymentResponse.setErrorCode(30314);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse
									.setFailedreason("Point Payment Posting Failed for Bill Payment or Recharge");
							return paymentResponse;
					}
				}
			} else if ((MarketplaceConstants.ETISALATADDON.getConstant())
					.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
				try {
					if (offerCatalog.getProvisioningChannel()
							.equalsIgnoreCase(MarketplaceConstants.RBT.getConstant())) {
						rbtResponse = rbtServices(offerCatalog.getProvisioningAttributes().getServiceId(),
								offerCatalog.getProvisioningAttributes().getPackName(), paymentReq.getAccountNumber(),
								paymentReq.getExtTransactionId());
						if (rbtResponse != null && rbtResponse.getStatusCode() != null
								&& rbtResponse.getStatusCode() == 0) {
							firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
									memberDetails.getMemberInfo().getMembershipCode(),
									paymentAdditionalRequest.getHeader(), memberDetails);
							referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
							if (!paymentReq.getSelectedOption()
									.equalsIgnoreCase(MarketplaceConstants.FULLPOINTS.getConstant())) {
								paymentResponse.setPaymentStatus("Success");
								paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
								if ((MarketplaceConstants.PARTIALPOINTSCC.getConstant())
										.equalsIgnoreCase(paymentReq.getSelectedOption())) {
									MerchantName merchantName = new MerchantName();
									double offerCost = 0.0;
									if (offerCatalog != null && offerCatalog.getMerchant() != null
											&& offerCatalog.getMerchant().getMerchantName() != null) {
										merchantName = offerCatalog.getMerchant().getMerchantName();
									}
									if (offerCatalog != null && offerCatalog.getOfferValues() != null
											&& offerCatalog.getOfferValues().getCost() != null) {
										offerCost = offerCatalog.getOfferValues().getCost();
									}
									LOG.info("---Calling sendSms  ----");
									sendSms(paymentReq, memberDetails, memberActivityResponse, activityCode,
											voucherCodes, merchantName, calculatedValue, offerCost);
									LOG.info("---Calling sendSms end ----");
								}
								//Calling CFM
								doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
								
								return paymentResponse;
							}
						} else if(rbtResponse == null){
							if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									}
									else if ((MarketplaceConstants.FULLCREDITCARD.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())){
											paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
									}
							paymentResponse.setErrorCode(30316);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setFailedreason("RBT Failed");
							return paymentResponse;
						} else {
							if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									} else if ((MarketplaceConstants.FULLCREDITCARD.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())){
										paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
									}
							paymentResponse.setErrorCode(30318);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setFailedreason("RBT Failed");
							return paymentResponse;
						}

					} else {
						String bypassPayment = null;
						String deductionRequired = null;
						String productCode = offerCatalog.getProvisioningAttributes().getRtfProductCode();
						String productType = offerCatalog.getProvisioningAttributes().getRtfProductType();
						if (paymentReq.getSelectedOption()
								.equalsIgnoreCase(MarketplaceConstants.FULLCREDITCARD.getConstant())
								|| (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
								|| (MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
								|| paymentReq.getSelectedOption().equalsIgnoreCase(MarketplaceConstants.FULLPOINTS.getConstant())) {
							bypassPayment = "TRUE";
							deductionRequired = "false";
						} else {
							bypassPayment = "FALSE";
							deductionRequired = "true";
						}
						String accountNumber = paymentReq.getAccountNumber();
						String rtfResponse = rtfFulfillment(productCode, accountNumber, productType, bypassPayment,
								paymentReq.getExtTransactionId(), deductionRequired);
						DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						InputSource src = new InputSource();
						src.setCharacterStream(new StringReader(rtfResponse));
						Document doc = builder.parse(src);
						String statusCode = doc.getElementsByTagName("requestCode").item(0).getTextContent();
						LOG.info("RTF Response StatusCode: " + statusCode);
						ThirdPartyCallLog callLog = new ThirdPartyCallLog();
						if (statusCode.equalsIgnoreCase("SC-00000")) {
							firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
									memberDetails.getMemberInfo().getMembershipCode(),
									paymentAdditionalRequest.getHeader(), memberDetails);
							referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
							if (!paymentReq.getSelectedOption()
									.equalsIgnoreCase(MarketplaceConstants.FULLPOINTS.getConstant())) {
								paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
								paymentResponse.setPaymentStatus("Success");
								if ((MarketplaceConstants.PARTIALPOINTSCC.getConstant())
										.equalsIgnoreCase(paymentReq.getSelectedOption())) {
									MerchantName merchantName = new MerchantName();
									double offerCost = 0.0;
									if (offerCatalog != null && offerCatalog.getMerchant() != null
											&& offerCatalog.getMerchant().getMerchantName() != null) {
										merchantName = offerCatalog.getMerchant().getMerchantName();
									}
									if (offerCatalog != null && offerCatalog.getOfferValues() != null
											&& offerCatalog.getOfferValues().getCost() != null) {
										offerCost = offerCatalog.getOfferValues().getCost();
									}
									LOG.info("---Calling sendSms  ----");
									sendSms(paymentReq, memberDetails, memberActivityResponse, activityCode,
											voucherCodes, merchantName, calculatedValue, offerCost);
									LOG.info("---Calling sendSms end ----");
								}
								//Calling CFM
								doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
								
								return paymentResponse;
							}
						} else {
							if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									} else if ((MarketplaceConstants.FULLCREDITCARD.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())){
										paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
									}
							paymentResponse.setErrorCode(30315);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setFailedreason("RTF Failed");
							return paymentResponse;
						}
					}
				} catch (ParserConfigurationException e) {
					if((MarketplaceConstants.FULLPOINTS.getConstant())
									.equalsIgnoreCase(paymentReq.getSelectedOption())) {
										manualRollback(reservePointsTxn,paymentAdditionalRequest.getHeader());
									} else if ((MarketplaceConstants.FULLCREDITCARD.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.APPLEPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
											|| (MarketplaceConstants.SAMSUNGPAY.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())){
										paymentResponse.setEpgTransactionId(paymentReq.getEpgTransactionId());
									}
					LOG.error("Etisalat Add-on Failed" + e.getMessage());
					paymentResponse.setErrorCode(30317);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setFailedreason("eService Failed");
							return paymentResponse;
				}
			}

			if ((MarketplaceConstants.FULLPOINTS.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
					&& isPaymentRequired) {
				MemberActivityPaymentDto memberActivityDto = new MemberActivityPaymentDto();
				memberActivityDto.setRedemptionType("Commit");
				memberActivityDto.setReserveTransactionId(reservePointsTxn);
				LOG.info("---------------Calling Member Activity Reserve API---------------");
				String jsonReq = new Gson().toJson(memberActivityDto);
				LOG.info("Commit Request Object " + jsonReq);
				com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus response = redemptionReq(
						memberActivityDto, paymentAdditionalRequest.getHeader());
				if (response.getApiStatus().getStatusCode() == 0) {
					memberActivityResponse = (MemberActivityResponse) serviceHelper
							.convertToObject(response.getResult(), MemberActivityResponse.class);
					reservePointsTxn = memberActivityResponse.getTransactionRefId();
					LOG.info("Redemption Id : " + reservePointsTxn);
					firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
							memberDetails.getMemberInfo().getMembershipCode(), paymentAdditionalRequest.getHeader(), memberDetails);
					referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
					paymentResponse.setPaymentStatus("Success");
					paymentResponse.setExtRefNo(reservePointsTxn);
					MerchantName merchantName=new MerchantName();
					double offerCost = 0.0;
					if(offerCatalog != null && offerCatalog.getMerchant() != null && offerCatalog.getMerchant().getMerchantName() != null) {
						merchantName = offerCatalog.getMerchant().getMerchantName();
					}
					if(offerCatalog != null && offerCatalog.getOfferValues() != null && offerCatalog.getOfferValues().getCost() != null) {
						offerCost = offerCatalog.getOfferValues().getCost();
					}
					sendSms(paymentReq, memberDetails, memberActivityResponse, activityCode, voucherCodes,
							merchantName, calculatedValue, offerCost);
					
					//Calling CFM
					doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
					
					return paymentResponse;
				} else {
					paymentResponse.setErrorCode(30319);
					paymentResponse.setPaymentStatus("Failed");
					paymentResponse.setFailedreason(response.getApiStatus().getMessage());
					return paymentResponse;
				}
			} else if ((MarketplaceConstants.FULLPOINTS.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption())
					&& !isPaymentRequired) {
				firstTimePurchaseWelcomeGift(paymentReq, requestedAccountInfo,
						memberDetails.getMemberInfo().getMembershipCode(), paymentAdditionalRequest.getHeader(), memberDetails);
				referralBonus(paymentReq,paymentAdditionalRequest.getHeader(), memberDetails);
				paymentResponse.setPaymentStatus("Success");
				paymentResponse.setExtRefNo(null);
				
				//Calling CFM
				doCFMAsyncCall( cmfRequestDto, paymentReq.getExtTransactionId(),  paymentReq.getSelectedPaymentItem(), paymentAdditionalRequest.getHeader(), partnerCode);
				
				return paymentResponse;
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Payment Failed. saving Exception to ExceptionLogs Collection");
			String id = exceptionLogService.saveExceptionsToExceptionLogs(e, paymentAdditionalRequest.getHeader().getExternalTransactionId(), paymentReq.getAccountNumber(), paymentAdditionalRequest.getHeader().getUserName());
			LOG.error("Exception saved in ExceptionLogs Collection with id {}", id);
					paymentResponse.setErrorCode(30320);
							paymentResponse.setPaymentStatus("Failed");
							paymentResponse.setFailedreason("Payment Failed. Please check ExceptionLogs Collection with id "+ id);
							return paymentResponse;
		}
		return paymentResponse;

	}

	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		LOG.info(builder.toString());
		return builder.toString();
	}

	private int calculatemonth(String[] startDateArr)
	{
		int lastday = 0;
		LOG.info("To set the end day of the month" + startDateArr[1]);
		int day = Integer.parseInt(startDateArr[1]);
		if (day == 1 || day == 3 || day == 5 || day == 7 || day == 8 || day == 10 || day == 12) {
			lastday = 31;
		} else if (day == 4 || day == 6 || day == 9 || day == 11) {
			lastday = 30;
		} else {
			boolean isLeapYear = isLeapYear(startDateArr[2]);
			if (isLeapYear) {
				lastday = 29;
			} else {
				lastday = 28;
			}
		}
	
	return lastday;
	}
	private boolean isLeapYear(String year) {
		if (year != null) {
			int checkLeapYear = Integer.parseInt(year);
			if (checkLeapYear % 4 == 0 && checkLeapYear % 100 == 0 && checkLeapYear % 400 == 0) {
				return true;
			}
		}
		return false;
	}
	
	private void sendSms(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			MemberActivityResponse memberActivityResponse, String activityCode, List<String> voucherCodes,
			MerchantName merchantName, double aedVal, double offerCost) {
		if ((MarketplaceConstants.BILLPAYMENT.getConstant()).equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
				|| (MarketplaceConstants.RECHARGES.getConstant())
						.equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {

			billpaymentAndRechargeSms(paymentReq, memberDetails, memberActivityResponse, activityCode, aedVal);
		}
		if (MarketplaceConstants.DISCOUNTVOUCHER.getConstant().equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
			cashVoucherSms(paymentReq, memberDetails, memberActivityResponse, merchantName, String.valueOf(offerCost));
		}
		if (MarketplaceConstants.DEALVOUCHER.getConstant().equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
			dealVoucherSms(paymentReq, memberDetails, memberActivityResponse, activityCode, merchantName);
		}
		if (MarketplaceConstants.ETISALATADDON.getConstant().equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
			etisalatAddonSms(paymentReq, memberDetails, memberActivityResponse);
		}
		//moved to asyc method with accrual for cashVoucher.
//		if (MarketplaceConstants.CASHVOUCHER.getConstant().equalsIgnoreCase(paymentReq.getSelectedPaymentItem())) {
//			cashVoucherSms(paymentReq, memberDetails, memberActivityResponse, merchantName, String.valueOf(paymentReq.getVoucherDenomination()));
//		}
	}

	private void billpaymentAndRechargeSms(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			MemberActivityResponse memberActivityResponse, String activityCode, double aedVal) {
		List<AccountInfoDto> requestedAccountInfo = new ArrayList<AccountInfoDto>();
		String preferredLanguage = "en";
		String actualMerchantName = null;
		String templateId = null;
		String notificationId = null;
		boolean primary = false;
		for (AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
			if (accountInfo.getAccountNumber().equalsIgnoreCase(paymentReq.getAccountNumber())) {
				requestedAccountInfo.add(accountInfo);
				if (accountInfo.isPrimary()) {
					primary = true;
				}
			}
		}
		if (!primary) {
			for (AccountInfoDto accountInfoPrimary : memberDetails.getAccountsInfo()) {
				if (accountInfoPrimary.isPrimary()) {
					requestedAccountInfo.add(accountInfoPrimary);
				}
			}
		}
		for (AccountInfoDto accountInfoSms : requestedAccountInfo) {
			if (paymentReq.getUiLanguage() != null) {
				preferredLanguage = paymentReq.getUiLanguage();
			} else if (accountInfoSms.getLanguage() != null) {
				preferredLanguage = accountInfoSms.getLanguage();
			}
			if (MarketplaceConstants.ARABIC.getConstant().equalsIgnoreCase(preferredLanguage)
					|| MarketplaceConstants.AR.getConstant().equalsIgnoreCase(preferredLanguage)) {
				templateId = "2074";
				notificationId = "13";
				preferredLanguage = "ar";
			} else {
				templateId = "2070";
				notificationId = "13";
				preferredLanguage = "en";
			}
			SMSRequestDto smsRequestDto = new SMSRequestDto();

			List<String> destinationNumber = new ArrayList<>();
			destinationNumber.add(accountInfoSms.getAccountNumber());
			double aedValue = paymentReq.getSpentAmount() * 0.01;

			Map<String, String> additionalParam = new HashMap<>();
			additionalParam.put(MarketplaceConstants.FIRSTNAME.getConstant(), accountInfoSms.getFirstName());
			additionalParam.put(MarketplaceConstants.REDEEMPOINTS.getConstant(),
					String.valueOf(paymentReq.getSpentPoints()));
			additionalParam.put(MarketplaceConstants.AEDVALUE.getConstant(),
					String.valueOf(aedVal));
			if (MarketplaceConstants.BILLPAYMENT.getConstant().equalsIgnoreCase(paymentReq.getSelectedPaymentItem()))
				additionalParam.put(MarketplaceConstants.ACTIVITYDESCRIPTION.getConstant(),
						MarketplaceConstants.BILLPAYMENTACTIVITY.getConstant());
			if (MarketplaceConstants.RECHARGES.getConstant().equalsIgnoreCase(paymentReq.getSelectedPaymentItem()))
				additionalParam.put(MarketplaceConstants.ACTIVITYDESCRIPTION.getConstant(),
						MarketplaceConstants.RECHARGEACTIVITY.getConstant());
			additionalParam.put(MarketplaceConstants.MSISDN.getConstant(), paymentReq.getAccountNumber());
			if (memberActivityResponse != null) {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getMembershipPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getAccountPoints()));
				}
			} else {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberDetails.getMemberInfo().getTotalPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(accountInfoSms.getTotalPoints()));
				}
			}

			smsRequestDto.setTemplateId(templateId);
			smsRequestDto.setNotificationId(notificationId);
			smsRequestDto.setNotificationCode("00");
			smsRequestDto.setLanguage(preferredLanguage);
			smsRequestDto.setMembershipCode(memberDetails.getMemberInfo().getMembershipCode());
			smsRequestDto.setTransactionId(paymentReq.getExtTransactionId());
			smsRequestDto.setAdditionalParameters(additionalParam);
			smsRequestDto.setDestinationNumber(destinationNumber);

			LOG.info("Sending Billpayment or Recharge Redeemption SMS to :" + accountInfoSms.getAccountNumber());

			try {
				jmsTemplate.convertAndSend(VoucherConstants.SMS_QUEUE, smsRequestDto);
			} catch (JmsException jme) {
				LOG.error("Billpayment or Recharge Redeemption SMS Failed");
				exceptionLogService.saveExceptionsToExceptionLogs(jme, paymentReq.getExtTransactionId(), accountInfoSms.getAccountNumber(), "");
				LOG.error("Exception deatils is saved in ExceptionLogs collection");
				LOG.error(jme.getMessage());
			}
		}
	}

	private void discountVoucherSms(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			String activityCode, List<String> voucherCodes, MerchantName merchantName) {
		List<AccountInfoDto> requestedAccountInfo = new ArrayList<AccountInfoDto>();
		String preferredLanguage = "en";
		String actualMerchantName = null;
		String templateId = null;
		String notificationId = null;
		boolean primary = false;
		for (AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
			if (accountInfo.getAccountNumber().equalsIgnoreCase(paymentReq.getAccountNumber())) {
				requestedAccountInfo.add(accountInfo);
				if (accountInfo.isPrimary()) {
					primary = true;
				}
			}
		}
		if (!primary) {
			for (AccountInfoDto accountInfoPrimary : memberDetails.getAccountsInfo()) {
				if (accountInfoPrimary.isPrimary()) {
					requestedAccountInfo.add(accountInfoPrimary);
				}
			}
		}
		for (AccountInfoDto accountInfoSms : requestedAccountInfo) {
			if (paymentReq.getUiLanguage() != null) {
				preferredLanguage = paymentReq.getUiLanguage();
			} else if (accountInfoSms.getLanguage() != null) {
				preferredLanguage = accountInfoSms.getLanguage();
			}
			if (MarketplaceConstants.ARABIC.getConstant().equalsIgnoreCase(preferredLanguage)
					|| MarketplaceConstants.AR.getConstant().equalsIgnoreCase(preferredLanguage)) {
				actualMerchantName = merchantName.getMerchantNameAr();
				templateId = "2076";
				notificationId = "11";
				preferredLanguage = "ar";
			} else {
				actualMerchantName = merchantName.getMerchantNameEn();
				templateId = "2062";
				notificationId = "11";
				preferredLanguage = "en";
			}
			for (int i = 0; i < voucherCodes.size(); i++) {
				SMSRequestDto smsRequestDto = new SMSRequestDto();
				List<String> destinationNumber = new ArrayList<>();
				destinationNumber.add(accountInfoSms.getAccountNumber());
				Map<String, String> additionalParam = new HashMap<>();
				additionalParam.put(MarketplaceConstants.FIRSTNAME.getConstant(), accountInfoSms.getFirstName());
				additionalParam.put(MarketplaceConstants.REDEEMPOINTS.getConstant(),
						String.valueOf(paymentReq.getSpentPoints()));
				additionalParam.put(MarketplaceConstants.VOUCHERNAME.getConstant(), actualMerchantName);
				additionalParam.put(MarketplaceConstants.VOUCHERCODE.getConstant(), voucherCodes.get(i));
				smsRequestDto.setTemplateId(templateId);
				smsRequestDto.setNotificationId(notificationId);
				smsRequestDto.setNotificationCode("00");
				smsRequestDto.setLanguage(preferredLanguage);
				smsRequestDto.setMembershipCode(memberDetails.getMemberInfo().getMembershipCode());
				smsRequestDto.setTransactionId(paymentReq.getExtTransactionId());
				smsRequestDto.setAdditionalParameters(additionalParam);
				smsRequestDto.setDestinationNumber(destinationNumber);
				LOG.info("Sending Discount Voucher Redeemption SMS to :" + accountInfoSms.getAccountNumber());
				try {
					jmsTemplate.convertAndSend(VoucherConstants.SMS_QUEUE, smsRequestDto);
				} catch (JmsException jme) {
					LOG.error("Discount Voucher Redeemption SMS Failed");
					exceptionLogService.saveExceptionsToExceptionLogs(jme, paymentReq.getExtTransactionId(), accountInfoSms.getAccountNumber(), "");
					LOG.error("Exception deatils is saved in ExceptionLogs collection");
					LOG.error(jme.getMessage());
				}
			}
		}
	}

	private void discountVoucherSmsCredit(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			List<String> voucherCodes, MerchantName merchantName) {
		List<AccountInfoDto> requestedAccountInfo = new ArrayList<AccountInfoDto>();
		String preferredLanguage = "en";
		String actualMerchantName = null;
		String templateId = null;
		String notificationId = null;
		boolean primary = false;
		for (AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
			if (accountInfo.getAccountNumber().equalsIgnoreCase(paymentReq.getAccountNumber())) {
				requestedAccountInfo.add(accountInfo);
				if (accountInfo.isPrimary()) {
					primary = true;
				}
			}
		}
		if (!primary) {
			for (AccountInfoDto accountInfoPrimary : memberDetails.getAccountsInfo()) {
				if (accountInfoPrimary.isPrimary()) {
					requestedAccountInfo.add(accountInfoPrimary);
				}
			}
		}
		for (AccountInfoDto accountInfoSms : requestedAccountInfo) {
			if (paymentReq.getUiLanguage() != null) {
				preferredLanguage = paymentReq.getUiLanguage();
			} else if (accountInfoSms.getLanguage() != null) {
				preferredLanguage = accountInfoSms.getLanguage();
			}
			if (MarketplaceConstants.ARABIC.getConstant().equalsIgnoreCase(preferredLanguage)
					|| MarketplaceConstants.AR.getConstant().equalsIgnoreCase(preferredLanguage)) {
				actualMerchantName = merchantName.getMerchantNameAr();
				templateId = "2077";
				notificationId = "10";
				preferredLanguage = "ar";
			} else {
				actualMerchantName = merchantName.getMerchantNameEn();
				templateId = "2063";
				notificationId = "10";
				preferredLanguage = "en";
			}
			for (int i = 0; i < voucherCodes.size(); i++) {
				SMSRequestDto smsRequestDto = new SMSRequestDto();
				List<String> destinationNumber = new ArrayList<>();
				destinationNumber.add(accountInfoSms.getAccountNumber());
				Map<String, String> additionalParam = new HashMap<>();
				additionalParam.put(MarketplaceConstants.VOUCHERNAME.getConstant(), actualMerchantName);
				additionalParam.put(MarketplaceConstants.VOUCHERCODE.getConstant(), voucherCodes.get(i));
				smsRequestDto.setTemplateId(templateId);
				smsRequestDto.setNotificationId(notificationId);
				smsRequestDto.setNotificationCode("00");
				smsRequestDto.setLanguage(preferredLanguage);
				smsRequestDto.setMembershipCode(memberDetails.getMemberInfo().getMembershipCode());
				smsRequestDto.setTransactionId(paymentReq.getExtTransactionId());
				smsRequestDto.setAdditionalParameters(additionalParam);
				smsRequestDto.setDestinationNumber(destinationNumber);
				LOG.info("Sending Discount Voucher Credit SMS to :" + accountInfoSms.getAccountNumber());
				try {
					jmsTemplate.convertAndSend(VoucherConstants.SMS_QUEUE, smsRequestDto);
				} catch (JmsException jme) {
					LOG.error("Discount Voucher Credit SMS Failed");
					exceptionLogService.saveExceptionsToExceptionLogs(jme, paymentReq.getExtTransactionId(), accountInfoSms.getAccountNumber(), "");
					LOG.error("Exception deatils is saved in ExceptionLogs collection");
					LOG.error(jme.getMessage());
				}
			}
		}
	}

	private void dealVoucherSms(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			MemberActivityResponse memberActivityResponse, String activityCode, MerchantName merchantName) {
		List<AccountInfoDto> requestedAccountInfo = new ArrayList<AccountInfoDto>();
		String preferredLanguage = "en";
		String actualMerchantName = null;
		String templateId = null;
		String notificationId = null;
		boolean primary = false;
		for (AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
			if (accountInfo.getAccountNumber().equalsIgnoreCase(paymentReq.getAccountNumber())) {
				requestedAccountInfo.add(accountInfo);
				if (accountInfo.isPrimary()) {
					primary = true;
				}
			}
		}
		if (!primary) {
			for (AccountInfoDto accountInfoPrimary : memberDetails.getAccountsInfo()) {
				if (accountInfoPrimary.isPrimary()) {
					requestedAccountInfo.add(accountInfoPrimary);
				}
			}
		}
		for (AccountInfoDto accountInfoSms : requestedAccountInfo) {
			if (paymentReq.getUiLanguage() != null) {
				preferredLanguage = paymentReq.getUiLanguage();
			} else if (accountInfoSms.getLanguage() != null) {
				preferredLanguage = accountInfoSms.getLanguage();
			}
			if (MarketplaceConstants.ARABIC.getConstant().equalsIgnoreCase(preferredLanguage)
					|| MarketplaceConstants.AR.getConstant().equalsIgnoreCase(preferredLanguage)) {
				actualMerchantName = merchantName.getMerchantNameAr();
				templateId = "2099";
				notificationId = "106";
				preferredLanguage = "ar";
			} else {
				actualMerchantName = merchantName.getMerchantNameEn();
				templateId = "2098";
				notificationId = "106";
				preferredLanguage = "en";
			}
			SMSRequestDto smsRequestDto = new SMSRequestDto();
			List<String> destinationNumber = new ArrayList<>();
			destinationNumber.add(accountInfoSms.getAccountNumber());
			Map<String, String> additionalParam = new HashMap<>();
			LOG.info("FirstName :" + accountInfoSms.getFirstName());
			additionalParam.put(MarketplaceConstants.FIRSTNAME.getConstant(), accountInfoSms.getFirstName());
			additionalParam.put(MarketplaceConstants.REDEEMPOINTS.getConstant(),
					String.valueOf(paymentReq.getSpentPoints()));
			additionalParam.put(MarketplaceConstants.QUANTITY.getConstant(),
					String.valueOf(paymentReq.getCouponQuantity()));
			additionalParam.put(MarketplaceConstants.PARTNER.getConstant(), actualMerchantName);
			if (memberActivityResponse != null) {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getMembershipPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getAccountPoints()));
				}
			} else {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberDetails.getMemberInfo().getTotalPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(accountInfoSms.getTotalPoints()));
				}
			}
			smsRequestDto.setTemplateId(templateId);
			smsRequestDto.setNotificationId(notificationId);
			smsRequestDto.setNotificationCode("00");
			smsRequestDto.setLanguage(preferredLanguage);
			smsRequestDto.setMembershipCode(memberDetails.getMemberInfo().getMembershipCode());
			smsRequestDto.setTransactionId(paymentReq.getExtTransactionId());
			smsRequestDto.setAdditionalParameters(additionalParam);
			smsRequestDto.setDestinationNumber(destinationNumber);
			LOG.info("Sending Deal Voucher Redeemption SMS to :" + accountInfoSms.getAccountNumber());
			try {
				jmsTemplate.convertAndSend(VoucherConstants.SMS_QUEUE, smsRequestDto);
			} catch (JmsException jme) {
				LOG.error("Deal Voucher Redeemption SMS Failed");
				exceptionLogService.saveExceptionsToExceptionLogs(jme, paymentReq.getExtTransactionId(), accountInfoSms.getAccountNumber(), "");
				LOG.error("Exception deatils is saved in ExceptionLogs collection");
				LOG.error(jme.getMessage());
			}
		}
	}

	private void etisalatAddonSms(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			MemberActivityResponse memberActivityResponse) {
		List<AccountInfoDto> requestedAccountInfo = new ArrayList<AccountInfoDto>();
		String preferredLanguage = "en";
		String actualMerchantName = null;
		String templateId = null;
		String notificationId = null;
		boolean primary = false;
		for (AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
			if (accountInfo.getAccountNumber().equalsIgnoreCase(paymentReq.getAccountNumber())) {
				requestedAccountInfo.add(accountInfo);
				if (accountInfo.isPrimary()) {
					primary = true;
				}
			}
		}
		if (!primary) {
			for (AccountInfoDto accountInfoPrimary : memberDetails.getAccountsInfo()) {
				if (accountInfoPrimary.isPrimary()) {
					requestedAccountInfo.add(accountInfoPrimary);
				}
			}
		}
		for (AccountInfoDto accountInfoSms : requestedAccountInfo) {
			if (paymentReq.getUiLanguage() != null) {
				preferredLanguage = paymentReq.getUiLanguage();
			} else if (accountInfoSms.getLanguage() != null) {
				preferredLanguage = accountInfoSms.getLanguage();
			}
			if (MarketplaceConstants.ARABIC.getConstant().equalsIgnoreCase(preferredLanguage)
					|| MarketplaceConstants.AR.getConstant().equalsIgnoreCase(preferredLanguage)) {
				templateId = "2075";
				notificationId = "12";
				preferredLanguage = "ar";
			} else {
				templateId = "2069";
				notificationId = "12";
				preferredLanguage = "en";
			}
			SMSRequestDto smsRequestDto = new SMSRequestDto();
			List<String> destinationNumber = new ArrayList<>();
			destinationNumber.add(accountInfoSms.getAccountNumber());
			Map<String, String> additionalParam = new HashMap<>();
			additionalParam.put(MarketplaceConstants.FIRSTNAME.getConstant(), accountInfoSms.getFirstName());
			additionalParam.put(MarketplaceConstants.REDEEMPOINTS.getConstant(),
					String.valueOf(paymentReq.getSpentPoints()));
			additionalParam.put(MarketplaceConstants.MSISDN.getConstant(),
					String.valueOf(paymentReq.getAccountNumber()));
			if (memberActivityResponse != null) {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getMembershipPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getAccountPoints()));
				}
			} else {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberDetails.getMemberInfo().getTotalPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(accountInfoSms.getTotalPoints()));
				}
			}
			smsRequestDto.setTemplateId(templateId);
			smsRequestDto.setNotificationId(notificationId);
			smsRequestDto.setNotificationCode("00");
			smsRequestDto.setLanguage(preferredLanguage);
			smsRequestDto.setMembershipCode(memberDetails.getMemberInfo().getMembershipCode());
			smsRequestDto.setTransactionId(paymentReq.getExtTransactionId());
			smsRequestDto.setAdditionalParameters(additionalParam);
			smsRequestDto.setDestinationNumber(destinationNumber);
			LOG.info("Sending Etisalat Addon Redeemption SMS to :" + accountInfoSms.getAccountNumber());
			try {
				jmsTemplate.convertAndSend(VoucherConstants.SMS_QUEUE, smsRequestDto);
			} catch (JmsException jme) {
				LOG.error("Etisalat Addon Redeemption SMS Failed");
				exceptionLogService.saveExceptionsToExceptionLogs(jme, paymentReq.getExtTransactionId(), accountInfoSms.getAccountNumber(), "");
				LOG.error("Exception deatils is saved in ExceptionLogs collection");
				LOG.error(jme.getMessage());
			}
		}
	}

	public void cashVoucherSms(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			MemberActivityResponse memberActivityResponse, MerchantName merchantName, String denomination) {
		List<AccountInfoDto> requestedAccountInfo = new ArrayList<AccountInfoDto>();
		String preferredLanguage = "en";
		String actualMerchantName = null;
		String templateId = null;
		String notificationId = null;
		boolean primary = false;
		for (AccountInfoDto accountInfo : memberDetails.getAccountsInfo()) {
			if (accountInfo.getAccountNumber().equalsIgnoreCase(paymentReq.getAccountNumber())) {
				requestedAccountInfo.add(accountInfo);
				if (accountInfo.isPrimary()) {
					primary = true;
				}
			}
		}
		if (!primary) {
			for (AccountInfoDto accountInfoPrimary : memberDetails.getAccountsInfo()) {
				if (accountInfoPrimary.isPrimary()) {
					requestedAccountInfo.add(accountInfoPrimary);
				}
			}
		}
		LOG.info("Language Selected :" + paymentReq.getUiLanguage());
		for (AccountInfoDto accountInfoSms : requestedAccountInfo) {
			if (paymentReq.getUiLanguage() != null) {
				preferredLanguage = paymentReq.getUiLanguage();
			} else if (accountInfoSms.getLanguage() != null) {
				preferredLanguage = accountInfoSms.getLanguage();
			}
			if (MarketplaceConstants.ARABIC.getConstant().equalsIgnoreCase(preferredLanguage)
					|| MarketplaceConstants.AR.getConstant().equalsIgnoreCase(preferredLanguage)) {
				actualMerchantName = merchantName.getMerchantNameAr();
				templateId = "2019";
				notificationId = "59";
				preferredLanguage = "ar";
			} else {
				actualMerchantName = merchantName.getMerchantNameEn();
				templateId = "1992";
				notificationId = "85";
				preferredLanguage = "en";
			}
			SMSRequestDto smsRequestDto = new SMSRequestDto();

			List<String> destinationNumber = new ArrayList<>();
			destinationNumber.add(accountInfoSms.getAccountNumber());
			Map<String, String> additionalParam = new HashMap<>();
			additionalParam.put(MarketplaceConstants.FIRSTNAME.getConstant(), accountInfoSms.getFirstName());
			additionalParam.put(MarketplaceConstants.REDEEMPOINTS.getConstant(),
					String.valueOf(paymentReq.getSpentPoints()));
			additionalParam.put(MarketplaceConstants.AEDVALCASHVOUCHER.getConstant(),
					denomination);
			additionalParam.put(MarketplaceConstants.PARTNER.getConstant(), actualMerchantName);
			if (memberActivityResponse != null) {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getMembershipPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberActivityResponse.getAccountPoints()));
				}
			} else {
				if (accountInfoSms.isPrimary()) {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(memberDetails.getMemberInfo().getTotalPoints()));
				} else {
					additionalParam.put(MarketplaceConstants.CURRENTBALANCE.getConstant(),
							String.valueOf(accountInfoSms.getTotalPoints()));
				}
			}

			smsRequestDto.setTemplateId(templateId);
			smsRequestDto.setNotificationId(notificationId);
			smsRequestDto.setNotificationCode("00");
			smsRequestDto.setLanguage(preferredLanguage);
			smsRequestDto.setMembershipCode(memberDetails.getMemberInfo().getMembershipCode());
			smsRequestDto.setTransactionId(paymentReq.getExtTransactionId());
			smsRequestDto.setAdditionalParameters(additionalParam);
			smsRequestDto.setDestinationNumber(destinationNumber);

			LOG.info("Sending Cash Voucher Redeemption SMS to :" + accountInfoSms.getAccountNumber());

			try {
				jmsTemplate.convertAndSend(VoucherConstants.SMS_QUEUE, smsRequestDto);
			} catch (JmsException jme) {
				LOG.error("Cash Voucher Redeemption SMS Failed");
				exceptionLogService.saveExceptionsToExceptionLogs(jme, paymentReq.getExtTransactionId(), accountInfoSms.getAccountNumber(), "");
				LOG.error("Exception deatils is saved in ExceptionLogs collection");
				LOG.error(jme.getMessage());
			}
		}
	}
	private void referralBonus(PurchaseRequestDto paymentReq, Headers header, GetMemberResponseDto memberDetails) {
		
		if(null !=referralBonusenable && referralBonusenable.equalsIgnoreCase("Y"))
		{
		SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if (null != memberDetails.getAccountsInfo().get(0).getReferralBonusAccount()) {
		if ((paymentReq.getSpentAmount() != null && paymentReq.getSpentAmount() > 0) || (paymentReq.getSpentPoints()!= null && paymentReq.getSpentPoints() > 0)) {
			boolean pointsReferralElig = true;

			if (pointsReferralElig) {
				List<PurchaseHistory> transactionList = purchaseRepository.findByAccountNumberAndMembershipCode(
						paymentReq.getAccountNumber(), paymentReq.getMembershipCode());

				if (transactionList.isEmpty()) {
					pointsReferralElig = true;
				} else {
					for (PurchaseHistory purchase : transactionList) {
						if (purchase.getStatus() != null && "Success".equalsIgnoreCase(purchase.getStatus())
								&& ((purchase.getSpentAmount() != null && purchase.getSpentAmount() > 0) || (purchase.getSpentPoints() != null && (purchase.getSpentPoints() > 0 || purchase.getSpentPoints() < 0)))) {
							pointsReferralElig = false;
							break;
						}
					}
				}
			}
			if (pointsReferralElig)
			{
				LOG.info("--------First time transaction-------");
				if (null != memberDetails.getAccountsInfo().get(0).getReferralBonusAccount()) {
					LOG.info("--------Inside Referal Bonus-------");
					long externalTransactionNumber = (long) (Math.random() * ((546546546 - 346546546) + 1)) + 346546546;
					MemberActivityPaymentDto memberActivity = new MemberActivityPaymentDto();

					memberActivity.setMembershipCode("");
					memberActivity.setAccountNumber(
							memberDetails.getAccountsInfo().get(0).getReferralBonusAccount().getAccountNumber());
					memberActivity.setActivityCode("REFBON");

					memberActivity.setEventDate(eventDateFormat.format(new Date()));
					memberActivity.setPartnerCode(OfferConstants.PURCHASE_PARTNER_CODE.get());
					if (paymentReq.getExtTransactionId() == null) {
						memberActivity.setExternalReferenceNumber("" + externalTransactionNumber);
					} else {
						memberActivity.setExternalReferenceNumber(paymentReq.getExtTransactionId());
					}
					memberActivity.setSpendValue(1);
					memberActivity.setMemberResponse(memberDetails);
					LOG.info("---------------Calling Member Activity Acrual API for Referal Bonus---------------");
					String jsonReq = new Gson().toJson(memberActivity);
					LOG.info("Referal Bonus Request Object " + jsonReq);
					com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus response = referralAccrualReq(
							memberActivity, header);
					if (response.getApiStatus().getStatusCode() == 0) {
						LOG.info("to call updateReferralBonusPath");
						String url = memberManagementUri + updateReferralBonusPath;
						LOG.info(url);
						UpdateReferralBonusDto requestDto = new UpdateReferralBonusDto();
						requestDto.setReferredAccountNumber(paymentReq.getAccountNumber());
						try {
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
							if (!ObjectUtils.isEmpty(headers)) {
								headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID,
										header.getExternalTransactionId());
								headers.set(RequestMappingConstants.TOKEN, header.getToken());
							}
							Map<String, String> param = new HashMap<String, String>();
							param.put("accountNumber", memberDetails.getAccountsInfo().get(0).getReferralBonusAccount()
									.getAccountNumber());
							URI uri = UriComponentsBuilder.fromUriString(url).buildAndExpand(param).toUri();
							LOG.info("updated uri" + uri);

							HttpEntity<UpdateReferralBonusDto> requestEntity = new HttpEntity<>(requestDto, headers);

							ResponseEntity<com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus> result = restTemplate
									.exchange(uri, HttpMethod.POST, requestEntity,
											com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus.class);
							com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus commonApiStatus = result
									.getBody();

							if (null != commonApiStatus) {
								LOG.info("commonApiStatus" + commonApiStatus);
								ApiStatus apiStatus = commonApiStatus.getApiStatus();

								if (apiStatus.getStatusCode() == 0) {

									Object resultob = commonApiStatus.getResult();
									LOG.info("result" + resultob);

								} else {

									for (ApiError apiError : apiStatus.getErrors()) {
										LOG.info("apiError" + apiError.getCode() + apiError.getMessage());
									}

								}

							} else {

								LOG.info("commonApiStatus is null");
							}

						} catch (Exception e) {
							LOG.info("Referral Bonus Acrual Failed :" + response.getApiStatus().getMessage());
							exceptionLogService.saveExceptionsToExceptionLogs(e, paymentReq.getExtTransactionId(), paymentReq.getAccountNumber(), "");
							LOG.error("Exception deatils is saved in ExceptionLogs collection");
						}

					} else {
						LOG.info("Referral Bonus Acrual Failed :" + response.getApiStatus().getMessage());
					}
				}
			}
		}
		}
		}
		else
		{
			LOG.info("Referral Bonus is disable");
		}
	}
	private void firstTimePurchaseWelcomeGift(PurchaseRequestDto paymentReq, AccountInfoDto requestedAccountInfo,
			String membershipCode, Headers headers, GetMemberResponseDto memberResponse) {
		boolean pointsWelcomeGiftElig = false;
		SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		double giftValue = 500;
		if (requestedAccountInfo.getWelcomeGifts() != null && !requestedAccountInfo.getWelcomeGifts().isEmpty()) {
			for (WelcomeGift gift : requestedAccountInfo.getWelcomeGifts()) {
				if (MarketplaceConstants.POINTS.getConstant().equalsIgnoreCase(gift.getGiftType())) {
					pointsWelcomeGiftElig = true;
					giftValue = Double.valueOf(gift.getGiftValue());
				}
			}
		}
		if (requestedAccountInfo.isWelcomegiftEligibleFlag() && pointsWelcomeGiftElig) {
			List<PurchaseHistory> transactionList = purchaseRepository
					.findByAccountNumberAndMembershipCode(paymentReq.getAccountNumber(), membershipCode);
			boolean firstTimePurchase = true;
			MemberActivityResponse memberActivityResponse = new MemberActivityResponse();
			if (transactionList.isEmpty()) {
				firstTimePurchase = true;
			} else {
				for (PurchaseHistory purchase : transactionList) {
					if (purchase.getStatus() != null && "Success".equalsIgnoreCase(purchase.getStatus()) && ((purchase.getSpentAmount() != null && purchase.getSpentAmount() > 0) || (purchase.getSpentPoints() != null && (purchase.getSpentPoints() > 0 || purchase.getSpentPoints() < 0)))) {
						firstTimePurchase = false;
						break;
					}
				}
			}
			LOG.info("First Time purchase : " + requestedAccountInfo.isWelcomegiftEligibleFlag());
			if (firstTimePurchase) {
				if ((paymentReq.getSpentAmount() != null && paymentReq.getSpentAmount() > 0) || (paymentReq.getSpentPoints() != null && paymentReq.getSpentPoints() > 0)) {
					long externalTransactionNumber = (long) (Math.random() * ((546546546 - 346546546) + 1)) + 346546546;
					MemberActivityPaymentDto memberActivity = new MemberActivityPaymentDto();
					memberActivity.setAccountNumber(paymentReq.getAccountNumber());
					memberActivity.setActivityCode(MarketplaceConstants.FIRSTACCESSACTIVITYCODE.getConstant());
					memberActivity.setEventDate(eventDateFormat.format(new Date()));
					memberActivity.setPartnerCode(OfferConstants.PURCHASE_PARTNER_CODE.get());
					if (paymentReq.getExtTransactionId() == null) {
						memberActivity.setExternalReferenceNumber("" + externalTransactionNumber);
					} else {
						memberActivity.setExternalReferenceNumber(paymentReq.getExtTransactionId());
					}
					memberActivity.setSpendValue(Double.valueOf(1));
					memberActivity.setMemberResponse(memberResponse);
					LOG.info("---------------Calling Member Activity Accrual API for Welcome Gift---------------");
					String jsonReq = new Gson().toJson(memberActivity);
					LOG.info("Accrual Request Object " + jsonReq);
					com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus response = redemptionReq(
							memberActivity, headers);
					if (response.getApiStatus().getStatusCode() == 0) {
						memberActivityResponse = (MemberActivityResponse) serviceHelper
								.convertToObject(response.getResult(), MemberActivityResponse.class);
						String reservePointsTxn = memberActivityResponse.getTransactionRefId();
						LOG.info("Welcome Gift Accrual Id : " + reservePointsTxn);
						UpdateWelcomeGiftReceivedFlagRequest request = new UpdateWelcomeGiftReceivedFlagRequest();
						request.setAccountNumber(paymentReq.getAccountNumber());
						request.setMembershipCode(membershipCode);
						updateWelcomeGiftRecieved(request, headers);
					} else {
						LOG.info("Welcome Gift Acrual Failed :" + response.getApiStatus().getMessage());
					}
				}
			}
		}
	}

	public void updateWelcomeGiftRecieved(UpdateWelcomeGiftReceivedFlagRequest welcomeGiftUpdate, Headers header) {
		try {
			LOG.info("Updating Member Management for WelcomeGift");
			String url = memberManagementUri + updateWelcomeGiftFlag;
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			if (!ObjectUtils.isEmpty(header)) {

				headers.set(RequestMappingConstants.TOKEN, header.getToken());
			}
			HttpEntity<UpdateWelcomeGiftReceivedFlagRequest> request = new HttpEntity<UpdateWelcomeGiftReceivedFlagRequest>(
					welcomeGiftUpdate, headers);
			ResponseEntity<String> response = retryCallForUpdateWelcomeGiftRecieved(url, request);
		} catch (Exception e) {
			LOG.info("Exception occured while Updating Member Management for WelcomeGift");
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), welcomeGiftUpdate.getAccountNumber(), header.getUserName());
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
			LOG.error(e.getMessage());
		}
	}

	
	private ResponseEntity<String> retryCallForUpdateWelcomeGiftRecieved(String url, HttpEntity<UpdateWelcomeGiftReceivedFlagRequest> request) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForUpdateWelcomeGiftRecieved method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return restTemplate.postForEntity(url, request, String.class);
		});		
	}

	public String rtfFulfillment(String productCode, String accountNumber, String productType, String bypassPayment,
			String externalRef, String deductionRequired) throws ParserConfigurationException {
		String xmlReq = getRequestBody("en", productCode, accountNumber, productType, bypassPayment, deductionRequired);
		LOG.info("RTF Request :" + xmlReq);
		
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_XML);
			HttpEntity<String> request = new HttpEntity<String>(xmlReq, headers);
			LOG.info("Reqest object : " + request);
			long start = System.currentTimeMillis();
			ResponseEntity<String> response = null;
			try {
				response = retryCallForRtfFulfillment(rtfUri, request);
			} catch (Exception e) {
			LOG.error("RTF Call Failed :" + e.getMessage());
			exceptionLogService.saveExceptionsToExceptionLogs(e, externalRef, accountNumber, "");
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
			
		}
			
			long end = System.currentTimeMillis();
			if(null !=response)
			{
			LOG.info("RTF Response :" + response.getBody().replaceAll("\\s{2,}", " "));
			ThirdPartyCallLog callLog = new ThirdPartyCallLog();
			ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
			String statusCode = null;
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(response.getBody()));
				Document doc = builder.parse(src);
				statusCode = doc.getElementsByTagName("requestCode").item(0).getTextContent();
			} catch (Exception e) {
				LOG.error("Parse Failed :" + e.getMessage());
				exceptionLogService.saveExceptionsToExceptionLogs(e, externalRef, accountNumber, "");
				LOG.error("Exception deatils is saved in ExceptionLogs collection");
				
			}
			if (statusCode.equalsIgnoreCase("SC-00000")) {
				
				
				serviceCallLogsDto.setAccountNumber(accountNumber);
				serviceCallLogsDto.setCreatedDate(new Date());
				serviceCallLogsDto.setAction("RTF");
				serviceCallLogsDto.setTransactionId(externalRef);
				serviceCallLogsDto.setServiceType("Outbound");
				serviceCallLogsDto.setRequest(request.getBody());
				serviceCallLogsDto.setResponse(response.getBody());
				serviceCallLogsDto.setStatus("Success");
				
				serviceCallLogsDto.setResponseTime((end-start));
				
				
				saveRequestResponse(serviceCallLogsDto);
			} else {
				
				serviceCallLogsDto.setAccountNumber(accountNumber);
				serviceCallLogsDto.setCreatedDate(new Date());
				serviceCallLogsDto.setAction("RTF");
				serviceCallLogsDto.setTransactionId(externalRef);
				serviceCallLogsDto.setServiceType("Outbound");
				serviceCallLogsDto.setRequest(request.getBody());
				serviceCallLogsDto.setResponse(response.getBody());
				serviceCallLogsDto.setStatus("Failed");
				
				serviceCallLogsDto.setResponseTime((end-start));
				saveRequestResponse(serviceCallLogsDto);
				
			}
			return response.getBody();
			}
		/*
		 * System.out.println(response); DocumentBuilderFactory factory =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder builder =
		 * factory.newDocumentBuilder(); org.w3c.dom.Document doc =
		 * builder.parse("<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?>" +
		 * response.getBody()); String str =
		 * doc.getElementsByTagName("response").item(0).getTextContent();
		 * System.out.println(str);
		 */
		return null;
	}

	private ResponseEntity<String> retryCallForRtfFulfillment(String rtfUri, HttpEntity<String> request) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForRtfFulfillment method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return  restTemplate.postForEntity(rtfUri, request, String.class);
		});		
	}
	
	public String getRequestBody(String language, String productCode, String accountNumber, String productType,
			String bypassPayment, String deductionRequired) {
		if (productCode == null) {
			productCode = "";
		}
		if (productType == null) {
			productType = "";
		}
		return new StringBuilder().append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("<request>")
				.append("<channel>").append("LOYALTY").append("</channel>").append("<priority>").append("10")
				.append("</priority>").append("<language>").append(language).append("</language>")
				.append("<requestItems>").append("<requestItem>").append("<productCode>").append(productCode)
				.append("</productCode>").append("<operation>").append("ADD").append("</operation>")
				.append("<accountNumber>").append(accountNumber).append("</accountNumber>").append("<productType>")
				.append(productType).append("</productType>").append("<bypassPayment>").append(bypassPayment)
				.append("</bypassPayment>").append("<requestItemParameters>").append("<parameter>").append("<name>").append("IS_DEDUCTION_REQUIRED")
				.append("</name>").append("<value>").append(deductionRequired).append("</value>").append("</parameter>").append("</requestItemParameters>").append("</requestItem>").append("</requestItems>").append("</request>")
				.toString();
	}

	/**
	 * @param memberActivityDto
	 * @param header
	 * @return
	 */
	public com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus redemptionReq(
			MemberActivityPaymentDto memberActivityDto, Headers header) {
		try {
			String url = memberActivityUri + reserveOrCommit;
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("username", "loyalty");
			if (!ObjectUtils.isEmpty(header)) {
				headers.set(RequestMappingConstants.CHANNEL_ID, header.getChannelId());
				headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, header.getExternalTransactionId());
				headers.set(RequestMappingConstants.TOKEN, header.getToken());
			}
			HttpEntity<MemberActivityPaymentDto> request = new HttpEntity<MemberActivityPaymentDto>(memberActivityDto,
					headers);
			ResponseEntity<String> response = retryCallForReedemptionOrAccrualRequest(url, request);
			Gson gsonObje = new Gson();
			com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus responseObj = gsonObje
					.fromJson(response.getBody(),
							com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus.class);
			LOG.info("Reserve API Response Object :" + responseObj);
			LOG.info("Reserve API Response message :" + responseObj.getApiStatus().getMessage());
			return responseObj;
		} catch (Exception e) {
			LOG.error("Exception occured while calling Reserve API");
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), memberActivityDto.getAccountNumber(), header.getUserName());
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
			LOG.error(e.getMessage());
		}
		return null;
	}

	public void manualRollback(String transactionRefId, Headers header) {
		try {
			RollbackAccrualOrRedemptionDto rollbackDto = new RollbackAccrualOrRedemptionDto();
			rollbackDto.setTransactionRefId(transactionRefId);
			String url = pointbankUri + rollbackApi;
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("username", "loyalty");
			if (!ObjectUtils.isEmpty(header)) {
				headers.set(RequestMappingConstants.CHANNEL_ID, header.getChannelId());
				headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, header.getExternalTransactionId());
				headers.set(RequestMappingConstants.TOKEN, header.getToken());
			}
			HttpEntity<RollbackAccrualOrRedemptionDto> request = new HttpEntity<RollbackAccrualOrRedemptionDto>(rollbackDto,
					headers);
			ResponseEntity<String> response = retryCallForManualRollback(url, request);
			Gson gsonObje = new Gson();
			com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus responseObj = gsonObje
					.fromJson(response.getBody(),
							com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus.class);
			LOG.info("Rollback API Response Object :" + responseObj);
			if (responseObj.getApiStatus().getStatusCode() == 0) {
				LOG.info("Successfully Rolled Back");
			}else{
				LOG.info("Rolled Back Failed");
			}
		} catch (Exception e) {
			LOG.error("Exception occured while manual rollback");
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), "", header.getUserName());
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
			LOG.error(e.getMessage());
		}
	}
	
	private ResponseEntity<String> retryCallForManualRollback(String url, HttpEntity<RollbackAccrualOrRedemptionDto> request) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForManualRollback method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return  restTemplate.postForEntity(url, request, String.class);
		});		
	}
	/**
	 * @param memberActivityDto
	 * @param header
	 * @return
	 */
	public com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus referralAccrualReq(
			MemberActivityPaymentDto memberActivityDto, Headers header) {
		try {
			String url = memberActivityUri + referralAccrual;
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("username", "loyalty");
			if (!ObjectUtils.isEmpty(header)) {
				headers.set(RequestMappingConstants.CHANNEL_ID, header.getChannelId());
				headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, header.getExternalTransactionId());
				headers.set(RequestMappingConstants.TOKEN, header.getToken());
			}
			HttpEntity<MemberActivityPaymentDto> request = new HttpEntity<MemberActivityPaymentDto>(memberActivityDto,
					headers);
			ResponseEntity<String> response = retryCallForReedemptionOrAccrualRequest(url, request);
			Gson gsonObje = new Gson();
			com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus responseObj = gsonObje
					.fromJson(response.getBody(),
							com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus.class);
			LOG.info("Referral Acrual API Response Object :" + responseObj);
			LOG.info("Referral Accrual API Response message :" + responseObj.getApiStatus().getMessage());
			return responseObj;
		} catch (Exception e) {
			LOG.error("Exception occured while Referral Acrual Reuest processing");
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), memberActivityDto.getAccountNumber(), header.getUserName());
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
			LOG.error(e.getMessage());
		}
		return null;
	}
	
	private ResponseEntity<String> retryCallForReedemptionOrAccrualRequest(String url, HttpEntity<MemberActivityPaymentDto> request) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForReedemptionOrAccrualRequest method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return  restTemplate.postForEntity(url, request, String.class);
		});		
	}
	public VoucherResponse generateVoucher(OfferCatalog offerCatalog, PurchaseRequestDto paymentReq, String uuid,
			Headers header) {
		VoucherRequestDto voucherRequestDto = new VoucherRequestDto();
		voucherRequestDto.setVoucherAction(offerCatalog.getVoucherInfo().getVoucherAction());
		voucherRequestDto.setMerchantCode(offerCatalog.getMerchant().getMerchantCode());
		voucherRequestDto.setMerchantName(offerCatalog.getMerchant().getMerchantName().getMerchantNameEn());
		voucherRequestDto.setPartnerCode(offerCatalog.getPartnerCode());
		if (offerCatalog.getSubOffer() != null && !offerCatalog.getSubOffer().isEmpty()) {
			voucherRequestDto.setSubOfferId(offerCatalog.getSubOffer().get(0).getSubOfferId());
		} else {
			voucherRequestDto.setSubOfferId(null);
		}
		if (offerCatalog.getDenominations() != null && !offerCatalog.getDenominations().isEmpty()) {
			voucherRequestDto.setDenominationId(offerCatalog.getDenominations().get(0).getDenominationId());
		} else {
			voucherRequestDto.setDenominationId(null);
		}

		SubOffer subOffer = paymentReq.getSelectedPaymentItem().equalsIgnoreCase(OffersConfigurationConstants.dealVoucherItem)
				? FilterValues.findAnySubOfferInList(offerCatalog.getSubOffer(),
						Predicates.sameSubOfferId(paymentReq.getSubOfferId()))
				: null;
		voucherRequestDto.setAccountNumber(paymentReq.getAccountNumber());
		voucherRequestDto.setMembershipCode(paymentReq.getMembershipCode());
		voucherRequestDto.setOfferId(offerCatalog.getOfferId());
		voucherRequestDto.setUuid(uuid);
		voucherRequestDto.setVoucherExpiryPeriod(offerCatalog.getVoucherInfo().getVoucherExpiryPeriod());
		voucherRequestDto.setVoucherExpiryDate(offerCatalog.getVoucherInfo().getVoucherExpiryDate());
		voucherRequestDto.setVoucherAmount(ProcessValues.getAmountValue(paymentReq, offerCatalog, subOffer));
		voucherRequestDto.setNumberOfVoucher(paymentReq.getCouponQuantity());
		voucherRequestDto.setPointsValue(paymentReq.getSpentPoints());
		voucherRequestDto.setCost(paymentReq.getSpentAmount());
		voucherRequestDto.setOfferType(offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn());
		voucherRequestDto.setBarcodeType(offerCatalog.getMerchant().getBarcodeType());
		voucherRequestDto.setOfferTypeId(offerCatalog.getOfferType().getOfferTypeId()); // check with Kinkar
		voucherRequestDto.setIsBirthdayGift(offerCatalog.getIsBirthdayGift()); // check with Kinkar
		voucherRequestDto.setExternalName(offerCatalog.getMerchant().getExternalName());
		voucherRequestDto.setVoucherDenomination(paymentReq.getVoucherDenomination());
		voucherRequestDto.setMambaFoodVoucher(Checks.checkIsMamba(offerCatalog.getFreeOffers()));
		voucherRequestDto.setSubscPromo(false);
		voucherRequestDto.setGifted(false);
		if(offerCatalog.getMerchant()!=null && offerCatalog.getMerchant().getContactPersons()!=null) {
			List<String> merchantEmail=offerCatalog.getMerchant().getContactPersons().stream().map(e -> e.getEmailId() == null?"null":e.getEmailId()).collect(Collectors.toList());
			voucherRequestDto.setMerchantEmail(merchantEmail);
		}

		if(!ObjectUtils.isEmpty(offerCatalog) && !ObjectUtils.isEmpty(offerCatalog.getSubscriptionDetails()) && !ObjectUtils.isEmpty(offerCatalog.getSubscriptionDetails().getSubscPromo()) &&
				offerCatalog.getSubscriptionDetails().getSubscPromo()) {
			voucherRequestDto.setSubscPromo(true);
			voucherRequestDto.setGifted(true);
		} 
		LOG.info("---------------Calling Marketplace for Voucher Generation---------------");
		String jsonReq = new Gson().toJson(voucherRequestDto);
		LOG.info("Generate Voucher Request Object " + jsonReq);
		try {
//			String url = marketplaceUri + generatevoucher;
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			if (!ObjectUtils.isEmpty(header)) {
//				headers.set(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, header.getExternalTransactionId());
//				headers.set(RequestMappingConstants.TOKEN, header.getToken());
//			}
//			HttpEntity<VoucherRequestDto> request = new HttpEntity<VoucherRequestDto>(voucherRequestDto, headers);
//			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//			Gson gsonObje = new Gson();
//			VoucherResponse responseObj = gsonObje.fromJson(response.getBody(), VoucherResponse.class);
			VoucherResponse responseObj = voucherController.generateVoucher(voucherRequestDto, null, null, header.getExternalTransactionId(), null, null, null, header.getChannelId(), null, null, header.getToken(), null);
			LOG.info("Generate Voucher Response Object :" + responseObj);
			return responseObj;
		} catch (Exception e) {
			LOG.error("Exception occured in generate voucher");
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), paymentReq.getAccountNumber(), header.getUserName());
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
			LOG.error(e.getMessage());
		}
		return null;
	}

	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		try{
			LOG.info("Requestand response saved :" + callLog.toString());
			jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
		} catch(Exception e){
			LOG.info("JMS Exception Occured");
			exceptionLogService.saveExceptionsToExceptionLogs(e, callLog.getTransactionId(), callLog.getAccountNumber(), callLog.getCreatedUser());
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
		}
	}

	public CommonApiStatus rbtServices(String serviceId, String packName, String msisdn, String externalRef) {
		URL wsdlUrl = null;
		 try {
			 wsdlUrl = new URL(rbtUri+"?wsdl");
	        } catch (MalformedURLException e) {
	        	LOG.info("Cannot create URL from : " + rbtUri+"?wsdl");
	        }
		ServiceOrderMgmtCUD_Service service = new ServiceOrderMgmtCUD_Service(wsdlUrl);
		ServiceOrderMgmtCUD port = service.getPort(ServiceOrderMgmtCUD.class);
		BindingProvider provider = (BindingProvider) port;
		provider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, tibcoUsername);
		provider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, tibcoPassword);
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, rbtUri);
		SubscribeServicesRequest subServiceReq = new SubscribeServicesRequest();
		SubscribeServicesResponse response = new SubscribeServicesResponse();
		com.loyalty.marketplace.outbound.dto.CommonApiStatus result = new com.loyalty.marketplace.outbound.dto.CommonApiStatus(
				null);
		ApplicationHeader header = new ApplicationHeader();
		String transactionID = "" + 1000 + Math.abs(new Random().nextInt(9000));
		header.setTransactionID(transactionID);
		header.setRequestedSystem(MarketplaceConstants.REQUESTEDSYSTEM.getConstant());
		subServiceReq.setApplicationHeader(header);
		SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails crbtSubDetails = new SubscribeServicesRequest.DataHeader.CRBTSubscriptionDetails();
		crbtSubDetails.setServiceID(serviceId);
		crbtSubDetails.setAPartyMSISDN(msisdn.replaceFirst("0", "971"));
		// crbtSubDetails.setToneID(toneId);
		crbtSubDetails.setPackName(packName);
		crbtSubDetails.setPriority(MarketplaceConstants.PRIORITY.getConstant());
		crbtSubDetails.setLanguageID(MarketplaceConstants.LANGUAGE_ID.getConstant());
		crbtSubDetails.setPaymentMode(MarketplaceConstants.PAYMENT_MODE.getConstant());
		crbtSubDetails.setActivityId(MarketplaceConstants.ACTIVITY_ID.getConstant());
		SubscribeServicesRequest.DataHeader dataHeader = new SubscribeServicesRequest.DataHeader();
		dataHeader.setMsisdn(msisdn);
		dataHeader.setTargetSystem(MarketplaceConstants.TARGETED_SYSTEM.getConstant());
		dataHeader.setDuration(MarketplaceConstants.DURATION.getConstant());
		dataHeader.setServiceID(serviceId);
		dataHeader.getCRBTSubscriptionDetails().add(crbtSubDetails);
		subServiceReq.setDataHeader(dataHeader);
		LOG.info("----------Created a Request for OpSubscribeServices--------------");
		LOG.info(Utils.toXML(subServiceReq));
		ThirdPartyCallLog callLog = new ThirdPartyCallLog();
		ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
		
			LOG.info("-----------Fetching the OpSubscribeService's response--------------");
			long start = System.currentTimeMillis();
			try {
			response = port.opSubscribeServices(subServiceReq);
			}
			 catch (Exception e) {
					LOG.error("Failed while subscibing the service" + e.getMessage());

					serviceCallLogsDto.setAccountNumber(msisdn);
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("RBT");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(subServiceReq));
					serviceCallLogsDto.setStatus("Exception");
					
					saveRequestResponse(serviceCallLogsDto);
					
				}
			long end = System.currentTimeMillis();
			
			if (response != null) {
				LOG.info(Utils.toXML(response));
				if (response.getAckMessage().getStatus().equals("SUCCESS")) {
					response.getAckMessage().setErrorCode(CHARGE_SUCCESS);
					result.setMessage("Success" + transactionID);
					result.setStatusCode(0);
					result.setExternalTransactionId(response.getResponseData().getTransactionID());
					
					
					serviceCallLogsDto.setAccountNumber(msisdn);
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("RBT");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(subServiceReq));
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Success");
					
					serviceCallLogsDto.setResponseTime((end-start));
					saveRequestResponse(serviceCallLogsDto);
					
				} else {
					Errors error = new Errors();
					error.setCode(1);
					error.setMessage(response.getAckMessage().getErrorDescription());
					List<Errors> errorList = new ArrayList<Errors>();
					result.setErrors(errorList);
					result.setStatusCode(1);
					result.setMessage(response.getAckMessage().getErrorDescription());
					
					
					serviceCallLogsDto.setAccountNumber(msisdn);
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("RBT");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(subServiceReq));
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Failed");
					
					serviceCallLogsDto.setResponseTime((end-start));
					saveRequestResponse(serviceCallLogsDto);
				}
			} else {
				callLog.setResponsePayload("RBT call Failed with Null Response");
				callLog.setRequest_Payload(Utils.toXML(subServiceReq));
				callLog.setResponseTransactionId(externalRef);
				callLog.setServiceName("RBT");
				callLog.setAccountNumber(msisdn);
				callLog.setStatus("Failed");
				
				serviceCallLogsDto.setAccountNumber(msisdn);
				serviceCallLogsDto.setCreatedDate(new Date());
				serviceCallLogsDto.setAction("RBT");
				serviceCallLogsDto.setTransactionId(externalRef);
				serviceCallLogsDto.setServiceType("Outbound");
				serviceCallLogsDto.setRequest(Utils.toXML(subServiceReq));
				
				serviceCallLogsDto.setStatus("Failed");
				
				serviceCallLogsDto.setResponseTime((end-start));
				saveRequestResponse(serviceCallLogsDto);
			}
		
		return result;
	}
	
	/**
	 * TIBCO Payment posting call
	 * 
	 * @param paidTotalAmount
	 * @param paymentType
	 * @param accountId
	 * @param accountNmbr
	 * @param transactionType
	 * @param referenceNmbr
	 * @param cardNmbr
	 * @param cardToken
	 * @param cardExpiryDt
	 * @param authorizationCode
	 * @param paidAmount
	 * @param epgTransactionId
	 * @param cardSubType
	 * @param membershipCode
	 * @param loyaltyPoints
	 * @return
	 * @throws MarketplaceException
	 */
	public CommonApiStatus paymentPostingTibco(String paidTotalAmount, String paymentType, long accountId,
			String accountNmbr, String transactionType, String referenceNmbr, String cardNmbr, String cardToken,
			String cardExpiryDt, String authorizationCode, String paidAmount, String epgTransactionId,
			String cardSubType, String membershipCode, String loyaltyPoints, String externalRef, double rate,  Headers requestHeader, Integer retryCount) throws MarketplaceException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String transactionID = sdf.format(new Date()) + (1000 + Math.abs(new Random().nextInt(9000)));
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String sDate = sdf.format(new java.util.Date());
		URL wsdlUrl = null;
		CommonApiStatus result = new CommonApiStatus(null);
		 try {
			 wsdlUrl = new URL(tibcoUri+"?wsdl");
	        } catch (MalformedURLException e) {
	        	LOG.info("Cannot load wsdl from : " + tibcoUri+"?wsdl");
	        }
		
		GenericPaymentServiceServiceagent service = new GenericPaymentServiceServiceagent(wsdlUrl);
		GenericPaymentService port = service.getPort(GenericPaymentService.class);
		BindingProvider provider = (BindingProvider) port;
		provider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, tibcoUsername);
		provider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, tibcoPassword);
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, tibcoUri);
		PaymentPostingResponse response = new PaymentPostingResponse();
		PaymentPostingRequest paymentPostingReq = new PaymentPostingRequest();
		ApplicationHeader header = new ApplicationHeader();
		header.setRequestedSystem(MarketplaceConstants.REQUESTEDSYSTEM.getConstant());
		header.setRetryLimit("0");
		header.setTransactionID(transactionID);
		paymentPostingReq.setApplicationHeader(header);

		PaymentPostingRequest.DataHeader dataHeader = new PaymentPostingRequest.DataHeader();
		dataHeader.setPaidTotalAmount(paidTotalAmount);
		dataHeader.setPaymentModeCode(MarketplaceConstants.paymentModeCode.getConstant());
		if (!ObjectUtils.isEmpty(requestHeader.getChannelId()) && requestHeader.getChannelId().equalsIgnoreCase(MarketplaceConstants.PAYMENTCODESAPP.getConstant())) {
			dataHeader.setPaymentModeCode(MarketplaceConstants.PAYMENTCODESAPP.getConstant());
			dataHeader.setExternalTransactionCode(MarketplaceConstants.PAYMENTCODESAPP.getConstant() + transactionID);
		} else {
			dataHeader.setPaymentModeCode(MarketplaceConstants.paymentModeCode.getConstant());
			dataHeader.setExternalTransactionCode(MarketplaceConstants.paymentModeCode.getConstant() + transactionID);
		}
		dataHeader.setIsCancelled(MarketplaceConstants.IsCancelled.getConstant());
		dataHeader.setCashierName(MarketplaceConstants.cashierName.getConstant());
		dataHeader.setCurrencyRate(new BigDecimal(0));
		dataHeader.setBankCode(paymentType == MarketplaceConstants.loyaltyPymTyp.getConstant()
				? MarketplaceConstants.ptsBnkCd.getConstant()
				: MarketplaceConstants.ccBnkCd.getConstant());
		dataHeader.setCurrency(MarketplaceConstants.currency.getConstant());
		dataHeader.setChannelType(MarketplaceConstants.channelType.getConstant());
		dataHeader.setCollectionRegionCode(MarketplaceConstants.colltnRgnCd.getConstant());
		dataHeader.setPaymentType(paymentType);
		dataHeader.setExternalTransactionCode(MarketplaceConstants.paymentModeCode.getConstant() + transactionID);
		dataHeader.setChannelTransactionDate(sDate);
		dataHeader.setPaymentAccepted(MarketplaceConstants.PaymentAccepted.getConstant());
		dataHeader.setBulkPayment(MarketplaceConstants.BulkPayment.getConstant());
		dataHeader.setPaymentProcessed(MarketplaceConstants.PaymentProcessed.getConstant());
		dataHeader.setSMSNotification(MarketplaceConstants.SMSNotification.getConstant());
		dataHeader.setEmailNotification(MarketplaceConstants.EmailNotification.getConstant());

		PaymentPostingRequest.DataHeader.PaymentDetails paymentDtls = new PaymentPostingRequest.DataHeader.PaymentDetails();
		paymentDtls.setAccountId(accountId);
		paymentDtls.setAccountNumber(accountNmbr);
		if (transactionType.equalsIgnoreCase("bilpay")) {
			paymentDtls.setAccountType(MarketplaceConstants.POSTPAID.getConstant());
		} else {
			paymentDtls.setAccountType(MarketplaceConstants.PREPAID.getConstant());
		}
		paymentDtls.setPaymentCategory(MarketplaceConstants.PaymentCategory.getConstant());
		paymentDtls.setRegionCode(MarketplaceConstants.RegionCode.getConstant());
		paymentDtls.setTransactionType(transactionType);
		paymentDtls.setTopupCompleted(MarketplaceConstants.TopupCompleted.getConstant());
		paymentDtls.setReferenceNumber(referenceNmbr);
		paymentDtls.setPaidAmount(Double.parseDouble(paidAmount));
		dataHeader.setPaymentDetails(paymentDtls);

		if (paymentType.equalsIgnoreCase(MarketplaceConstants.CCPaymentType.getConstant())) {
			if (!cardSubType.equals("VISA") && !cardSubType.equals("AMEX") && !cardSubType.equals("DINERS CLUB")
					&& !cardSubType.equals("MASTER") && !cardSubType.equalsIgnoreCase("ApplePay") 
					&& !cardSubType.equalsIgnoreCase("SamsungPay")) {
				cardSubType = "MISCELLANEOUS";
			}
			PaymentPostingRequest.DataHeader.CardDetails cardDtls = new PaymentPostingRequest.DataHeader.CardDetails();
			cardDtls.setCardNumber(cardNmbr);
			cardDtls.setCardSubType(cardSubType);
			cardDtls.setCardAmount(Double.parseDouble(paidAmount));
			cardDtls.setCardToken(cardToken);
			cardDtls.setAuthorizationCode(authorizationCode);
			cardDtls.setCardExpiryDate(cardExpiryDt);
			cardDtls.setEPGTransactionID(epgTransactionId);
			cardDtls.setInstallmentFlag(0);
			cardDtls.setBankID(0l);
			dataHeader.setCardDetails(cardDtls);
		} else {
			PaymentPostingRequest.DataHeader.LoyaltyDetails loyaltyDtls = new PaymentPostingRequest.DataHeader.LoyaltyDetails();
			loyaltyDtls.setConversionRate(ConversionRate);
			loyaltyDtls.setLoyaltyAmount(Double.parseDouble(loyaltyPoints) * 0.01);
			loyaltyDtls.setLoyaltyNumber(membershipCode);
			loyaltyDtls.setLoyaltyPoints(Long.parseLong(loyaltyPoints));
			dataHeader.setLoyaltyDetails(loyaltyDtls);
		}
		paymentPostingReq.setDataHeader(dataHeader);
		String reqXml = Utils.toXML(paymentPostingReq);
		LOG.info("-----------Tibco Payment Posting Request XML Start--------------");
		LOG.info(reqXml);
		LOG.info("-----------Tibco Payment Posting Request XML End--------------");
		ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
			long start = System.currentTimeMillis();
			try{
			response = port.opPaymentPosting(paymentPostingReq);
			}
			 catch (Exception e) {
				 retryCount = retryCount+1;	
				 if (retryCount <=3 ) {
					 LOG.error("Tibco payment posting Failed, retrying --> {}", retryCount);
					 return paymentPostingTibco(paidTotalAmount, paymentType, accountId,
								accountNmbr, transactionType, referenceNmbr, cardNmbr, cardToken,
								cardExpiryDt, authorizationCode, paidAmount, epgTransactionId,
								cardSubType, membershipCode, loyaltyPoints, externalRef, rate, requestHeader, retryCount);
				 }
				 else {
						long end = System.currentTimeMillis();	
						LOG.error("Tibco payment posting Failed, reached maximum retry count");
						
						serviceCallLogsDto.setAccountNumber(accountNmbr);
						serviceCallLogsDto.setCreatedDate(new Date());
						serviceCallLogsDto.setAction("Tibco payment Posting");
						serviceCallLogsDto.setTransactionId(externalRef);
						serviceCallLogsDto.setServiceType("Outbound");
						serviceCallLogsDto.setRequest(Utils.toXML(paymentPostingReq));
						serviceCallLogsDto.setResponse(e.getMessage());
						serviceCallLogsDto.setStatus("Failed");
						
						serviceCallLogsDto.setCreatedUser("");
						serviceCallLogsDto.setResponseTime((end-start));
						
						
						saveRequestResponse(serviceCallLogsDto);
						LOG.info("Tibco payment posting Failed saved Exception to serviceCallLogs, externalTransactionId : {}", externalRef);
						result.setStatusCode(1);
						result.setMessage("Tibco payment posting Failed");
					LOG.error("Tibco payment posting Failed " + e.getMessage());
					e.printStackTrace();
					throw new MarketplaceException(this.getClass().toString(), "paymentPostingTibco",
							e.getClass() + e.getMessage(), MarketplaceCode.TIBCO_PAYMENT_POSTING_FAILED);
				 }
				}
			long end = System.currentTimeMillis();
			
		//	ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
			if (response != null) {
				if (response.getAckMessage().getStatus().equals("SUCCESS")) {
					response.getAckMessage().setErrorCode(CHARGE_SUCCESS);
					result.setMessage("Payment Success" + transactionID);
					result.setStatusCode(0);
					result.setExternalTransactionId(response.getResponseData().getTransactionID());
					
					serviceCallLogsDto.setAccountNumber(accountNmbr);
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("TIbco Payment Posting");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(reqXml);
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Success");
					serviceCallLogsDto.setCreatedUser("");
					serviceCallLogsDto.setResponseTime((end-start));
					
					
					saveRequestResponse(serviceCallLogsDto);
					
				} else {
					Errors error = new Errors();
					error.setCode(1);
					error.setMessage(response.getAckMessage().getErrorDescription());
					List<Errors> errorList = new ArrayList<Errors>();
					result.setErrors(errorList);
					result.setStatusCode(1);
					result.setMessage(response.getAckMessage().getErrorDescription());
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setAccountNumber(accountNmbr);
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("TIbco Payment Posting");
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(reqXml);
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Failed");
					serviceCallLogsDto.setCreatedUser("");
					serviceCallLogsDto.setResponseTime((end-start));
					saveRequestResponse(serviceCallLogsDto);
				}

			}
		
		return result;
	}
	
	/**
	 * Tibco Adjustment Posting call
	 * 
	 * @param accountNumber
	 * @param accId
	 * @param amount
	 * @return
	 * @throws PaymentFault
	 * @throws MarketplaceException
	 */
	public CommonApiStatus adjustmentPostingTIBCO(String accountNumber, long accId, double amount, String externalRef, Integer retryCount)
			throws PaymentFault, MarketplaceException {
		SimpleDateFormat requestedDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		AdjustmentPostingResponse response = new AdjustmentPostingResponse();
		CommonApiStatus result = new CommonApiStatus(null);
		URL wsdlUrl = null;
		 try {
			 wsdlUrl = new URL(tibcoUri+"?wsdl");
	        } catch (MalformedURLException e) {
	        	LOG.info("Cannot load wsdl from : " + tibcoUri+"?wsdl");
	        }
		
			String transactionID = "Loyalty_" + Math.abs(new Random().nextInt());
			GenericPaymentServiceServiceagent service = new GenericPaymentServiceServiceagent(wsdlUrl);
			GenericPaymentService port = service.getPort(GenericPaymentService.class);
			BindingProvider provider = (BindingProvider) port;
			provider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, tibcoUsername);
			provider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, tibcoPassword);
			provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, tibcoUri);
			AdjustmentPostingRequest adjustmentPostingRequest = new AdjustmentPostingRequest();
			ApplicationHeader header = new ApplicationHeader();
			header.setTransactionID(transactionID);
			header.setRequestedSystem(MarketplaceConstants.REQUESTEDSYSTEM.getConstant());
			header.setRetryLimit("0");
			adjustmentPostingRequest.setApplicationHeader(header);
			DataHeader dataHeader = new DataHeader();
			AdjustmentPostingRequest.DataHeader.AdjustmentDetails adjustmentDtls = new AdjustmentPostingRequest.DataHeader.AdjustmentDetails();
			adjustmentDtls.setAdjChargeCode(MarketplaceConstants.CHARGE_CODE.getConstant());
			adjustmentDtls.setAdjType(MarketplaceConstants.CHARGE_TYPE_IMM.getConstant());
			adjustmentDtls.setAdjReasonCode(MarketplaceConstants.ADJUSTMNT_REASON.getConstant());
			adjustmentDtls.setAdjTransType(MarketplaceConstants.TRANS_TYPE.getConstant());
			adjustmentDtls.setAccountNumber(accountNumber);
			adjustmentDtls.setAccountID(accId);
			adjustmentDtls.setAccountType(MarketplaceConstants.POSTPAID.getConstant());
			adjustmentDtls.setAdjAmount(amount);
			adjustmentDtls.setReferenceNumber(transactionID);
			adjustmentDtls.getAdditionalInfo().add(setAdditionalInfo("VATAmount", "0"));
			adjustmentDtls.getAdditionalInfo().add(setAdditionalInfo("VATRatio", "0"));
			dataHeader.setRequestedUserName(MarketplaceConstants.REQUESTEDSYSTEM.getConstant());
			dataHeader.setExternalTransactionCode(transactionID);
			dataHeader.setCollectionLocationCode("");
			dataHeader.setCollectionLocationName("");
			dataHeader.setCollectionRegionCode("");
			dataHeader.setChannelTransactionDate(requestedDateFormat.format(new Date()));
			dataHeader.getAdjustmentDetails().add(adjustmentDtls);
			adjustmentPostingRequest.setDataHeader(dataHeader);
			// String reqXml = Utils.toXML(adjustmentPostingRequest);
			ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
			long start = System.currentTimeMillis();
			LOG.info("-----------Tibco Adjustment Posting Request XML Start--------------");
			LOG.info(Utils.toXML(adjustmentPostingRequest));
			LOG.info("-----------Tibco Adjustment Posting Request XML End--------------");
			LOG.info("Tibco Adjusment Posting calling");
			try {
			response = port.opAdjustmentPosting(adjustmentPostingRequest);
			}
			catch (Exception e) {
					 retryCount = retryCount+1;	
					 if (retryCount <=3 ) {
						 LOG.error("Tibco adjusment posting Failed, retrying --> {}", retryCount);
						 return adjustmentPostingTIBCO(accountNumber, accId, amount, externalRef, retryCount);
					 }
					 else {
							long end = System.currentTimeMillis();	
							LOG.error("Tibco adjusment posting Failed, reached maximum retry count");
						
							serviceCallLogsDto.setAccountNumber(accountNumber);
							serviceCallLogsDto.setCreatedDate(new Date());
							serviceCallLogsDto.setAction("Tibco Adjustment Posting");
							serviceCallLogsDto.setTransactionId(externalRef);
							serviceCallLogsDto.setServiceType("Outbound");
							serviceCallLogsDto.setRequest(Utils.toXML(adjustmentPostingRequest));
							serviceCallLogsDto.setResponse(e.getMessage());
							serviceCallLogsDto.setStatus("Failed");
							
							serviceCallLogsDto.setResponseTime((end-start));
							
							
							saveRequestResponse(serviceCallLogsDto);
							LOG.info("Tibco adjusment posting Failed saved Exception to serviceCallLogs, externalTransactionId : {}", externalRef);
							LOG.error("Tibco adjusment posting Failed " + e.getMessage());
							throw new MarketplaceException(this.getClass().toString(), "adjustmentPostingTIBCO",
									e.getClass() + e.getMessage(), MarketplaceCode.TIBCO_ADJUSMENT_POSTING_FAILED);
					}
			}
			ThirdPartyCallLog callLog = new ThirdPartyCallLog();
//			ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
			long end = System.currentTimeMillis();
			
			if (response != null) {
				LOG.info("-----------Tibco Adjustment Posting Response XML Start--------------");
				LOG.info(Utils.toXML(response));
				LOG.info("-----------Tibco Adjustment Posting Response XML End--------------");
				if (response.getAckMessage().getStatus().equals("SUCCESS")) {
					LOG.info("AdjusmentPosting Success");
					response.getAckMessage().setErrorCode(CHARGE_SUCCESS);
					result.setMessage("Payment Success" + transactionID);
					result.setStatusCode(0);
					result.setExternalTransactionId(response.getResponseData().getTransactionID());
					
					
					serviceCallLogsDto.setAccountNumber(accountNumber);
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("Tibco Adjustment Posting");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(adjustmentPostingRequest));
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Success");
					
					serviceCallLogsDto.setResponseTime((end-start));
					
					
					saveRequestResponse(serviceCallLogsDto);
					
					
				} else {
					LOG.info("AdjustmentPosting response failed");
					Errors error = new Errors();
					error.setCode(1);
					error.setMessage(response.getAckMessage().getErrorDescription());
					List<Errors> errorList = new ArrayList<Errors>();
					result.setErrors(errorList);
					result.setStatusCode(1);
					result.setMessage(response.getAckMessage().getErrorDescription());
					callLog.setResponsePayload(Utils.toXML(response));
					callLog.setRequest_Payload(Utils.toXML(adjustmentPostingRequest));
					callLog.setFailedRreason(response.getAckMessage().getErrorDescription());
					callLog.setServiceName("Tibco Adjustment Posting");
					callLog.setAccountNumber(accountNumber);
					callLog.setStatus("Failed");
					
					serviceCallLogsDto.setAccountNumber(accountNumber);
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("Tibco Adjustment Posting");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(adjustmentPostingRequest));
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Failed");
					
					serviceCallLogsDto.setResponseTime((end-start));
					
					
					saveRequestResponse(serviceCallLogsDto);
				}
			
		} 

		return result;
	}
	
	/**
	 * TIBCO MISC Payment Posting call
	 * 
	 * @param paidTotalAmount
	 * @param paymentType
	 * @param referenceNmbr
	 * @param cardNbr
	 * @param cardSubType
	 * @param cardToken
	 * @param authorizationCode
	 * @param membershipCode
	 * @param loyaltyPoints
	 * @return {@link MiscPaymentResponse}
	 * @throws MarketplaceException
	 */
	@SuppressWarnings("unused")
	public CommonApiStatus miscPaymentPostingTIBCO(String paidTotalAmount, String paymentType, String referenceNmbr,
			String cardNbr, String cardSubType, String cardToken, String authorizationCode, String membershipCode,
			String loyaltyPoints, String externalRef, String subscriptionFlag, String epgTransactionId,  Headers requestHeader, Integer retryCount) throws MarketplaceException {
		URL wsdlUrl = null;
		 try {
			 wsdlUrl = new URL(tibcoUri+"?wsdl");
	        } catch (MalformedURLException e) {
	        	LOG.info("Cannot load wsdl from : " + tibcoUri+"?wsdl");
	        }
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		GenericPaymentServiceServiceagent service = new GenericPaymentServiceServiceagent(wsdlUrl);
		GenericPaymentService port = service.getPort(GenericPaymentService.class);
		CommonApiStatus result = new CommonApiStatus(null);
		BindingProvider provider = (BindingProvider) port;
		provider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, tibcoUsername);
		provider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, tibcoPassword);
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, tibcoUri);
		String transactionID = "MISC_" + sdf.format(new Date()) + (1000 + Math.abs(new Random().nextInt(9000)));
		MiscPaymentRequest miscReq = new MiscPaymentRequest();
		MiscPaymentResponse response = new MiscPaymentResponse();
		ApplicationHeader header = new ApplicationHeader();
		header.setTransactionID(transactionID);
		header.setRequestedSystem(MarketplaceConstants.REQUESTEDSYSTEM.getConstant());
		header.setRetryLimit("0");
		miscReq.setApplicationHeader(header);
		MiscPaymentRequest.DataHeader dataHeader = new MiscPaymentRequest.DataHeader();
		double roundedPaidTotalAmount = (double) Math.round(Double.parseDouble(paidTotalAmount) * 100.0) / 100.0;
		dataHeader.setPaidTotalAmount(roundedPaidTotalAmount);
		dataHeader.setIsCancelled(MarketplaceConstants.IsCancelled.getConstant());
		String bankCode = paymentType == MarketplaceConstants.loyaltyPymTyp.getConstant()
				? MarketplaceConstants.ptsBnkCd.getConstant()
				: MarketplaceConstants.ccBnkCd.getConstant();

		if (requestHeader != null && requestHeader.getChannelId() != null 
				&& requestHeader.getChannelId().equalsIgnoreCase(MarketplaceConstants.CHANNEL_GCHAT.getConstant())) {
			bankCode = paymentType == MarketplaceConstants.loyaltyPymTyp.getConstant()
					? MarketplaceConstants.GCHAT_PTSBNKCODE.getConstant() : MarketplaceConstants.GCHAT_CCBNKCODE.getConstant();
		} else 
		if (null != subscriptionFlag && subscriptionFlag.equalsIgnoreCase(MarketplaceConstants.RENEW_AUTORENEWAL_SUBSCRIPTION.getConstant())) {
			bankCode =	MarketplaceConstants.SUBSCRIPTION_RENEWAL_BANKCODE.getConstant();
		}
		dataHeader.setBankCode(bankCode);
		dataHeader.setCurrency(MarketplaceConstants.currency.getConstant());
		dataHeader.setPaymentType(paymentType);
		dataHeader.setExternalTransactionCode(MarketplaceConstants.paymentModeCode.getConstant() + transactionID);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String sDate = sdf2.format(new java.util.Date());
		dataHeader.setChannelTransactionDate(sDate);
		dataHeader.setChannelType(MarketplaceConstants.channelType.getConstant());
		dataHeader.setCollectionRegionCode(MarketplaceConstants.colltnRgnCd.getConstant());
		dataHeader.setCashierName(MarketplaceConstants.cashierName.getConstant());
		dataHeader.setPaymentModeCode(MarketplaceConstants.paymentModeCode.getConstant());

		if (requestHeader != null && requestHeader.getChannelId() != null
				&& requestHeader.getChannelId().equalsIgnoreCase(MarketplaceConstants.CHANNEL_GCHAT.getConstant())) {
			dataHeader.setPaymentModeCode(MarketplaceConstants.GCHAT_PAYMENTMODECODE.getConstant());
	        dataHeader.setExternalTransactionCode(MarketplaceConstants.GCHAT_PAYMENTMODECODE.getConstant() + transactionID);
	    } else 
		if (requestHeader != null && requestHeader.getChannelId() != null 
	    		&& requestHeader.getChannelId().equalsIgnoreCase(MarketplaceConstants.PAYMENTCODESAPP.getConstant())) {
	    	dataHeader.setPaymentModeCode(MarketplaceConstants.PAYMENTCODESAPP.getConstant());
			dataHeader.setExternalTransactionCode(MarketplaceConstants.PAYMENTCODESAPP.getConstant() + transactionID);
		} else {
            dataHeader.setPaymentModeCode(MarketplaceConstants.paymentModeCode.getConstant());
            dataHeader.setExternalTransactionCode(MarketplaceConstants.paymentModeCode.getConstant() + transactionID);
        }
		
		miscReq.setDataHeader(dataHeader);
		MiscPaymentRequest.DataHeader.MiscPaymentDetails miscPaymentDetails = new MiscPaymentRequest.DataHeader.MiscPaymentDetails();
		miscPaymentDetails.setAmount(roundedPaidTotalAmount);
		miscPaymentDetails.setReferenceNumber(referenceNmbr);
		if (null != subscriptionFlag && (subscriptionFlag.equalsIgnoreCase(MarketplaceConstants.NEW_AUTORENEWAL_SUBSCRIPTION.getConstant())
				||subscriptionFlag.equalsIgnoreCase(MarketplaceConstants.RENEW_AUTORENEWAL_SUBSCRIPTION.getConstant()))) {
			miscPaymentDetails.setMiscPurposeCode("A27");	
		}
		else {
			miscPaymentDetails.setMiscPurposeCode("ZT");
		}
		if (null != subscriptionFlag && (subscriptionFlag.equalsIgnoreCase(MarketplaceConstants.NEW_AUTORENEWAL_SUBSCRIPTION.getConstant())
				||subscriptionFlag.equalsIgnoreCase(MarketplaceConstants.RENEW_AUTORENEWAL_SUBSCRIPTION.getConstant()))) {
			double vatAmount = (0.05 * Double.parseDouble(paidTotalAmount));
			
			miscPaymentDetails.getAdditionalInfo().add(setMiscAdditionalInfo("VATAmount", String.valueOf(vatAmount)));
			miscPaymentDetails.getAdditionalInfo().add(setMiscAdditionalInfo("VATRatio", "5"));
		}
		else {
			miscPaymentDetails.getAdditionalInfo().add(setMiscAdditionalInfo("VATAmount", "0"));
			miscPaymentDetails.getAdditionalInfo().add(setMiscAdditionalInfo("VATRatio", "0"));
		}

		dataHeader.getMiscPaymentDetails().add(miscPaymentDetails);
		if (paymentType.equalsIgnoreCase(MarketplaceConstants.cardPymTyp.getConstant())) {
			MiscPaymentRequest.DataHeader.CardDetails cardDtls = new MiscPaymentRequest.DataHeader.CardDetails();
			cardDtls.setCardNumber(cardNbr);
			cardDtls.setCardType(MarketplaceConstants.CREDIT.getConstant());
			if (!cardSubType.equalsIgnoreCase("VISA") && !cardSubType.equalsIgnoreCase("AMEX")
					&& !cardSubType.equalsIgnoreCase("DINERS CLUB") && !cardSubType.equalsIgnoreCase("MASTERCARD")
					&& !cardSubType.equalsIgnoreCase("ApplePay") && !cardSubType.equalsIgnoreCase("SamsungPay")) {
				cardSubType = "MISCELLANEOUS";
			}
			cardDtls.setCardSubType(cardSubType);
			cardDtls.setCardToken(cardToken);
			cardDtls.setAuthorizationCode(authorizationCode);
			cardDtls.setEPGTransactionID(epgTransactionId);	
			dataHeader.setCardDetails(cardDtls);
		} else if (paymentType.equalsIgnoreCase(MarketplaceConstants.loyaltyPymTyp.getConstant())) {
			MiscPaymentRequest.DataHeader.LoyaltyDetails loyaltyDtls = new MiscPaymentRequest.DataHeader.LoyaltyDetails();
			loyaltyDtls.setConversionRate(0.0);
			loyaltyDtls.setLoyaltyNumber(membershipCode);
			loyaltyDtls.setLoyaltyPoints(Long.parseLong(loyaltyPoints));
			dataHeader.setLoyaltyDetails(loyaltyDtls);
		}
		LOG.info("-----------Tibco MISC Payment Posting Request Start--------------");
		LOG.debug(Utils.toXML(miscReq));
		LOG.info("-----------Tibco MISC Payment Posting Request End--------------");
		ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();
			long start = System.currentTimeMillis();
			LOG.info("Tibco Misc Posting Called");
			
			try {
			response = port.opMiscPayment(miscReq);
			}
			 catch (Exception e) {
				 retryCount = retryCount+1;	
				 if (retryCount <3 ) {
					 LOG.error("Tibco misc posting Failed, retrying --> {}", retryCount);
					 return miscPaymentPostingTIBCO(paidTotalAmount, paymentType, referenceNmbr, cardNbr, cardSubType, cardToken, authorizationCode,
							 membershipCode, loyaltyPoints, externalRef, subscriptionFlag, epgTransactionId, requestHeader, retryCount);
				 }
				 else {
					 LOG.error("Tibco misc posting Failed reached maximum retry count");
						long end = System.currentTimeMillis();	
						
					 	serviceCallLogsDto.setCreatedDate(new Date());
						serviceCallLogsDto.setAction("TIbco Misc");
						serviceCallLogsDto.setTransactionId(externalRef);
						serviceCallLogsDto.setServiceType("Outbound");
						serviceCallLogsDto.setRequest(Utils.toXML(miscReq));
						serviceCallLogsDto.setResponse(e.getMessage());
						serviceCallLogsDto.setStatus("Failed");
						
						serviceCallLogsDto.setResponseTime((end-start));
						saveRequestResponse(serviceCallLogsDto);
						LOG.info("Tibco misc posting Failed saved Exception to serviceCallLogs, externalTransactionId : {}", externalRef);
					 throw new MarketplaceException(this.getClass().toString(), "miscPaymentPostingTIBCO",
							e.getClass() + e.getMessage(), MarketplaceCode.TIBCO_MISC_POSTING_FAILED);
				}
			 }
			long end = System.currentTimeMillis();
			
			ThirdPartyCallLog callLog = new ThirdPartyCallLog();
			if (response != null) {
				LOG.info("-----------Tibco MISC Payment Posting Response Start--------------");
				LOG.debug(Utils.toXML(response));
				LOG.info("-----------Tibco MISC Payment Posting Response End--------------");
				if (response.getAckMessage().getStatus().equals("SUCCESS")) {
					response.getAckMessage().setErrorCode(CHARGE_SUCCESS);
					result.setMessage("Payment Success " + transactionID);
					result.setExternalTransactionId(response.getResponseData().getTransactionID());
					result.setStatusCode(0);
					result.setOverallStatus("Success");
					
					
					
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("TIbco Misc");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(miscReq));
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Success");
					
					serviceCallLogsDto.setResponseTime((end-start));
					
					
					saveRequestResponse(serviceCallLogsDto);
				} else {
					Errors error = new Errors();
					error.setCode(1);
					error.setMessage(response.getAckMessage().getErrorDescription());
					List<Errors> errorList = new ArrayList<Errors>();
					result.setErrors(errorList);
					result.setStatusCode(1);
					result.setMessage(response.getAckMessage().getErrorDescription());
					result.setOverallStatus("Failed");
					
					
					serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("TIbco Misc");
					serviceCallLogsDto.setTransactionId(externalRef);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(miscReq));
					serviceCallLogsDto.setResponse(Utils.toXML(response));
					serviceCallLogsDto.setStatus("Failed");
					
					serviceCallLogsDto.setResponseTime((end-start));
					saveRequestResponse(serviceCallLogsDto);
				}

			}
		
		return result;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	private AdditionalInfo setAdditionalInfo(String name, String value) {
		AdditionalInfo additionalInfo = new AdditionalInfo();
		additionalInfo.setName(name);
		additionalInfo.setValue(value);
		return additionalInfo;
	}
	
	private ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest.AdditionalInfo setMiscAdditionalInfo(
			String name, String value) {
		ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest.AdditionalInfo additionalInfo = new ae.etisalat.middleware.genericpaymentservice.miscpaymentrequest.AdditionalInfo();
		additionalInfo.setName(name);
		additionalInfo.setValue(value);
		return additionalInfo;
	}

	/**
	 * 
	 */
	public PaymentService() {
		
	}
	
	/**
	 * @param purchaseReuestDto
	 * @return result
	 */
	public CommonApiStatus callMiscPaymentPostingForAutoRenewalSubscriptions(PurchaseRequestDto purchaseRequestDto, String subscriptionFlag, Headers header) {
		CommonApiStatus result = new CommonApiStatus(null);
		String referenceNmbr= null;
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		
		if (subscriptionFlag.equalsIgnoreCase(MarketplaceConstants.NEW_AUTORENEWAL_SUBSCRIPTION.getConstant())) {
			referenceNmbr = purchaseRequestDto.getAccountNumber() + "_" + sdf.format(new Date()) + "_" + purchaseRequestDto.getSubscriptionCatalogId() + "_N" ;
			}
		else referenceNmbr = purchaseRequestDto.getAccountNumber() + "_" + sdf.format(new Date()) + "_" + purchaseRequestDto.getSubscriptionCatalogId() + "_RN" ;
		try {
			result = miscPaymentPostingTIBCO(purchaseRequestDto.getSpentAmount().toString(), "Card", referenceNmbr, purchaseRequestDto.getCardNumber(),
					purchaseRequestDto.getCardSubType(), purchaseRequestDto.getCardToken(), purchaseRequestDto.getAuthorizationCode(), purchaseRequestDto.getMembershipCode(),
					null, header.getExternalTransactionId(), subscriptionFlag, purchaseRequestDto.getEpgTransactionId(), header, 0);
		} catch (MarketplaceException e) {
			LOG.error("Misc Payment Posting For New/Renewal of Auto-renewal Subscriptions Failed " + e.getMessage());
			result.setExternalTransactionId(header.getExternalTransactionId());
			result.setStatusCode(1);
			result.setOverallStatus("Failed");
			result.setMessage(e.getMessage());
			
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), purchaseRequestDto.getAccountNumber(), header.getUserName());
			LOG.error("Exception deatils is saved in ExceptionLogs collection");
			
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	public void doCFMAsyncCall(CFMRequestDto cmfRequestDto, String externalTransactionId, String selectedPaymentItem, Headers header, String partnerCode) {
		CompletableFuture.runAsync(() -> {
			if ((MarketplaceConstants.CHANNEL_SAPP.getConstant()).equalsIgnoreCase(header.getChannelId())) {
			try {
				LOG.info("Inside AdoCFMAsyncCall partnerCode : {}", partnerCode);
				if( null != partnerCode) {
					PartnerResultResponse response = equivalentPointsDomain.validatePartnerCode(partnerCode, header.getUserName(), header.getToken());
					LOG.info("Inside AdoCFMAsyncCall partnerResultResponse : {}", response);
					if (null != response ) {
						cmfRequestDto.setPartnerName(response.getPartners().get(0).getPartnerName());
					}
					else cmfRequestDto.setPartnerName(null);
				}
				
				cfmService.cfmPosting(cmfRequestDto, externalTransactionId, selectedPaymentItem);
			} catch (MarketplaceException e) {
				LOG.error("Async Call to CFM failed with exception : {}", e.getMessage());
				exceptionLogService.saveExceptionsToExceptionLogs(e, externalTransactionId, cmfRequestDto.getAccountNumber(), "");
				LOG.error("Exception deatils is saved in ExceptionLogs collection");
			} catch (EquivalentPointsException e) {
				try {
					LOG.info("Inside AdoCFMAsyncCall: calling cfmPosting without partnerName value.");
					
					cfmService.cfmPosting(cmfRequestDto, externalTransactionId, selectedPaymentItem);
				} catch (MarketplaceException e1) {
					LOG.error("Exception occured in AdoCFMAsyncCall: calling cfmPosting without partnerName value");
					e1.printStackTrace();
				}
				LOG.error("Async Call to CFM failed with EquivalentPointsException while fetching partner deatils : {}", e.getMessage());
				exceptionLogService.saveExceptionsToExceptionLogs(e, externalTransactionId, cmfRequestDto.getAccountNumber(), "");
				LOG.error("Exception deatils is saved in ExceptionLogs collection");
				e.printStackTrace();
			}
			}
		});
	}
	
//	public MemberActivityResponse accrualForCashVoucherPurchase(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
//			OfferCatalog offerCatalog, PaymentAdditionalRequest paymentAdditionalRequest, Double spentAmount, int spentPoints) {
//		
//		List<String> eligiblePaymentMethodsForAccrual = !ObjectUtils.isEmpty(offerCatalog.getAccrualDetails())
//			&& !CollectionUtils.isEmpty(offerCatalog.getAccrualDetails().getAccrualPaymentMethods())
//			 ? offerCatalog.getAccrualDetails().getAccrualPaymentMethods().stream().map(PaymentMethod::getDescription).collect(Collectors.toList())
//			 : null;		 
//		if (!CollectionUtils.isEmpty(eligiblePaymentMethodsForAccrual) 
//				
//		&& eligiblePaymentMethodsForAccrual.contains(paymentReq.getSelectedOption())) {
//			if ((MarketplaceConstants.FULLPOINTS.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()) &&
//					offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != null && offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != 0) {
//				doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCode(), Double.valueOf(spentPoints), null);
//			} else if((MarketplaceConstants.PARTIALPOINTSCC.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()) &&
//					offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != null && offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != 0 &&
//					offerCatalog.getEarnMultiplier() != null && offerCatalog.getEarnMultiplier() != 0) {
//				doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCode(), Double.valueOf(spentPoints), "_1");
//				doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCode(), spentAmount, "_2");
//			} else if (offerCatalog.getEarnMultiplier() != null && offerCatalog.getEarnMultiplier() != 0){
//				doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCode(), spentAmount, null);	
//			}
//		} 
//		return null;
//		
//	}
	
	public MemberActivityResponse doAccrual(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			
		OfferCatalog offerCatalog, PaymentAdditionalRequest paymentAdditionalRequest, String activityCode, Double spentValue, String partialPointCCindicator) {
		MemberActivityResponse memberActivityResponse = null;
		SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		long externalTransactionNumber = (long) (Math.random()
		* ((546546546 - 346546546) + 1)) + 346546546;
		MemberActivityPaymentDto memberActivity = new MemberActivityPaymentDto();
		memberActivity.setAccountNumber(paymentReq.getAccountNumber());
		memberActivity.setActivityCode(activityCode);
		memberActivity.setEventDate(eventDateFormat.format(new Date()));
		memberActivity.setPartnerCode(offerCatalog.getPartnerCode());
		if (paymentReq.getExtTransactionId() == null) {
		if((MarketplaceConstants.PARTIALPOINTSCC.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()))
		{
		memberActivity.setExternalReferenceNumber("" + externalTransactionNumber + partialPointCCindicator);
		memberActivity.setParentTrxId("" + externalTransactionNumber);
		}
		else {
		memberActivity.setExternalReferenceNumber("" + externalTransactionNumber);
		}
		} else {
		if((MarketplaceConstants.PARTIALPOINTSCC.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()))
		{
		memberActivity.setExternalReferenceNumber(paymentReq.getExtTransactionId() + partialPointCCindicator);
		memberActivity.setParentTrxId(paymentReq.getExtTransactionId());
		}
		else {
		memberActivity.setExternalReferenceNumber(paymentReq.getExtTransactionId());
		}
		}
		memberActivity.setPointsValue((int) (double) (spentValue));
		memberActivity.setSpendValue(spentValue);
		memberActivity.setMemberResponse(memberDetails);
		memberActivity.setTemplateId("150");
		LOG.info("---------------Calling Member Activity Accrual API---------------");
		String jsonReq = new Gson().toJson(memberActivity);
		LOG.info("Accrual Request Object " + jsonReq);
		com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus response = redemptionReq(
		memberActivity, paymentAdditionalRequest.getHeader());
		if (response.getApiStatus().getStatusCode() == 0) {
		memberActivityResponse = (MemberActivityResponse) serviceHelper
		.convertToObject(response.getResult(), MemberActivityResponse.class);
		}
		return memberActivityResponse;

	}
	
	public void blacklistVouchers(List<String> voucherCodes, String reason, String uuid, Headers header) {
		VoucherCancelRequestDto voucherCancelRequestDto = new VoucherCancelRequestDto();
		LOG.info("voucherCodes issued : {}", voucherCodes);
		if (!CollectionUtils.isEmpty(voucherCodes)) {
			for (String voucherCode : voucherCodes) {
				voucherCancelRequestDto.setAction(VoucherConstants.CANCEL_ACTION);
				voucherCancelRequestDto.setActionReason(reason);
				voucherCancelRequestDto.setPurchaseId(uuid);
				voucherCancelRequestDto.setVoucherCode(voucherCode);
				LOG.info("calling cancelVoucher for voucherCode : {}, cancelVoucher request : {}", voucherCode, voucherCancelRequestDto);
				VoucherResultResponse cancelVoucherResponse = voucherController.cancelVouchers(voucherCancelRequestDto, header.getProgram(), header.getAuthorization(), header.getExternalTransactionId(),
						header.getUserName(), header.getSessionId(), header.getUserPrev(), header.getChannelId(), header.getSystemId(), header.getSystemPassword(),
						header.getToken(), header.getTransactionId());
				LOG.info("cancel volucher response : {}", cancelVoucherResponse);
			}
		}
	}
}
	
