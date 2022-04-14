package com.loyalty.marketplace.gifting.inbound.dto;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BirthdayReminderDto {

	@NotEmpty(message = "{validation.birthdayReminderDto.accountNumber.notEmpty.msg}")
	private String accountNumber;
	
	@NotNull(message = "{validation.birthdayReminderDto.dob.notEmpty.msg}")
	private Date dob;
	
	@Valid
	@NotEmpty(message = "{validation.birthdayReminderDto.reminderList.notEmpty.msg}")
	private List<BirthdayReminderListDto> reminderList;
	
}
