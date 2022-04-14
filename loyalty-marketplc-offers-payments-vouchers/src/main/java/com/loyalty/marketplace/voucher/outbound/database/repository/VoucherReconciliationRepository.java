package com.loyalty.marketplace.voucher.outbound.database.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherReconciliation;

public interface VoucherReconciliationRepository extends MongoRepository<VoucherReconciliation, String> {
	@Query("{'_id': ?0}")
	public Optional<VoucherReconciliation> findById(String id);
	
}
