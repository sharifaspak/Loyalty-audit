package com.loyalty.marketplace.gifting.service.dto;

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
