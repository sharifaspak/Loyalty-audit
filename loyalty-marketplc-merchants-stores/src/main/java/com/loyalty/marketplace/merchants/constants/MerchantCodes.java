package com.loyalty.marketplace.merchants.constants;

public enum MerchantCodes {

	// API Status Codes
	STATUS_SUCCESS(0, "Success"), 
	STATUS_FAILURE(1, "Failed"),

	GET_MERCHANT_NAME_SUCCESS(2111, "Merchant name retrieved successfully."),
	GET_MERCHANT_NAME_FAILED(2112, "Merchant name retrieval failed."),
	NO_MERCHANT_FOUND(2113, "No merchant found for merchant code: "),
	NO_MERCHANT_NAME_POPULATED(2114, "Merchant name not populated in DB for merchant code: "),
	
	// Invalid parameters Codes
	INVALID_PARAMETERS(2121, "Invalid input parameters"),
	INVALID_EMAIL(2122, "Invalid Email"), 
	FIRST_NAME_CANNOT_BY_EMPTY(2123, "First Name cannot be empty"),
	LAST_NAME_CANNOT_BY_EMPTY(2124, "Last Name cannot be empty"), 
	INVALID_MOBILE_NUMBER(2125, "Invalid Mobile Number"),
	INVALID_FAX_NUMBER(2126, "Invalid Fax Number"), 
	INVALID_DATE(2127, "Invalid date.Should be in DD-MM-YYYY format"),
	INVALID_CATEGORY(2128,"Invalid Category Id"),
	INVALID_BARCODE(2129,"Invalid Barcode Id"),
	
	
	// Contact person Creation
	CONTACT_PERSON_CREATED(2130, "Merchant Contact persons created"),
	CONTACT_CREATION_FAILED(2131, "Contact Person Creation Failed"),
	CONTACT_PERSON_EXISTS(2132, "Contact already exists"),
	EITHER_STORECODE_MERCHANTCODE_MANDATORY(2133,"Either Merchant Code to create merchant contact or storeCode and Merchant Code  to create store contact is Mandatory."),
	CREDENTIALS_SAVE_TO_LDAP_FAILED(2134, "Failed to send credentials to LDAP"),
	CREDENTIALS_MAIL_TO_CONTACT_FAILED(2135, "Failed to mail credentials to contact person"),
	MERCHANT_NOT_AVAIABLE(2136, "Merchant not avaiable to create Contact Person"),
	INVALID_USER_NAME(2137, "Username cannot be Null or Empty to update Contact person"),
	USERNAME_NOT_FOUND(2138, "Username not found to update contact person"),
	EMAILID_EXITS(2139,"Email ID already Exists in DB"),

	// Merchant Creation
	MERCHANT_CREATED(2140, "Merchant created successfully"),
	MERCHANT_CREATION_FAILED(2141, "Failed to create Merchant"),
	MERCHANT_EXISTS(2142, "Merchant already exists"),
	INVALID_MERCHANT_CODE(2143,"Invalid merchant code. Merchant code can contain only 5 or less characters, with no whitespaces and special characters."),
	INVALID_PARTERCODE(2144,"No Partner Available with the Partner Code"),
	ERROR_FETCH_PARTERCODE(2145,"Unable to fetch the Partner Code"),
	
	UPDATE_ELIGIBLE_OFFERS_FAILED(2146, "Failed to update eligible offers"),

	// Merchant Status

	MERCHANT_ACTIVATED(2150, "Merchant activated successfully"),
	MERCHANT_DEACTIVATED(2151, "Merchant deactivated successfully"),
	MERCHANT_ACTIVATED_ALREADY(2152, "Merchant has already been activated"),
	MERCHANT_DEACTIVATED_ALREADY(2153, "Merchant has already been deactivated"),
	INVALID_MERCHANT_STATUS(2154, "Merchant status can be ACTIVE or INACTIVE"),
	MERCHANT_STATUS_UPDATE_FAILED(2155, "Status update failed."),
	MERCHANT_STATUS_UPDATION_FAILED(2156, "Failed to update merchant status"),
	
	 // Listing Merchants
	MERCHANTS_LISTED_SUCCESSFULLY(2160, "Merchants listed successfully"),
	NO_MERCHANTS_TO_DISPLAY(2161, "No merchants to display"),
	MERCHANTS_LISTED_FOR_PARTNER_SUCCESSFULLY(2162, "Merchants listed for a partner successfully"),
	NO_MERCHANTS_FOR_PARTNER_TO_DISPLAY(2163, "No merchants to display for a partner"),
	SPECIFIC_MERCHANT_LISTED_SUCCESSFULLY(2164, "Specific merchant details listed successfully"),
	NO_SPECIFIC_MERCHANT_TO_DISPLAY(2165, "No specific merchant to display"),
	LISTING_MERCHANTS_FAILED(2166, "Listing the merchant or merchants failed."),
	MERCHANTS_IMAGES_LISTED_SUCCESSFULLY(2167, "Merchants Images listed successfully"),
	NO_MERCHANTS_IMAGES_TO_DISPLAY(2168, "No merchants images to display"),
	KEYWORD_VALUE_REQUIRED(2169, "Filter value is required for the chosen filter KEYWORD"),
	
	LISTING_MERCHANTS_DROPDOWN_SUCCESS(2147, "Listing merchants for dropdown success."),
	LISTING_MERCHANTS_DROPDOWN_FAILED(2148, "Listing merchants for dropdown failed."),
	
	//merchant Updation	

	MERCHANT_UPDATED_SUCCESSFULLY(2180, "Merchant updated successfully"),
	MERCHANT_UPDATION_FAILED(2181, "Merchant updation failed"),
	MERCHANT_NOT_AVAIABLE_UPDATE(2182, "Merchant not available"),

	// Discount Billing rate
	DUPLICATE_DISCOUNT_BILLINGRATE(2170, "Duplicate Discount Billing rate"),
	INVALID_PARAMETERS_RATE_ZERO_OR_LESS(2171, "Rate should be greater than zero"),
	RATE_TYPE_ADDED_SUSSESSFULY(2172, "Rate type added successfully"),
	RATE_TYPE_ADDITION_FAILED(2173, "Rate type addition failed"), 
	INVALID_TYPE(2174, "Type must be DISCOUNT"),
	INVALID_RATE_TYPE(2176,"Rate Type not Configured in Database.POINTS and AED Rate types are applicable for Discount Billing Rates"),
	DUPLICATE_RATE_TYPE(2175,"Duplicate Rate Type.Not added in the DataBase"),
	RATE_TYPE_LIST_EMPTY(2177,"At Least One Rate Type is mandatory"),
	RATE_MANDATORY_WHEN_RATE_TYPE_PASSED(2176, "Rate is mandatory when rate type is passed."),
	
	// Static tables
	CATEGORY_ADDED_SUSSESSFULY(2178, "Category added successfully."),
	CATEGORY_ADDITION_FAILED(2179, "Category addition failed."),
	BARCODE_ADDED_SUSSESSFULY(2180, "Barcode added successfully."),
	BARCODE_ADDITION_FAILED(2181, "Barcode addition failed."),
	
	NO_CATEGORY_TO_DISPLAY(2182, "No category to display"),
	CATEGORY_LISTING_FAILED(2183,"Listing categories failed"),
	CATEGORY_WITH_DUPLICATES(2184, "Contains one or more categories that already exist."),
	DUPLICATE_CATEGORY(2185, "Category already exists."),
	CATEGORY_LISTED_SUCCESSFULLY(2186,"Category listed successfully"),
	
	//Pagination
	PAGINATION_PAGE_EMPTY(2187, "Page field cannot be empty when limit is provided."),
	PAGINATION_LIMIT_EMPTY(2188, "Limit field cannot be empty when page is provided."),

	FAILED_TO_SEND_EMAIL_MERCHANT_STATUS_UPDATE(2189, "Failed to send email for merchant status update."),
	
	// Section for Exception and Error Codes
	GENERIC_RUNTIME_EXCEPTION(2190, "Runtime Exception occurred. Please refer logs "),

	PROPERTIES_FILE_IO_ERROR(2191, "I/O exception while calling properties file"),
	PROPERTIES_FILE_NOT_FOUND(2192, "Properties file not found Exception"),

	MONGO_DB_CONNECTION_ERROR(2193, "Failed to connect to Mongo DB"),

	REST_CLIENT_EXCEPTION(2194, "REST client Exception"),

	AMQ_CONNECTION_EXCEPTION(2195, "AMQ - Error connecting to AMQ exchange"),
	AMQ_SUBSCRIPTION_EXCEPTION(2196, "AMQ - Error subscribing to topic"),
	AMQ_JSON_PARSER_EXCEPTION(2197, "AMQ - Error parsing json msg from topic"),
	MODEL_MAPPER_EXCEPTION(2198, "MongoDB persist exception occured while saving"),
	MONGO_WRITE_EXCEPTION(2199, "MongoDB persist exception occured while saving");
	
	private final int id;

	private final String msg;

	MerchantCodes(int id, String msg) {
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
