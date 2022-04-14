package com.loyalty.marketplace.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImageConstants {

	private ImageConstants() {}

	//GENERIC CONSTANTS
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String COMMA_SEPARATOR = ", ";
	public static final String ACTION_INSERT = "Insert";
	public static final String ACTION_UPDATE = "Update";
	
	//LOG CONSTANTS:
	public static final String CLASS_NAME = "CLASS: {} | ";
	public static final String METHOD_NAME = "METHOD: {} | ";
	public static final String REQUEST_PARAMS = "Request Parameters: {}";
	public static final String RESPONSE_PARAMS = "Response Parameters: {}";
	public static final String IMAGE_EXTENSION = "Uploaded Image Extension: {}";
	public static final String IMAGE_DIMENSION = "Uploaded Image Dimensions: {}*{}";
	public static final String CREATING_IMAGE_DIRECTORIES = "Creating image directories.";
	public static final String SAVED_TO_PATH = "Saved Image To Path: {}";
	public static final String PWD = "Present Working Directory: {}";
	public static final String IMAGE_UPLOAD_API_REQUEST_PARAMS = "Path, Action: {}, {}.";
	public static final String IMAGE_UPDATE_API_REQUEST_PARAMS = "Path, Existing Path, Action: {}, {}, {}.";
	public static final String META_DATA_API_REQUEST_PARAMS = "Path: {}.";
	public static final String META_DATA_LIST_FILES = "Number of files in directory: {}.";
	public static final String DELETE_IMAGE_REQUEST_PARAMS = "Path: {}";
	public static final String FORWARD_SLASH = "/";
	public static final String PERIOD = ".";
	public static final String SCHEDULED_LOG_START = "---------- START | Marketplace-images LOGs for {} ----------";
	public static final String SCHEDULED_LOG_END = "---------- END | Marketplace-images LOGs for {} ----------";
	public static final String LOG_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
	
	//PATH TO UPLOAD IMAGES
	public static final String PRESENT_DIRECTORY = "/Platform_dev/app/";
	public static final String IMAGES_DIRECTORY = PRESENT_DIRECTORY + "images/";
	
	public static final String PATH_FOR_IMAGE_BANNER = IMAGES_DIRECTORY + "BANNERs/";
	public static final String PATH_FOR_IMAGE_GIFTING = IMAGES_DIRECTORY + "GIFTING/";
	
	public static final String PATH_FOR_IMAGE_WEB = IMAGES_DIRECTORY + "WEB/";
	public static final String PATH_FOR_IMAGE_APP = IMAGES_DIRECTORY + "APP/";
	
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

	
	//ALLOWED IMAGE FORMATS
	public static final String JPG_LC = "jpg";
	public static final String JPG_UC = "JPG";
	public static final String JPEG_UC = "JPEG";
	public static final String JPEG_LC = "jpeg";
	public static final String PNG_UC = "PNG";
	public static final String PNG_LC = "png";
	
	public static final List<String> ALOWED_IMG_FORMATS = Collections
			.unmodifiableList(Arrays.asList(JPG_LC, JPG_UC, JPEG_UC, JPEG_LC, PNG_UC, PNG_LC));
	
}
