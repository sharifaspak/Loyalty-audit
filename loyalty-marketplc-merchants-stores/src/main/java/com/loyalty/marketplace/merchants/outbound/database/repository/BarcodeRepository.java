package com.loyalty.marketplace.merchants.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.merchants.outbound.database.entity.Barcode;

public interface BarcodeRepository extends MongoRepository<Barcode,String> {
	
	List<Barcode> findByProgram(String programCode);
	
}
