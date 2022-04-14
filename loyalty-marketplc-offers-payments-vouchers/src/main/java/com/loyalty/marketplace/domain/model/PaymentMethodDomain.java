package com.loyalty.marketplace.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class PaymentMethodDomain {

	private String paymentMethodId;
	private String programCode;
	private String description;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;



	public PaymentMethodDomain(PaymentMethodDomainBuilder paymentMethodDomainBuilder) {
		super();
		this.paymentMethodId = paymentMethodDomainBuilder.paymentMethodId;
		this.programCode = paymentMethodDomainBuilder.programCode;
		this.description = paymentMethodDomainBuilder.description;
		this.usrCreated = paymentMethodDomainBuilder.usrCreated;
		this.usrUpdated = paymentMethodDomainBuilder.usrUpdated;
		this.dtCreated = paymentMethodDomainBuilder.dtCreated;
		this.dtUpdated = paymentMethodDomainBuilder.dtUpdated;
	}
	
	public static class PaymentMethodDomainBuilder {
		
		private String paymentMethodId;
		private String programCode;
		private String description;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;

		public PaymentMethodDomainBuilder(String paymentMethodId) {
			super();
			this.paymentMethodId = paymentMethodId;
		}
		public PaymentMethodDomainBuilder(String paymentMethodId, String description) {
			super();
			this.paymentMethodId = paymentMethodId;
			this.description = description;
		}
		
		public PaymentMethodDomain build() {
			return new PaymentMethodDomain(this);
		}

	}
}
