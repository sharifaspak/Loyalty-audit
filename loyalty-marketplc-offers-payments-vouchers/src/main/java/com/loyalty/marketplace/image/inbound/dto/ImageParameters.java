package com.loyalty.marketplace.image.inbound.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageParameters {

	private String program;
	private String authorization;
	private String externalTransactionId;
	private String userName;
	private String sessionId;
	private String userPrev;
	private String channelId;
	private String systemId;
	private String systemPassword;
	private String token;
	private String transactionId;
	
	private MultipartFile file;
	private String status;
	
	private String offerId;
	private String offerType;
	private String deepLink;
	private Integer bannerOrder;
	private String isStaticBanner;
	private String isBogoOffer;
	private String coBrandedPartner;
	private String bannerPosition;
	private String startDate;
	private String endDate;
	
	private String type;
	private Integer priority;
	private Integer backgroundPriority;
	private String nameEn;
	private String nameAr;
	private String colorCode;
	private String colorDirection;
	private String greetingMessageEn;
	private String greetingMessageAr;
	
}
