package com.loyalty.marketplace.image.domain;

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
public class MerchantOfferImageDomain {

	private String domainId;
	private String domainName;
	private String availableInChannel;
	private String imageType;
	private Integer imageSize;
	private Integer imageLength;
	private Integer imageHeight;
	
	public MerchantOfferImageDomain(MerchantOfferImageBuilder imageBuilder) {
		this.domainId = imageBuilder.domainId;
		this.domainName = imageBuilder.domainName;
		this.availableInChannel = imageBuilder.availableInChannel;
		this.imageType = imageBuilder.imageType;
		this.imageSize = imageBuilder.imageSize;
		this.imageLength =  imageBuilder.imageLength;
		this.imageHeight =  imageBuilder.imageHeight;
	}
	
	public static class MerchantOfferImageBuilder {
		
		private String domainId;
		private String domainName;
		private String availableInChannel;
		private String imageType;
		private Integer imageSize;
		private Integer imageLength;
		private Integer imageHeight;
		
		public MerchantOfferImageBuilder(String domainId, String domainName, String availableInChannel, String imageType) {
			super();
			this.domainId = domainId;
			this.domainName = domainName;
			this.availableInChannel = availableInChannel;
			this.imageType = imageType;
		}
		
		public MerchantOfferImageBuilder imageSize(Integer imageSize) {
			this.imageSize = imageSize;
			return this;
		}
		
		public MerchantOfferImageBuilder imageLength(Integer imageLength) {
			this.imageLength = imageLength;
			return this;
		}
		
		public MerchantOfferImageBuilder imageHeight(Integer imageHeight) {
			this.imageHeight = imageHeight;
			return this;
		}
		
		public MerchantOfferImageDomain build() {
			return new MerchantOfferImageDomain(this);
		}
		
	}
	
}
