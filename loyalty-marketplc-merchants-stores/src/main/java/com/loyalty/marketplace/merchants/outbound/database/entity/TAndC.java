package com.loyalty.marketplace.merchants.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class TAndC {

	@Field("English")
	private String tnCEn;

	@Field("Arabic")
	private String tnCAr;

	public String getTnCEn() {
		return tnCEn;
	}

	public void setTnCEn(String tnCEn) {
		this.tnCEn = tnCEn;
	}

	public String getTnCAr() {
		return tnCAr;
	}

	public void setTnCAr(String tnCAr) {
		this.tnCAr = tnCAr;
	}

	@Override
	public String toString() {
		return "TAndC [tnCEn=" + tnCEn + ", tnCAr=" + tnCAr + "]";
	}
	
}
