package com.loyalty.marketplace.voucher.domain;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReconciliationInfoDomain {

	@Autowired
	ModelMapper modelMapper;

	private Integer noofCountTransaction;
	private Double totalAmount;

	public Integer getNoofCountTransaction() {
		return noofCountTransaction;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public ReconciliationInfoDomain() {

	}

	public ReconciliationInfoDomain(ReconciliationInfoBuilder reconciliationInfoBuilder) {
		this.noofCountTransaction = reconciliationInfoBuilder.noofCountTransaction;
		this.totalAmount = reconciliationInfoBuilder.totalAmount;

	}

	public static class ReconciliationInfoBuilder {

		private Integer noofCountTransaction;
		private Double totalAmount;

		public ReconciliationInfoBuilder(Integer noofCountTransaction, Double totalAmount) {
			super();
			this.noofCountTransaction = noofCountTransaction;
			this.totalAmount = totalAmount;

		}

		public ReconciliationInfoBuilder noofCountTransaction(Integer noofCountTransaction) {
			this.noofCountTransaction = noofCountTransaction;
			return this;
		}

		public ReconciliationInfoBuilder totalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
			return this;
		}

		public ReconciliationInfoDomain build() {
			return new ReconciliationInfoDomain(this);
		}

	}

}
