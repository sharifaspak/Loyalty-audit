package com.loyalty.marketplace.constants;

public class RequestMappingConstants {
	
	protected RequestMappingConstants() {}

	public static final String MARKETPLACE = "Marketplace"; 
	public static final String MARKETPLACE_BASE = "/marketplace";
	
	public static final String PROGRAM="program";
	public static final String AUTHORIZATION="Authorization";
	public static final String EXTERNAL_TRANSACTION_ID="externalTransactionId";
	public static final String USER_NAME="userName";
	public static final String PASSWORD="password";
	public static final String SESSION_ID="sessionId";
	public static final String USER_PREV="userPrev";
	public static final String CHANNEL_ID="channelId";
	public static final String SYSTEM_ID="systemId";
	public static final String SYSTEM_CREDENTIAL="systemPassword";
	public static final String TOKEN="token";
	public static final String TRANSACTION_ID="transactionId";
	public static final String FILE="file";
	public static final String IMAGE_NAME="imageName";
	public static final String IMAGE_TITLE_EN="imageTitleEn";
	public static final String IMAGE_TITLE_AR="imageTitleAr";
	public static final String IMAGE_DESC_EN="imageDescriptionEn";
	public static final String IMAGE_DESC_AR="imageDescriptionAr";
	public static final String IMAGE_TYPE="imageType";
	public static final String DOMAIN_ID="domainId";
	public static final String DOMAIN_NAME="domainName";
	public static final String STATUS_SUCCESS="Success";
	public static final String STATUS_ERROR="Failed";
	public static final String USER_ROLE = "userRole";
	public static final String UNATHOURIZED_USER = "Unauthorized user";
	 
	public static final String REPROCESS_EVENTS_LIST = "/reprocessJmsEvent/listCorrelationIds";
	public static final String REPROCESS_EVENTS_ALL = "/reprocessJmsEvent/reprocessAllJmsEvent";
	public static final String REPROCESS_EVENTS = "/reprocessJmsEvent/{correlationId}";

	public static final String LIST_ALL_PAYMENT_METHODS = "/paymentMethod";
	public static final String LIST_ALL_CATEGORIES = "/category";
	public static final String LIST_ALL_DENOMINATIONS = "/denominations";
	public static final String LIST_ALL_OFFER_TYPES = "/offerType";
	public static final String LIST_ALL_SUBCATEGORIES = "/subCategories";
	 
}
