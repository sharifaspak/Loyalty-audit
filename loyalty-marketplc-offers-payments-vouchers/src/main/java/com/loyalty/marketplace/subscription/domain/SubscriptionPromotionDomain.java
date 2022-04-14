package com.loyalty.marketplace.subscription.domain;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPromotion;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionPromotionRepository;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@Component
@Getter
@ToString
public class SubscriptionPromotionDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPromotionDomain.class);
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	SubscriptionPromotionRepository subscriptionPromotionRepository;
	
	private String id;
	private String programCode;
	private String subscriptionCatalogId;
	private String validity;	
	private Date startDate;	
	private Date endDate;	
	private String discountPercentage;	
	private String flatRate;	
	private String chargeabilityType;	
	private int discountMonths;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	
	
	public SubscriptionPromotionDomain() {
		
	}
	
	public SubscriptionPromotionDomain(SubscriptionPromotionBuilder subscriptionPromotionBuilder) {
		super();
		this.id = subscriptionPromotionBuilder.id;
		this.programCode = subscriptionPromotionBuilder.programCode;	
		this.subscriptionCatalogId = subscriptionPromotionBuilder.subscriptionCatalogId;
		this.validity = subscriptionPromotionBuilder.validity;
		this.startDate = subscriptionPromotionBuilder.startDate;
		this.endDate = subscriptionPromotionBuilder.endDate;
		this.discountPercentage = subscriptionPromotionBuilder.discountPercentage;
		this.flatRate = subscriptionPromotionBuilder.flatRate;	
		this.chargeabilityType = subscriptionPromotionBuilder.chargeabilityType;
		this.discountMonths = subscriptionPromotionBuilder.discountMonths;
		this.createdDate = subscriptionPromotionBuilder.createdDate;				
		this.createdUser = subscriptionPromotionBuilder.createdUser;
		this.updatedDate = subscriptionPromotionBuilder.updatedDate;
		this.updatedUser = subscriptionPromotionBuilder.updatedUser;
	}

	public static class SubscriptionPromotionBuilder {
		private String id;
		private String programCode;
		private String subscriptionCatalogId;
		private String validity;	
		private Date startDate;	
		private Date endDate;	
		private String discountPercentage;	
		private String flatRate;	
		private String chargeabilityType;	
		private int discountMonths;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;
		
		
		public SubscriptionPromotionBuilder(String programCode, String subscriptionCatalogId,
				String validity, Date startDate, Date endDate, String discountPercentage, String flatRate,
				String chargeabilityType, int discountMonths, Date createdDate, String createdUser, Date updatedDate,
				String updatedUser) {
			super();
			this.programCode = programCode;
			this.subscriptionCatalogId = subscriptionCatalogId;
			this.validity = validity;
			this.startDate = startDate;
			this.endDate = endDate;
			this.discountPercentage = discountPercentage;
			this.flatRate = flatRate;
			this.chargeabilityType = chargeabilityType;
			this.discountMonths = discountMonths;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
		}


		public SubscriptionPromotionDomain build() {
			return new SubscriptionPromotionDomain(this);
		}
	}
	
	public void saveSubscriptionPromotion(SubscriptionPromotionDomain subscriptionPromotionDomain) throws SubscriptionManagementException {
		LOG.info("Domain Object To Be Persisted: {}", subscriptionPromotionDomain);
		try {			
			SubscriptionPromotion subscriptionPromotion = modelMapper.map(subscriptionPromotionDomain, SubscriptionPromotion.class);			
			this.subscriptionPromotionRepository.insert(subscriptionPromotion);				
			LOG.info("Persisted Object: {}", subscriptionPromotion);
			
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving promotion to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscriptionPromotion",
					mongoException.getClass() + mongoException.getMessage(),
					SubscriptionManagementCode.SUBSCRIPTIONPROMOTION_CREATION_FAILED);	
		} catch (Exception e) {		
			LOG.error("Exception occured while saving promotion catalog to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscriptionPromotion",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		
		}
	}
	
	
	public void deleteSubscriptionPromotion(String subscriptionPromotionId) throws SubscriptionManagementException {
		try {
			subscriptionPromotionRepository.deleteById(subscriptionPromotionId);
		} catch (Exception e) {
			throw new SubscriptionManagementException(this.getClass().toString(), "deleteSubscriptionPromotion",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.SUBSCRIPTIONCATALOG_DELETION_FAILED);
		}
	}

}
