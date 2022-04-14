package com.loyalty.marketplace.voucher.outbound.database.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherAction;

public interface VoucherActionRepository  extends MongoRepository<VoucherAction, String>{

	public Optional<VoucherAction> findByRedemptionMethod(String redemptionMethod);
	
}
