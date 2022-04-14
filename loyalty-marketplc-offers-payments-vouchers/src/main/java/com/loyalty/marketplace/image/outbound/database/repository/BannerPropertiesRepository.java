package com.loyalty.marketplace.image.outbound.database.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.image.outbound.database.entity.BannerProperties;

public interface BannerPropertiesRepository extends MongoRepository<BannerProperties, String> {

	Optional<BannerProperties> findById(String id);
	
}
