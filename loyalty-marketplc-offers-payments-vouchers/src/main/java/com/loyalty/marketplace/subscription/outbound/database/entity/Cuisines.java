package com.loyalty.marketplace.subscription.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "Cuisines")
@Setter
@Getter
@ToString
public class Cuisines {
	
	@Id
	private String cuisineId;
	@Field("CuisinesName")
	private CuisinesName cuisineName;
	@Field("ProgramCode")
	private String programCode;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	

}
