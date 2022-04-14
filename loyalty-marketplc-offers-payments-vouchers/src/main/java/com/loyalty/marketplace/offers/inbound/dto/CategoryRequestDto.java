package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
public class CategoryRequestDto {
	
	@Valid
	@NotNull(message="{validation.CategoryRequestDto.categories.notNull}")
	private List<CategoryDto> categories;

}
