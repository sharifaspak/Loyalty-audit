package com.loyalty.marketplace.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillsListDto {

	private String msisdn;
	private Double billAmount;
	private String accountType;
	private String offerId;
}
