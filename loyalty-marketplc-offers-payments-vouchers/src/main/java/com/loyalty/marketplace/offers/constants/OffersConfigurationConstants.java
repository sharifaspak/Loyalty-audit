package com.loyalty.marketplace.offers.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.constants.RequestMappingConstants;

/**
 * 
 * @author jaya.shukla
 *
 */
@RefreshScope
@Component
public class OffersConfigurationConstants extends RequestMappingConstants{
	
    //All supress warnings
	public static final String RAW_TYPE = "rawtypes";
	public static final String UNCHECKED = "unchecked";
	
	//All property file values : Member Activity Service
	public static final String MEMBER_ACTIVITY_URI = "${memberActivity.uri}";
	public static final String PARTNER_ACTIVITY= "${memberActivity.partnerActivity.path}";
	public static final String PROGRAM_ACTIVITY_LIST = "${memberActivity.programActivityList.path}";
	public static final String EQUIVALENT_POINTS= "${memberActivity.equivalentPoints.path}";
	
	//All property file values : Member Management Service
	public static final String  MEMBER_MANAGEMENT_URI ="${memberManagement.uri}";
	public static final String GET_MEMBER_PATH = "${memberManagement.getMember.path}" ;
	public static final String ELIGIBILITY_MATRIX_PATH = "${memberManagement.eligibilityMatrix.path}";
	public static final String  ALL_CUSTOMER_TYPE_PATH = "${memberManagement.customerTypes.path}";
	public static final String GET_BIRTHDAY_DETAILS_PATH = "${memberManagement.getBirthdayDetails.path}";
	public static final String GET_COUNTRIES_DETAILS_PATH="${memberManagement.getcountrieslist.path}";
	public static final String UPDATE_REFFERAL_BONUS_PATH="${memberManagement.updateReferralBonus.path}";
	public static final String PAYMENT_REFERRALBONUS_ENABLE="${payment.referralbonus.enable}";
	
	//All property file values : Points Bank Service
	public static final String POINTS_BANK_URI = "${points.bank.uri}";
	public static final String ACCRUAL_TRANSACTIONS_PATH = "${points.bank.memberAccrual.path}";
	
	//All property file values : Decision Manager Service
	public static final String DECISION_MANAGER_URI = "${decisionManager.uri}";
	public static final String CUSTOMER_SEGMENT_CHECK_PATH = "${decisionManager.rulescheck.path}";
	public static final String GET_PROMOTIONAL_GIFT_PATH = "${decisionManager.promotional.gift.path}";
	
	//All property file values : Partner Management Service
	public static final String  PARTNER_MANAGEMENT_URI = "${partnerManagement.uri}";
	public static final String  GET_PARTNER_PATH = "${partnerManagement.getPartner.path}";
	public static final String  GET_PARTNER_TYPE_PATH = "${partnerManagement.getPartnerType.path}";
	
	//Cinema Offer Limits
	public static String cinemaRule;
	public static int standardGlobalLimit;
    public static int subscriberGlobalLimit;
    public static int specialGlobalLimit;
    public static int standardMemberLimit;
    public static int subscriberMemberLimit;
    public static int specialMemberLimit;
    public static int standardAccountLimit;
    public static int subscriberAccountLimit;
    public static int specialAccountLimit;
    
    @Value("${rule.cinema.offer}")
	public void setRules(String rule) {
		cinemaRule = rule;
	}
	
    @Value("${standard.global.limit}")
    public void setStandardGlobalLimit(String limit) {
    	standardGlobalLimit = Integer.parseInt(limit);
	}
    
	@Value("${subscriber.global.limit}")
    public void setSubscriberGlobalLimit(String limit) {
		subscriberGlobalLimit = Integer.parseInt(limit);
	}
	
	@Value("${special.global.limit}")
	public void setSpecialGlobalLimit(String limit) {
    	specialGlobalLimit = Integer.parseInt(limit);
	}
	
	@Value("${standard.member.limit}")
    public void setStandardMemberLimit(String limit) {
    	standardMemberLimit = Integer.parseInt(limit);
	}
    
	@Value("${subscriber.member.limit}")
    public void setSubscriberMemberLimit(String limit) {
		subscriberMemberLimit = Integer.parseInt(limit);
	}
	
	@Value("${special.member.limit}")
	public void setSpecialMemberLimit(String limit) {
		specialMemberLimit = Integer.parseInt(limit);
	}
	
	@Value("${standard.account.limit}")
    public void setStandardAccountLimit(String limit) {
    	standardAccountLimit = Integer.parseInt(limit);
	}
    
	@Value("${subscriber.account.limit}")
    public void setSubscriberAccountLimit(String limit) {
		subscriberAccountLimit = Integer.parseInt(limit);
	}
	
	@Value("${special.account.limit}")
	public void setSpecialAccountLimit(String limit) {
		specialAccountLimit = Integer.parseInt(limit);
	}
	
	//Payment Methods
	public static String fullCreditCard;
	public static String fullPoints;
	public static String deductFromBalance;
	public static String addToBill;
	public static String partialCardPoints;
	public static String applePay;
	public static String samsungPay;
	
	@Value("${paymentMethod.credit.card}")
	public void setFullCreditCard(String method) {
		fullCreditCard = method;
	}

	@Value("${paymentMethod.full.points}")
	public void setFullPoints(String method) {
		fullPoints = method;
	}
	
	@Value("${paymentMethod.deduct.from.balance}")
	public void setDeductFromBalance(String method) {
		deductFromBalance = method;
	}

	@Value("${paymentMethod.add.to.bill}")
	public void setAddToBill(String method) {
		addToBill = method;
	}
	
	@Value("${paymentMethod.partial.card}")
	public void setPartialCardPoints(String method) {
		partialCardPoints = method;
	}
	
	@Value("${paymentMethod.apple.pay}")
	public void setApplePay(String method) {
		applePay = method;
	}
	
	@Value("${paymentMethod.samsung.pay}")
	public void setSamsungPay(String method) {
		samsungPay = method;
	}
		
	//Provisioning Channel
	public static String coms;
	public static String rtf;
	public static String emcais;
	public static String rbt;
	public static String phonyTunes;
	
	@Value("${provisioningChannel.coms}")
	public void setComs(String channel) {
		coms = channel;
	}
	
	@Value("${provisioningChannel.rtf}")
	public void setRtf(String channel) {
		rtf = channel;
	}
	
	@Value("${provisioningChannel.emcais}")
	public void setEmcais(String channel) {
		emcais = channel;
	}
	
	@Value("${provisioningChannel.rbt}")
	public void setRbt(String channel) {
		rbt = channel;
	}
	
	@Value("${provisioningChannel.phonyTunes}")
	public void setPhonyTunes(String channel) {
		phonyTunes = channel;
	}
			
	//Portals		
	public static String sweb;
	public static String sapp;
	
	@Value("${portal.web}")
	public void setSweb(String channel) {
		sweb = channel;
	}
	
	@Value("${portal.app}")
	public void setSapp(String channel) {
		sapp = channel;
	}
	
	//PurchaseItems
	public static final String DISCOUNT_VOUCHER = "coupon";
	public static final String CASH_VOUCHER = "voucher";
	public static final String DEAL_VOUCHER = "dealVoucher";
	public static final String ETISALAT_ADD_ON = "eService";
	public static final String GOLD_CERTIFICATE = "goldCertificate";
	public static String discountVoucherItem;
	public static String cashVoucherItem;
	public static String dealVoucherItem;
	public static String addOnItem;
	public static String goldCertificateItem;
	public static String billPaymentItem;
	public static String rechargeItem;
	public static String subscriptionItem;
	
	@Value("${purchaseItem.dicount.voucher}")
	public void setDiscountOfferItem(String item) {
		discountVoucherItem = item;
	}
	
	@Value("${purchaseItem.cash.voucher}")
	public void setCashOfferItem(String item) {
		cashVoucherItem = item;
	}
	
	@Value("${purchaseItem.deal.voucher}")
	public void setDealOfferItem(String item) {
		dealVoucherItem = item;
	}
	
	@Value("${purchaseItem.etisalat.addon}")
	public void setAddOnItem(String item) {
		addOnItem = item;
	}
	
	@Value("${purchaseItem.gold.certificate}")
	public void setGoldCertificateItem(String item) {
		goldCertificateItem = item;
	}
	
	@Value("${purchaseItem.bill.payment}")
	public void setBillPaymentItem(String item) {
		billPaymentItem = item;
	}
	
	@Value("${purchaseItem.recharge}")
	public void setRechargeItem(String item) {
		rechargeItem = item;
	}
	
	@Value("${purchaseItem.subscription}")
	public void setSubscriptionItem(String item) {
		subscriptionItem = item;
	}
			
	//Offer Type
  	public static final String DISCOUNT_VOUCHER_ID = "1";
  	public static final String CASH_VOUCHER_ID = "2";
  	public static final String DEAL_VOUCHER_ID = "20";
  	public static final String ETISALAT_ADD_ON_ID = "5";
  	public static final String GOLD_CERTIFICATE_ID = "31";
  	public static String[] discountOfferType;
	public static String[] cashOfferType;
	public static String[] dealOfferType;
	public static String[] addOnOfferType;
	public static String[] goldCertificateOfferType;
	public static String[] telecomOfferType;
	public static String[] lifestyleOfferType;
	public static String[] welcomeGiftOfferType;
	public static String[] otherOfferType;
	
	@Value("${offer.typeId.discount.voucher.list}")
	public void setDiscountOfferType(String type) {
		discountOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.cash.voucher.list}")
	public void setCashOfferType(String type) {
		cashOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.deal.voucher.list}")
	public void setDealOfferType(String type) {
		dealOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.etisalat.addon.list}")
	public void setEtisalatOfferType(String type) {
		addOnOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.gold.certificate.list}")
	public void setGoldCertificateOfferType(String type) {
		goldCertificateOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.deal.voucher.list}")
	public void setTelecomOfferType(String type) {
		telecomOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.other.list}")
	public void setOtherOfferType(String type) {
		otherOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.lifestyle.offer.list}")
	public void setLifestyleOfferType(String type) {
		lifestyleOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	@Value("${offer.typeId.gold.certificate.list}")
	public void setWelcomeGiftOfferType(String type) {
		welcomeGiftOfferType = type.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	//Alerts
	public static final String LANGUAGE_EN = "en";
	public static final String LANGUAGE_AR = "ar";
	public static String languageEn;
	public static String languageAr;
	
	@Value("${language.english}")
	public void setLanguageEnglish(String language) {
		languageEn = language;
	}
	
	@Value("${language.arabic}")
	public void setLanguageArabic(String language) {
		languageAr = language;
	}
	
	//Birthday Push Notification
	public static final String NOTIFICATION_TEMPLATE_ID = "119";
	public static final String PUSH_NOTIFICATION_CODE = "00";
	public static String birthdayNotificationTemplateId;
	public static String birthdayNotificationCode;
	
		
	@Value("${birthday.alert.notification.template.id}")
	public void setBirthdayNotificationId(String id) {
		birthdayNotificationTemplateId = id;
	}
	
	@Value("${birthday.alert.push.notification.code}")
	public void setBirthdayNotificationCode(String code) {
		birthdayNotificationCode = code;
	}
		
	//Birthday SMS
	public static final String BIRTHDAY_SMS_TEMPLATE_ID = "142";
	public static final String BIRTHDAY_SMS_NOTIFICATION_ID = "142";
	public static final String BIRTHDAY_SMS_NOTIFICATION_CODE = "00";
	public static String birthdaySmsTemplateId;
	public static String birthdaySmsNotificationId;
	public static String birthdaySmsNotificationCode;
	
	@Value("${birthday.alert.sms.template.id}")
	public void setBirthdaySmsTemplateId(String id) {
		birthdaySmsTemplateId = id;
	}
	
	@Value("${birthday.alert.sms.notification.id}")
	public void setBirthdaySmsNotificationId(String id) {
		birthdaySmsNotificationId = id;
	}
	
	@Value("${birthday.alert.sms.notification.code}")
	public void setBirthdaySmsNotificationCode(String code) {
		birthdaySmsNotificationCode = code;
	}
		
	//Bogo Subscription
	public static final String BOGO_SUBSCRIPTION_NOTIFICATION_ID = "118"; 
	public static final String BOGO_SUBSCRIPTION_NOTIFICATION_CODE = "00";
	public static String bogoSubscriptionNotificationId;
	public static String bogoSubscriptionNotificationCode;
	
	@Value("${bogo.subscription.notification.template.id}")
	public void setBogoSubscriptionNotificationId(String id) {
		bogoSubscriptionNotificationId = id;
	}
	
	@Value("${bogo.subscription.push.notification.code}")
	public void setBogoSubscriptionNotificationCode(String code) {
		bogoSubscriptionNotificationCode = code;
	}
	
	//ConversionRate
	public static final String POINT_CONVERSION_PARTNER_CODE = "ES";
    public static final String POINT_GIFTING_PRODUCT_ITEM= "Gift Points";
    public static final Double BALANCE_TO_AMOUNT_RATE = 0.8;
    public static String discountVoucherProductItem;
    public static String cashVoucherProductItem;
    public static String dealVoucherProductItem;
    public static String addOnProductItem;
    public static String billPaymentProductItem;
    public static String rechargeProductItem;
    public static String subscriptionProductItem;
    public static String goldCertificateProductItem;
    public static String pointGiftingProductItem;
    public static String pointConversionPartnerCode;
    public static double goldCertificateConversionRate;
	
    @Value("${product.item.discount.voucher}")
	public void setDiscountVoucherProductItem(String item) {
    	discountVoucherProductItem = item;
	}
    
    @Value("${product.item.cash.voucher}")
	public void setCasgVoucherProductItem(String item) {
    	cashVoucherProductItem = item;
	}
    
    @Value("${product.item.deal.voucher}")
	public void setDealVoucherProductItem(String item) {
    	dealVoucherProductItem = item;
	}
    
    @Value("${product.item.add.on}")
	public void setAddOnProductItem(String item) {
    	addOnProductItem = item;
	}
    
    @Value("${product.item.bill.payment}")
	public void setBillPaymentProductItem(String item) {
    	billPaymentProductItem = item;
	}
    
    @Value("${product.item.recharge}")
	public void setRechargeProductItem(String item) {
    	rechargeProductItem = item;
	}
    
    @Value("${product.item.subscription}")
	public void setSubscriptionProductItem(String item) {
    	subscriptionProductItem = item;
	}
    
    @Value("${product.item.gold.certificate}")
	public void setGoldCertificateProductItem(String item) {
    	goldCertificateProductItem = item;
	}
    
    @Value("${point.conversion.product.item}")
	public void setPointGiftingProductItem(String item) {
    	pointGiftingProductItem = item;
	}
    
    @Value("${point.conversion.partner.code}")
	public void setPointConversionPartnerCode(String code) {
    	pointConversionPartnerCode = code;
	}
    
    @Value("${goldCertificate.balanceToAmount.rate}")
	public void setGoldCertificateConversionRate(String rate) {
		goldCertificateConversionRate = Double.parseDouble(rate);
	}
    
    //CustomerSegments
  	public static final String ALL_CUSTOMER_SEGMENTS = "${eligible.customerSegments.offers}";
    public static String nonEligibleCustomerSegment;
    public static String standardCustomerSegment;
    public static String subscriberCustomerSegment;
    public static String specialCustomerSegment;
    public static String[] customerSegments;
	
    @Value("${customerSegment.non.eligble}")
	public void setNonEligibleCustomerSegment(String segment) {
    	nonEligibleCustomerSegment = segment;
	}
    
    @Value("${customerSegment.standard}")
	public void setStandardCustomerSegment(String segment) {
    	standardCustomerSegment = segment;
	}
    
    @Value("${customerSegment.subscriber}")
	public void setSubscriberCustomerSegment(String segment) {
    	subscriberCustomerSegment = segment;
	}
    
    @Value("${customerSegment.special}")
	public void setSpecialCustomerSegment(String segment) {
    	specialCustomerSegment = segment;
	}
    
    @Value("${eligible.customerSegments.offers}")
	public void setCustomerSegments(String segments) {
		customerSegments = segments.split(OfferConstants.COMMA_SEPARATOR.get());
	}
    
    //Configured values
    public static final String OFFER_COUNTER_FLAG_ENABLED = "${counter.offerCounterFlagEnabled}";
    public static int dmDuration;  
    public static double defaultConversionRate;
    public static boolean isBatch;
    public static final String IS_BATCH_TOGGLE = "${togglz.features.BATCH.enabled}";
    public static final String DEFAULT_PROGRAM_CODE = "${programManagement.defaultProgramCode}";
    		
    @Value("${decision.manager.check.period}")
	public void setDurations(String configuredDuration) {
		dmDuration = Integer.parseInt(configuredDuration);
	}
    
    @Value("${default.conversion.rate}")
	public void setDefaultConversionRate(String rate) {
    	defaultConversionRate = Double.parseDouble(rate);
	}
    
    @Value("${togglz.features.BATCH.enabled}")
	public void setIsBatch(String batchValue) {
    	isBatch = Boolean.getBoolean(batchValue);
	}
	    
	//Role configuration
	public static final String ADMIN = "${role.admin}";
	public static final String OFFER_UPADTE_ROLES = "${role.offer.update}";
    
	//Voucher redemption
	public static String[] voucherRedeemTypes;
	public static final String DEFAULT_REDEEM_TYPE = "${default.redeem.type}";
	public static final String NON_PIN_REDEEM_TYPE = "${non.pin.redeem.type}";
	public static final String ONLINE_REDEEM_TYPE = "${online.redeem.type}";
	public static final String PARTNERPIN_REDEEM_TYPE = "${partnerPin.redeem.type}";
	
	@Value("${voucher.redeem.type}")
	public void setVoucherRedeemTypes(String redeemTypes) {
		voucherRedeemTypes = redeemTypes.split(OfferConstants.COMMA_SEPARATOR.get());
	}
	
	//Member Management 
	public static final String MM_DB_URI = "${membermanagement.mongodb.uri}";
	public static final String MM_DB = "${membermanagement.mongodb.dbname}";
		
  	//Offer Configurable Values
  	public static final Integer ZERO_INTEGER = 0;
  	public static final Double ZERO_DOUBLE = 0.0;
	public static final String THREAD_POOL_TASK_EXECUTOR = "threadPoolTaskExecutor";
	
	//Counter Reset
	public static final String ANNUAL="annual";
	public static final String MONTHLY="monthly";
	public static final String WEEKLY="weekly";
	public static final String DAILY="daily";
	public static final String TOTAL="total";
	

	//Restaurant Details
	public static final String RESTAURANTS_URI = "${restaurants.url}";
	public static final String RESTAURANTS_API_KEY = "${restaurants.api.key}";
	public static final String RESTAURANTS_API_SECRET = "${restaurants.api.secret}";
	
	public static final String LIFETIME_SAVINGS_FETCH_URL = "${lifetime.savings.retrieval.url}";
	  
  	
}
