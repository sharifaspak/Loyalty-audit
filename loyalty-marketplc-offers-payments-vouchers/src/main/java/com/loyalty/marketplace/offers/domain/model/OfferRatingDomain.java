package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.OfferRatingDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferRating;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRatingRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.offers.utils.OfferValidator;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
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
public class OfferRatingDomain {

	private static final Logger LOG = LoggerFactory.getLogger(OfferRatingDomain.class);

	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;

	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;

	@Getter(AccessLevel.NONE)
	@Autowired
	OfferRatingRepository offerRatingRepository;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	OfferRepository offerRepository;
		
	@Getter(AccessLevel.NONE)
	@Autowired
	OfferCatalogDomain offerCatalogDomain;
	
	private String id;
	private String programCode;
	private String offerId;
	private List<MemberRatingDomain> memberRatings;
	private Double averageRating;
	private Integer ratingCount;
	private Integer commentCount;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;

	public OfferRatingDomain(OfferRatingBuilder offerRating) {

		this.id = offerRating.id;
		this.programCode = offerRating.programCode;
		this.offerId = offerRating.offerId;
		this.memberRatings = offerRating.memberRatings;
		this.averageRating = offerRating.averageRating;
		this.ratingCount = offerRating.ratingCount;
		this.commentCount = offerRating.commentCount;
		this.createdUser = offerRating.createdUser;
		this.createdDate = offerRating.createdDate;
		this.updatedUser = offerRating.updatedUser;
		this.updatedDate = offerRating.updatedDate;

	}

	public static class OfferRatingBuilder {

		private String id;
		private String programCode;
		private String offerId;
		private List<MemberRatingDomain> memberRatings;
		private Double averageRating;
		private Integer ratingCount;
		private Integer commentCount;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;

		public OfferRatingBuilder(String id) {

			this.id = id;
			
		}
		
		public OfferRatingBuilder(String offerId, List<MemberRatingDomain> memberRatings,
				Double averageRating) {

			this.offerId = offerId;
			this.memberRatings = memberRatings;
			this.averageRating = averageRating;
		}

		public OfferRatingBuilder id(String id) {
			this.id = id;
			return this;
		}

		public OfferRatingBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public OfferRatingBuilder ratingCount(Integer ratingCount) {
			this.ratingCount = ratingCount;
			return this;
		}

		public OfferRatingBuilder commentCount(Integer commentCount) {
			this.commentCount = commentCount;
			return this;
		}

		public OfferRatingBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public OfferRatingBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public OfferRatingBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public OfferRatingBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public OfferRatingDomain build() {
			return new OfferRatingDomain(this);
		}

	}

	/**
	 * 
	 * @param offerRatingRequest
	 * @param headers
	 * @return
	 */
	public ResultResponse validateAndSaveOfferRating(OfferRatingDto offerRatingRequest, Headers headers) {

		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());

		try {
			
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));

			if (!OfferValidator.validateDto(offerRatingRequest, validator, resultResponse)) {
				resultResponse.setResult(OfferErrorCodes.OFFER_RATING_FAILED.getId(),
						OfferErrorCodes.OFFER_RATING_FAILED.getMsg());
				return resultResponse;
			}

			GetMemberResponse getMemberResponse = fetchServiceValues
					.getMemberDetails(offerRatingRequest.getAccountNumber(), resultResponse, headers);
			
			if (getMemberResponse == null) {
				resultResponse.setErrorAPIResponse(OfferErrorCodes.OFFER_RATING_MEMBER_UNAVAILABLE.getIntId(),
						OfferErrorCodes.OFFER_RATING_MEMBER_UNAVAILABLE.getMsg());
				resultResponse.setResult(OfferErrorCodes.OFFER_RATING_FAILED.getId(),
						OfferErrorCodes.OFFER_RATING_FAILED.getMsg());
				return resultResponse;
			} 

			OfferCatalog offerExists = offerRepository.findByOfferId(offerRatingRequest.getOfferId());

			OfferRatingDomain domainToSave = null;
			if (null == offerExists) {
				resultResponse.setErrorAPIResponse(OfferErrorCodes.OFFER_RATING_OFFER_UNAVAILABLE.getIntId(),
						OfferErrorCodes.OFFER_RATING_OFFER_UNAVAILABLE.getMsg() + offerRatingRequest.getOfferId());
				resultResponse.setResult(OfferErrorCodes.OFFER_RATING_FAILED.getId(),
						OfferErrorCodes.OFFER_RATING_FAILED.getMsg());
				return resultResponse;
			}

			OfferRating oldOfferRating = null;
			OfferRating offerRatingExists = offerRatingRepository.findByOfferId(offerRatingRequest.getOfferId());
			if (null != offerRatingExists) {
				
				Gson gson = new Gson();
				oldOfferRating  = gson.fromJson(gson.toJson(offerRatingExists), OfferRating.class);

				domainToSave = Checks.checkCommentsForAccountExists(offerRatingExists, offerRatingRequest.getAccountNumber(),
						getMemberResponse.getMembershipCode()) 
						? DomainConfiguration.createOfferRatingDomainForAccount(offerRatingExists, offerRatingRequest,
								getMemberResponse, headers.getUserName(), OfferConstants.OFFER_RATING_FOR_EXISTING_ACCOUNT.get())
						: DomainConfiguration.createOfferRatingDomainForAccount(offerRatingExists, offerRatingRequest,
								getMemberResponse, headers.getUserName(), OfferConstants.OFFER_RATING_FOR_NEW_ACCOUNT.get()); 

			} else {
				
				domainToSave = DomainConfiguration.createOfferRatingDomainNewOffer(offerRatingRequest, getMemberResponse,
						headers.getUserName(), headers.getProgram());
				
			}

			saveUpdateOfferRating(domainToSave,
					null != offerRatingExists ? OfferConstants.UPDATE_ACTION.get() : OfferConstants.INSERT_ACTION.get(),
							headers, oldOfferRating, offerExists);

			resultResponse.setResult(OfferSuccessCodes.OFFER_RATING_SUCCESS.getId(),
					OfferSuccessCodes.OFFER_RATING_SUCCESS.getMsg());


		} catch (MarketplaceException e) {

			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.VALIDATE_AND_SAVE_OFFER_RATING.get(),
					e.getClassName() + OfferConstants.COMMA_OPERATOR.get() + e.getMethodName()
							+ OfferConstants.MESSAGE_SEPARATOR.get() + e.getDetailMessage(),
					OfferErrorCodes.OFFER_RATING_FAILED).printMessage());
			resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg() + OfferConstants.REFER_LOGS.get());
			resultResponse.setResult(OfferErrorCodes.OFFER_RATING_FAILED.getId(),
					OfferErrorCodes.OFFER_RATING_FAILED.getMsg());

		} catch (Exception e) {

			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.VALIDATE_AND_SAVE_OFFER_RATING.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.SAVE_OFFER_RATING_DOMAIN_RUNTIME_EXCEPTION)
							.printMessage());

			resultResponse.setErrorAPIResponse(OfferExceptionCodes.SAVE_OFFER_RATING_DOMAIN_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.SAVE_OFFER_RATING_DOMAIN_RUNTIME_EXCEPTION.getMsg() + OfferConstants.REFER_LOGS.get());
			resultResponse.setResult(OfferErrorCodes.OFFER_RATING_FAILED.getId(),
					OfferErrorCodes.OFFER_RATING_FAILED.getMsg());

		}

		return resultResponse;

	}

	/**
	 * 
	 * @param offerRatingDomain
	 * @param action
	 * @param headers
	 * @param oldOfferRating
	 * @param offerExists
	 * @return
	 * @throws MarketplaceException
	 */
	public OfferRating saveUpdateOfferRating(OfferRatingDomain offerRatingDomain, String action,
			Headers headers, OfferRating oldOfferRating, OfferCatalog offerExists) throws MarketplaceException {

		try {

			OfferRating offerRating = modelMapper.map(offerRatingDomain, OfferRating.class);
			OfferRating savedOfferRating = offerRatingRepository.save(offerRating);
			
			offerCatalogDomain.setOfferRating(offerExists, savedOfferRating, headers);
			if (action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())) {

//				auditService.insertDataAudit(OffersDBConstants.OFFER_RATING, savedOfferRating,
//						OffersRequestMappingConstants.RATE_OFFER, headers.getExternalTransactionId(), headers.getUserName());

			} else if (action.equalsIgnoreCase(OfferConstants.UPDATE_ACTION.get())) {
               
				auditService.updateDataAudit(OffersDBConstants.OFFER_CATALOG, savedOfferRating,
						OffersRequestMappingConstants.RATE_OFFER, oldOfferRating, headers.getExternalTransactionId(), headers.getUserName());
			}
			
			offerCatalogDomain.getAndSaveEligibleOffers(headers);
			return savedOfferRating;

		} catch (MongoException mongoException) {

			throw new MarketplaceException(this.getClass().toString(),
					OfferConstants.SAVE_OFFER_RATING_REPOSITORY_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferErrorCodes.OFFER_RATING_FAILED);

		}  catch (Exception e) {

			OfferExceptionCodes exception = (offerRatingDomain.getId() == null)
					? OfferExceptionCodes.SAVE_OFFER_RATING_DOMAIN_RUNTIME_EXCEPTION
					: OfferExceptionCodes.UPDATE_OFFER_RATING_DOMAIN_RUNTIME_EXCEPTION;

			throw new MarketplaceException(this.getClass().toString(),
					OfferConstants.SAVE_OFFER_REPOSITORY_METHOD.get(), e.getClass() + e.getMessage(), exception);

		}

	}

}
