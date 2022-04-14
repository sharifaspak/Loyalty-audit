package com.loyalty.marketplace.equivalentpoints.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;

public interface ConversionRateRepository extends MongoRepository<ConversionRate, String> {

	Optional<ConversionRate> findByPartnerCodeAndChannelAndProductItemAndLowValueAndHighValue(String partnerCode,
			String channel, String productItem, Double lowValue, Double highValue);

	// for points to aed and vice versa
	List<ConversionRate> findByPartnerCodeAndChannelAndProductItem(String partnerCode, String channel,
			String productItem);

	@Query("{'partnerCode' : {$in : ?0}, 'productItem':{$in : ?1}}")
	List<ConversionRate> findByPartnerCodeAndProductItem(List<String> partnerCodeList, List<String> productItem);
	
	//Loyalty as a service.
	@Query("{'partnerCode' : {$in : ?0}, 'productItem':{$in : ?1}, 'programCode':?2}")
	List<ConversionRate> findByPartnerCodeAndProductItemAndProgramCodeIgnoreCase(List<String> partnerCodeList, List<String> productItem, String programCode);

	// for points to aed
	@Query("{'partnerCode':?0, 'channel':?1, 'productItem':?2, 'pointStart':{$lte:?3},'pointEnd':{$gte:?3}}")
	List<ConversionRate> findByPartnerCodeAndChannelAndProductItemAndPoint(String partnerCode, String channel,
			String productItem, Double point);

	@Query("{'partnerCode':?0, 'channel':?1, 'productItem':?2, 'lowValue':{$lte:?3},'highValue':{$gte:?3}}")
	Optional<ConversionRate> findByPartnerCodeAndChannelAndProductItemAndCalculatedAmount(String partnerCode,
			String channel, String productItem, Double calculatedAmount);

	// for aed to points
	@Query("{'partnerCode':?0, 'channel':?1, 'productItem':?2, 'lowValue':{$lte:?3},'highValue':{$gte:?3}}")
	Optional<ConversionRate> findByPartnerCodeAndChannelAndProductItemAndAmount(String partnerCode, String channel,
			String productItem, Double amount);

	List<ConversionRate> findAll();

}
