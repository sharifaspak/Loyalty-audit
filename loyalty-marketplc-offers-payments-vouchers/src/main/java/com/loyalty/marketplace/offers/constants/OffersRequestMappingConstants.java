package com.loyalty.marketplace.offers.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

/**
 * 
 * @author jaya.shukla
 *
 */
public class OffersRequestMappingConstants extends RequestMappingConstants{

	private OffersRequestMappingConstants() {}

	public static final String OFFER_ID = "offerId"; 
	public static final String OFFER_CODE = "offerCode"; 
	public static final String MERCHANT_CODE = "merchantCode";
	public static final String OFFER_STATUS = "status"; 
	public static final String CATEGORY_ID = "categoryId";
	public static final String PARENT_CATEGORY = "parentcategory";
	public static final String SUBCATEGORY_ID = "subCategoryId";
	public static final String STATUS = "status";
	public static final String ROLE = "role";
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String OFFER_TYPE_ID= "offerTypeId";
	public static final String FILTER_FLAG = "filterFlag";
	public static final String KEYWORDS = "keywords";
	public static final String PURCHASE_ITEM = "purchaseItem";
	public static final String OFFER_TYPE = "offerType";
	public static final String PAGE = "page";
	public static final String LIMIT = "limit";
	public static final String DAYS_TO_ADD = "noOfdaysToadd";
	public static final String CATEGORY_ID_LIST = "categoryIdList";
	public static final String FILE = "file";
	
	//All controller API end points
	public static final String CREATE_OFFER = "/offers";
	public static final String UPDATE_OFFER = "/offers/{offerId}";
	public static final String GET_OFFER_LIST_ADMINISRATOR = "/offers";
	public static final String GET_OFFER_LIST_PARTNER = "/partner/{partnerCode}/offers";
	public static final String GET_OFFER_LIST_MERCHANT = "/merchant/{merchantCode}/offers";
	public static final String GET_OFFER_DETAIL_PORTAL = "/offers/{offerId}";
	public static final String GET_OFFER_LIST_MEMBER = "/eligibleOffers";
	public static final String CONFIGURE_ELIGIBLE_OFFER = "/configureEligibleOffers";
	public static final String GET_OFFER_DETAIL_MEMBER = "/eligibleOffers/{offerId}";
	public static final String GET_ELIGIBLE_PAYMENT_METHOD = "offer/{purchaseItem}/paymentMethod";
	public static final String PURCHASE = "/payment";
	public static final String RESET_ALL_COUNTER = "/resetAllCounter"; 
	public static final String GET_ALL_TRANSACTIONS = "/transactions";
	public static final String OFFER_TYPE_DETAIL = "/offers/offerType/{offerType}";
	public static final String RATE_OFFER = "/offer/rateOffer";
	public static final String BATCH_PROCESS_FOR_BIRTHDAY_OFFERS = "/offers/birthdayGifts";
	public static final String SUBSCRIPTION_STATUS = "/isSubscribed/{accountNumber}";
	public static final String GET_MEMBER_BIRTHDAY_GIFTS  = "member/offers/birthdayGift";
	public static final String CREATE_CUSTOMER_SEGMENT = "/customerSegment";
	public static final String UPDATE_CUSTOMER_SEGMENT = "/customerSegment/{customerSegmentId}";
	public static final String CREATE_UPDATE_WISHLIST = "/member/wishlist/{accountNumber}";	
	public static final String GET_FROM_WISHLIST = "/member/wishlist/{accountNumber}";
	public static final String CONFIGURE_BIRTHDAY_INFORMATION = "/birthdayInfo";
	public static final String RETRIEVE_BIRTHDAY_INFORMATION_ACCOUNT = "/birthdayInfo/{accountNumber}";
	public static final String RETRIEVE_BIRTHDAY_INFORMATION_ADMIN = "/birthdayInfo";
	public static final String RETRIEVE_SINGLETON_OFFERS = "/singletonEligibleOffers";
	public static final String PROMOTIONAL_GIFT = "/promotionalGift";

	public static final String GET_REFUND_TRANSACTIONS = "/refundTransactions"; 
	public static final String OFFER_CATALOG_BULK_UPDATE = "/bulkUpdate/offerCatalog";
	public static final String RETRIEVE_RESTAURANTS = "/restaurants";

}
