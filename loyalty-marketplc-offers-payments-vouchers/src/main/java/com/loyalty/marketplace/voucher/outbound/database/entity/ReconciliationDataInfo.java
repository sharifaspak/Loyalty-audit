package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReconciliationDataInfo {

	@Field("PartnerReferenceNumber")
	private String partnerReferenceNumber;

	@Field("OrderReferenceNumber")
	private String orderReferenceNumber;

	@Field("VoucherCode")
	private String voucherCode;

	@Field("PartnerCode")
	private String partnerCode;

	@Field("LoyaltyTransaction_id")
	private String loyaltyTransactionId;

	@Field("PartnerTransaction_id")
	private String partnerTransactionId;

	@Field("VoucherAmount")
	private Double voucherAmount;

	@Field("TransactionDate")
	private Date transactionDate;

}
