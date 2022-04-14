package com.loyalty.marketplace.outbound.events.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketplaceDBConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.outbound.database.entity.BirthdayReminder;
import com.loyalty.marketplace.gifting.outbound.database.entity.BirthdayReminderList;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingCounter;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingHistory;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificate;
import com.loyalty.marketplace.gifting.outbound.database.repository.BirthdayReminderRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingCounterRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingHistoryRepository;
import com.loyalty.marketplace.gifting.outbound.database.repository.GoldCertificateRepository;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.AccountOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberOfferCounts;
import com.loyalty.marketplace.offers.outbound.database.entity.MemberRating;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.entity.WishlistEntity;
import com.loyalty.marketplace.offers.outbound.database.repository.AccountOfferCounterRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.BirthdayGiftTrackerRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.MemberOfferCounterRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferCountersRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRatingRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.WishlistRepository;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.FilterValues;
import com.loyalty.marketplace.offers.utils.MapValues;
import com.loyalty.marketplace.offers.utils.Predicates;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.events.eventobject.AccountCancelEvent;
import com.loyalty.marketplace.outbound.events.eventobject.AccountChangeEvent;
import com.loyalty.marketplace.outbound.events.eventobject.EnrollEvent;
import com.loyalty.marketplace.outbound.events.eventobject.MemberMergeEvent;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.domain.SubscriptionDomain;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementControllerHelper;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;

@Component
public class EventHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(EventHelper.class);
		
	@Autowired
	AuditService auditService;
	
	@Autowired
    PurchaseRepository purchaseRepository;
	
	@Autowired
	OfferRatingRepository offerRatingRepository;
	
	@Autowired
	GoldCertificateRepository goldCertificateRepository;
	
	@Autowired
	OfferCountersRepository offerCountersRepository;
	
	@Autowired
	BirthdayGiftTrackerRepository birthdayGiftTrackerRepository;
	
	@Autowired
	WishlistRepository wishlistRepository;
	
	@Autowired
    VoucherRepository voucherRepository;
	
	@Autowired
	SubscriptionDomain subscriptionDomain;
	
	@Autowired
	SubscriptionManagementControllerHelper subscriptionManagementControllerHelper;
	
	@Autowired
    SubscriptionRepository subscriptionRepository;
	
	@Autowired
	SubscriptionCatalogRepository subscriptionCatalogRepository;
	
	@Autowired
	BirthdayReminderRepository birthdayReminderRepository;
	
	@Autowired
	GiftingCounterRepository giftingCounterRepository;
	
	@Autowired
	GiftingHistoryRepository giftingHistoryRepository;
	
	@Autowired
	AccountOfferCounterRepository accountOfferCounterRepository;
	
	@Autowired
	MemberOfferCounterRepository memberOfferCounterRepository;
	
	@Value("${marketplace.listenerQueue}")
    private String marketplaceListenerQueue;
	
	/**
	 * Updates all the collections that have field account number
	 * @param accountChangeEvent
	 */
	public void updateAccountNumbers(AccountChangeEvent accountChangeEvent) {
		
		updatePurchaseHistoryRecords(accountChangeEvent); 
		updateOfferCounterRecords(accountChangeEvent);
		updateOfferRatingRecords(accountChangeEvent);
		updateWishlistRecords(accountChangeEvent);
		updateBirthdayTrackerRecords(accountChangeEvent);
		updateGoldCertificateTransactionRecords(accountChangeEvent);
		updateBirthdayReminderRecords(accountChangeEvent);
		updateGiftingCounterRecords(accountChangeEvent);
		updateGiftingHistoryRecords(accountChangeEvent);
		updateVoucherRecords(accountChangeEvent);
		updateSubscriptionRecords(accountChangeEvent);
	}

	/**
	 * Replaces old account number with new account number in existing purchase history records
	 * in case of account number change
	 * @param accountChangeEvent
	 */
	private void updatePurchaseHistoryRecords(AccountChangeEvent accountChangeEvent) {
		
		List<PurchaseHistory> purchaseList = purchaseRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		
		if(CollectionUtils.isNotEmpty(purchaseList)) {
			
			List<PurchaseHistory> originalPurchaseList = new ArrayList<>(purchaseList.size());
			originalPurchaseList.addAll(purchaseList);
			
			for(PurchaseHistory purchaseRecord : purchaseList) {
				
				purchaseRecord.setAccountNumber(accountChangeEvent.getNewAccountNumber());
				purchaseRecord.setUpdatedDate(new Date());
				purchaseRecord.setUpdatedUser(accountChangeEvent.getUserName());
			}
			
			purchaseRepository.saveAll(purchaseList);
			auditService.updateDataAudit(OffersDBConstants.PURCHASE_HISTORY, purchaseList, marketplaceListenerQueue, originalPurchaseList, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
		
	}
	
	/**
	 * Replaces old account number with new account number in existing offer counter 
	 * records in case of account number change
	 * @param accountChangeEvent
	 */
	private void updateOfferCounterRecords(AccountChangeEvent accountChangeEvent) {
		
		List<AccountOfferCounts> counterList = accountOfferCounterRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		
		if(CollectionUtils.isNotEmpty(counterList)) {
			
			List<AccountOfferCounts> originalCounterList  = new ArrayList<>(counterList.size());
			originalCounterList.addAll(counterList);
			counterList.forEach(c->c.setAccountNumber(accountChangeEvent.getNewAccountNumber()));
			accountOfferCounterRepository.saveAll(counterList);
			auditService.updateDataAudit(OffersDBConstants.ACCOUNT_OFFER_COUNTERS, counterList, marketplaceListenerQueue, originalCounterList, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
		}
		
	}
	
	/**
	 * Replaces old account number with new account number in existing offer rating
	 * records in case of account number change
	 * @param accountChangeEvent
	 */
	private void updateOfferRatingRecords(AccountChangeEvent accountChangeEvent) {
		
		List<OfferRating> offerRatingList = offerRatingRepository.findListByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		List<OfferRating> originalRatingList = new ArrayList<>(offerRatingList.size());
		originalRatingList.addAll(offerRatingList);
		
		for(OfferRating rating : offerRatingList) {
			
			MemberRating memberRating = rating.getMemberRatings().stream()
					.filter(m-> StringUtils.equals(m.getAccountNumber(), accountChangeEvent.getOldAccountNumber()))
					.findAny().orElse(null);
			
			if(!ObjectUtils.isEmpty(memberRating)) {
				
				memberRating.setAccountNumber(accountChangeEvent.getNewAccountNumber());
				rating.setUpdatedDate(new Date());
				rating.setUpdatedUser(accountChangeEvent.getUserName());
			}
		}
		
		offerRatingRepository.saveAll(offerRatingList);
		auditService.updateDataAudit(OffersDBConstants.OFFER_RATING, offerRatingList, marketplaceListenerQueue, originalRatingList, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
		
	}
	
	/**
	 * Replaces old account number with new account number in existing wishlist records 
	 * in case of account number change
	 * @param accountChangeEvent
	 */
	private void updateWishlistRecords(AccountChangeEvent accountChangeEvent) {
		
		WishlistEntity wishlist = wishlistRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		
		if(!ObjectUtils.isEmpty(wishlist)) {
			
			Gson gson = new Gson();
			WishlistEntity originalWishlist = gson.fromJson(gson.toJson(wishlist), WishlistEntity.class);
			
			wishlist.setAccountNumber(accountChangeEvent.getNewAccountNumber());
			wishlist.setDtUpdated(new Date());
			wishlist.setUsrUpdated(accountChangeEvent.getUserName());
			
			wishlistRepository.save(wishlist);
			auditService.updateDataAudit(OffersDBConstants.WISHLIST, wishlist, marketplaceListenerQueue, originalWishlist, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
		
	}
	
	/**
	 * Replaces old account number with new account number in existing birthday tracker 
	 * records in case of account number change
	 * @param accountChangeEvent
	 */
	private void updateBirthdayTrackerRecords(AccountChangeEvent accountChangeEvent) {
		
		BirthdayGiftTracker birthdayGiftTracker = birthdayGiftTrackerRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		
		if(!ObjectUtils.isEmpty(birthdayGiftTracker)) {
			
			Gson gson = new Gson();
			BirthdayGiftTracker originalTracker = gson.fromJson(gson.toJson(birthdayGiftTracker), BirthdayGiftTracker.class);
			
			birthdayGiftTracker.setAccountNumber(accountChangeEvent.getNewAccountNumber());
			birthdayGiftTracker.setUpdatedAt(new Date());
			birthdayGiftTracker.setUpdatedBy(accountChangeEvent.getUserName());
			
			birthdayGiftTrackerRepository.save(birthdayGiftTracker);
			auditService.updateDataAudit(OffersDBConstants.BIRTHDAY_HELPER, birthdayGiftTracker, marketplaceListenerQueue, originalTracker, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
		
	}
	
	/**
	 * Replaces old account number with new account number in existing gold certificate transaction 
	 * records in case of account number change
	 * @param accountChangeEvent
	 */
	private void updateGoldCertificateTransactionRecords(AccountChangeEvent accountChangeEvent) {
		
		GoldCertificate goldCertificate = goldCertificateRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		
		if(!ObjectUtils.isEmpty(goldCertificate)) {
			
			Gson gson = new Gson();
			GoldCertificate originalCertificate = gson.fromJson(gson.toJson(goldCertificate), GoldCertificate.class);
			
			goldCertificate.setAccountNumber(accountChangeEvent.getNewAccountNumber());
			goldCertificate.setUpdatedDate(new Date());
			goldCertificate.setUpdatedUser(accountChangeEvent.getUserName());
			
			goldCertificateRepository.save(goldCertificate);
			auditService.updateDataAudit(MarketplaceDBConstants.GOLD_CERTIFICATE, goldCertificate, marketplaceListenerQueue, originalCertificate, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
		
	}
	
	/**
	 * Replaces old account number with new account number in existing birthday reminder
	 * records in case of account number change.
	 * @param accountChangeEvent
	 */
	public void updateBirthdayReminderRecords(AccountChangeEvent accountChangeEvent) {
		
		BirthdayReminder birthdayReminder = birthdayReminderRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		if(!ObjectUtils.isEmpty(birthdayReminder)) {
			
			Gson gson = new Gson();
			BirthdayReminder originalBirthdayReminder = gson.fromJson(gson.toJson(birthdayReminder), BirthdayReminder.class);
			
			birthdayReminder.setAccountNumber(accountChangeEvent.getNewAccountNumber());
			birthdayReminder.setUpdatedDate(new Date());
			birthdayReminder.setUpdatedUser(accountChangeEvent.getUserName());
			
			birthdayReminderRepository.save(birthdayReminder);
			auditService.updateDataAudit(GiftingConfigurationConstants.BIRTHDAY_REMINDER, birthdayReminder, marketplaceListenerQueue, originalBirthdayReminder, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
		
		List<BirthdayReminder> birthdayReminders =  birthdayReminderRepository.findByReceiverAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		if(CollectionUtils.isNotEmpty(birthdayReminders)) {
			
			List<BirthdayReminder> originalBirthdayReminders = new ArrayList<>(birthdayReminders.size());
			originalBirthdayReminders.addAll(birthdayReminders);
			
			for (BirthdayReminder reminder : birthdayReminders) {

				BirthdayReminderList birthdayReminderList = reminder.getReminderList().stream()
						.filter(m -> StringUtils.equals(m.getAccountNumber(), accountChangeEvent.getOldAccountNumber()))
						.findAny().orElse(null);

				if (!ObjectUtils.isEmpty(birthdayReminderList)) {

					birthdayReminderList.setAccountNumber(accountChangeEvent.getNewAccountNumber());
					reminder.setUpdatedDate(new Date());
					reminder.setUpdatedUser(accountChangeEvent.getUserName());
				}

			}
			
			birthdayReminderRepository.saveAll(birthdayReminders);
			auditService.updateDataAudit(GiftingConfigurationConstants.BIRTHDAY_REMINDER, birthdayReminders, marketplaceListenerQueue, originalBirthdayReminders, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
	
	}
	
	/**
	 * Replaces old account number with new account number in existing gifting counter
	 * records in case of account number change.
	 * @param accountChangeEvent
	 */
	public void updateGiftingCounterRecords(AccountChangeEvent accountChangeEvent) {

		List<GiftingCounter> giftingCounters = giftingCounterRepository.findByAccountNumberAndMembershipCode(
				accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		if (CollectionUtils.isNotEmpty(giftingCounters)) {

			List<GiftingCounter> originalGiftingCounters = new ArrayList<>(giftingCounters.size());
			originalGiftingCounters.addAll(giftingCounters);

			for (GiftingCounter counter : giftingCounters) {

				if (null != counter.getAccountNumber()
						&& counter.getAccountNumber().equals(accountChangeEvent.getOldAccountNumber())) {

					counter.setAccountNumber(accountChangeEvent.getNewAccountNumber());
					counter.setUpdatedDate(new Date());
					counter.setUpdatedUser(accountChangeEvent.getUserName());

				}

			}

			giftingCounterRepository.saveAll(giftingCounters);
			auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_COUNTER, giftingCounters,
					marketplaceListenerQueue, originalGiftingCounters, accountChangeEvent.getExternalTransactionId(),
					accountChangeEvent.getUserName());

		}

	}
	
	/**
	 * Replaces old account number with new account number in existing gifting history
	 * records in case of account number change.
	 * @param accountChangeEvent
	 */
	public void updateGiftingHistoryRecords(AccountChangeEvent accountChangeEvent) {

		List<GiftingHistory> allGiftingHistory = giftingHistoryRepository.findByAccountNumberAndMembershipCode(
				accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());

		if (CollectionUtils.isNotEmpty(allGiftingHistory)) {

			List<GiftingHistory> originalGiftingHistory = new ArrayList<>(allGiftingHistory.size());
			originalGiftingHistory.addAll(allGiftingHistory);

			for (GiftingHistory giftingHistory : allGiftingHistory) {

				if (null != giftingHistory.getSenderInfo()
						&& (null != giftingHistory.getSenderInfo().getAccountNumber() && giftingHistory.getSenderInfo()
								.getAccountNumber().equals(accountChangeEvent.getOldAccountNumber()))
						&& (null != giftingHistory.getSenderInfo().getMembershipCode() && giftingHistory.getSenderInfo()
								.getMembershipCode().equals(accountChangeEvent.getMembershipCode()))) {

					giftingHistory.getSenderInfo().setAccountNumber(accountChangeEvent.getNewAccountNumber());
					giftingHistory.setUpdatedDate(new Date());
					giftingHistory.setUpdatedUser(accountChangeEvent.getUserName());

				}

				if (null != giftingHistory.getReceiverInfo()
						&& (null != giftingHistory.getReceiverInfo().getAccountNumber() && giftingHistory
								.getReceiverInfo().getAccountNumber().equals(accountChangeEvent.getOldAccountNumber()))
						&& (null != giftingHistory.getReceiverInfo().getMembershipCode()
								&& giftingHistory.getReceiverInfo().getMembershipCode()
										.equals(accountChangeEvent.getMembershipCode()))) {

					giftingHistory.getReceiverInfo().setAccountNumber(accountChangeEvent.getNewAccountNumber());
					giftingHistory.setUpdatedDate(new Date());
					giftingHistory.setUpdatedUser(accountChangeEvent.getUserName());

				}

			}

			giftingHistoryRepository.saveAll(allGiftingHistory);
			
			auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_HISTORY, allGiftingHistory,
					marketplaceListenerQueue, originalGiftingHistory, accountChangeEvent.getExternalTransactionId(),
					accountChangeEvent.getUserName());

		}

	}
	
	/**
	 * Replaces old account number with new account number in existing voucher collection 
	 * records in case of account number change
	 * @param accountChangeEvent
	 */
	private void updateVoucherRecords(AccountChangeEvent accountChangeEvent) {
		
		Voucher voucher = voucherRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		
		if(!ObjectUtils.isEmpty(voucher)) {
			
			Gson gson = new Gson();
			Voucher originalVoucher = gson.fromJson(gson.toJson(voucher), Voucher.class);
			
			voucher.setAccountNumber(accountChangeEvent.getNewAccountNumber());
			voucher.setUpdatedDate(new Date());
			voucher.setUpdatedUser(accountChangeEvent.getUserName());
			
			voucherRepository.save(voucher);
			auditService.updateDataAudit(DBConstants.VOUCHERS, voucher, marketplaceListenerQueue, originalVoucher, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
		
	}
	
	/**
	 * Replaces old account number with new account number in existing purchase history records
	 * in case of account number change
	 * @param accountChangeEvent
	 */
	private void updateSubscriptionRecords(AccountChangeEvent accountChangeEvent) {
		
		Subscription subscription = subscriptionRepository.findByAccountNumberAndMembershipCode(accountChangeEvent.getOldAccountNumber(), accountChangeEvent.getMembershipCode());
		
		if(!ObjectUtils.isEmpty(subscription)) {
			
			Gson gson = new Gson();
			Subscription originalSubscription = gson.fromJson(gson.toJson(subscription), Subscription.class);
			subscription.setAccountNumber(accountChangeEvent.getNewAccountNumber());
			subscription.setUpdatedDate(new Date());
			subscription.setUpdatedUser(accountChangeEvent.getUserName());
			subscriptionRepository.save(subscription);
			auditService.updateDataAudit("Subscription", subscription, marketplaceListenerQueue, originalSubscription, accountChangeEvent.getExternalTransactionId(), accountChangeEvent.getUserName());
			
		}
		
	}
	
	
	/**
	 * Updates subscription table with Membership code for newly enrolled Accounts
	 * @param enrollEvent
	 */
	public void updateSubscriptionWithMember(EnrollEvent enrollEvent) {
		
		 List<Subscription> subscriptionList = subscriptionRepository.findByAccountNumber(enrollEvent.getAccountNumber());
		 
		 if(null != subscriptionList) {
			 
			 for(Subscription subscription : subscriptionList) {
				 
				 Gson gson = new Gson();
				 Subscription originalSubscription = gson.fromJson(gson.toJson(subscription), Subscription.class);
				 subscription.setMembershipCode(enrollEvent.getMembershipCode());
				 subscriptionRepository.save(subscription);
				 auditService.updateDataAudit("Subscriptions", subscription, marketplaceListenerQueue, originalSubscription, enrollEvent.getExternalTransactionId(), enrollEvent.getUserName());
			 }
		 }
		
	}
	
	/**
	 * Cancel subscription for cancelled Accounts
	 * @param enrollEvent
	 * @throws SubscriptionManagementException 
	 */
	public void cancelSubscriptionForAccount(AccountCancelEvent cancelEvent) throws SubscriptionManagementException {
		
		ResultResponse resultResponse = new ResultResponse(cancelEvent.getExternalTransactionId());
		LOG.info("AccountCancelEvent : {}",cancelEvent);
		LOG.info("ChannelId : {}",cancelEvent.getChannelId());
		LOG.info("Account Cancel Channel Id : {}",cancelEvent.getChannel());
		 
		Headers headers = new Headers();
//		if(!headers.getUserName().equalsIgnoreCase(SubscriptionManagementConstants.CRM_JOB.get())) {
			headers.setUserName(SubscriptionManagementConstants.ACCOUNT_CANCEL_EVENT.get());
//		}
		headers.setExternalTransactionId(cancelEvent.getExternalTransactionId());
		headers.setChannelId(cancelEvent.getChannel());
	 
		List<Subscription> subscriptionList = subscriptionRepository.findByAccountNumber(cancelEvent.getAccountNumber());
		if(null != subscriptionList) {
			subscriptionManagementControllerHelper.validateAndCancelSubscription(null, subscriptionList, headers, resultResponse);
		}
		
	}
	
	
	public void updateAccountWithNewMembership(MemberMergeEvent memberMergeEvent) {
		
		LOG.info("=======Inside updateAccountWithNewMembership method=========");

		if (!ObjectUtils.isEmpty(memberMergeEvent) 
		 && !ObjectUtils.isEmpty(memberMergeEvent.getSourceMember())
		 && !ObjectUtils.isEmpty(memberMergeEvent.getTargetMember())
		 && !StringUtils.isEmpty(memberMergeEvent.getSourceMember().getMembershipCode())
		 && !StringUtils.isEmpty(memberMergeEvent.getTargetMember().getMembershipCode())
		 && !CollectionUtils.isEmpty(memberMergeEvent.getSourceMember().getAccountNumList())) {
			

			LOG.info("SourceMembershipCode : {}", memberMergeEvent.getSourceMember().getMembershipCode());
			LOG.info("targetMembershipCode : {}", memberMergeEvent.getSourceMember().getAccountNumList());
			LOG.info("accountNumberList : {}", memberMergeEvent.getTargetMember().getMembershipCode());

			checkAndUpdateVoucher(memberMergeEvent);
			checkAndUpdateSubscription(memberMergeEvent);
			checkAndUpdatePurchaseHistory(memberMergeEvent);
			checkAndUpdateWishlist(memberMergeEvent);
			checkAndUpdateOfferCounters(memberMergeEvent);
			checkAndUpdateBirthdayGiftTracker(memberMergeEvent);
			checkAndUpdateGoldTransaction(memberMergeEvent);
			checkAndUpdateGiftingCounter( memberMergeEvent);
			checkAndUpdateBirthdayReminder(memberMergeEvent);
			checkAndUpdateOfferRating(memberMergeEvent);
			checkAndUpdateGiftingHistory( memberMergeEvent);

		}
		
	}

	public void checkAndUpdateVoucher(MemberMergeEvent memberMergeEvent) {

		List<Voucher> voucherList = voucherRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
				memberMergeEvent.getSourceMember().getAccountNumList());

		LOG.info("voucherList : {}", voucherList);
		if (!CollectionUtils.isEmpty(voucherList)) {
			
			LOG.info("voucherListSize : {}",voucherList.size());
			
			for (Voucher voucher : voucherList) {
				
				Gson gson = new Gson();
				Voucher originalvoucher = gson.fromJson(gson.toJson(voucher), Voucher.class);
				
				voucher.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				voucher.setUpdatedDate(new Date());
				voucherRepository.save(voucher);
				 auditService.updateDataAudit("Voucher", voucher, marketplaceListenerQueue, originalvoucher, memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
			}
		}
		
	}
	public void checkAndUpdateSubscription(MemberMergeEvent memberMergeEvent) {
		List<Subscription> subscriptionList = subscriptionRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
				memberMergeEvent.getSourceMember().getAccountNumList());
		LOG.info("subscriptionList : {}",subscriptionList);
		
		if (!CollectionUtils.isEmpty(subscriptionList)) {
			
			LOG.info("subscriptionListSize : {}", subscriptionList.size());
			for (Subscription subscription : subscriptionList) {
				
				Gson gson = new Gson();
				Subscription originalSubscription = gson.fromJson(gson.toJson(subscription), Subscription.class);
				
				subscription.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				subscription.setUpdatedDate(new Date());
				subscriptionRepository.save(subscription);
				auditService.updateDataAudit("Subscription", subscription, marketplaceListenerQueue, originalSubscription,  memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
			}
		}
		
	}
	
	public void checkAndUpdatePurchaseHistory(MemberMergeEvent memberMergeEvent) {
		
		List<PurchaseHistory> purchaseHistoryList = purchaseRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
				memberMergeEvent.getSourceMember().getAccountNumList());
		
		LOG.info("purchaseHistoryList : {}", purchaseHistoryList);
		
		if (!CollectionUtils.isEmpty(purchaseHistoryList)) {
			
			LOG.info("purchaseHistoryListSize : {}", purchaseHistoryList.size());
			for (PurchaseHistory purchaseHistory : purchaseHistoryList) {
				
				Gson gson = new Gson();
				PurchaseHistory originalPurchaseHistory = gson.fromJson(gson.toJson(purchaseHistory), PurchaseHistory.class);
				
				purchaseHistory.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				purchaseHistory.setUpdatedDate(new Date());
				purchaseRepository.save(purchaseHistory);
				 auditService.updateDataAudit("PurchaseHistory", purchaseHistory, marketplaceListenerQueue, originalPurchaseHistory,  memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
			}
		}
		
	}
	
	public void checkAndUpdateWishlist(MemberMergeEvent memberMergeEvent) {
		
		List<WishlistEntity> wishlist = wishlistRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
				memberMergeEvent.getSourceMember().getAccountNumList());
	
		LOG.info("wishlist : {}", wishlist);
		
		if (!CollectionUtils.isEmpty(wishlist)) {
			
			LOG.info("wishlist : {}", wishlist.size());
			for (WishlistEntity wishlistEntity : wishlist) {
				
				Gson gson = new Gson();
				WishlistEntity originalWishlistEntity = gson.fromJson(gson.toJson(wishlistEntity), WishlistEntity.class);
				
				wishlistEntity.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				wishlistEntity.setDtUpdated(new Date());
				wishlistRepository.save(wishlistEntity);
				auditService.updateDataAudit("Wishlist", wishlistEntity, marketplaceListenerQueue, originalWishlistEntity, memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
				
			}
		}
		
	}
	
	public void checkAndUpdateBirthdayGiftTracker(MemberMergeEvent memberMergeEvent) {
		
		 List<BirthdayGiftTracker> bithdayGiftList = birthdayGiftTrackerRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
					memberMergeEvent.getSourceMember().getAccountNumList());
		
			LOG.info("bithdayGiftList : {}", bithdayGiftList);
		 
		 if (!CollectionUtils.isEmpty(bithdayGiftList)) {
			 LOG.info("bithdayGiftListSize : {}", bithdayGiftList.size());
			 
			for (BirthdayGiftTracker birthdayGiftTracker : bithdayGiftList) {
				
				Gson gson = new Gson();
				BirthdayGiftTracker originalBirthdayGiftTracker = gson.fromJson(gson.toJson(birthdayGiftTracker), BirthdayGiftTracker.class);
								
				birthdayGiftTracker.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				birthdayGiftTracker.setUpdatedAt(new Date());
				birthdayGiftTrackerRepository.save(birthdayGiftTracker);
				auditService.updateDataAudit("BirthdayGiftTracker", birthdayGiftTracker, marketplaceListenerQueue, originalBirthdayGiftTracker, memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
			}
		}
		
	}

	/**
	 * Update Counters for membership merge
	 * @param memberMergeEvent
	 */
	public void checkAndUpdateOfferCounters(MemberMergeEvent memberMergeEvent) {

		List<MemberOfferCounts> memberCounterList = memberOfferCounterRepository
				.findByMembershipCodeIn(Arrays.asList(memberMergeEvent.getSourceMember().getMembershipCode(), 
						memberMergeEvent.getTargetMember().getMembershipCode()));
		
		if(!CollectionUtils.isEmpty(memberCounterList)) {
		
			List<String> offerIdList = MapValues.mapOfferIdFromMemberOfferCounterList(memberCounterList);
			
			if(!CollectionUtils.isEmpty(offerIdList)) {
				
				List<AccountOfferCounts> accountCounterList = accountOfferCounterRepository
						.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
								memberMergeEvent.getSourceMember().getAccountNumList());
				
				List<MemberOfferCounts> sourceCounterList = new ArrayList<>(1); 
				List<MemberOfferCounts> targetCounterList = new ArrayList<>(1);
				
				List<MemberOfferCounts> originalMemberCounterList = new ArrayList<>(memberCounterList.size());
				originalMemberCounterList.addAll(memberCounterList);
				
				List<AccountOfferCounts> originalAccountCounterList = null;
				List<AccountOfferCounts> updatedAccountCounterList = null;
						
				if(!CollectionUtils.isEmpty(accountCounterList)) {
					
					originalAccountCounterList = new ArrayList<>(accountCounterList.size());
					originalAccountCounterList.addAll(accountCounterList);
					updatedAccountCounterList = new ArrayList<>(1);
				}
				
				for(String id : offerIdList) {
					
					updateAccountAndMemberCounter(id, sourceCounterList, targetCounterList, updatedAccountCounterList, memberMergeEvent, memberCounterList, accountCounterList);
					
				}
				
				memberOfferCounterRepository.saveAll(targetCounterList);
				auditService.updateDataAudit(OffersDBConstants.MEMBER_OFFER_COUNTERS, targetCounterList, marketplaceListenerQueue, originalMemberCounterList, memberMergeEvent.getExternalTransactionId(), memberMergeEvent.getUserName());
				memberOfferCounterRepository.deleteAll(sourceCounterList);
				auditService.deleteDataAudit(OffersDBConstants.MEMBER_OFFER_COUNTERS, sourceCounterList, marketplaceListenerQueue, memberMergeEvent.getExternalTransactionId(), memberMergeEvent.getUserName());
				
				if(!CollectionUtils.isEmpty(accountCounterList) && !CollectionUtils.isEmpty(updatedAccountCounterList)) {
					
					accountOfferCounterRepository.saveAll(updatedAccountCounterList);
					auditService.updateDataAudit(OffersDBConstants.ACCOUNT_OFFER_COUNTERS, updatedAccountCounterList, marketplaceListenerQueue, originalAccountCounterList, memberMergeEvent.getExternalTransactionId(), memberMergeEvent.getUserName());
				}
			}
			
		}
		
	}

	/**
	 * Update Member Offer Counters for membership merge
	 * @param id
	 * @param sourceCounterList
	 * @param targetCounterList
	 * @param updatedAccountCounterList
	 * @param memberMergeEvent
	 * @param memberCounterList
	 * @param accountCounterList
	 */
	private void updateAccountAndMemberCounter(String id, List<MemberOfferCounts> sourceCounterList,
			List<MemberOfferCounts> targetCounterList, List<AccountOfferCounts> updatedAccountCounterList,
			MemberMergeEvent memberMergeEvent, List<MemberOfferCounts> memberCounterList, List<AccountOfferCounts> accountCounterList) {
	
		MemberOfferCounts sourceCounter = FilterValues.findAnyMemberOfferCounterInList(memberCounterList, Predicates.isMemberCounterWithOfferIdAndMembershipCode(id, memberMergeEvent.getSourceMember().getMembershipCode()));
		MemberOfferCounts targetCounter = FilterValues.findAnyMemberOfferCounterInList(memberCounterList, Predicates.isMemberCounterWithOfferIdAndMembershipCode(id, memberMergeEvent.getTargetMember().getMembershipCode()));
		
		LOG.info("Source Counter : {}", sourceCounter);
		LOG.info("Target Counter : {}", targetCounter);
		
		if(!ObjectUtils.isEmpty(sourceCounter) && !ObjectUtils.isEmpty(targetCounter)) {
	
			targetCounter.setDailyCount(targetCounter.getDailyCount()+ sourceCounter.getDailyCount());
			targetCounter.setWeeklyCount(targetCounter.getWeeklyCount()+ sourceCounter.getWeeklyCount());
			targetCounter.setMonthlyCount(targetCounter.getMonthlyCount()+ sourceCounter.getMonthlyCount());
			targetCounter.setAnnualCount(targetCounter.getAnnualCount()+ sourceCounter.getAnnualCount());
			targetCounter.setTotalCount(targetCounter.getTotalCount()+ sourceCounter.getTotalCount());
			targetCounter.setLastPurchased(Checks.checkDateFirstGreaterThanSecond(targetCounter.getLastPurchased(), sourceCounter.getLastPurchased()) ? targetCounter.getLastPurchased() : sourceCounter.getLastPurchased());
		    targetCounterList.add(targetCounter);
		    sourceCounterList.add(sourceCounter);
			
			if(null != updatedAccountCounterList) {
				
				updatedAccountCounterList.addAll(getAccountOfferCounterForMerge(id, targetCounter, memberMergeEvent, accountCounterList));
			}
			
		} else if (!ObjectUtils.isEmpty(sourceCounter)) {
			
			sourceCounter.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
			targetCounterList.add(sourceCounter);
			
			if(null != updatedAccountCounterList) {
				
				updatedAccountCounterList.addAll(getAccountOfferCounterForMerge(id, sourceCounter, memberMergeEvent, accountCounterList));
			}
			
		}
		
	}

	/***
	 * Update Account Offer Counters for membership merge
	 * @param offerId
	 * @param memberCounter
	 * @param memberMergeEvent
	 * @param accountCounterList
	 * @return updated account offer counter list
	 */
	private List<AccountOfferCounts> getAccountOfferCounterForMerge(String offerId, MemberOfferCounts memberCounter, MemberMergeEvent memberMergeEvent, List<AccountOfferCounts> accountCounterList) {
		
		List<AccountOfferCounts> counterList = null;
		List<AccountOfferCounts> updatedCounterList = null;
		
		if(!CollectionUtils.isEmpty(accountCounterList) && !ObjectUtils.isEmpty(memberCounter)) {
			
			updatedCounterList = new ArrayList<>(1);
			counterList = FilterValues.filterAccountOfferCounterList(accountCounterList, 
					Predicates.isAccountCounterWithOfferId(offerId));
			
			if(!CollectionUtils.isEmpty(counterList)) {
				
				for(AccountOfferCounts counter : counterList) {
					
					counter.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
					counter.setMemberOfferCounter(memberCounter);
					updatedCounterList.add(counter);
				}
				
			}
			
		}
		
		return updatedCounterList;
	}
	
	public void checkAndUpdateGoldTransaction(MemberMergeEvent memberMergeEvent) {
		
		
		  List<GoldCertificate> goldCertificateList = goldCertificateRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
					memberMergeEvent.getSourceMember().getAccountNumList());
		
			LOG.info("goldCertificateList : {}", goldCertificateList);
		 
		 if (!CollectionUtils.isEmpty(goldCertificateList)) {
			 LOG.info("goldCertificateListSize : {}", goldCertificateList.size());
			 
			for (GoldCertificate goldCertificate : goldCertificateList) {
				
				Gson gson = new Gson();
				GoldCertificate originalGoldCertificate = gson.fromJson(gson.toJson(goldCertificate), GoldCertificate.class);
				
				
				goldCertificate.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				goldCertificate.setUpdatedDate(new Date());
				goldCertificateRepository.save(goldCertificate);
				auditService.updateDataAudit("GoldTransaction", goldCertificate, marketplaceListenerQueue, originalGoldCertificate, memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
			}
		}
		
	}
	
	public void checkAndUpdateGiftingCounter(MemberMergeEvent memberMergeEvent) {
		
		
		   List<GiftingCounter> giftingCounterList = giftingCounterRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
					memberMergeEvent.getSourceMember().getAccountNumList());
		
			LOG.info("giftingCounterList : {}", giftingCounterList);
		 
		 if (!CollectionUtils.isEmpty(giftingCounterList)) {
			 LOG.info("giftingCounterListSize : {}", giftingCounterList.size());
			 
			for (GiftingCounter giftingCounter : giftingCounterList) {
				
				Gson gson = new Gson();
				GiftingCounter originalGiftingCounter = gson.fromJson(gson.toJson(giftingCounter), GiftingCounter.class);
				
				
				giftingCounter.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				giftingCounter.setUpdatedDate(new Date());
				giftingCounterRepository.save(giftingCounter);
				auditService.updateDataAudit("GiftingCounter", giftingCounter, marketplaceListenerQueue, originalGiftingCounter, memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
			}
		}
		
	}
	
	public void checkAndUpdateBirthdayReminder(MemberMergeEvent memberMergeEvent) {
		
		
		 List<BirthdayReminder> birthdayReminderList = birthdayReminderRepository.findByMembershipCodeAndAccountNumberIn(memberMergeEvent.getSourceMember().getMembershipCode(),
					memberMergeEvent.getSourceMember().getAccountNumList());
		
			LOG.info("birthdayReminderList : {}", birthdayReminderList);
		 
		 if (!CollectionUtils.isEmpty(birthdayReminderList)) {
			 LOG.info("birthdayReminderListSize : {}", birthdayReminderList.size());
			 
			for (BirthdayReminder birthdayReminder : birthdayReminderList) {
				
				if(birthdayReminder.getMembershipCode().equals(memberMergeEvent.getSourceMember().getMembershipCode()) && 
						memberMergeEvent.getSourceMember().getAccountNumList().contains(birthdayReminder.getAccountNumber())	)
				{
					birthdayReminder.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				}
				
				for(BirthdayReminderList birthdayReminderlist:birthdayReminder.getReminderList())
				{
					if(birthdayReminderlist.getMembershipCode().equals(memberMergeEvent.getSourceMember().getMembershipCode()) && 
							memberMergeEvent.getSourceMember().getAccountNumList().contains(birthdayReminderlist.getAccountNumber()))
					{
						BirthdayReminderList birthdayReminderListNew=birthdayReminderlist;
						List<BirthdayReminderList> reminderList = birthdayReminder.getReminderList();
						reminderList.remove(birthdayReminderlist);
						birthdayReminderListNew.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
						reminderList.add(birthdayReminderListNew);
						birthdayReminder.setReminderList(reminderList);
					}
				}
				
				Gson gson = new Gson();
				BirthdayReminder originalBirthdayReminder = gson.fromJson(gson.toJson(birthdayReminder), BirthdayReminder.class);
				birthdayReminder.setUpdatedDate(new Date());
				birthdayReminderRepository.save(birthdayReminder);
				auditService.updateDataAudit("BirthdayReminder", birthdayReminder, marketplaceListenerQueue, originalBirthdayReminder, memberMergeEvent.getExternalTransactionId(),memberMergeEvent.getUserName());
			}
		}
		
	}
	
	public void checkAndUpdateOfferRating(MemberMergeEvent memberMergeEvent) {
		
		List<OfferRating> offerRatingList = offerRatingRepository.findByMembershipCodeAndAccountNumberIn(
				memberMergeEvent.getSourceMember().getMembershipCode(),
				memberMergeEvent.getSourceMember().getAccountNumList());

		LOG.info("offerRatingList : {}", offerRatingList);

		if (!CollectionUtils.isEmpty(offerRatingList)) {
			LOG.info("offerRatingListSize : {}", offerRatingList.size());

			for (OfferRating offerRating : offerRatingList) {
				for (MemberRating memberRating : offerRating.getMemberRatings()) {
					if (memberRating.getMembershipCode().equals(memberMergeEvent.getSourceMember().getMembershipCode())
							&& memberMergeEvent.getSourceMember().getAccountNumList()
									.contains(memberRating.getAccountNumber())) {
						Gson gson = new Gson();
						OfferRating originalOfferRating = gson.fromJson(gson.toJson(offerRating), OfferRating.class);

						MemberRating memberRatingNew = memberRating;
						List<MemberRating> memberRatings = offerRating.getMemberRatings();
						memberRatings.remove(memberRating);
						memberRatingNew.setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
						memberRatings.add(memberRatingNew);
						offerRating.setMemberRatings(memberRatings);
						offerRating.setUpdatedDate(new Date());
						offerRatingRepository.save(offerRating);
						auditService.updateDataAudit(OffersDBConstants.OFFER_RATING, offerRating, marketplaceListenerQueue,
								originalOfferRating, memberMergeEvent.getExternalTransactionId(),
								memberMergeEvent.getUserName());
					}
				}
			}
		}
	
	}
	
	public void checkAndUpdateGiftingHistory(MemberMergeEvent memberMergeEvent) {
		
		 List<GiftingHistory> giftingHistoryList = giftingHistoryRepository.findByAccountNumberInAndMembershipCode(
				memberMergeEvent.getSourceMember().getAccountNumList(),memberMergeEvent.getSourceMember().getMembershipCode()
				);

		LOG.info("giftingHistoryList : {}", giftingHistoryList);

		if (!CollectionUtils.isEmpty(giftingHistoryList)) {
			LOG.info("giftingHistoryList : {}", giftingHistoryList.size());

			for (GiftingHistory giftingHistory : giftingHistoryList) {
				if(giftingHistory.getSenderInfo().getMembershipCode().equals(memberMergeEvent.getSourceMember().getMembershipCode()) &&
						memberMergeEvent.getSourceMember().getAccountNumList().contains(giftingHistory.getSenderInfo().getAccountNumber()))
				{
					giftingHistory.getSenderInfo().setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				}
				if(giftingHistory.getReceiverInfo().getMembershipCode().equals(memberMergeEvent.getSourceMember().getMembershipCode()) &&
						memberMergeEvent.getSourceMember().getAccountNumList().contains(giftingHistory.getReceiverInfo().getAccountNumber()))
				{
					giftingHistory.getReceiverInfo().setMembershipCode(memberMergeEvent.getTargetMember().getMembershipCode());
				}
					
					Gson gson = new Gson();
					GiftingHistory originalGiftingHistory = gson.fromJson(gson.toJson(giftingHistory), GiftingHistory.class);

						
						giftingHistoryRepository.save(giftingHistory);
						auditService.updateDataAudit("GiftingHistory", giftingHistory, marketplaceListenerQueue,
								originalGiftingHistory, memberMergeEvent.getExternalTransactionId(),
								memberMergeEvent.getUserName());
				
			
			}
		}
	
	}
	
	public void rollbackNonVoucher(String pointsTransaction) {
	
		PurchaseHistory purchaseHistory = purchaseRepository.findByPointsTransactionId(pointsTransaction);
		
		if(purchaseHistory != null) {
			purchaseHistory.setRollBackFlag(true);
			purchaseHistory.setUpdatedDate(new Date());
			purchaseRepository.save(purchaseHistory);
		}
	}
	
	
}
