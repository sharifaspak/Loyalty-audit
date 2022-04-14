package com.loyalty.marketplace.voucher.utils;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Merchant {

	@Field("Name")
	private MerchantName merchantName;

	@Field("MerchantCode")
	private String merchantCode;
	
}