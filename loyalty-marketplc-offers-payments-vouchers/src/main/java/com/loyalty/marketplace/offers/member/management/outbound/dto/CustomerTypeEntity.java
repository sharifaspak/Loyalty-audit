package com.loyalty.marketplace.offers.member.management.outbound.dto;


import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Document(collection = "CustomerType")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypeEntity {
	@Id
	private String id;
	@Field(value = "CustomerType")
	private String customerType;
	@Field(value = "Description")
	private String customerDesc;
	
	@Field(value = "TypeId")
	private int typeId;
	@DBRef
	@Field(value = "ParentId")
	private CustomerTypeEntity parentId;
	@Field(value = "EligiblityMatrix")
	private EligibilityMatrix eligiblityMatrix;
	@Field(value = "Tier")
	private List<Tier> tier;
	@Field(value = "CreatedDate")
	private Date createdDate;
	@Field(value = "CreatedUser")
	private String createdUser;
	@Field(value = "UpdatedUser")
	private String updatedUser;
	@Field(value = "UpdatedDate")
	private Date updatedDate;
	@Field(value = "ProgramCode")
	private String programCode;
	
	@Field(value = "Gifts")
	private List<Gift> gift;
	
}

