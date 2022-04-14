package com.loyalty.marketplace.stores.constants;

/**
 * The StoreManagementCode should contain all the Business, Exception and Error
 * handling codes used thought the Store Management micro-service.
 * 
 * @author siddharth.panda
 */
public enum StoreCodes {

	// Section for Business Codes

	// Store Creation
	STORE_CREATED(2230, "Store Created successfully"), STORE_CREATION_FAILED(2231, "Store Creation Failed"),
	MERCHANT_NOT_AVAILABLE(2232, "Merchant not available to create Store"),
	STORE_EXISTS(2233, "Store Code Already exists"),
	INVALID_STORE_CODE(2234,
			"Invalid store code. Store code can contain only 5 or less characters, with no whitespaces and special characters."),

	// Store Updation

	STORE_UPDATED_SUCCESSFULLY(2240, "Store Updated successfully"), STORE_UPDATION_FAILED(2241, "Store updation failed"),
	STORE_UNAVAILABLE_TO_UPDATE(2242, "Store Unavailable to Update"),
	MERCHANT_NOT_AVAILABLE_UPDATE(2243, "Merchant not available to update Store"),

	// ContactPerson
	CONTACT_PERSON_CREATED(2251, "Store Contact Person Created"),
	CONTACT_PERSON_CREATION_FAILED(2252, "Store Contact Person Creation failed"),
	CONTACT_PERSON_EXISTS(2253, "EmailId  Already Exists"), INVALID_EMAIL(2254, "Invalid Email"),
	FIRST_NAME_CANNOT_BY_EMPTY(2255, "First Name cannot be empty"),
	LAST_NAME_CANNOT_BY_EMPTY(2256, "Last Name cannot be empty"), INVALID_MOBILE_NUMBER(2257, "Invalid Mobile Number"),
	INVALID_FAX_NUMBER(2258, "Invalid Fax Number"),
	STORE_NOT_AVAILABLE(2259, "Store Not available to create Contact Person"),
	USERNAME_NOT_FOUND(2260, "Username not found to update contact person"),
	INVALID_USER_NAME(2261, "Username cannot be Null or Empty to update Contact person"),

	// Listing Stores
	STORE_LISTED_SUCCESSFULLY(2210, "Stores listed successfully"), LISTING_STORES_FAILED(2211, "Listing stores failed"),
	NO_STORES_TO_DISPLAY(2212, "No Stores to display"),

	STORES_FOR_MERCHANT_LISTED_SUCCESSFULLY(2215, "Stores for a merchant listed successfully"),
	NO_STORES_FOR_MERCHANT_TO_DISPLAY(2216, "No stores for a merchant to display"),
	MERCHANT_UNAVAILABLE(2217, "Merchant does not exist"),

	STORE_LISTED_FOR_PARTNER_SUCCESSFULLY(2220, "Stores listed for a partner successfully"),
	NO_STORES_FOR_PARTNER_TO_DISPLAY(2221, "No store to display for a partner"),
	PARTNER_UNAVAILABLE(2222, "Partner does not exist"),

	SPECIFIC_STORE_LISTED_SUCCESSFULLY(2225, "Specific store details listed successfully"),
	NO_SPECIFIC_STORE_TO_DISPLAY(2226, "No specific store to display"),

	//Pagination
	PAGINATION_PAGE_EMPTY(2227, "Page field cannot be empty when limit is provided."),
	PAGINATION_LIMIT_EMPTY(2228, "Limit field cannot be empty when page is provided."),

	DUPLICATE_RECORDS_RETRIEVED_STORES(2229, "Duplicate records retrieved from Stores."),

	// Section for Exception and Error Codes
	GENERIC_RUNTIME_EXCEPTION(2290, "Runtime Exception occured. Please refer logs "),

	PROPERTIES_FILE_IO_ERROR(2291, "I/O exception while calling properties file"),
	PROPERTIES_FILE_NOT_FOUND(2292, "Properties file not found Exception"),

	MONGO_DB_CONNECTION_ERROR(2293, "Failed to connect to Mongo DB"),

	REST_CLIENT_EXCEPTION(2294, "REST client Exception"),

	AMQ_CONNECTION_EXCEPTION(2295, "AMQ - Error connecting to AMQ exchange"),
	AMQ_SUBSCRIPTION_EXCEPTION(2296, "AMQ - Error subscribing to topic"),
	AMQ_JSON_PARSER_EXCEPTION(2297, "AMQ - Error parsing json msg from topic"),
	MODEL_MAPPER_EXCEPTION(2298, "MongoDB persist exception occured while saving"),
	MONGO_WRITE_EXCEPTION(2299, "MongoDB persist exception occured while saving");

	private final int id;
	private final String msg;

	StoreCodes(int id, String msg) {
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
