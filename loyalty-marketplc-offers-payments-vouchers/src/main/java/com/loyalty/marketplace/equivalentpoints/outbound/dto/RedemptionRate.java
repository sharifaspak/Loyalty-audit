package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionRate {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("calculatedValue")
	private Double equivalentPoint;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("conversionRate")
	private Double rate;

}
