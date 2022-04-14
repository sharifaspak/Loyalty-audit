package com.loyalty.marketplace.voucher.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReconciliationInfo {
	
	@Field("NoofCountTransaction")
	private Integer noofCountTransaction;

	@Field("TotalAmount")
	private Double totalAmount;

}
