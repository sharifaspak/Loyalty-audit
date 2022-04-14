package com.loyalty.marketplace.offers.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

/**
 * 
 * @author jaya.shukla
 *
 */
public enum OfferExceptionCodes implements IMarketPlaceCode{
	
    GENERIC_RUNTIME_EXCEPTION(23200, "Runtime Exception occured. Please refer logs for details."),
	
	//Mongo DB Exceptions	
	MONGO_WRITE_EXCEPTION(23201, "MongoDB write error while saving offer entity to database."),
	VALIDATION_EXCEPTION(23202, "ModelMapper validation error while mapping using model mapper."),
	
	//Runtime Exception in Member Management Service
	GET_MEMBER_DETAILS_RUNTIME_EXCEPTION(23203,"Error while fetching customer details in member mangement service."),
	GET_AVAILABLE_CUSTOMER_TYPES_RUNTIME_EXCEPTION(23204,"Error while fetching all avaialable customer types in member mangement service."),
	GET_CUSTOMER_TYPES_RUNTIME_EXCEPTION(23205,"Error while fetching all customer types in member mangement service."),
	GET_ELIGIBLE_PAYMENT_METHODS_RUNTIME_EXCEPTION(23206,"Error while fetching all eligible payment methods for customer type in member mangement service."),
	GET_ELIGIBILITY_MATRIX_RUNTIME_EXCEPTION(23207,"Error while fetching eligibility matrix from member mangement service."),
	GET_BIRTHDAY_DETAILS_RUNTIME_EXCEPTION(23279,"Error while getting bithday Account details in member management service."),
	
	//Rest Client Exception in Member Management Service
	GET_MEMBER_DETAILS_REST_CLIENT_EXCEPTION(23208,"Error while connecting to member management microservice to fetch customer details."),
	GET_AVAILABLE_CUSTOMER_TYPES_REST_CLIENT_EXCEPTION(23209,"Error while connecting to member management microservice to get all avaialable customer types."),
	GET_CUSTOMER_TYPES_REST_CLIENT_EXCEPTION(23210,"Error while connecting to member management microservice to fetch all customer types."),
	GET_ELIGIBLE_PAYMENT_METHODS_REST_CLIENT_EXCEPTION(23211,"Error while connecting to member management microservice to fetch all eligible payment methods for customer type."),
	GET_ELIGIBILITY_MATRIX_REST_CLIENT_EXCEPTION(23212,"Error while connecting to member management microservice to fetch eligibility matrix."),
	GET_BIRTHDAY_DETAILS_REST_CLIENT_EXCEPTION(23278,"Error while connecting to member management microservice to fetch Bithday Account details."),
	
	
	//Runtime Exception in Member Activity Service
	SAVE_PARTNER_ACTIVITY_RUNTIME_EXCEPTION(23213,"Error while creating partner actvity code in member activity service."),
	GET_PARTNER_ACTIVITY_RUNTIME_EXCEPTION(23214,"Error while retrieving partner actvity code in member activity service."),
	GET_PROGRAM_ACTIVITY_LIST_RUNTIME_EXCEPTION(23215,"Error while retrieving program actvity list code in member activity service."),
	GET_EQUIVALENT_POINTS_RUNTIME_EXCEPTION(23216,"Error while getting equivalent points code in member activity service."),
	
	
	//Rest Client Exception in Member Activity Service
	SAVE_PARTNER_ACTIVITY_REST_CLIENT_EXCEPTION(23217,"Error while connecting to create partner actvity code in member activity service."),
	GET_PARTNER_ACTIVITY_REST_CLIENT_EXCEPTION(23218,"Error while connecting to retrieve partner actvity code in member activity service."),
	GET_PROGRAM_ACTIVITY_LIST_REST_CLIENT_EXCEPTION(23219,"Error while connecting to retrieve program actvity list in member activity service."),
	GET_EQUIVALENT_POINTS_REST_CLIENT_EXCEPTION(23220,"Error while connecting to get equivalent points in member activity service."),
	
	//Runtime Exception in Partner Management Service
	PARTNER_CHECK_RUNTIME_EXCEPTION(23221,"Error while checking partner exist status in partner management service."),
	
	//Rest Client Exception in Partner Management Service
	PARTNER_CHECK_REST_CLIENT_EXCEPTION(23223,"Error while connecting to check if partner exists in partner management service."),
	
	//Runtime Exception in Decision Manager Service
	CUSTOMER_SEGMENT_CHECK_RUNTIME_EXCEPTION(23225,"Error while checking customer segment in decision manager service."),
	PROMOTIONAL_GIFT_CHECK_RUNTIME_EXCEPTION(23266,"Error while checking promotional gift in decision manager service."),
	
	//Rest Client Exception in Decision Manager Service
	CUSTOMER_SEGMENT_REST_CLIENT_EXCEPTION(23227,"Error while connecting to customer segment check in decision manager service."),
	PROMOTIONAL_GIFT_REST_CLIENT_EXCEPTION(23265,"Error while connecting to get promotional gift in decision manager service."),
	
	//Runtime Exception in Marketplace Service
	VOUCHER_DETAILS_BY_PURCHASE_ID_RUNTIME_EXCEPTION(23229,"Error while fetching voucher details for purchase id in marketplace service."),
		
	//Rest Client Exception in Marketplace Service
	VOUCHER_DETAILS_BY_PURCHASE_ID_REST_CLIENT_EXCEPTION(23230,"Error while connecting to fetch voucher details for purchase id in marketplace service."),
	
	//Runtime Exception in Points Bank Event
	LIFETIME_SAVINGS_AMQ_RUNTIME_EXCEPTION(23231, "Error occured while publishing to points bank queue for lifetime savings."),
	
	//JMS Exception in Points Bank Event
	LIFETIME_SAVINGS_AMQ_JMS_EXCEPTION(23232, "Error occured while connecting to points bank queue for lifetime savings."),
	
	//Runtime exceptions in OfferCatalogDomain
	SAVE_OFFER_DOMAIN_RUNTIME_EXCEPTION(23233,"Error occured while saving the offer entity."),
	UPDATE_OFFER_DOMAIN_RUNTIME_EXCEPTION(23234,"Error occured while updating the offer entity."),
	GET_ADMIN_OFFERS_DOMAIN_RUNTIME_EXCEPTION(23235, "Error occured while fetching offers for admin."),
	GET_PARTNER_OFFERS_DOMAIN_RUNTIME_EXCEPTION(23236, "Error occured while fetching offers for partner."),
	GET_MERCHANT_OFFERS_DOMAIN_RUNTIME_EXCEPTION(23237, "Error occured while fetching offers for merchant."),
	GET_DETAIL_OFFER_DOMAIN_RUNTIME_EXCEPTION(23238, "Error occured while fetching detail offer."),
	GET_DETAIL_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION(23239, "Error occured while fetching detail eligible offer for member."),
	GET_LIST_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION(23240, "Error occured while listing eligible offers for members."),
	GET_ELIGIBLE_PAYMENT_METHODS_DOMAIN_RUNTIME_EXCEPTION(23241, "Error occured while fetching eligbile payment mehods."),
	PURCHASE_HISTORY_DOMAIN_RUNTIME_EXCEPTION(23242, "Generic exception occured."),
	UPDATE_OFFER_STATUS_REPOSITORY_RUNTIME_EXCEPTION(23245,"Some error while updating offer status in repository."),
	
	//Runtime Exceptions in configuring master tables
	OFFER_TYPE_ADDITION_RUNTIME_EXCEPTION(23246,"Some error while saving offer type to repository."),
	OFFER_TYPE_UPDATION_RUNTIME_EXCEPTION(23247,"Some error while updating offer type in repository."),
	OFFER_TYPE_FETCH_RUNTIME_EXCEPTION(23248,"Some error while fetching offer types from repository."),
	SUBCATEGORY_ADDITION_RUNTIME_EXCEPTION(23249,"Some error while saving subcategories to repository."),
	SUBCATEGORY_FETCH_RUNTIME_EXCEPTION(23250,"Some error while fetching subcategories from repository."),
	
	//Runtime Exception in offer purchase domain
	OFFER_PURCHASE_HISTORY_ADDITION_RUNTIME_EXCEPTION(23251,"Some error while saving offer purchase to repository."),
	OFFER_PURCHASE_HISTORY_UPDATION_RUNTIME_EXCEPTION(23252,"Some error while updating offer purchase history in repository."),
		
	//Runtime Exception in marketplace activity domain
	SAVE_ACTIVITY_CODE_REPOSITORY_RUNTIME_EXCEPTION(23257,"Some error while saving activity to repository."),
	UPDATE_ACTIVITY_CODE_REPOSITORY_RUNTIME_EXCEPTION(23258,"Some error while saving activity to repository."),
	
	//Other Runtime Exceptions
	MEMBER_OFFER_LIST_RUNTIME_EXCEPTION(23259, "Error occured in retrieving list for member"),
	PAYMENT_EXCEPTION(23263, "Error occured while saving purchase details."),
	
	PURCHASE_PAYMENT_METHOD_ADDITION_RUNTIME_EXCEPTION(23264,"Some error while saving purchase payment method to repository."),
    PURCHASE_PAYMENT_METHOD_UPDATION_RUNTIME_EXCEPTION(23265,"Some error while updating purchase payment method in repository."),
    PURCHASE_PAYMENT_METHOD_FETCH_RUNTIME_EXCEPTION(23266,"Some error while fetching purchase payment method from repository."),
    
    OBJECT_CONVERSION_EXCEPTION(23267,"Failed to convert object to respective class"), 
    FETCH_TRANSACTION_DOMAIN_EXCEPTION(23268, "Some error occured while fetching purchase transactions"),
    
    PURCHASE_CONFIGURATION_EXCEPTION(23269, "Error while saving purchase details."), 
    BILLING_EXCEPTION(23270,"Error occured while saving bill payment details"),
    RECHARGE_EXCEPTION(23271,"Error occured while saving recharge details"), 
    
    JSON_PARSING_EXCEPTION(23272, "Error occured while parsing JSON"),
    GET_MERCHANT_IDS_OFFERS_DOMAIN_RUNTIME_EXCEPTION(23273, "Error occured while fetching merchants ids"),
    OFFER_RESPONSE_RUNTIME_EXCEPTION(23274, "Some error occured while getting response for offer"),
    GETTING_ROLE_EXCEPTION(23275, "Some error occured while fetching roles"),
    
    BIRTHDAY_GIFT_AMQ_JMS_EXCEPTION(23277, "Error occured while connecting to Notification queue for Birthday Gift."),
    
    //Runtime exceptions in OfferRatingDomain
  	SAVE_OFFER_RATING_DOMAIN_RUNTIME_EXCEPTION(23276,"Error occured while saving the offer rating entity."),
  	UPDATE_OFFER_RATING_DOMAIN_RUNTIME_EXCEPTION(23277,"Error occured while updating the offer rating entity."), 
  	UPDATE_OFFER_RATING_REPOSITORY_RUNTIME_EXCEPTION(23278, "Error occured while updating offer rating in offer"), 
  	
  	SAVE_GOLD_CERTIFICATE_DOMAIN_RUNTIME_EXCEPTION(23279, "Error occured while creating gold certificate entity"),
  	UPDATE_GOLD_CERTIFICATE_DOMAIN_RUNTIME_EXCEPTION(23280, "Error occured while updating gold certificate entity"),

  	BOGO_NOTIFICATION_JMS_EXCEPTION(23281, "Error occured while connecting to notification queue for send bogo subscription message to member."),
    BOGO_NOTIFICATION_EXCEPTION(23282, "Error occured while publishing to notification queue for send bogo subscription message to member."),
  	
    UPDATE_PARTNER_ACTIVITY_REST_CLIENT_EXCEPTION(23283,"Error while connecting to update partner actvity code in member activity service."),
    UPDATE_PARTNER_ACTIVITY_RUNTIME_EXCEPTION(23284,"Error while updating partner actvity code in member activity service."), 
    
    CHECK_SUBSCRIPTION_STATUS_REST_CLIENT_EXCEPTION(23285, "Error while connecting to marketplace to check account subscription status"),
    CHECK_SUBSCRIPTION_STATUS_RUNTIME_EXCEPTION(23286, "Error while checking account subscription status in marketplace"),
	 
    BIRTHDAY_INFO_ADDITION_RUNTIME_EXCEPTION(23251,"Some error while saving birthday information to repository."),
    BIRTHDAY_INFO_UPDATION_RUNTIME_EXCEPTION(23252,"Some error while updating birthday information in repository."),
    CONFIGURE_BIRTHDAY_INFO_DOMAIN_RUNTIME_EXCEPTION(23253, "Error while configuring birthday info"),
    RETRIEVE_BIRTHDAY_INFO_ACCOUNT_DOMAIN_RUNTIME_EXCEPTION(23254, "Error while retrieving birthday info for member"),
    GET_LIST_BIRTHDAY_OFFER_DOMAIN_RUNTIME_EXCEPTION(23255, "Error occured while fetching eligible birthday offers"),
    BIRTHDAY_TRACKER_ADDITION_RUNTIME_EXCEPTION(23256, "Error occured while saving birthday tracker to repository"),
    BIRTHDAY_TRACKER_UPDATION_RUNTIME_EXCEPTION(23257, "Error occured while updating birthday tracker in repository"),
    BIRTHDAY_NOTIFICATION_EXCEPTION(23258, "Error occured while sending birthday gift alerts to member"),
    RETRIEVE_BIRTHDAY_INFO_ADMIN_DOMAIN_RUNTIME_EXCEPTION(23254, "Error while retrieving birthday info for admin"),
    CONFIGURE_ELIGIBLE_OFFER_DOMAIN_RUNTIME_EXCEPTION(23255, "Error occured while configuring eligible offers"),
    SAVE_OFFER_COUNTER_EXCEPTION(23256,"Error occured while saving offer counter"),
    SAVE_OFFER_COUNTER_LIST_EXCEPTION(23257,"Error occured while saving offer counter list"),
    SAVE_MEMBER_OFFER_COUNTER_EXCEPTION(23258,"Error occured while saving member offer counter"),
    SAVE_MEMBER_OFFER_COUNTER_LIST_EXCEPTION(23259,"Error occured while saving member offer counter list"),
    SAVE_ACCOUNT_OFFER_COUNTER_EXCEPTION(23260,"Error occured while saving account offer counter"),
    SAVE_ACCOUNT_OFFER_COUNTER_LIST_EXCEPTION(23261,"Error occured while saving account offer counter list"),
    FAILED_TO_CONNECT_MM_DB(23262, "Failed to establish connection with MM DB"),
   
    GET_PROMOTIONAL_GIFT_DOMAIN_RUNTIME_EXCEPTION(23263, "Error occured while gifting promotional gift"),
    GET_PROMOTIONAL_GIFT_DOMAIN_MARKETPLACE_EXCEPTION(23264, "Marketplace error "),
    
    //Wishlist Exception Codes
    WISHLIST_MONGO_WRITE_EXCEPTION(2775, "Mongo DB write error while saving offer entity to database."),
    WISHLIST_VALIDATION_EXCEPTION(2776, "Model Mapper validation error while mapping using model mapper."),
	ADD_TO_WISHLIST_DOMAIN_RUNTIME_EXCEPTION(2779,"Error occured while saving offer to wishlist."),
	REMOVE_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION(2780,"Error occured while removing offers to wishlist."),
	GET_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION(2780,"Error occured while getting offers from wishlist."),  
	WISHLIST_ADDITION_RUNTIME_EXCEPTION(2781, "Error occured while adding offer to wishlist repository"), 
	WISHLIST_UPDATION_RUNTIME_EXCEPTION(2782, "Error occured while removing offer from wishlist in repository"), 
	 
	RUNTIME_EXCEPTION(23265, "Runtime exception occured"),
	GET_LIFETIME_SAVINGS_DETAILS_REST_CLIENT_EXCEPTION(23266, "Error in connecting to points bank service"), 
	RESTAURANTS_REST_CLIENT_EXCEPTION(23266, "Error in connection to fetch restaurant list"),
	
    ;
	

	private final int id;
	private final String msg;

	OfferExceptionCodes(int id, String msg) {
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
