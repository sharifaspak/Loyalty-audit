package com.loyalty.marketplace.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMemberResponseDto {
	
	private String transactionId;
	private List<AccountInfoDto> accountsInfo;
	private MembershipDetailsDto memberInfo;

}
