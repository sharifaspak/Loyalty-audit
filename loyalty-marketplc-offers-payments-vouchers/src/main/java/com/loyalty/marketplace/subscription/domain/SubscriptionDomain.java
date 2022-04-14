package com.loyalty.marketplace.subscription.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.constants.SubscriptionRequestMappingConstants;
import com.loyalty.marketplace.subscription.inbound.dto.ManageSubscriptionRequestDto;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepository;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepositoryHelper;
import com.loyalty.marketplace.subscription.utils.SubscriptionCatalogValidator;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.subscription.utils.SubscriptionUtils;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@Component
@Getter
@ToString
public class SubscriptionDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionDomain.class);
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	SubscriptionRepositoryHelper subscriptionRepositoryHelper;
	
	@Autowired
	AuditService auditService;
	
	private String id;
	private String programCode;
	private String subscriptionCatalogId;
	private String accountNumber;
	private String membershipCode;
	private String promoCode;
	private Double cost;
	private Integer pointsValue;
	private Integer freeDuration;
	private Integer validityPeriod;
	private Date startDate;
	private Date endDate;
	private String status;
	private String paymentMethod;
	private String subscriptionMethod;
	private String subscriptionSegment;
	private String subscriptionChannel;
	private String unSubscriptionChannel;
	private Date unSubscriptionDate;
	private String phoneyTunesPackageId;
	private String transactionId;
	private Double lastChargedAmount;	
	private Date lastChargedDate;	
	private Date nextRenewalDate;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	
	@Autowired
	SubscriptionUtils subscriptionUtils;
	
	public SubscriptionDomain() {
		
	}
	
	public SubscriptionDomain(SubscriptionBuilder subscriptionBuilder) {
		super();
		this.id = subscriptionBuilder.id;
		this.programCode = subscriptionBuilder.programCode;	
		this.subscriptionCatalogId = subscriptionBuilder.subscriptionCatalogId;
		this.accountNumber = subscriptionBuilder.accountNumber;
		this.membershipCode = subscriptionBuilder.membershipCode;
		this.promoCode = subscriptionBuilder.promoCode;
		this.cost = subscriptionBuilder.cost;
		this.pointsValue = subscriptionBuilder.pointsValue;	
		this.freeDuration = subscriptionBuilder.freeDuration;
		this.validityPeriod = subscriptionBuilder.validityPeriod;
		this.startDate = subscriptionBuilder.startDate;
		this.endDate = subscriptionBuilder.endDate;
		this.status = subscriptionBuilder.status;
		this.paymentMethod = subscriptionBuilder.paymentMethod;	
		this.subscriptionMethod = subscriptionBuilder.subscriptionMethod;
		this.subscriptionSegment = subscriptionBuilder.subscriptionSegment;
		this.subscriptionChannel = subscriptionBuilder.subscriptionChannel;
		this.unSubscriptionChannel = subscriptionBuilder.unSubscriptionChannel;
		this.unSubscriptionDate = subscriptionBuilder.unSubscriptionDate;
		this.phoneyTunesPackageId = subscriptionBuilder.phoneyTunesPackageId;
		this.transactionId = subscriptionBuilder.transactionId;
		this.lastChargedAmount = subscriptionBuilder.lastChargedAmount;
		this.lastChargedDate = subscriptionBuilder.lastChargedDate;
		this.nextRenewalDate = subscriptionBuilder.nextRenewalDate;
		this.createdDate = subscriptionBuilder.createdDate;				
		this.createdUser = subscriptionBuilder.createdUser;
		this.updatedDate = subscriptionBuilder.updatedDate;
		this.updatedUser = subscriptionBuilder.updatedUser;
	}

	public static class SubscriptionBuilder {
		private String id;
		private String programCode;
		private String subscriptionCatalogId;
		private String accountNumber;
		private String membershipCode;
		private String promoCode;
		private Double cost;
		private Integer pointsValue;
		private Integer freeDuration;
		private Integer validityPeriod;
		private Date startDate;
		private Date endDate;
		private String status;
		private String paymentMethod;
		private String subscriptionMethod;
		private String subscriptionSegment;
		private String subscriptionChannel;
		private String unSubscriptionChannel;
		private Date unSubscriptionDate;
		private String phoneyTunesPackageId;
		private String transactionId;
		private Double lastChargedAmount;	
		private Date lastChargedDate;	
		private Date nextRenewalDate;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;

		public SubscriptionBuilder(String programCode, String subscriptionCatalogId, String accountNumber, String membershipCode,
				String promoCode, Double cost, Integer pointsValue, Integer freeDuration, Integer validityPeriod,
				Date startDate, Date endDate, String status, String paymentMethod, String subscriptionMethod, String subscriptionSegment, String subscriptionChannel,
				String unSubscriptionChannel, Date unSubscriptionDate, String phoneyTunesPackageId, String transactionId, Double lastChargedAmount, 
				Date lastChargedDate, Date nextRenewalDate, Date createdDate, String createdUser,
				Date updatedDate, String updatedUser) {
			super();
			this.programCode = programCode;
			this.subscriptionCatalogId = subscriptionCatalogId;
			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
			this.promoCode = promoCode;
			this.cost = cost;
			this.pointsValue = pointsValue;
			this.freeDuration = freeDuration;
			this.validityPeriod = validityPeriod;
			this.startDate = startDate;
			this.endDate = endDate;
			this.status = status;
			this.paymentMethod = paymentMethod;
			this.subscriptionMethod = subscriptionMethod;
			this.subscriptionSegment = subscriptionSegment;
			this.subscriptionChannel = subscriptionChannel;
			this.unSubscriptionChannel = unSubscriptionChannel;
			this.unSubscriptionDate = unSubscriptionDate;
			this.phoneyTunesPackageId = phoneyTunesPackageId;
			this.transactionId = transactionId;
			this.lastChargedAmount = lastChargedAmount;
			this.lastChargedDate = lastChargedDate;
			this.nextRenewalDate = nextRenewalDate;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
			
		}
		
		public SubscriptionBuilder(String id, String programCode, String subscriptionCatalogId, String accountNumber, String membershipCode,
				String promoCode, Double cost, Integer pointsValue, Integer freeDuration, Integer validityPeriod,
				Date startDate, Date endDate, String status, String paymentMethod, String subscriptionMethod, String subscriptionSegment, String subscriptionChannel,
				String unSubscriptionChannel, Date unSubscriptionDate, String phoneyTunesPackageId, String transactionId, Double lastChargedAmount, 
				Date lastChargedDate, Date nextRenewalDate, Date createdDate, String createdUser,
				Date updatedDate, String updatedUser) {
			super();
			this.id = id;
			this.programCode = programCode;
			this.subscriptionCatalogId = subscriptionCatalogId;
			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
			this.promoCode = promoCode;
			this.cost = cost;
			this.pointsValue = pointsValue;
			this.freeDuration = freeDuration;
			this.validityPeriod = validityPeriod;
			this.startDate = startDate;
			this.endDate = endDate;
			this.status = status;
			this.paymentMethod = paymentMethod;
			this.subscriptionMethod = subscriptionMethod;
			this.subscriptionSegment = subscriptionSegment;
			this.subscriptionChannel = subscriptionChannel;
			this.unSubscriptionChannel = unSubscriptionChannel;
			this.unSubscriptionDate = unSubscriptionDate;
			this.phoneyTunesPackageId = phoneyTunesPackageId;
			this.transactionId = transactionId;
			this.lastChargedAmount = lastChargedAmount;
			this.lastChargedDate = lastChargedDate;
			this.nextRenewalDate = nextRenewalDate;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
			
		}

		public SubscriptionBuilder(String id, String programCode, String subscriptionCatalogId, String accountNumber, String membershipCode,
				String promoCode, Double cost, Integer pointsValue, Integer freeDuration, Integer validityPeriod,
				Date startDate, Date endDate, String status, String paymentMethod, String subscriptionMethod, String subscriptionSegment, String subscriptionChannel,
				String unSubscriptionChannel, Date unSubscriptionDate, String phoneyTunesPackageId, String transactionId, Double lastChargedAmount, 
				Date lastChargedDate, Date nextRenewalDate, Date updatedDate, String updatedUser) {
			super();
			this.id = id;
			this.programCode = programCode;
			this.subscriptionCatalogId = subscriptionCatalogId;
			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
			this.promoCode = promoCode;
			this.cost = cost;
			this.pointsValue = pointsValue;
			this.freeDuration = freeDuration;
			this.validityPeriod = validityPeriod;
			this.startDate = startDate;
			this.endDate = endDate;
			this.status = status;
			this.paymentMethod = paymentMethod;
			this.subscriptionMethod = subscriptionMethod;
			this.subscriptionSegment = subscriptionSegment;
			this.subscriptionChannel = subscriptionChannel;
			this.unSubscriptionChannel = unSubscriptionChannel;
			this.unSubscriptionDate = unSubscriptionDate;
			this.phoneyTunesPackageId = phoneyTunesPackageId;
			this.transactionId = transactionId;
			this.lastChargedAmount = lastChargedAmount;
			this.lastChargedDate = lastChargedDate;
			this.nextRenewalDate = nextRenewalDate;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
			
		}

		public SubscriptionDomain build() {
			return new SubscriptionDomain(this);
		}
	}
	
	public Subscription saveSubscription(SubscriptionDomain subscriptionDomain) throws SubscriptionManagementException {
		LOG.info("Domain Object To Be Persisted: {}", subscriptionDomain);
		try {			
			Subscription subscription = modelMapper.map(subscriptionDomain, Subscription.class);			
			this.subscriptionRepository.save(subscription);				
			LOG.info("Persisted Object: {}", subscription);
			return subscription;
			
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving merchant to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscription",
					mongoException.getClass() + mongoException.getMessage(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED);	
		} catch (Exception e) {		
			LOG.error("Exception occured while saving subscription to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		
		}
	}
	
	public Subscription updateSubscription(SubscriptionDomain subscriptionDomain, Subscription updateSubscription) throws SubscriptionManagementException {
		LOG.info("Domain Object To Be Updated: {}", subscriptionDomain);
		try {
			this.subscriptionRepository.save(modelMapper.map(subscriptionDomain, Subscription.class));
			LOG.info("Updated Object: {}", updateSubscription);
			return updateSubscription;
		} catch (Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "updateSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED);
		}
	}
	
	public void updateSubscriptionStatus(Subscription subscription, String subscriptionStatus, Headers headers) throws SubscriptionManagementException {
		try {	
			Subscription fetchedSubscription = subscription;
			LOG.info("updateSubscriptionStatus : {} {}",subscriptionStatus, headers.getUserName());
			subscription.setStatus(subscriptionStatus);
			subscription.setUpdatedUser(headers.getUserName());
			subscription.setUpdatedDate(new Date());
			Subscription savedSubscription = this.subscriptionRepository.save(subscription);	
			auditService.updateDataAudit(DBConstants.SUBSCRIPTION, savedSubscription, SubscriptionRequestMappingConstants.SUBSCRIPTION_RENEWAL, fetchedSubscription, headers.getExternalTransactionId(), headers.getUserName());
		} catch (Exception e) {		
			LOG.info("Exception occured while updating subscription status.");
			throw new SubscriptionManagementException(this.getClass().toString(), "updateSubscriptionStatus",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED);
		
		} 
	}
		
	public void cancelSubscription(ManageSubscriptionRequestDto manageSubscriptionRequestDto, Subscription subscription, String subscriptionCancelAction, Headers headers) throws SubscriptionManagementException {
		try {	
			LOG.info("cancelSubscription : {}", headers.getUserName());
			if(!ObjectUtils.isEmpty(subscription.getNextRenewalDate()) && subscriptionUtils.checkFutureDate(subscription.getNextRenewalDate()) 
					&& StringUtils.isEmpty(subscriptionCancelAction) 
					&& (headers.getChannelId().equalsIgnoreCase(SubscriptionManagementConstants.ADMIN_PORTAL.get()) 
							|| headers.getChannelId().equalsIgnoreCase(SubscriptionManagementConstants.CHANNEL_ID_SAPP.get()))) {
				subscription.setEndDate(subscription.getNextRenewalDate());
			} else {
				subscription.setStatus(SubscriptionManagementConstants.CANCELLED_STATUS.get());
			}
			subscription.setUnSubscriptionChannel(headers.getChannelId());
			subscription.setUnSubscriptionDate(new Date());
			subscription.setUpdatedUser(headers.getUserName());
			subscription.setUpdatedDate(new Date());
			String cancellationReason = SubscriptionManagementConstants.REASON_NOT_APPLICABLE.get();
			if(!ObjectUtils.isEmpty(manageSubscriptionRequestDto)) {
				cancellationReason = !StringUtils.isEmpty(manageSubscriptionRequestDto.getCancellationReason()) ? manageSubscriptionRequestDto.getCancellationReason() : SubscriptionManagementConstants.REASON_NOT_APPLICABLE.get();
			}
			subscription.setCancellationReason(cancellationReason);
			this.subscriptionRepository.save(subscription);			
		} catch (Exception e) {		
			LOG.info("Exception occured while updating subscription status.");
			throw new SubscriptionManagementException(this.getClass().toString(), "cancelSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED);
		
		} 
	}
	
	public void extendSubscription(Subscription subscription, Date endDate, String userName) throws SubscriptionManagementException {
		try {	
			LOG.info("extendSubscription : {}", userName);
			subscription.setEndDate(endDate);
			subscription.setUpdatedUser(userName);
			subscription.setUpdatedDate(new Date());
			this.subscriptionRepository.save(subscription);			
		} catch (Exception e) {		
			LOG.info("Exception occured while extending subscription.");
			throw new SubscriptionManagementException(this.getClass().toString(), "extendSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_EXTENSION_FAILED);
		
		} 
	}
	
	public void updateSubscriptionForCharging(Subscription subscription, Double lastChargedAmount, Date lastChargedDate, Date nextRenewalDate, String subscriptionStatus, Headers headers) throws SubscriptionManagementException {
		try {	
			Subscription fetchedSubscription = subscription;
			LOG.info("updateSubscriptionForCharging");
			subscription.setStatus(subscriptionStatus);
			subscription.setUpdatedUser(headers.getUserName());
			subscription.setUpdatedDate(new Date());
			if(null != lastChargedAmount) {
				subscription.setLastChargedAmount(lastChargedAmount);
			}
			if(null != lastChargedDate) {
				subscription.setLastChargedDate(lastChargedDate);
			}
			if(null != nextRenewalDate) {
				nextRenewalDate = Utilities.setTimeInDate(nextRenewalDate, SubscriptionManagementConstants.UTC_DATE_TIME.get());
				subscription.setNextRenewalDate(nextRenewalDate);
			}
			
			Subscription savedSubscription = this.subscriptionRepository.save(subscription);	
			auditService.updateDataAudit(DBConstants.SUBSCRIPTION, savedSubscription, SubscriptionRequestMappingConstants.SUBSCRIPTION_RENEWAL, fetchedSubscription, headers.getExternalTransactionId(), headers.getUserName());
		} catch (Exception e) {		
			LOG.info("Exception occured while updating subscription charging.");
			throw new SubscriptionManagementException(this.getClass().toString(), "updateSubscriptionForCharging",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_UPDATION_FAILED);
		
		} 
	}
	
	public void deleteSubscription(String subscriptionId) throws SubscriptionManagementException {
		try {
			subscriptionRepository.deleteById(subscriptionId);
		} catch (Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "deleteSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_DELETION_FAILED);
		}
	}
	
	public Optional<Subscription> findSubscription(String subscriptionCatalogId) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findBySubscriptionCatalogId(subscriptionCatalogId);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findSubscription",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public Optional<Subscription> findBySubscriptionCatalogIdAndAccountNumber(String subscriptionCatalogId, String accountNumber) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findBySubscriptionCatalogIdAndAccountNumber(subscriptionCatalogId, accountNumber);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findBySubscriptionCatalogIdAndAccountNumber",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public Optional<Subscription> findBySubscriptionCatalogIdAndAccountNumberAndStatus(String subscriptionCatalogId, String accountNumber, String status) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findBySubscriptionCatalogIdAndAccountNumberAndStatus(subscriptionCatalogId, accountNumber, status);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findBySubscriptionCatalogIdAndAccountNumberAndStatus",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public Optional<Subscription> findSubscriptionForAccountWithStatus(List<String> accountNumber, String subscriptionSegment) throws SubscriptionManagementException {
		try {
            List<Subscription> subscriptionList = subscriptionRepositoryHelper.findSubscriptionStatusForAccountList(accountNumber, subscriptionSegment);
            LOG.info("findSubscriptionForAccountWithStatus :: subscriptionList : {}",subscriptionList);
            if(!CollectionUtils.isEmpty(subscriptionList) && subscriptionList.size() == 1) {
            		return Optional.of(subscriptionList.get(0));
            } else if(CollectionUtils.isEmpty(subscriptionList)) {
            	return Optional.empty();
            } else {
            	throw new SubscriptionManagementException(SubscriptionManagementCode.MULTIPLE_SUBSCRIPTION_FOUND);
            }
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByAccountNumberAndStatus",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public List<Subscription> findByAccountNumberAndStatusIn(String accountNumber, List<String> status) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByAccountNumberAndStatusIn(accountNumber, status);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByAccountNumberAndStatus",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public Optional<Subscription> findByAccountNumberAndStatus(String accountNumber) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByAccountNumberAndStatusAndSubscriptionSegmentNot(accountNumber, 
					SubscriptionManagementConstants.SUBSCRIBED_STATUS.get(), SubscriptionManagementConstants.SEGMENT_FOOD.get());
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByAccountNumberAndStatus",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public List<Subscription> findByAccountNumber(String accountNumber) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByAccountNumber(accountNumber);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByAccountNumber",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public Optional<Subscription> findByIdAndStatusAndProgramCode(String id, String status, String program) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByIdAndStatusAndProgramCodeIgnoreCase(id, status, program);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByIdAndStatusAndProgramCode",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	} 
	
	public List<Subscription> findByAccountNumberAndProgramCode(String account, String program) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByAccountNumberAndProgramCodeIgnoreCase(account, program);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByAccountNumberAndProgramCode",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	} 
	
	public Optional<Subscription> findById(String id) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findById(id);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findById",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	} 
	
	public List<Subscription> findByAccountNumberIn(List<String> accountNumber) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByAccountNumberIn(accountNumber);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByAccountNumberIn",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
		
	}
	
	public List<Subscription> findByMembershipCodeAndSubscriptionCatalogIdAndStatusIn(String membershipCode, String subscriptionCatalogId, List<String> status) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByMembershipCodeAndSubscriptionCatalogIdAndStatusIn(membershipCode, subscriptionCatalogId, status);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByMembershipCodeAndSubscriptionCatalogIdAndStatusIn",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
	}
	
	public List<Subscription> findByAccountNumberAndSubscriptionCatalogIdAndStatusIn(String accountNumber, String subscriptionCatalogId, List<String> status) throws SubscriptionManagementException {
		try {
			return subscriptionRepository.findByAccountNumberAndSubscriptionCatalogIdAndStatusIn(accountNumber, subscriptionCatalogId, status);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByAccountNumberAndSubscriptionCatalogIdAndStatusIn",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTION_LISTING_FAILED);
		}
	}
	
}
