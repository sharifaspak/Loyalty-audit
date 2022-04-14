package com.loyalty.marketplace.outbound.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.outbound.database.entity.UploadedFileContent;


public interface UploadedFileContentRepository extends MongoRepository<UploadedFileContent, String> {
	
	//List<UploadedFileContent> findByProgramCodeIgnoreCaseAndCreatedDateBetweenAndFileTypeIgnoreCase(String program, Date startDate, Date endDate, String fileType);

	List<UploadedFileContent> findByProgramCodeIgnoreCaseAndSubscriptionCatalogIdAndCreatedDateBetweenAndFileTypeIgnoreCase(
			String program, String subscriptionId, Date fromDate, Date toDate, String bogoBulkUpload);
	
	boolean existsUploadedFileContentByExternalReferenceNumberIn(List<String> externalReferenceNumber);

	List<UploadedFileContent> findByUploadedFileInfoIdAndProgramCodeIgnoreCaseAndFileTypeIgnoreCase(String fileId,
			String program, String bogoBulkUpload);

	List<UploadedFileContent> findByExternalReferenceNumberIn(List<String> externalReferenceNumberList);
	
	List<UploadedFileContent> findByUploadedFileInfoIdIn(List<String> uploadedFileInfoIdList);
	
	List<UploadedFileContent> findByUploadedFileInfoId(String fileId);

}
