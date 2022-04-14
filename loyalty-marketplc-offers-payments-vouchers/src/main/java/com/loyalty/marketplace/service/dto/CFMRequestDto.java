package com.loyalty.marketplace.service.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CFMRequestDto {

	private String accountNumber;
	private String paymentType;
	private String discount;
	private Date customerDOB;
	private String nationality;
	private String language;
	private String email;
	private String numberType;
	private String partnerName;
	private String subCatagory;
}
