package com.loyalty.marketplace.banners.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class BannerResponseDto {
	
	private String id;
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
    private String createOpUser;
	private String createdOpDateStamp;
	private String lastOpUser;
	private String lastOpDateStamp;
	private List<String> eligibleCustomerType;
	private List<String> eligiblecustomerSubsDetails;
	private List<String> eligibleCoBrandCardDetails;

}
