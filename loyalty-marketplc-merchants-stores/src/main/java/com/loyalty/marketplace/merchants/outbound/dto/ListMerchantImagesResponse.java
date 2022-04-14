package com.loyalty.marketplace.merchants.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.outbound.dto.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ListMerchantImagesResponse {

	CommonApiStatus apiStatus;
	
	Result result;
	
	List<MerchantImagesResult> listMerchantImages;

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

	public List<MerchantImagesResult> getListMerchantImages() {
		return listMerchantImages;
	}

	public void setListMerchantImages(List<MerchantImagesResult> listMerchantImages) {
		this.listMerchantImages = listMerchantImages;
	}

	@Override
	public String toString() {
		return "ListMerchantImagesResponse [apiStatus=" + apiStatus + ", result=" + result + ", listMerchantImages="
				+ listMerchantImages + "]";
	}

	
}
