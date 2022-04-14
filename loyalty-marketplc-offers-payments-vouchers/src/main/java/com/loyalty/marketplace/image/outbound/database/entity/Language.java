package com.loyalty.marketplace.image.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Language {

	@Field("English")
	private String english;
	
	@Field("Arabic")
	private String arabic;
	
}
