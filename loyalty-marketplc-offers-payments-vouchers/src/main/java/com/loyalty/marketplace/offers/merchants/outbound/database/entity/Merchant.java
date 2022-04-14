package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(collection = OffersDBConstants.MERCHANT)
public class Merchant {

	@Field("ProgramCode")
	private String program;
	
	@Id
	private String id;

	@Indexed
	@Field("MerchantCode")
	private String merchantCode;

	@Indexed
	@Field("Name")
	private MerchantName merchantName;

	@Field("PartnerCode")
	private String partner;

	@Field("Category")
	@DBRef
	private Category category;
	
	@Field("BarcodeType")
	@DBRef
	private Barcode barcodeType;

	@Field("Status")
	private String status;
	
	@Field("WhatYouGet")
	private WhatYouGet whatYouGet;
	
	@Field("TAndC")
	private TAndC tnC;
	
	@Field("MerchantDescription")
	private MerchantDescription merchantDescription;
	
	@Field("ExternalName")
	private String externalName;

	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;

	@Field("ContactPersons")
	private List<ContactPerson> contactPersons;

	@Field("DiscountBillingRates")
	private List<MerchantBillingRate> billingRates;
	
	@Field("ImageUrl")
	private String imageUrl;
	
	@Field("OfferCount")
	private long offerCount;
	
}