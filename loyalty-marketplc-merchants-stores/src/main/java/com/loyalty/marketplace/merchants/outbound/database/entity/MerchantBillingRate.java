package com.loyalty.marketplace.merchants.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

public class MerchantBillingRate {

	private String id;
	
	@Field(value = "Rate")
	private Double rate;
	
	@Field(value = "StartDate")
    private Date startDate;
    
	@Field(value = "EndDate")
    private Date endDate;
    
	@Field(value = "RateType")
	private String rateType;
    
	@Field(value = "Currency")
    private String currency;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
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
	public String toString() {
		return "MerchantBillingRate [rate=" + rate + ", startDate=" + startDate + ", endDate=" + endDate + ", rateType="
				+ rateType + ", currency=" + currency + "]";
	}
	
}
