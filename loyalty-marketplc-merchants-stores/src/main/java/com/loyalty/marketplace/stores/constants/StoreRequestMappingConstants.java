package com.loyalty.marketplace.stores.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class StoreRequestMappingConstants extends RequestMappingConstants {

	private StoreRequestMappingConstants() {}

	public static final String PARTNER_CODE = "partnerCode";
	public static final String STORE_CODE = "storeCode";
	public static final String MERCHANT_CODE = "merchantCode";
	public static final String CONFIGURE_STORE = "/stores";
	public static final String UPDATE_STORE = "/stores/{storeCode}";
	public static final String LIST_ALL_STORES= "/stores";
	public static final String LIST_MERCHANT_STORES="/merchants/{merchantCode}/stores";
	public static final String LIST_PARTNER_STORES="/partners/{partnerCode}/stores";
	public static final String VIEW_SPECIFIC_STORE= "/stores/{storeCode}";
	public static final String PAGE = "page";
	public static final String LIMIT = "limit";

}
