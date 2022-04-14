package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BirthdayAccountsResponse {

	private List<BirthdayAccountsDto> accountList;
}
