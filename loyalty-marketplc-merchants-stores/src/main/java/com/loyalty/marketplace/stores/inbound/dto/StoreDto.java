package com.loyalty.marketplace.stores.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;

public class StoreDto {
	
	@NotEmpty(message = "{validation.storeDto.storeCode.notEmpty.msg}")
	private String storeCode;	
	
	@NotEmpty(message = "{validation.storeDto.description.notEmpty.msg}")
	private String description;	
	
	@NotEmpty(message = "{validation.storeDto.descriptionAr.notEmpty.msg}")
	private String descriptionAr;
	
	@NotEmpty(message = "{validation.storeDto.address.notEmpty.msg}")
	private String address;
	
	@NotEmpty(message = "{validation.storeDto.addressAr.notEmpty.msg}")
	private String addressAr;
	
	@NotEmpty(message = "{validation.storeDto.longitude.notEmpty.msg}")
	private String longitude;
	
	@NotEmpty(message = "{validation.storeDto.latitude.notEmpty.msg}")
	private String latitude;
		
	@NotEmpty(message = "{validation.storeDto.merchantCode.notEmpty.msg}")
	private String merchantCode;
	
	@NotEmpty(message = "{validation.storeDto.contactPersons.notEmpty.msg}")
	@Valid
	private List<ContactPersonDto> contactPersons;
	
	private boolean optInOrOut;
	

	public boolean isOptInOrOut() {
		return optInOrOut;
	}

	public void setOptInOrOut(boolean optInOrOut) {
		this.optInOrOut = optInOrOut;
	}

	/**
	 * @return the storeCode
	 */
	public String getStoreCode() {
		return storeCode;
	}

	/**
	 * @param storeCode the storeCode to set
	 */
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	/**
	 * @return the descriptionEn
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param descriptionEn the descriptionEn to set
	 */
	public void setDescriptionEn(String description) {
		this.description = description;
	}

	/**
	 * @return the descriptionAr
	 */
	public String getDescriptionAr() {
		return descriptionAr;
	}

	/**
	 * @param descriptionAr the descriptionAr to set
	 */
	public void setDescriptionAr(String descriptionAr) {
		this.descriptionAr = descriptionAr;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the addressAr
	 */
	public String getAddressAr() {
		return addressAr;
	}

	/**
	 * @param addressAr the addressAr to set
	 */
	public void setAddressAr(String addressAr) {
		this.addressAr = addressAr;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the merchantCode
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * @param merchantCode the merchantCode to set
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * @return the contactPersons
	 */
	public List<ContactPersonDto> getContactPersons() {
		return contactPersons;
	}

	/**
	 * @param contactPersons the contactPersons to set
	 */
	public void setContactPersons(List<ContactPersonDto> contactPersons) {
		this.contactPersons = contactPersons;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public String toString() {
		return "StoreDto [storeCode=" + storeCode +   ", descriptionEn=" + description + ", descriptionAr="
				+ descriptionAr + ", address=" + address + ", addressAr=" + addressAr + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", merchantCode=" + merchantCode + ", contactPersons=" + contactPersons
				+ "]";
	}

	


}
