package com.loyalty.marketplace.stores.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.stores.outbound.database.entity.Store;

public interface StoreRepository extends MongoRepository<Store, String> {
 
	public List<Store> findByProgramCodeIgnoreCase(String programCode);
	
	public Store findByStoreCode(String code);
	
	public List<Store> findByMerchantCode(String merchantCode);
	
	public List<Store> findByMerchantCodeAndProgramCodeIgnoreCase(String merchantCode, String program);
	
	@Query("{'merchantCode': ?0}")
	public Page<Store> findByMerchantCodePagination(String merchantCode, Pageable pageNumberWithElements);

	@Query("{'merchantCode': ?0, 'programCode': {$regex:?1,$options:'i'}}")
	public Page<Store> findByMerchantCodeAndProgramCodeIgnoreCasePagination(String merchantCode, String programCode, Pageable pageNumberWithElements);

	@Query("{'programCode': {$regex:?0,$options:'i'}}")
	public Page<Store> findByProgramCodeIgnoreCasePagination(String programCode, Pageable pageNumberWithElements);
	
	public Page<Store> findAll(Pageable pageNumberWithElements);
	
	@Query("{'storeCode': ?0, 'contactPersons.emailId': ?1}")
	public Optional<Store> findByStoreCodeAndEmail(String code, String email);
	
	@Query("{'storeCode': ?0, 'merchantCode': ?1}")
	public Store findByStoreCodeAndMerchantCode(String storeCode, String merchantCode);
	
	@Query("{'storeCode': ?0, 'merchantCode': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Store findByStoreCodeAndMerchantCodeAndProgramIgnoreCase(String storeCode, String merchantCode, String program);

	@Query("{'merchantCode': ?0 , 'pin': ?1}")
	public Optional<Store> findByMerchantCodeAndPin(String merchantCode, Integer pin);

	@Query("{'storeCode': ?0, 'merchantCode': ?1, 'contactPersons.emailId': ?2}")
	public Optional<Store> findByStoreCodeAndMerchantCodeAndEmailId(String storeCode, String merchantCode,
			String emailId);
	
	@Query("{'storeCode': ?0, 'merchantCode': ?1, 'contactPersons.userName': ?2}")
	public Optional<Store> findByStoreCodeAndMerchantCodeAndUserName(String storeCode,String merchantCode,
			String userName);

	@Query("{'contactPersons.userName': {$regex:?0,$options:'i'}}")
    public Optional<Store> findByUserName(String userName);
	
    @Query("{'merchantCode':{'$in': ?0}}")
    public List<Store> findByMerchantCodes(List<String> merchantCodes);
    
    @Query("{'merchantCode':{'$in': ?0}, 'programCode': {$regex:?1,$options:'i'}}")
    public List<Store> findByMerchantCodesAndProgramCodeIgnoreCase(List<String> merchantCodes, String programCode);
    
    @Query("{'merchantCode':{'$in': ?0}}")
    public Page<Store> findByMerchantCodesPagination(List<String> merchantCodes, Pageable pageNumberWithElements);
    
    @Query("{'merchantCode':{'$in': ?0}, 'programCode': {$regex:?1,$options:'i'}}")
    public Page<Store> findByMerchantCodesPaginationAndProgramCodeIgnoreCase(List<String> merchantCodes, String programCode, Pageable pageNumberWithElements);

	@Query("{'storeCode': ?0, 'merchantCode': ?1, 'contactPersons.emailId': ?2}")
	public List<Store> findByStoreCodeAndMerchantCodeAndEmailIdList(String storeCode, String merchantCode, String emailId);

	@Query("{'storeCode': ?0, 'merchantCode': ?1}")
	public List<Store> findByStoreCodeAndMerchantCodeList(String storeCode, String merchantCode);

}
