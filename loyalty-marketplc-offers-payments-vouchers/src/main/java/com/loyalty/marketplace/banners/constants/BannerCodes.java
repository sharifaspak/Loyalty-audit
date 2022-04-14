package com.loyalty.marketplace.banners.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

/**
 * 
 * @author jaya.shukla
 *
 */
public enum BannerCodes implements IMarketPlaceCode{
	
	// API Status Codes
	STATUS_FAILURE(1, "Failed"),
		
	// Invalid parameters Codes
	INVALID_PARAMETERS(25000, "Invalid input parameters."),
	
	// Offer Creation
	BANNER_CREATION_SUCCESS(25001, "Successfully added banner with id "),
	BANNER_CREATION_FAILED(25002, "Failed to add banner"),
	BANNER_CREATION_ERROR(25003, "Error in creating banner "),
	BANNER_UPDATION_SUCCESS(25004, "Successfully updated banners"),
	BANNER_UPDATION_FAILED(25005, "Failed to update banner"),
	BANNER_UPDATION_ERROR(25006, "Error in updating banner "),
	BANNER_DELETION_SUCCESS(25007, "Successfully deleted banner"),
	BANNER_DELETION_FAILED(25008, "Failed to delete banner"),
	BANNER_DELETION_ERROR(25009, "Error in deleting banner "),
	BANNER_LISTING_SUCCESS(25010, "Successfully listed banners"),
	BANNER_LISTING_FAILED(25011, "Failed to list banners"),
	BANNER_LISTING_ERROR(25012, "Error in listing banner "),
	BANNER_LISTING_SPECIFIC_SUCCESS(25013, "Successfully listed banner detail"),
	BANNER_LISTING_SPECIFIC_FAILED(25014, "Failed to fetch banner details"),
	BANNER_LISTING_SPECIFIC_ERROR(25015, "Error in fetching details "),
	BANNER_SERVICE_EXCEPTION(25016, "Exception in service connection : "),
	BANNER_SERVICE_IMPROPER_RESPONSE(25017, "Proper response not received"),
	ERROR_IN_GETTING_TOKEN(25018, "Error in authentication : "),
	BANNER_RUNTIME_EXCEPTION(25019, "Exception occured. Please refer ExceptionLogs collection with id : "), 
	BANNER_REQUEST_EMPTY(25020, "Banner request cannot be empty"), 
	BANNER_NOT_PRESENT(25021, "Requested banner not present"), 
	NO_BANNERS_PRESENT(25022, "No banners retrieved"),
	
	
	;
	
	private final int id;

	private final String msg;

	BannerCodes(int id, String msg) {
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
