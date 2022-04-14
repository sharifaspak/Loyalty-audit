package com.loyalty.marketplace.offers.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface EligibleOfferRepository extends MongoRepository<EligibleOffers, String> {
	
}
