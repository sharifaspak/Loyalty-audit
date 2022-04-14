package com.loyalty.marketplace.image.helper;

import com.loyalty.marketplace.image.constants.ImageCodes;
import com.loyalty.marketplace.image.constants.ImageConstants;
import com.loyalty.marketplace.image.inbound.dto.BannerParameters;
import com.loyalty.marketplace.image.inbound.dto.GiftingParameters;
import com.loyalty.marketplace.image.inbound.dto.MerchantOfferParameters;
import com.loyalty.marketplace.image.outbound.dto.ResultResponse;

public enum CheckDimensions {

	//IMAGE_TYPE(Length, Height)
	
	//MERCHANT - WEB
	MERCHANT_WEB_LISTING(ImageDimensions.MERCHANT_WEB_LISTING_L, ImageDimensions.MERCHANT_WEB_LISTING_H, ImageConstants.MERCHANT_WEB_LISTING, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SWEB),
	MERCHANT_WEB_DETAIL(ImageDimensions.MERCHANT_WEB_DETAIL_L, ImageDimensions.MERCHANT_WEB_DETAIL_H, ImageConstants.MERCHANT_WEB_DETAIL, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SWEB),
	
	//MERCHANT - APP
	MERCHANT_APP_COUPONS(ImageDimensions.MERCHANT_APP_COUPONS_L, ImageDimensions.MERCHANT_APP_COUPONS_H, ImageConstants.MERCHANT_APP_COUPONS, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),
	MERCHANT_APP_DETAIL(ImageDimensions.MERCHANT_APP_DETAIL_L, ImageDimensions.MERCHANT_APP_DETAIL_H, ImageConstants.MERCHANT_APP_DETAIL, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),
	MERCHANT_APP_LISTING(ImageDimensions.MERCHANT_APP_LISTING_L, ImageDimensions.MERCHANT_APP_LISTING_H, ImageConstants.MERCHANT_APP_LISTING, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),
	MERCHANT_APP_LIST_LOGO(ImageDimensions.MERCHANT_APP_LIST_LOGO_L, ImageDimensions.MERCHANT_APP_LIST_LOGO_H, ImageConstants.MERCHANT_APP_LIST_LOGO, ImageConstants.DOMAIN_NAME_MERCHANT, ImageConstants.CHANNEL_SAPP),
	
	//OFFER - WEB
	OFFER_WEB_DETAIL(ImageDimensions.OFFER_WEB_DETAIL_L, ImageDimensions.OFFER_WEB_DETAIL_H, ImageConstants.OFFER_WEB_DETAIL, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),
	OFFER_WEB_LISTING(ImageDimensions.OFFER_WEB_LISTING_L, ImageDimensions.OFFER_WEB_LISTING_H, ImageConstants.OFFER_WEB_LISTING, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),
	OFFER_WEB_MY_COUPONS(ImageDimensions.OFFER_WEB_MY_COUPONS_L, ImageDimensions.OFFER_WEB_MY_COUPONS_H, ImageConstants.OFFER_WEB_MY_COUPONS, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),
	OFFER_WEB_FEATURED(ImageDimensions.OFFER_WEB_FEATURED_L, ImageDimensions.OFFER_WEB_FEATURED_H, ImageConstants.OFFER_WEB_FEATURED, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SWEB),
	
	//OFFER - APP
	OFFER_APP_DOD_LISTING_NEW(ImageDimensions.OFFER_APP_DOD_LISTING_NEW_L, ImageDimensions.OFFER_APP_DOD_LISTING_NEW_H, ImageConstants.OFFER_APP_DOD_LISTING_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	OFFER_APP_SMALL_HORIZONTAL_NEW(ImageDimensions.OFFER_APP_SMALL_HORIZONTAL_NEW_L, ImageDimensions.OFFER_APP_SMALL_HORIZONTAL_NEW_H, ImageConstants.OFFER_APP_SMALL_HORIZONTAL_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	//OFFER_APP_DETAIL_NEW(ImageDimensions.OFFER_APP_DETAIL_NEW_L, ImageDimensions.OFFER_APP_DETAIL_NEW_H, ImageConstants.OFFER_APP_DETAIL_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	OFFER_APP_DETAIL(ImageDimensions.OFFER_APP_DETAIL_L, ImageDimensions.OFFER_APP_DETAIL_H, ImageConstants.OFFER_APP_DETAIL, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	OFFER_APP_LUCKY_OFFER_NEW(ImageDimensions.OFFER_APP_LUCKY_OFFER_NEW_L, ImageDimensions.OFFER_APP_LUCKY_OFFER_NEW_H, ImageConstants.OFFER_APP_LUCKY_OFFER_NEW, ImageConstants.DOMAIN_NAME_OFFER, ImageConstants.CHANNEL_SAPP),
	
	//BANNER IMAGES
	BANNER_POSITION_TOP(ImageDimensions.BANNER_POSITION_TOP_L, ImageDimensions.BANNER_POSITION_TOP_H, ImageConstants.BANNER_POSITION_TOP),
	BANNER_POSITION_MIDDLE(ImageDimensions.BANNER_POSITION_MIDDLE_L, ImageDimensions.BANNER_POSITION_MIDDLE_H, ImageConstants.BANNER_POSITION_MIDDLE),
	BANNER_POSITION_BOTTOM(ImageDimensions.BANNER_POSITION_BOTTOM_L, ImageDimensions.BANNER_POSITION_BOTTOM_H, ImageConstants.BANNER_POSITION_BOTTOM),
	
	//GIFTING IMAGES
	GIFTING_IMAGE(ImageDimensions.GIFTING_IMAGE_L, ImageDimensions.GIFTING_IMAGE_H, ImageConstants.GIFTING_IMAGE);
	
	private int length;
	private int height;
	private String imageType;
	private String domainName;
	private String channel;
	
	
	CheckDimensions(int length, int height, String imageType, String domainName, String channel) {
		this.length = length;
		this.height = height;
		this.imageType = imageType;
		this.domainName = domainName;
		this.channel = channel;
	}
	
	CheckDimensions(int length, int height, String imageType) {
		this.length = length;
		this.height = height;
		this.imageType = imageType;
	}

	/**
	 * Method to validate image dimensions for Merchant image.
	 * @param merchantParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateDimensionsMerchant(MerchantOfferParameters merchantParameters, ResultResponse resultResponse) {
		if (merchantParameters.getLength() != this.length || merchantParameters.getHeight() != this.height) {
			resultResponse.addErrorAPIResponse(ImageCodes.MERCHANT_INVALID_IMAGE_DIMENSIONS.getIntId(),
					ImageCodes.MERCHANT_INVALID_IMAGE_DIMENSIONS.getMsg() + Utility.getCorrectDimensions(this.imageType, this.length, this.height));
			resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getId(),
					ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getMsg());
			return true;
		}
		return false;
	}
	
	/**
	 * Method to validate image dimensions for Offer image.
	 * @param offerParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateDimensionsOffer(MerchantOfferParameters offerParameters, ResultResponse resultResponse) {
		if (offerParameters.getLength() != this.length || offerParameters.getHeight() != this.height) {
			resultResponse.addErrorAPIResponse(ImageCodes.OFFER_INVALID_IMAGE_DIMENSIONS.getIntId(),
					ImageCodes.OFFER_INVALID_IMAGE_DIMENSIONS.getMsg() + Utility.getCorrectDimensions(this.imageType, this.length, this.height));
			resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getId(),
					ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getMsg());
			return true;
		}
		return false;
	}
	
	/**
	 * Method to validate image dimensions for Banner image.
	 * @param bannerParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateDimensionsBanner(BannerParameters bannerParameters, ResultResponse resultResponse) {
		if (bannerParameters.getLength() != this.length || bannerParameters.getHeight() != this.height) {
			resultResponse.addErrorAPIResponse(ImageCodes.BANNER_IMAGE_INVALID_DIMENSIONS.getIntId(),
					ImageCodes.BANNER_IMAGE_INVALID_DIMENSIONS.getMsg() + Utility.getCorrectDimensions(this.imageType, this.length, this.height));
			resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
			return true;
		}
		return false;
	}
	
	/**
	 * Method to validate image dimensions for Gifting image.
	 * @param giftingParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateDimensionsGifting(GiftingParameters giftingParameters, ResultResponse resultResponse) {
		if (giftingParameters.getLength() != this.length || giftingParameters.getHeight() != this.height) {
			resultResponse.addErrorAPIResponse(ImageCodes.GIFTING_IMAGE_INVALID_DIMENSIONS.getIntId(),
					ImageCodes.GIFTING_IMAGE_INVALID_DIMENSIONS.getMsg() + Utility.getCorrectDimensions(this.imageType, this.length, this.height));
			resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
			return true;
		}
		return false;
	}

	/**
	 * Method to retrieve dimensions for Merchant-Offer image.
	 * @param imageType
	 * @param domainName
	 * @param channel
	 * @return
	 */
	public static CheckDimensions getDimensionMerchantOffer(String imageType, String domainName, String channel) {
	    for(CheckDimensions e : values()) {
	        if(e.imageType.equalsIgnoreCase(imageType) && e.domainName.equalsIgnoreCase(domainName) && e.channel.equalsIgnoreCase(channel)) return e;
	    }
	    throw new IllegalArgumentException();
	}
	
	/**
	 * Method to retrieve dimension for Gifting image.
	 * @param imageType
	 * @return
	 */
	public static CheckDimensions getDimensionBannerGifting(String imageType) {
	    for(CheckDimensions e : values()) {
	        if(e.imageType.equalsIgnoreCase(imageType)) return e;
	    }
	    throw new IllegalArgumentException();
	}
	
}
