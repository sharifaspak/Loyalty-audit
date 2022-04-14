package com.loyalty.marketplace.merchants.outbound.dto;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MerchantDropdownResult {

	@Field("MerchantCode")
	private String merchantCode;
	
	@Field("English")
	private String merchantNameEn;
	
	@Field("Arabic")
	private String merchantNameAr;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

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
	
}
