package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CategoryDto {
	
	String categoryId;
	@NotEmpty(message = "{validation.CategoryDto.categoryNameEn.notEmpty.msg}")
	private String categoryNameEn;
	private String categoryNameAr;

}
