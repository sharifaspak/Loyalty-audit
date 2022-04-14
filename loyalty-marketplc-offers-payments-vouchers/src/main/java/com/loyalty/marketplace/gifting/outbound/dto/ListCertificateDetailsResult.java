package com.loyalty.marketplace.gifting.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Name;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class ListCertificateDetailsResult {

	private String certificateId;
	
	private String transactionId;
	
	private String transactionType;
	
	private String partnerCode;
	
	private String merchantCode;
	
	private Name merchantName;
	
	private Double originalGoldBalance;
	
	private Double currentGoldBalance;
	
	private Date startDate;
	
	private Integer pointAmount;
	
	private Double spentAmount;
	
}
