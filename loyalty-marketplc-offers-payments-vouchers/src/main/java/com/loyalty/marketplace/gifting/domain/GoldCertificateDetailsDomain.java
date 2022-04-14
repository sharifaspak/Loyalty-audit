package com.loyalty.marketplace.gifting.domain;

import java.util.Date;

import com.loyalty.marketplace.domain.model.NameDomain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoldCertificateDetailsDomain {
	private String certificateId;
	private String transactionId;
	private String transactionType;
	private String partnerCode;
	private String merchantCode;
	private NameDomain merchantName;
	private Double originalGoldBalance;
	private Double currentGoldBalance;
	private Double spentAmount;
	private Integer pointAmount;
	private Date startDate;	
}
