package com.loyalty.marketplace.merchants.inbound.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MerchantBillingRateDto {
	
	//@NotNull(message = "{validation.MerchantBillingRateDto.rate.notEmpty.msg}")
	private Double rate;
	
	//@NotNull(message = "Start date is mandatory.")
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private String startDate;
    
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private String endDate;
    
	//@NotEmpty(message = "{validation.MerchantBillingRateDto.rateType.notEmpty.msg}")
	private String rateType;
    
    private String currency;

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result + ((rateType == null) ? 0 : rateType.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof MerchantBillingRateDto)) {
            return false;
        }
        MerchantBillingRateDto merchantBillingRate  = (MerchantBillingRateDto) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getCurrency(), merchantBillingRate.getCurrency());
        builder.append(getEndDate(), merchantBillingRate.getEndDate());
        builder.append(getRate(), merchantBillingRate.getRate());
        builder.append(getRateType(), merchantBillingRate.getRateType());
        builder.append(getStartDate(), merchantBillingRate.getStartDate());
        return builder.isEquals();
    }
	
	@Override
	public String toString() {
		return "MerchantBillingRateDto [rate=" + rate + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", rateType=" + rateType + ", currency=" + currency + "]";
	}
	
}
