package com.loyalty.marketplace.equivalentpoints.inbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRateDto {

	private String partnerCode;
	private String channel;
	private String productItem;
	private DenominationRangeDto denominationRange;
	private Double valuePerPoint;

}
