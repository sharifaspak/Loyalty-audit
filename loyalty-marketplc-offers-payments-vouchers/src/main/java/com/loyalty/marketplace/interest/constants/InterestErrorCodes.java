package com.loyalty.marketplace.interest.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

public enum InterestErrorCodes implements IMarketPlaceCode {


	// API Status Codes
	GENERIC_RUNTIME_EXCEPTION(23500, "Runtime Exception occured. Please refer logs "),
	STATUS_FAILURE(1, "ERROR"),
	ACCOUNT_NOT_FOUND(23420, "Account does not exists"),
	NO_INTEREST_TO_DISPLAY(23420, "No interest to display"),
	INTEREST_NOT_CONFIGURED(23422, "Interest does not configuered for this account"),
	FAILED_TO_UPDATE_INTEREST_DETAILS(23420, "Unable to update interest Details");

	private final int id;

	private final String msg;

	InterestErrorCodes(int id, String msg) {
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
