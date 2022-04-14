package com.loyalty.marketplace.merchants.inbound.dto;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class RateTypeDto {

	private String typeId;
	
	@NotEmpty
	private String typeRate;

	private String typeRateDesc;

	@NotEmpty
	private String type;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeRate() {
		return typeRate;
	}

	public void setTypeRate(String typeRate) {
		this.typeRate = typeRate;
	}

	public String getTypeRateDesc() {
		return typeRateDesc;
	}

	public void setTypeRateDesc(String typeRateDesc) {
		this.typeRateDesc = typeRateDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = prime * result + ((typeRate == null) ? 0 : typeRate.hashCode());
		result = prime * result + ((typeRateDesc == null) ? 0 : typeRateDesc.hashCode());
		return result;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof RateTypeDto)) {
            return false;
        }
        RateTypeDto rateType  = (RateTypeDto) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getTypeId(), rateType.getTypeId());
        builder.append(getTypeRate(), rateType.getTypeRate());
        builder.append(getTypeRateDesc(), rateType.getTypeRateDesc());
        builder.append(getType(), rateType.getType());
        return builder.isEquals();
    }

	@Override
	public String toString() {
		return "RateTypeDto [typeId=" + typeId + ", typeRate=" + typeRate + ", typeRateDesc=" + typeRateDesc + ", type="
				+ type + "]";
	}
	
}
