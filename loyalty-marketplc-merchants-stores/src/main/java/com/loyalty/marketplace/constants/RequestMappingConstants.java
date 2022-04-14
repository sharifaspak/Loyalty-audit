package com.loyalty.marketplace.constants;

public class RequestMappingConstants {
	
	protected RequestMappingConstants() {}

	 public static final String PROGRAM="program";
	 public static final String AUTHORIZATION="Authorization";
	 public static final String EXTERNAL_TRANSACTION_ID="externalTransactionId";
	 public static final String USER_NAME="userName";
	 public static final String SESSION_ID="sessionId";
	 public static final String USER_PREV="userPrev";
	 public static final String CHANNEL_ID="channelId";
	 public static final String SYSTEM_ID="systemId";
	 public static final String SYSTEM_CREDENTIAL="systemPassword";
	 public static final String TOKEN="token";
	 public static final String TRANSACTION_ID="transactionId";
	 public static final String CONFIGURE_CONATACT_PERSON = "/contactPerson";
	 public static final String RESET_CATEGORY = "/resetCategory";
	 public static final String LIST_CATEGORY =  "/category";
	 public static final String MERCHANT_STATUS="merchantStatus";
	 public static final String FILTER_FLAG="filterFlag";
	 public static final String FILTER_VALUE="filterValue";	 
	 public static final String CATEGORY="category";
	 public static final String DROPDOWN="dropDown";
	 public static final String USER_ROLE = "userRole";	
	 public static final String UNATHOURIZED_USER = "Unauthorized user";
	 
	 public static final String REPROCESS_EVENTS_LIST = "/reprocessJmsEvent/listCorrelationIds";
	 public static final String REPROCESS_EVENTS_ALL = "/reprocessJmsEvent/reprocessAllJmsEvent";
	 public static final String REPROCESS_EVENTS = "/reprocessJmsEvent/{correlationId}";
	
}
