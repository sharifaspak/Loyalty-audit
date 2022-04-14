package com.loyalty.marketplace.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.outbound.database.entity.RetryLogs;

public interface RetryLogsRepository extends MongoRepository<RetryLogs, String>{

}
