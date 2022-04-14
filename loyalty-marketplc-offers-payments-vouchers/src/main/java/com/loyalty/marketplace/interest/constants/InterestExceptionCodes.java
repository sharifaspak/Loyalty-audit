package com.loyalty.marketplace.interest.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

public enum InterestExceptionCodes implements IMarketPlaceCode{

	//Section for Exception and Error Codes
		INTEREST_NOT_FOUND_EXCEPTION(23500, "Interest Details Not available."),
	    ACCOUNT_NOT_FOUND_EXCEPTION(23501, "Account Does not exists");
	

	private final int id;

	private final String msg;

	InterestExceptionCodes(int id, String msg) {
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