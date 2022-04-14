package com.loyalty.marketplace.image.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ListImageResult {
	
	private String id;
	private String programCode;
	
	private String imageCategory;
	private String status;
	private String imageUrl;
	private String imageUrlDr;
	private String imageUrlProd;
	private String originalFileName;
	
	private String domainId;
	private String domainName;
	private String availableInChannel;
	private String imageType;
	private Integer imageSize;
	private Integer imageLength;
	private Integer imageHeight;
	
	private String offerId;
	private String offerType;
	private String deepLink;
	private Integer bannerOrder;
	private String bannerPosition;
	private String isStaticBanner;
	private String isBogoOffer;
	private String coBrandedPartner;
	private Date startDate;
	private Date endDate;
	
	private Integer imageId;
	private String type;
	private Integer priority;
	private Integer backgroundPriority;
	private String colorCode;
	private String colorDirection;
	private Language name;
	private Language greetingMessage;
	
}
