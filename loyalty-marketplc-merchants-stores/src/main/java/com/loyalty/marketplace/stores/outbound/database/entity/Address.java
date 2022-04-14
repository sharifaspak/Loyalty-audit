package com.loyalty.marketplace.stores.outbound.database.entity;

public class Address {

	private String address;
	
	private String addressArb;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressArb() {
		return addressArb;
	}

	public void setAddressArb(String addressArb) {
		this.addressArb = addressArb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((addressArb == null) ? 0 : addressArb.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (addressArb == null) {
			if (other.addressArb != null)
				return false;
		} else if (!addressArb.equals(other.addressArb)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Address [address=" + address + ", addressArb=" + addressArb + "]";
	}
	
}
