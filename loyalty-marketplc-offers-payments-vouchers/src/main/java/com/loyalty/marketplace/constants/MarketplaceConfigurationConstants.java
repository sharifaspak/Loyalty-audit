package com.loyalty.marketplace.constants;

/**
 * 
 * @author jaya.shukla
 *
 */
public class MarketplaceConfigurationConstants extends RequestMappingConstants{
	
    private MarketplaceConfigurationConstants(){
    	
    }
	
    /**
     * Marketplace Application Constants
     */
	public static final String MONGO_TRANSACTION_MANAGER = "mongoTransactionManager";
	
	public static final String THREAD_POOL_EXECUTOR = "threadPoolTaskExecutor";
	public static final String THREAD_NAME_PREFIX = "JDAsync-";
	
	public static final String THREAD_POOL_EXECUTOR_FREE_VOUCHER = "threadPoolTaskExecutorForFreeVoucher";
	public static final String FREE_VOUCHER_THREAD_PREFIX = "FreeVoucher-";
	
	public static final String THREAD_POOL_EXECUTOR_FREE_VOUCHER_AFTER_LOGIN = "threadPoolTaskExecutorForFreeVoucherAfterLogin";
	public static final String FREE_VOUCHER_AFTER_LOGIN_THREAD_PREFIX = "FreeVoucherAfterLogin-";

	public static final String THREAD_POOL_EXECUTOR_RENEWAL_REPORT = "threadPoolTaskExecutorForRenewalReport";
	public static final String RENEWAL_REPORT_THREAD_PREFIX = "SubscriptionRenewalReport-";
	
	public static final String THREAD_POOL_EXECUTOR_PREMIUM_VOUCHER_GIFT = "threadPremiumVoucherGift";
	public static final String PREMIUM_VOUCHER_GIFT_THREAD_PREFIX = "PremiumVoucherGift-";

	public static final String THREAD_POOL_EXECUTOR_REFUND = "threadPoolTaskExecutorForRefund";
	public static final String REFUND_THREAD_PREFIX = "Refund-";
	
	public static final String THREAD_POOL_EXECUTOR_MERCHANT_OFFER_COUNT_UPDATE = "threadPoolTaskExecutorForMerchantOfferCountUpdate";
	public static final String MERCHANT_OFFER_COUNT_UPDATE_THREAD_PREFIX = "MerchantOfferCountUpdate-";
	
	public static final String THREAD_POOL_EXECUTOR_CASH_VOUCHER_ACCRUAL = "threadPoolTaskExecutorForCashVoucherAccrual";
	public static final String CASH_VOUCHER_ACCRUAL_THREAD_PREFIX = "CashVoucherAccrual-";
	
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	public static final String UNCHEKED_WARNING = "unchecked";
	
	public static final String TYPE_ID_PROPERTY_NAME = "_type";
	
	public static final String SMS_REQUEST_DTO = "SMSRequestDto";
	public static final String EMAIL_REQUEST_DTO = "EmailRequestDto";
	public static final String STRING = "String";
	public static final String AUDIT = "Audit";
	public static final String LIFETIME_SAVINGS_VOUCHER_EVENT = "LifeTimeSavingsVouchersEvent";
	public static final String PUSH_NOTIFICATION_REQUEST_DTO = "PushNotificationRequestDto";
	public static final String SERVICE_CALL_LOGS_DTO = "ServiceCallLogsDto";
	public static final String ACCOUNT_CHANGE_EVENT = "AccountChangeEvent";
	public static final String ENROLL_EVENT = "EnrollEvent";
		
	public static final String CLASSPATH_OFFER_MESSAGES = "classpath:offerMessages";
	public static final String CLASSPATH_SUBSCRIPTION_MESSAGES = "classpath:subscriptionMessages";
	public static final String CLASSPATH_VOUCHER_MESSAGES = "classpath:voucherMessages";
	public static final String CLASSPATH_IMAGE_MESSAGES = "classpath:imageMessages";
	public static final String CLASSPATH_EQUIVALENT_POINTS_MESSAGES = "classpath:equivalentPointsMessages";
	public static final String CLASSPATH_GIFTING_MESSAGES = "classpath:giftingMessages";
	public static final String CLASSPATH_CUSTOMER_SEGMENT_MESSAGES = "classpath:customerSegmentMessages";
	
	public static final String CUSTOM_REST_TEMPLATE_BEAN = "customRestTemplateBean";
	public static final String GET_TEMPLATE_BEAN = "getTemplateBean";
	
	public static final String BASE_PACKAGE = "com.loyalty.marketplace";
	public static final String SWAGGER_TITLE = "Marketplace API";
	public static final String SWAGGER_DESCRIPTION = "Documentation Resource Downtime API v1.0";
	public static final String TERMS_OF_SERVICE_URL = "termsofserviceurl";
	public static final String SWAGGER_LICENSE = "Apache License Version 2.0";
	public static final String SWAGGER_VERSION = "2.0";
	public static final String DOWNTIME_TITLE = "Resource Downtime API";
	
	public static final String EXCEPTION_CLASS = "java.lang.Exception";
	
	public static final String GET_TEMPLATE_DETAILS_URL = "integration.url.getTemplateDetails";
	
	/**
	 * Property Values
	 */
	public static final String PROGRAM_MANAGEMNT_URI = "${programManagement.uri}";
	public static final String INTEGRATION_PROXY_URL = "${integration.proxy.url}";
	public static final String INTEGRATION_PROXY_PORT = "${integration.proxy.port}";
	
	/**
	 * DB Constants
	 */
	public static final String DENOMINATION_DIRHAM_VALUE = "Value.Cost";
	
	/**
	 * Bulk Upload
	 */
	public static final String BOGO_BULK_UPLOAD = "bogoBulkUpload";
	public static final String SUBSCRIPTION_METHOD_BOGO = "BulkUpload";
	
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_PROCESSED = "PROCESSED";
	public static final String STATUS_PROCESSING = "PROCESSING";
	public static final String STATUS_FAILED = "FAILURE";
	public static final String FAILED_IN_MemMgmt_ERROR_CODE = "1111";
	public static final String STATUS_ACT = "ACT";
	public static final String STATUS_UPLOADED = "UPLOADED";
	
	/**
	 * Program Code
	 */
	public static final String DEFAULT_PROGRAM_CODE = "${programManagement.defaultProgramCode}";
			
	
	/**
	 * Others
	 */
	public static final String OUTBOUND = "Outbound";
	public static final String INBOUND = "Inbound";
	
	
	/**
	 * Variables
	 */
	public static String HEADER="header";	
	public static String BANNER_ID = "bannerId";
	
	public static final String STAGE = "stage";
	public static final String ARCHIVE = "archive";
	public static final String DISCARD = "discard";
	public static final String BACKUP = "backup";
}

