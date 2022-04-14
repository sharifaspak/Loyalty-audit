package com.loyalty.marketplace.payment.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.payment.outbound.database.entity.EPGTransaction;

public interface EPGTransactionRepository extends MongoRepository<EPGTransaction, String> {
	
	List<EPGTransaction> findByExternalTransactionId(String id);

}
