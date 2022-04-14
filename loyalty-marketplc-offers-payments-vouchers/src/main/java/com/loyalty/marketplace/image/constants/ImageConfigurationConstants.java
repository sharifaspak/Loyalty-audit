package com.loyalty.marketplace.image.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class ImageConfigurationConstants extends RequestMappingConstants {

	//ALL CONTROLLER APIs
	public static final String MARKETPLACE = "Marketplace";
	public static final String MARKETPLACE_BASE = "/marketplace";
	
	//REQUEST PARAMETERS
	public static final String FILE = "file";
	public static final String PATH = "path";
	public static final String ACTION = "action";
	public static final String EXISTING_PATH = "existingPath";
	public static final String IMAGE_TYPE = "imageType";
	public static final String DOMAIN_ID = "domainId";
	public static final String DOMAIN_NAME = "domainName";
	public static final String AVAILABLE_IN_CHANNEL = "availableInChannel";
	
	public static final String OFFER_ID = "offerId";
	public static final String OFFER_TYPE = "offerType";
	public static final String DEEP_LINK = "deepLink";
	public static final String IS_STATIC_BANNER = "isStaticBanner";
	public static final String BANNER_ORDER = "bannerOrder";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String IS_BOGO_OFFER = "isBogoOffer";
	public static final String CO_BRANDED_PARTNER = "coBrandedPartner";
	public static final String BANNER_POSITION = "bannerPosition";
	public static final String STATUS = "status";
	
	public static final String NAME_EN = "nameEn";
	public static final String TYPE = "type";
	public static final String COLOR_CODE = "colorCode";
	public static final String COLOR_DIRECTION = "colorDirection";
	public static final String PRIORITY = "priority";
	public static final String BACKGROUND_PRIORITY = "backgroundPriority";
	public static final String NAME_AR = "nameAr";
	public static final String GREETING_MSG_EN = "greetingMessageEn";
	public static final String GREETING_MSG_AR = "greetingMessageAr";
	
	public static final String IMAGE_ID = "id";
	public static final String LISTING_IMAGE_TYPE = "imageType";
	public static final String LISTING_STATUS = "status";

	//ENDPOINT URLs
	public static final String UPLOAD_IMAGE_MERCHANT_OFFER = "/merchantOfferImage";
	public static final String UPLOAD_IMAGE_BANNER = "/bannerImage";
	public static final String UPLOAD_IMAGE_GIFTING = "/giftingImage";
	public static final String UPDATE_IMAGE = "/images/{id}";
	public static final String DISPLAY_IMAGE = "/images/{id}";
	public static final String RETRIEVE_IMAGES = "/images";
	public static final String ADD_BANNER_PROPERTY = "/bannerProperty";
	public static final String UPDATE_BANNER_PROPERTY = "/bannerProperty/{id}";
	public static final String LIST_BANNER_PROPERTY = "/bannerProperty";
	public static final String LIST_IMAGE_DIMENSIONS = "/images/dimensionsList";
	public static final String GENERATE_META_DATA = "/images/generateMetaData";
	
	//DATABASE CONSTANTS
	public static final String DB_MARKETPLACE_IMAGE = "MarketplaceImage";
	public static final String DB_BANNER_PROPERTIES = "BannerProperties";
	public static final String DB_BULK_IMAGE_UPLOAD_ERRORS = "BulkImageUploadErrors";
		
	//METHOD NAMES
	public static final String BANNER_PROPERTIES_DOMAIN_CONFIGURE = "configureBannerProperty";
	public static final String BANNER_PROPERTIES_DOMAIN_UPDATE = "updateBannerProperty";
	public static final String BANNER_PROPERTIES_DOMAIN_LIST = "listBannerProperties";
	public static final String BANNER_PROPERTIES_DOMAIN_SAVE_DB = "saveBannerPropertyToDatabase";
	
	public static final String IMAGE_DOMAIN_MERCHANT_OFFER_IMAGE = "validateUploadMerchantOfferImage";
	public static final String IMAGE_DOMAIN_BANNER_IMAGE = "validateUploadBannerImage";
	public static final String IMAGE_DOMAIN_GIFTING_IMAGE = "validateUploadGiftingImage";
	public static final String IMAGE_DOMAIN_UPDATE_IMAGE = "updateBannerGiftingImage";
	public static final String IMAGE_DOMAIN_LIST_IMAGES = "listAllImages";
	public static final String IMAGE_DOMAIN_DISPLAY_IMG = "displayImage";
	public static final String IMAGE_DOMAIN_SAVE_DB = "uploadImageToDatabase";
	
	public static final String CONTROLLER_HELPER_VALIDATE_IMG_FORMAT = "validateImageFormat";
	public static final String CONTROLLER_HELPER_SET_DIMENSIONS = "setImageDimensions";
	public static final String CONTROLLER_HELPER_UPLOAD_DB = "uploadImageToDatabase";
	public static final String CONTROLLER_HELPER_UPDATE_DB = "updateImageToDatabase";
	public static final String CONTROLLER_HELPER_UPLOAD_NGINX = "uploadImage";
	public static final String CONTROLLER_HELPER_GENERATE_META_DATA = "generateMetaData";
	public static final String CONTROLLER_HELPER_RETRIEVE_DATA_NGINX = "retrieveImageDataFromNginx";
	public static final String CONTROLLER_HELPER_DELETE_IMAGE_NGINX = "deleteImageFromNginx";
	
	public static final String CONTROLLER_CONFIGURE_MERCHANT_OFFER_IMG = "configureMerchantOfferImage";
	public static final String CONTROLLER_CONFIGURE_BANNER_IMG = "configureBannerImage";
	public static final String CONTROLLER_CONFIGURE_GIFTING_IMG = "configureGiftingImage";
	public static final String CONTROLLER_CONFIGURE_BANNER_PROPERTIES = "configureBannerProperties";
	public static final String CONTROLLER_UPDATE_IMG = "updateBannerGiftingImage";
	public static final String CONTROLLER_UPDATE_PROPERTIES = "updateBannerProperties";
	public static final String CONTROLLER_LIST_IMG = "listImages";
	public static final String CONTROLLER_DISPLAY_IMG = "displayImage";
	public static final String CONTROLLER_LIST_PROPERTIES = "listBannerProperties";
	public static final String CONTROLLER_GENERATE_META_DATA = "generateMetaDataForMerchantOfferImages";
	
}
