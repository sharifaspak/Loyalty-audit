package com.loyalty.marketplace.voucher.member.management.outbound.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class GetListMemberResponseDto {
	
	private String accountNumber;
	private String accountStatus;
	private String membershipCode;
	private boolean isCoBrandedCard;
	private String tier;
	private Date dob;
	private String language; 
	private String uilanguage;
	private String firstName;
    private String lastName;
	
	public GetListMemberResponseDto(String accountNumber, String membershipCode, Date dob) {
		super();
		this.accountNumber = accountNumber;
		this.membershipCode = membershipCode;
		this.dob = dob;
	}

}
