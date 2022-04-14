package com.loyalty.marketplace.merchants.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;

public interface RateTypeRepository extends MongoRepository<RateType, String> {

	public RateType findByTypeRate(String typeRate);

	@Query(value = "{'ProgramCode': {$regex:?0,$options:'i'}}")
	public List<RateType> findByProgramIgnoreCase(String program);
	
}
