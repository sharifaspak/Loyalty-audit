package com.loyalty.marketplace.banners.outbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BannerServiceRequestDto {

	private Integer id;
	private String imageUrl;
	private String webUrl;
	private String isfeatured;
	private String isPromotional;
	private String bannerType;
	private String isActive;
	private Integer priority;
	private String reason;
	private String adsJsonAnimationUrl;
	private List<Integer> eligibleNationalitiesId;
	private String fromBirthDate;
	private String toBirthDate;
	private String createUserId;
	private String createdDateStamp;
	private String lastOpUser;
	private String lastOpDatestamp;
	private List<String> eligibleCustomerType;
	private List<String> eligibleCustomerSubsDetails;
	private List<String> eligibleCoBrandCardDetails;
	
}
