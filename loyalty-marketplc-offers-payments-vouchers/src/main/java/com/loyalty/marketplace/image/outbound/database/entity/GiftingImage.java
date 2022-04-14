package com.loyalty.marketplace.image.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GiftingImage {

	@Field("ImageId")
	private Integer imageId;
	
	@Field("Type")
	private String type;
	
	@Field("Priority")
	private Integer priority;
	
	@Field("BackgroundPriority")
	private Integer backgroundPriority;
	
	@Field("ColorCode")
	private String colorCode;
	
	@Field("ColorDirection")
	private String colorDirection;
	
	@Field("Name")
	private Language name;
	
	@Field("GreetingMessage")
	private Language greetingMessage;
	
}
