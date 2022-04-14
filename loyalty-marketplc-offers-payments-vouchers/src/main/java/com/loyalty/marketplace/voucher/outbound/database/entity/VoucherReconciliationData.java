package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders;

import lombok.Data;
import lombok.ToString;

@Document(collection = "ReconciliationData")
@Data
@ToString
public class VoucherReconciliationData {
	@Id	
	private String id;
	
	@Field("ProgramCode")
	private String programCode;

	@Field("ReconciliationLevel")
	private String reconciliationLevel;

	
	@Field("LoyaltyReconciliationData")
	private List<ReconciliationDataInfo> loyaltyReconciliationData;
	
	@Field("PartnerReconciliationData")
	private List<ReconciliationDataInfo> partnerReconciliationData;

	@Field("PartnerContent")
	private List<Orders> partnerContent;
	
	@Field("CreatedDate")
	private Date createdDate;

	@Field("CreatedUser")
	private String createdUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;

	@Field("UpdatedUser")
	private String updatedUser;
}
