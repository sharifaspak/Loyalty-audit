package com.loyalty.marketplace.gifting.inbound.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PointsGiftRequest {

	@NotEmpty(message = "{validation.giftingPointsDto.senderAccountNumber.notEmpty.msg}")
	private String senderAccountNumber;

	@NotEmpty(message = "{validation.giftingPointsDto.receiverAccountNumber.notEmpty.msg}")
	private String receiverAccountNumber;

	@NotEmpty(message = "{validation.giftingDto.receiverFirstName.notEmpty.msg}")
	private String receiverFirstName;

	private String receiverLastName;

	@Email(message = "{validation.voucherGiftRequestDto.email.format.msg}")	
	private String receiverEmail;
	
	@NotEmpty(message = "{validation.giftingPointsDto.message.notEmpty.msg}")
	private String message;

	private String isScheduled;
	
	private Date scheduledDate;
	
	@NotNull(message = "{validation.giftingPointsDto.imageId.notEmpty.msg}")
	private Integer imageId;
	
	@NotNull(message = "{validation.giftingPointsDto.giftPoints.notEmpty.msg}")
	private Double giftPoints;
	
	private PurchaseDetailsRequest purchaseDetails;
	
	@NotNull(message = "{validation.giftingPointsDto.fee.notEmpty.msg}")
	private Double fee;
	
	private String senderMembershipCode;
	
	private String receiverMembershipCode;
	
	private String senderTransactionRefId;
	
	private String receiverTransactionRefId;
	
}
