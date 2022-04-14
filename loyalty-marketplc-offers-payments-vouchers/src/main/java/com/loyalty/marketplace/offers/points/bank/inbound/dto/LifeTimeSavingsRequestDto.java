package com.loyalty.marketplace.offers.points.bank.inbound.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LifeTimeSavingsRequestDto {

		  private String loyaltyId;
		  private String accountNumber;
		  private Date fromDate;
		  private Date toDate;
		  private String duration;
		  private boolean categoryInfo;
		  private List<String> year;

}
