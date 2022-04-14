package com.loyalty.marketplace.voucher.outbound.database.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherReconciliationData;

public interface VoucherReconciliationDataRepository extends MongoRepository<VoucherReconciliationData, String> {

	@Query("{'_id': ?0}")
	public Optional<VoucherReconciliationData> findById(String id);
}
