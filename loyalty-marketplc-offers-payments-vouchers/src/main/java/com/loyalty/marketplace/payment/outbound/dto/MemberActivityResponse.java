package com.loyalty.marketplace.payment.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberActivityResponse {
	private Integer membershipPoints;
	private Integer accountPoints;
	@JsonIgnore
	private Integer pointsValue;
	//primary
	private String transactionRefId;
	//child
	private List<ChainedActivityEventDto> chainedActivities;
	//order details
	private ResponseOrderDetailsDto responseOrderDetailsDto;

}
