package com.loyalty.marketplace.offers.error.handler;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author jaya.shukla
 *
 */
@Repository
public interface ErrorRepository extends MongoRepository<ErrorRecords, String> {

	List<ErrorRecords> findByOfferIdAndStatusIgnoreCase(String offerId, String string);
	@Query("{$or : [{'offerId' : {$in :  ?0}, 'accountNumber' : ?1, 'membershipCode' : ?2}, {'rules' : {$in : ?3}}]}")
	List<ErrorRecords> findByCinemaOffersOrCurrentOffer(List<String> offerIdList, String accountNumber, String membershipCode, List<String> rules);
	@Query("{$or : [{'offerId' : ?0}, {'offerId': ?0, 'accountNumber' : ?1, 'membershipCode' : ?2}, {'offerId' : ?0, 'membershipCode' : ?2}]}")
	List<ErrorRecords> findByOfferIdAndAccountNumberAndMembershipCode(String offerId, String accountNumber, String membershipCode);
}