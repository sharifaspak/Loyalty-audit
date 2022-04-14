package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionRequestDto {
	
	  @NotEmpty(message = "{validation.TransactionRequestDto.fromDate.notEmpty.msg}")
	  private String fromDate;
	  @NotEmpty(message = "{validation.TransactionRequestDto.toDate.notEmpty.msg}")
	  private String toDate;
	  private String accountNumber;
	  private String membershipCode;
	  private String transactionType;
	  private boolean includeLinkedAccounts;
 	  private List<String> accountNumberList;
	  private boolean primaryAccountInList;
	  private String offerType;
	  private boolean channelCheck;
}
