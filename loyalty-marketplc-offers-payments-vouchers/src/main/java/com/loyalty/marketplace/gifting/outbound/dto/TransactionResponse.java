package com.loyalty.marketplace.gifting.outbound.dto;

import java.util.Date;
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
public class TransactionResponse {

	private String id;
	
	private String transactionType;
	
	private MemberInfoResponse recipientInfo;
	
	private String voucherCode;
	
	private PointsGiftTransactionResponse pointsGift;
	
	private List<GoldCertificateTransactionResponse> goldGift;
	
	private Integer imageId;
	
	private String imageUrl;
	
	private String message;
	
	private Date scheduledDate;
	
	private String receiverConsumption;
	
	private PurchaseDetailsResponse purchaseDetails;
	
}
