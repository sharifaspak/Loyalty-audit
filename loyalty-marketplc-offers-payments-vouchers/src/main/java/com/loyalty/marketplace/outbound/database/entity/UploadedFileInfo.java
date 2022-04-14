package com.loyalty.marketplace.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "UploadedFileInfo")
public class UploadedFileInfo {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("FileName")
	private String fileName;
	@Field("FileSource")
	private String fileSource;
	@Field("RecordsCount")
	private Integer recordsCount;
	@Field("UploadedDate")
	private Date uploadedDate;
	@Field("UploadedStatus")
	private String uploadedStatus;
	@Field("StatusReason")
	private String statusReason;
	@Field("ProcessingStatus")
	private String processingStatus;
	@Field("HandbackFlag")
	private boolean ishandback;
	@Field("HandBackFilePath")
	private String handbackFilePath;
	@Field("ErrorHandBackFileName")
	private String errorHandBackFileName;
	@Field("SuccessHandBackFileName")
	private String successHandBackFileName;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
	@Field("PartnerCode")
	private String partnerCode;
	@Field("InvoiceReferenceNumber")
	private String invoiceReferenceNumber;
	@Field("ExternalTransactionId")
	private String externalTransactionId;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("ErrorCode")
	private String errorCode;
	@Field("ErrorMessage")
	private String errorMessage;

}
