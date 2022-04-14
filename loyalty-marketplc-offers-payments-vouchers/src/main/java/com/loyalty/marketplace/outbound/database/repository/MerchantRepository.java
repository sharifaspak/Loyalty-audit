package com.loyalty.marketplace.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;

public interface MerchantRepository extends MongoRepository<Merchant, String> {

	public Merchant findByMerchantCode(String merchantCode);
	
	public Merchant findByMerchantCodeAndProgramIgnoreCase(String merchantCode, String program);
	
	public List<Merchant> findByPartner(String partner);
	
	public List<Merchant> findByStatusIgnoreCase(String status);
	
	@Query("{'merchantCode': ?0, 'contactPersons.emailId': ?1}")
	public Optional<Merchant> findByMerchantCodeAndEmailId(String merchantCode, String emailId);
	
	public List<Merchant> findByProgramIgnoreCase(String program);
	
}
