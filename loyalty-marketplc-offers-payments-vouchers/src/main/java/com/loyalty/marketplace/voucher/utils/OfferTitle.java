package com.loyalty.marketplace.voucher.utils;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OfferTitle {
	
	@Field("English")
	private String offerTitleEn;
	
	@Field("Arabic")
	private String offerTitleAr;

}
