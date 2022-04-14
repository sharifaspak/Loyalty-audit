package com.loyalty.marketplace.domain.model;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoOperations;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.equivalentpoints.constant.ApplicationConstants;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileContent;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileContentRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.mongodb.client.result.UpdateResult;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class UploadedFileContentDomain {

	private static final Logger LOG = LoggerFactory.getLogger(UploadedFileContentDomain.class);
	
	@Autowired
	UploadedFileContentRepository uploadedFileContentRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	private String programCode;
	private String membershipCode;
	private String accountNumber;
	private String subscriptionId;
	private String subscriptionCatalogId;
	private String externalReferenceNumber;
	private String uploadedFileInfoId;
	private String fileName;
	private String fileType;
	private String rowNum;
	private String contentString;
	private Date eventDate;
	private String status;
	private Date processedDate;
	private String errorCode;
	private String errorMessage;
	private Date createdDate;
	private Date uploadedDate;
	private String createdUser;
	private String updatedUser;
	
	
	public UploadedFileContentDomain(UploadedFileContentBuilder builder) {
		super();
		this.programCode = builder.programCode;
		this.membershipCode = builder.membershipCode;
		this.accountNumber = builder.accountNumber;
		this.subscriptionId = builder.subscriptionId;
		this.subscriptionCatalogId = builder.subscriptionCatalogId;
		this.externalReferenceNumber = builder.externalReferenceNumber;
		this.uploadedFileInfoId = builder.uploadedFileInfoId;
		this.fileName = builder.fileName;
		this.fileType = builder.fileType;
		this.rowNum = builder.rowNum;
		this.contentString = builder.contentString;
		this.eventDate = builder.eventDate;
		this.status = builder.status;
		this.processedDate = builder.processedDate;
		this.errorCode = builder.errorCode;
		this.errorMessage = builder.errorMessage;
		this.createdDate = builder.createdDate;
		this.uploadedDate = builder.uploadedDate;
		this.createdUser = builder.createdUser;
		this.updatedUser = builder.updatedUser;
	}
	
	public static class UploadedFileContentBuilder {
		
		private String programCode;
		private String membershipCode;
		private String accountNumber;
		private String subscriptionId;
		private String subscriptionCatalogId;
		private String externalReferenceNumber;
		private String uploadedFileInfoId;
		private String fileName;
		private String fileType;
		private String rowNum;
		private String contentString;
		private Date eventDate;
		private String status;
		private Date processedDate;
		private String errorCode;
		private String errorMessage;
		private Date createdDate;
		private Date uploadedDate;
		private String createdUser;
		private String updatedUser;
		
		
		public UploadedFileContentBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;			
		}		
		public UploadedFileContentBuilder membershipCode(String membershipCode) {
			this.membershipCode = membershipCode;
			return this;
		}		
		public UploadedFileContentBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}	
		public UploadedFileContentBuilder subscriptionId(String subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		public UploadedFileContentBuilder subscriptionCatalogId(String subscriptionCatalogId) {
			this.subscriptionCatalogId = subscriptionCatalogId;
			return this;
		}
		public UploadedFileContentBuilder externalReferenceNumber(String externalReferenceNumber) {
			this.externalReferenceNumber = externalReferenceNumber;
			return this;
		}
		public UploadedFileContentBuilder uploadedFileInfoId(String uploadedFileInfoId) {
			this.uploadedFileInfoId = uploadedFileInfoId;
			return this;
		}
		public UploadedFileContentBuilder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}
		public UploadedFileContentBuilder fileType(String fileType) {
			this.fileType = fileType;
			return this;
		}
		public UploadedFileContentBuilder rowNum(String rowNum) {
			this.rowNum = rowNum;
			return this;
		}
		public UploadedFileContentBuilder contentString(String contentString) {
			this.contentString = contentString;
			return this;
		}
		public UploadedFileContentBuilder eventDate(Date eventDate) {
			this.eventDate = eventDate;
			return this;
		}
		public UploadedFileContentBuilder status(String status) {
			this.status = status;
			return this;
		}
		public UploadedFileContentBuilder processedDate(Date processedDate) {
			this.processedDate = processedDate;
			return this;
		}
		public UploadedFileContentBuilder errorCode(String errorCode) {
			this.errorCode = errorCode;
			return this;
		}
		public UploadedFileContentBuilder errorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
			return this;
		}
		public UploadedFileContentBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		public UploadedFileContentBuilder uploadedDate(Date uploadedDate) {
			this.uploadedDate = uploadedDate;
			return this;
		}
		public UploadedFileContentBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		public UploadedFileContentBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public UploadedFileContentDomain build() {
			return new UploadedFileContentDomain(this);
		}
	}
	
	public UploadedFileContent saveFailedFileContent(UploadedFileContent savedUploadedFileContent, MarketPlaceCode errorCode) throws SubscriptionManagementException {
		try {	
			LOG.info("saveFailedFileContent");
			savedUploadedFileContent.setStatus(MarketplaceConfigurationConstants.STATUS_FAILED);
			savedUploadedFileContent.setErrorCode(errorCode.getId());
			savedUploadedFileContent.setErrorMessage(errorCode.getMsg());
			return uploadedFileContentRepository.save(savedUploadedFileContent);		
		} catch (Exception e) {		
			LOG.info("Exception occured while saving in content.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveFailedFileContent",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.FILE_CONTENT_SAVE_FAILED);
		
		} 		
	}
	
	public UploadedFileContent saveFailedFileContent(UploadedFileContent savedUploadedFileContent, ResultResponse resultResponse) throws SubscriptionManagementException {
		try {	
			LOG.info("saveFailedFileContent");
			savedUploadedFileContent.setStatus(MarketplaceConfigurationConstants.STATUS_FAILED);
			savedUploadedFileContent.setErrorCode(resultResponse.getApiStatus().getErrors().get(0).getCode().toString());
			savedUploadedFileContent.setErrorMessage(resultResponse.getApiStatus().getErrors().get(0).getMessage());
			return uploadedFileContentRepository.save(savedUploadedFileContent);		
		} catch (Exception e) {		
			LOG.info("Exception occured while saving in content.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveFailedFileContent",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.FILE_CONTENT_SAVE_FAILED);
		
		} 		
	}
	
	public void updateContentStatusToProcessing(List<String> uploadedFileInfoIdList) {
		LOG.info("Inside UploadedFileContentDomain.updateStatusInUploadedFileContentCollection");
		Query query = new Query();
		query.addCriteria(Criteria.where(ApplicationConstants.STATUS).is(ApplicationConstants.UPLOADED));
		query.addCriteria(Criteria.where("UploadedFileInfoId").in(uploadedFileInfoIdList));

		Update update = new Update().set(ApplicationConstants.STATUS, ApplicationConstants.PROCESSING);

		UpdateResult res = mongoOperations.updateMulti(query, update, ApplicationConstants.UPLOADEDFILECONTENTCOLLECTION); 
		LOG.info("Changing status of Uploaded File Content collection from 'Uploaded' to 'Processing' where Id for FileInfoContent is:{}", res.getUpsertedId());
		LOG.info("Changing status of Uploaded File Content collection from 'Uploaded' to 'Processing' where count of record is:{}", res.getModifiedCount());
		
	}
}
