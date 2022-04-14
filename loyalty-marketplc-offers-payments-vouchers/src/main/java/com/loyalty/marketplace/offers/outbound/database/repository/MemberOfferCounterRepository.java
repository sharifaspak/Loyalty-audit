package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface MemberOfferCounterRepository extends MongoRepository<MemberOfferCounts, String>{

	public List<MemberOfferCounts> findByMembershipCodeAndOfferIdIn(String membershipCode, List<String> offerIdList);
	public MemberOfferCounts findByMembershipCodeAndOfferId(String membershipCode, String offerId);
	public List<MemberOfferCounts> findByMembershipCodeInAndOfferIdIn(List<String> membershipCodeList, List<String> offerIdList);
	public List<MemberOfferCounts> findByMembershipCodeIn(List<String> membershipCodeList);

}
