package com.loyalty.marketplace.merchants.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;


public class MarketPlaceContactPersonDto {

	private String merchantCode;
	
	private String storeCode;
	
	private String userName; 
	
	private boolean optInOrOut;
	
	@NotNull(message = "One Contact Person is Mandatory")
	@Valid
	private List<ContactPersonDto> contactPersons;

	public String getMerchantCode() {
		return merchantCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isOptInOrOut() {
		return optInOrOut;
	}

	public void setOptInOrOut(boolean optInOrOut) {
		this.optInOrOut = optInOrOut;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public List<ContactPersonDto> getContactPersons() {
		return contactPersons;
	}

	public void setContactPersons(List<ContactPersonDto> contactPersons) {
		this.contactPersons = contactPersons;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contactPersons == null) ? 0 : contactPersons.hashCode());
		result = prime * result + ((merchantCode == null) ? 0 : merchantCode.hashCode());
		result = prime * result + ((storeCode == null) ? 0 : storeCode.hashCode());
		return result;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof MarketPlaceContactPersonDto)) {
            return false;
        }
        MarketPlaceContactPersonDto marketplaceContactPerson  = (MarketPlaceContactPersonDto) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getMerchantCode(), marketplaceContactPerson.getMerchantCode());
        builder.append(getStoreCode(), marketplaceContactPerson.getStoreCode());
        builder.append(getContactPersons(), marketplaceContactPerson.getContactPersons());
        return builder.isEquals();
    }

	@Override
	public String toString() {
		return "MarketPlaceContactPersonDto [merchantCode=" + merchantCode + ", storeCode=" + storeCode
				+ ", contactPersons=" + contactPersons + "]";
	}
	
}
