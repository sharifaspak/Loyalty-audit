package com.loyalty.marketplace.outbound.database.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.outbound.database.entity.Denomination;

public interface DenominationRepository extends MongoRepository<Denomination, String> {
	
	@Query("{'Value.Cost' : {$in : ?0}}")
	public List<Denomination> findAllByDirhamValue(List<Integer> dirhamValueList);

	@Query("{'ProgramCode' : {$regex:?0,$options:'i'}}")
	public List<Denomination> findByProgramCodeIgnoreCase(String programCode, Sort sort);
	
}
