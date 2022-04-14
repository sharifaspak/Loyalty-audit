package com.loyalty.marketplace.voucher.domain;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReconciliationDataInfoDomain {

	@Autowired
	ModelMapper modelMapper;

	private String partnerReferenceNumber;
	private String orderReferenceNumber;
	private String voucherCode;
	private String partnerCode;
	private String loyaltyTransactionId;
	private String partnerTransactionId;
	private Double voucherAmount;
	private Date transactionDate;

	public String getPartnerReferenceNumber() {
		return partnerReferenceNumber;
	}

	public String getOrderReferenceNumber() {
		return orderReferenceNumber;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public String getLoyaltyTransactionId() {
		return loyaltyTransactionId;
	}

	public String getPartnerTransactionId() {
		return partnerTransactionId;
	}

	public Double getVoucherAmount() {
		return voucherAmount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public ReconciliationDataInfoDomain() {

	}

	public ReconciliationDataInfoDomain(ReconciliationDataInfoBuilder reconciliationDataInfoBuilder) {
		this.partnerReferenceNumber = reconciliationDataInfoBuilder.partnerReferenceNumber;
		this.orderReferenceNumber = reconciliationDataInfoBuilder.orderReferenceNumber;
		this.voucherCode = reconciliationDataInfoBuilder.voucherCode;
		this.partnerCode = reconciliationDataInfoBuilder.partnerCode;
		this.loyaltyTransactionId = reconciliationDataInfoBuilder.loyaltyTransactionId;
		this.partnerTransactionId = reconciliationDataInfoBuilder.partnerTransactionId;
		this.voucherAmount = reconciliationDataInfoBuilder.voucherAmount;
		this.transactionDate = reconciliationDataInfoBuilder.transactionDate;

	}

	public static class ReconciliationDataInfoBuilder {

		private String partnerReferenceNumber;
		private String orderReferenceNumber;
		private String voucherCode;
		private String partnerCode;
		private String loyaltyTransactionId;
		private String partnerTransactionId;
		private Double voucherAmount;
		private Date transactionDate;

		public ReconciliationDataInfoBuilder(String partnerReferenceNumber, String orderReferenceNumber,
				String voucherCode, String partnerCode, String loyaltyTransactionId, String partnerTransactionId,
				Double voucherAmount, Date transactionDate) {
			super();
			this.partnerReferenceNumber = partnerReferenceNumber;
			this.orderReferenceNumber = orderReferenceNumber;
			this.voucherCode = voucherCode;
			this.partnerCode = partnerCode;
			this.loyaltyTransactionId = loyaltyTransactionId;
			this.partnerTransactionId = partnerTransactionId;
			this.voucherAmount = voucherAmount;
			this.transactionDate = transactionDate;
		}

		public ReconciliationDataInfoBuilder partnerReferenceNumber(String partnerReferenceNumber) {
			this.partnerReferenceNumber = partnerReferenceNumber;
			return this;
		}

		public ReconciliationDataInfoBuilder orderReferenceNumber(String orderReferenceNumber) {
			this.orderReferenceNumber = orderReferenceNumber;
			return this;
		}

		public ReconciliationDataInfoBuilder voucherCode(String voucherCode) {
			this.voucherCode = voucherCode;
			return this;
		}

		public ReconciliationDataInfoBuilder partnerCode(String partnerCode) {
			this.partnerCode = partnerCode;
			return this;
		}

		public ReconciliationDataInfoBuilder loyaltyTransactionId(String loyaltyTransactionId) {
			this.loyaltyTransactionId = loyaltyTransactionId;
			return this;
		}

		public ReconciliationDataInfoBuilder partnerTransactionId(String partnerTransactionId) {
			this.partnerTransactionId = partnerTransactionId;
			return this;
		}

		public ReconciliationDataInfoBuilder voucherAmount(Double voucherAmount) {
			this.voucherAmount = voucherAmount;
			return this;
		}

		public ReconciliationDataInfoBuilder transactionDate(Date transactionDate) {
			this.transactionDate = transactionDate;
			return this;
		}

		public ReconciliationDataInfoDomain build() {
			return new ReconciliationDataInfoDomain(this);
		}

	}

}
