package com.loyalty.marketplace.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.database.entity.ExceptionLogs;

public interface ExceptionLogsRepository extends MongoRepository<ExceptionLogs, String> {

}
