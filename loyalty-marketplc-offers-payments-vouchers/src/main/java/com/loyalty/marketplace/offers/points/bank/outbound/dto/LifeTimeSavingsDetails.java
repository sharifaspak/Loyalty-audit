package com.loyalty.marketplace.offers.points.bank.outbound.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeTimeSavingsDetails {

	private String accountNumber;
	private String membershipCode;
	private OverallSavings overallSavings;
	private List<YearwiseSaving> yearWiseSavings;  

}
