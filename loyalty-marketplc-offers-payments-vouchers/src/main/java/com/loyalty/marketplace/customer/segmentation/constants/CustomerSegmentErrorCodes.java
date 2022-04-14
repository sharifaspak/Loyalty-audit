//
//package com.loyalty.marketplace.customer.segmentation.constants;
//
//import com.loyalty.marketplace.constants.IMarketPlaceCode;
//
//public enum CustomerSegmentErrorCodes implements IMarketPlaceCode{
//	
//	// API Status Codes
//	STATUS_FAILURE(1, "Failed"),
//		
//	// Invalid parameters Codes
//	INVALID_PARAMETERS(2800, "Invalid input parameters."),
//	
//	CUSTOMER_SEGMENT_EXISTS(23483, "Customer segment with this name already exists"), 
//	CUSTOMER_SEGMENT_CREATION_FAILED(23844, "Customer segment creation failed"), 
//	
//	INVALID_OPERATION(23847, "Invalid operation for customer segment"), 
//	HIGH_VALUE_NOT_PRESENT(23848, "Range high value not present for"),
//	LOW_VALUE_NOT_PRESENT(23849, "Range low value not present for"),
//	EXACT_VALUE_NOT_PRESENT(23850, "Comparison value not present for"),
//	INVALID_UNIT(23851, "Invalid unit"), 
//	STRING_VALUE_BLANK(23852, "One or more list value is blank"),
//	;
//	
//	private final int id;
//
//	private final String msg;
//
//	CustomerSegmentErrorCodes(int id, String msg) {
//		this.id = id;
//		this.msg = msg;
//	}
//
//	public int getIntId() {
//		return this.id;
//	}
//
//	public String getId() {
//		return Integer.toString(this.id);
//	}
//
//	public String getMsg() {
//		return this.msg;
//	}
//
//}
