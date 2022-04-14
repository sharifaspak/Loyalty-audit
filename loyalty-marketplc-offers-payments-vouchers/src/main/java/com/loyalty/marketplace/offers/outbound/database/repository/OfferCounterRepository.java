package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounters;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface OfferCounterRepository extends MongoRepository<OfferCounters, String>{
	
	public List<OfferCounters> findByRulesIn(List<String> rules);

	public OfferCounters findByOfferId(String offerId);

	@Query("{$or : [{'rules' : {$in : ?0}}, {'offerId' : {$in :  ?1}}]}")
	public List<OfferCounters> findByRulesInOrOfferIdIn(List<String> rules, List<String> offerIdList);

	public List<OfferCounters> findByOfferIdIn(List<String> offerIdList);
	
		
}
