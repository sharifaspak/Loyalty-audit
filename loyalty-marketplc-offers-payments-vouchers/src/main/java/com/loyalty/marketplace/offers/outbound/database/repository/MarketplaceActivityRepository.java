package com.loyalty.marketplace.offers.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.outbound.database.entity.MarketplaceActivity;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface MarketplaceActivityRepository extends MongoRepository<MarketplaceActivity, String> {
	
	public MarketplaceActivity findByActivityCode(String activityCode);
	
}
