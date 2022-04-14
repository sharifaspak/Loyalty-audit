package com.loyalty.marketplace.gifting.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

public enum GiftingCodes implements IMarketPlaceCode {
	
	VALIDATOR_DELIMITOR(" "),
	
	//GENERAL CODES
	INVALID_PARAMETER(8200, "Invalid input parameters."),
	GENERIC_RUNTIME_EXCEPTION(8201, "Runtime exception occured. Please refer logs."),
	IMAGE_FILE_WRITE_EXCEPTION(8202, "Marketplace image file write exception. Please refer logs."),
	DATE_STRING_PARSE_ERROR(8203, "Parsing input date string to date format failed."),
	VALIDATION_EXCEPTION(8204, "ModelMapper validation error while mapping using model mapper."),
	MEMBER_MANAGEMENT_EXCEPTION(8205, "Failed to connect to Member Management microservice."),
	POINTS_BANK_EXCEPTION(8206, "Failed to connect to Points Bank microservice."),
	MEMBER_ACTIVITY_EXCEPTION(8207, "Failed to connect to Member Activity microservice."),
	
	//REQUEST PARAMETERS VALIDATION
	ACCOUNT_NUMBER_MANDATORY_FIELD(8208, "Account number is a mandatory field."),
	MEMBERSHIP_CODE_MANDATORY_FIELD(8209, "Membership code is a mandatory field."),
	GIFT_TYPE_MANDATORY_FIELD(8214, "Gift type is a mandatory field."),
	GIFTING_SPECIFIC_HISTORY_LIST_ID_MANDATORY_FIELD(8215, "Id is a mandatory field."),
	
	//LISTING GOLD TRANSACTION CODES
	LIST_GOLD_TRANSACTION_SUCCESS(8210, "Member gold transactions listed successfully."),
	LIST_GOLD_TRANSACTION_FAILURE(8211, "Member gold transactions listing failed."),
	NO_GOLD_TRANSACTION_FOR_MEMBER(8212, "No gold transactions available for the account number and membership code: "),
	LISTING_GOLD_TRANSACTION_MEMBER_DOES_NOT_EXIST(8213, "Member does not exists for the account number: "),
	
	//BIRTHDAY REMINDER CODES
	BIRTHDAY_REMINDER_REQUEST_SUCCESS(8220, "Birthday reminder requests sent successfully."),
	BIRTHDAY_REMINDER_REQUEST_FAILURE(8221, "Failed to send birthday reminder requests."),
	BIRTHDAY_REMINDER_MEMBER_DOES_NOT_EXIST(8222, "Member does not exists or is not active for the account number: "),
	BIRTHDAY_REMINDER_MEMBER_ALREADY_EXIST(8223, "Birthday reminder exists for member for account number: "),
	BIRTHDAY_REMINDER_STATUS_UPDATE_SUCCESS(8224, "Birthday reminder status updated successfully."),
	BIRTHDAY_REMINDER_STATUS_UPDATE_FAILURE(8225, "Birthday reminder status update failed."),
	BIRTHDAY_REMINDER_SENDER_ACCOUNT_DOES_NOT_EXIST(8226, "Sender account does not exists in reminder list."),
	BIRTHDAY_REMINDER_INVALID_STATUS(8227, "Invalid status. It can be either ACCEPTED or REJECTED."),
	BIRTHDAY_REMINDER_INVALID_REMIND_PRIOR(8228, "Invalid remind prior attribute. It can be either DAY or WEEK or MONTH.."),
	BIRTHDAY_REMINDER_ACCOUNT_DOES_NOT_EXIST(8229, "Account does not exist in sender's birthday reminder list. Sender, Receiver Account Numbers: "),
	
	BIRTHDAY_REMINDER_PUSH_NOTIFICATION_SUCCESS(8230, "Push notification for birthday reminder sent successfully."),
	BIRTHDAY_REMINDER_PUSH_NOTIFICATION_FAILURE(8231, "Failed to send push notification for birthday reminder."),

	BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_SUCCESS(8232, "Push notification for member's birthday sent successfully."),
	BIRTHDAY_NOTIFY_PUSH_NOTIFICATION_FAILURE(8233, "Failed to send push notification for member's birthday."),
	BIRTHDAY_REMINDER_LIST_EMPTY(8234, "No birthday reminder list available."),
	NO_BIRTHDAY_REMINDER_SCHEDULED(8235, "No birthday reminder scheduled to send for sender account number: "),
	BIRTHDAY_REMINDER_ACCOUNT_INACTIVE_NOT_EXISTS(8236, "Account does not exist or is not active for account number: "),
	
	//GIFTING POINTS & GOLD
	GIFT_TYPE_FIELD_MANDATORY(8216, "Gift type field is mandatory."),
	MARKETPLACE_GIFTING_FAILURE(8217, "Marketplace gifting failure."),
	GIFTING_GOLD_SUCCESS(8218, "Gold gifted to member successfully."),	
	INVALID_GIFT_TYPE(8240, "Invalid gift type. It can be either VOUCHER, POINTS or GOLD."),
	INVALID_FILTER_TYPE(8241, "Invalid filter type. It can be either SENT, RECEIVED or ALL."),
	MEMBER_DOES_NOT_EXIST(8242, "Member does not exist for the account number: "),
	GIFTING_POINTS_SUCCESS(8243, "Points gifted to member successfully."),
	GIFTING_POINTS_FAILURE(8244, "Gifting points to member failed."),
	MEMBER_ENROLL_FAILED(8245, "Member enrollment failed. Error: "),
	POINTS_BANK_RESPONSE_NOT_RECEIVED(8246, "Proper response not received from points bank"),
	GIFTING_GOLD_FAILURE(8247, "Gifting gold failed."),
	GIFTING_GOLD_FAILURE_INSUF(8248, "Gifting gold failed, insufficient gold balance"),
	SENDER_POINTS_UNAVAILABLE(8249, "Sender account does not have enough points. Sender points: "),
	NO_GIFTING_LIMITS_AVAILABLE(8250, "No gifting limits found for gift type: "),
	PAYMENT_FAILED(8251, "Payment failed. Reason: "),
	PAYMENT_FAIL(8252, "Payment failed."),
	POINT_SPEND_AMOUNT_UNEQUAL(8253, "Spend amount and points to be gifted are not equal. Spend Amount in Equivalent Points, Gift Points: "),
	MEMBER_INSUFFICIENT_POINTS(8254, "Member does not have enough points to gift."),
	EQUIVALENT_POINTS_CONVERSION_FAILED(8255, "Spend amount to equlvalent points conversion not retrieved."),
	GIFTING_GOLD_FAILURE_WRONG_FEE(8256, "Gifting gold failed, please pay the correct fee"),
	GIFTING_POINTS_FAILURE_WRONG_FEE(8257, "Gifting points failed, please pay the correct fee: "),
	GIFTING_GOLD_FAILURE_SCHEDULE_DATE(8258, "Gifting gold failed, please choose a date to schedule gift."),
	
	GIFTING_HISTORY_LIST_SUCCESS(8259, "Gifting history listed successfully."),
	GIFTING_HISTORY_LIST_FAILURE(8260, "Gifting history listing failed."),
	LISTING_GIFTING_HISTORY_MEMBER_DOES_NOT_EXIST(8261, "Member does not exists for the account number: "),
	LISTING_GIFTING_HISTORY_NOT_FOUND(8262, "No gifting history found for the account number: "),
	
	MEMBER_ACTIVTY_SERVICE_CALL_ERROR(8263, "Error response received from Member Activity microservice. Error: "),
	SCHEDULED_POINTS_GIFT_SUCCESS(8264, "Scheduled gift sent successfully."),
	SCHEDULED_POINTS_GIFT_FAILURE(8265, "Failed to send scheduled gift."),
	SCHEDULED_MEMBER_ACTIVTY_ERROR(8266, "Error response received from Member Activity microservice. Failed to send gift for id: "),
	NO_POINTS_GIFT_SCHEDULED(8267, "No gifts scheduled to be sent today."),
	NO_GIFT_HISTORY_FOR_ID(8268, "No gift history available for the id: "),
	POINTS_GIFT_ALREADY_SENT(8269, "This gift has already been gifted to receiver account. Receiver points transaction reference id: "),
	
	//VOUCHER GIFTING CODES
	VOUCHER_GIFTING_SUCCESS(8270, "Voucher gifted successfully."),
	VOUCHER_GIFTING_FAILED(8271, "Voucher gifting failed."),
	NO_VOUCHERS_FOR_GIFTING_AVAILABLE(8272, "Voucher is not available"),
	NO_IMAGES_AVAILABLE(8273, "Image selected is not available"),
	SCHEDULED_DATE_PAST(8274, "Scheduled date cannot be past date"),
	SCHEDULED_DATE_REQD(8275, "Scheduled date is required, since isScheduled flag is set"),
	
	//GIFTING LIMITS
	GIFTING_LIMITS_CONFIG_FAILED(8280, "Gifting limits configuration failed."),
	GIFTING_LIMITS_INSERT_SUCCESS(8281, "Gifting limits created successfully."),
	GIFTING_LIMITS_INSERT_FAILURE(8282, "Gifting limits creation failed."),
	GIFTING_LIMITS_UPDATE_SUCCESS(8283, "Gifting limits updated successfully."),
	GIFTING_LIMITS_UPDATE_FAILURE(8284, "Gifting limits updation failed."),
	GIFTING_LIMITS_UPDATE_NOT_FOUND(8285, "Gifting limit not found for updation."),
	GIFTING_LIMITS_EXCEPTION(8286, "Exception occured while creating gifting limits."),
	
	GIFTING_LIMITS_LIST_SUCCESS(8287, "Gifting limits listed successfully."),
	GIFTING_LIMITS_LIST_FAILURE(8288, "Gifting limits listing failed."),
	GIFTING_LIMITS_LIST_EXCEPTION(8289, "Exception occured while listing gifting limits."),
	NO_GIFTING_LIMITS_FOUND(8290, "No gifting limits found."),
	
	SENDER_ACCOUNT_NOT_ACTIVE(8291, "Sender account is not active"),
	SENDER_ACCOUNT_NOT_PRESENT(8291, "Sender account is not present"),
	MEMBER_ACCOUNT_NOT_ACTIVE(8293, "The member account is not active for accountNumber: "),

	SENDER_ACCOUNT_EXCEED_DAY(8294, "Sender's account has exceeded gifting limit for the day. Actual Count, Limit, Gift: "),
	SENDER_ACCOUNT_EXCEED_WEEK(8295, "Sender's account has exceeded gifting limit for the week. Actual Count, Limit, Gift: "),
	SENDER_ACCOUNT_EXCEED_MONTH(8296, "Sender's account has exceeded gifting limit for the month. Actual Count, Limit, Gift: "),
	RECEIVER_ACCOUNT_EXCEED_DAY(8297, "Receiver's account has exceeded receving gift limit for the day. Actual Count, Limit, Gift: "),
	RECEIVER_ACCOUNT_EXCEED_WEEK(8298, "Receiver's account has exceeded receving gift limit for the week. Actual Count, Limit, Gift: "),
	RECEIVER_ACCOUNT_EXCEED_MONTH(8299, "Receiver's account has exceeded receving gift limit for the month. Actual Count, Limit, Gift: "),
	
	SENDER_MEMBERSHIP_EXCEED_DAY(8300, "Sender's membership has exceeded gifting limit for the day. Actual Count, Limit, Gift: "),
	SENDER_MEMBERSHIP_EXCEED_WEEK(8301, "Sender's membership has exceeded gifting limit for the week. Actual Count, Limit, Gift: "),
	SENDER_MEMBERSHIP_EXCEED_MONTH(8302, "Sender's membership has exceeded gifting limit for the month. Actual Count, Limit, Gift: "),
	RECEIVER_MEMBERSHIP_EXCEED_DAY(8303, "Receiver's membership has exceeded receving gift limit for the day. Actual Count, Limit, Gift: "),
	RECEIVER_MEMBERSHIP_EXCEED_WEEK(8304, "Receiver's membership has exceeded receving gift limit for the week. Actual Count, Limit, Gift: "),
	RECEIVER_MEMBERSHIP_EXCEED_MONTH(8305, "Receiver's membership has exceeded receving gift limit for the month. Actual Count, Limit, Gift: "),
	
	GIFTING_COUNTER_RESET_SUCCESS(8306, "Gifting counters reset successfully."),
	GIFTING_COUNTER_RESET_FAILED(8307, "Gifting counters reset failed."),
	GIFTING_RESET_NO_COUNTERS_AVAILABLE(8308, "No counters available to reset."),
	
	LIST_NO_SENT_GIFTING_HISTORY_AVAILABLE(8309, "No sent gift history available."),
	LIST_NO_RECEIVED_GIFTING_HISTORY_AVAILABLE(8310, "No received gift history available."),
	LIST_NO_ALL_GIFTING_HISTORY_AVAILABLE(8311, "No gift history available for the member."),
	
	SENDER_CUSTOMER_SEGMENT_CHECK_FAILED(8312, "Sender account does not meet customer segment requirements."),
	RECEIVER_CUSTOMER_SEGMENT_CHECK_FAILED(8313, "Receiver account does not meet customer segment requirements."),
	SCHEDULED_DATE_IS_PAST(8314, "Scheduled date can not be a past date."),
	
	// LIST SPECIFIC GIFTING HISTORY CODES
	GIFTING_SPECIFIC_HISTORY_LIST_SUCCESS(8315, "Specific gift history listed successfully."),
	GIFTING_SPECIFIC_HISTORY_LIST_NOTIFICATION_SENT_SUCCESS(8316, "Specific gift history listed successfully, push notification and email sent to sender."),
	GIFTING_SPECIFIC_HISTORY_LIST_FAILURE(8317, "Specific gift history listing failed."),
	GIFTING_SPECIFIC_HISTORY_NOT_AVAILABLE(8318, "Gift history not present for the id: "),
	GIFTING_SPECIFIC_HISTORY_PUSH_NOTIFICATION_FAILURE(8319, "Failed to send push notification for viewed gift."),
	GIFTING_SPECIFIC_HISTORY_EMAIL_FAILURE(8320, "Failed to send email for viewed gift."),
	
	// CANCEL GIFT CODES
	VOUCHER_GIFT_CANCEL_SUCCESS(8321, "Voucher gift cancellled successfully."),
	VOUCHER_GIFT_CANCEL_FAILED(8322, "Voucher gift cancellation failed."),
	VOUCHER_GIFT_ALREADY_USED(8323, "Voucher gift cannot be cancelled since it has already been used."),
	VOUCHER_GIFT_VOUCHER_NOT_ACTIVE(8324, "Voucher not found or is not active."),
	VOUCHER_GIFT_PARTIAL_BALANCE(8325, "Voucher has already been used and cannot be gifted."),
	VOUCHER_GIFT_VOUCHER_NOT_GIFTED(8326, "Voucher has not been gifted."),
	VOUCHER_GIFT_NO_CRFR_BALANCE_RETRIEVED(8327, "No balance received from Carrefour."),
	VOUCHER_GIFT_NO_VOUCHER_AMOUNT(8328, "No voucher amount populated for the voucher."),
	VOUCHER_GIFT_NO_GIFT_ID(8329, "No gift id populated for the voucher."),
	VOUCHER_GIFT_NO_GIFT_HISTORY_FOUND(8330, "No gift history found for the id: "),
	VOUCHER_GIFT_NO_SENDER_ACCOUNT_NUM_FOUND(8331, "No sender account number found for reverting gift."),
	
	DECISION_MANAGER_ERROR_RESPONSE(8332, "Error response received from decision manager service."),
	
	//Gifting Codes
	MONGO_ERROR(8333, "Error from mongo db."),
	RUNTIME_EXCEPTION(8334, "Runtime Exception."),
	GIFT_NOT_FOUND(8335, "Gift not found in database."),
	INVALID_GIFT_TYPE_VALUE(8336, "Gift type is invalid"), 
	MIN_VALUE_NOT_PRESENT(8337, "Min value mandatory for premium gift"),
	MAX_VALUE_NOT_PRESENT(8338, "Max value mandatory for premium gift"),
	GIFT_ID_NOT_PRESENT(8339, "Gift Id is mandatory for promotional gift"),
	CHANNEL_ID_NOT_PRESENT(8340, "Channel Id is mandatory for promotional gift"),
	SUBSCRIPTION_CATALOG_ID_NOT_PRESENT(8341, "Subscription Catalog Id is mandatory for subscription purchase gift"), 
	PROMOTIONAL_GIFT_ID_EXISTING(8342, "Promotional gift with this id already exists"),
	PREMIUM_GIFT_POINT_RANGE_EXISTING(8343, "Premium gift with this point range already exists"),
	SUBSCRIPTION_GIFT_ID_EXISTING(8344, "Subscription gift with this subscription catalog id already exists"),
	GIFT_DELETED_SUCCESSFULLY(8345, "Gift Deleted Successfully"),
	GIFTING_CONFIGURATION_NOT_AVAILABLE(8346, "Gift Configurations not available"),
	GIFTING_CONFIGURATION_LIST_FAILURE(8347, "Gift Configurations listing failed"),
	GIFTING_CONFIGURATION_LIST_SUCCESS(8348, "Gift Configurations listed successfully"),
	GIFT_CONFIGURED_SUCCESSFULLY(8345, "Gift configured successfully with id "),
	GIFT_CONFIGURATION_FAILED(8345, "Gift configuration failed"),
	GIFT_UPDATED_SUCCESSFULLY(8345, "Gift Updated Successfully"),
	GIFT_UPDATION_FAILED(8345, "Gift Updation Failed"), 
	DENOMINATION_MANDATORY_FOR_CASH_VOUCHER(8346, "Denomination is mandatory for cash voucher"), 
	DENOMINATION_INVALID_FOR_CASH_VOUCHER(8347, "This denomination is not configured for this cash voucher"),
	SUB_OFFER_MANDATORY_FOR_DEAL_VOUCHER(8348, "Suboffer is mandatory for deal vocuher"),
	SUB_OFFER_INVALID_FOR_DEAL_VOUCHER(8349, "This suboffer is not valid for this deal vocuher"),
	GIFT_DELETION_FAILED(8350, "Gift Deletion Failed"), 
	OFFER_ALREADY_EXISTS_IN_GIFT(8351, "Offer already exists in gift"),
	OFFER_DOES_NOT_EXIST_IN_GIFT(8352, "Offer is not configured in gift"), 
	NOT_A_VALID_ACTION(8353, "Action is invalid for offer : "),
	OFFER_DOES_NOT_EXIST_IN_DB(8352, "Offer is not present in db"),
	OFFER_TYPE_INVALID(8352, "Offer type is not valid for the offer"),
	
	;
	
	private int id;
	private String msg;
	private String constant;
	
	GiftingCodes(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	GiftingCodes(String constant) {
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
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

}
