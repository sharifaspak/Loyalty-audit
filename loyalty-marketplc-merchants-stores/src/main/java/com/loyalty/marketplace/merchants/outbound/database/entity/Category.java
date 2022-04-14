package com.loyalty.marketplace.merchants.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.merchants.constants.DBConstants;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = DBConstants.BARCODE)
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
