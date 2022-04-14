package com.loyalty.marketplace.merchants.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class MerchantDescription {

	@Field("English")
	private String merchantDescEn;

	@Field("Arabic")
	private String merchantDescAr;

	public String getMerchantDescEn() {
		return merchantDescEn;
	}

	public void setMerchantDescEn(String merchantDescEn) {
		this.merchantDescEn = merchantDescEn;
	}

	public String getMerchantDescAr() {
		return merchantDescAr;
	}

	public void setMerchantDescAr(String merchantDescAr) {
		this.merchantDescAr = merchantDescAr;
	}

	@Override
	public String toString() {
		return "MerchantDescription [merchantDescEn=" + merchantDescEn + ", merchantDescAr=" + merchantDescAr + "]";
	}
	
}
