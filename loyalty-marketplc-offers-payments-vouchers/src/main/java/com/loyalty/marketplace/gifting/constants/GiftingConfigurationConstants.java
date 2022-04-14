package com.loyalty.marketplace.gifting.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class GiftingConfigurationConstants extends RequestMappingConstants {

	//All DB constants
	public static final String GIFTING_HISTORY = "GiftingHistory"; 
	public static final String GIFTING_LIMIT = "GiftingLimit"; 
	public static final String GIFTING_COUNTER = "GiftingCounter";
	public static final String BIRTHDAY_REMINDER = "BirthdayReminder";
	public static final String GIFTS = "Gifts";
	
	//REQUEST CONSTANTS
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String MEMBERSHIP_CODE = "membershipCode";
	public static final String GIFTING_LIMIT_ID = "id";
	public static final String GIFT_TYPE = "giftType";
	public static final String FILTER = "filter";
	public static final String GIFT_ID = "giftId";
	public static final String SEND_GIFT_ADHOC_GIFT_ID = "id";
	public static final String ID = "id";

	//ENDPOINT URLs
	public static final String MARKETPLACE = "Marketplace";
	public static final String MARKETPLACE_BASE = "/marketplace";
	
	public static final String LIST_GOLD_TRANSACTIONS = "/offers/goldTransaction";
	public static final String CREATE_BIRTHDAY_REMINDER_LIST = "/birthdayReminder";
	public static final String ACCEPT_REJECT_BIRTHDAY_REMINDER = "/birthdayReminderStatus";
	public static final String NOTIFY_MEMBER_BIRTHDAY = "/memberBirthday";
	
	public static final String GIFT = "/gift";
	public static final String CANCEL_GIFT = "/cancelGift";
	public static final String PROMOTIONAL_GIFT = "/promotionalGift";
	public static final String LIST_GIFT_HISTORY = "/gift/{accountNumber}";
	public static final String GIFT_LIMIT = "/giftLimit";
	public static final String UPDATE_GIFT_LIMIT = "/giftLimit/{id}";
	public static final String RESET_COUNTER = "/resetCounter";
	public static final String SEND_GIFT = "/sendGift";
	public static final String SEND_GIFT_ADHOC = "/sendGift/{id}";
	public static final String GIFT_ACCOUNT_CHANGE = "/gift/accountChange";
	
	public static final String GIFT_PREMIUM_VOUCHER = "/giftPremiumVoucher";
	
	//GiftingConfiguration
	public static final String CREATE_GIFT = "/gifting";
	public static final String UPDATE_GIFT = "/gifting/{id}";
	public static final String DELETE_GIFT = "/gifting/{id}";
	public static final String LIST_GIFT = "/gifting";
	public static final String GIFT_DETAIL = "/gifts/{id}";
	
	
	//METHOD NAMES
	public static final String CONTROLLER_HELPER_BIRTHDAY_REMINDER_NEW_MEMBER = "createBirthdayReminderListNewMember";
	public static final String CONTROLLER_HELPER_BIRTHDAY_REMINDER_EXISTING_MEMBER = "createBirthdayReminderListExistingMember";
	public static final String CONTROLLER_HELPER_BIRTHDAY_PUSH_NOTIFICATION = "sendPushNotificationBirthdayReminder";
	public static final String CONTROLLER_HELPER_BIRTHDAY_REMIND_DAY = "remindMemberBirthdayPushNotificationDay";
	public static final String CONTROLLER_HELPER_BIRTHDAY_REMIND_WEEK = "remindMemberBirthdayPushNotificationWeek";
	public static final String CONTROLLER_HELPER_BIRTHDAY_REMIND_MONTH = "remindMemberBirthdayPushNotificationMonth";
	public static final String CONTROLLER_HELPER_GIFT_VOUCHER = "giftVoucher";
	public static final String CONTROLLER_HELPER_GIFT_POINTS = "giftPoints";
	public static final String CONTROLLER_HELPER_GIFT_GOLD = "giftGold";
	public static final String CONTROLLER_HELPER_MEMBER_ACCRUAL_REDEMTION = "memberAccrualRedemption";
	public static final String CONTROLLER_HELPER_POINTS_AVAILABILITY = "checkMemberPointsAvailability";
	public static final String CONTROLLER_HELPER_CHECK_LIMITS = "checkLimits";
	public static final String CONTROLLER_HELPER_SEND_SCHEDULED_GIFT = "sendScheduledGift";
	public static final String CONTROLLER_HELPER_SEND_SCHEDULED_GIFT_ADHOC = "sendScheduledGiftAdhoc";
	public static final String CONTROLLER_HELPER_GIFT_CREATE_NOTIFICATION_DTO = "createNotificationHelperDto";
	public static final String CONTROLLER_HELPER_GIFT_LIST_SPECIFIC_HISTORY = "listSpecificGiftingHistory";
	public static final String CONTROLLER_HELPER_ENROLL_RECEIVER = "enrollReceiverValidation";
	public static final String BIRTHDAY_REMINDER_DOMAIN_CONFIGURE_LIST = "configureBirthdayReminderList";
	public static final String BIRTHDAY_REMINDER_DOMAIN_ACCEPT_REJECT = "acceptRejectBirthdayReminder";
	public static final String BIRTHDAY_REMINDER_DOMAIN_SAVE_DB = "saveUpdateBirthdayReminder";
	public static final String BIRTHDAY_REMINDER_DOMAIN_NOTIFY_MEMBER = "notifyMemberBirthday";
	
	public static final String GIFTING_COUNTER_DOMAIN_SAVE_DB = "saveUpdateGiftingCounter";
	public static final String GIFTING_LIMIT_DOMAIN_SAVE_DB = "saveUpdateGiftingLimits";
	public static final String GIFTING_CERTIFICATE_DOMAIN_SAVE_DB = "listGoldTransactions";
	public static final String GIFTING_LIMIT_CONFIGURE = "configureLimits";
	
	public static final String GIFTING_HISTORY_DOMAIN_SAVE_DB = "saveUpdateGiftingHistory";
	public static final String GIFTING_HISTORY_DOMAIN_LIST = "listGiftingHistory";
	public static final String GIFTING_HISTORY_DOMAIN_POPULATE_SENT = "populateSentListGiftResponse";
	public static final String GIFTING_HISTORY_DOMAIN_POPULATE_RECEIVED = "populateReceivedListGiftResponse";
	public static final String GIFTING_HISTORY_DOMAIN_POPULATE_ALL = "populateAllListGiftResponse";

	public static final String GIFTING_SERVICE_BIRTHDAY_PUSH_NOTIFICATION = "pushNotificationBirthdayReminder";
	public static final String GIFTING_SERVICE_ENROLL_MEMBER = "enrollMember";
	public static final String GIFTING_SERVICE_RETRIEVE_MEMBER_POINTS = "retrieveMemberPoints";
	public static final String GIFTING_SERVICE_ACCRUAL_REDEMPTION = "memberAccrualRedemption";
	public static final String GIFTING_SERVICE_SMS = "sendSMS";
	public static final String GIFTING_SERVICE_EMAIL = "sendEmail";
	public static final String GIFTING_SERVICE_LIST_MEMBER_DETAILS = "getListMemberDetails";
	public static final String GIFTING_SERVICE_LIST_GIFT_HISTORY_PUSH_NOTIFICATION = "pushNotificationViewedGift";
	public static final String GIFTING_SERVICE_LIST_GIFT_HISTORY_EMAIL = "emailViewedGift";
	
	public static final String CONTROLLER_GIFTING = "marketplaceGifting";
	public static final String CONTROLLER_SCHEDULED_GIFT = "sendScheduledGift";
	public static final String CONTROLLER_SCHEDULED_GIFT_ADHOC = "sendScheduledGiftAdhoc";
	public static final String CONTROLLER_RESET_COUNTER = "resetGiftingCounters";
	public static final String CONTROLLER_LIST_GIFTING_HISTORY = "listGiftingHistory";
	public static final String CONTROLLER_LIST_SPECIFIC_GIFTING_HISTORY = "listSpecificGiftingHistory";
	public static final String CONTROLLER_CONFIGURE_LIMITS = "configureGiftingLimits";
	public static final String CONTROLLER_UPDATE_LIMITS = "updateGiftingLimits";
	public static final String CONTROLLER_LIST_LIMITS = "listGiftingLimits";
	public static final String CONTROLLER_CONFIGURE_BIRTHDAY_LIST = "configureBirthdayReminderList";
	public static final String CONTROLLER_ACCEPT_REJECT_REMINDER = "acceptRejectBirthdayReminder";
	public static final String CONTROLLER_NOTIFY_REMINDER = "notifyMemberBirthday";
	public static final String CONTROLLER_LIST_GOLD_TRANSACTION = "listGoldTransaction";
	public static final String CONTROLLER_CANCEL_GIFT = "cancelVoucherGift";
	public static final String CONTROLLER_HELPER_CANCEL_GIFT_DB = "cancelVoucherUpdateDB";
	public static final String CONTROLLER_HELPER_CANCEL_GIFT_RETRIVED_CRFR_BAL = "retrieveCRFRBalance";
	
	
	public static final String MONGO_ID = "_id";
	public static final String IS_ACTIVE = "IsActive";
	public static final String OFFER_VALUES = "OfferValues";
	public static final String MIN_POINT_VALUE = "GiftDetails.MinPointValue";
	public static final String MAX_POINT_VALUE = "GiftDetails.MaxPointValue";
	public static final String PROMOTIONAL_GIFT_ID = "GiftDetails.PromotionalGiftId";
	public static final String SUBSCRIPTION_CATALOG_ID = "GiftDetails.SubscriptionCatalogId";
	
	//Action
	public static final String INSERT = "INSERT";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	
	

}
