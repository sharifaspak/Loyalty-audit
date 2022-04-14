package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface OfferTypeRepository extends MongoRepository<OfferType, String> {
	
	public OfferType findByOfferTypeId(String id);
	
	@Query("{'Description.English':{$regex:?0,$options:'i'}}")
	public OfferType findByDescriptionEnglish(String offerType);

	public List<OfferType> findByProgramCodeIgnoreCase(String programCode);
	
	@Query("{'offerTypeId': {$in : ?0}}")
	public List<OfferType> findOfferTypeByIdList(List<String> offerTypeIdList);
	
	public OfferType findByOfferTypeIdAndProgramCodeIgnoreCase(String id, String programCOde);
}
