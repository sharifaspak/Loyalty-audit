package com.loyalty.marketplace.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CategoryName {

	@Field("English")
	private String categoryNameEn;

	@Field("Arabic")
	private String categoryNameAr;

}
