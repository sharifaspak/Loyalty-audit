package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMemberResponseDto {
	@JsonProperty("transactionId")
	private String transactionId;
	@JsonProperty("accountsInfo")
	private List<AccountInfoDto> accountsInfo;
	@JsonProperty("memberInfo")
	private MembershipDetailsDto memberInfo;

}
