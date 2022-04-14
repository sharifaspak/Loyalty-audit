package com.loyalty.marketplace.subscription.outbound.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class HbFileDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accountNumber;
	private String membershipCode;
	private String externalReferenceNumber;
	private String partnerCode;
	private String invoiceRefNumber;
	private String subscriptionCatalogId;
	private String status;
	private String errorMessage;
}
