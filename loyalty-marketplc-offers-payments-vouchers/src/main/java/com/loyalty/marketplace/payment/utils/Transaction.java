package com.loyalty.marketplace.payment.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	
	private String ResponseCode;
	private String ResponseClass;
	private String ResponseDescription;
	private String ResponseClassDescription;
	private String Language;
	private String TransactionID;
	private String ApprovalCode;
	private String Account;
	private Balance Balance;
	private String OrderID;
	private Amount Amount;
	private Fees Fees;
	private String CardNumber;
	private Payer Payer;
	private String CardToken;
	private String CardBrand;
	private String CardType;
	private String UniqueID;
	
}