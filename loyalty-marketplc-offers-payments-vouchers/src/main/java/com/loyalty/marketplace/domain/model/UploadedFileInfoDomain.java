package com.loyalty.marketplace.domain.model;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.outbound.database.entity.UploadedFileInfo;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileInfoRepository;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Component
public class UploadedFileInfoDomain {
	
private static final Logger log = LoggerFactory.getLogger(UploadedFileInfoDomain.class);
	
	@Autowired
	private UploadedFileInfoRepository uploadedFileInfoRepository;
	
	private String id;
	private String programCode;
	private String fileName;
	private Date uploadedDate;
	private String uploadedStatus;
	private String statusReason;
	private String processingStatus;
	private String errorHandBackFileName;
	private String successHandBackFileName;
	private String subscriptionCatalogId;
	private String partnerCode;
	private String invoiceReferenceNumber;
	private String externalTransactionId;
	private Integer recordsCount;
	private Date createdDate;
	private String createdUser;
	private String updatedUser;
	private String errorCode;
	private String errorMessage;
	
	public UploadedFileInfoDomain() {
		
	}

	public UploadedFileInfoDomain(UploadedFileInfoBuilder builder) {
		super();
		this.programCode = builder.programCode;
		this.fileName = builder.fileName;
		this.uploadedDate = builder.uploadedDate;
		this.uploadedStatus = builder.uploadedStatus;
		this.statusReason = builder.statusReason;
		this.createdDate = builder.createdDate;
		this.createdUser = builder.createdUser;
		this.updatedUser = builder.updatedUser;
		this.processingStatus = builder.processingStatus;
		this.recordsCount = builder.recordsCount;
		this.errorHandBackFileName = builder.errorHandBackFileName;
		this.successHandBackFileName = builder.successHandBackFileName;
		this.subscriptionCatalogId = builder.subscriptionCatalogId;
		this.partnerCode = builder.partnerCode;
		this.invoiceReferenceNumber = builder.invoiceReferenceNumber;
		this.externalTransactionId = builder.externalTransactionId;
		this.errorCode = builder.errorCode;
		this.errorMessage = builder.errorMessage;

	}

	public static class UploadedFileInfoBuilder {

		private String programCode;
		private String fileName;
		private Date uploadedDate;
		private String uploadedStatus;
		private String statusReason;
		private String processingStatus;
		private String errorHandBackFileName;
		private String successHandBackFileName;
		private String subscriptionCatalogId;
		private String partnerCode;
		private String invoiceReferenceNumber;
		private String externalTransactionId;
		private Integer recordsCount;
		private Date createdDate;
		private String createdUser;
		private String updatedUser;
		private String errorCode;
		private String errorMessage;

		
		public UploadedFileInfoBuilder(String programCode, String fileName, Date uploadedDate, String uploadedStatus,
				String statusReason, String processingStatus, String errorHandBackFileName, String successHandBackFileName, String subscriptionCatalogId,
				String partnerCode, String invoiceReferenceNumber, String externalTransactionId, Integer recordsCount, Date createdDate,
				String createdUser, String updatedUser, String errorCode, String errorMessage) {
			super();
			this.programCode = programCode;
			this.fileName = fileName;
			this.uploadedDate = uploadedDate;
			this.uploadedStatus = uploadedStatus;
			this.statusReason = statusReason;
			this.processingStatus = processingStatus;
			this.errorHandBackFileName = errorHandBackFileName;
			this.successHandBackFileName = successHandBackFileName;
			this.subscriptionCatalogId = subscriptionCatalogId;
			this.partnerCode = partnerCode;
			this.invoiceReferenceNumber = invoiceReferenceNumber;
			this.externalTransactionId = externalTransactionId;
			this.recordsCount = recordsCount;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedUser = updatedUser;
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}


		public UploadedFileInfoDomain build() {
			return new UploadedFileInfoDomain(this);
		}

	}
	
	public void updateInfoStatus(List<UploadedFileInfo> uploadedFileInfoList, String status) {
		log.info("Enter updateInfoStatus :: update status to :{}", status);
		for(UploadedFileInfo uploadedFileInfo : uploadedFileInfoList) {
			uploadedFileInfo.setProcessingStatus(status);
		}		
		uploadedFileInfoRepository.saveAll(uploadedFileInfoList);
		log.info("Exit updateInfoStatus");
	}

}