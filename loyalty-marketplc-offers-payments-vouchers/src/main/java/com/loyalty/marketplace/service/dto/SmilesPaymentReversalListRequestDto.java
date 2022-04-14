package com.loyalty.marketplace.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SmilesPaymentReversalListRequestDto {

	private String transactionId;
	private String transactionDescription;
	private String msisdn;
	private String lang;
	private Object selectedPaymentItem;
	private Object selectedPaymentOption;
	private String offerId;
	private String voucherDenomination;
	private String accountType;
	private Integer quantity;
	private CreditCardDetailDto creditCardDetails;
}
