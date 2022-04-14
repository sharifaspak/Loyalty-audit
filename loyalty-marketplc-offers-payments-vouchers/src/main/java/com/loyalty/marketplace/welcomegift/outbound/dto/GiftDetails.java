package com.loyalty.marketplace.welcomegift.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class GiftDetails  extends ResultResponse{
	public GiftDetails(String externalTransactionId) {
		super(externalTransactionId);
	}
	List<WelcomeGiftDetails> welcomeGiftDetails;
}
