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
public class SubCategoryDto {

	String subCategoryId;
	@NotEmpty(message="{validation.SubCategoryDto.subCategoryNameEn.notEmpty.msg}")
	private String subCategoryNameEn;
	private String subCategoryNameAr;
	@NotEmpty(message="{validation.SubCategoryDto.parentCategory.notEmpty.msg}")
	private String parentCategory;

}
