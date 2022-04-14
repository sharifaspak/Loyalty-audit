package com.loyalty.marketplace.service.dto;


import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PaymentReversalListRequest {

	private List<SmilesPaymentReversalListRequestDto> payment;
}

