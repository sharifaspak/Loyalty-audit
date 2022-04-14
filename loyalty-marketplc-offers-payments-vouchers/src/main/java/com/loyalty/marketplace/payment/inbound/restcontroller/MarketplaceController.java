/**
 * 
 */
package com.loyalty.marketplace.payment.inbound.restcontroller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.equivalentpoints.inbound.dto.EquivalentPointsDto;
import com.loyalty.marketplace.equivalentpoints.inbound.restcontroller.EquivalentPointsController;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ListRedemptionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionRate;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferValues;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.SubOffer;
import com.loyalty.marketplace.offers.outbound.database.entity.VoucherInfo;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.promocode.constants.codes.PromoCodeErrorCodes;
import com.loyalty.marketplace.offers.promocode.constants.codes.PromoCodeSuccessCodes;
import com.loyalty.marketplace.offers.promocode.domain.PromoCodeDomain;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PromoCodeDetails;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PromoCodeRequest;
import com.loyalty.marketplace.offers.promocode.inbound.dto.ValidatePromoCodeRequest;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoCode;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoCodeResponse;
import com.loyalty.marketplace.offers.promocode.outbound.dto.ValidPromoCodeDetails;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.constants.PaymentConstants;
import com.loyalty.marketplace.payment.constants.RequestMappingConstants;
import com.loyalty.marketplace.payment.domain.model.marketplace.SmilesPaymentDomain;
import com.loyalty.marketplace.payment.inbound.dto.PaymentAdditionalRequest;
import com.loyalty.marketplace.payment.inbound.dto.PaymentGatewayRequest;
import com.loyalty.marketplace.payment.inbound.dto.PaymentPostingTIBCODto;
import com.loyalty.marketplace.payment.inbound.dto.PaymentRequestDto;
import com.loyalty.marketplace.payment.outbound.database.repository.SmilesPaymentRepository;
import com.loyalty.marketplace.payment.outbound.database.service.BillingAdapterService;
import com.loyalty.marketplace.payment.outbound.database.service.EPGIntegrationService;
import com.loyalty.marketplace.payment.outbound.database.service.PaymentService;
import com.loyalty.marketplace.payment.outbound.dto.PaymentGatewayResponse;
import com.loyalty.marketplace.payment.outbound.dto.PaymentGatewayResponseResult;
import com.loyalty.marketplace.payment.outbound.dto.PaymentResponse;
import com.loyalty.marketplace.payment.utils.EPGResponse;
import com.loyalty.marketplace.payment.utils.GetPaymentRequest;
import com.loyalty.marketplace.payment.utils.GetPaymentResponse;
import com.loyalty.marketplace.payment.utils.Utils;
import com.loyalty.marketplace.service.SmilesAppService;
import com.loyalty.marketplace.service.dto.PaymentReversalRequetsDto;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.service.SubscriptionService;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.welcomegift.constants.WelcomeGiftCodes;
import com.loyalty.marketplace.welcomegift.outbound.dto.GiftDetails;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftDetails;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftRequestDto;
import com.loyalty.marketplace.welcomegift.service.WelcomeGiftService;

import ae.billingadapter.etisalat._1.ChargeImmediatelyResponse;
import io.swagger.annotations.Api;

@Api(value = "marketplace")
@RestController
@RequestMapping("/marketplace")
public class MarketplaceController {

	private static final Logger LOG = LoggerFactory.getLogger(MarketplaceController.class);

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SmilesPaymentRepository paymentRepository;

	@Autowired
	SmilesPaymentDomain paymentDomain;

	@Autowired
	PaymentService paymentService;

	@Autowired
	PromoCodeDomain promoCodeDomain;

	@Autowired
	WelcomeGiftService welcomeGiftService;

	@Autowired
	BillingAdapterService billingAdapterService;

	@Autowired
	PurchaseRepository purchaseRepository;

	@Autowired
	private JmsTemplate template;
	
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	SubscriptionManagementController subscriptionController;
	
	@Autowired
	EquivalentPointsController equivalentPointsController;
	
	@Autowired
	OffersHelper helper;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	EPGIntegrationService epgIntegrationService;
	
	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	SubscriptionDomain subscriptionDomain;

	@Autowired
	SmilesAppService smilesAppService;
	
	@Autowired
	RepositoryHelper repositoryHelper;

	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;

	@PostMapping(value = "/payment/paymentGateway", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public PaymentGatewayResponse paymentGateway(
			@RequestHeader(name = RequestMappingConstants.CHANNEL_ID) String channelId,
			@RequestHeader(name = RequestMappingConstants.SYSTEM_ID) String ststemId,
			@RequestHeader(name = RequestMappingConstants.SYSTEM_PASSWORD) String systemPassword,
			@RequestHeader(name = RequestMappingConstants.TOKEN) String token,
			PaymentGatewayRequest paymentGatewayRequest) {
		PaymentGatewayResponse response = new PaymentGatewayResponse();
		PaymentGatewayResponseResult result = new PaymentGatewayResponseResult();
		long transactionId = 0;
		String subOfferId = null;
		try {

			if (paymentGatewayRequest != null && !Utils.validatePaymentGatewayInfo(paymentGatewayRequest, result)) {

				if (paymentGatewayRequest.getSelectedOption() != null
						&& paymentGatewayRequest.getSelectedOption().equalsIgnoreCase("partialPointsCC")) {

					// call the reserve point bank API
					transactionId = paymentService.callReservePointsAPI();

				}
				if (paymentGatewayRequest.getSelectedPaymentItem() != null
						&& paymentGatewayRequest.getSelectedPaymentItem().equalsIgnoreCase("dealVoucher")) {
					// getOfferIdFromSubOffer for get the parent offerId
					paymentService.getParentOfferBySuboffer(paymentGatewayRequest.getOfferId());
				}

				// call smiles authentication API

				/*
				 * paymentService.setPaymentGatewayParams(paymentGatewayRequest.
				 * getSelectedPaymentItem(), paymentGatewayRequest.getAccountNumber(),
				 * paymentGatewayRequest.getDirhamValue(),
				 * paymentGatewayRequest.getPointsValue(),
				 * paymentGatewayRequest.getSelectedOption(),
				 * paymentGatewayRequest.getOfferTitle(), channelId,
				 * paymentGatewayRequest.getPromoCode(), paymentGatewayRequest.getLanguage(),
				 * paymentGatewayRequest.getVoucherDenomination(), transactionId,
				 * paymentGatewayRequest.getAtgUsername(),
				 * paymentGatewayRequest.getOfferTitle(), token,
				 * paymentGatewayRequest.getCount(), subOfferId);
				 */

				String getRandomNumber = Utils.randomAlphaNumeric(16);
				String strCustomerName = "Demo Marchent";
				String strCustomerCurrency = "AED";
				String strReturnPath = "https://localhost:8888/marketplace/payment/paymentFinalizationWS";
				if (strReturnPath != null) {
					StringBuilder strString = new StringBuilder(strReturnPath);
					strString.append("?order_id=");
					strString.append(getRandomNumber);
					strReturnPath = strString.toString();
				}
				String strTransactionHint = "";
				String bundleTransactionHint = "";
				String orderInfo = "0 PTS";
				String orderName = "ORDER";
				String terminal = null;

				if (paymentGatewayRequest.getPointsValue() != null) {
					orderInfo = paymentGatewayRequest.getPointsValue() + " PTS";
				}

				if (paymentGatewayRequest.getOfferTitle() != null
						&& paymentGatewayRequest.getOfferTitle().length() > 0) {
					orderName = paymentGatewayRequest.getOfferTitle();
					if (orderName.length() > 25) {
						orderName = orderName.substring(0, 24);
					}
				}

				Long totalAmount = Math
						.round(paymentGatewayRequest.getDirhamValue() + (paymentGatewayRequest.getPointsValue()
								* Utils.pointsToAEDRate(paymentGatewayRequest.getPointsValue().intValue())));
				// call account api for get the customertype
				String custTypeDesc = "";
				if (paymentGatewayRequest.getSelectedPaymentItem() != null
						&& paymentGatewayRequest.getSelectedPaymentItem().equals(PaymentConstants.CASH_VOUCHER)) {
					if (custTypeDesc.equalsIgnoreCase(PaymentConstants.ETISALAT_VISITOR))
						terminal = PaymentConstants.CV_VISITOR;
					else if (custTypeDesc.equalsIgnoreCase(PaymentConstants.DU))
						terminal = PaymentConstants.CV_DU;
					else if (custTypeDesc.equalsIgnoreCase(PaymentConstants.INTERNATIONAL))
						terminal = PaymentConstants.INTERNATIONAL;
					else
						terminal = PaymentConstants.CV_ETISALAT;
				} else if (paymentGatewayRequest.getSelectedPaymentItem() != null
						&& paymentGatewayRequest.getSelectedPaymentItem().equals(PaymentConstants.COUPON)) {
					if (custTypeDesc.equalsIgnoreCase(PaymentConstants.ETISALAT_VISITOR))
						terminal = PaymentConstants.DC_VISITOR;
					else if (custTypeDesc.equalsIgnoreCase(PaymentConstants.DU))
						terminal = PaymentConstants.DC_DU;
					else if (custTypeDesc.equalsIgnoreCase(PaymentConstants.INTERNATIONAL))
						terminal = PaymentConstants.INTERNATIONAL;
					else
						terminal = PaymentConstants.DC_ETISALAT;
				} else if (paymentGatewayRequest.getSelectedPaymentItem() != null
						&& paymentGatewayRequest.getSelectedPaymentItem().equals(PaymentConstants.DEAL_VOUCHER)) {
					if (custTypeDesc.equalsIgnoreCase(PaymentConstants.ETISALAT_VISITOR))
						terminal = PaymentConstants.DV_VISITOR;
					else if (custTypeDesc.equalsIgnoreCase(PaymentConstants.DU))
						terminal = PaymentConstants.DV_DU;
					else if (custTypeDesc.equalsIgnoreCase(PaymentConstants.INTERNATIONAL))
						terminal = PaymentConstants.INTERNATIONAL;
					else
						terminal = PaymentConstants.DV_ETISALAT;
				} else if (paymentGatewayRequest.getSelectedPaymentItem() != null
						&& paymentGatewayRequest.getSelectedPaymentItem().equals(PaymentConstants.BUNDLES)) {
					if (custTypeDesc.equalsIgnoreCase(PaymentConstants.ETISALAT_VISITOR))
						terminal = PaymentConstants.BUNDLE_VISITOR;
					else
						terminal = PaymentConstants.BUNDLE_ETISALAT;

					GetPaymentRequest getPaymentRequest = Utils.setPaymentRequest(strCustomerName,
							paymentGatewayRequest.getLanguage(), paymentGatewayRequest.getDirhamValue(),
							strCustomerCurrency, getRandomNumber, orderInfo, orderName, strReturnPath, terminal,
							totalAmount, paymentGatewayRequest.getAccountNumber(),
							paymentGatewayRequest.getSelectedPaymentItem(), strTransactionHint);
					paymentService.paymentRegistration(getPaymentRequest);
				}
			} else {
				LOG.info("[paymentGateway] -REST , input validation failed");
				/*
				 * result.setResponseCode(001);
				 * result.setResponseDescription("input validation failed");
				 */
				response.setResult(result);
			}

			/*
			 * String getRandomNumber = Utils.randomAlphaNumeric(16); SmilesPaymentDomain
			 * paymentDomain = new SmilesPaymentDomain.SmilesPaymentBuilder(getRandomNumber,
			 * "122334", "12234", "2", "en", new Double(20), new Double(25), "dealVoucher",
			 * "fullcreditCard", "12345", 75, "12345678890", "20/2022", "123", 12, "GET50",
			 * "234", "etisaluser", "visa", "999", "token", "333", "count", new Date(),
			 * "etisaluser", "etisaluser", new Date(), paymentRepository).build();
			 * 
			 * paymentDomain.saveSmilesPayment(paymentDomain);
			 */

		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			LOG.info("[paymentGateway] -REST , Exception occure");
			result.setResponseCode(001);
			result.setResponseDescription("Error while processing your request");
			response.setResult(result);
		}

		return response;
	}

	/*
	 * @GetMapping(value = "/tibcoCall/adjustmentPosting") public
	 * AdjustmentPostingResponse callAdjustmentPostingTibco() { try {
	 * LOG.info("Tibco Adjustment called..............."); return
	 * paymentService.adjustmentPostingTIBCO("0507158521", 699282289, 2.0); } catch
	 * (PaymentFault e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * catch (MarketplaceException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return null; }
	 */
	@PostMapping(value = "/tibcoCall/paymentPosting")
	public ResultResponse callTibco(@RequestBody PaymentPostingTIBCODto req,
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
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		try {
			LOG.info("Tibco Payment Posting called...............");
			LOG.info("Payment Posting DTO :" + req.toString());
			Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
					channelId, systemId, systemPassword, token, transactionId);
			//double calculatedValue = conversionAmount(req.getAccountNmbr(),req.getLoyaltyPoints(),channelId,headers);
			String accountNumber = req.getAccountNmbr(); 
			String points = req.getLoyaltyPoints(); 
			double calculatedValue = 0.00;
			EquivalentPointsDto eqPointDto = new EquivalentPointsDto();
			eqPointDto.setAccountNumber(accountNumber);
			eqPointDto.setPoint(Double.valueOf(points));
			eqPointDto.setOperationType("redeeming");
			eqPointDto.setOfferType("billRecharge");
			eqPointDto.setPartnerCode("ES");
			LOG.info("---Calling EquivalentPoint point----");
			ResponseEntity<com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus> response1 = equivalentPointsController
					.equivalentPoints(eqPointDto, header.getUserName(), header.getSessionId(),
							header.getUserPrev(), header.getChannelId(), header.getSystemId(),
							header.getSystemPassword(), header.getToken(), header.getExternalTransactionId(),
							header.getProgram());
			com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus commonApiStatus = response1
					.getBody();
			List<ListRedemptionRate> listRedemptionRate = null;

			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
				listRedemptionRate = (List<ListRedemptionRate>) commonApiStatus.getResult();
			}
			RedemptionRate redemptionRate = new RedemptionRate();
			double rate = 0.00;
			if (listRedemptionRate != null && !listRedemptionRate.isEmpty()) {
				redemptionRate = listRedemptionRate.get(0).getRedemptionCalculatedValue();
			}
			if (redemptionRate != null) {
				rate = redemptionRate.getRate();
				calculatedValue = redemptionRate.getEquivalentPoint();
			}
			LOG.info("Conversion Rate from equivalentpoint : " + rate);
			CommonApiStatus response =  paymentService.paymentPostingTibco(String.valueOf(calculatedValue), req.getPaymentType(),
					req.getAccountId(), req.getAccountNmbr(), req.getTransactionType(), req.getReferenceNmbr(),
					req.getCardNmbr(), req.getCardToken(), req.getCardExpiryDt(), req.getAuthorizationCode(),
					String.valueOf(calculatedValue), req.getEpgTransactionId(), req.getCardSubType(), req.getMembershipCode(),
					req.getLoyaltyPoints(), externalTransactionId, rate, header, 0);
			if(response.getStatusCode() == 0) {
				resultResponse.setResult("5401","Sussessfully Called TIBCO");	
			} else {
				resultResponse.addErrorAPIResponse(5402,"TIBCO Call Failed");
			}
			
		} catch (MarketplaceException e) {
			// TODO Auto-generated catch block
			resultResponse.addErrorAPIResponse(5402,"TIBCO Call Failed");
		}
		return resultResponse;
	}
	
	private double conversionAmount(String accountNumber, String points, String channelId, Headers header) {
			double calculatedValue = 0.00;
			EquivalentPointsDto eqPointDto = new EquivalentPointsDto();
			eqPointDto.setAccountNumber(accountNumber);
			eqPointDto.setPoint(Double.valueOf(points));
			eqPointDto.setOperationType("redeeming");
			eqPointDto.setOfferType("billRecharge");
			eqPointDto.setPartnerCode("ES");
			LOG.info("---Calling EquivalentPoint point----");
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
			double rate = 0.00;
			if (listRedemptionRate != null && !listRedemptionRate.isEmpty()) {
				redemptionRate = listRedemptionRate.get(0).getRedemptionCalculatedValue();
			}
			if (redemptionRate != null) {
				rate = redemptionRate.getRate();
				calculatedValue = redemptionRate.getEquivalentPoint();
			}
			LOG.info("Conversion Rate from equivalentpoint : " + rate);
		return calculatedValue;
	}

	@GetMapping(value = "/tibcoCall/miscPosting")
	public CommonApiStatus callMiscPostingTibco() {
		Headers header = new Headers();
		header.setChannelId(MarketplaceConstants.PAYMENTCODESAPP.getConstant());
		try {
			LOG.info("Tibco Misc Payment Posting called...............");
			return paymentService.miscPaymentPostingTIBCO("100.0", "Card", "966117910137660188721846",
					"457269******6444", "Visa", "4572695672826444", "273564", null, null, null, null, null, header, 0);
		} catch (MarketplaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(value = "/billingAdapter")
	public ChargeImmediatelyResponse callBillingAdapter() {
		try {
			LOG.info("Billing Adapter called...............");
			return billingAdapterService.billingAdapterCall("0529686753", 100, null);
		} catch (MarketplaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	@PostMapping(value = "/paymentAndProvision", consumes = MediaType.APPLICATION_JSON_VALUE)
	public PaymentResponse payment(@Valid @RequestBody PaymentRequestDto paymentRequest) {
		PurchaseRequestDto paymentReq = new PurchaseRequestDto();
		paymentReq.setAccountNumber(paymentRequest.getAccountNumber());
		paymentReq.setMembershipCode(paymentRequest.getMembershipCode());
		paymentReq.setAuthorizationCode(paymentRequest.getAuthorizationCode());
		paymentReq.setSpentPoints(paymentRequest.getPointsValue());
		paymentReq.setSpentAmount(paymentRequest.getCost());
		paymentReq.setAtgUserName(paymentRequest.getAtgUsername());
		paymentReq.setCardNumber(paymentRequest.getCardNumber());
		paymentReq.setCardType(paymentRequest.getCardType());
		paymentReq.setCardToken(paymentRequest.getCardToken());
		paymentReq.setCardSubType(paymentRequest.getCardSubType());
		paymentReq.setCardExpiryDate(paymentRequest.getCardExpiryDate());
		paymentReq.setExtTransactionId(paymentRequest.getExtTransactionId());
		paymentReq.setEpgTransactionId(paymentRequest.getEpgTransactionId());
		paymentReq.setUiLanguage(paymentRequest.getLanguage());
		paymentReq.setOfferId(paymentRequest.getOfferId());
		paymentReq.setPaymentType(paymentRequest.getPaymentType());
		paymentReq.setSelectedOption(paymentRequest.getPaymentMethod());
		paymentReq.setSelectedPaymentItem(paymentRequest.getPaymentItem());
		paymentReq.setCouponQuantity(paymentRequest.getCouponQuantity());
		OfferCatalog offerCatalog = new OfferCatalog();
		OfferType offerType = new OfferType();
		offerType.setOfferTypeId(paymentRequest.getPaymentItem());
		offerCatalog.setOfferType(offerType);
		Merchant merchant = new Merchant();
		MerchantName merchantName = new MerchantName();
		merchantName.setMerchantNameEn(paymentRequest.getMerchantName());
		merchant.setMerchantName(merchantName);
		merchant.setMerchantCode(paymentRequest.getMerchantCode());
		Barcode barCode = new Barcode();
		barCode.setId(paymentRequest.getBarcodeId());
		merchant.setBarcodeType(barCode);
		SubOffer subOffer = new SubOffer();
		subOffer.setSubOfferId(paymentRequest.getSubOfferId());
		List<SubOffer> subOfferList = new ArrayList<SubOffer>();
		subOfferList.add(subOffer);
		Denomination denomination = new Denomination();
		denomination.setDenominationId(paymentRequest.getDenominationId());
		List<Denomination> denominationList = new ArrayList<Denomination>();
		denominationList.add(denomination);
		OfferValues offerValues = new OfferValues();
		offerValues.setCost(paymentRequest.getCost());
		offerCatalog.setOfferValues(offerValues);
		offerCatalog.setMerchant(merchant);
		offerCatalog.setPartnerCode(paymentRequest.getPartnerCode());
		offerCatalog.setSubOffer(subOfferList);
		offerCatalog.setDenominations(denominationList);
		offerCatalog.setOfferId(paymentRequest.getOfferId());
		VoucherInfo voucherInfo = new VoucherInfo();
		voucherInfo.setVoucherAction(paymentRequest.getVoucherAction());
		voucherInfo.setVoucherExpiryPeriod(paymentRequest.getVoucherExpiryPeriod());
		offerCatalog.setVoucherInfo(voucherInfo);
		PaymentAdditionalRequest addtnReq = new PaymentAdditionalRequest();
		addtnReq.setActivityCode(paymentRequest.getActivityCode());
		addtnReq.setChannelId(paymentRequest.getChannelId());
		addtnReq.setPaymentRequired(true);
		addtnReq.setUuid(paymentRequest.getUuid());
		return paymentService.paymentAndProvisioning(paymentReq, null, offerCatalog, null, addtnReq);

	}

	/*
	 * @PostMapping(value = "/payment", consumes = MediaType.ALL_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public CreatePaymentResponse
	 * createPayment(@RequestHeader(value = RequestMappingConstants.PROGRAM,
	 * required = false) String program,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required =
	 * false) String authorization,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID,
	 * required = false) String externalTransactionId,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.USER_NAME, required = false)
	 * String userName,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false)
	 * String sessionId,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.USER_PREV, required = false)
	 * String userPrev,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = true)
	 * String channelId,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false)
	 * String systemId,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required =
	 * false) String systemPassword,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.TOKEN, required = false)
	 * String token,
	 * 
	 * @RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required =
	 * false) String transactionId,
	 * 
	 * @Valid CreatePaymentRequest createPaymentRequest) { CreatePaymentResponse
	 * response = new CreatePaymentResponse(); try { CreatePaymentRequestObj
	 * requestObj = createPaymentRequest.getCreatePaymentInfo(); if
	 * (!Utils.validvalidatePaymentInfo(createPaymentRequest)) { //
	 * requestObj.getSelectedOption() -- fullpoints/fullcreditcard //
	 * requestObj.getSelectedPaymentItem()--- cashVocher/dealVocher if
	 * (requestObj.getPointsValue() != null &&
	 * requestObj.getPointsValue().intValue() < 0) {
	 * LOG.info("[createPayment] -REST , Negative pointsValue,{}",
	 * requestObj.getPointsValue().intValue());
	 * response.getResult().setPaymentStatus(-2);
	 * response.getResult().setTransactionNo(null); return response; } // if payitem
	 * is dealVoucher get the offerId from subOffer
	 * 
	 * // smiles authentication check // we need to call smiles authentication API
	 * boolean checkAuthentication = false; if (checkAuthentication) {
	 * LOG.debug("Login Validation Failure");
	 * response.getResult().setPaymentStatus(-2);
	 * response.getResult().setTransactionNo(null); return response; }
	 * 
	 * // check cutomer eligibilty String isEligibleToRedeem = null;
	 * 
	 * if (isEligibleToRedeem == null) {
	 * 
	 * }
	 * 
	 * // check system authentication by calling API
	 * 
	 * if (checkAuthentication) { LOG.debug("system authentication Failure");
	 * response.getResult().setPaymentStatus(-1);
	 * response.getResult().setTransactionNo(null); return response; }
	 * 
	 * if (requestObj.getAccountNumber() != null &&
	 * !"SWEB".equalsIgnoreCase(channelId)) {
	 * 
	 * // call the account API to validate the memship by membership code or //
	 * accountNumber
	 * 
	 * } else if (requestObj.getAtgUsername() != null &&
	 * "SWEB".equalsIgnoreCase(channelId)) { // call the account API to validate the
	 * memship by membership code or // atgusername }
	 * 
	 * // check account is valid or not like influencer key
	 * 
	 * // check checkValidRedemptionAmountsForDealVouchers or //
	 * checkValidRedemptionAmounts // rerurns the validRedemption value boolean
	 * validRedemption = false; if (validRedemption &&
	 * "dealVoucher".equals(requestObj.getSelectedPaymentItem())) { // get
	 * parentofferId using subofferId }
	 * 
	 * // call the offer API to get the offer details
	 * 
	 * if ( offer != null && "eService".equals(requestObj.getSelectedPaymentItem()))
	 * { // get the event type String eventType = "test"; if (eventType == null) {
	 * LOG.info("Product is unavailable now");
	 * response.getResult().setPaymentStatus(Integer.parseInt(MarketplaceCode.
	 * ERROR_STATUS.getId())); response.getResult().setTransactionNo(null); return
	 * response; } else if (eventType != null && requestObj.getSelectedOption()
	 * .equals("fullPoints") && (pointsAmount > availablePoints) ) {
	 * LOG.info("Not Enough Points");
	 * response.getResult().setPaymentStatus(Integer.parseInt(MarketplaceCode.
	 * ERROR_STATUS.getId())); response.getResult().setTransactionNo(null); return
	 * response; } else if (eventType != null &&
	 * ((requestObj.getSelectedOption().equals("fullPoints") && (pointsAmount <=
	 * availablePoints) ) || requestObj.getSelectedOption().equals("addToBill") ||
	 * requestObj.getSelectedOption().equals("deductFromBalance") ||
	 * requestObj.getSelectedOption().equals("fullCreditCard"))) { String
	 * provisioningChannel = "CBCM";// CBCM/EMCAIS/COMS/RTF/RBT if
	 * (provisioningChannel.contentEquals("CBCM")) {
	 * 
	 * } }
	 * 
	 * } else if ( offer != null &&
	 * ("billMsisdn".equals(requestObj.getSelectedPaymentItem())) ||
	 * ("rechargeMsisdn".equals(requestObj.getSelectedPaymentItem()))) { // get the
	 * event type
	 * 
	 * } else if ( offer != null &&
	 * ("coupon".equals(requestObj.getSelectedPaymentItem()) ||
	 * "voucher".equals(requestObj.getSelectedPaymentItem()) ||
	 * "dealVoucher".equals(requestObj.getSelectedPaymentItem()))) { // get the
	 * event type
	 * 
	 * } else if (true offer == null ) {
	 * LOG.info("System Error. Please Try Again Later"); CreatePaymentResponseResult
	 * result = new CreatePaymentResponseResult(); result.setPaymentStatus(1000);
	 * result.setTransactionNo(null); response.setResult(result); return response; }
	 * 
	 * } else { LOG.info("[createPayment] -REST , Input validation is failed");
	 * response.getResult().setPaymentStatus(Integer.parseInt(MarketplaceCode.
	 * ERROR_STATUS.getId())); response.getResult().setTransactionNo(null); return
	 * response; } } catch (Exception e) {
	 * LOG.error(ExceptionUtils.getStackTrace(e));
	 * LOG.info("[createPayment] -REST , Exception occure");
	 * response.getResult().setPaymentStatus(Integer.parseInt(MarketplaceCode.
	 * ERROR_STATUS.getId())); response.getResult().setTransactionNo(null); return
	 * response; } return response; }
	 * 
	 * @PostMapping(value = "/payment/paymentFinalization", consumes =
	 * MediaType.APPLICATION_FORM_URLENCODED_VALUE) public @ResponseBody
	 * PaymentFinalizationResponse paymentFinalization(
	 * 
	 * @RequestParam(name = "orderId", required = true) String orderId,
	 * PaymentFinalizationRequest paymentFinalizationRequest) {
	 * 
	 * return new PaymentFinalizationResponse(); }
	 */

	/*
	 * @PostMapping(value = "/redemption", consumes =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<String>
	 * redemptionReq() {
	 * 
	 * MemberActivityDto memberActivity = new MemberActivityDto();
	 * memberActivity.setLoyaltyId(""); memberActivity.setMembershipCode("0");
	 * memberActivity.setAccountNumber("35354545");
	 * memberActivity.setActivityCode("rr3"); memberActivity.setEventDate(new
	 * Date()); memberActivity.setPartnerCode("Smiles_P0001119");
	 * memberActivity.setExternalReferenceNumber("11211223453237");
	 * memberActivity.setSpendValue(120.0);
	 * memberActivity.setRedemptionType("Reserve");
	 * 
	 * return paymentService.redemptionReq(memberActivity); }
	 */

	@PostMapping(value = "promotion/promocode", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse createPromoCode(@RequestBody PromoCodeRequest promoCodeRequest,
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
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		resultResponse = promoCodeDomain.createPromoCode(promoCodeRequest, resultResponse, program, userName);
		return resultResponse;
	}
	
	@PostMapping(value = "/promocode/file")
	public ResultResponse createBulkPromoCode(@RequestParam("file") MultipartFile file,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) throws IOException {
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		if (file != null && !file.isEmpty() && file.getContentType() != null) 
		{
			LOG.info("Before calling promocodedomain....");
			resultResponse=promoCodeDomain.createPromoCodeInBulk(file, resultResponse, program, userName, headers);
			LOG.info("result.....");
		}
		return resultResponse;
		
	
	}
	

	@PostMapping(value = "promotion/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ValidPromoCodeDetails validatePromoCode(@RequestBody ValidatePromoCodeRequest promoCodeRequest,
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
		ValidPromoCodeDetails resultResponse = new ValidPromoCodeDetails(externalTransactionId);
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		String offerId = null;
		String subscriptionId = null;
		String accountNumber = null;
		int denomination = 0;
		if(promoCodeRequest.getOfferId() != null) {
			offerId = promoCodeRequest.getOfferId();
		}
		if(promoCodeRequest.getSubscriptionCatalogId() != null) {
			subscriptionId = promoCodeRequest.getSubscriptionCatalogId();
		}
		if(promoCodeRequest.getAccountNumber() != null && !promoCodeRequest.getAccountNumber().isEmpty()) {
			accountNumber = promoCodeRequest.getAccountNumber().get(0);
		}
		if (promoCodeRequest.getPromoDenominationAmount() > 0) {
			denomination = promoCodeRequest.getPromoDenominationAmount();
		}
		resultResponse = promoCodeDomain.validatePromoCode(promoCodeRequest.getPromoCodeDetails().getPromoCode(),
				resultResponse, offerId, promoCodeRequest.getOfferType(),
				subscriptionId, accountNumber,denomination,
				header, null);
		return resultResponse;
	}
	@PostMapping(value = "/welcomeGift", consumes = MediaType.APPLICATION_JSON_VALUE)
	public GiftDetails generateWelcomeGift(@RequestBody WelcomeGiftRequestDto welcomeGiftRequest,
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
		
		GiftDetails giftDetails = new GiftDetails(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		WelcomeGiftDetails welcomeGiftDetails = new WelcomeGiftDetails();
		WelcomeGiftDetails welcomeGiftDetails1 = new WelcomeGiftDetails();
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		List<WelcomeGiftDetails> welcomeGiftDetailsList = new ArrayList<WelcomeGiftDetails>();
		String accountNumber = welcomeGiftRequest.getAccountNumber();
		String customerType = welcomeGiftRequest.getCustomerType();
		String giftType = null;
			giftType = welcomeGiftRequest.getGiftType();
			LOG.info("MarketplaceController giftType: "+ giftType);
		if (giftType != null && giftType.equalsIgnoreCase("Points")) {
			LOG.info("Inside Points GiftType");
			giftDetails.setResult(WelcomeGiftCodes.EARNED_POINTS.getId(), WelcomeGiftCodes.EARNED_POINTS.getMsg());
			welcomeGiftDetails.setAccountNumber(accountNumber);
			welcomeGiftDetails.setGiftType("Points");
			welcomeGiftDetails.setGiftValue("500");
			welcomeGiftDetailsList.add(welcomeGiftDetails);
			giftDetails.setWelcomeGiftDetails(welcomeGiftDetailsList);
			LOG.info("Points giftDetails:{} ", giftDetails);
			return giftDetails;
		}
		if (giftType != null && giftType.equalsIgnoreCase("Promocode")) {
			LOG.info("Inside Promocode GiftType");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 30);
			List<String> accountList = new ArrayList<String>();
			accountList.add(accountNumber);
			Date endDate = c.getTime();
			String startDateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String endDateStr = new SimpleDateFormat("dd-MM-yyyy").format(endDate);
			PromoCodeRequest promoCodeRequest = new PromoCodeRequest();

			List<String> accountno=new ArrayList<String>();
			accountno.add(accountNumber);
			promoCodeRequest.setAccountNumber(accountno);

			promoCodeRequest.setAccountNumber(accountList);

			promoCodeRequest.setOfferType("1");
			promoCodeRequest.setRuleId("1");
			PromoCodeDetails promoCodeDetails = new PromoCodeDetails();
			promoCodeDetails.setPromoCode("PromoCode" + Math.abs(new Random().nextInt()));
			promoCodeDetails.setPromoCodeDescription("Welcome Gift");
			promoCodeDetails.setPromoCodeDuration(30);
			promoCodeDetails.setPromoCodeLevel("Instance");
			promoCodeDetails.setPromoCodeTotalCount(1);
			promoCodeDetails.setPromoCodeType("Free");
			promoCodeDetails.setPromoUserRedeemCountLimit(1);
			promoCodeDetails.setStartDate(startDateStr);
			promoCodeDetails.setEndDate(endDateStr);
			promoCodeDetails.setValue(0);
			promoCodeRequest.setPromoCodeDetails(promoCodeDetails);
			PromoCode promoCode = promoCodeDomain.createPromoCodeWelcomeGift(promoCodeRequest, giftDetails, program,
					userName);
			giftDetails.setResult(PromoCodeSuccessCodes.PROMOCODE_CREATED_SUCCESSFULLY.getId(),
					PromoCodeSuccessCodes.PROMOCODE_CREATED_SUCCESSFULLY.getMsg());
			welcomeGiftDetails.setAccountNumber(accountNumber);
			welcomeGiftDetails.setGiftType("PromoCode");
			welcomeGiftDetails.setGiftValue(promoCode.getPromoCode());
			welcomeGiftDetails.setStartDate(startDateStr);
			welcomeGiftDetails.setEndDate(endDateStr);
			welcomeGiftDetailsList.add(welcomeGiftDetails);
			giftDetails.setWelcomeGiftDetails(welcomeGiftDetailsList);
			LOG.info("Promocode giftDetails:{} ", giftDetails);
			return giftDetails;
		}
		
		if (giftType != null && giftType.equalsIgnoreCase("Subscription")) {
			
			LOG.info("Inside Subscription GiftType");
			String startDateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
			purchaseRequestDto.setAccountNumber(accountNumber);
			purchaseRequestDto.setMembershipCode(welcomeGiftRequest.getMembershipCode());
			purchaseRequestDto.setSelectedOption(MarketplaceConstants.FULLPOINTS.getConstant());
			purchaseRequestDto.setSelectedPaymentItem("subscription");
			purchaseRequestDto.setSubscriptionMethod(SubscriptionManagementConstants.WELCOME_GIFT.get());
			
			PurchaseResultResponse purchaseResultResponse = subscriptionController.createWelcomeGiftSubscription(purchaseRequestDto, welcomeGiftRequest, headers);
			if(purchaseResultResponse.getApiStatus().getOverallStatus().equalsIgnoreCase("Success") && null != purchaseResultResponse.getResult().getDescription()) {
				welcomeGiftDetails1.setAccountNumber(accountNumber);
				welcomeGiftDetails1.setGiftType("Subscription");
				welcomeGiftDetails1.setGiftValue(purchaseResultResponse.getResult().getDescription());
				welcomeGiftDetailsList.add(welcomeGiftDetails);
				giftDetails.setWelcomeGiftDetails(welcomeGiftDetailsList);
			} else {
				giftDetails.addErrorAPIResponse(purchaseResultResponse.getApiStatus().getErrors().get(0).getCode(),
						purchaseResultResponse.getApiStatus().getErrors().get(0).getMessage());
			}
			LOG.info("Subscription giftDetails:{} ", giftDetails);
			return giftDetails;
		}
		if(giftType != null && giftType.equalsIgnoreCase("voucherAndSubscription")) {
			LOG.info("Inside voucherAndSubscription GiftType");
			boolean voucherFlag = false;
			boolean subscriptionFlag = false;
			headers.setChannelId(welcomeGiftRequest.getChannelId());
			PurchaseRequestDto purchaseRequest  = new PurchaseRequestDto();
			purchaseRequest.setSelectedPaymentItem(MarketplaceConstants.DISCOUNTVOUCHER.getConstant());
			purchaseRequest.setSelectedOption(MarketplaceConstants.FULLPOINTS.getConstant());
			purchaseRequest.setSpentAmount(0d);
			purchaseRequest.setSpentPoints(0);
			purchaseRequest.setAccountNumber(welcomeGiftRequest.getAccountNumber());
			purchaseRequest.setMembershipCode(welcomeGiftRequest.getMembershipCode());
			purchaseRequest.setCouponQuantity(1);
			String voucherId = null;
			try {
				voucherId = helper.validateAndGiftOffer(purchaseRequest, headers, welcomeGiftRequest.getCardType(), giftDetails);
			} catch (MarketplaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(voucherId != null) {
				Voucher voucher = voucherRepository.findByVoucherCode(voucherId);
				giftDetails.setResult(WelcomeGiftCodes.VOUCHER_WELCOME_GIFT.getId(),
						WelcomeGiftCodes.VOUCHER_WELCOME_GIFT.getMsg());
				welcomeGiftDetails.setAccountNumber(accountNumber);
				welcomeGiftDetails.setGiftType("Voucher");
				welcomeGiftDetails.setGiftValue(voucherId);
				welcomeGiftDetails.setOfferId(voucher.getOfferInfo().getOffer());
				welcomeGiftDetailsList.add(welcomeGiftDetails);
				LOG.info("setting voucher");
				voucherFlag = true;
				}
			LOG.info("logging WCGiftDetails after vouhcer :{}", welcomeGiftDetailsList);
			String startDateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto();
			purchaseRequestDto.setAccountNumber(accountNumber);
			purchaseRequestDto.setMembershipCode(welcomeGiftRequest.getMembershipCode());
			purchaseRequestDto.setSelectedOption(MarketplaceConstants.FULLPOINTS.getConstant());
			purchaseRequestDto.setSelectedPaymentItem("subscription");
			purchaseRequestDto.setSubscriptionMethod(SubscriptionManagementConstants.WELCOME_GIFT.get());
			purchaseRequestDto.setCardType(welcomeGiftRequest.getCardType());
			PurchaseResultResponse purchaseResultResponse = subscriptionController.createWelcomeGiftSubscription(purchaseRequestDto, welcomeGiftRequest, headers);
			if(purchaseResultResponse.getApiStatus().getOverallStatus().equalsIgnoreCase("Success") && null != purchaseResultResponse.getResult().getDescription()) {
				
				try {
					Optional<Subscription> subscription =  subscriptionDomain.findById(purchaseResultResponse.getResult().getDescription());
					welcomeGiftDetails1.setSubscriptionCatalogId(subscription.get().getSubscriptionCatalogId());
				} catch (SubscriptionManagementException e) {
					e.printStackTrace();
				}
				welcomeGiftDetails1.setAccountNumber(accountNumber);
				welcomeGiftDetails1.setGiftType("Subscription");
				welcomeGiftDetails1.setGiftValue(purchaseResultResponse.getResult().getDescription());
				welcomeGiftDetailsList.add(welcomeGiftDetails1);
				LOG.info("setting subscription");
				subscriptionFlag = true;
				giftDetails.setResult(WelcomeGiftCodes.SUBSCRIPTION_WELCOME_GIT.getId(),
						WelcomeGiftCodes.SUBSCRIPTION_WELCOME_GIT.getMsg());
			} else {
				giftDetails.addErrorAPIResponse(purchaseResultResponse.getApiStatus().getErrors().get(0).getCode(),
						purchaseResultResponse.getApiStatus().getErrors().get(0).getMessage());
			}
			LOG.info("logging WCGiftDetails after subscription :{}", welcomeGiftDetailsList);
			LOG.info("voucher and subscriptionFlags: {}, {}", voucherFlag, subscriptionFlag);
			if (voucherFlag && subscriptionFlag) {
				LOG.info("both voucher and subscription are given");
				giftDetails.setResult(WelcomeGiftCodes.VOUCHER_SUBSCRIPTION_WELCOME_GIFT.getId(),
						WelcomeGiftCodes.VOUCHER_SUBSCRIPTION_WELCOME_GIFT.getMsg());
			}
			if (voucherFlag || subscriptionFlag) {
				LOG.info("setting success");
				giftDetails.setSuccessAPIResponse();
			} else {
				giftDetails.setResult(WelcomeGiftCodes.WELCOME_GIFT_NOT_PROVIDED.getId(),
						WelcomeGiftCodes.WELCOME_GIFT_NOT_PROVIDED.getMsg());
			}
			giftDetails.setWelcomeGiftDetails(welcomeGiftDetailsList);
			LOG.info("voucherAndSubscription giftDetails:{} ", giftDetails);
			return giftDetails;
		}
		
		if(giftType != null && giftType.equalsIgnoreCase("Voucher")) {
			LOG.info("Inside Voucher GiftType");
			headers.setChannelId(welcomeGiftRequest.getChannelId());
			PurchaseRequestDto purchaseRequest  = new PurchaseRequestDto();
			purchaseRequest.setSelectedPaymentItem(MarketplaceConstants.DISCOUNTVOUCHER.getConstant());
			purchaseRequest.setSelectedOption(MarketplaceConstants.FULLPOINTS.getConstant());
			purchaseRequest.setSpentAmount(0d);
			purchaseRequest.setSpentPoints(0);
			purchaseRequest.setAccountNumber(welcomeGiftRequest.getAccountNumber());
			purchaseRequest.setMembershipCode(welcomeGiftRequest.getMembershipCode());
			purchaseRequest.setCouponQuantity(1);
			LOG.info("purchaseRequest:{} ", purchaseRequest.toString());
			String voucherId = null;
			try {
				voucherId = helper.validateAndGiftOffer(purchaseRequest, headers, welcomeGiftRequest, giftDetails);
			} catch (MarketplaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(voucherId != null) {
				Voucher voucher = voucherRepository.findByVoucherCode(voucherId);
				String endDate =  new SimpleDateFormat("dd-MM-yyyy").format(voucher.getExpiryDate());
				LOG.info("end date of voucher:{} ", endDate);
				String offerTitleEn = null;
				String offerTitleAr = null;
				OfferCatalog giftOffer = offerRepository.findByOfferIdAndIsGift(welcomeGiftRequest.getGiftId() ,OfferConstants.FLAG_SET.get());
				if (!ObjectUtils.isEmpty(giftOffer.getOffer())) {
					offerTitleEn = giftOffer.getOffer().getOfferTitleDescription().getOfferTitleDescriptionEn();
					offerTitleAr = giftOffer.getOffer().getOfferTitleDescription().getOfferTitleDescriptionAr();
				}
				giftDetails.setResult(WelcomeGiftCodes.VOUCHER_WELCOME_GIFT.getId(),
						WelcomeGiftCodes.VOUCHER_WELCOME_GIFT.getMsg());
				welcomeGiftDetails.setAccountNumber(accountNumber);
				welcomeGiftDetails.setGiftType("Voucher");
				welcomeGiftDetails.setGiftValue(voucherId);
				welcomeGiftDetails.setEndDate(endDate);
				welcomeGiftDetails.setOfferTitleEn(offerTitleEn);
				welcomeGiftDetails.setOfferTitleAr(offerTitleAr);
				welcomeGiftDetailsList.add(welcomeGiftDetails);
				giftDetails.setWelcomeGiftDetails(welcomeGiftDetailsList);
				}
			LOG.info("Voucher giftDetails:{} ", giftDetails);
			return giftDetails;
		}
		
		if(giftType != null && giftType.equalsIgnoreCase("subscriptionPromo")) {
			LOG.info("Inside subscriptionPromo GiftType");
			headers.setChannelId(welcomeGiftRequest.getChannelId());
			PurchaseRequestDto purchaseRequest  = new PurchaseRequestDto();
			purchaseRequest.setSelectedPaymentItem(MarketplaceConstants.DISCOUNTVOUCHER.getConstant());
			purchaseRequest.setSelectedOption(MarketplaceConstants.FULLPOINTS.getConstant());
			purchaseRequest.setSpentAmount(0d);
			purchaseRequest.setSpentPoints(0);
			purchaseRequest.setAccountNumber(welcomeGiftRequest.getAccountNumber());
			purchaseRequest.setMembershipCode(welcomeGiftRequest.getMembershipCode());
			purchaseRequest.setCouponQuantity(1);
			LOG.info("purchaseRequest:{} ", purchaseRequest.toString());
			String voucherId = null;
			try {
				voucherId = helper.validateAndGiftOffer(purchaseRequest, headers, welcomeGiftRequest, giftDetails);
			} catch (MarketplaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(voucherId != null) {
				Voucher voucher = voucherRepository.findByVoucherCode(voucherId);
				String endDate =  new SimpleDateFormat("dd-MM-yyyy").format(voucher.getExpiryDate());
				String startDate =  new SimpleDateFormat("dd-MM-yyyy").format(voucher.getDownloadedDate());
				LOG.info("start date of voucher:{} ", startDate);
				LOG.info("end date of voucher:{} ", endDate);
				
				PromoCodeRequest promocodeRequest = new PromoCodeRequest();
				
				List<String> accountno=new ArrayList<String>();
				accountno.add(accountNumber);
				promocodeRequest.setAccountNumber(accountno);
				promocodeRequest.setSubscriptionCatalogId(welcomeGiftRequest.getSubscriptionCatalogId());				
				PromoCodeDetails promoCodeDetails = new PromoCodeDetails();
				promoCodeDetails.setPromoCode(voucherId);
				promoCodeDetails.setPromoCodeDuration(welcomeGiftRequest.getFreeDuration());
				promoCodeDetails.setWelcomeGift(true);
				promoCodeDetails.setPromoCodeDescription("Welcome Gift");
				promoCodeDetails.setPromoCodeTotalCount(1);
				promoCodeDetails.setPromoCodeType("Free Duration");
				promoCodeDetails.setPromoCodeLevel("Instance");
				promoCodeDetails.setPromoUserRedeemCountLimit(1);
				promoCodeDetails.setValue(0);
				promoCodeDetails.setEndDate(endDate);
				promoCodeDetails.setStartDate(startDate);
				promocodeRequest.setPromoCodeDetails(promoCodeDetails);
				LOG.info("promoCodeRequest:{} ", promocodeRequest.toString());
				ResultResponse resultResponse = new ResultResponse(externalTransactionId);
				resultResponse = promoCodeDomain.createPromoCode(promocodeRequest, resultResponse, program, userName);
				String offerTitleEn = null;
				String offerTitleAr = null;
				OfferCatalog giftOffer = offerRepository.findByOfferIdAndIsGift(welcomeGiftRequest.getGiftId() ,OfferConstants.FLAG_SET.get());
				if (!ObjectUtils.isEmpty(giftOffer.getOffer())) {
					offerTitleEn = giftOffer.getOffer().getOfferTitleDescription().getOfferTitleDescriptionEn();
					offerTitleAr = giftOffer.getOffer().getOfferTitleDescription().getOfferTitleDescriptionAr();
				}
				giftDetails.setResult(WelcomeGiftCodes.SUBSCRIPTIONPROMO_WELCOME_GIFT.getId(),
						WelcomeGiftCodes.SUBSCRIPTIONPROMO_WELCOME_GIFT.getMsg());
				welcomeGiftDetails.setAccountNumber(accountNumber);
				welcomeGiftDetails.setGiftType("subscriptionPromo");
				welcomeGiftDetails.setGiftValue(voucherId);
				welcomeGiftDetails.setEndDate(endDate);
				welcomeGiftDetails.setOfferTitleEn(offerTitleEn);
				welcomeGiftDetails.setOfferTitleAr(offerTitleAr);
				welcomeGiftDetailsList.add(welcomeGiftDetails);
				giftDetails.setWelcomeGiftDetails(welcomeGiftDetailsList);
				}
			LOG.info("subscriptionPromo giftDetails:{} ", giftDetails);
			return giftDetails;
		}
		
		if (giftType != null && giftType.equalsIgnoreCase(WelcomeGiftCodes.ACCOUNT_NUMBER_NOT_VALID.getId())) {
			return giftDetails;
		}
		if(giftType == null) {
			giftDetails.addErrorAPIResponse(WelcomeGiftCodes.WELCOME_GIFTTYPE_NULL.getIntId(),
					WelcomeGiftCodes.WELCOME_GIFTTYPE_NULL.getMsg());
		}
		LOG.info("MarketplaceController method ended");
		return giftDetails;
	
	}
	
	@GetMapping(value = "/promocodes")
	public PromoCodeResponse getAllPromocodes(
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "size", required = false) String size,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId)
	{
		
		PromoCodeResponse  promoCodeResponse=new PromoCodeResponse(transactionId);
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		List<PromoCode> promoCodes=promoCodeDomain.getPromocodeAll(page,size);
		if(null==promoCodes || promoCodes.isEmpty() )
		{
			promoCodeResponse.addErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_DETAILS_NOT_FOUND.getIntId(), PromoCodeErrorCodes.PROMOCODE_DETAILS_NOT_FOUND.getMsg());
		}
		promoCodeResponse.setPromoCoderesult(promoCodes);
		
		return promoCodeResponse;
		
	}
	
	@PostMapping(value = "/updatePromocode")
	public ResultResponse updatePromoCode(
			@RequestBody PromoCode promoCodeRequest,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId)
	{
		ResultResponse resultResponse=new ResultResponse(transactionId);
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		LOG.info("request",promoCodeRequest);
		 resultResponse=promoCodeDomain.updatePromoCodeDetails(promoCodeRequest,resultResponse,userName,headers);
		
		return resultResponse;
	
	}
	

	@PostMapping(value = "/writeExcelSheet", produces = MediaType.APPLICATION_JSON_VALUE)
	public void writeExcel(@RequestBody String offerType) throws FileNotFoundException, IOException {
		List<PurchaseHistory> purchaseHistoryList = purchaseRepository.findByOfferType(offerType);
		WriteExcelData(purchaseHistoryList, offerType);
	}

	private void WriteExcelData(List<PurchaseHistory> purchaseHistoryList, String offerType)
			throws FileNotFoundException, IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		String sheets[] = { offerType, "2.DODCounts", "3.Discounts", "4.MemberLogin", "5.NewEnrollments" };

		Object[][] headerData = {
				{ "Count", "Type Of Payment", "Offer Title", "Description", "Owner Transaction", "Gift",
						"Transaction Date" },
				{ "Count", "Type Of Payment", "Offer Title", "Description", "Owner Transaction", "Gift",
						"Transaction Date" },
				{ "Count", "Type Of Payment", "Offer Title", "Description", "Owner Transaction", "Gift",
						"Transaction Date" },
				{ "Count", "Type Of Payment", "Offer Title", "Description", "Owner Transaction", "Gift",
						"Transaction Date" },
				{ "Count", "Type Of Payment", "Offer Title", "Description", "Owner Transaction", "Gift",
						"Transaction Date" } };

		int headerIndex = 0;

		for (String s : sheets) {
			XSSFSheet sheet = workbook.createSheet(s);

			int rowCount = 0;
			int cellCount = 0;

			// header Logic
			Row header = sheet.createRow(rowCount);
			Object[] obj = headerData[headerIndex];
			for (Object headercell : obj) {
				header.createCell(cellCount++).setCellValue(headercell.toString());
			}
			headerIndex++;

			for (PurchaseHistory purchaseHistory : purchaseHistoryList) {
				Row row = sheet.createRow(++rowCount);
				row.createCell(6).setCellValue(purchaseHistory.getCreatedDate());
				row.createCell(5).setCellValue("Gift");
				row.createCell(4).setCellValue(purchaseHistory.getChannelId());
				row.createCell(3).setCellValue(purchaseHistory.getPurchaseItem());
				row.createCell(2).setCellValue(purchaseHistory.getOfferId());
				row.createCell(1).setCellValue(purchaseHistory.getPaymentMethod());
				row.createCell(0).setCellValue(purchaseHistory.getPaymentMethod());
			}

			try (FileOutputStream outputStream = new FileOutputStream("Statistics.xlsx")) {
				workbook.write(outputStream);
			}

		}

	}

	public void callServiceLog(String externalTransactionId, String json, String json2, String action,
			String accountNumber, String status, String userName) {
		// TODO Auto-generated method stub
		ServiceCallLogsDto callLog = new ServiceCallLogsDto();
		callLog.setAccountNumber(accountNumber);
		callLog.setAction(action);
		callLog.setCreatedDate(new Date());
		callLog.setCreatedUser(userName);
		callLog.setRequest(json2);
		callLog.setResponse(json);
		callLog.setStatus(status);
		callLog.setTransactionId(externalTransactionId);
		LOG.info("Saving Service Log");
		template.convertAndSend("serviceCallLogQueue", callLog);
	}

	@GetMapping(value = "/TestEPGPayment")
	public EPGResponse getEPGpayment(
			@RequestBody PurchaseRequestDto request,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId)
	{
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		ResultResponse resultResponse = new ResultResponse(null);
		EPGResponse response = null;
		try {
			response = epgIntegrationService.callEPGPayerNotPresentPayment(request, header, resultResponse);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	@GetMapping(value = "/tibcoCall/testAutorenewalSubscriptionMiscPosting")
	public CommonApiStatus callMiscPaymentPostingForAutoRenewalSubs(
			@RequestBody PurchaseRequestDto request,
			@RequestParam(value = "subscriptionFlag", required = true) String subscriptionFlag,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) throws MarketplaceException {
		
		//Changes for loyalty as a service.
				if (null == program) program = defaultProgramCode;
				Headers header = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
						channelId, systemId, systemPassword, token, transactionId);
		LOG.info("Tibco Misc Payment Posting for subscription called...............");
		return paymentService.callMiscPaymentPostingForAutoRenewalSubscriptions(request, subscriptionFlag, header);
	}

	/**
	 * This method runs bulk payment-reversal process
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@PostMapping(value = "/paymentReversalBatch", consumes = MediaType.ALL_VALUE)
	public ResultResponse paymentReversalBatch(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info("Entering paymentReversalBatch() of class {}", this.getClass().getSimpleName());

		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		try {
			resultResponse = smilesAppService.bulkPaymentReversal(headers);
		} catch (Exception e) {
			LOG.info("Exception occured at /paymentReversalBatch of  {} :{}", this.getClass().getSimpleName(), e.getMessage());
			e.printStackTrace();
		}

		LOG.info("Leaving paymentReversalBatch() of class {}", this.getClass().getSimpleName());

		return resultResponse;

	}
	
	/**
	 * This method tests payment-reversal integration
	 * @param request
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@PostMapping(value = "/testPaymentReversal", consumes = MediaType.ALL_VALUE)
	public ResultResponse paymentReversal(
			@RequestBody PurchaseRequestDto request,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info("Entering paymentReversal() of class {}", this.getClass().getSimpleName());

		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		resultResponse.getApiStatus().setExternalTransactionId(externalTransactionId);
		
		String accountType = null;
		try {	
		GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(request.getAccountNumber(), new ResultResponse(headers.getExternalTransactionId()), headers);
		accountType = !ObjectUtils.isEmpty(memberDetails)
				&& !CollectionUtils.isEmpty(memberDetails.getCustomerType())
				? memberDetails.getCustomerType().get(0)
				: null;
		PaymentReversalRequetsDto reversalRequest = new PaymentReversalRequetsDto();
		reversalRequest.setPurchaseRequestDto(request);
		reversalRequest.setAccountType(accountType);

		resultResponse = smilesAppService.smilesPaymentReversal(Arrays.asList(reversalRequest), false, headers);
		} catch (Exception e) {
			LOG.info("Exception occured at /testPaymentReversal of  {} :{}", this.getClass().getSimpleName(), e.getMessage());
			e.printStackTrace();
		}

		LOG.info("Leaving paymentReversal() of class {}", this.getClass().getSimpleName());
		LOG.info("resultResposne : {}", resultResponse);
		return resultResponse;

	}
	
}

