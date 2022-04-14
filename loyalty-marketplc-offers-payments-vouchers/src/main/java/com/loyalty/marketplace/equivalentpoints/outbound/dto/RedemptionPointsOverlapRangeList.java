package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionPointsOverlapRangeList {
	
	private String overlapId;
	private Double pointOverlapStart;
	private Double pointOverlapEnd;
	private Double aedOverlapStart;
	private Double aedOverlapEnd;
	private Double coefficientA;
	private Double coefficientB;
	private String productItem;
	private String channel;
	private String partnerCode;
	private String updatedUser;
	private Date updatedDate;

}
