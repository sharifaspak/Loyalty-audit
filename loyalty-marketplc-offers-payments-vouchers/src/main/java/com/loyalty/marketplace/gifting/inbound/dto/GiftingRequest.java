package com.loyalty.marketplace.gifting.inbound.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GiftingRequest {

	private String giftType;

	private String senderAccountNumber;

	private String receiverAccountNumber;

	private String receiverFirstName;

	private String receiverLastName;

	private String receiverEmail;
	
	private String message;

	private String isScheduled;
	
	private Date scheduledDate;
	
	private Integer imageId;
	
	private Double fee;
		
	private PurchaseDetailsRequest purchaseDetails;
	
	private String voucherCode;
	
	private Double giftPoints;
	
	private Double giftGold;	

}
