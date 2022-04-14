package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VoucherGiftDetailsResult {

	private String isGift;
	
	private String giftId;
	
	private String transactionType;

	private String receiverConsumption;
	
	private String senderFirstName;
	
	private String senderLastName;
	
	private String senderAccountNumber;
	
	private String receiverFirstName;
	
	private String receiverLastName;
	
	private String receiverAccountNumber;
	
	private Date giftedDate;

}
