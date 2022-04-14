package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loyalty.marketplace.constants.MarketPlaceCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ListAccountWithActiveVouchersResponse {

	CommonApiStatus apiStatus;
	ListAccountWithActiveVouchersResult result;
	
	public ListAccountWithActiveVouchersResponse(String externalTransactionId) {
		if (null == externalTransactionId) {
			externalTransactionId="";
		}
		apiStatus = new CommonApiStatus(externalTransactionId);
		result = new ListAccountWithActiveVouchersResult();
		setSuccessAPIResponse();
	}
	
	public void setErrorAPIResponse(Integer errorCode, String errorMessage) {
		this.apiStatus.setStatusCode(MarketPlaceCode.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(MarketPlaceCode.STATUS_FAILURE.getMsg());
		Errors error = new Errors();
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		List<Errors> errorList = new ArrayList<>();
		errorList.add(error);
		this.apiStatus.setErrors(errorList);
	}

	public void setSuccessAPIResponse() {
		this.apiStatus.setStatusCode(MarketPlaceCode.STATUS_SUCCESS.getIntId());
		this.apiStatus.setOverallStatus(MarketPlaceCode.STATUS_SUCCESS.getMsg());
		this.apiStatus.setMessage("");
	}

	public void setBulkErrorAPIResponse(List<Errors> errorList) {
		this.apiStatus.setStatusCode(MarketPlaceCode.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(MarketPlaceCode.STATUS_FAILURE.getMsg());
		this.apiStatus.setErrors(errorList);
	}

	public void addErrorAPIResponse(Integer errorCode, String errorMessage) {
		this.apiStatus.setStatusCode(MarketPlaceCode.STATUS_FAILURE.getIntId());
		this.apiStatus.setOverallStatus(MarketPlaceCode.STATUS_FAILURE.getMsg());
		Errors error = new Errors();
		error.setCode(errorCode);
		error.setMessage(errorMessage);
		this.apiStatus.getErrors().add(error);
	}
}
