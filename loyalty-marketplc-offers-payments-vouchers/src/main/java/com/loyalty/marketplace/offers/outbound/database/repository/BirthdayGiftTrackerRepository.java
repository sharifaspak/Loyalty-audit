package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface BirthdayGiftTrackerRepository extends MongoRepository<BirthdayGiftTracker, String>{

	BirthdayGiftTracker findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	
	//Loyalty as a service.
	BirthdayGiftTracker findByAccountNumberAndMembershipCodeAndProgramCodeIgnoreCase(String accountNumber, String membershipCode, String programCode);
	
	List<BirthdayGiftTracker> findByAccountNumberInAndMembershipCodeIn(List<String> accountNumberList,
			List<String> membershipCodeList);
	
	public List<BirthdayGiftTracker> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);


}
