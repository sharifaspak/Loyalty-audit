package com.loyalty.marketplace.stores.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class StoreDescription {
	
	@Field("English")
    private String description;
	
	@Field("Arabic")
	private String descriptionAr;

	@Override
	public String toString() {
		return "StoreDescription [description=" + description + ", descriptionAr=" + descriptionAr + "]";
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getDescriptionAr() {
		return descriptionAr;
	}

	public void setDescriptionAr(String descriptionAr) {
		this.descriptionAr = descriptionAr;
	}

}
