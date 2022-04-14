package com.loyalty.marketplace.image.domain;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Component
@AllArgsConstructor
@NoArgsConstructor
public class BannerImageDomain {

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
	
	public BannerImageDomain(BannerImageBuilder imageBuilder) {
		this.offerId = imageBuilder.offerId;
		this.offerType = imageBuilder.offerType;
		this.deepLink = imageBuilder.deepLink;
		this.isStaticBanner = imageBuilder.isStaticBanner;
		this.bannerOrder = imageBuilder.bannerOrder;
		this.startDate = imageBuilder.startDate;
		this.endDate =  imageBuilder.endDate;
		this.isBogoOffer = imageBuilder.isBogoOffer;
		this.coBrandedPartner = imageBuilder.coBrandedPartner;
		this.bannerPosition = imageBuilder.bannerPosition;
	}
	
	public static class BannerImageBuilder {
		
		private String offerId;
		private String offerType;
		private String deepLink;
		private String isStaticBanner;
		private Integer bannerOrder;
		private Date startDate;
		private Date endDate;
		private String isBogoOffer;
		private String coBrandedPartner;
		private String bannerPosition;
		
		public BannerImageBuilder(String isStaticBanner, Integer bannerOrder, Date startDate, Date endDate, String isBogoOffer,
				String coBrandedPartner, String bannerPosition) {
			super();
			this.isStaticBanner = isStaticBanner;
			this.bannerOrder = bannerOrder;
			this.startDate = startDate;
			this.endDate = endDate;
			this.isBogoOffer = isBogoOffer;
			this.coBrandedPartner = coBrandedPartner;
			this.bannerPosition = bannerPosition;
		}
		
		public BannerImageBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}
		
		public BannerImageBuilder offerType(String offerType) {
			this.offerType = offerType;
			return this;
		}
		
		public BannerImageBuilder deepLink(String deepLink) {
			this.deepLink = deepLink;
			return this;
		}
		
		public BannerImageDomain build() {
			return new BannerImageDomain(this);
		}
		
	}
	
}
