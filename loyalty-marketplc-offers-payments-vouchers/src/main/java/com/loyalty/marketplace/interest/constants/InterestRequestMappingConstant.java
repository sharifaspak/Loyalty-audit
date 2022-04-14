package com.loyalty.marketplace.interest.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class InterestRequestMappingConstant  extends RequestMappingConstants {
      
	
	public InterestRequestMappingConstant() {
		super();
	}

	public static final String ACCOUNT_NUMBER="accountNumber";
	public static final String API_UPDATE_INTEREST = "/{accountNumber}/interests";
	public static final String API_GET_INTEREST = "/{accountNumber}/interests";
	public static final String RESET_DB = "/resetInterest";
}
