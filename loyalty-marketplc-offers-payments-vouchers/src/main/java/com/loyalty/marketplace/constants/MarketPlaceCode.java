package com.loyalty.marketplace.constants;

public enum MarketPlaceCode implements IMarketPlaceCode{
	
	VALIDATOR_DELIMITOR(" "),
	STATUS_SUCCESS(0, "Success"),
	STATUS_FAILURE(1, "Failed"),
	INVALID_PARAMETER(100, "Invalid input parameters"),
	INVALID_PROGRAM_CODE(0001, "Invalid Program Code. Please check if the program exists."),
	PROGRAM_CODE_FETCH_ERROR(0000, "Unable to get any program code from Program Management. Please check if any Program Code is configured. "),
	GENERIC_RUNTIME_EXCEPTION(0002, "Runtime Exception occured. Please refer logs "),
	IMAGE_FILE_WRITE_EXCEPTION(0003, "Marketplace image file write exception. Please refer logs."),
	
	JMS_REPROCESS_SUCCESS(110, "JMS messages reprocessed successfully."),
	JMS_REPROCESS_FAILED(111, "JMS messages reprocessing failed."),
	
	PARTNER_PROMOTE_INSERTION_FAILED(129,"Partner Promote Insertion Failed"),
	PARTNER_PROMOTE_REPORT_FAILED(130, "Partner Promote Report Generation Failed"),
	RECORD_NOT_FOUND(131, "Record not found in the Database"), 
	
	PUBLISH_JMS_EVENT_EXCEPTION(132, "Error in publishing JMS Message"),
	REPROCESS_JMS_EVENT_EXCEPTION(133, "Error in reprocessing JMS Message"),
	SUBSCRIPTION_FILE_UPLOADED_SUCCESS(134, "File Uploaded Successfully & Processing is In Progress"),
	SUBSCRIPTION_FILE_UPLOADED_INVALID(135, "Invalid File Input"),
	SUBSCRIPTION_FILE_LISTED_SUCCESS(136, "Handback files listed successfully"),
	SUBSCRIPTION_FILE_NOT_FOUND(137, "No files found based on input filters"),
	SUBSCRIPTION_FILE_LISTING_FAILED(138, "Handback files listing failed"),	
	SUBSCRIPTION_FILE_CONTAINS_DUP_EXTERNALREFNUM(139, "Subscription File contains duplicate external reference number"),
	SUBSCRIPTION_FILE_CONTAINS_DUP_EXTERNALREFNUM_DB(140, "Subscription File's external reference number is already available in DB, it should be unique"),
	SUBSCRIPTION_SPECIFIC_HBFILE_LISTED_SUCCESS(141, "Specific Handback files downloaded successfully"),
	SUBSCRIPTION_SPECIFIC_HB_FILE_CONTENT_NOT_FOUND(142, "No files found based on given FileId"),
	SUBSCRIPTION_SPECIFIC_HB_FILE_CONTENT_LISTING_FAILED(143, "Handback file Content listing failed"),
	SUBSCRIPTION_FILE_HAS_DUPLICATE_FILENAME(144, "File name already exists"),
	SUBSCRIPTION_FILE_HAS_DUPLICATE_INVOICEREFNUM(145, "Invoice Reference Number already exists."),
	SUBSCRIPTION_FILE_IS_EMPTY(146, "Input file is empty."),
	SUBSCRIPTION_FILE_CONTENT_STRING_INVALID(148, "Content string is not valid."),
	SUBSCRIPTION_FILE_CONTAINS_INVALID_MEMBERSHIP_CODE(149, "Invalid Membership Code"),
	SUBSCRIPTION_FILE_CONTAINS_EMPTY_ACCOUNT_NUMBER(150, "Account Number is Empty or Null"),
	FAILED_IN_MEM_MGMT(151, "Response is null from Member Management"),
	MEMBER_NOT_ENROLLED(152, "Member not enrolled"),
	
	FAILED_TO_CREATE_SERVICE_CALL_LOG_OBJECT(160, "Exception in creating ServiceCallLogsDto object : "),
	
	FILE_WRITE_ERROR(161, "Exception occured in writing the file"),
	FILE_UPLOAD_FAILED(162, "File upload failed"),
	FILE_UPLOAD_SUCCESS(163, "File uploaded successfully"),
	FILE_NOT_FOUND(137, "No files found"),
	;
	
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
