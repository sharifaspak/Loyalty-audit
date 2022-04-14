package com.loyalty.marketplace.image.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class BannerParameters {

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
	private String offerId;
	private String offerType;
	private String deepLink;
	@NotEmpty(message = "{validation.allParameters.status.notEmpty.msg}")
	private String status;
	@NotNull(message = "{validation.bannerParameters.bannerOrder.notNull.msg}")
	private Integer bannerOrder;
	@NotNull(message = "{validation.bannerParameters.isStaticBanner.notNull.msg}")
	private String isStaticBanner;
	@NotNull(message = "{validation.bannerParameters.isBogoOffer.notNull.msg}")
	private String isBogoOffer;
	@NotEmpty(message = "{validation.bannerParameters.coBrandedPartner.notEmpty.msg}")
	private String coBrandedPartner;
	@NotEmpty(message = "{validation.bannerParameters.bannerPosition.notEmpty.msg}")
	private String bannerPosition;
	@NotEmpty(message = "{validation.bannerParameters.startDate.notEmpty.msg}")
	private String startDate;
	@NotEmpty(message = "{validation.bannerParameters.endDate.notEmpty.msg}")
	private String endDate;
	
	private int length;
	private int height;
	private String path;
	private String imageUrlDr;
	private String imageUrlProd;
	
	public BannerParameters(String program, String authorization, String externalTransactionId, String userName,
			String sessionId, String userPrev, String channelId, String systemId, String systemPassword, String token,
			String transactionId, MultipartFile file, String offerId, String offerType, String deepLink, String status,
			Integer bannerOrder, String isStaticBanner, String isBogoOffer, String coBrandedPartner, String bannerPosition,
			String startDate, String endDate) {
		super();
		this.program = program;
		this.authorization = authorization;
		this.externalTransactionId = externalTransactionId;
		this.userName = userName;
		this.sessionId = sessionId;
		this.userPrev = userPrev;
		this.channelId = channelId;
		this.systemId = systemId;
		this.systemPassword = systemPassword;
		this.token = token;
		this.transactionId = transactionId;
		this.file = file;
		this.offerId = offerId;
		this.offerType = offerType;
		this.deepLink = deepLink;
		this.status = status;
		this.bannerOrder = bannerOrder;
		this.isStaticBanner = isStaticBanner;
		this.isBogoOffer = isBogoOffer;
		this.coBrandedPartner = coBrandedPartner;
		this.bannerPosition = bannerPosition;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
}