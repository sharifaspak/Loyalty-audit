package com.loyalty.marketplace.voucher.constants;

public class VoucherRequestMappingConstants{
	
	protected VoucherRequestMappingConstants() {}
	
	public static final String VOUCHER_CODE = "voucherCode";
	public static final String VOUCHER_CODES = "voucherCodes";
	public static final String PURCHASE_ID = "purchaseId";
	public static final String ACTION = "action"; 
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String VOUCHER_STATUS = "voucherStatus";
	public static final String BUSINESS_ID = "businessId";
	public static final String MERCHANT_CODE = "merchantCode";
	public static final String PARTNER_CODE = "partnerCode";
	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";
	public static final String DATE = "date";
	public static final String STORE_CODE = "storeCode";
	public static final String FORMAT_TYPE = "FormatType";
	public static final String VOUCHER_ID = "voucherId";
	public static final String PAGE = "page";
	public static final String LIMIT = "limit";
	public static final String OFFER_ID = "offerId";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String ID = "id";
	public static final String FILE = "file";
	public static final String CHANNEL_CHECK = "channelCheck";

	public static final String GENERATE_VOUCHERS = "/vouchers";
	public static final String VOUCHER_BALANCE_FROM_CARREFOUR = "/vouchers/balanceFromCarrefour";
	public static final String VOUCHER_ACTION = "/voucherAction";
	public static final String VOUCHER_ACTION_RESET = "/resetVoucherAction";
	public static final String VOUCHER_CANCEL ="/vouchers/cancel";
	public static final String VOUCHER_BURN ="/vouchers/burn";
	public static final String VOUCHER_TRANSFER ="/vouchers/transfer";
	public static final String VOUCHER_UPLOAD ="/vouchers/file";
	public static final String VOUCHER_RECONCILIATION ="vouchers/reconciliation";
	public static final String VOUCHER_RECONCILIATION_FIXED_PARAMS = "/vouchers/reconciliationFixedDate";
	public static final String VOUCHER_LIST_BY_STATUS= "/vouchers/status/{voucherStatus}";
	public static final String VOUCHER_LIST_BY_BUSINESSID="/vouchers/businessId/{businessId}";
	public static final String VOUCHER_LIST_BY_LIST_OF_BUSINESSID="/vouchers/businessId";
	public static final String VOUCHER_LIST_BY_CODE= "/vouchers/code/{voucherCode}";
	public static final String VOUCHER_LIST_BY_ID= "/vouchers/id/{voucherId}";
	public static final String VOUCHER_GENERATE_PDF= "/vouchers/pdf";
	public static final String VOUCHER_LIST_BY_MERCHANT = "/voucher/files/{merchantCode}";
	public static final String VOUCHER_LIST = "/voucher/files";
	public static final String VOUCHER_UPLOAD_CONTENT = "/voucher/file/{fileId}";
	public static final String VOUCHER_UPLOAD_DOWNLOADFILE = "/uploadVoucher/file/{fileId}";
	public static final String VOUCHER_GENERATE_BURN_REPORT = "/vouchers/report/burn";
	public static final String VOUCHER_LIST_BY_PARTNERCODE = "/vouchers/partnerMerchantVoucher";
	public static final String VOUCHER_LIST_BY_VOUCHERCODELIST = "/vouchers/voucherCodes";
	
	public static final String VOUCHER_EXPIRY_NOTIFICATION = "/vouchers/expiryVoucher";
	public static final String VOUCHER_STATISTICS_NOTIFICATION = "/vouchers/voucherStatistics";
	public static final String VOUCHER_COUNT_NOTIFICATION = "/vouchers/voucherCount";
	
	public static final String ACCOUNT_UPLOAD_FREE_VOUCHER ="/vouchers/freeGeneration";
	public static final String VOUCHER_LIST_BY_OFFER_ID = "/voucher/files/offerId/{offerId}";
	public static final String GIFT_VOUCHER ="/vouchers/gift";
	public static final String GIFT_VOUCHER_DETAILS ="/vouchers/gift/{giftId}";
	public static final String CARREFOUR_VOUCHER_BALANCE ="/vouchers/carrefourBalance";
	public static final String VOUCHER_GIFT_ID = "giftId";
	
	public static final String ACCOUNT_WITH_ACTIVE_VOUCHER = "/vouchers/listAccountWithActiveVouchers";
	
	public static final String CARREFOUR_FAILURE_ALERT = "/vouchers/carrefourFailureAlert";
	public static final String YGAG_FAILURE_ALERT = "/vouchers/ygagFailureAlert";
	public static final String BURN_CASH_VOUCHER ="/vouchers/burnCashVoucher";
	public static final String VOUCHER_BURN_ROLLBACK ="/vouchers/rollbackVoucherBurn";
	public static final String VOUCHER_UPLOAD_HANDBACK_LIST ="/vouchers/freeVoucherHandbackFiles";
	public static final String VOUCHER_UPLOAD_HANDBACK_SINGLE ="/vouchers/freeVoucherHandbackFile";
}

