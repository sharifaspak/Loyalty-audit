package com.loyalty.marketplace.merchants.outbound.dto;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImageAttributes {

	@Field("ImageCategory")
	private String imageCategory;
	
	@Field("DomainId")
	private String domainId;
	
	@Field("ImageUrl")
	private String imageUrl;
	
	@Field("ImageUrlDR")
	private String imageUrlDr;
	
	@Field("ImageUrlProd")
	private String imageUrlProd;
	
	@Field("AvailableInChannel")
	private String availableInChannel;
	
	@Field("ImageType")
	private String imageType;
	
}
