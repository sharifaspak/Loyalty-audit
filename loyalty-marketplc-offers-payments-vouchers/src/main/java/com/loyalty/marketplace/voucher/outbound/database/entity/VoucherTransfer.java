package com.loyalty.marketplace.voucher.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class VoucherTransfer {

	@Field("IsTransfer")
	private boolean isTransfer;
	
	@Field("OldAccountNumber")
	private String oldAccountNumber;
	
	@Field("AgentName")
	private String agentName;
	
}
