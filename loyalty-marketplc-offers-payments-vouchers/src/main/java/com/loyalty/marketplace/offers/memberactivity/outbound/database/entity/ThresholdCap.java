package com.loyalty.marketplace.offers.memberactivity.outbound.database.entity;

import com.loyalty.marketplace.offers.memberactivity.inbound.dto.UOMDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ThresholdCap {

	private String thresholdCapType;
	private long thresholdCapValue;
	private String thresholdCapTypeDescription;
	private long duration;
	private UOMDto uom;
	
}
