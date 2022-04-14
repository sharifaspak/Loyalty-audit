package com.loyalty.marketplace.outbound.database.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.loyalty.marketplace.outbound.database.entity.UploadedFileInfo;

@Repository
public interface UploadedFileInfoRepository extends MongoRepository<UploadedFileInfo, String> {

	List<UploadedFileInfo> findByIdIn(List<String> uploadedFileInfoIdList);

	List<UploadedFileInfo> findByProgramCodeIgnoreCaseAndSubscriptionCatalogIdAndCreatedDateBetween(String program,
			String subscriptionCatalogId, Date fromDate, Date toDate);
	List<UploadedFileInfo>  findByProcessingStatusAndFileSource(String status, String fileType);
	
	List<UploadedFileInfo>  findByProgramCodeIgnoreCaseAndFileSource(String program, String fileType);
	
	Optional<UploadedFileInfo>  findById(String fileId);

}
