package com.loyalty.marketplace.outbound.jms.events.database;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface JmsRepository extends MongoRepository<JmsMessages, String> {

	boolean existsByCorrelationId(String correlationId);
	
	JmsMessages findByCorrelationId(String correlationId);

	void deleteByCorrelationId(String correlationId);
	
	public List<JmsMessages> findByStatus(String status);
	
	@Query("{'correlationId' : {$in : ?0}}") 
	List<JmsMessages> findByCorrelationIdList(List<String> correlationIds);

}
