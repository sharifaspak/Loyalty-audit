package com.loyalty.marketplace.gifting.outbound.database.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;

public interface GiftingHistoryRepository extends MongoRepository<GiftingHistory, String> {

	Optional<GiftingHistory> findById(String id);
	
	Optional<GiftingHistory> findByIdAndProgramCodeIgnoreCase(String id, String programCode);
	
	@Query("{'SenderInfo.AccountNumber' : ?0, 'SenderInfo.MembershipCode' : ?1, 'GiftType' : ?2}")
	List<GiftingHistory> findBySenderAccountNumberAndMembershipCodeAndGiftType(String accountNumber, String membershipCode, String giftType, Sort sort);
	
	@Query("{'ReceiverInfo.AccountNumber' : ?0, 'ReceiverInfo.MembershipCode' : ?1, 'GiftType' : ?2, '$and' : [{'ScheduledDate':{'$lte':?3}}]}")
	List<GiftingHistory> findByReceiverAccountNumberAndMembershipCodeAndGiftType(String accountNumber, String membershipCode, String giftType, Date date, Sort sort);
	
	@Query("{'$or' : [ {'$and' : [{'SenderInfo.AccountNumber': '?0'}, {'SenderInfo.MembershipCode':'?1'}]}, {'$and' : [{'ReceiverInfo.AccountNumber': '?0'}, {'ReceiverInfo.MembershipCode':'?1'}]} ], 'GiftType' : ?2}")
	List<GiftingHistory> findByAllAccountNumberAndMembershipCodeAndGiftType(String accountNumber, String membershipCode, String giftType, Sort sort);
	
	@Query("{'GiftType' : ?0}")
	List<GiftingHistory> findByGiftType(String giftType);
	
	@Query("{'$or' : [ {'$and' : [{'SenderInfo.AccountNumber': '?0'}, {'SenderInfo.MembershipCode':'?1'}]}, {'$and' : [{'ReceiverInfo.AccountNumber': '?0'}, {'ReceiverInfo.MembershipCode':'?1'}]} ]}")
	List<GiftingHistory> findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	
	@Query("{'_id' : {$in : ?0}}") 
	List<GiftingHistory> findByGiftIdList(List<String> giftIdList);

	@Query("{'$or' : [ {'$and' : [{'SenderInfo.AccountNumber': {$in : ?0}}, {'SenderInfo.MembershipCode':'?1'}]}, {'$and' : [{'ReceiverInfo.AccountNumber': {$in : ?0}}, {'ReceiverInfo.MembershipCode':'?1'}]} ]}")
	List<GiftingHistory> findByAccountNumberInAndMembershipCode(List<String>accountNumber, String membershipCode);
	
	void deleteById(String id);
	
}