package com.loyalty.marketplace.offers.points.bank.outbound.dto;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document
@AllArgsConstructor
public class CategoryWiseSaving {

	@Field("CategoryCode")
	private String categoryCode;
	@Field("CategoryDescription")
	private String categoryDescription;
	@Field("Savings")
	private Double savings;
	@Field("TotalPurchaseCount")
	private Integer totalPurchaseCount;
	
}
