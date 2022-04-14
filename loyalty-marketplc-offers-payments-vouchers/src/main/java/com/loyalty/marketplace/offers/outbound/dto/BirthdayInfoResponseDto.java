package com.loyalty.marketplace.offers.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BirthdayInfoResponseDto {

	private Date dob;
	private String titleEn;
	private String titleAr;
	private String subTitleEn;
	private String subTitleAr;
	private String descriptionEn;
	private String descriptionAr;
	private String iconTextEn;
	private String iconTextAr;
	private String weekIconEn;
	private String weekIconAr;
	private Integer purchaseLimit;
	private Integer thresholdPlusValue;
	private Integer thresholdMinusValue;
	private Integer displayLimit;
	private String firstName;
	private String lastName;
	
	
}