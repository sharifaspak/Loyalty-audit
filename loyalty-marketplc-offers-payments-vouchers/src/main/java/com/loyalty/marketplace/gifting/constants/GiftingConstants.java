package com.loyalty.marketplace.gifting.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiftingConstants {
	
	//GENERIC CONSTANTS
	public static final String COMMA_SEPARATOR = ", ";
	public static final String LOG_ENTERING = "ENTERING ";
	public static final String LOG_LEAVING = "EXITING ";
	public static final String CLASS_NAME = "CLASS: {} | ";
	public static final String METHOD_NAME = "METHOD: {} | ";
	public static final String API_NAME = "API: {}";
	public static final String REQUEST_PARAMS = "Request Parameters: {}";
	public static final String RESPONSE_PARAMS = "Response Parameters: {}";
	public static final String DOMAIN_TO_SAVE = "Domain To Save: {}";
	public static final String ENTITY_TO_SAVE = "Entity To Save: {}";
	public static final String SAVED_ENTITY = "Saved Entity: {}";
	public static final String RETRIEVED_ENTITY = "Retrieved Entity: {}";
	public static final String MAPPED_ENTITY = "Response Object Mapped from Entity: {}";
	public static final String RETRIEVED_MEMBER_DETAILS = "Retrieved Member Details: {}";
	public static final String RETRIEVED_MEMBER_DETAILS_AFTER_ENROLL = "Retrieved Member Details After Enrollment: {}";
	public static final String SENDER_RECEIVER_MEMBER_DETAILS = "Sender Member Details: {} | Receiver Member Details: {}";
	public static final String MM_URL = "Member Management URL: {}";
	public static final String MA_URL = "Member Activity URL: {}";
	public static final String PB_URL = "Points Bank URL: {}";
	public static final String MEMBER_ENROLL_RESPONSE = "Member Enroll API Response: {}";
	public static final String LIST_MEMBER_BY_ACCOUNT_LIST_RESPONSE = "Get Member By Accounts List API Response: {}";
	public static final String MEMBER_ACTIVITY_REQUEST = "Member Accrual/Redemption API Request: {}";
	public static final String MEMBER_ACTIVITY_RESPONSE = "Member Accrual/Redemption API Response: {}";
	public static final String PAYMENT_RESPONSE = "Payment Response: {}";
	public static final String POINTS_BANK_RESPONSE = "Points Bank API Response: {}";
	public static final String MEMBER_ENROLL_EXCEPTION = "Failed, Exception in Member Management: ";
	public static final String MEMBER_ENROLL_FAILED_STATUS = "Failed";
	public static final String NOTIFICATION_DTO = "Notification DTO: {}";
	public static final String NOTIFICATION_HELPER_DTO = "Notification Helper DTO: {}";
	public static final String BIRTHDAY_REMINDER_ACCOUNTS = "Send Reminder To Account: {}";
	public static final String CRFR_BALANCE = "Retrieved CRFR Balance: {}";
	public static final String ORIGINAL_ACCOUNT_DETAILS = "Original Account Number: {}, Membership Code: {}";
	public static final String ACTION_INSERT = "Insert";
	public static final String ACTION_UPDATE = "Update";
	public static final String CHANNEL_ID = "channelId";
	public static final String CHANNEL_ID_CCPORTAL = "CC_PORTAL";
	public static final String MEMBERSHIP_CODE = "membershipCode";
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	public static final String ACCOUNT_STATUS_ACT= "ACT";
	public static final String STRING_DATE_FORMAT = "dd-MM-yyyy";
	public static final String NOTIFICATION_CODE = "00";
	
	//BIRTHDAY REMINDER CONSTANTS
	public static final String BIRTHDAY_REMINDER_ACCEPTED = "Accepted";
	public static final String BIRTHDAY_REMINDER_PENDING = "Pending";
	public static final String BIRTHDAY_REMINDER_REJECTED = "Rejected";
	public static final String BIRTHDAY_REMINDER_PRIOR_DAY = "Day";
	public static final String BIRTHDAY_REMINDER_PRIOR_WEEK = "Week";
	public static final String BIRTHDAY_REMINDER_PRIOR_MONTH = "Month";
	
	public static final String BIRTHDAY_LIST_NOTIFICATION_TITLE_EN = "Birthday Reminder Request";
	public static final String BIRTHDAY_LIST_NOTIFICATION_TITLE_AR = "صلاحية قسيمتك على وشك الانتهاء";
	public static final String BIRTHDAY_LIST_NOTIFICATION_DESC_EN = "Your friend {0} has sent you a request to add a reminder for his birthday. If you would like to add the reminder, you can accept it or reject it.";
	public static final String BIRTHDAY_LIST_NOTIFICATION_DESC_AR = "قسيمة {0} الخاصة بك من ستنتهي صلاحيتها بتاريخ  استبدلها الآن كي لا يفوتك التوفير.";
	public static final String BIRTHDAY_REMINDER_LIST_NOTIFICATION_ID = "9";
	public static final String SEND_BIRTHDAY_REMINDER_NOTIFICATION_ID = "9";
	
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_TITLE_EN = "Your Friend's Birthday!";
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_TITLE_AR = "صلاحية قسيمتك على وشك الانتهاء";
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_DESC_DAY_EN = "Your friend {0} has their birthday tomorrow. Send them a gift!";
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_DESC_DAY_AR = "قسيمة {0} الخاصة بك من ستنتهي صلاحيتها بتاريخ  استبدلها الآن كي لا يفوتك التوفير.";
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_DESC_WEEK_EN = "Your friend {0} has their birthday in a week. Send them a gift!";
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_DESC_WEEK_AR = "قسيمة {0} الخاصة بك من ستنتهي صلاحيتها بتاريخ  استبدلها الآن كي لا يفوتك التوفير.";
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_DESC_MONTH_EN = "Your friend {0} has their birthday in a month. Send them a gift!";
	public static final String NOTIFY_BIRTHDAY_NOTIFICATION_DESC_MONTH_AR = "قسيمة {0} الخاصة بك من ستنتهي صلاحيتها بتاريخ  استبدلها الآن كي لا يفوتك التوفير.";
	
	//QUEUE
	public static final String PUSH_NOTIFICATION_QUEUE = "pushNotificationQueue";
	
	//GIFTING CONSTANTS
	public static final String RECEIVER_CONSUMPTION_YES = "YES";
	public static final String RECEIVER_CONSUMPTION_NO = "NO";
	public static final String IS_GIFT_NO = "NO";
	public static final String IS_GIFT_YES = "YES";
	public static final String IMAGE_TYPE_GIFTING = "Gifting";
	public static final String GIFT_TYPE_VOUCHER = "Voucher";
	public static final String GIFT_TYPE_POINTS = "Points";
	public static final String GIFT_TYPE_GOLD = "Gold";
	public static final String VOUCHER_GIFT_REQUEST_PARAMS = "Voucher Gift Request Parameters: {}";
	public static final String POINTS_GIFT_REQUEST_PARAMS = "Points Gift Request Parameters: {}";
	public static final String GOLD_GIFT_REQUEST_PARAMS = "Gold Gift Request Parameters: {}";
	public static final String TRANSAC_TYPE_GIFTED = "GIFTED";
	public static final String TRANSAC_TYPE_GIFT = "GIFT";
	public static final String LEVEL_ACCOUNT= "Account";
	public static final String LEVEL_MEMBERSHIP = "Membership";
	public static final String SENDER_AVAILABLE_POINTS = "Sender's available points: {}";
	public static final String GIFTING_COUNTER_RESPONSE = "Response from GiftingCounter collection: {}";
	public static final String POINT_CONVERSION_PARTNER_CODE = "Smiles";
	public static final String POINT_GIFTING_PRODUCT_ITEM = "Gift Points";
	public static final String POINT_GIFTING_PAYMENT_ITEM = "goldPoints";
	public static final String GOLD_GIFTING_PRODUCT_ITEM = "goldGift";
	public static final String PAYMENT_FAILED_STATUS = "Failed";
	public static final String PARTIAL_POINTS_CC = "partialPointsCC";
	public static final String MA_ACTIVITY_CODE_ACCRUAL = "GIFT_PT_ACR";
	public static final String MA_ACTIVITY_CODE_REDEMPTION = "GIFT_PT_RDM";
	public static final String REDEMPTION_TYPE = "Direct";
	
	public static final String FILTER_ALL = "All";
	public static final String FILTER_SENT = "Sent";
	public static final String FILTER_RECEIVED = "Received";
	public static final String FILTER_ADMIN = "Admin";
	
	public static final String TRANSACTION_TYPE_ACR = "Accrual";
	public static final String TRANSACTION_TYPE_RDM = "Redemption";
	
	public static final String ORDER_DESC_TRANSACTION_DATE = "PaymentTransactionDate";
	
	public static final List<String> GIFT_TYPE_LIST = Collections.unmodifiableList(Arrays.asList(GIFT_TYPE_VOUCHER, GIFT_TYPE_POINTS, GIFT_TYPE_GOLD));
	
	public static final String MEMBER_ACT_STATUS = "ACT";
	public static final String MEMBER_ACTIVE_STATUS = "ACTIVE";
	
	//NOTIFICATION - SMS & EMAIL
	public static final String NOTIFICATION_TYPE_SMS= "SMS";
	public static final String NOTIFICATION_TYPE_EMAIL= "EMAIL";
	
	public static final String TYPE_SENDER = "Sender";
	public static final String TYPE_RECEIVER = "Receiver";
	
	public static final String LANGUAGE_ENGLISH = "ENGLISH";
	public static final String LANGUAGE_ARABIC = "ARABIC";
	public static final String LANGUAGE_EN = "EN";
	public static final String LANGUAGE_AR = "AR";
	public static final String NOTIFICATION_LANG_EN = "en";
	public static final String NOTIFICATION_LANG_AR = "ar";
	
	public static final List<String> LANGUAGE_ENGLISH_LIST = Collections.unmodifiableList(Arrays.asList(LANGUAGE_ENGLISH, LANGUAGE_EN));
	public static final List<String> LANGUAGE_ARABIC_LIST = Collections.unmodifiableList(Arrays.asList(LANGUAGE_ARABIC, LANGUAGE_AR));
	
	//SENDER SMS CONSTANTS
	public static final String SENDER_SMS_TEMPLATE_ID_GIFT_VOUCHER_EN = "143";
	public static final String SENDER_SMS_TEMPLATE_ID_GIFT_VOUCHER_AR = "143";
	public static final String SENDER_SMS_TEMPLATE_ID_GIFT_POINTS_EN = "133";
	public static final String SENDER_SMS_TEMPLATE_ID_GIFT_POINTS_AR = "134";
	public static final String SENDER_SMS_TEMPLATE_ID_GIFT_GOLD_EN = "133";
	public static final String SENDER_SMS_TEMPLATE_ID_GIFT_GOLD_AR = "134";
	
	public static final String SENDER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_EN = "143";
	public static final String SENDER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_AR = "143";
	public static final String SENDER_SMS_NOTIFICATION_ID_GIFT_POINTS_EN = "133";
	public static final String SENDER_SMS_NOTIFICATION_ID_GIFT_POINTS_AR = "134";
	public static final String SENDER_SMS_NOTIFICATION_ID_GIFT_GOLD_EN = "133";
	public static final String SENDER_SMS_NOTIFICATION_ID_GIFT_GOLD_AR = "134";
	
	public static final String SENDER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_EN = "00";
	public static final String SENDER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_AR = "00";
	public static final String SENDER_SMS_NOTIFICATION_CODE_GIFT_POINTS_EN = "00";
	public static final String SENDER_SMS_NOTIFICATION_CODE_GIFT_POINTS_AR = "00";
	public static final String SENDER_SMS_NOTIFICATION_CODE_GIFT_GOLD_EN = "00";
	public static final String SENDER_SMS_NOTIFICATION_CODE_GIFT_GOLD_AR = "00";
	
	//RECEIVER SMS CONSTANTS
	public static final String RECEIVER_SMS_TEMPLATE_ID_GIFT_VOUCHER_EN = "112";
	public static final String RECEIVER_SMS_TEMPLATE_ID_GIFT_VOUCHER_AR = "112";
	public static final String RECEIVER_SMS_TEMPLATE_ID_GIFT_POINTS_EN = "135";
	public static final String RECEIVER_SMS_TEMPLATE_ID_GIFT_POINTS_AR = "135";
	public static final String RECEIVER_SMS_TEMPLATE_ID_GIFT_GOLD_EN = "133";
	public static final String RECEIVER_SMS_TEMPLATE_ID_GIFT_GOLD_AR = "134";
	
	public static final String RECEIVER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_EN = "112";
	public static final String RECEIVER_SMS_NOTIFICATION_ID_GIFT_VOUCHER_AR = "112";
	public static final String RECEIVER_SMS_NOTIFICATION_ID_GIFT_POINTS_EN = "135";
	public static final String RECEIVER_SMS_NOTIFICATION_ID_GIFT_POINTS_AR = "135";
	public static final String RECEIVER_SMS_NOTIFICATION_ID_GIFT_GOLD_EN = "133";
	public static final String RECEIVER_SMS_NOTIFICATION_ID_GIFT_GOLD_AR = "134";
	
	public static final String RECEIVER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_EN = "00";
	public static final String RECEIVER_SMS_NOTIFICATION_CODE_GIFT_VOUCHER_AR = "00";
	public static final String RECEIVER_SMS_NOTIFICATION_CODE_GIFT_POINTS_EN = "00";
	public static final String RECEIVER_SMS_NOTIFICATION_CODE_GIFT_POINTS_AR = "00";
	public static final String RECEIVER_SMS_NOTIFICATION_CODE_GIFT_GOLD_EN = "00";
	public static final String RECEIVER_SMS_NOTIFICATION_CODE_GIFT_GOLD_AR = "00";
	
	//SENDER EMAIL CONSTANTS
	public static final String SENDER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_EN = "530972679";
	public static final String SENDER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_AR = "530972679";
	public static final String SENDER_EMAIL_TEMPLATE_ID_GIFT_POINTS_EN = "133";
	public static final String SENDER_EMAIL_TEMPLATE_ID_GIFT_POINTS_AR = "134";
	public static final String SENDER_EMAIL_TEMPLATE_ID_GIFT_GOLD_EN = "133";
	public static final String SENDER_EMAIL_TEMPLATE_ID_GIFT_GOLD_AR = "134";
	
	public static final String SENDER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_EN = "5";
	public static final String SENDER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_AR = "5";
	public static final String SENDER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_EN = "133";
	public static final String SENDER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_AR = "134";
	public static final String SENDER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_EN = "133";
	public static final String SENDER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_AR = "134";
	
	public static final String SENDER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_EN = "00";
	public static final String SENDER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_AR = "00";
	public static final String SENDER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_EN = "00";
	public static final String SENDER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_AR = "00";
	public static final String SENDER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_EN = "00";
	public static final String SENDER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_AR = "00";
	
	//RECEIVER EMAIL CONSTANTS
	public static final String RECEIVER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_EN = "3";
	public static final String RECEIVER_EMAIL_TEMPLATE_ID_GIFT_VOUCHER_AR = "3";
	public static final String RECEIVER_EMAIL_TEMPLATE_ID_GIFT_POINTS_EN = "133";
	public static final String RECEIVER_EMAIL_TEMPLATE_ID_GIFT_POINTS_AR = "134";
	public static final String RECEIVER_EMAIL_TEMPLATE_ID_GIFT_GOLD_EN = "133";
	public static final String RECEIVER_EMAIL_TEMPLATE_ID_GIFT_GOLD_AR = "134";
	
	public static final String RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_EN = "3";
	public static final String RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_VOUCHER_AR = "3";
	public static final String RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_EN = "133";
	public static final String RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_POINTS_AR = "134";
	public static final String RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_EN = "133";
	public static final String RECEIVER_EMAIL_NOTIFICATION_ID_GIFT_GOLD_AR = "134";
	
	public static final String RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_EN = "00";
	public static final String RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_VOUCHER_AR = "00";
	public static final String RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_EN = "00";
	public static final String RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_POINTS_AR = "00";
	public static final String RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_EN = "00";
	public static final String RECEIVER_EMAIL_NOTIFICATION_CODE_GIFT_GOLD_AR = "00";

	public static final String SMS_CUSTOMER_NAME = "Customer_Name";
	public static final String SMS_URL = "URL";
	public static final String SMS_FIRST_NAME_SENDER = "FIRST_NAME_SENDER";
	public static final String SMS_GIFT_ID = "GIFT_ID";
	public static final String SMS_POINTS_GIFTED = "Points_Gifted";
	public static final String SMS_GOLD_GIFTED = "Gold_Gifted";
	
	public static final String EMAIL_SENDER_NAME = "SENDER_NAME";
	public static final String EMAIL_RECEIVER_NAME = "RECIEVER_NAME";
	public static final String EMAIL_SENDER_NUMBER = "SENDER_NUMBER";
	public static final String EMAIL_RECEIVER_NUMBER = "RECIEVER_NUMBER";
	public static final String EMAIL_BACKGROUND_URL = "BG_URL";
	public static final String EMAIL_BACKGROUND_DESCRIPTION = "BG_DESC";
	public static final String EMAIL_GIFT_ID = "GIFT_ID";
	public static final String EMAIL_POINTS_GIFTED = "Points_Gifted";
	public static final String EMAIL_GOLD_GIFTED = "Gold_Gifted";

	/* Voucher email config*/
	public static final String EMAIL_OFFER_URL = "OF_URL";
	public static final String EMAIL_OFFER_DESC = "OF_DESC";
	public static final String EMAIL_VOUCER_DELV_DATE = "VO_DELV_DATE";
	public static final String EMAIL_VOUCER_EXPIRY_DATE = "VO_EXP_DATE";
	public static final String EMAIL_TRANSAC_ID = "TRANSACTION_ID";
	public static final String EMAIL_AMOUNT_PAID = "AED_PAID";
	public static final String EMAIL_PAYMENT_METHOD = "PAYMENT_METHOD";
	public static final String EMAIL_PAYMENT_DATE = "PAYMENT_DATE";
	
	public static final String LIST_GIFT_HISTORY_NOTIFICATION_SENDER_NAME = "SENDER_NAME";
	public static final String LIST_GIFT_HISTORY_NOTIFICATION_RECEIVER_NAME = "RECIEVER_NAME";
	public static final String LIST_GIFT_HISTORY_NOTIFICATION_ID = "121";
	
	public static final String LIST_GIFT_HISTORY_EMAIL_TEMPLATE_ID_ENGLISH = "530972680";
	public static final String LIST_GIFT_HISTORY_EMAIL_TEMPLATE_ID_ARABIC = "530972680";
	public static final String LIST_GIFT_HISTORY_EMAIL_NOTIFICATION_ID = "6";
	public static final String LIST_GIFT_HISTORY_EMAIL_NOTIFICATION_CODE = "00";
	public static final String LIST_GIFT_HISTORY_EMAIL_SENDER_NAME = "SENDER_NAME";
	public static final String LIST_GIFT_HISTORY_EMAIL_RECIEVER_NAME = "RECIEVER_NAME";
	public static final String LIST_GIFT_HISTORY_EMAIL_IMG_URL = "IMG";
	public static final String LIST_GIFT_HISTORY_EMAIL_OFFER_DESC = "OF_DESC";
	public static final String LIST_GIFT_HISTORY_EMAIL_MSG = "MSG";

	public static final String CARREFOUR = "CRFR";
	public static final String IS_CRFR_VOUCHER = "CRFR_Voucher";
	public static final String NOT_CRFR_VOUCHER = "Not_CRFR_Voucher";
	
	//Premium Voucher Gifting
	public static final String GIFT_PREMIUM_VOUCHERS_METHOD = "giftPremiumVoucherForPointRedemption";
	public static final String FIRST_NAME_SMS_PARAMETER = "First_Name";
	public static final String POINTS_REDEEMED_SMS_PARAMETER = "Points_Redeemed";
	public static final String VOUCHER_DENOMINATION = "Voucher_Denomination";
	public static final String ACTIVITY_DESCRIPTION = "Activity_Description";
	
	public static final String DELETE_GIFT_CONFIGURATION = "deleteGiftConfiguration";
	public static final String LIST_GIFT_CONFIGURATION_METHOD = "listingGiftConfiguration";

}
