package com.loyalty.marketplace.merchants.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class Name {

	@Field("English")
	private String english;

	@Field("Arabic")
	private String arabic;

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getArabic() {
		return arabic;
	}

	public void setArabic(String arabic) {
		this.arabic = arabic;
	}

	@Override
	public String toString() {
		return "Name [english=" + english + ", arabic=" + arabic + "]";
	}
	
}
