package com.loyalty.marketplace.gifting.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ListGoldTransactionResult {

	private String id;
	
	private String accountNumber;
	
	private String membershipCode;
	
	private Double totalGoldBalance;
	
	private Integer totalPointAmount;
	
	private Double totalSpentAmount;
	
	private List<ListCertificateDetailsResult> certificateDetails;
	
}
