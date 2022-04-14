package com.loyalty.marketplace.subscription.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPromotion;

public interface SubscriptionPromotionRepository extends MongoRepository<SubscriptionPromotion, String>{
	
}
