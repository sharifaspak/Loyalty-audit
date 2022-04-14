package com.loyalty.marketplace.gifting.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.gifting.outbound.database.entity.MemberInfo;
import com.loyalty.marketplace.voucher.domain.PurchaseDetailsDomain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class GiftingHistoryResult {

	private String programCode;
	private String id;
	private String giftType;
	private String voucherCode;
	private MemberInfo senderInfo;
	private MemberInfo receiverInfo;
	private Integer pointsGifted;
	private Double goldGifted;	
	private Integer imageId;
	private String imageName;
	private String imageCategory;
	private String imageUrl;
	private String message;
	private String isScheduled;
	private Date scheduledDate;
	private String status;
	private String statusReason;
	private String transactionNo;
	private Date transactionDate;	
	private String extRefNo;
	private String epgTransactionId;
	private Double spentAmount;
	private Integer spentPoints;
	private Double purchaseAmount;
	private PurchaseDetailsDomain purchaseDetails;
	private String receiverConsumption;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;

}
