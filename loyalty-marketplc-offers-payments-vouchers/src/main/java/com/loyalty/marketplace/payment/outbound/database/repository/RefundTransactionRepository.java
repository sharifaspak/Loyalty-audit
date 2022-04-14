package com.loyalty.marketplace.payment.outbound.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.payment.outbound.database.entity.RefundTransaction;

public interface RefundTransactionRepository extends MongoRepository<RefundTransaction, String>{

	//TODO: Test the query!
    @Query("{'epgRerversalCode' : {$in:?0, $ne : ?2},'epgRefundCode':{$ne : ?1}, 'reprocessedFlag':{$eq: ?3}"
    		+ "'createdDate':{$not:{$lt:?4}}}")
	public List<RefundTransaction> findAllFailedTransactions(List<String> epgReversalCode, String refundCode, String reversalCode, boolean reprocessedFlag, Date today);
}
