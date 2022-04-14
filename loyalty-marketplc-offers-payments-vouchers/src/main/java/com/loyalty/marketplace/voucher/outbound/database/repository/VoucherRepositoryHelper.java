package com.loyalty.marketplace.voucher.outbound.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherStatus;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.mongodb.client.result.UpdateResult;

@Component
public class VoucherRepositoryHelper {
	
	@Autowired
	MongoOperations mongoOperations;
	
	
	public List<Voucher> findVoucherForAccountList(List<String> accountNumberList) {
		Query voucherForAccountsQuery = new Query();
		voucherForAccountsQuery.addCriteria(
			Criteria.where("AccountNumber").in(accountNumberList)
			.and("Status").regex(ProcessValues.getRegexStartEndExpression(VoucherConstants.ACTIVE),	OfferConstants.CASE_INSENSITIVE.get())
			.and("ExpiryDate").gte(new Date())
		);
		return mongoOperations.find(voucherForAccountsQuery, Voucher.class);	
	}
	
	public UpdateResult updateExpiredVouchers(Date date) {
		Query expiredVoucherQuery = new Query();
		expiredVoucherQuery.addCriteria(
			Criteria.where("ExpiryDate").lt(date)
			.and("Status").not().regex(ProcessValues.getRegexStartEndExpression(VoucherConstants.BURNT), OfferConstants.CASE_INSENSITIVE.get())
		);
		
		Update update = new Update();
		update.set("Status", VoucherStatus.EXPIRED);
		
		return mongoOperations.updateMulti(expiredVoucherQuery, update, Voucher.class);
	}
	
	public UpdateResult updateCashVoucherBurntInfo(Voucher voucher, Headers headers) {						
		Query cashVoucherBurntQuery = new Query();
		cashVoucherBurntQuery.addCriteria(
			Criteria.where("_id").is(voucher.getId()));
		
		Update update = new Update();		
		update.set("CashVoucherBurntInfo", voucher.getCashVoucherBurntInfo()).set("VoucherBalance", voucher.getVoucherBalance())
		.set("UpdatedDate", new Date()).set("UpdatedUser", headers.getUserName());
		
		if(null != voucher.getBurntInfo()) {
			update.set("BurntInfo", voucher.getBurntInfo());
			update.set("Status","active");
		}
		
		return mongoOperations.updateFirst(cashVoucherBurntQuery, update, Voucher.class);
	}
	

}
