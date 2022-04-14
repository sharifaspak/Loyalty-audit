package com.loyalty.marketplace.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillsAndRechargesDto {

	private List<BillsListDto> billsList;
	private List<RechargesListDto> rechargesList; 
}
