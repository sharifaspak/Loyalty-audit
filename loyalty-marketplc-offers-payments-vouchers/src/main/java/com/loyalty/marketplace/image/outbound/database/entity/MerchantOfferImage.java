package com.loyalty.marketplace.image.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MerchantOfferImage {

	@Field("DomainId")
	private String domainId;

	@Field("DomainName")
	private String domainName;
	
	@Field("AvailableInChannel")
	private String availableInChannel;
	
	@Field("ImageType")
	private String imageType;
	
	@Field("ImageSize")
	private Integer imageSize;
	
	@Field("Length")
	private Integer imageLength;
	
	@Field("Height")
	private Integer imageHeight;
	
}
