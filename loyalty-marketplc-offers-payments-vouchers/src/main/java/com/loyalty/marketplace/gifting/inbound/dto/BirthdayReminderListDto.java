package com.loyalty.marketplace.gifting.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BirthdayReminderListDto {

	@NotEmpty(message = "{validation.birthdayReminderListDto.accountNumber.notEmpty.msg}")
	private String accountNumber;
	
	@NotEmpty(message = "{validation.birthdayReminderListDto.remindPrior.notEmpty.msg}")
	private String remindPrior;
	
}
