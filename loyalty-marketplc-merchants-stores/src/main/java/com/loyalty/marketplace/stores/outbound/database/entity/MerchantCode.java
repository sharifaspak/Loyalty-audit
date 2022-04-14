package com.loyalty.marketplace.stores.outbound.database.entity;

public class MerchantCode {
	
	private String merchantCode;

	

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	public MerchantCode(String merchantCode) {
		this.merchantCode=merchantCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((merchantCode == null) ? 0 : merchantCode.hashCode());
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
		MerchantCode other = (MerchantCode) obj;
		if (merchantCode == null) {
			if (other.merchantCode != null)
				return false;
		} else if (!merchantCode.equals(other.merchantCode))
					return false;
		return true;
	}

	@Override
	public String toString() {
		return "MerchantCode [merchantCode=" + merchantCode + "]";
	}

}
