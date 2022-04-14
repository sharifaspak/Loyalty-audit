package com.loyalty.marketplace.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargesListDto {

	 private String msisdn;
     private Double rechargesAmount;
     private String accountType;
     private String offerId;
}
