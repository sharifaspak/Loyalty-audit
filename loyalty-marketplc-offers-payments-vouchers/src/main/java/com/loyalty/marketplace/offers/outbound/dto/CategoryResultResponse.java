package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.offers.inbound.dto.CategoryDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
@JsonInclude(Include.NON_NULL)
public class CategoryResultResponse extends ResultResponse{
	
	private List<CategoryDto> categories;

	public CategoryResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	public List<CategoryDto> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDto> categories) {
		this.categories = categories;
	}
	
	@Override
	public String toString() {
		return "CategoryResultResponse [categories=" + categories + "]";
	}


}