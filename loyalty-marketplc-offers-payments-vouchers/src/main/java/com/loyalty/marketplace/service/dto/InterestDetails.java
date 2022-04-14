package com.loyalty.marketplace.service.dto;

import java.util.List;

import com.loyalty.marketplace.interest.outbound.dto.CategoriesInterestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestDetails {
	private List<CategoriesInterestDto> interestList;
}
