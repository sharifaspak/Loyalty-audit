package com.loyalty.marketplace.offers.constants;

/**
 * 
 * @author jaya.shukla
 *
 */
public enum OfferConstants {

	EMPTY_CHARACTER(""),
	SPACE_CHARACTER(" "),
    MESSAGE_SEPARATOR(" : "),
    DOUBLE_COLON(" :: "),
    UNDERSCORE_SEPARATOR("_"),
	COMMA_OPERATOR(", "),
	COMMA_SEPARATOR(","),
	STAR_CHARACTER("*"), 
	FORWARD_SLASH("/"),
	DOT_CHARACTER("."),
		
    OFFER_CODE_PREF("OF_"),
	SUBOFFER_CODE_PREF("SUBOF_"),
	VOUCHER_EN(" Voucher Purchase"),
	VOUCHER_AR("شراء القسيمة "),
	
	KEYWORD_PATTERN("\\s*,\\s*"),
	OFFER_DEFAULT_STATUS("Inactive"),
	ACTIVE_STATUS("Active"),
	INACTIVE_STATUS("Inactive"),
		
	REQUEST_PARAMS("Request Parameters : "),
	RESPONSE_PARAMS("Response Parameters : "),
	SERVICE_REQUEST_PARAMS("Service Request Parameters : "),
	SERVICE_RESPONSE_PARAMS("Service Response Parameters : "),
	SERVICE_HEADER_PARAMS("Service headers Parameters : "),
	DOMAIN_PERSIST("Domain Object To Be Persisted:"),
	CONVERTED_ENTITY("Entity mapped from domain oject : "),
	DOMAIN_PERSISTED("Persisted Object: "),
	OFFER_ID_FOR("Offer id for "),
	PARTNER_CODE_FOR("Partner code for "),
	MERCHANT_CODE_FOR("Merchant code for "),
	IN(" in "), 
	REFER_LOGS(" Please refer logs for details."),
	REFER_EXCEPTION_LOGS(" Please refer ExceptionLogs collection for details : "),
	
	LOG_CONSTANTS("{} {}"),
	
	DATE_FORMAT("yyyy-MM-dd HH:mm:ss"),
	DAY_FULL_FORMAT("EEEE"),
	TRANSACTIONS_DATE_FORMAT("yyyy-MM-dd"),
	
	INSERT_ACTION("Insert"),
	ACTIVATE_ACTION("Activate"),
	DEACTIVATE_ACTION("Deactivate"),
	UPDATE_ACTION("Update"),
		
	FLAG_SET("Yes"),
	FLAG_NOT_SET("No"),
	FLAG_PARENT("Parent"),

	REEDEMING("redeeming"),
	
	ACTIVITY_ACCRUAL("Accrual"),
	ACTIVITY_REDEMPTION("Redemption"),
	
	ACTIVITY_ACCRUAL_CODE("ACR"),
	ACTIVITY_ACCRUAL_CODE_FOR_POINTS("PACR"),
	ACTIVITY_REDEMPTION_CODE("RED"),
	
	ACTIVITY_TYPE_NAME_EARNING_EN("earning"),
	ACTIVITY_TYPE_NAME_EARNING_AR("كسب"),
	ACTIVITY_TYPE_DESCRIPTION_EARNING_EN("accrual"),
	ACTIVITY_TYPE_DESCRIPTION_EARNING_AR("تراكمي"),
	
	ACTIVITY_TYPE_NAME_REEDEMING_EN(REEDEMING.get()),
	ACTIVITY_TYPE_NAME_REEDEMING_AR("الفداء"),
	ACTIVITY_TYPE_DESCRIPTION_REEDEMING_EN(REEDEMING.get()),
	ACTIVITY_TYPE_DESCRIPTION_REEDEMING_AR("الفداء"),
	
	ACTIVITY_CATALOGUE_ETISALAT_NAME_EN("Etisalat"),
	ACTIVITY_CATALOGUE_ETISALAT_NAME_AR("اتصالات"),
	ACTIVITY_CATALOGUE_ETISALAT_DESCRIPTION_EN("Etisalat"),
	ACTIVITY_CATALOGUE_ETISALAT_DESCRIPTION_AR("اتصالات"),
	ACTIVITY_CATALOGUE_PARTNER_NAME_EN("Partners"),
	ACTIVITY_CATALOGUE_PARTNER_NAME_AR("شركاء"),
	ACTIVITY_CATALOGUE_PARTNER_DESCRIPTION_EN("Partners"),
	ACTIVITY_CATALOGUE_PARTNER_DESCRIPTION_AR("شركاء"),
	
	RATE_TYPE_NAME_ACCRUAL("STDACR"),
	RATE_TYPE_DESCRIPTION_ACCRUAL("Standard Accrual"),
	RATE_TYPE_NAME_REDEMPTION("STDRED"),
	RATE_TYPE_DESCRIPTION_REDEMPTION("Standard Redemption"),
	RATE_TYPE_NAME_BONUS("STDBON"),
	RATE_TYPE_DESCRIPTION_BONUS("Standard Bonus"),
	
	NUMBER_TYPE_NAME("WS"),
	NUMBER_TYPE_DESCRIPTION("Description"),
	
	MARKETPLACE_PROGRAM_ACTIVITY("Marketplace Purchase"),
	TELECOM_SPEND_PROGRAM_ACTIVITY("Telecom Spend"),
	
	ACTIVITY_ACCRUAL_DESCRIPTION_EN("Accrual activity code for partner"),
	ACTIVITY_ACCRUAL_DESCRIPTION_AR("رمز نشاط الاستحقاق للشريك"),
	ACTIVITY_REDEMPTION_DESCRIPTION_EN("Redemption activity code for partner"),
	ACTIVITY_REDEMPTION_DESCRIPTION_AR("رمز نشاط الاسترداد للشريك"),
	
	DATE_PATTERN("dd-MM-yyyy"),
	END_DATE("31-12-9999"),
	
	PARTNER_EXISTS_MESSAGE("Partner Activity Already exists."),
	PARTNER_EXISTS_CODE("3005"),
	RESULT_KEY("result"), 
	MEMBER_RESPONSE_KEY("memberResponse"),
	LIST_PROGRAM_ACTIVITY_KEY("listProgramActivity"),
		
	SUCCESS("Success"),
	
	PAYMENT_METHOD_POINTS("Smiles Points"),
	PAYMENT_METHOD_CARDS("Credit Card"),
	PAYMENT_METHOD_DCB("DCB"),
	PAYMENT_METHOD_OTH("Card + Points"),
	
	SAVE_OFFER_REPOSITORY_METHOD("saveUpdateOffer"),
	UPDATE_OFFER_STATUS_REPOSITORY_METHOD("updateOfferStatus"),
	SAVE_ACTIVITY_REPOSITORY_METHOD("saveActivityCode"),
	
	PURCHASE_PARTNER_CODE("ES"),
	PROGRAM_ACTIVITY_BILL("Telecom Bill Payment"),
	PROGRAM_ACTIVITY_RECHARGE("Telecom Recharge"),
		
	SEND_MESSAGE("Sending mesage '%s' to "), 
	MESSAGE_SENT("Message sent to "), 
	
	CASH_VOUCHER_LIFETIME_SAVINGS("PV"),
	DISCOUNT_VOUCHER_LIFETIME_SAVINGS("DC"), 
	DEAL_VOUCHER_LIFETIME_SAVINGS("DV"),
	ETISALAT_SERVICES_LIFETIME_SAVINGS("ES"),
	ETISALAT_BUNDLE_LIFETIME_SAVINGS("EB"),
	GOLD_CERTIFICATE_LIFETIME_SAVINGS("GC"),
	SUBSCRIPTION_LIFETIME_SAVINGS("SC"),
	BILL_PAYMENT_LIFETIME_SAVINGS("BP"),
	RECHARGE_LIFETIME_SAVINGS("RC"),
	
	HITTING_URL("Hitting url "),
	REQUEST_PARAMS_FOR("Request parameters "),
	RESPONSE_PARAMS_FOR("Response recieved"),
	HEADER_PARAMS_FOR("Header parameters for "),
	
	INSIDE("Inside "),
	LEAVING("Leaving "),
	
	ENTERING("Entering "),
	EXITING("Exiting "),
		
	SINGLE_MESSAGE(" {} "),
			
	VOUCHER_DETAILS_FOR_PURCHASE_ID_METHOD("getVoucherDetailsForPurchaseId"),
	VOUCHER_DETAILS_FOR_PURCHASE_ID_LIST_METHOD("getVoucherDetailsForPurchaseIdList"),
	CUSTOMER_CHECK_METHOD("checkCustomerSegment"),
	GET_PROMOTIONAL_GIFT_METHOD("getPromotionalGift"),
	CHECK_PARTNER_EXISTS_METHOD("checkPartnerExists"), 
	CREATE_PARTNER_ACTIVITY_METHOD("createPartnerActivity"),
	GET_PROGRAM_ACTIVITY_LIST_METHOD("getProgramActivityList"),
	GET_MEMBER_DETAILS_METHOD("getMemberDetails"),
	GET_ELIGIBLE_PAYMENT_METHODS_METHOD("getEligiblePaymentMethods"),
	GET_PARTNER_ACTIVITY_CODE_METHOD("getPartnerActivityCode"),
	GET_EQUIVALENT_POINTS_METHOD("getEquivalentPoints"),
	GET_EQUIVALENT_POINTS_AMOUNT_METHOD("getEquivalentPointsAmount"),
	GET_AVAILABLE_CUSTOMER_TYPE_METHOD("getAvailableCustomerType"),
	GET_ALL_CUSTOMER_TYPES_METHOD("getAllCustomerTypesWithParent"),
	GET_ALL_MERCHANTCODES_FOR_OFFER_METHOD("getAllMerchantCodesForOffer"),

	PUBLISH_LIFETIME_SAVINGS_EVENT("publishLifetimeSavingsEvent"),
		
	CREATE_PARTNER_ACTIVITY_CODE_DESC("create partner activity "),
	UPDATE_PARTNER_ACTIVITY_CODE_DESC("update partner activity "),
	MEMBER_ACCRUAL_FOR_RECHARGE_DESC("get accrual transactions for member "),
	RULES_CHECK_DESC("rules check for offers from decision manager "),
	CHECK_PARTNER_EXISTS_DESC(" check if partner exists"), 
	GET_PROGRAM_ACTIVITY_LIST_DESC("get list of all program activities "),
	GET_MEMBER_DETAILS_DESC("get details of a member using account number "),
	GET_ELIGIBLE_PAYMENT_METHODS_DESC("get eligiblepayment methods"),
	GET_PARTNER_ACTIVITY_CODE_DESC("getPartnerActivityCode"),
	GET_EQUIVALENT_POINTS_DESC("get equivalent points for amount "),
	GET_AVAILABLE_CUSTOMER_TYPE_DESC("get available customer types "),
	GET_ALL_CUSTOMER_TYPES_DESC("get all the customer types "),	
	
	MEMBERSHIP_CODE("membershipCode"), 
	FROM_DATE("fromDate"),
	TO_DATE("toDate"),
	TRANSACTION_TYPE("transactionType"),
	
	ACCRUAL("Accrual"),
	REDEMPTION("Redemption"),
	PURCHASE("Purchase"),
	API_STATUS("apiStatus"),
	CODE("code"),
	MESSAGE("message"),
	ERRORS("errors"), 
	VIEW_ELIGIBILITY_RESULT("viewEligibilityResult"),
	ELIGIBILITY("eligibility"),
	CUSTOMER_TYPE("customerType"), 
	
	LIFETIME_SAVINGS_EVENT("Publish lifeTimeSavings"), 
	BIRTHDAY_GIFT_EVENT("Publish BithdayGift"), 
	
	CREATE_OFFER_METHOD("createOffer"),
	VALIDATE_AND_SAVE_OFFER("validateAndSaveOffer"),
	UPDATE_SINGLE_OFFER("updateOffer"),
	VALIDATE_AND_UPDATE_OFFER("validateAndUpdateOffer"),
	LIST_ALL_OFFERS("listAdminOffers"),
	LIST_PARTNER_OFFER("listAllOffersForPartner"),
	LIST_MERCHANT_OFFER("listAllOffersForMerchant"),
	LIST_DETAIL_OFFER("getSpecificOfferDetails"),
	UPDATE_OFFER_STATUS("activateDeactivateOffer"),
	CHANGE_OFFER_STATUS("changeOfferStatus"),
	LIST_ELIGIBLE_PAYMENT_METHODS("listEligiblePaymentMethods"),
	GET_ELIGIBLE_PAYMENT_METHODS("getEligiblePaymentMethods"),
	LIST_ALL_ELIGIBLE_OFFER("listEligibleOffers"),
	GET_ELIGIBLE_OFFERS_LIST("getEligibleOfferList"),
	LIST_DETAIL_ELIGIBLE_OFFER("getDetailedOfferForMember"),
	LIST_SPECIFIC_OFFER_FOR_MEMBER("listSpecificOfferForMember"),
	MAKE_PURCHASE("validateAndSavePurchaseHistory"),
	CREATE_PURCHASE_DETAILS("createPurchaseDetails"),
	RETRIEVE_MEMBER_OFFER_LIST("listOffers"),
	CONFIGURE_ELIGIBLE_OFFER("configureEligibleOffers"),
	
	
	GET_ADMIN_OFFERS_DOMAIN("getAllOffersForAdministrator"),
	GET_PARTNER_OFFERS_DOMAIN("getAllOffersForPartner"),
	GET_MERCHANT_OFFERS_DOMAIN("getAllOffersForMerchant"),
	GET_DETAIL_OFFER_DOMAIN("getDetailedOfferPortal"),
	GET_ELIGIBILITY_MATRIX_METHOD("getEligibilityMatrix"),
	GET_ELIGIBILITY_MATRIX_DESC("getting list of all eligibility matrix "),
		
	DAILY_LIMIT("Daily limit "),
	MONTHLY_LIMIT("Monthly limit "),
	WEEKLY_LIMIT("Weekly limit "),
	ANNUAL_LIMIT("Annual limit "),
	TOTAL_LIMIT("Total limit "),
	
	EXCEEDED_FOR(" exceeded for "),
	REACHED_FOR(" reached for "),
	
	OFFER("offer"),
	ACCOUNT_OFFER("account for this offer"),
	MEMBER_OFFER("member for this offer"),
	DENOMINATION_OFFER("the denomination for this offer"),
	DENOMINATION_ACCOUNT_OFFER("the denomination for this offer at account level"),
	DENOMINATION_MEMBER_OFFER("the denomination for this offer at member level"), 
		
	DATE_FORMAT_POINTS_BANK("yyyy-MM-dd"), 
	
	BILL_PAYMENT("Bill Payment"),
	RECHARGE("Recharge"), 
	
	CASH_VOUCHER_PRODUCT("voucher"),
	DISCOUNT_COUPON_PRODUCT("discount"),
	ETISLAT_ADDON_PRODUCT("eService"),
	DEAL_VOUCHER_PRODUCT("dealVoucher"),
	BILL_PAYMENT_PRODUCT("billRecharge"),
	RECHARGE_PRODUCT("billRecharge"), 
	SUBSCRIPTION_PRODUCT("lifestyleOffer"),
	GOLD_CERTIFICATE_PRODUCT("goldCertificate"),
	
	OFFER_STATUS("OfferStatus"),
	MERCHANT_STATUS("MerchantStatus"),
	LINKED_ACTIVE_STORE("LinkedActiveStore"),
	ELIGIBLE_CUSTOMER_TYPE("CustomerTypeEligibility"),
	DOWNLOAD_LIMIT("DownloadLimit"), 
	BIRTHDAY_OFFER_PURCHASE("BirthdayOfferPurchase"),
	DAY_ELIGIBILITY("DayEligibility"),
	
	CONFIGURE_OFFER_TYPE_METHOD("configureOfferTypes"), 
	LIST_OFFER_TYPE_METHOD("listOfferType"), 
	UPDATE_OFFER_TYPE_METHOD("updateOfferType"), 
	CONFIGURE_SUBCATGEORIES_METHOD("configureSubCategories"), 
	LIST_SUBCATEGORY_METHOD("listSubCategory"),
	
	CONFIGURE_BILLNG_DOMAIN_METHOD("saveUpdateBillingDetails"),
	CONFIGURE_OFFER_PURCHASE_DOMAIN_METHOD("saveUpdateOfferPurchaseDetails"), 
	CONFIGURE_CUSTOMER_SEGMENT_DOMAIN_METHOD("saveUpdateCustomerSegment"), 
	    
    OTHER_PURCHASE_ITEM("Other"),
    WELCOME_GIFT_ITEM("welcomeGift"),
    LIFESTYLE_OFFER_ITEM("lifestyleOffer"),
    
    NEW_STATUS("New"), 
        
    GET_PAYMENT_RESPONSE_FOR_OFFER_METHOD("getPaymentResponseForOffer"),
    
    FAILED("Failed"), 
    PAYMENT("Payment"),
    
    CONFIGURE_PURCHASE_PAYMENT_METHOD("configurePurchasePaymentMethod"),
    LIST_PURCHASE_PAYMENT_METHOD("listPurchasePaymentMethods"),
    UPDATE_PURCHASE_PAYMENT_METHOD("updatePurchasePaymentMethod"), 
    LIST_ALL_TRANSACTIONS("listAllTransactions"), 
    
    FETCH_TRANSACTION_LIST_METHOD("getTransactionDetails"), 
    ALL("ALL"), 
    
    EMPTY_JSON_STRING("[]"), 
    
    SET_ERROR_DATA_METHOD("setErrorData"), 
    OFFER_COUNTER("Offer"),
    OFFER_DENOMINATION("Offer denomination"),
    ACCOUNT_OFFER_COUNTER("AccountOffer"),
    ACCOUNT_OFFER_DENOMINATION("Account offer denomination"),
    MEMBER_OFFER_COUNTER("MemberOffer"), 
    MEMBER_OFFER_DENOMINATION("Member offer denomination"),
    ACCOUNT_COUNTER("Account"),
	MEMBER_COUNTER("Member"),
	DENOMINATION_COUNTER("Denomination"),
    
    PAYMENT_RESPOSNE_BILL_METHOD("getPaymentResponseForBill"),
    
    GET_OFFER_RESPONSE_METHOD("getOfferResponse"), 
    UPDATE_OFFERS_CONTROLLER_METHOD("updateOffers"), 
    
    MEMBER_HASH_MAP_NAME("memberResponse"), 
    
    GET_MEMBER_FULL_DETAILS_DESC("get member details with the eligibility matrix "),
    GET_MEMBER_FULL_DETAILS_METHOD("getMemberDetails"), 
    
    MAKE_PURCHASE_CONTROLLER("createPurchaseDetails"), 
    PROMOTIONAL_GIFT_CONTROLLER("promotionalGiftRequest"),
    OFFER_CATALOG_BULK_UPDATE("updateOfferCatalog"),
    
    PATH_PARAM_FOR("Value of path variable "),
    
    //Offer rating constants:
    OFFER_RATING_FOR_NEW_ACCOUNT("NewAccount"),
    OFFER_RATING_FOR_EXISTING_ACCOUNT("ExistingAccount"), 
    
    VALIDATE_AND_SAVE_OFFER_RATING("validateAndSaveOfferRating"),
    RATE_OFFERS("rateOffer"),
    SAVE_OFFER_RATING_REPOSITORY_METHOD("saveUpdateOfferRating"), 
    
    UPDATE_OFFER_RATING_REPOSITORY_METHOD("setOfferRating"),
    PUBLISH_BIRTHDAY_GIFT_EVENT("publishBirthdayGiftEvent"), 
    
    GET_BIRTHDAY_ACCOUNT_DETAILS("getBirthdayAccountDetails"), 
    REQUEST("REQUEST"),
    RESPONSE("RESPONSE"),
    VARIABLE("VARIABLE"), 
    
    OFFER_STATUS_CHECK("OfferStatus"),
    MERCHANT_STATUS_CHECK("MerchantStatus"),
    LINKED_STORE_CHECK("LinkedStore"),
    PORTAL_CHECK("AvailableInPortal"),
    OFFER_EXPIRY_CHECK("OfferExpiry"),
    CUSTOMER_ELIGIBILITY_CHECK("CustomerTypeEligibility"),
    CUSTOMER_SEGMENT_ELIGIBILITY_CHECK("CustomerSegmentEligibility"),
    
    ERROR_OCCURED("Error occured"), 
    
    SAVE_GOLD_CERTIFICATE_REPOSITORY_METHOD("saveUpdateGoldCertificate"), 
    SMILES("SMILES"), 
    
    TRUE("true"), 
    
    BOGO_MESSAGE_START_EN("Dear "),
    BOGO_MESSAGE_END_EN(", Did you know that you could have gotten your discount voucher purchase for free? Click here to subscribe to one of Unlimited Buy 1 Get 1 packages and enjoy all discount vouchers for free from now on!"),
    BOGO_MESSAGE_START_AR("، هل تعلم أنه يمكنك الحصول على قسيمة الخصم الخاصة بك مجاناً؟ اضغط هنا للاشتراك في إحدى "),
    BOGO_MESSAGE_END_AR("، هل تعلم أنه يمكنك الحصول على قسيمة الخصم الخاصة بك مجاناً؟ اضغط هنا للاشتراك في إحدى باقات \"اشترِ 1 واحصل على الثاني مجاناً\" والاستمتاع بجميع قسائم الخصم مجاناً بدءاً من الآن!"), 
    
    UPDATE_PARTNER_ACTIVITY_METHOD("updatePartnerActivity"), 
    
    ARABIC("Arabic"),
    ENGLISH("English"), 
    
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_THAN_EQUAL_TO("gte"),
    LESS_THAN_EQUAL_TO("lte"),
    EQUAL_TO("eq"),
    NOT_EQUAL_TO("neq"),
    BETWEEN_INCLUSIVE("bi"),
    BETWEEN_EXCLUSIVE("be"),
    NOT_BETWEEN_INCLUSIVE("nbi"),
    NOT_BETWEEN_EXCLUSIVE("nbe"),
    
    DURATION_UNIT_DAY("dd"),
    DURATION_UNIT_MONTH("mm"),
    DURATION_UNIT_YEAR("yy"), 
    
    DOUBLE_LINE(" ======================================= "), 
    SINGLE_LINE(" ------ "),
    OTHER_LINE(" ~~~~~~ "),
    
    PARTNER_CODE_VARIABLE("partnerCode"),
    MERCHANT_CODE_VARIABLE("merchantCode"),
    OFFER_ID_VARIABLE("offerId"),
    ACCOUNT_NUMBER_VARIABLE("accountNumber"),
    CHANNEL_ID_VARIABLE("channelId"),
    PURCHASE_ITEM_VARIABLE("purchaseItem"),
    
    CHECK_IS_SUBSCRIBED_METHOD("checkIsSubscribed"), 
    BIRTHDAY_OFFER_METHOD("listMemberBirthdayGiftOffers"), 
    CREATE_CUSTOMER_SEGMENT_METHOD("createCustomerSegment"), 
    RESET_ALL_OFFER_COUNTERS_METHOD("resetAllCounters"),
    
    LIMIT("Limit"),
    COUNTER("Counter"), 
    CERTIFICATE_PREFIX("GOLD_CERT_"), 
    
    UPDATE_OFFER_STATUS_METHOD("updateOfferStatus"), 
    
    
    EMPTY_STRING(" "),
	HYPHEN_OPERATOR("-"),
	
	FOR("for "),
		
    WISHLIST_PREF("WISH_"),
	
    INPUT_PARAM("Input parameter "),
	OUTPUT_PARAM("Output parameter "),
	
	CONFIGURE_WISHLIST_CONTROLLER("configureWishlist"), 
	GET_WISHLIST_CONTROLLER("getFromWishlist"),
	
	SAVE_WISHLIST_DOMAIN("validateAndSaveOfferToWishlist"),
	UPDATE_WISHLIST_DOMAIN("validateAndRemoveOfferFromWishlist"),
	GET_WISHLIST_DOMAIN("getActiveOffersFromWishlist"), 
	
	GET_ACTIVE_WISHLIST_OFFERS_HELPER("getActiveWishlistOffers"),
	
	OFFERS_LIST("offersList"),
	ACCOUNT_NUMBER("accountNumber"),
	RESULT_RESPONSE("resultResponse"),
	OFFER_CATALOG_RESULT_RESPONSE("offerCatalogResultResponse"), 
	WISHLIST_REQUEST("wishlistRequest"), 
	EXTERNAL_TRANSACTION_ID("externalTransactionId"), 
	PROGRAM("program"), 
	USERNAME("userName"), 
	MEMBER_DETAILS("memmberDetails"),
	
	ADD_ACTION("ADD"),
	REMOVE_ACTION("REMOVE"), 
	CHANNEL_ID(" channelId "),
	SUBSCRIBED(" subscribed "), 
	ES_PARTNER_CODE("ES"),
	
	DATE_SEPARATOR(":"), 
    FROM_DATE_TIME("00:00:00"),
    END_DATE_TIME("23:59:59"), 
    
    CONFIGURE_BIRTHDAY_INFO_DOMAIN_METHOD("saveUpdateBirthdayInfoDomain"), 
    CONFIGURE_BIRTHDAY_INFO_CONTROLLER("configureBirthdayInfo"), 
    RETRIEVE_BIRTHDAY_INFO_ACCOUNT_CONTROLLER("retrieveBirthdayInfoAccount"),
    RETRIEVE_BIRTHDAY_INFO_ADMIN_CONTROLLER("retrieveBirthdayInfoAdmin"),
    VALIDATE_AND_CONFIGURE_BIRTHDAY_INFO("configureBirthdayInfo"),
    GET_BIRTHDAY_INFO_FOR_ACCOUNT("getBirthdayInfoForAccount"), 
    GET_BIRTHDAY_INFO_FOR_ADMIN("getBirthdayInfoForAdmin"), 
    GET_ELIGIBLE_BIRTHDAY_OFFER_LIST("getAllEligibleBirthdayOffers"), 
    CONFIGURE_WISHLIST_DOMAIN_METHOD("saveUpdateWishlist"), 
    CONFIGURE_BIRTHDAY_TRACKER_DOMAIN_METHOD("saveUpdateBirthdayTracker"), 
    DOWNLOAD_LIMIT_FAILURE("Download limit not left for any denomination"), 
    GIVE_PROMOTIONAL_GIFT_DOMAIN_METHOD("givePromotionalGift"),
    
    
    COBRANDED_ACTIVE_STATUS("ACT"), 
    
    CASE_INSENSITIVE("i"), 
    REGEX_START("^"),
    REGEX_END("$"), 
    REGEX_STAR(".*"),
    
    CALL_TO("Call to "),
    RESPONSE_FROM("Response from "), 
    DB_AT(" db at "),
    SERVICE_AT(" service at "), 
    CALL_FOR_RECORD_COUNT_TO("Call for getting record count to "),
    RESPONSE_FOR_RECORD_COUNT_FROM("Response after getting record count response from "), 
    
    MEMBER_MANAGEMENT_SERVICE("Member Management Service"),
    MEMBER_ACTIVITY_SERVICE("Member Activity Service"),
    DECISION_MANAGER_SERVICE("Decision Manager Service"),
    PARTNER_MANAGEMENT_SERVICE("Partner Management Service"),
    POINTS_BANK_SERVICE("Points Bank Service"),
    
    PAYMENT_RESPONSE_VARIABLE("PaymentResponse"), 
    
    UTC("UTC"), 
    
    VALIDATE_AND_GIFT_OFFER_METHOD("validateAndGiftOffer"), 
    PURCHASE_REQUEST("purchaseRequest"), 
    HEADER("headers"), 
    SUB_CARD_TYPE("subCardType"), 
    FIRST_NAME_SMS("First_Name"),
    FIRST_NAME_BOGO_SUBSCRIPTION("FirstName"),
    FIRST_NAME_PUSH_NOTIFICATION("FIRST_NAME"), 
    
    PURCHASE_COUNT_VARIABLE("purchaseCount"), 
    APPLICABLE_CONVERSION_RATE("applicableRate"), 
    AMOUNT_INFO_VARIABLE("AmountInfo"), 
    CUSTOMER_SEGMENT_CHECK_REQUIRED_VARIABLE("customerSegmentCheckRequired"), 
    SEND_BIRTHDAY_GIFTS_METHOD("sendBirthdayGiftAlerts"), 
    UTC_TIME_ZONE("UTC"),
    GST_TIME_ZONE("GST"),
    
    GET_SHORT_OFFER_RESPONSE_METHOD("getShortOfferResponse"), 
    PROMO_CODE_VARIABLE("promoCodeApply"), 
    STAR_REGEX("(.*)"), 
    LIST_MEMBER_ELIGIBLE_OFFER("listEligibleOffersForMembers"), 
    GET_AND_SAVE_ELIGIBLE_OFFERS("getAndSaveEligibleOffers"), 
    SINGELTON_SETTER_FROM_DB_LOG_START("-----------------Inside setter of Singelton class : setting from DB-----------------"), 
    SINGELTON_SETTER_FROM_DB_LOG_END("-----------------Leaving setter of Singelton class : setting from DB-----------------"),
    SINGELTON_SETTER_AFTER_CHANGE_LOG_START("-----------------Inside setter of Singelton class : setting after populating DB-----------------"),
    SINGELTON_SETTER_AFTER_CHANGE_LOG_END("-----------------Leaving setter of Singelton class : setting after populating DB-----------------"),
    
    SORT_DESC_CREATED_DATE("CreatedDate"), 
    RETRIEVE_SINGLETON_OFFER("getEligibleOffersInSingleton"),
    
    ELIGIBLE_OFFER_LIST_CONTEXT("eligibleOffersList"),
    
	SAVE_ALL_OFFER_COUNTERS_METHOD("saveAllOfferCounters"),
    SAVE_OFFER_COUNTER_METHOD("saveUpdateOfferCounter"),
    SAVE_ALL_MEMBER_OFFER_COUNTERS_METHOD("saveAllMemberOfferCounters"),
    SAVE_MEMBER_OFFER_COUNTER_METHOD("saveUpdateOfferCounter"),
    SAVE_ALL_ACCOUNT_OFFER_COUNTERS_METHOD("saveAllAccountOfferCounters"),
    SAVE_ACCOUNT_OFFER_COUNTER_METHOD("saveUpdateAccountOfferCounter"), 
    COUNTER_ROLLBACK_PERFORMED("Performing Rollback for counters"),
    DB_ERROR_OFFER_COUNTER("Error while adding offer counter to db"),
    DB_ERROR_OFFER_COUNTER_LIST("Error while adding offer counter list to db"),
    DB_ERROR_ACCOUNT_OFFER_COUNTER("Error while adding account offer counter to db"),
    DB_ERROR_ACCOUNT_OFFER_COUNTER_LIST("Error while adding account offer counter list to db"),
    DB_ERROR_MEMBER_OFFER_COUNTER("Error while adding member offer counter to db"),
    DB_ERROR_MEMBER_OFFER_COUNTER_LIST("Error while adding member offer counter list to db"), 
    ELIGIBLE_OFFERS_POPULATING_AT_STARTUP("Populating eligible offers at application startup"), 
    ELIGIBLE_OFFERS_POPULATED_AT_STARTUP("Completed population eligible offers at application startup"), 
    SETTING_ELIGIBLE_OFFERS_TO_SERVLET_CONTEXT_START("Setting eligible offers to servlet context variable"),
    SETTING_ELIGIBLE_OFFERS_TO_SERVLET_CONTEXT_END("Eligible offers set to servlet context variable"), 
    FETCH_ELIGIBLE_OFFERS_FROM_SERVLET_CONTEXT("Fetching eligible offers from servlet context"), 
    FETCH_ELIGIBLE_OFFERS_FROM_DB("DB fetch required as records have been updated. Fetching eligible offers from DB."), 
    RETURNING_ELIGIBLE_OFFERS_FROM_SERVLET_CONTEXT("Returning eligible offers from servlet context"), 
    CONTEXT_UPDATE_DATE_VARIABLE("contextUpdatedDate"), 
    DB_UPDATE_DATE_VARIABLE("dbUpdatedDate"), 
    PREPAID_CUSTOMER_TYPE("PREPAID"),
    
    REDEMPTION_POINTS_TRANSACTION_TYPE("RedemptionPoints"),
    SAPP("SAPP"),
    BANNER_DATE_FORMAT("dd-MM-yyyy"),
    ADMIN_PORTAL_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    
    ;
    private final String constant;

	OfferConstants(String constant) {
		this.constant = constant;
	}

	public String get() {
		return this.constant;
	}

}

