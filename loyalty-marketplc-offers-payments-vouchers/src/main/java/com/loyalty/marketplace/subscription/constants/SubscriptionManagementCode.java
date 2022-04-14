package com.loyalty.marketplace.subscription.constants;

/**
 * The SubscriptionManagementCode should contain all the Business, Exception and Error handling codes 
 * used through the MarketPlace micro-service.
 * 
 * @author siddharth.panda
 */
public enum SubscriptionManagementCode {
	
	// API Status Codes
	STATUS_SUCCESS(0, "Success"), 
	STATUS_FAILURE(1, "Failed"),
	
	// Invalid parameters Codes
	INVALID_HEADERS(2400, "Invalid input headers"),
	INVALID_PARAMETERS(2401, "Invalid input parameters"),
	INVALID_PROGRAM_CODE(2402, "Incorrect Program. This pack was created for a different program."),
	INVALID_ACTION(2403, "Invalid input parameter : Action"),
	INVALID_DATE(2404, "Invalid Date. Accepted Format : (dd/MM/yyyy)"),
	INVALID_CHARGEABILITY_TYPE(2405, "Invalid chargeability type. Accepted Values : (one-time, auto-renewable, life-time)"),
	INVALID_PAYMENT_METHOD(2406, "Invalid payment method."),
	INVALID_CUSTOMER_SEGMENT(2407, "Invalid Customer segment."),
	INELIGIBLE_PAYMENT_METHOD(2408, "Payment method not eligible."),
	INELIGIBLE_CUSTOMER_SEGMENT(2409, "Customer segment not eligible."),
	INELIGIBLE_CHANNEL(2410, "ChannelId not eligible."),
	INVALID_START_DATE(2411, "Start Date cannot be after End Date."),
	INVALID_OFFER(2412, "Invalid OfferID. Unable to link."),
	INVALID_MEMBERSHIP_CODE(2413, "Invalid Membership Code. Please pass a membership code associated with your account."),
	MANDATORY_DISCOUNT_MONTHS(2414,"Discount Months is mandatory for Auto-Renewable Subscriptions"),
	ACCOUNT_INELIGIBLE_FOR_PAYMENT_METHOD(2415,"Account is not eligible for this payment method"),
	INVALID_SUBSCRIPTION_SEGMENT(2416,"Invalid subscription segment. Accepted Values : (lifestyle, food, combo)"),
	SPENT_AMOUNT_MANDATORY(2417,"Spent Amount is Mandatory for fullCreditCard and DCB"),
	SPENT_POINTS_MANDATORY(2418,"Spent Points is Mandatory"),
	
	
	
		
	//Subscription Catalog
	SUBSCRIPTIONCATALOG_CREATED(2420,"Subscription Catalog Created successfully"),
	SUBSCRIPTIONCATALOG_CREATION_FAILED(2421,"Subscription Catalog Creation Failed"),
	SUBSCRIPTIONCATALOG_EXISTS(2422,"Subscription Catalog Already exists"),
	SUBSCRIPTIONCATALOG_UPDATED(2423,"Subscription Catalog Updated succesfully"),
	SUBSCRIPTIONCATALOG_UPDATION_FAILED(2424,"Subscription Catalog Updation failed"),
	SUBSCRIPTIONCATALOG_NOT_FOUND(2425,"No Subscription Catalog Found"),
	SUBSCRIPTIONCATALOG_DELETED(2426,"Subscription Catalog Deleted succesfully"),
	SUBSCRIPTIONCATALOG_DELETION_FAILED(2427,"Subscription Catalog Deletion failed"),	
	SUBSCRIPTIONCATALOG_LISTED_SUCCESSFULLY(2428,"Subscription Catalog Fetched succesfully"),
	SUBSCRIPTIONCATALOG_LISTING_FAILED(2429,"Subscription Catalog Fetch failed"),
	SUBSCRIBTIONCATALOG_ACTIVE(2430, "Subscription Catalog Active."),
	SUBSCRIBTIONCATALOG_INVALID(2431, "Invalid Subscription Catalog status. Accepted Values : (Active, Inactive)"),
	SUBSCRIPTIONPROMOTION_CREATED(2432,"Timed Promotion Created successfully."),
	SUBSCRIPTIONPROMOTION_CREATION_FAILED(2433,"Timed Promotion Creation Failed."),
	GENERATE_PROMOCODE_FAILED(2434,"Generate Promocode Failed."),
	SUBSCRIPTIONCATALOG_FREE_NOT_FOUND(2435,"No Free Subscription Catalog Found."),
	SUBSCRIBTIONCATALOG_NOT_ACTIVE(2436, "Subscription Catalog is not Active."),
	MULTIPLE_FREE_SUBSCRIPTION_FOUND(2437,"Multiple Free Subscription Catalog Found."),
	SUBSCRIPTION_METHOD_NOT_APPLICABLE(2438,"Subscription Method not applicable for this transaction."),
	SUBSCRIPTIONCATALOG_FREE(2439,"This is a free subscription. Selected Option should be Free."),
	
	
	//Subscription
	SUBSCRIPTION_CREATED(2440,"Subscription Completed Successfully"),
	SUBSCRIPTION_CREATION_FAILED(2441,"Subscription Failed"),
	SUBSCRIPTION_EXISTS(2442,"Subscription Already Exists"),
	SUBSCRIPTION_UPDATED(2443,"Subscription Updated successfully"),
	SUBSCRIPTION_UPDATION_FAILED(2444,"Subscription Updation Failed"),
	SUBSCRIPTION_NOT_FOUND(2445,"No Subscription Found"),
	SUBSCRIBTION_AUTO_NOT_FOUND(2446,"No Auto-Renewable Subscription found."),
	SUBSCRIPTION_DELETED(2447,"Subscription Deleted successfully"),
	SUBSCRIPTION_DELETION_FAILED(2448,"Subscription Deletion Failed"),	
	SUBSCRIPTION_LISTED_SUCCESSFULLY(2449,"Subscription Fetched successfully"),
	SUBSCRIPTION_LISTING_FAILED(2450,"Subscription Fetch Failed"),
	SUBSCRIBTION_ACTIVE(2451, "Subscription Active."),
	SUBSCRIBTION_INVALID(2452, "Invalid Subscription status. Accepted Values : (Subscribed, Unsubscribed)"),
	SUBSCRIPTION_CANCELLED(2453,"Subscription Cancelled successfully"),
	SUBSCRIPTION_CANCEL_FAILED(2454,"Subscription Cancellation Failed"),
	SUBSCRIPTION_PARKED(2455,"Subscription Parked succesfully"),
	SUBSCRIPTION_PARK_FAILED(2456,"Subscription Parking Failed"),
	SUBSCRIPTION_ACTIVATED(2457,"Subscription Activated succesfully"),
	SUBSCRIPTION_ACTIVATION_FAILED(2458,"Subscription Activation Failed"),
	MULTIPLE_SUBSCRIPTION_FOUND(2459,"Multiple Subscriptions Found"),
	SUBSCRIPTION_EXTENDED(2460,"Subscription Extended Successfully"),
	SUBSCRIPTION_EXTENSION_FAILED(2461,"Subscription Updation Failed"),
	SUBSCRIPTION_PAYMENT_RENEWAL_FAILED(2462,"Renewal of Payment Method Failed"),
	SUBSCRIPTION_PAYMENT_METHOD_ALREADY_EXISTS(2463,"Payment Method already exists"),
	SUBSCRIPTION_PAYMENT_CCDETAILS_MISSING(2464,"Credit Card Details are missing."),
	SUBSCRIPTION_PAYMENT_CCDETAILS_ALREADY_EXISTS(2465,"Credit Card Details already exists."),
	SUBSCRIPTION_INVALID_PAYMENT_METHOD(2466,"Invalid Payment Method"),
	SUBSCRIPTION_RENEWAL_PAYMENT_METHOD_SUCCESS(2467,"Subscription Payment Method Renewal Completed Successfully."),
	SUBSCRIPTION_RENEWAL_INVALID_SUBSCRIPTION_STATUS(2468,"Invalid Subscription Status"),
	
	//Extras
	PHONY_TUNES_CONNECTION_ERROR(2480,"Phony Tunes Connection Error"),
	NO_ACTIVITY_CODE_FOUND(2481,"Activity Code is NULL"),
	PAYMENT_FAILED(2482,"Payment Failed : Transaction ID is Null"),
	INSUFFICIENT_POINTS(2483,"Insufficient Points : Add more points to Subscribe this Pack"),
	GET_OFFERS_REST_CLIENT_EXCEPTION(2484,"Error while connecting to offer microservice to get avaialable offerId."),
	GET_CRMCALL_REST_CLIENT_EXCEPTION(2485,"Error while connecting to member microservice to get CRM."),
	CRM_DISABLED_EXCEPTION(2486,"Account is not eligible for this payment method"),
	NON_ETISALAT_AUTO_EXCEPTION(2487,"Only Etisalat Customers are eligible for Auto-Renewal Subscription without Credit Card"),
	ACCOUNT_SUBSCRIBED(2488,"Account Subscribed for Another Subscription"),
	ACCOUNT_NOT_ELIGIBLE_FOR_WELCOME_GIFT(2489,"Account is not eligible for Welcome Gift Subscription"),
	ACCOUNT_PARKED(2490,"Account Parked for Another Subscription"),
	PHONY_TUNES_ERROR(2491,"Phony Tunes Failed"),
	INVALID_PROMO_CODE(2492,"Invalid Promo Code"),
	ONE_TIME_SUBSCRIBED(2493,"Account Already Subscribed to One-Time Subscription"),
	AUTO_RENEWABLE_SUBSCRIBED(2494,"Account Already Subscribed to Auto-Renewable Subscription"),
	LIFE_TIME_SUBSCRIBED(2495,"Account Already Subscribed for Lifetime Subscription"),
	INVALID_CARD_TYPE(2496, "Invalid cardType"),
	SUBSCRIPTIONCATALOG_NOT_CONFIGURED_FOR_CARDTYPE(2497,"Subscription Catalog Not Configured For Card Type."),
	FOOD_SUBSCRIBED(2498,"Account Already Subscribed to Food Subscription"),
	EMAIL_ERROR(2499,"Error while sending email/sms"),
	FILE_CONTENT_SAVE_FAILED(2500,"Saving failure response to File Content failed"),
	NO_MASTER_EPG_TRANSACTION_ID_FOUND(2500,"Master EPG Transaction Id is required"),
	SUBSCRIPTION_CATALOG_ID_DUPLICATE(2501,"Subscription Catalog with input id already exists"),
	SUBSCRIPTIONCATALOG_NOT_CONFIGURED(2502,"Subscription Catalog Not Configured."),
	INVALID_MERCHANT_CODE(2503,"Invalid Merchant Code"),
	INVALID_CUISINE_CODE(2504,"Invalid Cuisine Code"),
	
	//Subscription Renewal
    SUBSCRIPTION_RENEWAL_LISTING_FAILED(2600,"Subscription Renewal Listing Failed"),

	
		
	// Section for Exception and Error Codes
	GENERIC_RUNTIME_EXCEPTION(2000, "Runtime Exception occured. Please refer logs "),
	PROPERTIES_FILE_IO_ERROR(2001, "I/O exception while calling properties file"),
	PROPERTIES_FILE_NOT_FOUND(2002, "Properties file not found Exception"),
	MONGO_DB_CONNECTION_ERROR(2003, "Failed to connect to Mongo DB"),
	REST_CLIENT_EXCEPTION(2004, "REST client Exception"),
	AMQ_CONNECTION_EXCEPTION(2005, "AMQ - Error connecting to AMQ exchange"),
	AMQ_SUBSCRIPTION_EXCEPTION(2006, "AMQ - Error subscribing to topic"),
	AMQ_JSON_PARSER_EXCEPTION(2007, "AMQ - Error parsing json msg from topic"),
	MODEL_MAPPER_EXCEPTION(2008, "MongoDB persist exception occured while saving"),
	MONGO_WRITE_EXCEPTION(2009, "MongoDB persist exception occured while saving"),
	
	
	CUISINES_LISTED_SUCCESSFULLY(2010,"Cuisines Fetched successfully"),
	CUISINES_LISTING_FAILED(2011,"Cuisines Fetch Failed"),
	CUISINES_LIST_EMPTY(2012,"Cuisines List is Empty"),
	ACCOUNTNUMBER_IS_MANDATORY(2013,"Invalid Input Parameters.AccountNumber is Mandatory"),
	AUTORENEWABLE_ELIGIBLE_PROMOTYPE(2014,"Only Free Duration Promotype is eligible for AutoRenewable Catalog"),
	ONETIME_NOT_ELIGIBLE_PROMO_TYPE(2015, "One-Time Catalog is not eligible for Free Duration Promo Type"),
	GIFT_DETAILS_FETCHED_SUCCESSFULLY(2016, "Gift Details fetched Successfully"),
	REJECT_AUTO_WHEN_ONE_TIME_SUBSCRIBED(2017, "You are already subscribed. Expiry Date is "),
	MEMBER_DETAILS_NOT_FOUND(2018,"Member details not found"),
	ACCOUNT_NOT_ELIGIBLE_FOR_GIFT(2019,"Account is not eligible for the gift"),
	
	
	;
	
	private final int id;
	private final String msg;

	SubscriptionManagementCode(int id, String msg) {
		this.id = id;
	    this.msg = msg;
	}

	public int getIntId() {
		return this.id;
	}
	  
	public String getId() {
		return Integer.toString(this.id);
	}

	public String getMsg() {
		return this.msg;
	}
	  
}
