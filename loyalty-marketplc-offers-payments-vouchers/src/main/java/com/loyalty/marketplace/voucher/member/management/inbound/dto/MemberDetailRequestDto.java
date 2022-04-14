package com.loyalty.marketplace.voucher.member.management.inbound.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MemberDetailRequestDto {
	
	 private String accountNumber;
	 private String membershipCode;
	 private String loyaltyId;

}