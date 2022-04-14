package com.loyalty.marketplace.promote.partner.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document(value = "PatnerPromotion")
public class PartnerPromoteEntity {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field(value = "PatnerCode")
	private String patnerCode;
	@Field(value = "AccountNumber")
	private String accountNumber;
	@Field(value = "ContactName")
	private String contactName;
	@Field(value = "ContactNumber")
	private String contactNumber;
	@Field(value = "ContactEmail")
	private String contactEmail;
	@Field(value = "DOB")
	private String dob;
	@Field(value = "Nationality")
	private String nationality;
	@Field(value = "Language")
	private String language;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
}
