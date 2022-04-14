package com.loyalty.marketplace.offers.merchants.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "Category")
public class Category {
	
	@Id
	private String id;
	
	@Field("Id")
	private String categoryId;
	
	@Field("Name")
	private Name name;
	
	@Field("ParentCategory")
	@DBRef
	private Category parentCategory;

	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;

}
