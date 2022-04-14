package com.loyalty.marketplace.offers.promocode.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PartnerPromoCode;

public interface PartnerPromoCodeRepository extends MongoRepository<PartnerPromoCode, String>{

}
