package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class GiftInfo {
	
	@Field("IsGift")
	private String isGift;
	@Field("GiftChannels")
	private List<String> giftChannels;
	@Field("GiftSubCardTypes")
	private List<String> giftSubCardTypes;

}
