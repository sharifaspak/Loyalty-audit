package com.loyalty.marketplace.merchants.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;

public interface MerchantRepository extends MongoRepository<Merchant, String> {

	@Query("{'ProgramCode': {$regex:?0,$options:'i'}}")
	public Page<Merchant> findByProgramCodeIgnoreCasePagination(String programCode, Pageable pageNumberWithElements);
	
	public Merchant findByMerchantCode(String merchantCode);
	
	public Merchant findByMerchantCodeAndProgramCodeIgnoreCase(String merchantCode, String programCode);
	
	@Query("{'MerchantCode': ?0}")
	public Page<Merchant> findByMerchantCodePagination(String merchantCode, Pageable pageNumberWithElements);
	
	@Query("{'MerchantCode': ?0, 'ProgramCode': {$regex:?1,$options:'i'}}")
	public Page<Merchant> findByMerchantCodeAndProgramCodeIgnoreCasePagination(String merchantCode, String program, Pageable pageNumberWithElements);
	
	public List<Merchant> findByPartnerCode(String partnerCode);
	
	public List<Merchant> findByPartnerCodeAndProgramCodeIgnoreCase(String partnerCode, String programCode);
	
	@Query("{'PartnerCode': ?0}")
	public Page<Merchant> findByPartnerCodePagination(String partnerCode, Pageable pageNumberWithElements);
	
	@Query("{'PartnerCode': ?0, 'ProgramCode': {$regex:?1,$options:'i'}}")
	public Page<Merchant> findByPartnerCodeAndProgramCodeIgnoreCasePagination(String partnerCode, String programCode, Pageable pageNumberWithElements);
	
	@Query("{'merchantCode': ?0, 'contactPersons.emailId': ?1}")
	public Optional<Merchant> findByMerchantCodeAndEmailId(String merchantCode, String emailId);

	@Query("{'merchantCode': ?0, 'contactPersons.userName': ?1}")
	public Optional<Merchant> findByMerchantCodeAndUserName(String merchantCode, String userName);
	
	public List<Merchant> findByStatus(String status);
	
	@Query("{'Status': ?0}")
	public Page<Merchant> findByStatusWithPage(String status, Pageable pageNumberWithElements);
	
	@Query("{'Status': ?0, 'ProgramCode': {$regex:?1,$options:'i'}}")
	public Page<Merchant> findByProgramCodeIgnoreCaseAndStatusWithPage(String status, String program, Pageable pageNumberWithElements);
	
	@Query("{'Status': ?0}")
	public List<Merchant> findByStatusPagination(String status, Pageable pageNumberWithElements);

	@Query("{'merchantCode': ?0, {'contactPersons.emailId': ?1,'contactPersons.userName':{$ne : ?2}}}")
	public Optional<Merchant> findByMerchantCodeAndEmailIdAndUserName(String merchantCode, String emailId,
			String username);

	@Query("{'merchantCode': ?0, 'status': ?1}")
	public Merchant findByMerchantCodeAndStatus(String merchantCode, String status);
	
	@Query("{'merchantCode': ?0, 'status': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Merchant findByMerchantCodeAndStatusAndProgramCodeIgnoreCase(String merchantCode, String status, String programCode);
	
	@Query("{'merchantCode': ?0, 'status': ?1}")
	public Page<Merchant> findByMerchantCodeAndStatusPagination(String merchantCode, String status, Pageable pageNumberWithElements);
	
	@Query("{'merchantCode': ?0, 'status': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Page<Merchant> findByMerchantCodeAndStatusAndProgramCodeIgnoreCasePagination(String merchantCode, String status, String program, Pageable pageNumberWithElements);
	
	@Query("{ '$or':[{'Name.English':{$regex:?0,$options:'i'}}, {'Name.Arabic':{$regex:?0,$options:'i'}}] ,'status': ?1}") 
	public List<Merchant> findByMerchantName(String merchantName,String status);
	
	@Query("{ '$or':[{'Name.English':{$regex:?0,$options:'i'}}, {'Name.Arabic':{$regex:?0,$options:'i'}}] ,'status': ?1}") 
	public Page<Merchant> findByMerchantNamePagination(String merchantName,String status, Pageable pageNumberWithElements);
	
	@Query("{ '$or':[{'Name.English':{$regex:?0,$options:'i'}}, {'Name.Arabic':{$regex:?0,$options:'i'}}] ,'status': ?1, 'programCode': {$regex:?2,$options:'i'}}") 
	public Page<Merchant> findByMerchantNamAndProgramCodeePagination(String merchantName,String status, String program, Pageable pageNumberWithElements);
	
	@Query("{ '$or':[{'Name.English':{$regex:?0,$options:'i'}}, {'Name.Arabic':{$regex:?0,$options:'i'}}] ,'status': ?1 ,'category.categoryId': ?2}") 
	public Page<Merchant> findByMerchantNameCategoryId(String merchantName, String status, String categoryId, Pageable pageNumberWithElements);
	
	@Query("{ '$or':[{'Name.English':{$regex:?0,$options:'i'}}, {'Name.Arabic':{$regex:?0,$options:'i'}}] ,'status': ?1 ,'category.categoryId': ?2, 'programCode': {$regex:?3,$options:'i'}}") 
	public Page<Merchant> findByMerchantNameCategoryIdAndProgramCode(String merchantName, String status, String categoryId, String program, Pageable pageNumberWithElements);
	
	@Query("{'partnerCode':{'$in': ?0},'status': ?1}")
	public List<Merchant> findByPartnerCodeList(List<String> partnerCodes,String status);
	
	@Query("{'partnerCode':{'$in': ?0},'status': ?1}")
	public Page<Merchant> findByPartnerCodeListPagination(List<String> partnerCodes,String status, Pageable pageNumberWithElements);
	
	@Query("{'partnerCode':{'$in': ?0},'status': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Page<Merchant> findByPartnerCodeListAndProgramCodeIgnoreCasePagination(List<String> partnerCodes, String status, String program, Pageable pageNumberWithElements);
	
	@Query("{'partnerCode':{'$in': ?0},'status': ?1, 'category.categoryId': ?2}")
	public List<Merchant> findByPartnerCodeListCategoryId(List<String> partnerCodes,String status,String categoryId);	
	
	@Query("{'partnerCode':{'$in': ?0},'status': ?1, 'category.categoryId': ?2}")
	public Page<Merchant> findByPartnerCodeListCategoryIdPagination(List<String> partnerCodes,String status,String categoryId, Pageable pageNumberWithElements);
	
	@Query("{'partnerCode':{'$in': ?0},'status': ?1, 'category.categoryId': ?2, 'programCode': {$regex:?3,$options:'i'}}")
	public Page<Merchant> findByPartnerCodeListCategoryIdAndProgramCodeIgnoreCasePagination(List<String> partnerCodes, String status, String categoryId, String program, Pageable pageNumberWithElements);
	
	@Query("{'category.categoryId': ?0, 'status': ?1}")
	public Page<Merchant> findByCategoryId(String filterValue,String status, Pageable pageNumberWithElements);
	
	@Query("{'category.categoryId': ?0, 'status': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Page<Merchant> findByCategoryIdAndProgramIgnoreCase(String filterValue, String status, String program, Pageable pageNumberWithElements);
	
	@Query("{'id': {'$in': ?0}, 'status': ?1}")
	public Page<Merchant> findAllByMerchantIds(List<String> ids, String status, Pageable pageNumberWithElements);
	
	@Query("{'id': {'$in': ?0}, 'status': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Page<Merchant> findAllByMerchantIdsAndProgramCodeIgnoreCase(List<String> ids, String status, String program, Pageable pageNumberWithElements);
	
	@Query("{'id': {'$in': ?0}, 'status': ?1, 'category.categoryId': ?2}")
	public Page<Merchant> findAllByMerchantIdsCategoryId(List<String> ids, String status,String categoryId, Pageable pageNumberWithElements);
	
	@Query("{'id': {'$in': ?0}, 'status': ?1, 'category.categoryId': ?2, 'programCode': {$regex:?3,$options:'i'}}")
	public Page<Merchant> findAllByMerchantIdsCategoryIdAndProgramCodeIgnoreCase(List<String> ids, String status, String categoryId, String program, Pageable pageNumberWithElements);
	
	@Query("{'contactPersons.userName': {$regex:?0,$options:'i'}, 'status': {$regex:?1,$options:'i'}}")
    public Optional<Merchant> findByUserNameAndActive(String userName, String active);
	
	@Query("{'MerchantCode' : {$in : ?0}}") 
	List<Merchant> findByMerchantCodeList(List<String> merchantCodeList);
	
	@Query("{'MerchantCode' : {$in : ?0}, 'ProgramCode': {$regex:?1,$options:'i'}}") 
	List<Merchant> findByMerchantCodeAndProgramCodeListIgnoreCase(List<String> merchantCodeList, String program);
	
}
