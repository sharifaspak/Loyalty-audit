package com.loyalty.marketplace.payment.inbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdditionalParamsDto {

	private String agentActivityReason;
	private String referenceTxnId;
	private String agentNotes;

}
