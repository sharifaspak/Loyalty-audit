package com.loyalty.marketplace.payment.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.payment.outbound.database.entity.ThirdPartyCallLog;

public interface ThirdPartyCallLogRepository extends MongoRepository<ThirdPartyCallLog, String> {

}
