package com.loyalty.marketplace.subscription.outbound.database.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.constants.MarketplaceDBConstants;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionReportRequestDto;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.mongodb.client.result.UpdateResult;

@Component
public class SubscriptionRepositoryHelper {
	
	@Autowired
	MongoOperations mongoOperations;
	
	
	public List<Subscription> findSubscriptionStatusForAccountList(List<String> accountNumberList, String subscriptionSegment) {
		Query subscriptionForAccountsQuery = new Query();
        subscriptionForAccountsQuery.addCriteria(
            Criteria.where("AccountNumber").in(accountNumberList).and("SubscriptionSegment").is(subscriptionSegment)
            .orOperator(
                Criteria.where("Status").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get()),
                        OfferConstants.CASE_INSENSITIVE.get()),
                Criteria.where("Status").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.PARKED_STATUS.get()),
                        OfferConstants.CASE_INSENSITIVE.get()))
        );
		return mongoOperations.find(subscriptionForAccountsQuery, Subscription.class);
	}
	
	public List<Subscription> findSubscriptionForAccountList(List<String> accountNumberList) {
		Query subscriptionForAccountsQuery = new Query();
        subscriptionForAccountsQuery.addCriteria(
            Criteria.where("AccountNumber").in(accountNumberList)
            .and("Status").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get()),OfferConstants.CASE_INSENSITIVE.get())
        );
		return mongoOperations.find(subscriptionForAccountsQuery, Subscription.class);
	}
	
	public Subscription findFreeDurationForAccount(String accountNumber) {
		Query freeDurationForAccountsQuery = new Query();		
		freeDurationForAccountsQuery.addCriteria(
            Criteria.where("AccountNumber").is(accountNumber).and("FreeDuration").gt(0));
            
		return mongoOperations.findOne(freeDurationForAccountsQuery, Subscription.class);
	}
	
	public Subscription findFreeDurationForAccountAndSegment(String accountNumber, String subscriptionSegment) {
		Query freeDurationForAccountsQuery = new Query();		
		freeDurationForAccountsQuery.addCriteria(
            Criteria.where("AccountNumber").is(accountNumber).and("FreeDuration").gt(0)
            .and("SubscriptionSegment").is(subscriptionSegment));
            
		return mongoOperations.findOne(freeDurationForAccountsQuery, Subscription.class);
	}
	
	public UpdateResult cancelExpiredSubscriptions(List<String> subscriptionCatalogId, String status, Date date) {
		Query expiredSubscriptionQuery = new Query();
		expiredSubscriptionQuery.addCriteria(
			Criteria.where("SubscriptionCatalogId").in(subscriptionCatalogId)
			.and("Status").regex(ProcessValues.getRegexStartEndExpression(status),OfferConstants.CASE_INSENSITIVE.get())
			.and("EndDate").lte(date)
		);
		
		Update update = new Update();
		update.set("Status", SubscriptionManagementConstants.CANCELLED_STATUS.get());
		update.set("EndDate", new Date());
		
		return mongoOperations.updateMulti(expiredSubscriptionQuery, update, Subscription.class);
	}
	
	public SubscriptionCatalog findSubscriptionCatalogToCancelSubscription(String subscriptionCatalogId) {
		Query subscriptionForCatalogIdQuery = new Query();
		subscriptionForCatalogIdQuery.addCriteria(
            Criteria.where("Id").is(subscriptionCatalogId)
            .orOperator(
            	Criteria.where("ChargeabilityType").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get()),
            			OfferConstants.CASE_INSENSITIVE.get()),
            	Criteria.where("ChargeabilityType").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get()),
            			OfferConstants.CASE_INSENSITIVE.get()).and("Cost").is(0.0).orOperator(Criteria.where("PointsValue").is(0),Criteria.where("PointsValue").is(null))
            )
        );
		return mongoOperations.findOne(subscriptionForCatalogIdQuery, SubscriptionCatalog.class);
	}
	
	public List<Subscription> findSubscriptionToRenew(List<String> subscriptionCatalogId, Date renewDate) {
		
		Query subscriptionForRenewQuery = new Query();
		subscriptionForRenewQuery.addCriteria(
            Criteria.where("SubscriptionCatalogId").in(subscriptionCatalogId)
            .orOperator(
            		Criteria.where("Status").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get()),
                            OfferConstants.CASE_INSENSITIVE.get()).and("NextRenewalDate").lte(renewDate),
            		Criteria.where("Status").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.PARKED_STATUS.get()),
                            OfferConstants.CASE_INSENSITIVE.get()))
            .and("PaymentMethod").is(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get())
        );
		return mongoOperations.find(subscriptionForRenewQuery, Subscription.class);
	}
	
	public List<Subscription> filterSubscriptionReport(SubscriptionReportRequestDto subcriptionReportRequestDto) throws ParseException {
		Query subscriptionReportQuery = new Query();
		Criteria criteria = new Criteria();
		List<Criteria> andCriterias = new ArrayList<>();
		
		if(subcriptionReportRequestDto.getSubscriptionChannel() != null) {
			andCriterias.add(Criteria.where("SubscriptionChannel").in(subcriptionReportRequestDto.getSubscriptionChannel()));
		}
		if(subcriptionReportRequestDto.getSubscriptionCatalogId() != null) {
			andCriterias.add(Criteria.where("SubscriptionCatalogId").in(subcriptionReportRequestDto.getSubscriptionCatalogId()));
		}
		if(subcriptionReportRequestDto.getDateType() != null && subcriptionReportRequestDto.getFromDate() != null && subcriptionReportRequestDto.getToDate() != null) {
			
			Date fromDate = Utilities.changeStringToDateWithTimeFormatAndTimeZone(subcriptionReportRequestDto.getFromDate(), OfferConstants.END_DATE_TIME.get());
            Date toDate = Utilities.changeStringToDateWithTimeFormatAndTimeZone(subcriptionReportRequestDto.getToDate(), OfferConstants.END_DATE_TIME.get());
            
			//Date fromDate=new SimpleDateFormat("dd-MM-yyyy").parse(subcriptionReportRequestDto.getFromDate());
            //Date toDate=new SimpleDateFormat("dd-MM-yyyy").parse(subcriptionReportRequestDto.getToDate());
			
			andCriterias.add(Criteria.where(subcriptionReportRequestDto.getDateType()).gte(fromDate).lt(toDate));
		}
		
		criteria.andOperator(andCriterias.toArray(new Criteria[andCriterias.size()]));
		subscriptionReportQuery.addCriteria(criteria);
		
		return mongoOperations.find(subscriptionReportQuery, Subscription.class);
	}
	
	
	/**
	 * 
	 * @param id
	 * @return status to indicate subscription catalog with id already exists
	 */
	public boolean checkUniqueSubscriptionCatalog(String id) {
		
		return mongoOperations.exists(
				 query(where("_id")
				.is(id)), 
				 SubscriptionCatalog.class);
	}

	/**
	 * 
	 * @param subscriptionIdList
	 * @return list of subscription with input list of subscription id
	 */
	public List<Subscription> findSubscriptionListForIdList(List<String> subscriptionIdList) {
		List<Subscription> subscriptionList = null;
		
		if(!CollectionUtils.isEmpty(subscriptionIdList)) {
		
			Query subscriptionQuery = new Query();
			subscriptionQuery.addCriteria(Criteria.where("_id")
					.in(subscriptionIdList));
			
			subscriptionQuery.fields()
			.include("_id")
			.include("Status");
			
			subscriptionList = mongoOperations.find(subscriptionQuery, 
					Subscription.class, MarketplaceDBConstants.SUBSCRIPTION);
		}
		
		return subscriptionList;
	}

	public List<Subscription> findSubscriptionsListForAutoRenewal(List<String> subscriptionCatalogId, Date renewDate) {
		
		Query subscriptionForRenewQuery = new Query();
		subscriptionForRenewQuery.addCriteria(
            Criteria.where("SubscriptionCatalogId").in(subscriptionCatalogId)
            .orOperator(
            		Criteria.where("Status").regex(ProcessValues.getRegexStartEndExpression(SubscriptionManagementConstants.SUBSCRIBED_STATUS.get()),
                            OfferConstants.CASE_INSENSITIVE.get()).and("NextRenewalDate").lte(renewDate))
        );
		return mongoOperations.find(subscriptionForRenewQuery, Subscription.class);
	}

}
