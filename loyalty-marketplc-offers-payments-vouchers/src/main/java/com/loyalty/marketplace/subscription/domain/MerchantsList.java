package com.loyalty.marketplace.subscription.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MerchantsList {
	private String merchantCode;
	private String merchantNameEn;
	private String merchantNameAr;
}
