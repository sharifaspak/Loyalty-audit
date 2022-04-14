package com.loyalty.marketplace.merchants.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MerchantImageRequestDto {
	
	@NotNull (message = "Merchant Code cannot be null")
	@NotEmpty(message="Merchant Code must not be empty")
	private String programCode;
	
	@NotNull (message = "Image Name cannot be null")
	@NotEmpty(message="Image Name must not be empty")
	private String imageName;

	private String titleEn;

	private String titeAr;

	private String descriptionEn;

	private String descriptionAr;

	@NotNull (message = "Image Path cannot be null")
	@NotEmpty(message="Image Path must not be empty")
	private String imagePath;
	
	private String imageOrder;
	
	private String imageType;
	
	@NotNull (message = "Domain Id cannot be null")
	@NotEmpty(message="Domain Id must not be empty")
	private String domainId;
	
	@NotNull (message = "Domain Name cannot be null")
	@NotEmpty(message="Domain Name must not be empty")
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

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
		return "MerchantImageRequestDto [programCode=" + programCode + ", imageName=" + imageName + ", titleEn="
				+ titleEn + ", titeAr=" + titeAr + ", descriptionEn=" + descriptionEn + ", descriptionAr="
				+ descriptionAr + ", imagePath=" + imagePath + ", imageOrder=" + imageOrder + ", imageType=" + imageType
				+ ", domainId=" + domainId + ", domain=" + domain + "]";
	}
	
}
