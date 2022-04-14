package com.loyalty.marketplace.subscription.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethodDetailsDomain {
	
	private String paymentMethodId;
	private String paymentMethod;	
	private String masterEPGTransactionId;	
	private String epgTransactionID;	
	private CCDetailsDomain ccDetailsdomain;	
	private String status;	
	private Date inactiveDate;
}
