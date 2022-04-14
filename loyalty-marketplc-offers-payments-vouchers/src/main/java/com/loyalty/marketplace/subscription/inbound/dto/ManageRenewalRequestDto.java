package com.loyalty.marketplace.subscription.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ManageRenewalRequestDto {

	@NotNull(message="{validation.subscriptionId.notNull.msg}")
	@NotEmpty(message = "{validation.subscriptionId.notEmpty.msg}")
	private String subscriptionId;
	
	@NotNull(message="{validation.newPaymentMethod.notNull.msg}")
	@NotEmpty(message = "{validation.newPaymentMethod.notEmpty.msg}")
	private String renewPaymentMethod;
	
	private String masterEPGTransactionId ;
	private String epgTransactionID;
	private CCDetails ccDetails;
		
}
