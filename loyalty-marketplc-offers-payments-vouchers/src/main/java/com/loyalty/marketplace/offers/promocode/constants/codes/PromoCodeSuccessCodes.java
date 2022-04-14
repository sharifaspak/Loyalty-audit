package com.loyalty.marketplace.offers.promocode.constants.codes;

public enum PromoCodeSuccessCodes {
	
STATUS_SUCCESS(0, "Success"),
	
	PROMOCODE_CREATED_SUCCESSFULLY(2800,"PromoCode created successfully"),
	PROMOCODE_VALIDATED_SUCCESSFULLY(2801,"PromoCode is valid"),
	PROMOCODE_UPDATED_SUCCESSFULLY(2802,"PromoCode updated successfully"),
    ;

	private final int id;

	private final String msg;

	PromoCodeSuccessCodes(int id, String msg) {
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
