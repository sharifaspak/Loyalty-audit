package com.loyalty.marketplace.subscription.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NamingConventions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.inbound.dto.SubscribedOfferTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionDescriptionDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionMarketDescLineDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionMarketTitleLineDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionSubTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionTitleDto;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionTitleHeaderDto;
import com.loyalty.marketplace.subscription.inbound.dto.TermsAndConditionsDto;
import com.loyalty.marketplace.subscription.outbound.database.entity.FreeOffer;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
import com.loyalty.marketplace.subscription.outbound.dto.CuisinesResponseDto;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@Component
@Getter
@ToString
public class SubscriptionCatalogDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionCatalogDomain.class);
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	SubscriptionCatalogRepository subscriptionCatalogRepository;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	private String id;
	private SubscriptionTitleDto subscriptionTitle;
	private SubscriptionDescriptionDto subscriptionDescription;
	private TermsAndConditionsDto termsAndConditions;
	private SubscriptionTitleHeaderDto subscriptionTitleHeader;
	private SubscriptionSubTitleDto subscriptionSubTitle;
	private SubscribedOfferTitleDto subscribedOfferTitle;
	private SubscriptionMarketTitleLineDto subscriptionMarketTitleLine;
	private SubscriptionMarketDescLineDto subscriptionMarketDescLine;
	private String imageUrl;
	private List<MerchantsList> merchantsList;
	private List<CuisinesResponseDto> cuisinesList;
	private Double cost;
	private Integer pointsValue;
	private Integer freeDuration;
	private Integer validityPeriod;
	private Date startDate;
	private Date endDate;
	private String status;
	private String chargeabilityType;
	private List<PaymentMethods> paymentMethods;
	private List<String> customerSegments;
	private List<String> eligibleChannels;
	private List<LinkedOffers> linkedOffers;
	private List<FreeOffer> freeOffer;
	private List<Benefits> benefits;
	private String packageType;
	private String subscriptionSegment;
	private String subscriptionLogo;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	private String programCode;
	
	
	public SubscriptionCatalogDomain() {
		
	}
	
	public SubscriptionCatalogDomain(SubscriptionCatalogBuilder subscriptionCatalogBuilder) {
		super();
		this.id = subscriptionCatalogBuilder.id;
		this.subscriptionTitle = subscriptionCatalogBuilder.subscriptionTitle;
		this.subscriptionDescription = subscriptionCatalogBuilder.subscriptionDescription;
		this.termsAndConditions = subscriptionCatalogBuilder.termsAndConditions;
		this.subscriptionTitleHeader = subscriptionCatalogBuilder.subscriptionTitleHeader;
		this.subscriptionSubTitle = subscriptionCatalogBuilder.subscriptionSubTitle;
		this.subscribedOfferTitle = subscriptionCatalogBuilder.subscribedOfferTitle;
		this.subscriptionMarketTitleLine = subscriptionCatalogBuilder.subscriptionMarketTitleLine;
		this.subscriptionMarketDescLine = subscriptionCatalogBuilder.subscriptionMarketDescLine;
		this.imageUrl = subscriptionCatalogBuilder.imageUrl;
		this.merchantsList = subscriptionCatalogBuilder.merchantsList;
		this.cuisinesList = subscriptionCatalogBuilder.cuisinesList;
		this.cost = subscriptionCatalogBuilder.cost;
		this.pointsValue = subscriptionCatalogBuilder.pointsValue;
		this.freeDuration = subscriptionCatalogBuilder.freeDuration;
		this.validityPeriod = subscriptionCatalogBuilder.validityPeriod;
		this.startDate = subscriptionCatalogBuilder.startDate;
		this.endDate = subscriptionCatalogBuilder.endDate;
		this.status = subscriptionCatalogBuilder.status;
		this.chargeabilityType = subscriptionCatalogBuilder.chargeabilityType;				
		this.paymentMethods = subscriptionCatalogBuilder.paymentMethods;
		this.customerSegments = subscriptionCatalogBuilder.customerSegments;
		this.eligibleChannels = subscriptionCatalogBuilder.eligibleChannels;
		this.linkedOffers = subscriptionCatalogBuilder.linkedOffers;
		this.freeOffer = subscriptionCatalogBuilder.freeOffer;
		this.benefits = subscriptionCatalogBuilder.benefits;
		this.packageType = subscriptionCatalogBuilder.packageType;
		this.subscriptionSegment = subscriptionCatalogBuilder.subscriptionSegment;
		this.subscriptionLogo = subscriptionCatalogBuilder.subscriptionLogo;
		this.createdDate = subscriptionCatalogBuilder.createdDate;				
		this.createdUser = subscriptionCatalogBuilder.createdUser;
		this.updatedDate = subscriptionCatalogBuilder.updatedDate;
		this.updatedUser = subscriptionCatalogBuilder.updatedUser;
		this.programCode = subscriptionCatalogBuilder.programCode;
		
	}

	
	public static class SubscriptionCatalogBuilder {
		private String id;
		private SubscriptionTitleDto subscriptionTitle;
		private SubscriptionDescriptionDto subscriptionDescription;
		private TermsAndConditionsDto termsAndConditions;
		private SubscriptionTitleHeaderDto subscriptionTitleHeader;
		private SubscriptionSubTitleDto subscriptionSubTitle;
		private SubscribedOfferTitleDto subscribedOfferTitle;
		private SubscriptionMarketTitleLineDto subscriptionMarketTitleLine;
		private SubscriptionMarketDescLineDto subscriptionMarketDescLine;
		private String imageUrl;
		private List<MerchantsList> merchantsList;
		private List<CuisinesResponseDto> cuisinesList;
		private Double cost;
		private Integer pointsValue;
		private Integer freeDuration;
		private Integer validityPeriod;
		private Date startDate;
		private Date endDate;
		private String status;
		private String chargeabilityType;
		private List<PaymentMethods> paymentMethods;
		private List<String> customerSegments;
		private List<String> eligibleChannels;
		private List<LinkedOffers> linkedOffers;
		private List<FreeOffer> freeOffer;
		private List<Benefits> benefits;
		private String packageType;
		private String subscriptionSegment;
		private String subscriptionLogo;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;
		private String programCode;
		
		
		public SubscriptionCatalogBuilder(String id, SubscriptionTitleDto subscriptionTitle, SubscriptionDescriptionDto subscriptionDescription,
				TermsAndConditionsDto termsAndConditions, SubscriptionTitleHeaderDto subscriptionTitleHeader, SubscriptionSubTitleDto subscriptionSubTitle, 
				SubscribedOfferTitleDto subscribedOfferTitle, SubscriptionMarketTitleLineDto subscriptionMarketTitleLine, SubscriptionMarketDescLineDto subscriptionMarketDescLine, String imageUrl, List<MerchantsList> merchantsList, List<CuisinesResponseDto> cuisinesList,
				Double cost, Integer pointsValue, Integer freeDuration, Integer validityPeriod, Date startDate,
				Date endDate, String status, String chargeabilityType, List<PaymentMethods> paymentMethods,
				List<String> customerSegments, List<String> eligibleChannels, List<LinkedOffers> linkedOffers, List<FreeOffer> freeOffer, List<Benefits> benefits,
				String packageType, String subscriptionSegment, Date createdDate, String createdUser, Date updatedDate, String updatedUser, String programCode) {
			super();
			this.id = id;
			this.subscriptionTitle = subscriptionTitle;
			this.subscriptionDescription = subscriptionDescription;
			this.termsAndConditions = termsAndConditions;
			this.subscriptionTitleHeader = subscriptionTitleHeader;
			this.subscriptionSubTitle = subscriptionSubTitle;
			this.subscribedOfferTitle = subscribedOfferTitle;
			this.subscriptionMarketTitleLine = subscriptionMarketTitleLine;
			this.subscriptionMarketDescLine = subscriptionMarketDescLine;
			this.imageUrl = imageUrl;
			this.merchantsList = merchantsList;
			this.cuisinesList = cuisinesList;
			this.cost = cost;
			this.pointsValue = pointsValue;
			this.freeDuration = freeDuration;
			this.validityPeriod = validityPeriod;
			this.startDate = startDate;
			this.endDate = endDate;
			this.status = status;
			this.chargeabilityType = chargeabilityType;
			this.paymentMethods = paymentMethods;
			this.customerSegments = customerSegments;
			this.eligibleChannels = eligibleChannels;
			this.linkedOffers = linkedOffers;
			this.freeOffer = freeOffer;
			this.benefits = benefits;
			this.packageType = packageType;
			this.subscriptionSegment = subscriptionSegment;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
			this.programCode = programCode;
			
		}
		
		public SubscriptionCatalogDomain build() {
			return new SubscriptionCatalogDomain(this);
		}
	}
	
	public void saveSubscriptionCatalog(SubscriptionCatalogDomain subscriptionCatalogDomain, String changeType, Headers header) throws SubscriptionManagementException {
		LOG.info("Domain Object To Be Persisted: {}", subscriptionCatalogDomain);
		try {
			ModelMapper mp = new ModelMapper();
			mp.getConfiguration()
			  .setMatchingStrategy(MatchingStrategies.LOOSE)
		      .setFieldMatchingEnabled(true)
		      .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
		      .setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
			SubscriptionCatalog subscriptionCatalog = mp.map(subscriptionCatalogDomain, SubscriptionCatalog.class);			
			this.subscriptionCatalogRepository.insert(subscriptionCatalog);	
//			auditService.insertDataAudit(SubscriptionManagementConstants.SUBSCRIPTION_CATALOG, savedSubscriptionCatalog, changeType, header.getExternalTransactionId(), header.getUserName());
			
			LOG.info("Persisted Object: {}", subscriptionCatalog);
			
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving merchant to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscriptionCatalog",
					mongoException.getClass() + mongoException.getMessage(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED);	
		} catch (Exception e) {		
			LOG.error("Exception occured while saving subscription catalog to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		
		}
	}
	
	
	public void updateSubscriptionCatalog(SubscriptionCatalogDomain subscriptionCatalogDomain, SubscriptionCatalog updateSubscriptionCatalog, String changeType, Headers header) throws SubscriptionManagementException {
		LOG.info("Domain Object To Be Updated: {}", subscriptionCatalogDomain);
		try {
			SubscriptionCatalog updatedSubscriptionCatalog = this.subscriptionCatalogRepository.save(modelMapper.map(subscriptionCatalogDomain, SubscriptionCatalog.class));
			auditService.updateDataAudit(SubscriptionManagementConstants.SUBSCRIPTION_CATALOG, updatedSubscriptionCatalog, changeType, updateSubscriptionCatalog, header.getExternalTransactionId(), header.getUserName());
			LOG.info("Persisted Object: {}", updateSubscriptionCatalog);
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving merchant to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "updateSubscriptionCatalog",
					mongoException.getClass() + mongoException.getMessage(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_UPDATION_FAILED);	
		} catch (Exception e) {
			LOG.error("Exception occured while updating subscription catalog to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "updateSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}
	}
	
	public void deleteSubscriptionCatalog(Optional<SubscriptionCatalog> subscriptionCatalog, String changeType, Headers header) throws SubscriptionManagementException {
		try {
			subscriptionCatalogRepository.deleteById(subscriptionCatalog.get().getId());
			auditService.deleteDataAudit(SubscriptionManagementConstants.SUBSCRIPTION_CATALOG, subscriptionCatalog, changeType, header.getExternalTransactionId(), header.getUserName());
		} catch (Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "deleteSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_DELETION_FAILED);
		}
	}
	
	public SubscriptionCatalog findSubscriptionCatalog(String id) throws SubscriptionManagementException {
		try {
			Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(id);
			if (subscriptionCatalog.isPresent()) {
				return subscriptionCatalog.get();
			}
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
		return null;
		
	}
	
	public List<SubscriptionCatalog> findSubscriptionCatalog(List<String> id) throws SubscriptionManagementException {
		try {
			List<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findByIdIn(id);
			if (!subscriptionCatalog.isEmpty()) {
				return subscriptionCatalog;
			}
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findSubscriptionCatalog",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
		return null;
		
	}
	
	public Optional<SubscriptionCatalog> findByIdAndStatus(String id, String status) throws SubscriptionManagementException {
		
		try {
			return subscriptionCatalogRepository.findByIdAndStatus(id, status);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByIdAndStatus",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
		
	}
	
	public Optional<SubscriptionCatalog> findByIdAndChargeabilityType(String id, String chargeabilityType) throws SubscriptionManagementException {
		try {
			return subscriptionCatalogRepository.findByIdAndChargeabilityType(id, chargeabilityType);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByIdAndChargeabilityType",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
		
	}
	
	public List<SubscriptionCatalog> findByChargeabilityTypeAndStatus(String chargeabilityType, String status) throws SubscriptionManagementException {
		try {
			return subscriptionCatalogRepository.findByChargeabilityTypeAndStatus(chargeabilityType, status);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByChargeabilityType",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
		
	}
	
	public List<SubscriptionCatalog> findByValidityPeriodAndStatus(Integer validityPeriod, String status) throws SubscriptionManagementException {
		try {
			return subscriptionCatalogRepository.findByValidityPeriodAndStatus(validityPeriod, status);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByValidityPeriodAndStatus",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
		
	}
	
	public List<SubscriptionCatalog> findByChargeabilityType(String cargeabilityType) throws SubscriptionManagementException {
		try {
			return subscriptionCatalogRepository.findByChargeabilityType(cargeabilityType);
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByChargeabilityType",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
		
	}
	
	public String fetchChargeabilityType(String subscriptionCatalogId) {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		if(subscriptionCatalog.isPresent()) {
			return subscriptionCatalog.get().getChargeabilityType();
		}
		return null;
	}
	
	public Map<String,String> fetchChargeabilityType(List<String> subscriptionCatalogIdList) {
		Map<String,String> catalogChargeabilityMap = new HashMap<String,String>();
		List<SubscriptionCatalog> subscriptionCatalogList = subscriptionCatalogRepository.findByIdIn(subscriptionCatalogIdList);
		for(SubscriptionCatalog subscriptionCatalog : subscriptionCatalogList) {
			catalogChargeabilityMap.put(subscriptionCatalog.getId(),subscriptionCatalog.getChargeabilityType());
		}
		return catalogChargeabilityMap;
	}
	
	public String fetchPackageType(String subscriptionCatalogId) {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		if(subscriptionCatalog.isPresent()) {
			return subscriptionCatalog.get().getPackageType();
		}
		return null;
	}
	
	public SubscriptionCatalog findByPackageType(String packageType) throws SubscriptionManagementException {
		try {
			Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findByPackageType(packageType);
			return subscriptionCatalog.isPresent() ? subscriptionCatalog.get() : null;
		} catch(Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "findByPackageType",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_LISTING_FAILED);
		}
	}
		 
}
