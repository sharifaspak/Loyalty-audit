package com.loyalty.marketplace.image.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.image.constants.ImageCodes;
import com.loyalty.marketplace.image.constants.ImageConfigurationConstants;
import com.loyalty.marketplace.image.constants.ImageConstants;
import com.loyalty.marketplace.image.helper.ImageValidator;
import com.loyalty.marketplace.image.helper.Utility;
import com.loyalty.marketplace.image.inbound.dto.BannerPropertiesRequest;
import com.loyalty.marketplace.image.outbound.database.entity.BannerProperties;
import com.loyalty.marketplace.image.outbound.database.repository.BannerPropertiesRepository;
import com.loyalty.marketplace.image.outbound.dto.ListBannerPropertiesResponse;
import com.loyalty.marketplace.image.outbound.dto.ListBannerPropertiesResult;
import com.loyalty.marketplace.image.outbound.dto.ResultResponse;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoWriteException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Component
@AllArgsConstructor
@NoArgsConstructor
public class BannerPropertiesDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(BannerPropertiesDomain.class);
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	Validator validator;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	BannerPropertiesRepository bannerPropertiesRepository;
	
	@Autowired
	ProgramManagement programManagement;
	
	private String id;
	private String programCode;
	private Integer topBannerLimit;
	private Integer middleBannerLimit;
	private Integer bottomBannerLimit;
	private boolean includeRedeemedOffers;
	private Integer personalizeBannerCount;
	private Integer fixedBannerCount;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;
	
	public BannerPropertiesDomain(BannerPropertiesBuilder propertiesBuilder) {
		this.id = propertiesBuilder.id;
		this.programCode = propertiesBuilder.programCode;
		this.topBannerLimit = propertiesBuilder.topBannerLimit;
		this.middleBannerLimit = propertiesBuilder.middleBannerLimit;
		this.bottomBannerLimit = propertiesBuilder.bottomBannerLimit;
		this.includeRedeemedOffers = propertiesBuilder.includeRedeemedOffers;
		this.personalizeBannerCount = propertiesBuilder.personalizeBannerCount;
		this.fixedBannerCount = propertiesBuilder.fixedBannerCount;
		this.createdUser = propertiesBuilder.createdUser;
		this.createdDate = propertiesBuilder.createdDate;
		this.updatedUser = propertiesBuilder.updatedUser;
		this.updatedDate = propertiesBuilder.updatedDate;
	}
	
	public static class BannerPropertiesBuilder {
		
		private String id;
		private String programCode;
		private Integer topBannerLimit;
		private Integer middleBannerLimit;
		private Integer bottomBannerLimit;
		private boolean includeRedeemedOffers;
		private Integer personalizeBannerCount;
		private Integer fixedBannerCount;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;

		public BannerPropertiesBuilder(Integer topBannerLimit, Integer middleBannerLimit, Integer bottomBannerLimit,
				boolean includeRedeemedOffers, Integer personalizeBannerCount, Integer fixedBannerCount) {
			super();
			this.topBannerLimit = topBannerLimit;
			this.middleBannerLimit = middleBannerLimit;
			this.bottomBannerLimit = bottomBannerLimit;
			this.includeRedeemedOffers = includeRedeemedOffers;
			this.personalizeBannerCount = personalizeBannerCount;
			this.fixedBannerCount = fixedBannerCount;
		}

		public BannerPropertiesBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public BannerPropertiesBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public BannerPropertiesBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public BannerPropertiesBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		
		public BannerPropertiesBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public BannerPropertiesBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public BannerPropertiesDomain build() {
			return new BannerPropertiesDomain(this);
		}
		
	}
	
	/**
	 * Method to configure banner properties.
	 * @param bannerPropertiesRequest
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse configureBannerProperty(BannerPropertiesRequest bannerPropertiesRequest, ResultResponse resultResponse, Headers headers) {
		 
		try {

			bannerPropertiesRequest.setProgram(programManagement.getProgramCode(headers.getProgram()));

			if (!ImageValidator.validateRequestParameters(bannerPropertiesRequest, validator, resultResponse)) {
				resultResponse.setResult(ImageCodes.BANNER_PROPERTY_CONFIGURE_FAILURE.getId(),
						ImageCodes.BANNER_PROPERTY_CONFIGURE_FAILURE.getMsg());
				return resultResponse;
			}
			
			if(!Utility.setBooleanValueConfigureBannerProperty(bannerPropertiesRequest, resultResponse)) return resultResponse;
			
			BannerPropertiesDomain bannerPropertiesDomain = createBannerPropertDomain(bannerPropertiesRequest);
			saveBannerPropertyToDatabase(bannerPropertiesDomain, ImageConstants.ACTION_INSERT, bannerPropertiesRequest, null);

			resultResponse.setResult(ImageCodes.BANNER_PROPERTY_CONFIGURE_SUCCESS.getId(),
					ImageCodes.BANNER_PROPERTY_CONFIGURE_SUCCESS.getMsg());
			
		} catch (Exception e) {
			resultResponse.setResult(ImageCodes.BANNER_PROPERTY_CONFIGURE_FAILURE.getId(),
        			ImageCodes.BANNER_PROPERTY_CONFIGURE_FAILURE.getMsg());
        	resultResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_CONFIGURE,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
	}

	/**
	 * Create banner properties domain to insert to BannerProperties collection in DB.
	 * @param bannerPropertiesRequest
	 * @return
	 */
	private BannerPropertiesDomain createBannerPropertDomain(BannerPropertiesRequest bannerPropertiesRequest) {
		return new BannerPropertiesDomain.BannerPropertiesBuilder(
				bannerPropertiesRequest.getTopBannerLimit(), bannerPropertiesRequest.getMiddleBannerLimit(),
				bannerPropertiesRequest.getBottomBannerLimit(), bannerPropertiesRequest.isInclRedeemedOffer(),
				bannerPropertiesRequest.getPersonalizeBannerCount(), bannerPropertiesRequest.getFixedBannerCount())
						.programCode(bannerPropertiesRequest.getProgram())
						.createdUser(null != bannerPropertiesRequest.getUserName() ? bannerPropertiesRequest.getUserName() : bannerPropertiesRequest.getToken())
						.createdDate(new Date())
						.build();
	}
	
	/** 
	 * Method to update banner properties.
	 * @param bannerPropertiesRequest
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse updateBannerProperty(BannerPropertiesRequest bannerPropertiesRequest, ResultResponse resultResponse) {
		 
		try {

			BannerPropertiesDomain bannerPropertiesDomain = null;

			BannerProperties existingProperty;
			Optional<BannerProperties> property = bannerPropertiesRepository.findById(bannerPropertiesRequest.getId());
			if (property.isPresent()) {
				existingProperty = property.get();
				if(!Utility.setBooleanValueUpdateBannerProperty(bannerPropertiesRequest, existingProperty, resultResponse)) return resultResponse;
				bannerPropertiesDomain = updateBannerPropertDomain(bannerPropertiesRequest, existingProperty, resultResponse);
			} else {
				resultResponse.addErrorAPIResponse(ImageCodes.EXISTING_BANNER_PROPERTY_NOT_FOUND.getIntId(),
						ImageCodes.EXISTING_BANNER_PROPERTY_NOT_FOUND.getMsg());
				resultResponse.setResult(ImageCodes.BANNER_PROPERTY_UPDATE_FAILURE.getId(),
						ImageCodes.BANNER_PROPERTY_UPDATE_FAILURE.getMsg());
				return resultResponse;
			}

			saveBannerPropertyToDatabase(bannerPropertiesDomain, ImageConstants.ACTION_UPDATE, bannerPropertiesRequest, existingProperty);
		
			resultResponse.setResult(ImageCodes.BANNER_PROPERTY_UPDATE_SUCCESS.getId(),
					ImageCodes.BANNER_PROPERTY_UPDATE_SUCCESS.getMsg());
			
		} catch (Exception e) {
			resultResponse.setResult(ImageCodes.BANNER_PROPERTY_UPDATE_FAILURE.getId(),
        			ImageCodes.BANNER_PROPERTY_UPDATE_FAILURE.getMsg());
        	resultResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_UPDATE,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
	}
	
	/**
	 * Create banner properties domain to update BannerProperties collection in DB.
	 * @param bannerPropertiesRequest
	 * @param existingProperty
	 * @param resultResponse
	 * @return
	 */
	private BannerPropertiesDomain updateBannerPropertDomain(BannerPropertiesRequest bannerPropertiesRequest, BannerProperties existingProperty, ResultResponse resultResponse) {
	
		return new BannerPropertiesDomain.BannerPropertiesBuilder(
				null != bannerPropertiesRequest.getTopBannerLimit() ? bannerPropertiesRequest.getTopBannerLimit() : existingProperty.getTopBannerLimit(), 
				null != bannerPropertiesRequest.getMiddleBannerLimit() ? bannerPropertiesRequest.getMiddleBannerLimit() : existingProperty.getMiddleBannerLimit(),
				null != bannerPropertiesRequest.getBottomBannerLimit() ? bannerPropertiesRequest.getBottomBannerLimit() : existingProperty.getBottomBannerLimit(), 
				bannerPropertiesRequest.isInclRedeemedOffer(),
				null != bannerPropertiesRequest.getPersonalizeBannerCount() ? bannerPropertiesRequest.getPersonalizeBannerCount() : existingProperty.getPersonalizeBannerCount(), 
				null != bannerPropertiesRequest.getFixedBannerCount() ? bannerPropertiesRequest.getFixedBannerCount() : existingProperty.getFixedBannerCount())
				.id(bannerPropertiesRequest.getId())
				.programCode(null != bannerPropertiesRequest.getProgram() ? bannerPropertiesRequest.getProgram() : existingProperty.getProgramCode())
				.createdUser(existingProperty.getCreatedUser())
				.createdDate(existingProperty.getCreatedDate())
				.updatedUser(bannerPropertiesRequest.getUserName())
				.updatedDate(new Date())
				.build();

	}
	
	/**
	 * Method to list banner properties API.
	 * @param listBannerPropertiesResponse
	 * @return
	 */
	public ListBannerPropertiesResponse listBannerProperties(ListBannerPropertiesResponse listBannerPropertiesResponse) {
		
		try {

			List<BannerProperties> allBannerProperties = bannerPropertiesRepository.findAll();
			List<ListBannerPropertiesResult> bannerProperties = new ArrayList<>();

			if (!allBannerProperties.isEmpty()) {

				for (BannerProperties property : allBannerProperties) {
					ListBannerPropertiesResult listBannerPropertiesResult = modelMapper.map(property, ListBannerPropertiesResult.class);
					bannerProperties.add(listBannerPropertiesResult);
				}

				listBannerPropertiesResponse.setBannerProperties(bannerProperties);
				listBannerPropertiesResponse.setResult(ImageCodes.LIST_BANNER_PROPERTY_SUCCESS.getId(),
						ImageCodes.LIST_BANNER_PROPERTY_SUCCESS.getMsg());

			} else {
				listBannerPropertiesResponse.addErrorAPIResponse(ImageCodes.NO_BANNER_PROPERTY_AVAILABLE.getIntId(),
						ImageCodes.NO_BANNER_PROPERTY_AVAILABLE.getMsg());
				listBannerPropertiesResponse.setResult(ImageCodes.LIST_BANNER_PROPERTY_FAILED.getId(),
						ImageCodes.LIST_BANNER_PROPERTY_FAILED.getMsg());
			}

		} catch (Exception e) {
			listBannerPropertiesResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					ImageCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			listBannerPropertiesResponse.setResult(ImageCodes.LIST_BANNER_PROPERTY_FAILED.getId(),
					ImageCodes.LIST_BANNER_PROPERTY_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_LIST,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return listBannerPropertiesResponse;
		
	}
	
	/**
	 * Method to save banner properties to DB.
	 * @param bannerPropertiesDomain
	 * @param action
	 * @param bannerPropertiesRequest
	 * @param existingProperty
	 * @return
	 * @throws MarketplaceException
	 */
	public BannerProperties saveBannerPropertyToDatabase(BannerPropertiesDomain bannerPropertiesDomain, String action, BannerPropertiesRequest bannerPropertiesRequest, BannerProperties existingProperty) throws MarketplaceException {
		
		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.DOMAIN_TO_SAVE,
				this.getClass().getName(), ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_SAVE_DB, bannerPropertiesDomain);
		
		try {
			
			BannerProperties propertyToSave = modelMapper.map(bannerPropertiesDomain, BannerProperties.class);
			LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.ENTITY_TO_SAVE,
    				this.getClass().getName(), ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_SAVE_DB, propertyToSave);
			
			BannerProperties uploadedImage = bannerPropertiesRepository.save(propertyToSave);
        	LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.SAVED_ENTITY,
    				this.getClass().getName(), ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_SAVE_DB, uploadedImage);
        	
        	if (action.equalsIgnoreCase(ImageConstants.ACTION_INSERT)) {

//				auditService.insertDataAudit(ImageConfigurationConstants.DB_BANNER_PROPERTIES, uploadedImage,
//						ImageConfigurationConstants.ADD_BANNER_PROPERTY, bannerPropertiesRequest.getExternalTransactionId(), bannerPropertiesRequest.getUserName());

			} else if (action.equalsIgnoreCase(ImageConstants.ACTION_UPDATE)) {

				auditService.updateDataAudit(ImageConfigurationConstants.DB_BANNER_PROPERTIES, uploadedImage,
						ImageConfigurationConstants.UPDATE_BANNER_PROPERTY, existingProperty, bannerPropertiesRequest.getExternalTransactionId(), bannerPropertiesRequest.getUserName());
			
			}
        	
        	return uploadedImage;
		
		} catch (MongoWriteException mongoException) {
			throw new MarketplaceException(this.getClass().toString(),
					ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_SAVE_DB,
					mongoException.getClass() + mongoException.getMessage(), ImageCodes.BANNER_PROPERTY_MONGO_EXCEPTION);
		} catch (ValidationException validationException) {
			throw new MarketplaceException(this.getClass().toString(),
					ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_SAVE_DB,
					validationException.getClass() + validationException.getMessage(),
					ImageCodes.VALIDATION_EXCEPTION);
		} catch (Exception e) {
			throw new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.BANNER_PROPERTIES_DOMAIN_SAVE_DB,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION);
		}
		
	}
	
}
