package com.loyalty.marketplace.gifting.inbound.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class VoucherGiftRequest {
	
	@NotEmpty(message = "{validation.voucherGiftRequestDto.senderAccountNumber.notEmpty.msg}")
	private String senderAccountNumber;
	
	@NotEmpty(message = "{validation.voucherGiftRequestDto.receiverAccountNumber.notEmpty.msg}")
	private String receiverAccountNumber;
	
	@NotEmpty(message = "{validation.voucherGiftRequestDto.receiverFirstName.notEmpty.msg}")
	private String receiverFirstName;
	
	private String receiverLastName;
	
	@Email(message = "{validation.voucherGiftRequestDto.email.format.msg}")	
	private String receiverEmail;
	
	@NotEmpty(message = "{validation.voucherGiftRequestDto.voucherCode.notEmpty.msg}")
	private String voucherCode;
	
	@NotNull(message = "{validation.voucherGiftRequestDto.imageFileName.notEmpty.msg}")
	private Integer imageId;
	
	@NotEmpty(message = "{validation.voucherGiftRequestDto.message.notEmpty.msg}")
	private String message;
	
	private String isScheduled;
		
	private Date scheduledDate;
	
	private PurchaseDetailsRequest purchaseDetails;
	
}
