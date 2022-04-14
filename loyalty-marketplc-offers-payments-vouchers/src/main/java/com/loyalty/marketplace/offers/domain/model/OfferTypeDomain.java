package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;
import java.util.List;

import com.loyalty.marketplace.domain.model.PaymentMethodDomain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferTypeDomain {

	private String offerTypeId;
	private OfferTypeDescriptionDomain offerDescription;
	private List<PaymentMethodDomain> paymentMethods;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	
	public OfferTypeDomain(OfferTypeBuilder offerType) {
		super();
		this.offerTypeId = offerType.offerTypeId;
		this.offerDescription =offerType.offerDescription;
		this.paymentMethods = offerType.paymentMethods;
		this.usrCreated = offerType.usrCreated;
		this.usrUpdated = offerType.usrUpdated;
		this.dtCreated = offerType.dtCreated;
		this.dtUpdated = offerType.dtUpdated;
	}
	
	public static class OfferTypeBuilder {
		
		private String offerTypeId;
		private OfferTypeDescriptionDomain offerDescription;
		private List<PaymentMethodDomain> paymentMethods;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;
		
		public OfferTypeBuilder(String offerTypeId) {
			this.offerTypeId = offerTypeId;
		}
		
		public OfferTypeBuilder offerTypeId(String offerTypeId) {
			this.offerTypeId = offerTypeId;
			return this;
		}
		
		public OfferTypeBuilder offerDescription(OfferTypeDescriptionDomain offerDescription) {
			this.offerDescription = offerDescription;
			return this;
		}
		
		public OfferTypeBuilder paymentMethods(List<PaymentMethodDomain> paymentMethods) {
			this.paymentMethods = paymentMethods;
			return this;
		}
		
		public OfferTypeBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}
		
		public OfferTypeBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public OfferTypeBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}

		public OfferTypeBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}
		
		public OfferTypeDomain build() {
			return new OfferTypeDomain(this);
		}

	}
	
	
}
