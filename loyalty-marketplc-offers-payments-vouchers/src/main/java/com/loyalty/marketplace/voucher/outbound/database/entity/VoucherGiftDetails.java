package com.loyalty.marketplace.voucher.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VoucherGiftDetails {

	@Field("GiftId")
	private String giftId;
	
	@Field("GiftedAccountNumber")
	private String giftedAccountNumber;
	
	@Field("IsGift")
	private String isGift;
	
}
