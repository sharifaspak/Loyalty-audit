package com.loyalty.marketplace.stores.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.Result;
import com.loyalty.marketplace.stores.inbound.dto.StoreDto;

public class StoreResultResponse {
	
	private CommonApiStatus apiStatus;
	
	private Result result;
	
	private List<StoreDto> partners;

	public CommonApiStatus getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(CommonApiStatus apiStatus) {
		this.apiStatus = apiStatus;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public List<StoreDto> getPartners() {
		return partners;
	}

	public void setPartners(List<StoreDto> partners) {
		this.partners = partners;
	}

	@Override
	public String toString() {
		return "ViewPartnerResultResponse [apiStatus=" + apiStatus + ", result=" + result + ", partners=" + partners
				+ "]";
	}

}
