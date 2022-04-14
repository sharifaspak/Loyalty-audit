package com.loyalty.marketplace.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

public interface PaymentMethodRepository extends MongoRepository<PaymentMethod, String> {
	
	public PaymentMethod findByPaymentMethodId(String paymentMethodId); 
	public PaymentMethod findByDescription(String paymentMethodDescription);
	@Query("{'description' : {$in : ?0}}")
	public List<PaymentMethod> findAllPaymentMethodsInSpecifiedList(List<String> methodNames);
	public List<PaymentMethod> findByProgramCodeIgnoreCase(String program);
	@Query("{'paymentMethodId' : {$in : ?0}}")
	public List<PaymentMethod> findAllPaymentMethodsInSpecifiedListOfIds(List<String> paymentMethodIds);
}
