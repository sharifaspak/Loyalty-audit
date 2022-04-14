package com.loyalty.marketplace.gifting.outbound.dto;

import java.util.Date;
import java.util.List;

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
public class ListSpecificGiftingHistoryResult {

	private String id;
	
	private String programCode;
	
	private String giftType;
	
	private MemberInfoResponse senderInfo;
	
	private MemberInfoResponse receiverInfo;

	private String receiverConsumption;
	
	private String voucherCode;
	
	private PointsGiftTransactionResponse pointsGift;

	private List<GoldCertificateTransactionResponse> goldGift;	
	
	private Integer imageId;
	
	private String imageUrl;
	
	private String message;
	
	private Date scheduledDate;
	
	private PurchaseDetailsResponse purchaseDetails;

	private Date createdDate;
	
	private String createdUser;
	
	private Date updatedDate;
	
	private String updatedUser;
	
}
