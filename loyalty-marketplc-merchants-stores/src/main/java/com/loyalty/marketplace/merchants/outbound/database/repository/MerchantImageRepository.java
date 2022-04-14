package com.loyalty.marketplace.merchants.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.merchants.outbound.database.entity.MerchantImage;

public interface MerchantImageRepository extends MongoRepository<MerchantImage, String> {

	@Query("{'domain': ?0, 'domainId': ?1}")
	public List<MerchantImage> findByDomainAndDomainId(String domain, String domainId);
	
	@Query("{'domain': ?0}")
	public List<MerchantImage> findByDomain(String domain);
	
}
