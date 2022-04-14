package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.interest.outbound.dto.CategoriesInterestDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class SubscriptionInfoResponseDto {
	
	private String accountNumber;
	private SubscribedSegmentResponseDto subscribed;
	private List<BenefitsResponseDto> benefits;
	private List<CategoriesInterestDto> interestList;

}
