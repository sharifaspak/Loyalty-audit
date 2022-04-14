package com.loyalty.marketplace.gifting.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BirthdayReminderStatusDto {

	@NotEmpty(message = "{validation.birthdayReminderStatusDto.senderAccountNumber.notEmpty.msg}")
	private String senderAccountNumber;
	
	@NotEmpty(message = "{validation.birthdayReminderStatusDto.receiverAccountNumber.notEmpty.msg}")
	private String receiverAccountNumber;
	
	@NotEmpty(message = "{validation.birthdayReminderStatusDto.status.notEmpty.msg}")
	private String status;
	
}
