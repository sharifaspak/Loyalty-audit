package com.loyalty.marketplace.offers.constants;

import com.loyalty.marketplace.constants.MarketplaceDBConstants;

/**
 * 
 * @author jaya.shukla
 *
 */
public class OffersDBConstants extends MarketplaceDBConstants{

	protected OffersDBConstants() {}
	
	//All DB constants
	public static final String OFFER_CATALOG = "OfferCatalog";
	public static final String ELIGIBLE_OFFERS = "EligibleOffers";
	public static final String OFFER_COUNTER = "OfferCounter";
	public static final String PURCHASE_PAYMENT_METHOD = "PurchasePaymentMethod";
	public static final String PURCHASE_HISTORY = "PurchaseHistory";
	public static final String OFFER_RATING = "OfferRating";
	public static final String BIRTHDAY_HELPER = "BirthdayGiftTracker";
	public static final String BIRTHDAY_INFO = "BirthdayInformation";
	public static final String WISHLIST = "Wishlist";
	public static final String ERROR_RECORDS = "ErrorRecords";
	public static final String OFFER_COUNTERS = "OfferCounters";
	public static final String ACCOUNT_OFFER_COUNTERS = "AccountOfferCounter";
	public static final String MEMBER_OFFER_COUNTERS = "MemberOfferCounter";
	
	public static final String ID = "_id" ;
	
	//Offer Catalog Fields
	public static final String STATUS = "Status";
	public static final String AVAILABLE_IN_PORTAL = "AvailableInPortals";
	public static final String IS_BIRTHDAY_GIFT = "IsBirthdayGift";	
	public static final String OFFER_DATE = "OfferDate";
	public static final String OFFER_END_DATE = "OfferDate.EndDate";
	public static final String SUBCATEGORY = "SubCategory";
	public static final String MERCHANT_CODE = "MerchantCode";
	public static final String OFFER_STORES = "OfferStores";
	public static final String TAGS = "Tags";
	public static final String TAGS_EN = "Tags.English";
	public static final String TAGS_AR = "Tags.Arabic";
	public static final String GROUPED_FLAG = "GroupedFlag";
	public static final String NEW_OFFER = "NewOffer";
	public static final String CUSTOMER_TYPES = "CustomerType";
	public static final String CUSTOMER_TYPES_ELIGIBLE = "CustomerType.Eligible";
	public static final String CUSTOMER_TYPES_EXCLUSION = "CustomerType.Exclusion";
	public static final String CUSTOMER_SEGMENTS = "CustomerSegments";
	public static final String CUSTOMER_SEGMENTS_ELIGIBLE = "CustomerSegments.Eligible";
	public static final String CUSTOMER_SEGMENTS_EXCLUSION = "CustomerSegments.Exclusion";
	public static final String RULES = "Rules";
	public static final String PARTNER_CODE = "PartnerCode";
	public static final String OFFER_CODE = "OfferCode";
	public static final String OFFER_TYPE = "OfferType";
	public static final String TITLE_EN = "Offer.Title.English";
	public static final String TITLE_AR = "Offer.Title.Arabic";
	public static final String TITLE_DESCRIPTION_EN = "Offer.Description.English";
	public static final String TITLE_DESCRIPTION_AR = "Offer.Description.Arabic";
	public static final String MERCHANT_ID = "Merchant.$id";
	public static final String STORE_ID = "OfferStores.$id";
	public static final String OFFER_CATEGORY_ID = "Category.$id";
	public static final String OFFER_SUBCATEGORY_ID = "SubCategory.$id";
	public static final String MERCHANT_NAME_EN = "Merchant.Name.English";
	public static final String MERCHANT_NAME_AR = "Merchant.Name.Arabic";
	public static final String VOUCHER_ACTION = "VoucherInfo.VoucherAction";

	//Category Fields
	public static final String CATEGORY_ID = "CategoryId";
	
	//PurchaseHistory Fields
	public static final String OFFER_ID = "OfferId";
	public static final String PURCHASE_ITEM = "PurchaseItem";
	public static final String ACCOUNT_NUMBER = "AccountNumber";
	public static final String MEMBERSHIP_CODE = "MembershipCode";
	public static final String CREATED_DATE = "CreatedDate";
	public static final String TRANSACTION_TYPE = "TransactionType";
	public static final String ADDITIONAL_DETAILS = "AdditionalDetails";
	public static final String IS_BIRTHDAY = "AdditionalDetails.IsBirthdayOffer";
	public static final String PROGRAM_CODE = "ProgramCode";

	public static final String CHANNEL_ID = "ChannelId";
	
	public static final String PAYMENT_METHOD = "PaymentMethod";
	public static final String SPENT_POINTS = "SpentPoints";

	//EligibleOffers Fields
	public static final String LAST_UPDATE_DATE = "LastUpdateDate";
	
	//SubscriptionCatalogFields
	public static final String POINTS_VALUE = "PointsValue";
	
	//Counter fields
	public static final String ANNUAL_COUNT = "AnnualCount";
	public static final String MONTHLY_COUNT = "MonthlyCount";
	public static final String WEEKLY_COUNT = "WeeklyCount";
	public static final String DAILY_COUNT = "DailyCount";
	public static final String TOTAL_COUNT = "TotalCount";
	public static final String LAST_PURCHASED_DATE = "LastPurchased";
	public static final String DENOMINATION_ANNUAL_COUNT = "DenominationCount.$[].AnnualCount";
	public static final String DENOMINATION_MONTHLY_COUNT = "DenominationCount.$[].MonthlyCount";
	public static final String DENOMINATION_WEEKLY_COUNT = "DenominationCount.$[].WeeklyCount";
	public static final String DENOMINATION_DAILY_COUNT = "DenominationCount.$[].DailyCount";
	public static final String DENOMINATION_TOTAL_COUNT = "DenominationCount.$[].TotalCount";
	public static final String DENOMINATION_LAST_PURCHASED_DATE = "DenominationCount.$[].LastPurchased";
	public static final String DENOMINATION_COUNT = "DenominationCount";
	
	
	//RefundTransaction fields
	public static final String REFUND_STATUS = "RefundStatus";
		
	//Collections and fields from Other Microservices
	public static final String CUSTOMER_TYPE = "CustomerType";
	public static final String ELIGIBILITY_MATRIX = "EligiblityMatrix";
	
		
}
