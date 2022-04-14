package com.loyalty.marketplace.voucher.inbound.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FreeVoucherAccountDto {
	private String accountNumber;
	private String membershipCode;
	private String loyaltyId;
}
