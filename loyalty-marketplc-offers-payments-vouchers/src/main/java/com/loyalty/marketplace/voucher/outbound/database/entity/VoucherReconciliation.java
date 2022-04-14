package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Document(collection = "Reconciliation")
@Data
@ToString
public class VoucherReconciliation {
	@Id	
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("StartDateTime")
	private Date startDateTime;
	
	@Field("EndDateTime")
	private Date endDateTime;
	
	@Field("PartnerCode")
	private String partnerCode;

	@Field("Loyalty")
	private ReconciliationInfo loyalty;

	@Field("Partner")
	private ReconciliationInfo partner;
	
	@Field("ReconciliationRefId")
	@DBRef
	private VoucherReconciliationData reconciliationRefId;	

	@Field("CreatedDate")
	private Date createdDate;

	@Field("CreatedUser")
	private String createdUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;

	@Field("UpdatedUser")
	private String updatedUser;
}
