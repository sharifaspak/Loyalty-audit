package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.outbound.database.entity.WishlistEntity;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface WishlistRepository  extends MongoRepository<WishlistEntity, String>{
	
	public WishlistEntity findByAccountNumberAndMembershipCode(String accounNumber, String membershipCode);
	
	public List<WishlistEntity> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);

}
