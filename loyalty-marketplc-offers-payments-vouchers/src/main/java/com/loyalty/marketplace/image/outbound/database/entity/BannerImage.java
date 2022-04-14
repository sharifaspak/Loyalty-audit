package com.loyalty.marketplace.image.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BannerImage {
	
	@Field("OfferId")
	private String offerId;
	
	@Field("OfferType")
	private String offerType;
	
	@Field("DeepLink")
	private String deepLink;
	
	@Field("BannerOrder")
	private Integer bannerOrder;
	
	@Field("BannerPosition")
	private String bannerPosition;
	
	@Field("IsStaticBanner")
	private String isStaticBanner;
	
	@Field("IsBogoOffer")
	private String isBogoOffer;
	
	@Field("CoBrandedPartner")
	private String coBrandedPartner;
	
	@Field("StartDate")
	private Date startDate;
	
	@Field("EndDate")
	private Date endDate;
	
}
