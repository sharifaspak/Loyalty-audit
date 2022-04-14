package com.loyalty.marketplace.merchants.domain.model;

import org.springframework.stereotype.Component;

@Component
public class MerchantDescriptionDomain {

	private String merchantDescEn;

	private String merchantDescAr;
	
	public String getMerchantDescEn() {
		return merchantDescEn;
	}

	public String getMerchantDescAr() {
		return merchantDescAr;
	}

	public MerchantDescriptionDomain() {

	}

	public MerchantDescriptionDomain(MerchantDescriptionBuilder name) {
		super();
		this.merchantDescEn = name.merchantDescEn;
		this.merchantDescAr = name.merchantDescAr;
	}

	public static class MerchantDescriptionBuilder {

		private String merchantDescEn;

		private String merchantDescAr;

		public MerchantDescriptionBuilder() {
			/*TBD */
		}

		public MerchantDescriptionBuilder merchantDescEn(String merchantDescEn) {
			this.merchantDescEn = merchantDescEn;
			return this;
		}

		public MerchantDescriptionBuilder merchantDescAr(String merchantDescAr) {
			this.merchantDescAr = merchantDescAr;
			return this;
		}

		public MerchantDescriptionDomain build() {
			return new MerchantDescriptionDomain(this);
		}
	}
	
}
