package com.loyalty.marketplace.constants;

public enum MarketPlaceCode {

	VALIDATOR_DELIMITOR(" "),
	SAVE_MERCHANT("saveMerchant"),
	
	INVALID_PARAMETER(100, "Invalid input parameters"),
	LIST_ALL_MERCHANTS("listAllMerchants"),
	
	JMS_REPROCESS_SUCCESS(110, "JMS messages reprocessed successfully."),
	JMS_REPROCESS_FAILED(111, "JMS messages reprocessing failed."),
	
	SAVE_STORE("saveStore"),
	GENERIC_RUNTIME_EXCEPTION(2190, "Runtime Exception occured. Please refer logs "),
	TYPE_UNAVAILABLE_FOR_BILLING_RATE(2175, "Rate Type Unavailable for Discount Billing rate"),
	USER_CREATION_EXCEPTION(2146,"Failed to Create User"),
	USER_PASSWORD_UPDATE_EXCEPTION(2149,"Failed to update user password"),
	TOKEN_EXCPETION(2147,"Failed to get KeyClock Token"),
	USER_CREATED_SUCCESSFULLY(2148,"User or users created Successfully"),
	INVALID_PROGRAM_CODE(0001, "Invalid Program Code. Please check if the program exists."),
	PROGRAM_CODE_FETCH_ERROR(0000, "Unable to get any program code from Program Management. Please check if any Program Code is configured. "),
	
	//Rest Client Exception in Partner Management Service
	PARTNER_CHECK_REST_CLIENT_EXCEPTION(200,"Error while connecting to check if partner exists in partner management service."),
	
	PUBLISH_JMS_EVENT_EXCEPTION(132, "Error in publishing JMS Message"),
	REPROCESS_JMS_EVENT_EXCEPTION(133, "Error in reprocessing JMS Message"),;

	private int id = 0;
	private String constant = "";
	  private String msg = "";

	  MarketPlaceCode(int id, String msg) {
	    this.id = id;
	    this.msg = msg;
	  }
	  
	  MarketPlaceCode(String constant) {
		    this.constant = constant;
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
	  
	  public String getConstant() {
		    return this.constant;
		  }

}
