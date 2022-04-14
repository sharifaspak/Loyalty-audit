package com.loyalty.marketplace.equivalentpoints.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquivalentPointsDto {
	
	@NotEmpty(message = "{validation.equivalent.points.null.operationType}")
	private String operationType;
	private String accountNumber;
	private Double amount;
	private Double point;
	private String activityCode;
	private String partnerCode;
	private String channel;
	private String offerType; 

}
