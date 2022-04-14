package com.loyalty.marketplace.payment.constants;

/**
 * The MarketplaceConstants should contain all the Application Constants
 * used thought the Partner Management micro-service.
 * 
 * @author 
 */
public enum MarketplaceConstants {
	
	CHANNEL("WEB"),
	SUB_CHANNEL("CUST_PORTAL"),
	LANGUAGE("en"),
	ENGLISH("English"),
	ENG("en"),
	ARABIC("Arabic"),
	AR("ar"),
	FIRSTNAME("FIRST_NAME"),
	REDEEMPOINTS("REDM_PTS"),
	ACTIVITYDESCRIPTION("activity_description"),
	MSISDN("MSISDN"),
	CURRENTBALANCE("xxx"),
	AEDVALUE("REDM_VAL_NON_VOU"),
	AEDVALCASHVOUCHER("REDM_VAL"),
	VOUCHERCODE("VOUCHER_CODE"),
	VOUCHERNAME("VOUCHER_NAME"),
	QUANTITY("QUANTITY"),
	PARTNER("partner"),
	ACTION_TYPE("ADDSRVICE"),
	OFFER_CODE("offer_code"),
	USER_ID_ATTR("user_id"),
	USER_ID_VAL("WEBATM"),
	BILLPAYMENTACTIVITY("Post Paid Bill Payment"),
	RECHARGEACTIVITY("WASEL Recharge"),
	IS_DEDUCTION_REQUIRED_ATTR("isDeductionRequired"),
	TRANSACTION_TYPE_ATTR("transactiontype"),
	TRANSACTION_TYPE_VAL("Card"),
	AMOUNT_ATTR("amount"),
	PAYMENT_REF_NUM_ATTR("paymentrfrncnmb"),
	RESELLER_CODE_ATTR("resellercode"),
	CASHIER_ID_ATTR("cashier_id"),
	CASHIER_ID_VAL("WAPGTWAY"),
	AUTHORIZATION_CODE_ATTR("authorizationcode"),
	CARD_NUMBER_ATTR("cardnumber"),
	INSTALLMENT_FLAG_ATTR("installamentflag"),
	PAYMENT_MODE_ATTR("paymentmode"),
	PAYMENT_MODE_VAL("loyalty"),
	CHANNEL_TYPE_ATTR("channeltype"),
	CHANNEL_TYPE_VAL("LOYALTY"),
	CARD_TYPE_ATTR("cardtype"),
	CARD_SUB_TYPE_ATTR("cardsubtype"),
	CARD_SUB_TYPE_VAL("MASTER"),
	CARD_EXPIRY_DATE_ATTR("cardexpirydate"),
	P_TYPE("Smiles Redemption"),
	P_STAGE("COMS Provisioning"),
	PREPAID_ACCOUNT("PRE"),
	POSTPAID_ACCOUNT("POS"),
	IS_DEDUCT_FROM_BALANCE("isDeductFromBalance"),
	IS_FULL_POINTS("isFullPoints"),
	REQUESTEDSYSTEM("Loyalty"),
	ADJUSTMNT_REASON("ADJ-MISCEL"),
	CHARGE_TYPE_IMM("IMM"),
	FIRSTACCESSACTIVITYCODE("ENR2"),
	CHARGE_TYPE_OFF("1OFF"),
	CHARGE_CODE("B27"),
	TRANS_TYPE("D"),
	POSTPAID("Postpaid"),
	PREPAID("Prepaid"),
	paymentModeCode("MOBILEAPP"),
	IsCancelled("N"),
	cashierName("WAPGTWAY"),
	ptsBnkCd("7101"),
	ccBnkCd("7102"),
	loyaltyPymTyp("Loyalty"),
	cardPymTyp("Card"),
	POINTS("Points"),
	currency("AED"),
	channelType("E"),
	colltnRgnCd("DX"),
	CREDIT("Credit"),
	PaymentAccepted("Y"),
	BulkPayment("N"),
	PaymentProcessed("N"),
	SMSNotification("Y"),
	EmailNotification("Y"),
	RegionCode("DX"),
	TopupCompleted("N"),
	PaymentCategory("MobileNonMobile"),
	CHARGE_SUCCESS("TIB-000"),
	CCPaymentType("Card"),
	FULLCREDITCARD("fullCreditCard"),
	FULLPOINTS("fullPoints"),
	PARTIALPOINTSCC("partialPointsCC"),
	ETISALATADDON("eService"),
	RBT("RBT"),
	BILLPAYMENT("billPayment"),
	RECHARGES("recharge"),
	ADDTOBILL("addToBill"),
	DEALVOUCHER("dealVoucher"),
	CASHVOUCHER("voucher"),
	DISCOUNTVOUCHER("coupon"),
	SUBSCRIPTION("subscription"),
	GOLDCERTIFICATE("goldCertificate"),
	GOLDGIFT("goldGift"),
	GOLDPOINTS("goldPoints"),
	DEDUCTFROMBALANCE("deductFromBalance"),
	CONTENT_NAME("Smiles Discount Voucher Purchase"),
	STATUS_SUCCESS("SUCCESS"),
	STATUS_FAILED("FAILED"),
	STATUS_FAILURE("FAILURE"),
	STATUS_ERROR("ERROR"),
	DURATION("4"),
	PRIORITY("0"),
	LANGUAGE_ID("2"),
	PAYMENT_MODE("1"),
	ACTIVITY_ID("1"),
	TARGETED_SYSTEM("CRBT"),
	PACKAGE_ID("1038"),
	ERROR_MESSAGE_400("Bad Request"),
	FREE("free"),
	PAYMENTCODESAPP("SAPP"),
	RENEW_AUTORENEWAL_SUBSCRIPTION("C"),
	NEW_AUTORENEWAL_SUBSCRIPTION("N"),
	CHANNEL_GCHAT("GCHAT"),
	SUCCESS("Success"),
	CHANNEL_SAPP("SAPP"),
	SUBSCRIPTION_RENEWAL_BANKCODE("3459"),
	APPLEPAY("applePay"),
	SAMSUNGPAY("samsungPay"),
    GCHAT_PTSBNKCODE("7101"),
    GCHAT_CCBNKCODE("7102"),
    GCHAT_PAYMENTMODECODE("MOBILEAPP"),


	
	
	;
	
	
	private final String constant;

	public String getConstant() {
		return constant;
	}

	private MarketplaceConstants(String constant) {
		this.constant = constant;
	}
	
	
	
	
}

