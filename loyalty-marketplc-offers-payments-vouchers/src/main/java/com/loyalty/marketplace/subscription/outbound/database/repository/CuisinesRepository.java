package com.loyalty.marketplace.subscription.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.subscription.outbound.database.entity.Cuisines;

public interface CuisinesRepository extends MongoRepository<Cuisines, String>{

}
