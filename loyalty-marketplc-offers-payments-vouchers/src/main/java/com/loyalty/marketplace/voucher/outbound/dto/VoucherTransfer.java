package com.loyalty.marketplace.voucher.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
public class VoucherTransfer {
	private String transactionRefId;
	private String agentName;
	private String memberShipCode;
}
