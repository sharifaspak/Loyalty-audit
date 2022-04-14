package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface PurchaseRepository extends MongoRepository<PurchaseHistory, String> {
	
	@Query("{'status' : {$regex:?0,$options:'i'}, 'purchaseItem' : {$in : ?1}, 'createdDate' : {$gte : ?2}, 'accountNumber' : ?3, 'membershipCode' : ?4}")
	public List<PurchaseHistory> findSuccessfulRecordsByPurchaseItemsInSpecifiedDurationForSpecificAccount(String status, List<String> purchaseItems, Date startDate,
			String accountNumber, String membershipCode);
	@Query("{'status' : {$regex:?0,$options:'i'}, 'additionalDetails.isBirthdayOffer' : ?1, 'accountNumber' : {$in : ?2}, 'membershipCode' : {$in : ?3}, 'createdDate' : {$gte : ?4, $lte : ?5}}")
	public List<PurchaseHistory> findBirthdayOffersForAccountList(String status, boolean isBirthday, List<String> accountNumberList,
			List<String> membershipCodeList, Date startDate, Date endDate);
	public List<PurchaseHistory> findByAccountNumberAndMembershipCode(String accountNumber, String membershipCode);
	public List<PurchaseHistory> findByAccountNumberAndPromoCodeAndStatusIgnoreCase(String accountNumber, String promoCode, String status);
	public List<PurchaseHistory> findByPromoCodeAndStatusIgnoreCase(String promoCode, String status);
	public List<PurchaseHistory> findByOfferType(String offerType);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndTransactionTypeIgnoreCaseAndAccountNumberAndMembershipCodeAndCreatedDateBetween(String status, String transactionType,
			String accountNumber, String membershipCode, Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndTransactionTypeIgnoreCaseAndAccountNumberInAndMembershipCodeAndCreatedDateBetween(String status, String transactionType,
			List<String> accountNumberList, String membershipCode, Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndTransactionTypeIgnoreCaseAndMembershipCodeAndCreatedDateBetween(String status, String transactionType,
			String membershipCode, Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndTransactionTypeIgnoreCaseAndCreatedDateBetween(String status, String transactionType,
			Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndAccountNumberAndMembershipCodeAndCreatedDateBetween(String status, 
			String accountNumber, String membershipCode, Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndAccountNumberInAndMembershipCodeAndCreatedDateBetween(String status, 
			List<String> accountNumberList, String membershipCode, Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndMembershipCodeAndCreatedDateBetween(String status, 
			String membershipCode, Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndCreatedDateBetween(String status, Date fromDate, Date toDate, Sort sort);
	public List<PurchaseHistory> findByStatusIgnoreCaseAndOfferIdInAndCreatedDateBetweenAndAccountNumberAndMembershipCode(String status, List<String> offerIdList,
			Date startDate, Date endDate, String accountNumber, String membershipCode);
	public PurchaseHistory findByPurchaseItemIgnoreCaseAndSubscriptionIdAndStatusIgnoreCase(String purchaseItem, String subscriptionId, String status);
	
	public List<PurchaseHistory> findByPartnerCodeAndStatusIgnoreCaseAndStatusReasonAndCreatedDateBetween(String partnerCode, String status, String statusReason, Date fromDate, Date toDate);
	
	public List<PurchaseHistory> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);
	public PurchaseHistory findByPurchaseItemAndExtRefNo(String purchaseItem, String externalTransactionId);
	public PurchaseHistory findByPointsTransactionId(String pointsTransactionId);
	@Query("{'id' : ?0}")
	public PurchaseHistory findByMongoId(String id);
}
