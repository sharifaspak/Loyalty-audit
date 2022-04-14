package com.loyalty.marketplace.stores.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class StoreAddress {

	@Field("English")
	private String address;

	@Field("Arabic")
	private String addressAr;

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

	@Override
	public String toString() {
		return "StoreAddress [address=" + address + ", addressAr=" + addressAr + "]";
	}

}
