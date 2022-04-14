package com.loyalty.marketplace.stores.constants;

public enum StoreConstants{
	
	STORE_DEFAULT_STATUS("Active"),
	SEMI_COLON(" : ");
	
	 private final String constant;
	
	StoreConstants(String constant) {
		    this.constant = constant;
		  }
	
	public String get() {
	    return this.constant;
	  }
}
