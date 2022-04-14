package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.outbound.database.entity.OfferCounter;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface OfferCountersRepository extends MongoRepository<OfferCounter, String>{
	
	public OfferCounter findByOfferId(String offerId);

	@Query("{$or : [{'offerId' : {$in :  ?0}}, {'rules' : {$in : ?1}}]}")
	public List<OfferCounter> findByOfferIdList(List<String> offerIdList, List<String> rules);
	
	@Query("{'accountOfferCount' : {$elemMatch : {'accountNumber' : ?0, 'membershipCode' : ?1 }}}")
	public List<OfferCounter> findCounterWithAccountNumberAndMembershipCode(String account,
			String membershipCode);
	
}
