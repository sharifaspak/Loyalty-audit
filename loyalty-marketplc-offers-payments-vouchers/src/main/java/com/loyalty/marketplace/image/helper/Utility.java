package com.loyalty.marketplace.image.helper;

import java.text.MessageFormat;

import org.modelmapper.PropertyMap;

import com.loyalty.marketplace.image.constants.ImageCodes;
import com.loyalty.marketplace.image.constants.ImageConstants;
import com.loyalty.marketplace.image.inbound.dto.BannerPropertiesRequest;
import com.loyalty.marketplace.image.inbound.dto.GenerateMetaDataRequest;
import com.loyalty.marketplace.image.outbound.database.entity.BannerProperties;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.image.outbound.dto.GenerateMetaDataResponse;
import com.loyalty.marketplace.image.outbound.dto.ListImageResponse;
import com.loyalty.marketplace.image.outbound.dto.ListImageResult;
import com.loyalty.marketplace.image.outbound.dto.ResultResponse;

public class Utility {
	
	private Utility() {}
	
	/**
	 * Method to format a string message with correct image dimension to be diplayed in API response.
	 * @param type
	 * @param length
	 * @param height
	 * @return
	 */
	public static String getCorrectDimensions(String type, int length, int height) {
		return MessageFormat.format(ImageConstants.REQUIRED_DIMENSION_FOR_TYPE, type, length, height);
	}
	
	/**
	 * Validate input boolean parameters.
	 * @param isStaticBanner
	 * @param isBogoOffer
	 * @param resultResponse
	 * @return
	 */
	public static boolean validateBannerBooleanParameters(String isStaticBanner, String isBogoOffer, ResultResponse resultResponse) {
		if(null != isStaticBanner) {
			if (!isStaticBanner.equalsIgnoreCase(ImageConstants.YES)
					&& !isStaticBanner.equalsIgnoreCase(ImageConstants.NO)) {
				resultResponse.addErrorAPIResponse(ImageCodes.INVALID_INPUT_FOR_BOOLEAN.getIntId(),
						ImageCodes.INVALID_INPUT_FOR_BOOLEAN.getMsg() + isStaticBanner);
			}
		} 
		
		if(null != isBogoOffer) {
			if (!isBogoOffer.equalsIgnoreCase(ImageConstants.YES)
					&& !isBogoOffer.equalsIgnoreCase(ImageConstants.NO)) {
				resultResponse.addErrorAPIResponse(ImageCodes.INVALID_INPUT_FOR_BOOLEAN.getIntId(),
						ImageCodes.INVALID_INPUT_FOR_BOOLEAN.getMsg() + isBogoOffer);
			}
		} 
		return resultResponse.getApiStatus().getErrors().isEmpty();
	}
	
	/**
	 * Set boolean parameters for banner image request - insert.
	 * @param bannerPropertiesRequest
	 * @param resultResponse
	 * @return
	 */
	public static boolean setBooleanValueConfigureBannerProperty(BannerPropertiesRequest bannerPropertiesRequest, ResultResponse resultResponse) {

		if (bannerPropertiesRequest.getIncludeRedeemedOffers().equalsIgnoreCase(ImageConstants.YES)) {
			bannerPropertiesRequest.setInclRedeemedOffer(true);
			return true;
		} else if (bannerPropertiesRequest.getIncludeRedeemedOffers().equalsIgnoreCase(ImageConstants.NO)) {
			bannerPropertiesRequest.setInclRedeemedOffer(false);
			return true;
		}
		
		resultResponse.addErrorAPIResponse(ImageCodes.INVALID_VALUE_INCL_REDEEMED_OFFER.getIntId(),
				ImageCodes.INVALID_VALUE_INCL_REDEEMED_OFFER.getMsg());
		resultResponse.setResult(ImageCodes.BANNER_PROPERTY_CONFIGURE_FAILURE.getId(),
				ImageCodes.BANNER_PROPERTY_CONFIGURE_FAILURE.getMsg());
		return false;
	
	}
	
	/**
	 * Set boolean parameters for banner image request - update.
	 * @param bannerPropertiesRequest
	 * @param existingProperty
	 * @param resultResponse
	 * @return
	 */
	public static boolean setBooleanValueUpdateBannerProperty(BannerPropertiesRequest bannerPropertiesRequest, BannerProperties existingProperty, ResultResponse resultResponse) {

		if(null == bannerPropertiesRequest.getIncludeRedeemedOffers()) {
			bannerPropertiesRequest.setInclRedeemedOffer(existingProperty.isIncludeRedeemedOffers());
			return true;
		} else {
			if (bannerPropertiesRequest.getIncludeRedeemedOffers().equalsIgnoreCase(ImageConstants.YES)) {
				bannerPropertiesRequest.setInclRedeemedOffer(true);
				return true;
			} else if (bannerPropertiesRequest.getIncludeRedeemedOffers().equalsIgnoreCase(ImageConstants.NO)) {
				bannerPropertiesRequest.setInclRedeemedOffer(false);
				return true;
			}
		}
		
		resultResponse.addErrorAPIResponse(ImageCodes.INVALID_VALUE_INCL_REDEEMED_OFFER.getIntId(),
				ImageCodes.INVALID_VALUE_INCL_REDEEMED_OFFER.getMsg());
		resultResponse.setResult(ImageCodes.BANNER_PROPERTY_UPDATE_FAILURE.getId(),
				ImageCodes.BANNER_PROPERTY_UPDATE_FAILURE.getMsg());
		return false;
	
	}
	
	/**
	 * Validate input parameters for GET images API.
	 * @param listImageResponse
	 * @param imageType
	 * @param status
	 * @return
	 */
	public static boolean validateListingInputParameters(ListImageResponse listImageResponse, String imageType, String status) {
		
		if(null != imageType && !imageType.isEmpty() && !imageType.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER) && !imageType.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_GIFTING)) {
			listImageResponse.addErrorAPIResponse(ImageCodes.LISTING_INVALID_IMAGE_TYPE.getIntId(),
					ImageCodes.LISTING_INVALID_IMAGE_TYPE.getMsg());
			listImageResponse.setResult(ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getId(),
					ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getMsg());
			return false;
		}
		
		if(null != status && !status.isEmpty() && !status.equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)) {
			listImageResponse.addErrorAPIResponse(ImageCodes.LISTING_INVALID_STATUS.getIntId(),
					ImageCodes.LISTING_INVALID_STATUS.getMsg());
			listImageResponse.setResult(ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getId(),
					ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getMsg());
			return false;
		}
		
		return true;
	}
	
	/**
	 * PropertyMap for ModelMapper to map values to response object - used in GET API to list images.
	 */
	public static final PropertyMap<MarketplaceImage, ListImageResult> imageFieldMapping = new PropertyMap<MarketplaceImage, ListImageResult>() {
		
		@Override
		protected void configure() {
			
			map().setId(source.getId());
			map().setProgramCode(source.getProgramCode());
			
			map().setDomainId(source.getMerchantOfferImage().getDomainId());
			map().setDomainName(source.getMerchantOfferImage().getDomainName());
			map().setAvailableInChannel(source.getMerchantOfferImage().getAvailableInChannel());
			map().setImageType(source.getMerchantOfferImage().getImageType());
			map().setImageSize(source.getMerchantOfferImage().getImageSize());
			map().setImageLength(source.getMerchantOfferImage().getImageLength());
			map().setImageHeight(source.getMerchantOfferImage().getImageHeight());
			
			map().setOfferId(source.getBannerImage().getOfferId());
			map().setOfferType(source.getBannerImage().getOfferType());
			map().setDeepLink(source.getBannerImage().getDeepLink());
			map().setBannerOrder(source.getBannerImage().getBannerOrder());
			map().setBannerPosition(source.getBannerImage().getBannerPosition());
			map().setIsStaticBanner(source.getBannerImage().getIsStaticBanner());
			map().setIsBogoOffer(source.getBannerImage().getIsBogoOffer());
			map().setCoBrandedPartner(source.getBannerImage().getCoBrandedPartner());
			map().setStartDate(source.getBannerImage().getStartDate());
			map().setEndDate(source.getBannerImage().getEndDate());
			
			map().setImageId(source.getGiftingImage().getImageId());
			map().setType(source.getGiftingImage().getType());
			map().setPriority(source.getGiftingImage().getPriority());
			map().setBackgroundPriority(source.getGiftingImage().getBackgroundPriority());
			map().setColorCode(source.getGiftingImage().getColorCode());
			map().setColorDirection(source.getGiftingImage().getColorDirection());
			map().getName().setEnglish(source.getGiftingImage().getName().getEnglish());
			map().getName().setArabic(source.getGiftingImage().getName().getArabic());
			map().getGreetingMessage().setEnglish(source.getGiftingImage().getGreetingMessage().getEnglish());
			map().getGreetingMessage().setArabic(source.getGiftingImage().getGreetingMessage().getArabic());
		
		}
	};

	/**
	 * This method is used to validate request parameters for meta data generation API.
	 * @param generateMetaDataRequest
	 * @param generateMetaDataResponse
	 * @return
	 */
	public static boolean validateGenerateMetaDataRequestParameters(GenerateMetaDataRequest generateMetaDataRequest, GenerateMetaDataResponse generateMetaDataResponse) {
		
		switch (generateMetaDataRequest.getDomainName().toUpperCase()) {

		case ImageConstants.DOMAIN_NAME_MERCHANT:
			
			generateMetaDataRequest.setDomainName(ImageConstants.DOMAIN_NAME_MERCHANT);
			validateMerchantDomainType(generateMetaDataRequest, generateMetaDataResponse);
			generateMetaDataRequest.setImageType(generateMetaDataRequest.getImageType().toUpperCase());
			break;

		case ImageConstants.DOMAIN_NAME_OFFER:
		
			generateMetaDataRequest.setDomainName(ImageConstants.DOMAIN_NAME_OFFER);
			validateOfferDomainType(generateMetaDataRequest, generateMetaDataResponse);
			generateMetaDataRequest.setImageType(generateMetaDataRequest.getImageType().toUpperCase());
			break;

		default:
		
			generateMetaDataResponse.addErrorAPIResponse(ImageCodes.INVALID_DOMAIN_NAME.getIntId(),
					ImageCodes.INVALID_DOMAIN_NAME.getMsg() + generateMetaDataRequest.getDomainName());

		}
		
		if(!ImageConstants.CHANNEL_LIST.contains(generateMetaDataRequest.getAvailableInChannel().toUpperCase())) {
			generateMetaDataResponse.addErrorAPIResponse(ImageCodes.INVALID_CHANNEL.getIntId(),
					ImageCodes.INVALID_CHANNEL.getMsg());
		}
		
		return generateMetaDataResponse.getApiStatus().getErrors().isEmpty();
		
	}
	
	/**
	 * This method is used to validate image type for merchant domain - meta data generation API.
	 * @param generateMetaDataRequest
	 * @param generateMetaDataResponse
	 */
	private static void validateMerchantDomainType(GenerateMetaDataRequest generateMetaDataRequest, GenerateMetaDataResponse generateMetaDataResponse) {
		
		if (generateMetaDataRequest.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SWEB)) {
			generateMetaDataRequest.setAvailableInChannel(ImageConstants.CHANNEL_SWEB);
			if (!ImageConstants.MERCHANT_SWEB_IMAGE_TYPE_LIST.contains(generateMetaDataRequest.getImageType().toUpperCase())) {
				generateMetaDataResponse.addErrorAPIResponse(ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_WEB.getIntId(),
						ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_WEB.getMsg());
			}
		}
		if (generateMetaDataRequest.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SAPP)) {
			generateMetaDataRequest.setAvailableInChannel(ImageConstants.CHANNEL_SAPP);
			if (!ImageConstants.MERCHANT_SAPP_IMAGE_TYPE_LIST.contains(generateMetaDataRequest.getImageType().toUpperCase())) {
				generateMetaDataResponse.addErrorAPIResponse(ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_APP.getIntId(),
						ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_APP.getMsg());
			}
		}
		
	}
	
	/**
	 * This method is used to validate image type for offer domain - meta data generation API.
	 * @param generateMetaDataRequest
	 * @param generateMetaDataResponse
	 */
	private static void validateOfferDomainType(GenerateMetaDataRequest generateMetaDataRequest, GenerateMetaDataResponse generateMetaDataResponse) {
		
		if (generateMetaDataRequest.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SWEB)) {
			generateMetaDataRequest.setAvailableInChannel(ImageConstants.CHANNEL_SWEB);
			if (!ImageConstants.OFFER_SWEB_IMAGE_TYPE_LIST.contains(generateMetaDataRequest.getImageType().toUpperCase())) {
				generateMetaDataResponse.addErrorAPIResponse(ImageCodes.OFFER_INVALID_IMAGE_TYPE_WEB.getIntId(),
						ImageCodes.OFFER_INVALID_IMAGE_TYPE_WEB.getMsg());
			}
		}
		if (generateMetaDataRequest.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SAPP)) {
			generateMetaDataRequest.setAvailableInChannel(ImageConstants.CHANNEL_SAPP);
			if (!ImageConstants.OFFER_SAPP_IMAGE_TYPE_LIST.contains(generateMetaDataRequest.getImageType().toUpperCase())) {
				generateMetaDataResponse.addErrorAPIResponse(ImageCodes.OFFER_INVALID_IMAGE_TYPE_APP.getIntId(),
						ImageCodes.OFFER_INVALID_IMAGE_TYPE_APP.getMsg());
			}
		}
		
	}
	
}
