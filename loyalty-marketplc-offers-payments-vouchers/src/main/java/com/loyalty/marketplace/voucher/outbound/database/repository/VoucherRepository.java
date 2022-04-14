package com.loyalty.marketplace.voucher.outbound.database.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;

public interface VoucherRepository extends MongoRepository<Voucher, String> {

	public Voucher findByVoucherCode(String voucherCode);
	
	public Voucher findByVoucherCodeAndProgramCodeIgnoreCase(String voucherCode, String programCode);
	
	public List<Voucher> findByVoucherCodeIn(List<String> voucherCodeList);
	
	public Optional<Voucher> findById(String voucherId);

	@Query("{'uuid.id': ?0}")
	public List<Voucher> findByUuid(String uuid);
	
	@Query(value = "{'MerchantCode': ?0, 'OfferInfo.OfferId': ?1, 'BulkId':{$ne:null},'Status': ?4, 'VoucherType': ?5, '$and':[{ '$or':[{'StartDate':{'$lte':?3}},{'StartDate':null}]},{'$or':[{'ExpiryDate':{'$gte':?2}},{'ExpiryDate':null}]}] }")
	public List<Voucher> findByOfferId(String merchantCode, String offerId, Date todayBeginning, Date todayEnd, String status, String type, Pageable pageable);

	@Query(value = "{'MerchantCode': ?0, 'VoucherAmount': ?1, 'OfferInfo.OfferId': ?2, 'BulkId':{$ne:null},'Status': ?5, 'VoucherType': ?6, '$and':[{ '$or':[{'StartDate':{'$lte':?4}},{'StartDate':null}]},{'$or':[{'ExpiryDate':{'$gte':?3}},{'ExpiryDate':null}]}] }")
	public List<Voucher> findByOfferIdCashVoucher(String merchantCode, Double voucherAmount, String offerId, Date todayBeginning, Date todayEnd, String status, String type, Pageable pageable);
	
	@Query(value = "{'MerchantCode': ?0, 'OfferInfo.OfferId': ?1, 'OfferInfo.SubOfferId': ?2, 'BulkId':{$ne:null},'Status': ?5, 'VoucherType': ?6, '$and':[{'$or':[{'StartDate':{'$lte': ?4}}, {'StartDate':null}]}, {'$or':[{'ExpiryDate':{'$gte': ?3}},{'ExpiryDate':null}]}] }")
	public List<Voucher> findBySubOfferId(String merchantCode, String offerId, String subOfferId,
			Date todayBeginning, Date todayEnd, String status, String type, Pageable pageable);
	
	@Query("{'accountNumber': ?0, 'status': ?1}")
	public List<Voucher> findByAccountNumberAndStatus(String accountNumber, String status);
	
	@Query("{'voucherCode': ?0, 'status': ?1}")
	public Voucher findByVoucherCodeAndStatus(String voucherCode, String status);
	
	@Query("{'accountNumber': ?0}")
	public List<Voucher> findByAccountNumber(String accountNumber);
	
	@Query("{'voucherCode': ?0, 'merchantCode': ?1, 'status': ?2}")
	public Voucher findByCodeAndMerchantCodeAndStatus(String voucherCode, String merchantCode, String active);

	@Query("{'voucherCode': ?0, 'status': ?1, '$and' : [{'expiryDate':{'$gte':?2}}] }")
	public Voucher findByCodeAndStatusAndExpiryDate(String voucherCode, String active, Date date);
	
	@Query("{'accountNumber': ?0, 'status': ?1, '$and' : [{'expiryDate':{'$gte':?2}}] }")
	public List<Voucher> findByAccountNumberAndStatusAndExpiryDate(String accountNumber, String active, Date date);
	
	@Query("{'MerchantCode': ?0, 'BurntInfo.VoucherBurntUser': ?1, 'Status': ?2, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?3}}, {'BurntInfo.VoucherBurntDate':{'$lte':?4}}]}] }")
	public List<Voucher> findByCodeAndMerchantCodeAndStatusAndFromDate(String merchantCode, String userName, String burnt,Date fromDate,Date toDate);
	
	@Query("{'Status': ?0, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?1}}, {'BurntInfo.VoucherBurntDate':{'$lte':?2}}]}] }")
	public List<Voucher> findAdminBurntStatusAndDate(String burnt,Date fromDate,Date toDate);
	
	@Query("{'Status': ?0, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?1}}, {'BurntInfo.VoucherBurntDate':{'$lte':?2}}]}], 'ProgramCode': {$regex:?3,$options:'i'} }")
	public List<Voucher> findAdminBurntStatusAndDateAndProgramCodeIgnoreCase(String burnt,Date fromDate,Date toDate, String programCode);
	
	@Query("{'MerchantCode': ?0, 'Status': ?1, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?2}}, {'BurntInfo.VoucherBurntDate':{'$lte':?3}}]}], 'ProgramCode': {$regex:?4,$options:'i'} }")
	public List<Voucher> findByMerchantCodeAndBurntStatusAndDateAndProgramCodeIgnoreCase(String merchantCode, String burnt,Date fromDate,Date toDate, String program);
	
	@Query("{'MerchantCode': ?0, 'Status': ?1, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?2}}, {'BurntInfo.VoucherBurntDate':{'$lte':?3}}]}] }")
	public List<Voucher> findByMerchantCodeAndBurntStatusAndDate(String merchantCode, String burnt,Date fromDate,Date toDate);
	
	@Query("{'PartnerCode': ?0, 'Status': ?1, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?2}}, {'BurntInfo.VoucherBurntDate':{'$lte':?3}}]}] }")
	public List<Voucher> findByPartnerCodeAndBurntStatusAndDate(String partnerCode, String burnt,Date fromDate,Date toDate);
	
	@Query("{'PartnerCode': ?0, 'Status': ?1, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?2}}, {'BurntInfo.VoucherBurntDate':{'$lte':?3}}]}], 'ProgramCode': {$regex:?4,$options:'i'} }")
	public List<Voucher> findByPartnerCodeAndBurntStatusAndDateAndProgramCodeIgnoreCase(String partnerCode, String burnt,Date fromDate,Date toDate, String programCode);
	
	@Query("{'BurntInfo.StoreCode': ?0, 'Status': ?1, 'BurntInfo.VoucherBurntUser':{$regex:?2,$options:'i'}, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?3}}, {'BurntInfo.VoucherBurntDate':{'$lte':?4}}]}] }")
	public List<Voucher> findByStoreCodeAndBurntStatusAndDate(String storeCode, String burnt,String userName, Date fromDate,Date toDate);
	
	@Query("{'BurntInfo.StoreCode': ?0, 'Status': ?1, 'BurntInfo.VoucherBurntUser':{$regex:?2,$options:'i'}, '$and' : [{ '$and' :[{'BurntInfo.VoucherBurntDate':{'$gte':?3}}, {'BurntInfo.VoucherBurntDate':{'$lte':?4}}]}], 'ProgramCode': {$regex:?5,$options:'i'} }")
	public List<Voucher> findByStoreCodeAndBurntStatusAndDateAndProgramCodeIgnoreCase(String storeCode, String burnt,String userName, Date fromDate,Date toDate, String programCode);
	
	@Query("{'partnerCode': ?0}")
	public List<Voucher> findByPartnerCode(String partnerCode, Sort sort);
	
	public List<Voucher> findByVoucherCodeIn(List<String> voucherCode, Sort sort);
	
	public List<Voucher> findByMerchantCode(String merchantCode, Sort sort);
	
	public List<Voucher> findByPartnerCodeAndMerchantCode(String partnerCode, String merchantCode, Sort sort);	
	
	@Query("{'voucherCode': ?0, 'merchantCode': ?1, 'status': ?2, '$and' : [{'expiryDate':{'$gte':?3}}]}")
	public Voucher findByCodeAndMerchantCodeAndStatusAndDate(String voucherCode, String merchantCode, String active, Date date);

	@Query("{'voucherCode': ?0, 'partnerCode':{$regex:?1,$options:'i'}, 'status': ?2, '$and' : [{'expiryDate':{'$gte':?3}}]}")
	public Voucher findByCodeAndPartnerCodeAndStatusAndDate(String voucherCode, String partnerCode, String active, Date date);

	@Query("{'voucherCode': ?0, 'status': ?1, '$and' : [{'expiryDate':{'$gte':?2}}]}")
	public Voucher findByCodeAndStatusAndDate(String voucherCode, String active, Date date);

	@Query("{'voucherCode': ?0, 'accountNumber': ?1, 'status': ?2, '$and' : [{'expiryDate':{'$gte':?3}}] }")
	public Voucher findByCodeAndAccountNumberAndStatusAndExpiryDate(String voucherCode, String accountNumber,
			String active, Date date);
	
	@Query("{'partnerCode':?0, '$and' : [{'createdDate':{'$gte':?1}},{'createdDate':{'$lt':?2}}]} ")
	public List<Voucher> findByPartnerCodeAndCreatedDate(String partnerCode, Date startDate, Date endDate);
	
	@Query("{'status': ?0, '$and' : [{'expiryDate':{'$lte':?1}}]}")
	public List<Voucher> findByStatusAndExpiryDate(String status, Date expiryDate);
	
	public List<Voucher> findByStatus(String status);
	
	@Query("{'bulkId':{$ne:null}}")
	public List<Voucher> findByBulkId();
	
	@Query("{'merchantCode': ?0, 'bulkId':{$ne:null}}")
	public List<Voucher> findByMerchantCodeAndBulkId(String merchantCode);
	
	@Query("{'merchantCode': ?0, 'status': ?1, 'bulkId':{$ne:null}}")
	public List<Voucher> findByMerchantCodeAndStatusAndBulkId(String merchantCode, String status);

	@Query("{'voucherCode': ?0, 'status': ?1, 'accountNumber': ?2, '$or' : [{'GiftDetails.IsGift': 'NO'},{'GiftDetails':null}], '$and' : [{'expiryDate':{'$gte':?3}}] }")
	public Voucher findByCodeAndStatusAndExpiryDateNotGift(String voucherCode,  String active, String senderAccNumber, Date date);
	
	@Query("{'merchantCode': ?0, 'OfferInfo.OfferId': ?1, 'bulkId':{$ne:null}}")
	public List<Voucher> findByMerchantCodeAndOfferIdAndBulkId(String merchantCode, String offerId);
	
	@Query("{'merchantCode': ?0, 'status': ?1, 'OfferInfo.OfferId': ?2, 'bulkId':{$ne:null}}")
	public List<Voucher> findByMerchantCodeAndStatusAndOfferIdAndBulkId(String merchantCode, String status, String offerId);
	
	//LISTING VOUCHER QUERIES
	
	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1 }")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDate(String accountNumber, String active, Date startDate, Pageable pageable);
	
	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListed(String accountNumber, String active, Date startDate, Pageable pageable);
	
	@Query("{'accountNumber': ?0}, '$or' : [{'startDate':{'$lte':?1}}, {'startDate':{'$in':[null]}}]]")
	public List<Voucher> findByAccountNumberAndStartDate(String accountNumber, Date startDate);
	
	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDate(String accountNumber, String status, Date startDate);
	
	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]} ], 'status': ?2}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatus(String accountNumber, Date startDate, String status, Pageable pageable);
	
	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]} ], 'status': ?2, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatusAndIsBlackListed(String accountNumber, Date startDate, String status, Pageable pageable);
	
	@Query("{'$or' : [{'AccountNumber': ?0,'Status':'burnt'},{'GiftDetails.GiftedAccountNumber': ?0}]}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumber(String accountNumber, Pageable pageable);
	
	@Query("{'$and' : [{'$or' : [{'AccountNumber': ?0,'Status':'burnt'},{'GiftDetails.GiftedAccountNumber': ?0}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}] }")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndIsBlackListed(String accountNumber, Pageable pageable);
	
	@Query("{'uuid.id': ?0, '$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]}")
	public List<Voucher> findByUuidAndStartDate(String uuid, Date startDate, Sort sort);
	
	@Query("{'uuid.id': ?0, '$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}], 'ProgramCode': {$regex:?2,$options:'i'}}")
	public List<Voucher> findByUuidAndStartDateAndProgramCodeIgnoreCase(String uuid, Date startDate, String program, Sort sort);
	
	@Query("{'uuid.id': ?0, '$and' : [{'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}] }")
	public List<Voucher> findByUuidAndStartDateAndIsBlackListed(String uuid, Date startDate, Sort sort);
	
	@Query("{'uuid.id': ?0, '$and' : [{'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}], 'ProgramCode': {$regex:?2,$options:'i'}}")
	public List<Voucher> findByUuidAndStartDateAndIsBlackListed(String uuid, Date startDate, String program, Sort sort);
	
	@Query("{'voucherCode': ?0, '$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]}")
	public Voucher findByVoucherCodeAndStartDate(String voucherCode, Date startDate);
	
	@Query("{'voucherCode': ?1, '$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ]}")
	public Voucher findByAccountNumberAndVoucherCodeAndStartDate(String accountNumber, String voucherCode, Date startDate);
	
	@Query("{'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}], 'voucherCode': ?1}")
	public Voucher findByAccountNumberAndVoucherCode(String accountNumber, String voucherCode);
	
	@Query("{'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}], 'voucherCode': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Voucher findByAccountNumberAndVoucherCodeAndProgramCodeIgnoreCase(String accountNumber, String voucherCode, String programCode);
	
	@Query("{'$and' : [{'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}], 'voucherCode': ?1}")
	public Voucher findByAccountNumberAndVoucherCodeAndIsBlackListed(String accountNumber, String voucherCode);
	
	@Query("{'$and' : [{'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}], 'voucherCode': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Voucher findByAccountNumberAndVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCase(String accountNumber, String voucherCode, String programCode);
	
	@Query("{'id': ?0, '$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]}")
	public Optional<Voucher> findByIdAndStartDate(String voucherId, Date startDate);
	
	@Query("{'id': ?0, '$and' : [{'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}] }")
	public Optional<Voucher> findByIdAndStartDateAndIsBlackListed(String voucherId, Date startDate);

	@Query("{'status': ?0, '$and' : [{'expiryDate':{'$lte':?1}}, {'expiryDate':{'$gte':?2}}]}")
	public List<Voucher> findByStatusAndExpiryDate(String status, Date expiryDate, Date todayDate);
	
	@Query("{'status': ?0, '$and' : [{'expiryDate':{'$gte':?1}}]}")
	public List<Voucher> findByStatusAndVoucherExpiryDate(String status, Date todayDate);
	
	@Query("{'uuid': {$in : ?0}}}")
	public List<Voucher> findByUuid(List<String> uuid, Sort sort);
	
	@Query("{'uuid': {$in : ?0}, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}")
	public List<Voucher> findByUuidAndIsBlackListed(List<String> uuid, Sort sort);
	
	@Query("{'accountNumber':{'$in': ?0}}")
    public List<Voucher> findByAccountNumber(List<String> accountNumber);

	@Query("{'accountNumber':?0, 'membershipCode':?1 }")
	public Voucher findByAccountNumberAndMembershipCode(String oldAccountNumber, String membershipCode);
	
	@Query("{'voucherCode': ?0, 'merchantCode': ?1}")
	public Voucher findByCodeAndMerchantCode(String voucherCode, String merchantCode);
	
	@Query("{'voucherCode': ?0, 'merchantCode': ?1, 'programCode': {$regex:?2,$options:'i'}}")
	public Voucher findByCodeAndMerchantCodeAndProgramCodeIgnoreCase(String voucherCode, String merchantCode, String programCode);
	
	@Query("{'voucherCode': ?0, 'merchantCode': ?1, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}")
	public Voucher findByCodeAndMerchantCodeAndIsBlackListed(String voucherCode, String merchantCode);
	
	@Query("{'voucherCode': ?0, 'merchantCode': ?1, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}], 'programCode': {$regex:?2,$options:'i'}}")
	public Voucher findByCodeAndMerchantCodeAndIsBlackListedAndProgramCodeIgnoreCase(String voucherCode, String merchantCode, String programCode);

	@Query("{'voucherCode': ?0, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}")
	public Voucher findByVoucherCodeAndIsBlackListed(String voucherCode);
	
	@Query("{'voucherCode': ?0, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}], 'programCode': {$regex:?1,$options:'i'}}")
	public Voucher findByVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCase(String voucherCode, String programCode);
	
	@Query("{'$and' : [{'DownloadedDate':{'$gte':?0}}, {'DownloadedDate':{'$lt':?1}}]}")
	public List<Voucher> findByDownloadedDateVoucherStats(Date todayDate, Date tomorrowDate);
	
	@Query("{'bulkId':{$ne:null}, 'Status' : {$in : ?0}}")
	public List<Voucher> findByBulkIdAndStatus(List<String> status);
	
	@Query("{'BulkId':{$ne:null}, '$and' : [{'$or' : [{'StartDate':{'$lte':?0}}, {'StartDate':{'$in':[null]}}]}, {'$or' : [{'ExpiryDate': {'$gte':?1}}, {'ExpiryDate':null}]}] }")
	public List<Voucher> findByBulkIdAndStartDateAndExpiryDate(Date startDate, Date expiryDate);
	
	//@Query(value="{'_id' : $0}", delete = true)
	//public Voucher deleteByGivenId (String id);
	
	public List<Voucher> findByMembershipCodeAndAccountNumberIn(String membershipCode,List<String> accountNumberList);
	
	@Query("{'$and' : [ {'offerInfo.offer': ?3}, {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndOfferId(String accountNumber, String active, Date startDate, String offerId, Pageable pageable);

	@Query("{'$and' : [ {'offerInfo.offer': ?3}, {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]} ")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListedAndOfferId(String accountNumber, String active, Date startDate,String offerId, Pageable pageable);
	
	@Query("{'voucherCode': ?0, 'CashVoucherBurntInfo.ExternalTransactionId': ?1}")
	public List<Voucher> findByVoucherCodeAndExternalTransactionId(String voucherCode, String externalTransactionId);
	
	@Query("{'voucherCode': {$in : ?0}, '$and' : [{'expiryDate':{'$gte':?1}}]}")
	public List<Voucher> findByVoucherCodeAndDate(List<String> voucherCode, Date date);

	@Query("{'$and' : [ {'offerInfo.offer': ?3}, {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1, 'channel' : {$regex : ?3, $options:'i'}}")
	public List<Voucher>  findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndOfferIdAndChannelIgnoreCase(
			String accountNumber, String active, Date currentDate, String offerId, String channelId, Sort sort);

	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1, 'channel' : {$regex : ?3, $options:'i'} }")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndChannelIgnoreCase(
			String accountNumber, String active, Date currentDate, String channelId, Sort sort);
	
	@Query("{'$and' : [ {'offerInfo.offer': ?3}, {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}], 'channel' : {$regex : ?4, $options:'i'}} ")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListedAndOfferIdAndChannelIgnoreCase(
			String accountNumber, String active, Date currentDate, String offerId, String channelId, Sort sort);

	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?2}}, {'startDate':{'$in':[null]}}]} ], 'status': ?1, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}], 'channel' : {$regex : ?3, $options:'i'}}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStatusAndStartDateAndIsBlackListedAndChannelIgnoreCase(
			String accountNumber, String active, Date currentDate, String channelId, Sort sort);

	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]} ], 'status': ?2, 'channel' : {$regex : ?3, $options:'i'}}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatusAndChannelIgnoreCase(
			String accountNumber, Date currentDate, String expired, String channelId, Sort sort);

	@Query("{'$and' : [ {'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0, 'GiftDetails.IsGift': {'$not' : {$regex:'yes',$options:'i'}}}]}, {'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]} ], 'status': ?2, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}], 'channel' : {$regex : ?3, $options:'i'}}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndStartDateAndStatusAndIsBlackListedAndChannelIgnoreCase(
			String accountNumber, Date currentDate, String expired, String channelId, Sort sort);

	@Query("{'$or' : [{'AccountNumber': ?0,'Status':'burnt'},{'GiftDetails.GiftedAccountNumber': ?0}], 'channel' : {$regex : ?1, $options:'i'}}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndChannelIgnoreCase(String accountNumber,
			String channelId, Sort sort);

	@Query("{'$and' : [{'$or' : [{'AccountNumber': ?0,'Status':'burnt'},{'GiftDetails.GiftedAccountNumber': ?0}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}], 'channel' : {$regex : ?1, $options:'i'}}")
	public List<Voucher> findByAccountNumberOrGiftedAccountNumberAndIsBlackListedAndChannelIgnoreCase(
			String accountNumber, String channelId, Sort sort);

	@Query("{'id': ?0, '$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}], 'channel' : {$regex : ?2, $options:'i'}}")
	public Optional<Voucher> findByIdAndStartDateAndChannel(String voucherId, Date currentDate, String channelId);

	@Query("{'id': ?0, '$and' : [{'$or' : [{'startDate':{'$lt':?1}}, {'startDate':{'$in':[null]}}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}], 'channel' : {$regex : ?2, $options:'i'} }")
	public Optional<Voucher> findByIdAndStartDateAndIsBlackListedAndChannel(String voucherId, Date currentDate,
			String channelId);

	@Query("{'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}], 'voucherCode': ?1, 'programCode': {$regex:?2,$options:'i'}, 'channel': {$regex:?3,$options:'i'}}")
	public Voucher findByAccountNumberAndVoucherCodeAndProgramCodeIgnoreCaseAndChannel(String accountNumber,
			String voucherCode, String program, String channelId);

	@Query("{'$and' : [{'$or' : [{'AccountNumber': ?0},{'GiftDetails.GiftedAccountNumber': ?0}]}, {'$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}]}], 'voucherCode': ?1, 'programCode': {$regex:?2,$options:'i'}, 'channel': {$regex:?3,$options:'i'}}")
	public Voucher findByAccountNumberAndVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCaseAndChannel(
			String accountNumber, String voucherCode, String program, String channelId);

	@Query("{'voucherCode': ?0, 'merchantCode': ?1, 'programCode': {$regex:?2,$options:'i'}, 'channel': {$regex:?3,$options:'i'}}")
	public Voucher findByCodeAndMerchantCodeAndProgramCodeIgnoreCaseAndChannel(String voucherCode, String merchantCode,
			String program, String channelId);

	@Query("{'voucherCode': ?0, 'merchantCode': ?1, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}], 'programCode': {$regex:?2,$options:'i'}, 'channel': {$regex:?3,$options:'i'}}")
	public Voucher findByCodeAndMerchantCodeAndIsBlackListedAndProgramCodeIgnoreCaseAndChannel(String voucherCode,
			String merchantCode, String program, String channelId);

	public Voucher findByVoucherCodeAndProgramCodeIgnoreCaseAndChannelIgnoreCase(String voucherCode, String program,
			String channelId);

	@Query("{'voucherCode': ?0, '$or' : [{'IsBlackListed': false}, {'IsBlackListed':null}], 'programCode': {$regex:?1,$options:'i'}, 'channel': {$regex:?2,$options:'i'}}")
	public Voucher findByVoucherCodeAndIsBlackListedAndProgramCodeIgnoreCaseAndChannel(String voucherCode,
			String program, String channelId);

}
