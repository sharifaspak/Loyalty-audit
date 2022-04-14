package com.loyalty.marketplace.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberActivityResponse {

	private String transactionRefId;

	private ResponseOrderDetailsDto responseOrderDetailsDto;

}
