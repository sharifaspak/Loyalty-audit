package com.loyalty.marketplace.merchants.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class ListMerchantsResponse extends ResultResponse{

	List<MerchantResult> listMerchants;

	private long totalRecords;
	
	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	public ListMerchantsResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<MerchantResult> getListMerchants() {
		return listMerchants;
	}

	public void setListMerchants(List<MerchantResult> listMerchants) {
		this.listMerchants = listMerchants;
	}

	@Override
	public String toString() {
		return "ListMerchantsResponse [listMerchants=" + listMerchants + "]";
	}

	
}
