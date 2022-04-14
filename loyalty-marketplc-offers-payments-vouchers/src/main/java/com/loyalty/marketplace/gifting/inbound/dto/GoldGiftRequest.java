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
public class GoldGiftRequest {

	@NotEmpty(message = "{validation.giftingGoldDto.senderAccountNumber.notEmpty.msg}")
	private String senderAccountNumber;

	@NotEmpty(message = "{validation.giftingGoldDto.receiverAccountNumber.notEmpty.msg}")
	private String receiverAccountNumber;

	@NotEmpty(message = "{validation.giftingDto.receiverFirstName.notEmpty.msg}")
	private String receiverFirstName;

	private String receiverLastName;
	
	@Email(message = "{validation.voucherGiftRequestDto.email.format.msg}")	
	private String receiverEmail;
	
	@NotEmpty(message = "{validation.giftingGoldDto.message.notEmpty.msg}")
	private String message;

	private PurchaseDetailsRequest purchaseDetails;
	
	private String isScheduled;
		
	private Date scheduledDate;
	
	@NotNull(message = "{validation.giftingGoldDto.imageId.notEmpty.msg}")
	private Integer imageId;
	
	@NotNull(message = "{validation.giftingGoldDto.giftGold.notEmpty.msg}")
	private Double giftGold;	
	
	@NotNull(message = "{validation.giftingGoldDto.fee.notEmpty.msg}")
	private Double fee;
}
