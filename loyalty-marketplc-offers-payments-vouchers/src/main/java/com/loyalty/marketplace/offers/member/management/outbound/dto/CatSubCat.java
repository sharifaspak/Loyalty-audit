package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CatSubCat {
	
	private String category;
	private List<String> subCategory;

}
