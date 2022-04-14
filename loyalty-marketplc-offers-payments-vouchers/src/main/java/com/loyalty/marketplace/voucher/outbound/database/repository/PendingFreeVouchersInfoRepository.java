package com.loyalty.marketplace.voucher.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.voucher.outbound.database.entity.PendingFreeVouchersInfo;

public interface PendingFreeVouchersInfoRepository  extends MongoRepository<PendingFreeVouchersInfo, String>{

	public List<PendingFreeVouchersInfo> findByAccountNumberAndStatusIgnoreCaseAndVoucherTypeIgnoreCaseAndProgramCodeIgnoreCase(String account, String status, String type, String program);
	
}
