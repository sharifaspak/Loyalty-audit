package com.loyalty.marketplace.offers.helper.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BirthdayDurationInfoDto {
	
	private Date dob;
	private Date startDate;
	private Date endDate;
	private Date lastYearDob;
	private Integer purchaseLimit;

}
