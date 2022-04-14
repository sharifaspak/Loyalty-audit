package com.loyalty.marketplace.promote.partner.outbound.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.promote.partner.outbound.database.entity.PartnerPromoteEntity;

public interface PartnerPromoteRepository extends MongoRepository<PartnerPromoteEntity, String>{
	
	List<PartnerPromoteEntity> findByAccountNumberAndProgramCodeIgnoreCase(String accountNumber, String programCode);

	boolean existsByAccountNumberAndPatnerCode(String accountNumber, String patnerCode);
	
	PartnerPromoteEntity findByAccountNumberAndPatnerCode(String accountNumber, String patnerCode);
	
	List<PartnerPromoteEntity> findByCreatedDateBetween(Date startDate, Date endDate);
	
	List<PartnerPromoteEntity> findByProgramCodeIgnoreCaseAndCreatedDateBetween(String programCode, Date startDate, Date endDate);

}

