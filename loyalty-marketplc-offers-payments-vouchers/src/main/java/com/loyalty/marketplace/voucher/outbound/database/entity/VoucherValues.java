package com.loyalty.marketplace.voucher.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class VoucherValues {
	
	@Field("PointsValue")
	private long pointsValue;
	
	@Field("Cost")
	private double cost;
}
