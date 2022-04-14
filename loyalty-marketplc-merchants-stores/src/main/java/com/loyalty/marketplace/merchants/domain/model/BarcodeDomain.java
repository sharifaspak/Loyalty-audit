package com.loyalty.marketplace.merchants.domain.model;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class BarcodeDomain {

	private String id;
	
	private String name;
	
	private String description;

	private String usrCreated;

	private String usrUpdated;

	private Date dtCreated;

	private Date dtUpdated;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getUsrCreated() {
		return usrCreated;
	}

	public String getUsrUpdated() {
		return usrUpdated;
	}

	public Date getDtCreated() {
		return dtCreated;
	}

	public Date getDtUpdated() {
		return dtUpdated;
	}
	
	public BarcodeDomain() {
		
	}

	public BarcodeDomain(BarcodeBuilder barcode) {
		super();
		this.id = barcode.id;
		this.name = barcode.name;
		this.description = barcode.description;
		this.usrCreated = barcode.usrCreated;
		this.usrUpdated = barcode.usrUpdated;
		this.dtCreated = barcode.dtCreated;
		this.dtUpdated = barcode.dtUpdated;
	}
	
	public static class BarcodeBuilder {

		private String id;
		
		private String name;
		
		private String description;

		private String usrCreated;

		private String usrUpdated;

		private Date dtCreated;

		private Date dtUpdated;

		public BarcodeBuilder(String name) {
			super();
			this.name = name;
		}

		public BarcodeBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public BarcodeBuilder description(String description) {
			this.description = description;
			return this;
		}

		public BarcodeBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}
		
		public BarcodeBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}
		
		public BarcodeBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}

		public BarcodeBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public BarcodeDomain build() {
			return new BarcodeDomain(this);
		}
	}
	
}
