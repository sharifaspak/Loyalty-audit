package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesInterestDto {
	private String interestId;	
	private String interestName;	
	private String interestNameAr;	
	private boolean interestSelected;
	private String interestImageUrl;	
	private CategoryDto category;
	private CategoryDto subCategory;
}
