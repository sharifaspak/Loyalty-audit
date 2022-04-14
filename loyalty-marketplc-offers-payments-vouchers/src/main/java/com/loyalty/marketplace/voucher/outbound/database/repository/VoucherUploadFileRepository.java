package com.loyalty.marketplace.voucher.outbound.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherUploadFile;

public interface VoucherUploadFileRepository extends MongoRepository<VoucherUploadFile, String> {

	@Query("{'MerchantCode': ?0}")
	Page<VoucherUploadFile> findByMerchantCode(String merchantCode, Pageable pageNumberWithElements);
	
	@Query("{'ProgramCode': {$regex:?0,$options:'i'}}")
	Page<VoucherUploadFile> findByProgramCodeIgnoreCase(String programCode, Pageable pageNumberWithElements);
	
	@Query("{'MerchantCode': ?0, 'ProgramCode': {$regex:?1,$options:'i'}}")
	Page<VoucherUploadFile> findByMerchantCodeAndProgramCodeIgnoreCase(String merchantCode, String programCode, Pageable pageNumberWithElements);
	
	
	Page<VoucherUploadFile> findByMerchantCodeIn(List<String> merchantCode, Pageable pageNumberWithElements);
	
	@Query("{'MerchantCode' : {$in : ?0}, 'ProgramCode': {$regex:?1,$options:'i'}}") 
	Page<VoucherUploadFile> findByProgramCodeIgnoreCaseAndMerchantCodeIn(List<String> merchantCode, String programCode, Pageable pageNumberWithElements);

	@Query("{'OfferId': ?0}")
	Page<VoucherUploadFile> findByOfferId(String offerId, Pageable pageNumberWithElements);

	List<VoucherUploadFile> findByProgramCodeIgnoreCaseAndUploadedDateBetweenAndFileTypeIgnoreCase(String program, Date startDate, Date endDate, String fileType);

	List<VoucherUploadFile> findByProgramCodeIgnoreCaseAndOfferIdAndUploadedDateBetweenAndFileTypeIgnoreCase(String program, String offerId, Date fromDate, Date toDate, String fileType);

	VoucherUploadFile findByIdAndProgramCodeIgnoreCaseAndFileTypeIgnoreCase(String fileId, String program, String fileType);
}
