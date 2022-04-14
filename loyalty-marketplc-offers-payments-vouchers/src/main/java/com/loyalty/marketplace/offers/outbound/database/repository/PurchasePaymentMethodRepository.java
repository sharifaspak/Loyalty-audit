package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface PurchasePaymentMethodRepository extends MongoRepository<PurchasePaymentMethod, String> {
	
	public PurchasePaymentMethod findByPurchaseItem(String purchaseItem);
	
	@Query("{'purchaseItem' : {$in : ?0}}")
	public List<PurchasePaymentMethod> findByPurchaseItemList(List<String> purchaseItemList);
	
	@Query("{'purchaseItem' : {$in : ?0}, 'programCode' : {$regex:?1,$options:'i'}}")
	public List<PurchasePaymentMethod> findByPurchaseItemListAndProgramCodeIgnoreCase(List<String> purchaseItemList, String program);
	
}
