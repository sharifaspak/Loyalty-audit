package com.loyalty.marketplace.merchants.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class MerchantName {
	
	@Field("English")
	private String merchantNameEn;

	@Field("Arabic")
	private String merchantNameAr;

	public String getMerchantNameEn() {
		return merchantNameEn;
	}

	public void setMerchantNameEn(String merchantNameEn) {
		this.merchantNameEn = merchantNameEn;
	}

	public String getMerchantNameAr() {
		return merchantNameAr;
	}

	public void setMerchantNameAr(String merchantNameAr) {
		this.merchantNameAr = merchantNameAr;
	}

	@Override
	public String toString() {
		return "MerchantName [merchantNameEn=" + merchantNameEn + ", merchantNameAr=" + merchantNameAr + "]";
	}
	
}
