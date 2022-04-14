package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.inbound.dto.SubCategoryDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
public class SubCategoryResultResponse extends ResultResponse{
	
	private List<SubCategoryDto> subCategories;
	
	public SubCategoryResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	public List<SubCategoryDto> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SubCategoryDto> subCategories) {
		this.subCategories = subCategories;
	}

	@Override
	public String toString() {
		return "SubCategoryResultResponse [SubCategories=" + subCategories + "]";
	}

}
