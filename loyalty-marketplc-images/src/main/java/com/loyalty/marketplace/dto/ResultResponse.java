package com.loyalty.marketplace.dto;

import java.util.ArrayList;
import java.util.List;

import com.loyalty.marketplace.constants.ImageCodes;

public class ResultResponse {

	CommonApiStatus apiStatus;
	Result result;

	public ResultResponse(String externalTransactionId) {
		if (null == externalTransactionId) {
			externalTransactionId = "";
		}
		apiStatus = new CommonApiStatus(externalTransactionId);
		result = new Result();
		setSuccessAPIResponse();
	}

	public CommonApiStatus getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(CommonApiStatus apiStatus) {
		this.apiStatus = apiStatus;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(String errorCode, String errorMessage) {

		this.result.setResponse(errorCode);
		this.result.setDescription(errorMessage);
	}

	public void setResult(Result result) {

		this.result = result;

	}

	public void setErrorAPIResponse(Integer errorCode, String errorMessage) {
		this.apiStatus.setStatusCode(ImageCodes.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(ImageCodes.STATUS_FAILURE.getMsg());
		Errors error = new Errors();
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		List<Errors> errorList = new ArrayList<>();
		errorList.add(error);
		this.apiStatus.setErrors(errorList);

	}

	public void setSuccessAPIResponse() {
		this.apiStatus.setStatusCode(ImageCodes.STATUS_SUCCESS.getIntId());
		this.apiStatus.setOverallStatus(ImageCodes.STATUS_SUCCESS.getMsg());
		this.apiStatus.setMessage("");

	}

	public void setBulkErrorAPIResponse(List<Errors> errorList) {
		this.apiStatus.setStatusCode(ImageCodes.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(ImageCodes.STATUS_FAILURE.getMsg());
		this.apiStatus.setErrors(errorList);

	}

	public void addErrorAPIResponse(Integer errorCode, String errorMessage) {
		this.apiStatus.setStatusCode(ImageCodes.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(ImageCodes.STATUS_FAILURE.getMsg());
		Errors error = new Errors();
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		this.apiStatus.getErrors().add(error);

	}

	@Override
	public String toString() {
		return "ResultResponse [apiStatus=" + apiStatus + ", result=" + result + "]";
	}

}
