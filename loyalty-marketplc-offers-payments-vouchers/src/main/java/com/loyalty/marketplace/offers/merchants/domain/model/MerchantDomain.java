package com.loyalty.marketplace.offers.merchants.domain.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@NoArgsConstructor
@ToString
public class MerchantDomain {

	private String id;
	private String programCode;
	private String merchantCode;
	private MerchantNameDomain merchantName;
	private String partnerCode;
	private String categoryId;
	private String barcodeType;
	private String status;
	private WhatYouGetDomain whatYouGet;
	private TAndCDomain tnC;
	private MerchantDescriptionDomain merchantDescription;
	private String externalName;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	private List<MerchantContactPersonDomain> contactPersons;
	private List<MerchantBillingRateDomain> billingRates;

	public MerchantDomain(MerchantBuilder merchantBuilder) {
		super();
		this.id = merchantBuilder.id;
		this.programCode = merchantBuilder.programCode;
		this.merchantCode = merchantBuilder.merchantCode;
		this.merchantName = merchantBuilder.merchantName;
		this.partnerCode = merchantBuilder.partnerCode;
		this.categoryId = merchantBuilder.categoryId;
		this.barcodeType = merchantBuilder.barcodeType;
		this.status = merchantBuilder.status;
		this.whatYouGet = merchantBuilder.whatYouGet;
		this.tnC = merchantBuilder.tnC;
		this.merchantDescription = merchantBuilder.merchantDescription;
		this.externalName = merchantBuilder.externalName;
		this.dtCreated = merchantBuilder.dtCreated;
		this.dtUpdated = merchantBuilder.dtUpdated;
		this.usrCreated = merchantBuilder.usrCreated;
		this.usrUpdated = merchantBuilder.usrUpdated;
		this.contactPersons = merchantBuilder.contactPersons;
		this.billingRates = merchantBuilder.billingRates;

	}

	public static class MerchantBuilder {
		
		private String id;
		private String programCode;
		private String merchantCode;
		private MerchantNameDomain merchantName;
		private String partnerCode;
		private String categoryId;
		private String barcodeType;
		private String status;
		private WhatYouGetDomain whatYouGet;
		private TAndCDomain tnC;
		private MerchantDescriptionDomain merchantDescription;
		private String externalName;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;
		private List<MerchantContactPersonDomain> contactPersons;
		private List<MerchantBillingRateDomain> billingRates;

		public MerchantBuilder(String id) {
			super();
			this.id = id;
		}
		
		public MerchantDomain build() {
			return new MerchantDomain(this);
		}

	}

}
