package com.loyalty.marketplace.offers.constants;

/**
 * 
 * @author jaya.shukla
 *
 */
public enum OfferSuccessCodes {

	// API Status Codes
	STATUS_SUCCESS(0, "Success"),
		
	// Offer Creation
	CASH_VOUCHER_CREATED(23951, "Cash voucher created successfully"),
	DISCOUNT_OFFER_CREATED(23952, "Discount offer created successfully"),
	DEAL_VOUCHER_CREATED(23953, "Deal voucher created successfully"),
	ETISALAT_COMS_CREATED(23954, "Etisalat add on for COMS provisioning channel created successfully"),
	ETISALAT_RTF_CREATED(23955, "Etisalat add on for RTF  provisioning channel created successfully"),
	ETISALAT_EMCAIS_CREATED(23956, "Etisalat add on for EMCAIS  provisioning channel created successfully"),
		
	// Offer Status
	OFFER_ACTIVATED(23957, "Offer activated successfully"),
	OFFER_DEACTIVATED(23958, "Offer deactivated successfully"),
	
	//Eligible payment methods
	PAYMENT_METHODS_LISTED_SUCCESSFULLY(23959, "Eligible payment methods listed successfully."),
	
	//Offer Updation	
	CASH_VOUCHER_UPDATED(23960, "Cash voucher updated successfully"),
	DISCOUNT_OFFER_UPDATED(23961, "Discount offer updated successfully"),
	DEAL_VOUCHER_UPDATED(23962, "Deal voucher updated successfully"),
	ETISALAT_COMS_UPDATED(23963, "Etisalat add on for COMS provisioning channel updated successfully"),
	ETISALAT_RTF_UPDATED(23964, "Etisalat add on for RTF provisioning channel updated successfully"),
	ETISALAT_EMCAIS_UPDATED(23965, "Etisalat add on for EMCAIS provisioning channel updated successfully"),
	
	// Listing Offers
	OFFERS_LISTED_SUCCESSFULLY(23966, "Offers listed successfully."),
	OFFERS_LISTED_FOR_PARTNER_SUCCESSFULLY(23967, "Offers listed for the partner successfully."),
	OFFERS_LISTED_FOR_MERCHANT_SUCCESSFULLY(23968, "Offers listed for the merchant successfully."),
	SPECIFIC_OFFER_PORTAL_LISTED_SUCCESSFULLY(23969, "Specific offer details listed successfully."),
	OFFERS_LISTED_FOR_MEMBER_SUCCESSFULLY(23970, "Offers listed for the member successfully."),
	ELIGIBLE_OFFERS_LISTED_SUCCESSFULLY(23971,"Eligible offers listed successfully"),
		
	// Static tables
	PAYMENTMETHOD_ADDED_SUCCESSFULLY(23972, "Payment method added successfully."),
	OFFERTYPE_ADDED_SUCCESSFULLY(23973, "Offer type added successfully."),
	CATEGORY_ADDED_SUSSESSFULY(23974, "Category added successfully."),
	SUBCATEGORY_ADDED_SUSSESSFULY(23975, "Subcategory added successfully."),
	DENOMINATION_ADDED_SUSSESSFULY(23976, "Denomination added successfully."),
		
	// Fetching Static Table Data 
    PAYMENT_METHOD_LISTED_SUCCESSFULLY(23977,"Payment methods listed successfully"),
    OFFER_TYPE_LISTED_SUCCESSFULLY(23978,"Offer types listed successfully"),
	DENOMINATION_LISTED_SUCCESSFULLY(23979,"Denominations listed successfully"),
	CATEGORY_LISTED_SUCCESSFULLY(23980,"Category listed successfully"),
	SUBCATEGORY_LISTED_SUCCESSFULLY(23981,"Subcategory listed successfully"),
		
	//Static table OfferType updation
    OFFERTYPE_UPDATED_SUCCESSFULLY(23982,"OfferType updated successfully"),
        
    // Offer purchase
    OFFER_PURCHASED_SUCCESSFULLY(23983,"Offer purchased successfully"),
    BILL_PAYMENT_COMPLETED_SUCCESSFULLY(23984,"Bill payment completed successfully"),
    RECHARGE_COMPLETED_SUCCESSFULLY(23985,"Recharge completed successfully"), 
        
    DETAILED_ELIGIBLE_OFFER_DISPLAYED_SUCCESSFULLY(23986,"Detailed eligible offer displayed successfully"),
    DETAILED_MEMBER_OFFER_DISPLAYED_SUCCESSFULLY(23987,"Detailed eligible offer for member displayed successfully"),
    
    PURCHASE_PAYMENT_METHOD_ADDED_SUCCESSFULLY(23988,"Purchase payment method added successfully"),
    PURCHASE_PAYMENT_METHODS_LISTED_SUCCESSFULLY(23989,"Purchase payment method listed successfully"),
    PURCHASE_PAYMENT_METHODS_UPDATED_SUCCESSFULLY(23990,"Purchase payment method updated successfully"),
    
    TRANSACTIONS_FETCHED_SUCCESSFULLY(23991, "Purchase transactions fetched successully"),
    
    OFFER_RATING_SUCCESS(23992, "Offer rated successfully"),
    
    BATCH_PROCESS_FOR_BIRTHDAY_OFFERS_SUCCESSFULLY(24093, "Batch Process for Birthday Gift Alert"), 
    
    GOLD_CERTIFICATE_CREATED(24094, "Gold certificate created successfully"),
    GOLD_CERTIFICATE_UPDATED(24095, "Gold certificate updated successfully"), 
    ETISALAT_PHONEY_TUNES_CREATED(24096, "Etisalat add on for PHONEYTUNES provisioning channel created successfully"),
    ETISALAT_PHONEY_TUNES_UPDATED(24097, "Etisalat add on for PHONEYTUNES provisioning channel updated successfully"),
    ETISALAT_RBT_CREATED(24098, "Etisalat add on for RBT provisioning channel created successfully"),
    ETISALAT_RBT_UPDATED(24099, "Etisalat add on for RBT provisioning channel updated successfully"),
    GOLD_CERTIFICATE_PURCHASED_SUCCESSFULLY(24100,"Gold Certificate purchased successfully"),
    BIRTHDAYGIFT_OFFERS_LISTED_FOR_ACCOUNT_SUCCESSFULLY(24101, "Birthday Gift Offers listed for the member successfully."), 
    CUSTOMER_SEGMENT_CREATED_SUCCESSFULLY(24102, "Customer segment created successfully"),
    
    //Wishlist Codes
    OFFER_ADDED_SUCCESSFULLY_TO_WISHLIST(2700,"Offer added successfully to your wishlist."),
	OFFER_REMOVED_SUCCESSFULLY_FROM_WISHLIST(2701,"Offer removed successfully from your wishlist."),
	OFFER_DISPLAYED_SUCCESSFULLY_FROM_WISHLIST(2702,"Offer(s) fetched successfully from your wishlist."),
    
	//Birthday Info Codes
	BIRTHDAY_INFO_CONFIGURED_SUCCESSFULLY(24103,"Birthday information configured successfully"),
	BIRTHDAY_INFO_RETRIEVED_SUCCESSFULLY_FOR_ACCOUNT(24104,"Birthday information retrieved successfully for account"),
	BIRTHDAY_INFO_RETRIEVED_SUCCESSFULLY_FOR_ADMIN(24105,"Birthday information retrieved successfully for admin"),
	
	ELIGIBLE_OFFERS_CONFIGURATION_SUCCESSFULL(24106, "Refreshing eligible offers. Please try after 2 minutes."), 
	SINGLETON_RECORDS_RETRIEVED_SUCCESSFULLY(24107, "Eligible offers in singleton class fetched successfully"),
	PROMOTIONAL_VOUCHER_SUCCESS(24108, "Promotional gift voucher given successfully"),
	PROMOTIONAL_SUBSCRIPTION_SUCCESS(24109, "Promotional gift subscription given successfully"),
	PROMOTIONAL_BOTH_GIFT_SUCCESS(24109, "Promotional gift voucher and subscription given successfully"),
	PROMOTIONAL_GIFT_SUBSCRIPTION_EXISTS_VOUCHER_GIFTED(24110, "Voucher gifted successfully but subscription not given as account already subscribed"),
	
	PROMOTIONAL_GIFT_SUBSCRIPTION_FAILED_VOUCHER_GIFTED(24111, "Failed to gift subscription but voucher gifted successfully"),
	PROMOTIONAL_VOUCHER_GIFT_FAILED_SUBSCRIPTION_GIFTED(24112, "Failed to gift voucher but subscription gifted successfully"),
	PROMOTIONAL_GIFT_SUCCESS(24110, "Promotional gift given successfully"),
	
	RESET_ALL_COUNTER_SUCCESS(24114, "Reset All Counter Success"),
	
    FILE_PROCESSED_SUCCESSFULLY(24115, "File Processed Successfully"),
	
	REFUND_TRANSACTIONS_FETCHED_SUCCESSFULLY(24120, "Refund transactions fetched successully"),
	
	RESTAURANTS_FETCHED_SUCCESSFULLY(24121, "Restaurants listed successfully."),

	;
	
	private final int id;

	private final String msg;

	OfferSuccessCodes(int id, String msg) {
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
