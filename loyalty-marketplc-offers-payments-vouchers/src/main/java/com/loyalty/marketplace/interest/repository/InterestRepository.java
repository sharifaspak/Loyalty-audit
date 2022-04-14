package com.loyalty.marketplace.interest.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.interest.outbound.entity.InterestEntity;

public interface InterestRepository extends MongoRepository<InterestEntity, String> {
	List<InterestRepository> findByIdIn(List<String> selectedInterests);
}
