package com.loyalty.marketplace.gifting.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.loyalty.marketplace.gifting.outbound.database.entity.Gifts;

@Repository
public interface GiftRepository extends MongoRepository<Gifts, String> {

	@Query("{'giftType': ?0, 'giftDetails.minPointValue' : {$lte:?1}, 'giftDetails.maxPointValue' : {$gte:?2}, 'isActive' : ?3}")
	public Gifts findPremiumGift(String giftType, Integer value1, Integer value2, boolean activeStatus);
	
	@Query("{'id': ?0, 'isActive' : ?1}")
	public Gifts getGiftByIdAndStatus(String id, Boolean status);
	
	@Query("{'giftType': ?0, 'isActive' : ?1}")
	public List<Gifts> getGiftByGiftTypeAndStatus(String giftType, Boolean status);
	
	@Query("{'isActive' : ?0}")
	public List<Gifts> getGiftByStatus(Boolean status);
	
//	public Gifts findByGifts_GiftDetails_SubscriptionCatalogId(String subscriptionCatalogId);
	
	@Query("{'giftDetails.subscriptionCatalogId': ?0, 'isActive' : ?1}")
	public Gifts findBySubscriptionCatalogIdAndIsActive(String subscriptionCatalogId, boolean isActive);
	
	@Query("{'giftDetails.promotionalGiftId': ?0, 'isActive' : ?1}")
	public Gifts findByPromotionalGiftIdAndIsActive(String promotionalGiftId, boolean isActive);
	

}
