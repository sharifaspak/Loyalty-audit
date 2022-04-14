package com.loyalty.marketplace.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiStatus {
	private String externalTransationId;
	private String message;
	private String overallStatus;
	private Integer statusCode;
	private List<ApiError> errors;

	public ApiStatus() {
		super();
	}

	public ApiStatus(String externalTransationId, String message, String overallStatus, Integer statusCode,
			List<ApiError> errors) {
		super();
		this.externalTransationId = externalTransationId;
		this.message = message;
		this.overallStatus = overallStatus;
		this.statusCode = statusCode;
		this.errors = errors;
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

	public List<ApiError> getErrors() {
		return errors;
	}

	public void setErrors(List<ApiError> errors) {
		this.errors = errors;
	}

	public String getExternalTransationId() {
		return externalTransationId;
	}

	public void setExternalTransationId(String externalTransationId) {
		this.externalTransationId = externalTransationId;
	}

}
