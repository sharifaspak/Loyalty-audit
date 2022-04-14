package com.loyalty.marketplace.merchants.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class RateTypeRequestDto {

	@Valid
	@NotNull
	private List<RateTypeDto> rateTypes;

	public List<RateTypeDto> getRateTypes() {
		return rateTypes;
	}

	public void setRateTypes(List<RateTypeDto> rateTypes) {
		this.rateTypes = rateTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rateTypes == null) ? 0 : rateTypes.hashCode());
		return result;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof RateTypeRequestDto)) {
            return false;
        }
        RateTypeRequestDto rateTypeRequest  = (RateTypeRequestDto) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getRateTypes(), rateTypeRequest.getRateTypes());
        return builder.isEquals();
    }

	@Override
	public String toString() {
		return "RateTypeRequestDto [rateTypes=" + rateTypes + "]";
	}
	
}
