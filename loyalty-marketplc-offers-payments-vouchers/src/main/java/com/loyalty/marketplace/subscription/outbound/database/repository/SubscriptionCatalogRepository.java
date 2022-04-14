package com.loyalty.marketplace.subscription.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;

public interface SubscriptionCatalogRepository extends MongoRepository<SubscriptionCatalog, String>{
	
	public Optional<SubscriptionCatalog> findByIdAndStatus(String id, String status);
	
	public Optional<SubscriptionCatalog> findByIdAndStatusAndProgramCodeIgnoreCase(String id, String status, String program);
	
	public List<SubscriptionCatalog> findByStatus(String status);
	
	public List<SubscriptionCatalog> findByStatusAndProgramCodeIgnoreCase(String status, String program);
	
	public Optional<SubscriptionCatalog> findByIdAndChargeabilityType(String id, String chargeabilityType);
	
	public Optional<SubscriptionCatalog> findBySubscriptionTitle_SubscriptionTitleEn(String subscriptionTitle);
	
	public List<SubscriptionCatalog> findByCustomerSegmentsAndStatus(String customerSegment, String status);
	
	public List<SubscriptionCatalog> findByProgramCodeIgnoreCase(String program);
	
	public List<SubscriptionCatalog> findByIdAndProgramCodeIgnoreCase(String id, String program);

	public List<SubscriptionCatalog> findByChargeabilityTypeAndStatus(String chargeabilityType, String status);
	
	public List<SubscriptionCatalog> findByValidityPeriodAndStatus(Integer validityPeriod, String status);
	
	public List<SubscriptionCatalog> findByChargeabilityType(String cargeabilityType);
	
	public Optional<SubscriptionCatalog> findByPackageType(String packageType);
	
	public List<SubscriptionCatalog> findByIdIn(List<String> id);
	
}
