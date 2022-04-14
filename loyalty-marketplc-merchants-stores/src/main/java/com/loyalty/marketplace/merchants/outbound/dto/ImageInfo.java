package com.loyalty.marketplace.merchants.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ImageInfo {

	private String availableInChannel;
	private String imageType;
	private String imageUrl;
	private String imageUrlDr;
	private String imageUrlProd;
	
	public String getImageUrlDr() {
		return imageUrlDr;
	}

	public void setImageUrlDr(String imageUrlDr) {
		this.imageUrlDr = imageUrlDr;
	}

	public String getImageUrlProd() {
		return imageUrlProd;
	}

	public void setImageUrlProd(String imageUrlProd) {
		this.imageUrlProd = imageUrlProd;
	}

	public String getAvailableInChannel() {
		return availableInChannel;
	}

	public void setAvailableInChannel(String availableInChannel) {
		this.availableInChannel = availableInChannel;
	}

	public String getImageType() {
		return imageType;
	}
	
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@Override
	public String toString() {
		return "ImageInfo [availableIncChannel=" + availableInChannel + ", imageType=" + imageType + ", imageUrl="
				+ imageUrl + "]";
	}
	
}
