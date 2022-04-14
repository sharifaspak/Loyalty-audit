package com.loyalty.marketplace.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;

public interface StoreRepository extends MongoRepository<Store, String> {
 
	public Store findByStoreCode(String code);
	
	@Query("{'storeCode': ?0, 'merchantCode': ?1}")
	public Store findByStoreCodeAndMerchantCode(String storeCode, String merchantCode);
	
	@Query("{'storeCode': ?0, 'merchantCode': ?1, 'programCode': ?2}")
	public Store findByStoreCodeAndMerchantCodeAndProgramCode(String storeCode, String merchantCode, String programCode);

	@Query("{'storeCode': {$in : ?0}, 'merchantCode': ?1}")
	public List<Store> findAllByStoreCodeAndMerchantCode(List<String> storeCodes, String merchantCode);
	
	@Query("{'contactPersons.pin': ?0}")
	public Store findByPin(Integer storePin);
	
	@Query("{'contactPersons.pin': ?0, 'merchantCode': ?1}")
	public Store findByPinAndMerchantCode(Integer storePin, String merchantCode);
	
	@Query("{'contactPersons.userName': {$regex:?0,$options:'i'}}")
	public Store findByUserName(String userName);
	
	@Query("{'contactPersons.userName': {$regex:?0,$options:'i'}, 'programCode': ?1}")
	public Store findByUserNameAndProgram(String userName, String program);
	
	public List<Store> findByMerchantCodeInAndStatusIgnoreCase(List<String> merchantCodes, String status);
	
	public List<Store> findByIdInAndStatusIgnoreCase(List<String> merchantCodes, String status);
	
}
