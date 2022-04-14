package com.loyalty.marketplace.constants;

public enum MarketPlaceConstants {

	EMPTY_CHARACTER(""),
	SPACE_CHARACTER(" "),
    MESSAGE_SEPARATOR(" : "),
	OFFER_CODE_PREF("OF_"),
	SUBOFFER_CODE_PREF("SUBOF_"),
	OFFER_DEFAULT_STATUS("Inactive"),
	ACTIVE_STATUS("Active"),
	AED_TYPE_RATE("AED"),
	CHANNELID_WEB("WEB"),
	REQUEST_PARAMS("Request Parameters:"),
	RESPONSE_PARAMS("Response Parameters:"),
	LOG_CONSTANTS("{} {}"),
	FORWARD_SLASH("/"),
	DOMAIN_PERSIST("Domain Object To Be Persisted:"),
	DOMAIN_PERSISTED("Persisted Object: "),
	DATE_FORMAT("yyyy-MM-dd HH:mm:ss"),
	INSERT_ACTION("Insert"),
	UPDATE_ACTION("Update"),
	ACTIVATE_ACTION("Activate"),
	DEACTIVATE_ACTION("Deactivate"),
	ADMINISTRATOR_ROLE("Admin"),
	PARTNER_ROLE("Partner"),
	MERCHANT_ROLE("Merchant"),
	STORE_ROLE("Store"),
	EXCEPTION(" and Exception is :"),
	SAVE_MERCHANT("saveMerchant"),
	SAVE_STORE("saveStore"),
	UPDATE_MERCHANT_PASSWORD("updateMerchantPassword"),
	GET_AUTHENTICATION_TOKEN("getAuthenticationToken");

	
	
	private final String constant;

	MarketPlaceConstants(String constant) {
		this.constant = constant;
	}

	public String get() {
		return this.constant;
	}

}
