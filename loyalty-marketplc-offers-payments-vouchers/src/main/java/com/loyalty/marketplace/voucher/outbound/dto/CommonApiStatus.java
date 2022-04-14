package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.ArrayList;
import java.util.List;

public class CommonApiStatus {

	private String externalTransactionId;
	private String message;
	private String overallStatus;
	private Integer statusCode;
	private List<Errors> errors = new ArrayList<>();

	public CommonApiStatus(String externalTransactionId) {
		super();
		this.externalTransactionId = externalTransactionId;
	}

	@Override
	public String toString() {
		return "CommonApiStatus [externalTransationId=" + externalTransactionId + ", message=" + message
				+ ", overallStatus=" + overallStatus + ", statusCode=" + statusCode + ", errors=" + errors + "]";
	}

	public String getExternalTransactionId() {
		return externalTransactionId;
	}

	public void setExternalTransactionId(String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public List<Errors> getErrors() {
		return errors;
	}

	public void setErrors(List<Errors> errors) {
		this.errors = errors;
	}

}
