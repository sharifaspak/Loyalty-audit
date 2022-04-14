package com.loyalty.marketplace.welcomegift.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

public enum WelcomeGiftCodes implements IMarketPlaceCode{
	
	// API Status Codes
	STATUS_FAILURE(1, "Failed"),
		
	// Invalid parameters Codes
	INVALID_PARAMETERS(2720, "Invalid input parameters."), 
		 
	ACCOUNT_NUMBER_NOT_VALID(3001, "Account Number is not Valid"),
	EARNED_POINTS(3002, "Get Points on first transaction"),
	SUBSCRIPTION_WELCOME_GIT(3003, "Subscription added as Welcome gift"), 
	VOUCHER_SUBSCRIPTION_WELCOME_GIFT(3005,"Voucher and Subscription added as Welcome gift"),
	DECISION_MANAGER_ISSUE(3004, "No Response from Decision Manager"),
	OFFER_WITH_ID_NOT_PRESENT(2725, "The offer with following id is not available"),
	NO_OFFER_IN_WISHLIST(2726, "There are no offers present in your wishlist."), 
	NO_WISHLIST_FOR_ACCOUNT(2727, "Wishlist does not exist for this account number"),
	NO_ACTIVE_OFFER_IN_WISHLIST(2728, "There are no active offers present in your wishlist."), 
	OFFER_NOT_IN_WISHLIST(2729, "The offer is not present in your wishlist "), 
	OFFER_ALREADY_IN_WISHLIST(2730, "The offer is already present in your wishlist"), 
	UPDATING_WISHLIST_FAILED(2731,"Updating the wishlist failed"), 
	NOT_A_VALID_WISHLIST_ACTION(2732,"The action entered is not valid"), 
	NO_OFFERS_IN_DATABASE(2733, "There are no offers present in the database"),
	VOUCHER_WELCOME_GIFT(3006,"Voucher added as Welcome gift"),
	WELCOME_GIFTTYPE_NULL(3007,"GiftType is null in Welcome gift"),
	SUBSCRIPTIONPROMO_WELCOME_GIFT(3008,"SubscriptionPromo added as Welcome gift"),
	WELCOME_GIFT_NOT_PROVIDED(3009,"Welcome gift not provided"),
	
	;
	
	private final int id;

	private final String msg;

	WelcomeGiftCodes(int id, String msg) {
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
