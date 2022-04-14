package com.loyalty.marketplace.gifting.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Name;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class GoldCertificateTransaction {

	@Field("CertficateId")
	private String certificateId;
	
	@Field("TransactionId")
	private String transactionId;
	
	@Field("TransactionType")
	private String transactionType;
	
	@Field("PartnerCode")
	private String partnerCode;
	
	@Field("MerchantCode")
	private String merchantCode;
	
	@Field("MerchantName")
	private Name merchantName;
	
	@Field("OriginalGoldBalance")
	private Double originalGoldBalance;
	
	@Field("CurrentGoldBalance")
	private Double currentGoldBalance;
	
	@Field("StartDate")
	private Date startDate;
	
	@Field("PointAmount")
	private Integer pointAmount;
	
	@Field("SpentAmount")
	private Double spentAmount;
	
}
