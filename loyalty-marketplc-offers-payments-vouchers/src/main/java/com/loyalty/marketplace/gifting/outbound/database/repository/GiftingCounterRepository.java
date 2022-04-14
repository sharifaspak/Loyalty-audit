package com.loyalty.marketplace.gifting.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingCounter;

public interface GiftingCounterRepository extends MongoRepository<GiftingCounter, String> {

	List<GiftingCounter> findByGiftType(String giftType);
	
	GiftingCounter findByGiftTypeAndLevelAndAccountNumberAndMembershipCode(String giftType, String level, String accountNumber, String membershipCode);
	
	@Query("{'GiftType' : ?0, 'MembershipCode' : {$in : ?1}}")
	List<GiftingCounter> findByGiftTypeAndMembershipCode(String giftType, List<String> memberships);
	
	List<GiftingCounter> findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	
	@Query("{'GiftType' : {$in : ?0}}")
	List<GiftingCounter> findByGiftTypeIn(List<String> giftType);
	
	public List<GiftingCounter> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);

	
}
