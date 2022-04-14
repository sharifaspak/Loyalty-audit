package com.loyalty.marketplace.merchants.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class WhatYouGet {

	@Field("English")
	private String whatYouGetEn;

	@Field("Arabic")
	private String whatYouGetAr;

	public String getWhatYouGetEn() {
		return whatYouGetEn;
	}

	public void setWhatYouGetEn(String whatYouGetEn) {
		this.whatYouGetEn = whatYouGetEn;
	}

	public String getWhatYouGetAr() {
		return whatYouGetAr;
	}

	public void setWhatYouGetAr(String whatYouGetAr) {
		this.whatYouGetAr = whatYouGetAr;
	}

	@Override
	public String toString() {
		return "WhatYouGet [whatYouGetEn=" + whatYouGetEn + ", whatYouGetAr=" + whatYouGetAr + "]";
	}
	
}
