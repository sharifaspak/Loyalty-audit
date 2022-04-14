package com.loyalty.marketplace.voucher.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class VoucherConstants {
	
	
	private VoucherConstants() {}
	
	public static final String FILENAME = "FileName";
	public static final String PROCESSING = "Processing";
	public static final String ADMID_PORTAL_CHANNEL_ID = "ADMIN_PORTAL";
	public static final String YES = "yes";
	public static final String ACTIVE = "Active";
	public static final String BURNT = "Burnt";
	public static final String CANCELLED = "Cancelled";
	public static final String ERROR = "Error Occured in Cancellation";
	public static final String GENERATE = "2";
	public static final String ASK = "1";
	public static final String SEARCH = "3";
	public static final String DEAL_OFFER="Deal Offer";
	public static final String DISOUNT_OFFER="Discount Offer";
	public static final String CASH_OFFER="Cash Voucher";
	public static final String CANCEL_ACTION = "cancel";
	public static final String VOUCHER_TYPE = "EVO";
	public static final String MESSAGE_SEPARATOR = " : ";	
	public static final String VERIFY_LINK = "VERIFY_LINK";
	public static final String CUSTOME_NAME = "CUSTOME_NAME";
	public static final String OFFER_ID = "OFFER_ID";
	public static final String ADMIN_PORTAL_LINK = "ADMIN_PORTAL_LINK";
	public static final String DATE = "DATE";
	public static final String TEMPLATE_ID = "1";
	public static final String NOTIFICATION_ID = "1";
	public static final String NOTIFICATION_CODE = "00";
	public static final String LANGUAGE = "English";
	public static final String VOUCHER_GEN_TEMPLATE_ID = "530973375";
	public static final String VOUCHER_GEN_NOTIFICATION_ID = "18";
	public static final String VOUCHER_GEN_NOTIFICATION_CODE = "00";
	public static final String VOUCHER_GEN_LANGUAGE = "en";
	public static final String VOUCHER_RECON_TEMPLATE_ID = "530973374";
	public static final String VOUCHER_RECON_NOTIFICATION_ID = "17";
	public static final String VOUCHER_RECON_NOTIFICATION_CODE = "00";
	public static final String VOUCHER_RECON_LANGUAGE = "en";
	public static final String MAF = "MAFGC";
	public static final String CARREFOUR = "CRFR";
	public static final String YGAG = "YGAG";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	public static final String TRUE = "TRUE";
	public static final String APP = "app";
	public static final String TANDC = "1. Vouchers are only valid at restaurant locations mentioned\r\n" + 
     		"on the voucher.\r\n" + 
     		"2. Valid for dine-in only.\r\n" + 
     		"3. Vouchers cannot be combined or used in conjunction with\r\n" + 
     		"any other offers or discounts.\r\n" + 
     		"4. Vouchers are not valid during Public holidays.\r\n" + 
     		"5. Vouchers cannot be transferred, cancelled or refunded.\r\n" + 
     		"6. Vouchers must not be copied, redistributed for any\r\n" + 
     		"purposes, including but not limited to sale, trade or exchange,\r\n" + 
     		"unless otherwise specified.\r\n" + 
     		"7. A voucher can only be redeemed once.\r\n" + 
     		"8. Vouchers expire 2 weeks from date of purchase, unless\r\n" + 
     		"otherwise specified. However, voucher must be redeemed\r\n" + 
     		"before the expiry date.\r\n" + 
     		"9. Voucher Offer Policy - When two or more people are dining\r\n" + 
     		"together, the rule will apply:\r\n" + 
     		"- One bill for the entire table. The total sum cannot be divided\r\n" + 
     		"into separate bills.\r\n" + 
     		"- A maximum limit of 4 vouchers can redeemed per\r\n" + 
     		"group/table.";
	public static final String	VALIDATOR_DELIMITOR= " ";	
	public static final String	COLON= ";";
	
	public static final String	COMMA= ",";
	public static final String IMAGE_FORMAT = "image/x-png";
	public static final Object CSV = "csv";
	public static final Object PDF = "pdf";
	public static final String TEXT_CSV ="text/csv";
	public static final String APP_OCTET_STREAM ="application/octet-stream";
	public static final String APP_MS_EXCEL ="application/vnd.ms-excel";
	public static final String CHECK_PARTNER_TYPE_EXISTS_METHOD = "checkPartnerTypeExists";
	public static final String RECONCILE_LOYALTY = "loyalty";
	public static final String RECONCILE_PARTNER = "partner";
	public static final String ADD_TO_BILL = "addToBill";
	public static final String DEDUCT_FROM_BALANCE = "deductFromBalance";
	public static final String RECONCILE_SUMMARY="Summary";
	public static final String COUNT_TRANSAC_PARTNER="COUNT_PARTNER";
	public static final String COUNT_TRANSAC_LOYALTY="COUNT_SMILES";
	public static final String TOTAL_AMOUNT_PARTNER="TOTAL_AMOUNT_PARTNER";
	public static final String TOTAL_AMOUNT_LOYALTY="TOTAL_AMOUNT_SMILES";
	public static final String RECONCILE_DETAIL="Detail";
	public static final String RECONCILE_DETAIL_VOUCHER_CODE="VOUCHER_CODE";
	public static final String[] HEADERS = { RECONCILE_DETAIL_VOUCHER_CODE, "Upload date", "Denomination", "Merchant code", "Expiry date", "Offer id",
			"Start date", "End date", "Sub offer id", "Status","Error Code" };
	public static final String RECONCILE_DETAIL_PARTNER_CODE="PARTNER_CODE";
	public static final String RECONCILE_DETAIL_LOYALTY_ID="LOYALTY_TRANSAC_ID";
	public static final String RECONCILE_DETAIL_PARTNER_ID="PARTNER_TRANSAC_ID";
	public static final String RECONCILE_DETAIL_VOUCHER_AMT="VOUCHER_AMT";
	public static final String RECONCILE_DETAIL_TRANSAC_DATE="TRANSAC_DATE";
	public static final String HANDBACK_CSV="handback_csv_";
	public static final String UNDERSCORE="_";
	public static final String PDFCONSTANT=".PDF";
	public static final String ADMIN = "admin";
	public static final String OTHERS = "OTHERS";
	public static final String GENERATE_FREE_VOUCHER = "Generate free Voucher";

	public static final String CHECK_VOUCHER_INVOICED = "checkIfVoucherInvoiced";
	public static final String DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";
	public static final String RECONCILE_DATE_FORMAT_FROM_PARTNER="yyyy-MM-dd HH:mm:ss";
	public static final String HANDBACK_FILE_ERROR="Error;";
	public static final String DISCOUNT="Discount";
	public static final String PERCENTAGE = "%";
	public static final String VOUCHER_EXPIRY_DATE_FORMAT = "dd-MM-yyyy";
	public static final String CURRENT_DATE_FUTURE_DATE = "Current date: {}, Current Date + 7 Days: {}";
	public static final String VOUCHER_EXPIRY_NOTIFICATION_ID = "117";
	public static final String VOUCHER_EXPIRY_COUPON_NAME = "COUPON_NAME";
	public static final String VOUCHER_EXPIRY_VALUE = "VALUE";
	public static final String VOUCHER_EXPIRY_EXP_DATE = "EXP_DATE";
	public static final int VOUCHER_EXPIRY_PLUS_7_DAYS = 7;
	public static final String FORWARD_SLASH = "/";
	public static final String VOUCHER_STATISTICS_ACCOUNT_NUMBER = "accountNumber";
	public static final String VOUCHER_STATISTICS_CONTACT_NUMBER = "contact";
	public static final String MEMBER_TIER_REWARDS = "REWARDS";
	public static final String MEMBER_TIER_GOLD = "GOLD";
	public static final String MEMBER_TIER_SILVER = "SILVER";
	public static final String MEMBER_COBRANDED = "COBRANDED";
	public static final String V_COUNT_NORMAL = "V_COUNT_NORMAL";
	public static final String V_COUNT_SPECIAL = "V_COUNT_SPECIAL";
	public static final String VOUCHER_STATS_TEMPLATE_ID = "139";
	public static final String VOUCHER_STATS_NOTIFICATION_ID = "139";
	public static final String MERCHANT_NAME = "partner_name";
	public static final String MERCHANT_NAME_NO_VOUCHER_CODE = "MERCHANT_NAME";
	public static final String OFFER_ID_NO_VOUCHER_CODE = "OfferId";
	public static final String OFFER_TITLE = "OFFER_TITLE";
	public static final String AVAILABLE_COUNT = "AVAILABLE_COUNT";
	public static final double MIN_PERCENT_VOUCHER_COUNT = 0.15;
	public static final double ZERO_PERCENT_VOUCHER_COUNT = 0.0;
	public static final String DECIMAL_FORMAT = "###.##";
	
	public static final String VOUCHER_COUNT_TYPE_ZERO = "VOUCHER_COUNT_ZERO";
	public static final String VOUCHER_COUNT_TYPE_ZERO_TEMPLATE_ID = "149";
	public static final String VOUCHER_COUNT_TYPE_ZERO_NOTIFICATION_ID = "149";
	
	public static final String VOUCHER_COUNT_TYPE_15_PERC = "VOUCHER_COUNT_15_PERC";
	public static final String VOUCHER_COUNT_TYPE_15_TEMPLATE_ID = "148";
	public static final String VOUCHER_COUNT_TYPE_15_NOTIFICATION_ID = "148";
	
	public static final String COMMA_SEPARATOR = ", ";
	public static final String LOG_ENTERING = "ENTERING ";
	public static final String LOG_LEAVING = "EXITING ";
	public static final String CLASS_NAME = "CLASS: {} | ";
	public static final String METHOD_NAME = "METHOD: {} | ";
	public static final String API_NAME = "API: {}";
	public static final String REQUEST_PARAMS = "Request Parameters: {}";
	public static final String RESPONSE_PARAMS = "Response Parameters: {}";
	public static final String NOTIFICATION_DTO = "Notification DTO: {}";
	public static final String REQUEST_PARAMS_LIST_VOUCHER_BY_CODE = "Request Parameters - VoucherCode, AccountNumber, StoreCode: {}, {}, {}";
	public static final String UPLOADED_VOUCHER_COUNT = "Uploaded Voucher Count: {}";
	public static final String AVAILABLE_VOUCHER_STATISTICS = "Ready Vouchers: {}, Total Vouchers: {}, Available Voucher Codes %: {}, Merchant Code: {}, OfferId: {}";
	public static final String REQUEST_PARAMS_LIST_VOUCHER_STATUS = "Request Parameters - Account Number: {}, Status: {}";
	
	public static final String PUSH_NOTIFICATION_QUEUE = "pushNotificationQueue";
	public static final String SMS_QUEUE = "smsQueue";
	
	//METHOD NAMES
	public static final String CONTROLLER_VOUCHER_EXPIRY = "voucherExpiryNotification";
	public static final String CONTROLLER_HELPER_VOUCHER_EXPIRY = "notifyExpiredVouchers";
	public static final String SERVICE_VOUCHER_EXPIRY = "pushNotificationVoucherExpiry";
	
	public static final String CONTROLLER_VOUCHER_STATISTICS = "voucherStatisticsNotification";
	public static final String CONTROLLER_HELPER_VOUCHER_STATISTICS = "notifyVoucherStatistics";
	public static final String SERVICE_VOUCHER_STATISTICS = "sendSMSVoucherStatistics";

	public static final String CONTROLLER_VOUCHER_COUNT = "voucherCountNotification";
	public static final String CONTROLLER_HELPER_VOUCHER_COUNT = "notifyVoucherCount";
	public static final String SERVICE_VOUCHER_COUNT = "sendSMSVoucherCount";
	
	public static final String VOUCHER_FREE_GENERATE = "uploadAccountFreeVoucherFile";
	public static final String ERROR_PARSE_CSV="Error while parsing csv";
	public static final String MEMBER_ACTIVE_STATUS = "ACT";
	public static final String MEMBER_UI_LANG_ENGLISH = "English";
	public static final String MEMBER_UI_LANG_EN = "En";
	public static final String MEMBER_UI_LANG_ARABIC = "Arabic";
	public static final String MEMBER_UI_LANG_AR = "Ar";
	public static final String VOUCHER_GIFT_SENT = "Sent";
	public static final String VOUCHER_GIFT_RECEIVED = "Received";
	public static final String NOTIFICATION_LANGUAGE_EN = "en";
	public static final String NOTIFICATION_LANGUAGE_AR = "ar";
	
	public static final String CONTROLLER_CARREFOUR_FAILURE_ALERT = "carrefourFailureAlert";
	public static final String CONTROLLER_HELPER_CARREFOUR_FAILURE_ALERT = "carrefourFailureAlertHelper";
	public static final String CONTROLLER_LIST_VOUCHER_BY_STATUS = "listVouchersByStatus";
	public static final String CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID = "listVouchersByBusinessId";
	public static final String CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID_LIST = "listVouchersByBusinessIdList";
	public static final String CONTROLLER_LIST_VOUCHER_BY_CODE = "listVouchersByCode";
	public static final String CONTROLLER_LIST_VOUCHER_BY_ID = "listVouchersById";
	public static final String CONTROLLER_LIST_VOUCHER_BY_PARTNER_MERCHANT = "listVouchersByPartnerAndMerchantCode";
	
	public static final String CONTROLLER_YGAG_FAILURE_ALERT = "ygagFailureAlert";
	public static final String CONTROLLER_HELPER_YGAG_FAILURE_ALERT = "ygagFailureAlertHelper";
	
	public static final String REQUEST_PARAMS_LIST_VOUCHER_CODE = "Request Parameters - Account Number: {}, Voucher Code: {}";
	
	public static final String DISCOUNT_OFFER_ID = "1";
	public static final String CASH_OFFER_ID = "2";
	public static final String DEAL_OFFER_ID = "20";
		
	public static final String MM_STATUS_ACT = "ACT";
	public static final String MM_STATUS_ACTIVE = "ACTIVE";
	public static final String MM_STATUS_SUSPENDED = "SUSPENDED";
	
	public static final String SORT_DESC_DOWNLOADED_DATE = "DownloadedDate";
	
	public static final List<String> LIST_VOUCHER_ELIGIBLE_API = Collections
			.unmodifiableList(Arrays.asList(CONTROLLER_LIST_VOUCHER_BY_CODE, CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID,
					CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID_LIST));
	
	public static final CharSequence FAIL = "fail";
	public static final String OFFER_NOT_PRESENT = "Offer not present";
	public static final String VOUCHER_EXPIRED = "The date for gifting voucher has already expired";
	public static final String CANCELLED_MEMBER = "Member is in cancelled state";
	
	public static final String HANDBACK_FILE_ERROR_EXT_TRANS_ID = "External transaction id is mandatory";
	public static final String HANDBACK_FILE_ERROR_DENOMINATION = "Denomination is mandatory for cash voucher";
	public static final String HANDBACK_FILE_ERROR_SUSPENDED_MEMBER = "Account is parked as in suspended status";
	public static final String HANDBACK_FILE_ERROR_CURRENTLY_ENROLLED = "Account is parked as enrolled newly with membership code ";
	public static final String HANDBACK_FILE_ERROR_NOT_ENROLLED = "Account is parked as as does not exist";
	public static final String HANDBACK_FILE_ERROR_INACTIVE = "Account is parked as not active for more than ";
	public static final String HANDBACK_FILE_ERROR_DUPLICATE_EXT_TRANS_ID_FILE = "External transaction id is duplicate, present for another record in file";
	public static final String HANDBACK_FILE_ERROR_DUPLICATE_EXT_TRANS_ID_DB = "External transaction id is duplicate, present for another previously processed record in DB";
	public static final String ACCOUNT_NUMBER_CODE = "accountNumber";
	public static final String MEMBERSHIP_CODE_CODE = "membershipCode";
	public static final String LOYALTY_ID_CODE = "loyaltyId";
	public static final String DAYS = " days";
	
	
	public static final String NEWLINE_CHARACTER = "\n";
	public static final String COMMA_CHARACTER = ",";
	public static final String SEMICOLON_CHARACTER = ";";
	
	public static final String EXTERNAL_REF_NO = "ExtRefNo";
	public static final String STATUS = "Status";
	public static final String IGNORE_CASE = "i";
	public static final String SMLS = "SMLS";
	public static final Integer SMLS_RESPOSNE_CODE = 100;
	
	
}

