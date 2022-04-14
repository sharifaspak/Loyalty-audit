package com.loyalty.marketplace.offers.helper.dto;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class OfferMerchantDto {
	
	private ObjectId id;
	@Field("MerchantCode")
	private String merchantCode;
	@Field("Status")
	private String merchantStatus;

}
