package com.loyalty.marketplace.payment.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

/**
 * The MarketplaceCode should contain all the Business, Exception and
 * Error handling codes used thought the Partner Management micro-service.
 * 
 * @author n.chilukuri
 */
public enum MarketplaceCode implements IMarketPlaceCode{

	// Section for Business Codes
	LOGIN_FAILURE(-2, "Login failure"),

	ERROR_STATUS(-1, "Requested details not found"),

	// Section for Exception and Error Codes
	GENERIC_RUNTIME_EXCEPTION(-3, "Runtime Exception occured"),

	MONGO_DB_CONNECTION_ERROR(1120, "Failed to connect to Mongo DB"),

	REST_CLIENT_EXCEPTION(1130, "REST client Exception"),
	
	INVALID_PARAM_SELECTEDPAYMENTITEM(2001, "selectedPaymentItem is null or empty"),
	
	INVALID_PARAM_DIRHAMVALUE(2001,"dirham values is null or invalid"),
	
	INVALID_PARAM_POINTSVALUE(2001,"point values is null or invalid"),
	
	INVALID_PARAM_SELECTEDOPTION(2001,"selectoption is null or invalid"),
	
	INVALID_PARAM_OFFERID(2001,"offerId is null or invalid"),
	
	INVALID_PARAM_VOUCHER_DENOMINATION(2001,"voucherDenomination is null or invalid"),
	
	INVALID_PARAM_ACCOUNTNUMBER(2001,"accountNumber is null or invalid"),
	
	INVALID_PARAM_PROMOCODE(2001,"promoCode is null or invalid"),
	
	INVALID_PARAM_LANGUAGE(2001,"language is null or invalid"),
	
	INVALID_PARAM_ATGUSERNAME(2001, "atgUsername is null or invalid"),
	
	INVALID_PARAM_COUNT(2001,"count is null or invalid"),
	
	TIBCO_ADJUSMENT_POSTING_FAILED(2502, "Tibco Adjusment Posting Payment failed"),
	
	TIBCO_MISC_POSTING_FAILED(2503, "Tibco Misc Posting Payment failed"),
	
	TIBCO_PAYMENT_POSTING_FAILED(2504, "Tibco Payment Posting failed"), 
	
	BILLING_ADAPTER_FAILED(2505, "Billing Adapter Service call failed"),
	
	EPG_PAYER_NOT_PRESENT_PAYMENT_SCENARIO_FAILED(2506, "EPG Payer Not Present Payment Scenario Integartion Failed"),
	
	SMILES_PAYMENT_REVERSAL_API_FAILED(2507, "Smiles payment Reversal API call Failed."),
	
	FILE_ID_EMPTY(2507, "File Id is Mandatory"),
	
	FILE_ID_INVALID(2507, "Invalid File Id"),
	
	FILE_NOT_FOUND(2507, "File not found for given data"),
	;


	private final int id;
	private final String msg;

	MarketplaceCode(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public int getIntId() {
		return this.id;
	}

	public String getId() {
		return Integer.toString(this.id);
	}

	public String getMsg() {
		return this.msg;
	}

}
