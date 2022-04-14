package com.loyalty.marketplace.voucher.constants;

public enum VoucherManagementCode {

	// API Status Codes
	STATUS_SUCCESS(0, "Success"), STATUS_FAILURE(1, "Failed"),

	// Voucher action static table
	VOUCHER_ACTION_CREATION_FAILED(2600, "Voucher action creation failed"),
	VOUCHER_ACTION_ALREADY_EXISTS(2601, "Voucher action already exists"),
	VOUCHER_ACTION_CREATED_SUCCESSFULLY(2603, "Voucher Action Created Succesfully"),
	
	BURNT_VOUCHER_REPORT_GENERATION_FAILED(2604,"Burnt Voucher Report Generation Failed"),
	BURNT_VOUCHER_REPORT_GENERATED_SUCCESSFULLY(2604,"Burnt Voucher Report Generated Succesfully"),
	INVALID_FORMAT_TYPE(2605,"Invalid Format Type.pdf or csv are application format Types"),
	INVALID_SELECTED_TYPE(2606,"Please provide a partner code or merchant code or store code or an admin role "),
	INVALID_REQUEST(2607, "Invalid Request"),
	

	INVALID_VOUCHER_ACTION(2610, "Invalid Voucher action"),
	VOUCHER_GENERATION_FAILED(2611, "Voucher Generation failed"),
	INVALID_OFFER_ID(2612, "Invalid Offer ID"),
	VOUCHER_GENERATED_SUCCESSFULLY(2613, "Voucher Generated Succesfully"),
	DUPLICATE_VOUCHER_ACTION(2614, "Duplicate Voucher Action"),

	VOUCHER_ALREADY_BURNT(2615, "Voucher Already Burnt"),
	VOUCHER_CANCELLATION_FAILED(2616, "Voucher or Vouchers Cancellation failed"),
	VOUCHER_CANCELLED_SUCCESSFULLY(2617, "Voucher or Vouchers Cancelled Succesfully"),
	INVALID_ACTION(2618, "Invalid action"), LISTING_VOUCHER_FAILED(2619, "Listing Vouchers failed"),
	NO_VOUCHERS_AVAILABLE(2620, "Voucher or Vouchers not available"),
	VOUCHER_BURNT_ALREADY(2630, "Voucher or Vouchers were already burnt"),
	VOUCHERS_CODE_NOT_UNIQUE(2631,"Voucher Code is not Unqiqe"),
	VOUCHER_INVOICE_ALREADY(2632, "Voucher or Vouchers were already invoiced"),
	
	VOUCHER_BALANCE_FROM_CARREFOUR_SUCCESSFULL(2633,"Voucher Balance Retrieved Successfully"),
	VOUCHER_BALANCE_FROM_CARREFOUR_FAILED(2634, "Voucher Balance Enquiry Failed"),
	
	// Listing Vouchers Codes
	NO_ACTIVE_VOUCHERS_AVAILABLE(2640, "No active vouchers available for member."),
	NO_REDEEMED_VOUCHERS_AVAILABLE(2641, "No redeemed vouchers available for member."),
	NO_EXPIRED_VOUCHERS_AVAILABLE(2642, "No expired vouchers available for member."),
	INVALID_VOUCHER_STATUS(2643, "Invalid voucher status, it can be either ACTIVE, REDEEMED or EXPIRED."),
	NO_VOUCHERS_FOR_TRANSACTION(2644, "No voucher available for this transaction."),
	NO_VOUCHER_AVAILABLE(2645, "No voucher available for this voucher code."),
	VOUCHER_CODE_DOES_NOT_BELONG_TO_MERCHANT(2646, "This voucher code does not belong to the merchant."),
	VOUCHER_CODE_DOES_NOT_BELONG_TO_PARTNER(2646, "This voucher code does not belong to the partner."),
	NO_VOUCHERS_FOR_VOUCHER_ID(2647, "No voucher available for this voucher id."),
	
	LISTING_VOUCHERS_SUCCESS(2648, "Vouchers listed successfully"),
	LISTING_VOUCHERS_FAILED(2649, "Listing Vvuchers for member failed"),
	
	VOUCHER_BURN_FAILED(2750, "Burning Voucher Failed"), 
	INVALID_STORE(2751, "Invalid Store"),
	VOUCHER_EXPIRED(2752, "Voucher Expired"),
	VOUCHER_BURN_SUCCESSFULLY(2753, "Voucher Burnt Successfully"),
	VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE(2754,"Voucher not available or not in active status"),
	INVALID_STORE_PIN(2755,"Store pin is required"),
	VOUCHER_LESS_BALANCE(2778, "Please choose a lesser voucher amount to burn, voucher balance is "),
	
	VOUCHER_TRANSFER_FAILED(2756, "Failed to tranfer Voucher"),
	VOUCHER_TRANSFERED_SUCESSFULLY(2757, "Voucher Transferred Succesfully"),
	VOUCHER_PDF_GENERATED_SUCCESSFULLY(2758,"PDF Generated Succesfully"),
	VOUCHER_PDF_GENERATION_FAILED(2759,"PDF Generation falied"),
	TARGETACCOUNT_NUMBER_NOT_EXISTS(2780,"Target Account Number Not Found"),

	// Section for Exception and Error Codes
	GENERIC_RUNTIME_EXCEPTION(2690, "Runtime Exception occured. Please refer logs "),
	INVALID_VOUCHER_QUANTITY(2691, "Voucher quantity does not match with vouchers stored in database"),
	VOUCHERS_NOT_AVAILABLE(2692, "Vouchers are not available"),
	VOUCHER_STATUS_FAILED(2693, "Voucher update status failed"), 
	VOUCHER_EMAIL_FAILED(2694, "Email could not be sent"),
	FILE_NOT_FOUND_EXCEPTION(2695,"File Not found Exception"),
	IO_EXCEPTION(2696,"IO Exception occured"),
	INVALID_PARAMETERS(2697,"Invalid input parameters"),
	DATE_FORMAT_EXCEPTION(2698,"Unable to parse the date expected format is dd/MM/yyyy"),

	VOUCHER_UPLOAD_FAILED_INVALID_FILE(2701, "Invalid file type, please choose a CSV file"),
	VOUCHER_UPLOAD_FAILED(2702, "Upload voucher failed"),	
	VOUCHER_LIST_UPLOAD_FILES(2703,"Success. Please check handback file after some time."),
	VOUCHER_LIST_UPLOAD_FILES_NO_DATA(2704,"No files uploaded for the merchant code"),
	VOUCHER_UPLOAD_FILE_CONTENT_NO_DATA(2704,"No record present for the given fileId"),
	VOUCHER_UPLOAD_FAILED_INVALID_CONTENT(2705, "Upload voucher failed, please check the handback file for more details"),
	VOUCHER_DOWNLOAD_CSV_FAILED(2706,"Error while generating csv file"),
	VOUCHER_LIST_UPLOAD_FILES_PARTNER_ERROR(2707,"Error while connecting to merchant-store microservice"),
	VOUCHER_UPLOAD_FILE_DUPLICATE_VOUCHER_CODE(2708,"Duplicate or null voucher codes in file, please correct it and upload"),
	VOUCHER_UPLOAD_FILE_MERCHANT_CODE_MULTIPLE(2709,"Multiple merchant codes are not allowed per file, please correct it and upload"),
	VOUCHER_UPLOAD_FILE_OFFER_ID_MULTIPLE(2699,"Multiple offer ids are not allowed per file, please correct it and upload"),
	VOUCHER_UPLOAD_FILE_PARTNER_NOT_REDEMPTION(2700,"Partner is not of type Redemption"),
	VOUCHER_UPLOAD_FILE_OFFER_ID_NOT_EXIST(2722,"Offer id does not exist"),
	
	NO_VOUCHERS_FOR_PARTNER(2710, "No Voucher Available for Partner"),
	NO_VOUCHERS_FOR_MERCHANT(2711, "No Voucher Available for Merchant"),
	NO_VOUCHERS_FOR_PARTNER_MERCHANT(2712, "No Voucher Available for Partner and Merchant"),
	PARTER_MERCHANT_CODE_MANDATORY(2713, "Partner Code or Merchant Code mandatory."),
	
	LISTING_SPECIFIC_VOUCHER_SUCCESS(2714, "Specific voucher listed successfully."),
	LISTING_VOUCHERS_FOR_TRANSACTION_SUCCESS(2715, "Vouchers listed for transaction successfully."),
	LISTING_REDEEMED_VOUCHERS_SUCCESS(2716, "Burnt vouchers listed successfully."),
	LISTING_EXPIRED_VOUCHERS_SUCCESS(2717, "Expired vouchers listed successfully."),
	LISTING_ACTIVE_VOUCHERS_SUCCESS(2718, "Active vouchers listed successfully."),
	LISTING_VOUCHERS_FOR_VOUCHER_ID_SUCCESS(2719, "Voucher listed for voucher id successfully."),
	
	PARTNER_TYPE_CHECK_REST_CLIENT_EXCEPTION(2720,"Error while connecting to check if partner type exists in partner management service."),
	PARTNER_TYPE_CHECK_RUNTIME_EXCEPTION(2721,"Error while checking partner type in partner management service."),
	
	VOUCHER_RECONCILIATION_SUCCESS(2730, "Voucher reconciliation successful"),
	VOUCHER_RECONCILIATION_FAIL(2731, "Voucher reconciliation failed"),
	VOUCHER_RECONCILIATION_NO_RECORDS(2732, "No data to reconcile, email is not sent"),
	
	VOUCHER_INVOICED_CHECK_REST_CLIENT_EXCEPTION(2720,"Error while connecting to check if voucher code is invoiced in points bank service."),
	VOUCHER_INVOICED_CHECK_RUNTIME_EXCEPTION(2721,"Error while checking voucher code is invoiced in points bank service."),
	
	VOUCHER_EXPIRY_NOTIFICATION_FAILED(2722, "Voucher expiry push notification failed."),
	
	MEMBER_MANAGEMENT_EXCEPTION(2723, "Failed to connect to Member Management microservice."),
	NO_VOUCHERS_EXPIRING_SOON(2724, "No vouchers expiring in next 7 days."),
	NO_NOTIFICATION_SENT_VOUCHER_EXPIRY(2725, "No voucher expiry notification sent. "),
	NO_MEMBER_FOR_ACCOUNT_NUMBER(2726, "Member does not exist for this account number: "),
	NO_OFFER_ASSOCIATED_FOR_VOUCHER(2727,"Offer associated with this voucher does not exist. Voucher Code, Offer ID: "),
	VOUCHER_EXPIRY_NOTIFICATION_SUCCESS(2728,"Voucher expiry notification sent successfully to voucher codes:  "),
	
	VOUCHER_STATISTICS_NOTIFICATION_SUCCESS(2729,"Voucher statistics notification sent successfully."),
	VOUCHER_STATISTICS_NOTIFICATION_FAILED(2730,"Voucher statistics send notification failed."),
	
	//SUFFICIENT_VOUCHER_CODES_AVAILABLE(2731, "Sufficient voucher codes available for MerchantCode, OfferId: "),
	VOUCHER_COUNT_NOTIFICATION_SUCCESS(2732,"Voucher codes count SMS notification sent successfully. "),
	NO_VOUCHER_FOR_MERCHANT_CODE(2733,"No voucher found for merchant code: "),
	NO_UPLOADED_VOUCHER_CODES(2734,"No uploaded vouchers found."),
	VOUCHER_COUNT_NOTIFICATION_FAILED(2735,"No voucher codes count notification sent. Sufficient voucher codes available."),
	NO_OFFER_ID_FOR_VOUCHER(2736,"No offer associated with this voucher code: "),
	
	NO_ACTIVE_VOUCHER_AVAILABLE(2737, "No active vouchers available to list."),
	
	MAF_EXTERNAL_EXCEPTION(2738, "Error while Connecting to MAF External Service"),
	MAF_SERVICE_ERROR(2739, "Error Response from MAF Service"),
	CARREFOUR_SERVICE_ERROR(2740, "Error Response from Carrefour Service"),
	CARREFOUR_INIT_EXTERNAL_EXCEPTION(2741, "Error while Connecting to Initialize Carrefour External Service"),
	CARREFOUR_INIT_SERVICE_ERROR(2742, "Failure Response from Initialize Carrefour Service"),
	CARREFOUR_GC_EXTERNAL_EXCEPTION(2743, "Error while Connecting to Carrefour GC External Service"),
	CARREFOUR_GC_SERVICE_ERROR(2744, "Error Response from Carrefour GC Service"),
	CARREFOUR_GC_CONFIRM_EXTERNAL_EXCEPTION(2745, "Error while Connecting to Carrefour GC Confirm External Service"),
	CARREFOUR_GC_CONFIRM_SERVICE_ERROR(2746, "Error Response from Carrefour GC Confirm Service"),
	YGAG_EXTERNAL_EXCEPTION(2743, "Error while Connecting to YGAG External Service"),
	YGAG_SERVICE_ERROR(2739, "Error Response from YGAG Service"),
	UNKNOWN_PARTNER(2747, "Unknown Partner"),
	SMLS_SERVICE_ERROR(2792, "Error Response from SMLS Service"),
	SMLS_EXTERNAL_EXCEPTION(2793, "Error while Connecting to SMLS External Service"),
	PARTNER_NOTCONFIGURED(2747, "Partner is not configured to Ask Voucher"),
	
	FREE_VOUCHER_OFFER_NOT_VALID(2747, "Offer id is not free or active"),	
	SENDER_ACCOUNT_NOT_ACTIVE(2748, "Sender account is not active"),
	VOUCHER_GIFTED_SUCESSFULLY(2749, "Voucher gifted Succesfully"),
	ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED(2750, "Upload Account free voucher failed"),
	ACCOUNT_UPLOAD_FAILED_INVALID_CONTENT(2751, "Upload account and generate free voucher failed, please check the handback file for more details"),
	FREE_VOUCHER_OFFER_NOT_DEAL(2752, "Only discount or cash offers supported, please choose a different offer."),
	VOUCHER_GIFTED_NOT_SUCESSFULL(2753, "Voucher could not be gifted"),
	VOUCHER_GIFTED_NOT_SAVED(2754, "Voucher gifting could not be saved"),
	SENDER_ACCOUNT_NOT_PRESENT(2755, "Sender account is not present"),
	VOUCHER_GIFT_NOT_PRESENT(2756, "Voucher gift details not present"),
	VOUCHER_GIFT_INVALIDDATEORTIME(2757,"Invalid date or time"),
	VOUCHER_GIFT_TEMPLATE_ERROR(2758,"Error while getting templates for sms/email"),
	NO_VOUCHERS_FOR_GIFTING_AVAILABLE(2759, "Voucher is not available"),
	NO_IMAGES_AVAILABLE(2760, "Image selected is not available"),
	VOUCHER_GIFT_DETAILS_SUCCESS(2761, "Fetching voucher gift details successful"),
	
	LISTING_VOUCHERS_FOR_TRANSACTION_FAILED(2762, "Vouchers listing for transaction failed."),
	LISTING_VOUCHERS_FOR_TRANSACTION_BUSINESS_ID_EMPTY(2763, "Business IDs list must not be empty."),
	LISTING_VOUCHERS_FAILURE(2764, "Listing vouchers failed."),
	NO_MEMBER_ACCOUNT_NUMBER(2765, "Member does not exist or is not active for this account number: "),
	INVALID_DATE_FORMAT(2766, "Please provide date in this format (yyyy-MM-dd)"),
	LISTING_SPECIFIC_VOUCHERS_FAILURE(2767, "Listing specifc voucher failed."),
	LISTING_VOUCHERS_FOR_VOUCHER_ID_FAILURE(2769, "Listing voucher for voucher id failed."),
	NO_VOUCHER_AVAILABLE_FOR_CODE_ACCOUNT(2770, "No voucher available for this voucher code & account number: "),
	NO_ACCOUNT_AVAILABLE_FOR_CODE(2771, "Account not found for account number: "),
	
	INVALID_USER_NAME(2772,"User name is not present or invalid"),
	
	NO_VOUCHERS_DOWNLOADED_TODAY(2773, "No vouchers downloaded today."),
	
	CRFR_ALERT_SUCCESS(2774, "Carrefour Alert Success"),
	CRFR_ALERT_FAILED(2775, "Carrefour Alert Failed"),
	YGAG_ALERT_SUCCESS(2776, "YGAG Alert Success"),
	YGAG_ALERT_FAILED(2777, "YGAG Alert Failed"),

	NO_OFFERS_VOUCHER_ACTION_THREE(2778,"No bulk upload offers available."),
	MULTIPLE_VOUCHERS_FOUND(2779,"Multiple Vouchers Found"),
	VOUCHER_BURN_ROLLBACK_SUCCESSFUL(2780, "Voucher Burn RollBack Successful"),
	VOUCHER_ALREADY_ROLLBACK(2781, "This Transaction is Already Rolled Back"),
	ACCOUNT_UPLOAD_FAILED_INVALID_EXT_TRANSACTIONID(2782, "Please provide external transaction id"),
	ACCOUNT_UPLOAD_FAILED_INVALID_DENOMINATION(2783, "Please provide denomination for cash vouchers"),
	HANDBACK_FILES_LISTED_SUCCESSFULLY(2784, "Handback files listed successfully"),
	HANDBACK_FILES_LISTING_FAILED(2785, "Handback files listing failed"),
	HANDBACK_FILES_DATA_NOT_FOUND_IN_RANGE(2786, "No files found based on input filters"),
	HANDBACK_SPECIFIC_FILE_LISTED_SUCCESSFULLY(2787, "Handback file listed successfully"),
	HANDBACK_SPECIFIC_FILE_LISTING_FAILED(2788, "Handback file listing failed"),
	HANDBACK_SPECIFIC_FILE_DATA_NOT_FOUND(2789, "No file found with the id"),

	UPLOADED_FILE_EMPTY(2790, "The uploaded file is empty"),
	UPLOADED_FILE_EXIST(3151, "File Name Exists"),
	COULD_NOT_GENERATE_VOUCHER(2791, "Could not generate vouchers. Please retry."), 
	
	;
	
	private final int id;
	private final String msg;

	VoucherManagementCode(int id, String msg) {
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

