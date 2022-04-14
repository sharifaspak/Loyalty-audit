package com.loyalty.marketplace.interest.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.interest.outbound.entity.CustomerInterestEntity;

public interface CustomerInterest extends MongoRepository<CustomerInterestEntity, String> {
	Optional<CustomerInterestEntity> findByAccountNumber(String accountNumber); 
	
	Optional<CustomerInterestEntity> findByAccountNumberAndProgramCodeIgnoreCase(String accountNumber, String programCode); 
}
