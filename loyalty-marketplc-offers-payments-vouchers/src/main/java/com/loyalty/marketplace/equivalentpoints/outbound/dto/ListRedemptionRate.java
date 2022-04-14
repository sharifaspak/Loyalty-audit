package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListRedemptionRate {
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private RedemptionRate redemptionCalculatedValue;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private RedemptionRate accrualCalculatedValue;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<RedemptionPointsOverlapRangeList> redemptionPointsOverlapRangeList;
	
}