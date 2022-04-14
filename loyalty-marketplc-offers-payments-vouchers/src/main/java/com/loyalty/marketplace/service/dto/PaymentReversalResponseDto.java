package com.loyalty.marketplace.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class PaymentReversalResponseDto {
	
	private String responseCode;
	private String responseMsg;
	private List<PaymentReversalResultDto> result;
}
