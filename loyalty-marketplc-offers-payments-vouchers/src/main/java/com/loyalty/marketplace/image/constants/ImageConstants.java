package com.loyalty.marketplace.image.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class ImageConstants extends RequestMappingConstants {

	private ImageConstants() {}
	
	//GENERIC CONSTANTS
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String COMMA_SEPARATOR = ", ";
	public static final String STATUS_ACTIVE = "Active";
	public static final String STATUS_INACTIVE = "Inactive";
	public static final String REQUIRED_DIMENSION_FOR_TYPE = "Required dimension for type {0} is {1}*{2}.";
	
	public static final String PROGRAM_CODE = "programCode";
	public static final String USERNAME = "userName";
	public static final String TOKEN = "token";
	public static final String IMAGE_CATEGORY = "category";
	public static final String IMAGE_PATH = "path";
	public static final String IMAGE_PATH_DR = "imageUrlDr";
	public static final String IMAGE_PATH_PROD = "imageUrlProd";
	public static final String IMAGE_STATUS = "status";
	public static final String ORIGINAL_FILE_NAME = "originalFileName";
	
	public static final String YES = "Yes";
	public static final String NO = "No";
	
	public static final String ACTION_INSERT = "Insert";
	public static final String ACTION_UPDATE = "Update";
	public static final String ACTION_ERROR = "Error";
	
	//LOG CONSTANTS:
	public static final String LOG_ENTERING = "ENTERING ";
	public static final String LOG_LEAVING = "EXITING ";
	public static final String CLASS_NAME = "CLASS: {} | ";
	public static final String METHOD_NAME = "METHOD: {} | ";
	public static final String API_NAME = "API: {}";
	public static final String REQUEST_PARAMS = "Request Parameters: {}";
	public static final String RESPONSE_PARAMS = "Response Parameters: {}";
	public static final String LIST_IMAGE_REQUEST_PARAMS = "Image Type: {}, Status: {}";
	public static final String ALLOWED_IMAGE_FORMATS = "Allowed Image Formats: {}";
	public static final String IMAGE_EXTENSION = "Uploaded Image Extension: {}";
	public static final String IMAGE_DIMENSION = "Uploaded Image Dimensions: {}*{}";
	public static final String DOMAIN_TO_SAVE = "Domain To Save: {}";
	public static final String ENTITY_TO_SAVE = "Entity To Save: {}";
	public static final String SAVED_ENTITY = "Saved Entity: {}";
	public static final String CREATING_IMAGE_DIRECTORIES = "Creating image directories.";
	public static final String IMAGE_MIRGRATION_ERRORS = "Error migrating images: {}";
	public static final String TOTAL_FILE_COUNT = "Total number of files: {}";
	public static final String IN_PROD_SERVER = "Inside production server: {}";
	public static final String DISPLAY_IMAGE = "Displayed Image Successfully.";
	public static final String UPLOADING_TO_NGINX_SERVER = "Upload image to nginx server.";
	public static final String DELETNG_IMAGE_NGINX_SERVER_CAUSE_ERROR = "Deleting image from first nginx server due to error in second nginx server.";
	public static final String DELETNG_IMAGE_NGINX_SERVER_CAUSE_UPDATE = "Deleting older images after updating to new images from nginx server.";
	
	//PATH TO UPLOAD IMAGES
	public static final String FORWARD_SLASH = "/";
	public static final String IMAGE_PATH_DIRECTORY = "/images";
	public static final String IMAGES_DIRECTORY = "images/";
	
	public static final String PATH_FOR_IMAGE_BANNER = "BANNERS/";
	public static final String PATH_FOR_IMAGE_GIFTING = "GIFTING/";
	
	public static final String PATH_FOR_IMAGE_WEB = "WEB/";
	public static final String PATH_FOR_IMAGE_APP = "APP/";
	
	public static final String PATH_FOR_IMAGE_MERCHANT_WEB = PATH_FOR_IMAGE_WEB + "PARTNERS/";
	public static final String PATH_FOR_IMAGE_MERCHANT_APP = PATH_FOR_IMAGE_APP + "PARTNERS/";
	public static final String PATH_FOR_IMAGE_OFFER_WEB = PATH_FOR_IMAGE_WEB + "OFFERS/";
	public static final String PATH_FOR_IMAGE_OFFER_APP = PATH_FOR_IMAGE_APP + "OFFERS/";
	
	public static final String PATH_FOR_IMAGE_MERCHANT_WEB_LISTING = PATH_FOR_IMAGE_MERCHANT_WEB + "LISTING/";
	public static final String PATH_FOR_IMAGE_MERCHANT_WEB_DETAIL = PATH_FOR_IMAGE_MERCHANT_WEB + "DETAIL/";
	public static final String PATH_FOR_IMAGE_MERCHANT_APP_COUPONS = PATH_FOR_IMAGE_MERCHANT_APP + "COUPONS/";
	public static final String PATH_FOR_IMAGE_MERCHANT_APP_DETAIL = PATH_FOR_IMAGE_MERCHANT_APP + "DETAIL/";
	public static final String PATH_FOR_IMAGE_MERCHANT_APP_LISTING = PATH_FOR_IMAGE_MERCHANT_APP + "LISTING/";
	public static final String PATH_FOR_IMAGE_MERCHANT_APP_LIST_LOGO = PATH_FOR_IMAGE_MERCHANT_APP + "LIST_LOGO/";
	
	public static final String PATH_FOR_IMAGE_OFFER_WEB_DETAIL = PATH_FOR_IMAGE_OFFER_WEB + "DETAIL/";
	public static final String PATH_FOR_IMAGE_OFFER_WEB_LISTING = PATH_FOR_IMAGE_OFFER_WEB + "LISTING/";
	public static final String PATH_FOR_IMAGE_OFFER_WEB_MY_COUPONS = PATH_FOR_IMAGE_OFFER_WEB + "MYCOUPONS/";
	public static final String PATH_FOR_IMAGE_OFFER_WEB_FEATURED = PATH_FOR_IMAGE_OFFER_WEB + "FEATURED/";
	public static final String PATH_FOR_IMAGE_OFFER_APP_DOD_LISTING_NEW = PATH_FOR_IMAGE_OFFER_APP + "DOD_LISTING/";
	public static final String PATH_FOR_IMAGE_OFFER_APP_SMALL_HORIZONTAL_NEW = PATH_FOR_IMAGE_OFFER_APP + "SMALL_HORIZONTAL/";
	public static final String PATH_FOR_IMAGE_OFFER_APP_DETAIL = PATH_FOR_IMAGE_OFFER_APP + "DETAIL/";
	public static final String PATH_FOR_IMAGE_OFFER_APP_LUCKY_OFFER_NEW = PATH_FOR_IMAGE_OFFER_APP + "LUCKY_OFFER/";
	
	public static final String DOMAIN_NAME_MERCHANT = "MERCHANT";
	public static final String DOMAIN_NAME_OFFER = "OFFER";
	public static final String APPEND_EXTENSION_PERIOD = ".";
	
	public static final String CHANNEL_SWEB = "SWEB";
	public static final String CHANNEL_SAPP = "SAPP";
	
	//MERCHANT WEB + APP IMAGE TYPE
	public static final String MERCHANT_WEB_LISTING = "LISTING";
	public static final String MERCHANT_WEB_DETAIL = "DETAIL";
	public static final String MERCHANT_APP_COUPONS = "COUPONS";
	public static final String MERCHANT_APP_DETAIL = "DETAIL";
	public static final String MERCHANT_APP_LISTING = "LISTING";
	public static final String MERCHANT_APP_LIST_LOGO = "LIST_LOGO";
	
	//OFFER WEB + APP IMAGE TYPE
	public static final String OFFER_WEB_DETAIL = "DETAIL";
	public static final String OFFER_WEB_LISTING = "LISTING";
	public static final String OFFER_WEB_MY_COUPONS = "MYCOUPONS";
	public static final String OFFER_WEB_FEATURED = "FEATURED";
	public static final String OFFER_APP_DOD_LISTING_NEW = "DOD_LISTING";
	public static final String OFFER_APP_SMALL_HORIZONTAL_NEW = "SMALL_HORIZONTAL";
	public static final String OFFER_APP_DETAIL = "DETAIL";
	public static final String OFFER_APP_LUCKY_OFFER_NEW = "LUCKY_OFFER";
		
	//GIFTING WEB + APP IMAGE TYPE
	public static final String GIFTING_IMAGE = "GIFTING";
	public static final String BANNER_IMAGE = "BANNER";
	
	//IMAGE CATEGORY
	public static final String IMAGE_CATEGORY_MERCHANT_OFFER = "MerchantOffer";
	public static final String IMAGE_CATEGORY_BANNER = "Banner";
	public static final String IMAGE_CATEGORY_GIFTING = "Gifting";
	
	//BANNER POSITION
	public static final String BANNER_POSITION_TOP = "TOP";
	public static final String BANNER_POSITION_MIDDLE = "MIDDLE";
	public static final String BANNER_POSITION_BOTTOM = "BOTTOM";
	
	//GIFTING PROPERTIES
	public static final String GIFTING_TYPE_SOLID = "SOLID";
	public static final String GIFTING_TYPE_IMAGE = "IMAGE";
	
	//BANNER PROPERTIES
	public static final String BANNER_PROPERTY_INSERT = "INSERT";
	public static final String BANNER_PROPERTY_UPDATE = "UPDATE";
	
	public static final List<String> CHANNEL_LIST = Collections
			.unmodifiableList(Arrays.asList(CHANNEL_SWEB, CHANNEL_SAPP));

	public static final List<String> DOMAIN_NAME_LIST = Collections
			.unmodifiableList(Arrays.asList(DOMAIN_NAME_MERCHANT, DOMAIN_NAME_OFFER));

	public static final List<String> MERCHANT_SWEB_IMAGE_TYPE_LIST = Collections
			.unmodifiableList(Arrays.asList(MERCHANT_WEB_LISTING, MERCHANT_WEB_DETAIL));

	public static final List<String> MERCHANT_SAPP_IMAGE_TYPE_LIST = Collections.unmodifiableList(
			Arrays.asList(MERCHANT_APP_COUPONS, MERCHANT_APP_DETAIL, MERCHANT_APP_LISTING, MERCHANT_APP_LIST_LOGO));

	public static final List<String> OFFER_SWEB_IMAGE_TYPE_LIST = Collections.unmodifiableList(
			Arrays.asList(OFFER_WEB_DETAIL, OFFER_WEB_LISTING, OFFER_WEB_MY_COUPONS, OFFER_WEB_FEATURED));

	public static final List<String> OFFER_SAPP_IMAGE_TYPE_LIST = Collections
			.unmodifiableList(Arrays.asList(OFFER_APP_DOD_LISTING_NEW, OFFER_APP_SMALL_HORIZONTAL_NEW,
					OFFER_APP_DETAIL, OFFER_APP_LUCKY_OFFER_NEW));
	
	public static final List<String> BANNER_POSITION_LIST = Collections
			.unmodifiableList(Arrays.asList(BANNER_POSITION_TOP, BANNER_POSITION_MIDDLE, BANNER_POSITION_BOTTOM));

}
