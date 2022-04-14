package com.loyalty.marketplace.domain.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class DenominationDomain {

	private String denominationId;
	private String programCode;
	private DenominationDescriptionDomain denominationDescription;
	private DenominationValueDomain denominationValue;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	
	
	public DenominationDomain(DenominationBuilder denomination) {
		super();
		this.denominationId = denomination.denominationId;
		this.programCode = denomination.programCode;
		this.denominationDescription = denomination.denominationDescription;
		this.denominationValue = denomination.denominationValue;
		this.usrCreated = denomination.usrCreated;
		this.usrUpdated = denomination.usrUpdated;
		this.dtCreated = denomination.dtCreated;
		this.dtUpdated = denomination.dtUpdated;
	}
	
	public static class DenominationBuilder {
		
		private String denominationId;
		private String programCode;
		private DenominationDescriptionDomain denominationDescription;
		private DenominationValueDomain denominationValue;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;
		
		public DenominationBuilder(String denominationId) {
			this.denominationId = denominationId;
		}
		
		public DenominationBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public DenominationBuilder denominationId(String denominationId) {
			this.denominationId = denominationId;
			return this;
		}
		
		public DenominationBuilder denominationDescription(DenominationDescriptionDomain denominationDescription) {
			this.denominationDescription = denominationDescription;
			return this;
		}
		
		public DenominationBuilder denominationValue(DenominationValueDomain denominationValue) {
			this.denominationValue = denominationValue;
			return this;
		}
		
		public DenominationBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}
		
		public DenominationBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public DenominationBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}

		public DenominationBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}
		
		public DenominationDomain build() {
			return new DenominationDomain(this);
		}

	}
	
	
	
	
}
