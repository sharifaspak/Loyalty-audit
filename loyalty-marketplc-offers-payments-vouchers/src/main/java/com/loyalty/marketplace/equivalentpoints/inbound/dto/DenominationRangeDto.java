package com.loyalty.marketplace.equivalentpoints.inbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DenominationRangeDto {

	private Double lowValue;
	private Double highValue;
	private Double pointStart;
	private Double pointEnd;
	private Double coefficientA;
	private Double coefficientB;
	
}
