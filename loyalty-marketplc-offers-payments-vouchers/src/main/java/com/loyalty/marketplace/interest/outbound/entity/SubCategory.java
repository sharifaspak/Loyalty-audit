package com.loyalty.marketplace.interest.outbound.entity;

import com.loyalty.marketplace.outbound.database.entity.CategoryName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {
	
	private String subCategoryId;
	private String parentCategory;
	private CategoryName categoryName;
}
