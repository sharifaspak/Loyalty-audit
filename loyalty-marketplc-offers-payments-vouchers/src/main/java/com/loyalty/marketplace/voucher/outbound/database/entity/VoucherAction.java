package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Document(collection = "VoucherAction")
@Data
@ToString
public class VoucherAction {
	
	@Id
	private String id;
	@Field("ProgramCode")
	private String program;
	@Field("Action")
	private String action;
	@Field("RedemptionMethod")
	private String redemptionMethod;
	@Field("Label")
	private String label;
	@Field("CreatedUser") 	
	private String createdUser;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("UpdatedDate")
	private Date updatedDate;

}
