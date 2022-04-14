package com.loyalty.marketplace.subscription.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPayment;

public interface SubscriptionPaymentRepository extends MongoRepository<SubscriptionPayment, String>{
	
	public SubscriptionPayment findBySubscriptionIdAndPaymentMethodDetails_Status(String subscriptionId, String status);
	
	public List<SubscriptionPayment> findBySubscriptionIdInAndPaymentMethodDetails_Status(List<String> subscriptionId, String status);
	
	public SubscriptionPayment findBySubscriptionId(String subscriptionId);
	
}
