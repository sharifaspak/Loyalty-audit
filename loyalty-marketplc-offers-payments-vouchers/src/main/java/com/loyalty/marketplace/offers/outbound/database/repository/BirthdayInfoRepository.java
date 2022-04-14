package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayInfo;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface BirthdayInfoRepository extends MongoRepository<BirthdayInfo, String>{
	
	public List<BirthdayInfo> findByProgramCodeIgnoreCase(String programCode);

}
