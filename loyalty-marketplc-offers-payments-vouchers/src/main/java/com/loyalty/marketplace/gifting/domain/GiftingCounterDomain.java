package com.loyalty.marketplace.gifting.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.outbound.database.entity.GiftingCounter;
import com.loyalty.marketplace.gifting.outbound.database.repository.GiftingCounterRepository;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class GiftingCounterDomain {

	private static final Logger LOG = LoggerFactory.getLogger(GiftingCounterDomain.class);

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	@Autowired
	GiftingCounterRepository giftingCounterRepository;
	
	private String id;
	private String programCode;
	private String giftType;
	private String level;
	private String accountNumber;
	private String membershipCode;
	private Double sentDayCount;
	private Double sentWeekCount;
	private Double sentMonthCount;
	private Double receivedDayCount;
	private Double receivedWeekCount;
	private Double receivedMonthCount;
	private Date lastResetDate;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;
	
	public GiftingCounterDomain(GiftingCounterBuilder giftingCounter) {

		this.id = giftingCounter.id;
		this.programCode = giftingCounter.programCode;
		this.giftType = giftingCounter.giftType;
		this.level = giftingCounter.level;
		this.accountNumber = giftingCounter.accountNumber;
		this.membershipCode = giftingCounter.membershipCode;
		this.sentDayCount = giftingCounter.sentDayCount;
		this.sentWeekCount = giftingCounter.sentWeekCount;
		this.sentMonthCount = giftingCounter.sentMonthCount;
		this.receivedDayCount = giftingCounter.receivedDayCount;
		this.receivedWeekCount = giftingCounter.receivedWeekCount;
		this.receivedMonthCount = giftingCounter.receivedMonthCount;
		this.lastResetDate = giftingCounter.lastResetDate;
		this.createdUser = giftingCounter.createdUser;
		this.createdDate = giftingCounter.createdDate;
		this.updatedUser = giftingCounter.updatedUser;
		this.updatedDate = giftingCounter.updatedDate;

	}

	public static class GiftingCounterBuilder {

		private String id;
		private String programCode;
		private String giftType;
		private String level;
		private String accountNumber;
		private String membershipCode;
		private Double sentDayCount;
		private Double sentWeekCount;
		private Double sentMonthCount;
		private Double receivedDayCount;
		private Double receivedWeekCount;
		private Double receivedMonthCount;
		private Date lastResetDate;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;

		public GiftingCounterBuilder(String giftType, String level) {
			this.giftType = giftType;
			this.level = level;
		}

		public GiftingCounterBuilder id(String id) {
			this.id = id;
			return this;
		}

		public GiftingCounterBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public GiftingCounterBuilder giftType(String giftType) {
			this.giftType = giftType;
			return this;
		}

		public GiftingCounterBuilder level(String level) {
			this.level = level;
			return this;
		}

		public GiftingCounterBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public GiftingCounterBuilder membershipCode(String membershipCode) {
			this.membershipCode = membershipCode;
			return this;
		}

		public GiftingCounterBuilder sentDayCount(Double sentDayCount) {
			this.sentDayCount = sentDayCount;
			return this;
		}

		public GiftingCounterBuilder sentWeekCount(Double sentWeekCount) {
			this.sentWeekCount = sentWeekCount;
			return this;
		}

		public GiftingCounterBuilder sentMonthCount(Double sentMonthCount) {
			this.sentMonthCount = sentMonthCount;
			return this;
		}

		public GiftingCounterBuilder receivedDayCount(Double receivedDayCount) {
			this.receivedDayCount = receivedDayCount;
			return this;
		}

		public GiftingCounterBuilder receivedWeekCount(Double receivedWeekCount) {
			this.receivedWeekCount = receivedWeekCount;
			return this;
		}

		public GiftingCounterBuilder receivedMonthCount(Double receivedMonthCount) {
			this.receivedMonthCount = receivedMonthCount;
			return this;
		}
		
		public GiftingCounterBuilder lastResetDate(Date lastResetDate) {
			this.lastResetDate = lastResetDate;
			return this;
		}

		public GiftingCounterBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public GiftingCounterBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public GiftingCounterBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public GiftingCounterBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public GiftingCounterDomain build() {
			return new GiftingCounterDomain(this);
		}

	}
	
	/**
	 * This method is used to save/update to the GiftingCounter collection.
	 * @param giftingCounterDomains
	 * @param headers
	 * @param existingGiftingCounterEntities
	 * @param api
	 * @return
	 * @throws MarketplaceException
	 */
	public List<GiftingCounter> saveUpdateGiftingCounter(List<GiftingCounterDomain> giftingCounterDomains,
			Headers headers, List<GiftingCounter> existingGiftingCounterEntities, String api) throws MarketplaceException {

		List<GiftingCounter> giftingCounterEntities = new ArrayList<>();
		List<GiftingCounter> newGiftingCounters = new ArrayList<>();
		List<GiftingCounter> existingGiftingCounters = new ArrayList<>();
		
		LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.DOMAIN_TO_SAVE,
				this.getClass().getName(), GiftingConfigurationConstants.GIFTING_COUNTER_DOMAIN_SAVE_DB,
				giftingCounterDomains);

		try {

			for(GiftingCounterDomain counterDomain : giftingCounterDomains) {
				GiftingCounter giftingCounter = modelMapper.map(counterDomain, GiftingCounter.class);
				giftingCounterEntities.add(giftingCounter);
				
				if(null != giftingCounter.getId()) { 
					existingGiftingCounters.add(giftingCounter);
				} else {
					newGiftingCounters.add(giftingCounter);
				}
				
			}
			
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.ENTITY_TO_SAVE,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_COUNTER_DOMAIN_SAVE_DB, giftingCounterEntities);

			List<GiftingCounter> savedGiftingCounters = giftingCounterRepository.saveAll(giftingCounterEntities);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.SAVED_ENTITY,
					this.getClass().getName(), GiftingConfigurationConstants.GIFTING_COUNTER_DOMAIN_SAVE_DB,
					savedGiftingCounters);

			if (!newGiftingCounters.isEmpty()) {
//				auditService.insertDataAudit(GiftingConfigurationConstants.GIFTING_COUNTER, existingGiftingCounters, api,
//						headers.getExternalTransactionId(), headers.getUserName());
			} 
			
			if (!existingGiftingCounters.isEmpty()) {
				auditService.updateDataAudit(GiftingConfigurationConstants.GIFTING_COUNTER, newGiftingCounters, api,
						existingGiftingCounterEntities, headers.getExternalTransactionId(), headers.getUserName());
			}

			return savedGiftingCounters;

		} catch (MongoWriteException mongoException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_COUNTER_DOMAIN_SAVE_DB,
					mongoException.getClass() + mongoException.getMessage(), GiftingCodes.GIFTING_LIMITS_CONFIG_FAILED);

		} catch (ValidationException validationException) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_COUNTER_DOMAIN_SAVE_DB,
					validationException.getClass() + validationException.getMessage(),
					GiftingCodes.VALIDATION_EXCEPTION);

		} catch (Exception e) {

			throw new MarketplaceException(this.getClass().toString(),
					GiftingConfigurationConstants.GIFTING_COUNTER_DOMAIN_SAVE_DB, e.getClass() + e.getMessage(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION);

		}

	}
	
}
