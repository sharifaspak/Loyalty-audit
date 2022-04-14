package com.loyalty.marketplace.gifting.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberActivityResponse {

	// primary
	private String transactionRefId;
	// child
	private List<ChainedActivityEventDto> chainedActivities;
	// order details
	private ResponseOrderDetailsDto responseOrderDetails;

}
