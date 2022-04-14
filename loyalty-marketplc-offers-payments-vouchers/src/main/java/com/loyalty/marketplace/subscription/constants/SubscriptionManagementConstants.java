package com.loyalty.marketplace.subscription.constants;

public enum SubscriptionManagementConstants {
	
	CHANNELID_WEB("WEB"),
	SUBSCRIPTION_ACTIVE_STATUS("Active"),
	SUBSCRIPTION_INACTIVE_STATUS("Inactive"),
	SUBSCRIBED_STATUS("Subscribed"),
	CANCELLED_STATUS("Cancelled"),
	PARKED_STATUS("Parked"),
	SUBSCRIPTION_CATALOG_INSERT("insert"),
	SUBSCRIPTION_CATALOG_UPDATE("update"),
	SUBSCRIPTION_CATALOG_DELETE("delete"),
	CHARGEABILITY_TYPE_ONE("one-time"),
	CHARGEABILITY_TYPE_AUTO("auto-renewable"),
	CHARGEABILITY_TYPE_LIFE("life-time"),
	SEGMENT_LIFESTYLE("lifestyle"),
	SEGMENT_FOOD("food"),
	SEGMENT_COMBO("combo"),
	PAYMENT_METHOD_CARD("fullCreditCard"),
	PAYMENT_METHOD_DCB_ATB("addToBill"),
	PAYMENT_METHOD_DCB_DFB("deductFromBalance"),
	PAYMENT_METHOD_POINTS("fullPoints"),
	PAYMENT_METHOD_DFB("deductFromBalance"),
	PAYMENT_METHOD_PARTIALPOINTS("partialPointsCC"),
	PAYMENT_METHOD_PREPAID("prepaid"),
	SUBSCRIPTION_METHOD_FREE("free"),
	PAYMENT_SERVICE_LOYALTY("loyalty"),
	PAYMENT_SERVICE_PHONEY_TUNES("phoneyTunes"),
	PROGRAM_ACTIVITY_SUBSCRIPTION("Subscription"),
	SUBSCRIPTION_PARTNER_CODE("ES"),
	SUBSCRIPTION_ACTIVITY_CODE("Subscription"),
	SUBSCRIPTION_ACTIVITY_ID("5e0f25c72d243f6fb5b00654"),
	SUBSCRIPTION_ACTIVITY_CODE_DESCRIPTION_EN("Subscription"),
	SUBSCRIPTION_ACTIVITY_CODE_DESCRIPTION_AR("تنازلي النشاط"),
	CANCEL_SUBSCRIPTION("cancel"),
	PARK_SUBSCRIPTION("park"),
	ACTIVATE_SUBSCRIPTION("activate"),
	WELCOME_GIFT("welcome"),
	ENROL_CHANNEL_PARTNER("partner"),
	CRM_THIRD_PARTY_PRODUCTCODE("RPGSNOTIFYA"),
	ENTERPRISE_CUSTOMER_TYPE("ENTERPRISE"),
	ENTERPRISE_CUSTOMER_TYPE_MOBILE("ENTERPRISE_MOBILE"),
	ETISALAT_PARENT_CUSTOMER_TYPE("Etisalat"),
	CHANNELID_CBD("CBD"),
	CARD_TYPE_PLATINUM("PLAT"),
	CARD_TYPE_SIGNATURE("SIGN"),
	SIMPLE_DATE_FORMAT("dd/MM/yyyy"),
	PACKAGE_TYPE_APOLO("Apolo"),
	PACKAGE_TYPE_ELIFE("Elife"),
	CANCEL_ONE_EXTEND_ONE("cancel-one-extend-one"),
	CANCEL_ONE_SUBSCRIBE_LIFE("cancel-one-subscribe-life"),
	CANCEL_AUTO_SUBSCRIBE_ONE("cancel-auto-subscribe-one"),
	CANCEL_AUTO_SUBSCRIBE_LIFE("cancel-auto-subscribe-life"),
	TIBCO_SYSTEM("TIBCO_SYSTEM"),
	ADMIN_PORTAL("ADMIN_PORTAL"),
	ACCOUNT_CANCEL_EVENT("ACCOUNT_CANCEL_EVENT"),
	CRM_JOB("memberMngmnt-updateCrmInfo-job"),
	DATE_FORMAT_FOR_EMAIL("dd MMMM yyyy"),
	PAYMENT_TYPE("Renewal"), 
	RENEWAL_FILE_PREFIX("Renewal_Report_"),
	FROM_DATE_TIME("00:00:00"),
	END_DATE_TIME("23:59:59"), 
	END_DATE_TIME_ONE_TIME("00:00:00"),
	UTC_DATE_TIME("04:00:00"),
	CSV_EXTENSION(".csv"), 
	CSV("csv"),
	SUBSCRIPTION_RENEWAL_REPORT("Subscription Renewal Report"), 
	ENGLISH_LANGUAGE("en"),
	ARABIC_LANGUAGE("ar"),
	FILE_PATH("filePath"), 
	FILE_NAME("fileName"),
	REPORT_NAME("REPORT_NAME"),
	FILE_TYPE("fileType"),
	ACCOUNT_NUMBER("ACCOUNT NUMBER"),
	SUBSCRIPTION_ID("SUBSCRIPTION ID"),
	SUBSCRIPTION_CATALOG_ID("SUBSCRIPTION CATALOG ID"),
	SUBSCRIPTION_STATUS("SUBSCRIPTION STATUS"),
	AMOUNT("RENEWAL AMOUNT"),
	EPG_TRANSACTION_ID("EPG TRANSACTION ID"),
	MASTER_EPG_TRANSCATION_ID("MASTER EPG TRANSCATION ID"),
	EPG_STATUS("EPG STATUS"),
	EPG_RESPONSE("EPG RESPONSE"),
	TRANSACTIONS_DATE_FORMAT("yyyy-MM-dd"), 
	EXTERNAL_TRANSACTION_ID("EXTERNAL TRANSACTION ID"),
	DATE_FORMAT("yyyy-MM-dd HH:mm:ss"),
	NUMBER_TYPE_PREPAID("PRE"),
	NUMBER_TYPE_POSTPAID("POS"),
	CHANNEL_ID_SAPP("SAPP"),
	ACTION_TYPE_ACTIVATED("Activated"),
	ACTION_TYPE_RENEWED("Renewed"),
	FOOD_FREE_ULTRA("OF_9999_FOOD_FREE_Ultra"),
	FREE_DURATION_PROMO_TYPE("4"),
	REASON_NOT_APPLICABLE("NA"),

	
	
	
	
	;
	
	
	private final String constant;
	public static final String SMS_TEMPLATEID_CHARGEABILITY_TYPE_AUTO = "22";
	public static final String EMAIL_TEMPLATEID_CHARGEABILITY_TYPE_AUTO = "22";
	public static final String SMS_NOTIFICATION_CHARGEABILITY_TYPE_AUTO = "22";
	public static final String EMAIL_NOTIFICATION_CHARGEABILITY_TYPE_AUTO = "22";
	public static final String EMAIL_TEMPLATEID_AUTORENEWAL = "22";
	public static final String SMS_TEMPLATEID_AUTORENEWAL = "158";
	public static final String SMS_NOTIFICATIONID_AUTORENEWAL = "158";
	public static final String EMAIL_NOTIFICATION_AUTORENEWAL = "22";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String EVENT = "EVENT";
	public static final String PACK = "PACK";
	public static final String PACK_TYPE ="PACK_TYPE";
	public static final String AMOUNT_PAID = "AMOUNT_PAID";
	public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
	public static final String PAYMENT_ICON = "PAYMENT_ICON";
	public static final String CC_DETAILS = "CC_DETAILS";
	public static final String SMILES_POINTS = "SMILES_POINTS";
	public static final String SUB_TOTAL = "SUB_TOTAL";
	public static final String VAT = "VAT";
	public static final String TOTAL = "TOTAL";
	public static final String ORDER_DETAILS1 = "ORDER_DETAILS_1";
	public static final String ORDER_DETAILS2 = "ORDER_DETAILS_2";
	public static final String ORDER_DETAILS3 = "ORDER_DETAILS_3";
	public static final String TNC = "TNC";
	public static final String MANAGE_SUB = "MANAGE_SUB";
	public static final String GREETING1 = "Greeting1";
	public static final String NEW_SUBSCRIPTION_EVENT = "Confirmed";
	public static final String RENEWED_EVENT = "Renewed";
	public static final String DATE = "DATE";
	public static final String PACKAGE_NAME = "Package_Name";
	public static final String ACTION_TYPE = "ACTION_TYPE";
	public static final String BENEFITS = "BENEFITS";
	public static final String DD_Mon_YYYY = "DD-Mon-YYYY";
	public static final String PRICE = "PRICE";
	public static final String Status = "Status";
	public static final String English = "English";
	public static final String Arabic = "Arabic";
	
	public static final String LIFESTYLE_PACKNAME_ARABIC = "عروض \"اشتر 1 واحصل على 1 مجاناً\" غير محدودة من بسمات";
	public static final String FOOD_PACKNAME_ARABIC = "توصيل مجاني غير محدود مع بسمات";
	public static final String LIFESTYLE_BENEFIT_ARABIC = "تمتع بقسائم خصومات غير محدودة على \"اشتر 1 واحصل على 1 مجاناً\"";
	public static final String FOOD_BENEFIT_ARABIC = "يمكنك الآن الحصول على توصيل مجاني لكل طلب قيمته 50 درهم أو أكثر من أي مطعم";
	public static final String LIFESTYLE_PRICE_ARABIC = "20 درهماً";
	public static final String FOOD_PRICE_ARABIC = "30 درهماً";
	public static final String ONETIME_SUBSCRIPTION_STATUS_ARABIC = "انتهاء الصلاحية";
	public static final String AUTORENEWAL_SUBSCRIPTION_STATUS_ARABIC = "إعادة التجديد";
	public static final String ACTIONTYPE_ACTIVATED_ARABIC = "مفعل";
	public static final String ACTIONTYPE_RENEWED_ARABIC = "متجدد";

	public static final String SUBSCRIPTION = "Subscription";
	public static final String SUBSCRIPTION_PAYMENT = "SubscriptionPayment";

	public static final String SUBSCRIPTION_CATALOG = "SubscriptionCatalog";
	public static final String TOKEN = "token";
	public static final Integer ZERO_INTEGER = 0;
  	public static final Double ZERO_DOUBLE = 0.0;


	SubscriptionManagementConstants(String constant) {
		this.constant = constant;
	}

	public String get() {
		return this.constant;
	}
}


