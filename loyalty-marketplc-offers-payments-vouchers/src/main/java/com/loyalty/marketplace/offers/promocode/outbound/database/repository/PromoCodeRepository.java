package com.loyalty.marketplace.offers.promocode.outbound.database.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoCode;

public interface PromoCodeRepository extends MongoRepository<PromoCode, String>{

	List<PromoCode> findByPromoCode(String promoCode);
	
	List<PromoCode> findBySubcscriptionsIn(List<String> subscription);
	
	List<PromoCode> findByPartnerAndPartnerRef(String partner, String partnerRef);
}
