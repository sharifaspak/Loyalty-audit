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
public class ListGiftingHistoryResult {
	
	private String id;
	
	private String programCode;
	
	private String giftType;
	
	private String type;
	
	private String accountNumber;
	
	private String membershipCode;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private List<TransactionResponse> transactions;
	
}
