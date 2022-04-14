package com.loyalty.marketplace.offers.decision.manager.inbound.dto;

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
@NoArgsConstructor
@ToString
public class PromotionalGiftDMRequestDto {
	
	private String channelId; 
	private String promotionalGiftId;

}
