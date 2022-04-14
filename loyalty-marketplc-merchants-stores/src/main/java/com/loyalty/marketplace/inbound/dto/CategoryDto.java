package com.loyalty.marketplace.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {
	
	String categoryId;
	@NotEmpty(message = "{validation.CategoryDto.categoryNameEn.notEmpty.msg}")
	private String categoryNameEn;
	@NotEmpty(message = "{validation.CategoryDto.categoryNameAr.notEmpty.msg}")
	private String categoryNameAr;

}
