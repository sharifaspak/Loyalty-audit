package com.loyalty.marketplace.offers.stores.domain.model;

import java.util.Date;
import java.util.List;

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
public class StoreDomain {

	private String id;
	private String programCode;
    private String storeCode;
	private NameDomain storeName;
	private String merchantCode;
	private String status;
	private NameDomain address;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	private List<StoreContactPersonDomain> contactPersons;

	public StoreDomain(StoreBuilder storeBuilder) {
		super();
		this.id = storeBuilder.id;
		this.programCode = storeBuilder.programCode;
		this.merchantCode = storeBuilder.merchantCode;
		this.storeCode = storeBuilder.storeCode;
		this.address = storeBuilder.address;
		this.storeName = storeBuilder.storeName;
		this.status = storeBuilder.status;
		this.dtCreated = storeBuilder.dtCreated;
		this.dtUpdated = storeBuilder.dtUpdated;
		this.usrCreated = storeBuilder.usrCreated;
		this.usrUpdated = storeBuilder.usrUpdated;
		this.contactPersons = storeBuilder.contactPersons;
	
	}

	public static class StoreBuilder {
		
		private String id;
        private String programCode;
		private String merchantCode;
		private NameDomain storeName;
		private String storeCode;
		private String status;
		private NameDomain address;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;
		private List<StoreContactPersonDomain> contactPersons;

		public StoreBuilder(String id) {
			super();
			this.id = id;
		}
				
		public StoreDomain build() {
			return new StoreDomain(this);
		}

	}
	
}
