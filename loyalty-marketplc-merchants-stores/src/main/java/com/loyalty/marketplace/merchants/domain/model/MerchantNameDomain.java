package com.loyalty.marketplace.merchants.domain.model;

import org.springframework.stereotype.Component;

@Component
public class MerchantNameDomain {

	private String merchantNameEn;

	private String merchantNameAr;
	
	public String getMerchantNameEn() {
		return merchantNameEn;
	}

	public String getMerchantNameAr() {
		return merchantNameAr;
	}

	public MerchantNameDomain() {

	}

	public MerchantNameDomain(MerchantNameBuilder name) {
		super();
		this.merchantNameEn = name.merchantNameEn;
		this.merchantNameAr = name.merchantNameAr;
	}

	public static class MerchantNameBuilder {

		private String merchantNameEn;

		private String merchantNameAr;

		public MerchantNameBuilder(String merchantNameEn, String merchantNameAr) {
			super();
			this.merchantNameEn = merchantNameEn;
			this.merchantNameAr = merchantNameAr;
		}

		public MerchantNameBuilder merchantNameEn(String merchantNameEn) {
			this.merchantNameEn = merchantNameEn;
			return this;

		}

		public MerchantNameBuilder merchantNameAr(String merchantNameAr) {
			this.merchantNameAr = merchantNameAr;
			return this;

		}

		public MerchantNameDomain build() {
			return new MerchantNameDomain(this);
		}
	}
	
}
