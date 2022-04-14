package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class SubscriptionRenewalResultResponse extends ResultResponse {
	
	List<SubscriptionRenewalResponseDto> subscriptionRenewalList;
		
	public SubscriptionRenewalResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

}
