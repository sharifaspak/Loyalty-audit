package com.loyalty.marketplace.outbound.database.entity.image;

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
