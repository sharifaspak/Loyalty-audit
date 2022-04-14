package com.loyalty.marketplace.offers.promocode.constants.codes;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

public enum PromoCodeErrorCodes implements IMarketPlaceCode{
		// API Status Codes
		STATUS_FAILURE(1, "Failed"),
			
		// Invalid parameters Codes
		INVALID_PARAMETERS(2700, "Invalid input parameters."), 
			 
		GENERIC_RUNTIME_EXCEPTION(2000, "Runtime Exception occured. Please refer logs "),
		PROMOCOD_CREATION_FAILED(2701, "Promo Code Creation Failed"),
		PROMOCODE_VALIDATION_FAILED(2702, "Promo Code is not Valid for this Offer"),
		PROMOCODE_EXIST(2703, "Promo Code is already exist"),
		GETTING_FROM_WISHLIST_FAILED(2723, "Displaying offers from wishlist failed"), 
		OFFER_NOT_PRESENT(2724, "The offer is not available"),
		OFFER_WITH_ID_NOT_PRESENT(2725, "The offer with following id is not available"),
		NO_OFFER_IN_WISHLIST(2726, "There are no offers present in your wishlist."), 
		NO_WISHLIST_FOR_ACCOUNT(2727, "Wishlist does not exist for this account number"),
		NO_ACTIVE_OFFER_IN_WISHLIST(2728, "There are no active offers present in your wishlist."), 
		OFFER_NOT_IN_WISHLIST(2729, "The offer is not present in your wishlist "), 
		OFFER_ALREADY_IN_WISHLIST(2730, "The offer is already present in your wishlist"), 
		UPDATING_WISHLIST_FAILED(2731,"Updating the wishlist failed"), 
		NOT_A_VALID_WISHLIST_ACTION(2732,"The action entered is not valid"), 
		NO_OFFERS_IN_DATABASE(2733, "There are no offers present in the database"), 
		MEMBER_NOT_PRESENT(2734, "The member details not found"), 
		PROMOCODE_DETAILS_NOT_FOUND(2735, "Promocode details not found"), 
		INVALID_PROMOCODE_DETAILS(2736, "Can not update RuleId/PromoCode/PromoCodeLevel Details."),
		PROMOCODE_UPDATION_FAILED(2737,"Promo Code Updation Failed"),
		;
		
		private final int id;

		private final String msg;

		PromoCodeErrorCodes(int id, String msg) {
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
