package com.loyalty.marketplace.gifting.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.gifting.outbound.database.entity.BirthdayReminder;

public interface BirthdayReminderRepository extends MongoRepository<BirthdayReminder, String> {

	BirthdayReminder findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	
	@Query("{ 'ReminderList': { $elemMatch: { 'AccountNumber' : ?0 } }, 'ReminderList': { $elemMatch: { 'MembershipCode' : ?1 } } }")
	List<BirthdayReminder> findByReceiverAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	

	
	@Query("{'$or' : [ {'reminderList' : {$elemMatch : {'membershipCode' : ?0, 'accountNumber' : {$in : ?1} }}}, {'$and' : [{'membershipCode' : ?0}, {'accountNumber' : {$in : ?1}}]} ]}")
	public List<BirthdayReminder> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);


	
}
