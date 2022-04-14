package com.loyalty.marketplace.gifting.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificate;

public interface GoldCertificateRepository extends MongoRepository<GoldCertificate, String> {
	
	public GoldCertificate findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	
	public List<GoldCertificate> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);

}
