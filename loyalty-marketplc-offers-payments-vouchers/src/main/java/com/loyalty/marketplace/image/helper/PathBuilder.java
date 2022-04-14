package com.loyalty.marketplace.image.helper;

import com.loyalty.marketplace.image.constants.ImageConstants;

public enum PathBuilder {

	// MERCHANT
	MERCHANT_WEB_LISTING(ImageConstants.PATH_FOR_IMAGE_MERCHANT_WEB_LISTING, ImageConstants.MERCHANT_WEB_LISTING, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SWEB),
	MERCHANT_WEB_DETAIL(ImageConstants.PATH_FOR_IMAGE_MERCHANT_WEB_DETAIL, ImageConstants.MERCHANT_WEB_DETAIL, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SWEB),

	// MERCHANT - APP
	MERCHANT_APP_COUPONS(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_COUPONS, ImageConstants.MERCHANT_APP_COUPONS, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),
	MERCHANT_APP_DETAIL(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_DETAIL, ImageConstants.MERCHANT_APP_DETAIL, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),
	MERCHANT_APP_LISTING(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_LISTING, ImageConstants.MERCHANT_APP_LISTING, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),
	MERCHANT_APP_LIST_LOGO(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_LIST_LOGO, ImageConstants.MERCHANT_APP_LIST_LOGO, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),

	// OFFER - WEB
	OFFER_WEB_DETAIL(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_DETAIL, ImageConstants.OFFER_WEB_DETAIL, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),
	OFFER_WEB_LISTING(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_LISTING, ImageConstants.OFFER_WEB_LISTING, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),
	OFFER_WEB_MY_COUPONS(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_MY_COUPONS, ImageConstants.OFFER_WEB_MY_COUPONS, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),
	OFFER_WEB_FEATURED(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_FEATURED, ImageConstants.OFFER_WEB_FEATURED, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),

	// OFFER - APP
	OFFER_APP_DOD_LISTING_NEW(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_DOD_LISTING_NEW, ImageConstants.OFFER_APP_DOD_LISTING_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	OFFER_APP_SMALL_HORIZONTAL_NEW(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_SMALL_HORIZONTAL_NEW, ImageConstants.OFFER_APP_SMALL_HORIZONTAL_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	//OFFER_APP_DETAIL_NEW(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_DETAIL_NEW, ImageConstants.OFFER_APP_DETAIL_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	OFFER_APP_DETAIL(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_DETAIL, ImageConstants.OFFER_APP_DETAIL, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	OFFER_APP_LUCKY_OFFER_NEW(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_LUCKY_OFFER_NEW, ImageConstants.OFFER_APP_LUCKY_OFFER_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),

	// BANNER IMAGES
	BANNER_IMAGE(ImageConstants.PATH_FOR_IMAGE_BANNER, ImageConstants.BANNER_IMAGE),
	
	// GIFTING IMAGES
	GIFTING_IMAGE(ImageConstants.PATH_FOR_IMAGE_GIFTING, ImageConstants.GIFTING_IMAGE);

	private String path;
	private String imageType;
	private String domainName;
	private String channel;

	PathBuilder(String path, String imageType, String domainName, String channel) {
		this.path = path;
		this.imageType = imageType;
		this.domainName = domainName;
		this.channel = channel;
	}

	PathBuilder(String path, String imageType) {
		this.path = path;
		this.imageType = imageType;
	}
	
	/**
	 * Method to retrieve image upload path for Merchant-Offer image.
	 * @param imageType
	 * @param domainName
	 * @param channel
	 * @return
	 */
	public static PathBuilder getPathMerchantOffer(String imageType, String domainName, String channel) {
	    for(PathBuilder e : values()) {
	        if(e.imageType.equalsIgnoreCase(imageType) && e.domainName.equalsIgnoreCase(domainName) && e.channel.equalsIgnoreCase(channel)) return e;
	    }
	    throw new IllegalArgumentException();
	}
	
	/**
	 * Method to retrieve image upload path for Banner & Gifting image.
	 * @param imageType
	 * @return
	 */
	public static PathBuilder getPathBannerGifting(String imageType) {
	    for(PathBuilder e : values()) {
	        if(e.imageType.equalsIgnoreCase(imageType)) return e;
	    }
	    throw new IllegalArgumentException();
	}
	
	/**
	 * Convert path to string for Merchant-Offer image.
	 * @param imageType
	 * @param domainName
	 * @param channel
	 * @return
	 */
	public static String getStringPathMerchantOffer(String imageType, String domainName, String channel) {
		PathBuilder pathToSave = getPathMerchantOffer(imageType, domainName, channel);
		return pathToSave.path;
	}
	
	/**
	 * Convert path to string for Banner & Gifting image.
	 * @param imageType
	 * @return
	 */
	public static String getStringPathBannerGifting(String imageType) {
		PathBuilder pathToSave = getPathBannerGifting(imageType);
		return pathToSave.path;
	}

}
