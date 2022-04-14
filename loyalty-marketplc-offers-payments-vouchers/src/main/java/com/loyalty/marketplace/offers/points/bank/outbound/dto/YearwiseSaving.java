package com.loyalty.marketplace.offers.points.bank.outbound.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document
@AllArgsConstructor
public class YearwiseSaving {

	@Field("Year")
	private String year;
	@Field("Total")
	private double total;
	@Field("TotalPurchaseCount")
	private Integer totalPurchaseCount;
	@Field("CategoryWiseSavings")
	private List<CategoryWiseSaving> categoryWiseSavings;
}
