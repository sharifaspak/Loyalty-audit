package com.loyalty.marketplace.interest.outbound.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loyalty.marketplace.interest.constants.InterestErrorCodes;
import com.loyalty.marketplace.interest.constants.InterestSuccessCodes;
import com.loyalty.marketplace.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.Errors;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InterestResponseResult {

	private CommonApiStatus apiStatus;

	private Object result;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public InterestResponseResult(String externalTransactionId) {
		if (null == externalTransactionId) {
			externalTransactionId = "";
		}
		apiStatus = new CommonApiStatus(externalTransactionId);
		result = new Object();
		setSuccessAPIResponse();
	}

	public void setSuccessAPIResponse() {
		this.apiStatus.setOverallStatus(InterestSuccessCodes.STATUS_SUCCESS.getMsg());
		this.apiStatus.setStatusCode(InterestSuccessCodes.STATUS_SUCCESS.getIntId());
		this.apiStatus.setMessage("");

	}

	public CommonApiStatus getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(CommonApiStatus apiStatus) {
		this.apiStatus = apiStatus;
	}

	public void setErrorAPIResponse(Integer errorCode, String errorMessage) {
		this.apiStatus.setStatusCode(InterestErrorCodes.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(InterestErrorCodes.STATUS_FAILURE.getMsg());
		this.apiStatus.setMessage(errorMessage);
		Errors error = new Errors();
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		List<Errors> errorList = new ArrayList<>();
		errorList.add(error);
		this.apiStatus.setErrors(errorList);

	}

	public void setBulkErrorAPIResponse(List<Errors> errorList) {
		this.apiStatus.setStatusCode(InterestErrorCodes.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(InterestErrorCodes.STATUS_FAILURE.getMsg());
		this.apiStatus.setErrors(errorList);

	}

	public void addErrorAPIResponse(Integer errorCode, String errorMessage) {
		this.apiStatus.setStatusCode(InterestErrorCodes.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(InterestErrorCodes.STATUS_FAILURE.getMsg());
		Errors error = new Errors();
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		this.apiStatus.getErrors().add(error);

	}

}
