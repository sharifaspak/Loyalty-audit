package com.loyalty.marketplace.gifting.outbound.database.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingLimit;

public interface GiftingLimitRepository extends MongoRepository<GiftingLimit, String> {

	Optional<GiftingLimit> findById(String id);
	
	GiftingLimit findByGiftType(String giftType);
	
}
