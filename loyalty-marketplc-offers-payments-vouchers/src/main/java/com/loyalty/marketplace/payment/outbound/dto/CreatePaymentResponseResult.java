package com.loyalty.marketplace.payment.outbound.dto;

import java.io.Serializable;
import java.util.HashMap;

public class CreatePaymentResponseResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer paymentStatus;
	private Integer transactionNo;
	private String activityCode;
	private Double earnPointsRate;
	private String partnerCode;
	private HashMap<String, String> additionalParameters;
	
	
	public Integer getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Integer getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(Integer transactionNo) {
		this.transactionNo = transactionNo;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public Double getEarnPointsRate() {
		return earnPointsRate;
	}
	public void setEarnPointsRate(Double earnPointsRate) {
		this.earnPointsRate = earnPointsRate;
	}
	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	public HashMap<String, String> getAdditionalParameters() {
		return additionalParameters;
	}
	public void setAdditionalParameters(HashMap<String, String> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}
	
}
