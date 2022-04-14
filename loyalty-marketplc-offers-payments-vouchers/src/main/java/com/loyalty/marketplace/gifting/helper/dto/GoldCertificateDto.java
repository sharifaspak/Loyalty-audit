package com.loyalty.marketplace.gifting.helper.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GoldCertificateDto {

	private String certificateId;
	private String transactionId;
	private String transactionType;
	private String accountNumber;
	private String membershipCode;
	private Double balance;
	private Double aedAmount;
	private Integer pointsValue;
	private String partnerCode;
	private String merchantCode;
	private String merchantNameEn;
	private String merchantNameAr;
	private Date startDate;
	
}
