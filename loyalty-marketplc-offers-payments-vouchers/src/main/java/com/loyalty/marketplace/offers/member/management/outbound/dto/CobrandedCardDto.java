package com.loyalty.marketplace.offers.member.management.outbound.dto;

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
public class CobrandedCardDto {

	private String partnerCode;
	private String bankId;
	private String status;
	private String cardType;
}
