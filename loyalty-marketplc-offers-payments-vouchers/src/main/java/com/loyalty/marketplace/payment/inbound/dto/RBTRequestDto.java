package com.loyalty.marketplace.payment.inbound.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RBTRequestDto {
	private String serviceId; 
	private String packName;
	private String msisdn;
}
