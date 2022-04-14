package com.loyalty.marketplace.offers.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GiftInfoDomain {
	
	private String isGift;
	private List<String> giftChannels;
	private List<String> giftSubCardTypes;

}
