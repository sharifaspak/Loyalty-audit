package com.loyalty.marketplace.merchants.outbound.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantOffers {
	
	@JsonProperty(value = "categoryId")
	private String category;
	
	@JsonProperty(value = "offerTitleEn")
	private String offerTitle;
	
	@JsonProperty(value = "offerTypeId")
	private String offerType;
	
	private int pointValue;
	
	private String spendValue;
	
	private String offerId;
	private String offerTitleAr;
	private String offerTitleDescriptionEn;
	private String offerTitleDescriptionAr;
	private String pointsValue;
	private String cost;
	private String typeDescriptionEn;
	private String typeDescriptionAr;
	private String categoryId;
	private String merchantNameEn;
	private String merchantNameAr;
	
}
