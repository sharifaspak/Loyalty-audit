package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.BirthdayGiftTracker;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@Component
public class BirthdayGiftTrackerDomain {

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	private String id;
	private String programCode;
	private String membershipCode;
	private String accountNumber;
	private Date birthDate;
	private Date lastViewedDate;
	private Date createdAt;
	private String createdBy;
	private String updatedBy;
	private Date updatedAt;

	public BirthdayGiftTrackerDomain(BirthdayGiftTrackerDomainBuilder birthdayGiftTrackerDomainBuilder) {
		super();
		this.id = birthdayGiftTrackerDomainBuilder.id;
		this.programCode = birthdayGiftTrackerDomainBuilder.programCode;
		this.membershipCode = birthdayGiftTrackerDomainBuilder.membershipCode;
		this.accountNumber = birthdayGiftTrackerDomainBuilder.accountNumber;
		this.birthDate = birthdayGiftTrackerDomainBuilder.birthDate;
		this.lastViewedDate = birthdayGiftTrackerDomainBuilder.lastViewedDate;
		this.createdAt = birthdayGiftTrackerDomainBuilder.createdAt;
		this.createdBy = birthdayGiftTrackerDomainBuilder.createdBy;
		this.updatedBy = birthdayGiftTrackerDomainBuilder.updatedBy;
		this.updatedAt = birthdayGiftTrackerDomainBuilder.updatedAt;
	}

	public static class BirthdayGiftTrackerDomainBuilder {

		private String id;
		private String programCode;
		private String membershipCode;
		private String accountNumber;
		private Date birthDate;
		private Date lastViewedDate;
		private Date createdAt;
		private String createdBy;
		private String updatedBy;
		private Date updatedAt;

		public BirthdayGiftTrackerDomainBuilder(String membershipCode, String accountNumber, Date birthDate,
				Date lastViewedDate) {
			super();
			this.membershipCode = membershipCode;
			this.accountNumber = accountNumber;
			this.birthDate = birthDate;
			this.lastViewedDate = lastViewedDate;
		}
		
		public BirthdayGiftTrackerDomainBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
        public BirthdayGiftTrackerDomainBuilder id(String id) {
			
			this.id = id;
			return this;
		}
		
		public BirthdayGiftTrackerDomainBuilder createdAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public BirthdayGiftTrackerDomainBuilder createdBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}

		public BirthdayGiftTrackerDomainBuilder updatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public BirthdayGiftTrackerDomainBuilder updatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
			return this;
		}

		public BirthdayGiftTrackerDomain build() {
			return new BirthdayGiftTrackerDomain(this);
		}

	}
	
	/**
	 * Domain method to save/update birthday tracker to repository
	 * @param birthdayTrackerDomain
	 * @param birthdayTracker
	 * @param action
	 * @param headers
	 * @return saved birthday tracker
	 * @throws MarketplaceException
	 */
	public BirthdayGiftTracker saveUpdateBirthdayTracker(BirthdayGiftTrackerDomain birthdayTrackerDomain,
			BirthdayGiftTracker birthdayTracker, String action, Headers headers) throws MarketplaceException {
		
		try {			
			
			BirthdayGiftTracker birthdayTrackerToSave = modelMapper.map(birthdayTrackerDomain, BirthdayGiftTracker.class);
			BirthdayGiftTracker savedBirthdayTracker = repositoryHelper.saveBirthdayTracker(birthdayTrackerToSave);	
			
			if(action.equals(OfferConstants.INSERT_ACTION.get())) {
				
//				auditService.insertDataAudit(OffersDBConstants.BIRTHDAY_HELPER, savedBirthdayTracker, OffersRequestMappingConstants.GET_MEMBER_BIRTHDAY_GIFTS, headers.getExternalTransactionId(), headers.getUserName());
			
			} else if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.BIRTHDAY_HELPER, savedBirthdayTracker, OffersRequestMappingConstants.GET_MEMBER_BIRTHDAY_GIFTS, birthdayTracker,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
			return  savedBirthdayTracker;
			
		} catch (MongoException mongoException) {
			
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_BIRTHDAY_TRACKER_DOMAIN_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
			
			
		
		} catch (Exception e) {
		
			OfferExceptionCodes exception = (StringUtils.isEmpty(birthdayTrackerDomain.getId()) )
					? OfferExceptionCodes.BIRTHDAY_TRACKER_ADDITION_RUNTIME_EXCEPTION
					: OfferExceptionCodes.BIRTHDAY_TRACKER_UPDATION_RUNTIME_EXCEPTION; 
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_BIRTHDAY_TRACKER_DOMAIN_METHOD.get(),
					e.getClass() + e.getMessage(), exception);
		
		}
	}
	
}
