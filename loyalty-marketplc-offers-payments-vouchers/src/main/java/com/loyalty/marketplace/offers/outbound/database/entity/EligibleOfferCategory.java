package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.outbound.database.entity.CategoryName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EligibleOfferCategory {
	
	@Field("Id")
	private String categoryId;

	@Field("Name")
	private CategoryName categoryName;
	
	@Field("ParentCategory")
	private EligibleOfferCategory parentCategory;

}
