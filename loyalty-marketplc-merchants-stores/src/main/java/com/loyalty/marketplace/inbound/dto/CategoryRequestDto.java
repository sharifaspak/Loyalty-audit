package com.loyalty.marketplace.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRequestDto {
	
	@Valid
	@NotEmpty(message="{validation.CategoryRequestDto.categories.notEmpty}")
	private List<CategoryDto> categories;

}
