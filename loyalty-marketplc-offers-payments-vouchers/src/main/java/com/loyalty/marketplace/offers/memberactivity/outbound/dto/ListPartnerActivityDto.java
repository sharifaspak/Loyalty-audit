package com.loyalty.marketplace.offers.memberactivity.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.memberactivity.inbound.dto.PartnerActivityDto;

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
public class ListPartnerActivityDto {

	private List<PartnerActivityDto> listPartnerActivity;

}
