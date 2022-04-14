package com.loyalty.marketplace.merchants.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class ListMerchantsStoreOfferResponse extends ResultResponse {

	MerchantStoreOfferResult listMerchants;

	private long totalRecords;
	
	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	public ListMerchantsStoreOfferResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public MerchantStoreOfferResult getListMerchants() {
		return listMerchants;
	}

	public void setListMerchants(MerchantStoreOfferResult listMerchants) {
		this.listMerchants = listMerchants;
	}

	
	@Override
	public String toString() {
		return "ListMerchantsStoreOfferResponse [listMerchants=" + listMerchants + "]";
	}
	
}
