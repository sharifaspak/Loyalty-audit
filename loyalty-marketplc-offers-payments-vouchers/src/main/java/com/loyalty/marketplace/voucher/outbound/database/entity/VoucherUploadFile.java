package com.loyalty.marketplace.voucher.outbound.database.entity;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Document(collection = "UploadVoucherFile")
@Data
@ToString
public class VoucherUploadFile {

	@Field("ProgramCode")
	private String programCode;

	@Id	
	private String id;

	@NotEmpty(message="File name cannot be empty")
	@Field("FileName")
	private String fileName;

	@NotEmpty(message="Merchant Code cannot be empty")
	@Field("MerchantCode")
	private String merchantCode;

	
	@Field("OfferId")
	private String offerId;

	@NotEmpty(message="Upload date cannot be empty")
	@Field("UploadedDate")
	private Date uploadedDate;

	@Field("FileType")
	private String fileType;

	@NotEmpty(message="File content cannot be empty")
	@Field("FileContent")
	private String fileContent;

	@Field("FileProcessingStatus")
	private String fileProcessingStatus;
	
	@Field("Reason")
	private String reason;

	@NotEmpty(message="Handback file content cannot be empty")
	@Field("HandbackFile")
	private String handbackFile;
	
	@Field("TranscationId")
	private String transactionId;
	
	@Field("CreatedDate")
	private Date createdDate;

	@Field("CreatedUser")
	private String createdUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;

	@Field("UpdatedUser")
	private String updatedUser;
}
