package com.loyalty.marketplace.payment.inbound.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PromoCodeApply {

	private int status;
	private double cost;
	private int pointsValue;
	private int duration;
	private Date endDate;
	private String promoType;
}
