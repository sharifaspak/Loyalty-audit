package com.loyalty.marketplace.subscription.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Title {

	@Field("Id")
	private String id;
	@Field("English")
	private String english;
	@Field("Arabic")
	private String arabic;
}
