package com.loyalty.marketplace.stores.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;

@JsonInclude(Include.NON_NULL)
public class StoreResult {

    private String storeCode;
	
	private String merchantCode;
	
	private String merchantName;
	
	private String description;
	
	private String descriptionAr;
	
	private String address;
	
	private String addressAr;
	
	private String longitude;
	
	private String latitude;
	
	private String status;
	
	private List<ContactPersonResponseDto> contactPersons;

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionAr() {
		return descriptionAr;
	}

	public void setDescriptionAr(String descriptionAr) {
		this.descriptionAr = descriptionAr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressAr() {
		return addressAr;
	}

	public void setAddressAr(String addressAr) {
		this.addressAr = addressAr;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ContactPersonResponseDto> getContactPersons() {
		return contactPersons;
	}

	public void setContactPersons(List<ContactPersonResponseDto> contactPersons) {
		this.contactPersons = contactPersons;
	}

}
