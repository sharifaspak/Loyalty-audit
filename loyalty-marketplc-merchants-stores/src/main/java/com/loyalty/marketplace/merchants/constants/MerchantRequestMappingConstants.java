package com.loyalty.marketplace.merchants.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class MerchantRequestMappingConstants extends RequestMappingConstants {

	private MerchantRequestMappingConstants() {}
	
	public static final String PARTNER_CODE="partnerCode"; 
	public static final String MERCHANT_CODE="merchantCode";
	public static final String MERCHANT_STATUS="status";
	public static final String MERCHANT_DOMAIN="merchant";
	public static final String CREATE_MERCHANT="/merchants";
	public static final String UPDATE_MERCHANT="/merchants/{merchantCode}";
	public static final String ACTIVATE_DEACTIVATE_MERCHANT="/merchants/{merchantCode}/merchantStatus";
	public static final String CONFIGURE_RATETYPE="/rateType";
	public static final String CONFIGURE_BARCODE = "/barcode";
	public static final String RESSET_RATETYPE="/resetRateTypes";
	public static final String RESSET_BARCODE = "/resetBarcodes";
	public static final String LIST_MERCHANT_IMAGES ="/merchants/merchantImages/{domain}";
	public static final String LIST_MERCHANT_THUMDNAILS ="/merchants/thumbnails";
	public static final String CONFIGURE_IMAGE = "/images";
	public static final String VIEW_SPECIFIC_MERCHANT = "/merchants/{merchantCode}";
	public static final String VIEW_PARTNER_MERCHANTS = "/partners/{partnerCode}/merchants";
	public static final String LIST_ALL_MERCHANTS = "/merchants";
	public static final String LIST_BARCODES = "/merchants/barcodes";
	public static final String LIST_RATETYPES = "/merchants/rateTypes";
	public static final String GET_MERCHANT = "/merchant/{userName}";
	public static final String GET_STORE_MERCHANT = "/storeMerchant/{userName}";
	public static final String LIST_DROPDOWN_MERCHANTS = "/dropdownMerchants";
	public static final String GET_MERCHANT_NAME = "/merchantName/{merchantCode}";
	public static final String PAGE = "page";
	public static final String LIMIT = "limit";
	
}
