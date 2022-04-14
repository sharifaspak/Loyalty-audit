package com.loyalty.marketplace.voucher.utils;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class OfferDetails {
	
	@Field("Title")
	private OfferTitle offerTitle;
	
}
