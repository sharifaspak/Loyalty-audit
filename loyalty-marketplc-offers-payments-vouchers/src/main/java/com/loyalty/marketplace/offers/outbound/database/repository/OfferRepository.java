package com.loyalty.marketplace.offers.outbound.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;

/**
 * 
 * @author jaya.shukla
 *
 */
public interface OfferRepository extends MongoRepository<OfferCatalog, String> {
	
	public OfferCatalog findByOfferCode(String offerCode);
	
	@Query("{'offerId': ?0}")
	public OfferCatalog findByOfferId(String offerId);
	
	@Query("{'offerId': ?0, 'programCode' : {$regex:?1,$options:'i'}}")
	public OfferCatalog findByOfferIdAndProgramCodeIgnoreCase(String offerId, String programCode);
	
    @Query("{'status' : {$in : ?0},'availableInPortals':{$in : ?1},'isBirthdayGift':{$nin : ?2},"
    		+ "'offerDates.offerEndDate':{$not:{$lt:?3}}}")
    public List<OfferCatalog> findAllActiveEligibleOffers(List<String> status, List<String> validPortal, List<String> flagValue, Date today);
    
    @Query("{'status' : {$in:?0},'isBirthdayGift':{$nin : ?1},"
    		+ "'offerDates.offerEndDate':{$not:{$lt:?2}}}")
    public List<OfferCatalog> findAllActiveOffers(List<String> status, List<String> flagValue, Date today);
   
    @Query("{'status':{$in:?0},'availableInPortals':{$in : ?1},'isBirthdayGift':{$nin : ?2},"
    		+ "'offerDates.offerEndDate':{$not:{$lt:?3}}, 'offerId' : {$in : ?4}}")
    public List<OfferCatalog> findAllActiveOffersFromList(List<String> status, List<String> validPortal, List<String> flagValue, Date today, List<String> idList);
    
    @Query("{'subOffer.subOfferId': ?0}")
	public OfferCatalog findBySubOfferId(String subOfferId);
	
	@Query("{'offerType.offerTypeId': ?0, 'Status' : ?1}")
	public List<OfferCatalog> findByOfferTypeMerchantCode(String offerId, String status);

	public List<OfferCatalog> findByIsBirthdayGift(String isBirthdayGift);
	
	@Query("{'giftInfo' : {$exists : true}, 'giftInfo.isGift': ?0}")
	public List<OfferCatalog> findByIsGift(String flagStatus);
	
	@Query("{'giftInfo' : {$exists : true}, 'offerId': ?0, 'giftInfo.isGift': ?1}")
	public OfferCatalog findByOfferIdAndIsGift(String offerId, String flagStatus);
	
	@Query("{'offerId': ?0, 'status' : {$regex:?1,$options:'i'}, 'offerValues.pointsValue' : 0, 'offerValues.cost' : 0.0}")
	public OfferCatalog findByOfferIdStatusFree(String offerId, String status);

	@Query("{'offerId' : {$in : ?0}}") 
	List<OfferCatalog> findByOfferIdList(List<String> offerIdList);
	
	@Query("{'offerId' : {$in : ?0}, 'programCode' : {$regex:?1,$options:'i'}}") 
	List<OfferCatalog> findByOfferIdListAndProgramCodeIgnoreCase(List<String> offerIdList, String programCode);

	@Query("{'status':{$in:?0},'availableInPortals':{$in : ?1},'isBirthdayGift':{$exists : true, $in : ?2},"
    		+ "'offerDates.offerEndDate':{$not:{$lt:?3}}}")
    public List<OfferCatalog> findAllActiveBirthdayOffers(List<String> status, List<String> validPortal, List<String> flagValue, Date today);

	@Query("{'status':{$in:?0},'availableInPortals':{$in : ?1},"
    		+ "'offerDates.offerEndDate':{$not:{$lt:?2}}, 'offerId' : {$in : ?3}}")
	public List<OfferCatalog> findAllActiveOffersFromOfferIdList(List<String> status, List<String> validPortalList, 
			Date date, List<String> offersList);

	@Query("{'offerId': {$in : ?0}, 'status' : {$regex:?1,$options:'i'}, 'offerValues.pointsValue' : 0, 'offerValues.cost' : 0.0}")
	public List<OfferCatalog> findByActiveOfferList(List<String> offerIdList, String status);
	
	@Query("{'merchant': ?0, 'Status' : ?1}")
	public List<OfferCatalog> findByMerchantAndStatusIn(Merchant merchant, List<String> status);

}
