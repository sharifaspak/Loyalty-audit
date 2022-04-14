package com.loyalty.marketplace.merchants.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class ListMerchantsDropdownResponse extends ResultResponse {

	List<MerchantDropdownResult> merchantsList;

	public ListMerchantsDropdownResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<MerchantDropdownResult> getMerchantsList() {
		return merchantsList;
	}

	public void setMerchantsList(List<MerchantDropdownResult> merchantsList) {
		this.merchantsList = merchantsList;
	}
	
}
