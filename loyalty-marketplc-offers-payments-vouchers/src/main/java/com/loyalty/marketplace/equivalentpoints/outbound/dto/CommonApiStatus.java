package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loyalty.marketplace.equivalentpoints.constant.ApplicationConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommonApiStatus {
	private ApiStatus apiStatus;
	private Object result;

	public CommonApiStatus(String externalTransactionId) {
		if (null == externalTransactionId) {
			externalTransactionId = "";
		}
		apiStatus = new ApiStatus(externalTransactionId);
	}

	public void setBulkErrorAPIResponse(List<ApiError> errorList) {
		this.apiStatus.setStatusCode(1);
		this.apiStatus.setOverallStatus(ApplicationConstants.STATUS_ERROR);
		this.apiStatus.setMessage(ApplicationConstants.ERROR_MESSAGE_400);
		this.apiStatus.setErrors(errorList);
	}

	public void addErrorAPIResponse(Integer errorCode, String errorMessage) {
		this.apiStatus.setStatusCode(1);
		this.apiStatus.setOverallStatus(ApplicationConstants.STATUS_ERROR);
		ApiError error = new ApiError();
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		this.apiStatus.getErrors().add(error);

	}
}
