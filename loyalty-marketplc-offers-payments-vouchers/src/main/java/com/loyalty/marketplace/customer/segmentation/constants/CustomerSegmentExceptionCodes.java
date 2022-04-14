//package com.loyalty.marketplace.customer.segmentation.constants;
//
//import com.loyalty.marketplace.constants.IMarketPlaceCode;
//
//public enum CustomerSegmentExceptionCodes implements IMarketPlaceCode{
//	
//    //Mongo DB Exceptions	
//	MONGO_WRITE_EXCEPTION(2851, "MongoDB write error while saving offer entity to database."),
//	VALIDATION_EXCEPTION(2852, "ModelMapper validation error while mapping using model mapper."),
//	
//	CUSTOMER_SEGMENT_ADDITION_RUNTIME_EXCEPTION(2853,"Some error while saving customer segment to repository."),
//    CUSTOMER_SEGMENT_UPDATION_RUNTIME_EXCEPTION(2854,"Some error while updating customer segment in repository."),
//    
//    
//    ;
//	
//
//	private final int id;
//	private final String msg;
//
//	CustomerSegmentExceptionCodes(int id, String msg) {
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
