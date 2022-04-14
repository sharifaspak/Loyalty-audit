package com.loyalty.marketplace.merchants.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MerchantImagesResult {
		
	private String programCode;
	
	private String imageName;

	private String titleEn;

	private String titeAr;

	private String descriptionEn;

	private String descriptionAr;

	private String imageUrl;
	
	private String imageOrder;
	
	private String imageType;
	
	private String domainId;
	
	private String domain;

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getTiteAr() {
		return titeAr;
	}

	public void setTiteAr(String titeAr) {
		this.titeAr = titeAr;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getDescriptionAr() {
		return descriptionAr;
	}

	public void setDescriptionAr(String descriptionAr) {
		this.descriptionAr = descriptionAr;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageOrder() {
		return imageOrder;
	}

	public void setImageOrder(String imageOrder) {
		this.imageOrder = imageOrder;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return "MerchantImagesResult [programCode=" + programCode + ", imageName=" + imageName + ", titleEn=" + titleEn
				+ ", titeAr=" + titeAr + ", descriptionEn=" + descriptionEn + ", descriptionAr=" + descriptionAr
				+ ", imageUrl=" + imageUrl + ", imageOrder=" + imageOrder + ", imageType=" + imageType + ", domainId="
				+ domainId + ", domain=" + domain + "]";
	}

}
