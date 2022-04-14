package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface AccountOfferCounterRepository extends MongoRepository<AccountOfferCounts, String>{
	
	public AccountOfferCounts findByOfferIdAndAccountNumberAndMembershipCode(String offerId, String accountNumber, String membershipCode);
	public List<AccountOfferCounts> findByAccountNumberAndMembershipCodeAndOfferIdIn(String accountNumber, String membershipCode,
			List<String> offerIdList);
	public List<AccountOfferCounts> findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	public List<AccountOfferCounts> findByAccountNumberInAndMembershipCodeInAndOfferIdIn(List<String> accountNumberList,
			List<String> membershipCodeList, List<String> offerIdList);
	public List<AccountOfferCounts> findByMembershipCodeAndAccountNumberIn(String membershipCode, List<String> accountNumberList);
    public List<AccountOfferCounts> findByMembershipCodeInAndAccountNumberIn(List<String> membershipCodeList, List<String> accountNumberList);
    public List<AccountOfferCounts> findByMemberOfferCounter(MemberOfferCounts counter);
    
}
