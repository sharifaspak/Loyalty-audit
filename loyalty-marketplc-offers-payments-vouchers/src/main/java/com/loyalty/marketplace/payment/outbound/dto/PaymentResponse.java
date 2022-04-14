package com.loyalty.marketplace.payment.outbound.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PaymentResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String paymentStatus;
	private String transactionNo;
	private String failedreason;
	private int errorCode;
	//for Points transaction
	private String extRefNo;
	//for Credit card transaction
	private String epgTransactionId;
	private Integer pointsValue;
	private List<String> voucherCode;
 }
