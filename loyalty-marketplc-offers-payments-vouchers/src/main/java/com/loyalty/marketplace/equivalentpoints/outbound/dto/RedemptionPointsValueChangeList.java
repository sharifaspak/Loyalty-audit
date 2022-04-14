package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionPointsValueChangeList {

	private String paymentFactorRangeId;
	private Double factor;
	private Double amountStart;
	private Double amountEnd;
	private Double pointStart;
	private Double pointEnd;
	private String productItem;
	private String channel;
	private String partnerCode;
	private String updatedUser;
	private Date updatedDate;

}
