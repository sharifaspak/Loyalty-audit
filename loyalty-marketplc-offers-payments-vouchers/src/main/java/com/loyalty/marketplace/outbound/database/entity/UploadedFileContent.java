package com.loyalty.marketplace.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UploadedFileContent")
public class UploadedFileContent {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("UploadedFileInfoId")
	private String uploadedFileInfoId;
	@Field("FileName")
	private String fileName;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("ExternalReferenceNumber")
	private String externalReferenceNumber;
	@Field("SubscriptionId")
	private String subscriptionId;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
	@Field("FileType")
	private String fileType;
	@Field("RowNum")
	private String rowNum;
	@Field("ContentString")
	private String contentString;
	@Field("EventDate")
	private Date eventDate;
	@Field("Status")
	private String status;
	@Field("ProcessedDate")
	private Date processedDate;
	@Field("ErrorCode")
	private String errorCode;
	@Field("ErrorMessage")
	private String errorMessage;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("CreatedUser")
	private String createdUser;
	@Field("UploadedDate")
	private Date uploadedDate;
	@Field("UpdatedDate")
	@LastModifiedDate
	private Date updatedDate;
	@Field("UpdatedUser")
	private String updatedUser;
}
