package com.loyalty.marketplace.payment.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChainedActivityEventDto {
	public ChainedActivityEventDto(String activityCode, int chainedActivitysequence, String transactionRefId) {
		this.activityCode=activityCode;
		this.sequence=chainedActivitysequence;
		this.transactionRefId=transactionRefId;
	}
	private String activityCode;
	private Integer sequence;
	private String transactionRefId;

}
