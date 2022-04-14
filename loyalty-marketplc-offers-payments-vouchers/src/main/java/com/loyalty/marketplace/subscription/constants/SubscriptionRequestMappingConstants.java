package com.loyalty.marketplace.subscription.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class SubscriptionRequestMappingConstants extends RequestMappingConstants {
	
	private SubscriptionRequestMappingConstants() {
		
	}
	
	public static final String ACTION="action";
	public static final String ACCOUNT_NUMBER="accountNumber";
	public static final String SUBSCRIPTION_STATUS="subscriptionStatus";
	public static final String CANCEL_TYPE="cancelType";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String RENEW_DATE = "renewDate";
	public static final String SUBSCRIPTION_ID = "subscriptionId";
	public static final String SUBSCRIPTION_CATALOG_ID = "subscriptionCatalogId";
	public static final String PARTNER_CODE = "partnerCode";
	public static final String INVOICE_REFERENCE_NUMBER = "invoiceReferenceNumber";
	public static final String UPLOADED_FILE_INFO_ID = "fileId";

	public static final String MANAGE_RENEWAL = "/manageRenewal";
	public static final String CONFIGURE_SUBSCRIPTION_CATALOG = "/subscriptionCatalog";
	public static final String SUBSCRIPTION_RENEWAL = "/subscriptionRenewal";
	public static final String UPDATE_SUBSCRIPTION_CATALOG = "/subscriptionCatalog/{subscriptionCatalogId}";

}
