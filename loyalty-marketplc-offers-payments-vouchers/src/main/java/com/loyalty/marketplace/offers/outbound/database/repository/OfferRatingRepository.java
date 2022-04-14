package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface OfferRatingRepository extends MongoRepository<OfferRating, String> {
	
	public OfferRating findByOfferId(String offerId);

	@Query("{'memberRatings' : {$elemMatch : {'accountNumber' : ?0, 'membershipCOde' : ?1 }}}")
	public List<OfferRating> findListByAccountNumberAndMembershipCode(String oldAccountNumber, String membershipCode);

	@Query("{'memberRatings' : {$elemMatch : {'membershipCode' : ?0, 'accountNumber' : {$in : ?1} }}}")
	public List<OfferRating> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);
}
