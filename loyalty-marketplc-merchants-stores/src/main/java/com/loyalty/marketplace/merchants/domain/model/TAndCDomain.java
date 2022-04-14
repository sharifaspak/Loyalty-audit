package com.loyalty.marketplace.merchants.domain.model;

import org.springframework.stereotype.Component;

@Component
public class TAndCDomain {

	private String tnCEn;

	private String tnCAr;
	
	public String getTnCEn() {
		return tnCEn;
	}

	public String getTnCAr() {
		return tnCAr;
	}

	public TAndCDomain() {

	}

	public TAndCDomain(TAndCBuilder name) {
		super();
		this.tnCEn = name.tnCEn;
		this.tnCAr = name.tnCAr;
	}

	public static class TAndCBuilder {

		private String tnCEn;

		private String tnCAr;

		public TAndCBuilder(String tnCEn, String tnCAr) {
			super();
			this.tnCEn = tnCEn;
			this.tnCAr = tnCAr;
		}

		public TAndCBuilder tnCEn(String tnCEn) {
			this.tnCEn = tnCEn;
			return this;
		}

		public TAndCBuilder tnCAr(String tnCAr) {
			this.tnCAr = tnCAr;
			return this;
		}

		public TAndCDomain build() {
			return new TAndCDomain(this);
		}
	}
	
}
