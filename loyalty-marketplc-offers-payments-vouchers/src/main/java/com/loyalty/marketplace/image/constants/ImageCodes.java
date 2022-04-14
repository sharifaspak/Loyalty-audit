package com.loyalty.marketplace.image.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

public enum ImageCodes implements IMarketPlaceCode {
	
	VALIDATOR_DELIMITOR(" "),
	
	//GENERAL CODES
	INVALID_PARAMETER(8000, "Invalid input parameters"),
	GENERIC_RUNTIME_EXCEPTION(8001, "Runtime Exception occurred. Please refer logs."),
	IMAGE_FILE_WRITE_EXCEPTION(8002, "Marketplace image file write exception. Please refer logs."),
	DATE_STRING_PARSE_ERROR(8003, "Parsing input date string to date format failed."),
	VALIDATION_EXCEPTION(8004, "ModelMapper validation error while mapping using model mapper."),
	
	//GENERAL IMAGE CODES
	MARKETPLACE_IMAGE_UPLOAD_FAILED(8010, "Marketplace image upload failed."),
	INVALID_IMAGE_FORMAT(8011, "Invalid image format. Allowed image formats: "),
	INVALID_DOMAIN_NAME(8012, "Invalid domain name: "),
	INVALID_CHANNEL(8013, "Invalid channel. Must be SWEB or SAPP."),
	IMAGE_EXISTS_FOR_IMAGE_TYPE(8014, "Image already exists for the image type, domain id and channel: "),
	IMAGE_REQUEST_FILE_MANDATORY(8015, "Uploading an image is mandatory."),
	INVALID_IMAGE_CATEGORY(8016, "Invalid image category."),
	INVALID_STATUS(8017, "Invalid status. It should be ACTIVE or INACTIVE."),
	
	//MERCHANT IMAGE CODES
	IMAGE_UPLOAD_MERCHANT_SUCCESS(8020, "Marketplace image upload for merchant success."),
	IMAGE_UPLOAD_MERCHANT_FAILED(8021, "Marketplace image upload for merchant failed."),
	MERCHANT_INVALID_IMAGE_DIMENSIONS(8022, "Invalid image dimensions for merchants. "),
	MERCHANT_INVALID_IMAGE_TYPE_WEB(8023, "Invalid image type for merchants for SWEB."),
	MERCHANT_INVALID_IMAGE_TYPE_APP(8024, "Invalid image type for merchants for SAPP."),
	MERCHANT_NOT_AVAILABLE(8025, "Merchant does not exist for the merchantCode: "),
	
	//OFFER IMAGE CODES
	IMAGE_UPLOAD_OFFER_SUCCESS(8030, "Marketplace image upload for offer success."),
	IMAGE_UPLOAD_OFFER_FAILED(8031, "Marketplace image upload for offer failed."),
	OFFER_INVALID_IMAGE_DIMENSIONS(8032, "Invalid image dimensions for offer. "),
	OFFER_INVALID_IMAGE_TYPE_WEB(8033, "Invalid image type for offer for SWEB."),
	OFFER_INVALID_IMAGE_TYPE_APP(8034, "Invalid image type for offer for SAPP."),
	OFFER_NOT_AVAILABLE(8035, "Offer does not exist for the offerId: "),
	
	//BANNER IMAGE CODES
	BANNER_IMAGE_UPLOAD_SUCCESS(8040, "Banner image uploaded successfully."),
	BANNER_IMAGE_UPLOAD_FAILED(8041, "Banner image upload failed."),
	BANNER_REQUEST_OFFER_TYPE_MANDATORY(8042, "Offer type is a mandatory field when offer id is provided."),
	BANNER_REQUEST_OFFER_DEEPLINK_MANDATORY(8043, "Offer deep link is a mandatory field when offer id and offer type is provided."),
	BANNER_OFFER_UNAVAILABLE(8044,"No offer available for the offerId: "),
	BANNER_OFFER_TYPE_MISMATCH(8045,"Offer id is not linked to the provided offer type: "),
	BANNER_NO_ASSOCIATED_OFFER_TYPE(8046,"No offer type associated with this offer id: "),
	BANNER_IMAGE_INVALID_DIMENSIONS(8047, "Invalid image dimensions for banners. "),
	INVALID_BANNER_POSITION(8048, "Invalid banner position. It can be either TOP, MIDDLE or BOTTOM."),
	BANNER_IMAGE_EXISTS_FOR_GIVEN_NAME(8049,"Banner image already exists for the given file name: "),
	
	//GIFTING IMAGE CODES
	GIFTING_IMAGE_UPLOAD_SUCCESS(8050, "Gifting image uploaded successfully."),
	GIFTING_IMAGE_UPLOAD_FAILED(8051, "Gifting image upload failed."),
	GIFTING_REQUEST_COLOUR_CODE_MANDATORY(8052, "Colour code is a mandatory field when type is SOLID."),
	GIFTING_REQUEST_COLOUR_DIRECTION_MANDATORY(8053, "Colour direction is a mandatory field when type is SOLID."),
	GIFTING_IMAGE_INVALID_DIMENSIONS(8054, "Invalid image dimensions for gifting image. "),
	GIFTING_INVALID_IMAGE_TYPE(8055, "Invalid gifting image type. It can be either SOLID or IMAGE."),
	GIFTING_IMAGE_EXISTS_FOR_GIVEN_NAME(8056,"Gifting image already exists for the given file name: "),
	
	//IMAGE UPDATION CODES
	IMAGE_UPDATE_SUCCESS(8060,"Image updated successfully."),
	IMAGE_UPDATE_FAILED(8061,"Image updation failed."),
	UPDATE_IMAGE_NOT_FOUND(8062,"Image to be updated not found."),
	IMAGE_MANDATORY_FOR_BANNER_POSITION_CHANGE(8063, "Banner image is mandatory when banner position needs to be updated."),
	GIFTING_IMAGE_TYPE_UPDATE_NOT_ALLOWED_IMAGE_TO_SOLID(8064, "Cannot update gifting type IMAGE to SOLID."),
	GIFTING_IMAGE_TYPE_UPDATE_NOT_ALLOWED_SOLID_TO_IMAGE(8065, "Cannot update gifting type SOLID to IMAGE."),
	INVALID_INPUT_FOR_BOOLEAN(8066, "Invalid input. It can be either YES or NO: "),
	
	//BANNER PROPERTIES CODES
	BANNER_PROPERTY_CONFIGURE_SUCCESS(8070,"Banner property created successfully."),
	BANNER_PROPERTY_CONFIGURE_FAILURE(8071,"Banner property creation failed."),
	BANNER_PROPERTY_UPDATE_SUCCESS(8072,"Banner property updated successfully."),
	BANNER_PROPERTY_UPDATE_FAILURE(8073,"Banner property updation failed."),
	EXISTING_BANNER_PROPERTY_NOT_FOUND(8074,"Existing banner property not found to update."),
	INVALID_VALUE_INCL_REDEEMED_OFFER(8075,"Invalid value for include redeemed offers. It can be either YES or NO."),
	BANNER_PROPERTY_MONGO_EXCEPTION(8076, "MongoDB exception. Failed to insert or update banner properties."),
	
	//LISTING IMAGES CODES
	LISTING_MARKETPLACE_IMAGES_SUCCESS(8080, "Marketplace images listed successfully."),
	LISTING_MARKETPLACE_IMAGES_FAILED(8081, "Marketplace images listing failed."),
	NO_IMAGES_AVAILABLE_TO_LIST(8082, "No marketplace images available for listing."),
	LIST_BANNER_PROPERTY_SUCCESS(8083,"Listing banner properties success."),
	LIST_BANNER_PROPERTY_FAILED(8084,"Listing banner properties failed."),	
	NO_BANNER_PROPERTY_AVAILABLE(8085,"No banner property available to list."),	
	LISTING_INVALID_IMAGE_TYPE(8086,"Invalid image type. It can be either BANNER, GIFTING or empty."),	
	LISTING_INVALID_STATUS(8087,"Invalid image type. It can be either ACTIVE or empty."),
	DISPLAY_IMAGE_FAILED(8089, "Marketplace image failed to display."),
	NO_IMAGE_AVAILABLE_FOR_ID(8090, "No image available for the id: "),
	
	//NGINX RESPONSE CODES
	NGINX_ERROR_RESPONSE(8091, "Error response received from nginx server."),
	NGINX_SERVER_CONNECTION_ERROR(8092, "Error connecting to nginx server for image upload."),
	NGINX_SERVER_CONNECTION_ERROR_META_DATA(8093, "Error connecting to nginx server for metadata retrieval."),
	NGINX_ERROR_RESPONSE_DELETE_IMAGE(8094, "Error response received from nginx server which deleting image. Please refer logs."),
	SECOND_NGINX_UPLOAD_ERROR_RESPONSE(8095, "Error response received from second nginx server."),
	NGINX_SERVER_SECOND_CONNECTION_ERROR(8096, "Error connecting to second nginx server for image upload."),
	
	//BULK UPLOAD CODES
	BULK_IMAGE_UPLOAD_SUCCESS(8097, "Meta data generation successful."),
	BULK_IMAGE_UPLOAD_FAILED(8098, "Meta data generation failed."),
	DIRECTORY_NOT_FOUND(8099, "Directory not found. Error received from nginx server."),
	META_DATA_ALREADY_GENERATED(8100, "Metadata has already been generated for all images for the image type: "),
	
	//OTHERS
	UPDATE_ELIGIBLE_OFFERS_FAILED(8101, "Failed to update eligible offers.");
	
	private int id;
	private String msg;
	private String constant;
	
	ImageCodes(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	ImageCodes(String constant) {
		this.constant = constant;
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

	public String getConstant() {
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

}
