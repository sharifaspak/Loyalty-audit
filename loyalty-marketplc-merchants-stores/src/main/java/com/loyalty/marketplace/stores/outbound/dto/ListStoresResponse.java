package com.loyalty.marketplace.stores.outbound.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

@JsonInclude(Include.NON_NULL)
public class ListStoresResponse extends ResultResponse {
	
	List<StoreResult> stores = new ArrayList<>();
	
	private long totalRecords;
	
	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public ListStoresResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<StoreResult> getStores() {
		return stores;
	}

	public void setStores(List<StoreResult> stores) {
		this.stores = stores;
	}

	@Override
	public String toString() {
		return "ListStoresResponse [stores=" + stores + "]";
	}
	
}
