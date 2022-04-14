package com.loyalty.marketplace.banners.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BannerRequestDto {

	private String imageUrl;
	private String webUrl;
	private String isfeatured;
	private String isPromotional;
	private String bannerType;
	private String isActive;
	private String priority;
	private String reason;
	private String adsJsonAnimationUrl;
	private List<String> eligibleNationalitiesId;
	private String fromBirthDate;
	private String toBirthDate;
	private String createUserId;
	private String createdDateStamp;
	private String lastOpUser;
	private String lastOpDatestamp;
	private List<String> eligibleCustomerType;
	private List<String> eligiblecustomerSubsDetails;
	private List<String> eligibleCoBrandCardDetails;
	
}
