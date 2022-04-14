package com.loyalty.marketplace.subscription.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;

public interface SubscriptionRepository extends MongoRepository<Subscription, String>{
	
	public Optional<Subscription> findById(String id);
	
	public Optional<Subscription> findByIdAndStatus(String id, String status);
	
	public Optional<Subscription> findByIdAndStatusAndProgramCodeIgnoreCase(String id, String status, String program);
	
	public Optional<Subscription> findByIdAndProgramCodeIgnoreCase(String id, String program);
	
	public Optional<Subscription> findBySubscriptionCatalogId(String subscriptionCatalogId);
	
	public Optional<Subscription> findByIdAndAccountNumber(String id, String accountNumber);
	
	public Optional<Subscription> findBySubscriptionCatalogIdAndAccountNumber(String subscriptionCatalogId, String accountNumber);
	
	public Optional<Subscription> findBySubscriptionCatalogIdAndAccountNumberAndStatus(String subscriptionCatalogId, String accountNumber, String status);
	
	public List<Subscription> findByStatus(String status);
	
	public List<Subscription> findByAccountNumberAndStatusIn(String accountNumber, List<String> status);
	
	public Optional<Subscription> findByAccountNumberAndStatusAndSubscriptionSegmentNot(String accountNumber, String status, String segment);
	
	public List<Subscription> findByAccountNumber(String accountNumber);
	
	public List<Subscription> findByAccountNumberAndStatusAndProgramCodeIgnoreCase(String accountNumber, String status, String program);
	
	public List<Subscription> findByAccountNumberIn(List<String> accountNumber);
	
	public List<Subscription> findByAccountNumberAndProgramCodeIgnoreCase(String accountNumber, String program);
	
	public Optional<Subscription> findBySubscriptionCatalogIdAndAccountNumberAndStatusIgnoreCase(String subscriptionCatalogId, String accountNumber, String status);

	public Subscription findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	
	public List<Subscription> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);
	
	public List<Subscription> findByMembershipCodeAndStatus(String membershipCode,String status);
	
	public List<Subscription> findByMembershipCodeAndSubscriptionCatalogIdAndStatusIn(String membershipCode, String subscriptionCatalogId, List<String> status);
	
	public List<Subscription> findByAccountNumberAndSubscriptionCatalogIdAndStatusIn(String accountNumber, String subscriptionCatalogId, List<String> status);

	public Optional<Subscription> findByAccountNumberAndStatusInAndSubscriptionSegment(String accountNumber, List<String> status, String segment);
}
