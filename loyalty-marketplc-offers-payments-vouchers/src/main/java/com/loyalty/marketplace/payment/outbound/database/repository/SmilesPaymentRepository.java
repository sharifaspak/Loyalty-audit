package com.loyalty.marketplace.payment.outbound.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.payment.outbound.database.entity.CreditCardTransaction;

public interface SmilesPaymentRepository extends MongoRepository<CreditCardTransaction,String>{

}
