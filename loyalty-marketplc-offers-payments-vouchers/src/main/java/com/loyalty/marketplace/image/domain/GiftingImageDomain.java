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
public class GiftingImageDomain {

	private Integer imageId;
	private String type;
	private Integer priority;
	private Integer backgroundPriority;
	private String colorCode;
	private String colorDirection;
	private LanguageDomain name;
	private LanguageDomain greetingMessage;
	
	public GiftingImageDomain(GiftingImageBuilder imageBuilder) {
		this.imageId = imageBuilder.imageId;
		this.type = imageBuilder.type;
		this.priority = imageBuilder.priority;
		this.backgroundPriority =  imageBuilder.backgroundPriority;
		this.colorCode = imageBuilder.colorCode;
		this.colorDirection = imageBuilder.colorDirection;
		this.name = imageBuilder.name;
		this.greetingMessage = imageBuilder.greetingMessage;
	}
	
	public static class GiftingImageBuilder {
		
		private Integer imageId;
		private String type;
		private Integer priority;
		private Integer backgroundPriority;
		private String colorCode;
		private String colorDirection;
		private LanguageDomain name;
		private LanguageDomain greetingMessage;
		
		public GiftingImageBuilder(Integer imageId, LanguageDomain name, String type, Integer priority,
				Integer backgroundPriority, LanguageDomain greetingMessage) {
			super();
			this.imageId = imageId;
			this.name = name;
			this.type = type;
			this.priority = priority;
			this.backgroundPriority = backgroundPriority;
			this.greetingMessage = greetingMessage;
		}
		
		public GiftingImageBuilder colorCode(String colorCode) {
			this.colorCode = colorCode;
			return this;
		}
		
		public GiftingImageBuilder colorDirection(String colorDirection) {
			this.colorDirection = colorDirection;
			return this;
		}
		
		public GiftingImageDomain build() {
			return new GiftingImageDomain(this);
		}
		
	}
	
}
