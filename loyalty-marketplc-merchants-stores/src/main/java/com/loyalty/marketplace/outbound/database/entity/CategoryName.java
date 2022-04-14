package com.loyalty.marketplace.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryName {

	@Field("English")
	private String categoryNameEn;

	@Field("Arabic")
	private String categoryNameAr;

}
