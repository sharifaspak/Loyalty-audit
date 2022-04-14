package com.loyalty.marketplace.image.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class BannerPropertiesRequest {

	@NotNull(message = "{validation.bannerProperties.topBannerLimit.notNull.msg}")
	private Integer topBannerLimit;
	
	@NotNull(message = "{validation.bannerProperties.middleBannerLimit.notNull.msg}")
	private Integer middleBannerLimit;
	
	@NotNull(message = "{validation.bannerProperties.bottomBannerLimit.notNull.msg}")
	private Integer bottomBannerLimit;
	
	@NotEmpty(message = "{validation.bannerProperties.includeRedeemedOffers.notEmpty.msg}")
	private String includeRedeemedOffers;
	
	@NotNull(message = "{validation.bannerProperties.personalizeBannerCount.notNull.msg}")
	private Integer personalizeBannerCount;
	
	@NotNull(message = "{validation.bannerProperties.fixedBannerCount.notNull.msg}")
	private Integer fixedBannerCount;
	
	private String id;
	private boolean inclRedeemedOffer;
	
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
	
	public BannerPropertiesRequest(Integer topBannerLimit, Integer middleBannerLimit, Integer bottomBannerLimit,
			String includeRedeemedOffers, Integer personalizeBannerCount, Integer fixedBannerCount, String program,
			String userName, String token, String externalTransactionId) {
		super();
		this.topBannerLimit = topBannerLimit;
		this.middleBannerLimit = middleBannerLimit;
		this.bottomBannerLimit = bottomBannerLimit;
		this.includeRedeemedOffers = includeRedeemedOffers;
		this.personalizeBannerCount = personalizeBannerCount;
		this.fixedBannerCount = fixedBannerCount;
		this.program = program;
		this.userName = userName;
		this.token = token;
		this.externalTransactionId = externalTransactionId;
	}

	public BannerPropertiesRequest(Integer topBannerLimit, Integer middleBannerLimit, Integer bottomBannerLimit,
			String includeRedeemedOffers, Integer personalizeBannerCount, Integer fixedBannerCount, String id,
			String program, String userName, String token, String externalTransactionId) {
		super();
		this.topBannerLimit = topBannerLimit;
		this.middleBannerLimit = middleBannerLimit;
		this.bottomBannerLimit = bottomBannerLimit;
		this.includeRedeemedOffers = includeRedeemedOffers;
		this.personalizeBannerCount = personalizeBannerCount;
		this.fixedBannerCount = fixedBannerCount;
		this.id = id;
		this.program = program;
		this.userName = userName;
		this.token = token;
		this.externalTransactionId = externalTransactionId;
	}
	
	
	
}
