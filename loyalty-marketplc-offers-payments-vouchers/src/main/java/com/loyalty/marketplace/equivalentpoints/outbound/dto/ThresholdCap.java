package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import com.loyalty.marketplace.offers.memberactivity.inbound.dto.UOMDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ThresholdCap {

	private String thresholdCapType;
	private Long thresholdCapValue;
	private Long thresholdPointsCapValue;
	private String thresholdCapTypeDescription;
	private Long duration;
	private UOMDto uom;

}
