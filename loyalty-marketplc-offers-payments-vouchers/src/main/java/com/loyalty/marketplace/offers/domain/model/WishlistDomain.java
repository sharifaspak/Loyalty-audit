package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.EligibilityInfo;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.WishlistRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.WishlistEntity;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponse;
import com.loyalty.marketplace.offers.outbound.dto.OfferCatalogResultResponseDto;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.DomainConfiguration;
import com.loyalty.marketplace.offers.utils.FilterValues;
import com.loyalty.marketplace.offers.utils.OfferValidator;
import com.loyalty.marketplace.offers.utils.Predicates;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.offers.utils.Responses;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoWriteException;

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
public class WishlistDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(WishlistDomain.class);
	
	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	OffersHelper helper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;
	
	private String id;
	private String program;
	private String accountNumber;
	private String membershipCode;
	private List<String> offers;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	
	
	public WishlistDomain(WishlistBuilder wishlist) {
		
		this.id = wishlist.id;
		this.program = wishlist.program;
		this.accountNumber = wishlist.accountNumber;
		this.membershipCode = wishlist.membershipCode;
		this.offers = wishlist.offers;
		this.usrCreated = wishlist.usrCreated;
		this.usrUpdated = wishlist.usrUpdated;
		this.dtCreated = wishlist.dtCreated;
		this.dtUpdated = wishlist.dtUpdated;
	}
	
	public static class WishlistBuilder {
		
		
		private String id;
		private String program;
		private String accountNumber;
		private String membershipCode;
		private List<String> offers;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;
		
		public WishlistBuilder(String id) {
			
			this.id = id;
		}
		
		public WishlistBuilder(String id, String program, String accountNumber, List<String> offers) {
			
			this.id = id;
			this.program = program;
			this.accountNumber = accountNumber;
			this.offers = offers;
		}
		
		public WishlistBuilder(String program, String accountNumber, List<String> offers) {
			
			this.program = program;
			this.accountNumber = accountNumber;
			this.offers = offers;
			
		}
		
		public WishlistBuilder program(String program) {
			this.program = program;
			return this;
		}
		
		public WishlistBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}
		
		public WishlistBuilder membershipCode(String membershipCode) {
			this.membershipCode = membershipCode;
			return this;
		}
		
		public WishlistBuilder offers(List<String> offers) {
			this.offers = offers;
			return this;
		}
		
		public WishlistBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}
		
		public WishlistBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public WishlistBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}

		public WishlistBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}
		
		public WishlistDomain build() {
			return new WishlistDomain(this);
		}

	}
	
	/**
	 * Domain method to save/update wishlist to the repository
	 * @param wishlistDomain
	 * @param wishlist
	 * @param action
	 * @param headers
	 * @return saved/updated wishlist
	 * @throws MarketplaceException
	 */
	public WishlistEntity saveUpdateWishlist(WishlistDomain wishlistDomain,
			WishlistEntity wishlist, String action, Headers headers) throws MarketplaceException {
		
		try {			
			
			WishlistEntity wishlistToSave = modelMapper.map(wishlistDomain, WishlistEntity.class);
			
			WishlistEntity savedWishlist = repositoryHelper.saveWishlist(wishlistToSave);	
			
			if(action.equals(OfferConstants.INSERT_ACTION.get())) {
				
//				auditService.insertDataAudit(OffersDBConstants.WISHLIST, savedWishlist, OffersRequestMappingConstants.CREATE_UPDATE_WISHLIST, headers.getExternalTransactionId(), headers.getUserName());
			
			} else if(action.equals(OfferConstants.UPDATE_ACTION.get())) {
				
				auditService.updateDataAudit(OffersDBConstants.WISHLIST, savedWishlist, OffersRequestMappingConstants.CREATE_UPDATE_WISHLIST, wishlist,  headers.getExternalTransactionId(), headers.getUserName());
			}
			
			return  savedWishlist;
			
		} catch (MongoWriteException mongoException) {
			
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_WISHLIST_DOMAIN_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferExceptionCodes.MONGO_WRITE_EXCEPTION);
			
			
		
		} catch (ValidationException validationException) {
		
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_WISHLIST_DOMAIN_METHOD.get(),
					validationException.getClass() + validationException.getMessage(),
					OfferExceptionCodes.VALIDATION_EXCEPTION);
		
		} catch (Exception e) {
		
			OfferExceptionCodes exception = (StringUtils.isEmpty(wishlistDomain.getId()))
					? OfferExceptionCodes.WISHLIST_ADDITION_RUNTIME_EXCEPTION
					: OfferExceptionCodes.WISHLIST_UPDATION_RUNTIME_EXCEPTION; 
			
			throw new MarketplaceException(this.getClass().toString(), OfferConstants.CONFIGURE_WISHLIST_DOMAIN_METHOD.get(),
					e.getClass() + e.getMessage(), exception);
		
		}
	}
	
	/**
	 * Domain method to validate conditions before saving offer to wishlist
	 * @param wishlistRequest
	 * @param accountNumber
	 * @param headers
	 * @return status after saving wishlist
	 */
	public ResultResponse validateAndSaveOfferToWishlist(WishlistRequestDto wishlistRequest,
			String accountNumber, Headers headers) {
		
		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
		
		try {
			
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			WishlistDomain wishlistOfferDomain = null;
			WishlistEntity wishlist = null;
			
			if(OfferValidator.validateDto(wishlistRequest, validator, resultResponse)){
				
				OfferCatalog offer = repositoryHelper.findByOfferId(wishlistRequest.getOfferId());
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offer), OfferErrorCodes.OFFER_WITH_ID_NOT_PRESENT, resultResponse)) {
					
					GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, headers);
					
					if(Checks.checkNoErrors(resultResponse)) {
						
						wishlist = repositoryHelper.findWishlistForSpecificAccount(accountNumber, memberDetails.getMembershipCode());
						wishlistOfferDomain = DomainConfiguration.getWishListDomainForAddingOffer(memberDetails, wishlist, headers.getProgram(), 
								accountNumber, wishlistRequest, headers.getUserName(), resultResponse);
						
					}
					
					saveUpdateWishlist(wishlistOfferDomain, wishlist, ProcessValues.getAction(wishlist), headers);
					
				} 
				
			}
			
		} catch (ValidationException e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_WISHLIST_DOMAIN.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.VALIDATION_EXCEPTION).printMessage());
			
			resultResponse.setErrorAPIResponse(OfferExceptionCodes.VALIDATION_EXCEPTION.getIntId(),
					OfferExceptionCodes.VALIDATION_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
			
			resultResponse.setResult(OfferErrorCodes.ADDING_TO_WISHLIST_FAILED.getId(),
					OfferErrorCodes.ADDING_TO_WISHLIST_FAILED.getMsg());
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.SAVE_WISHLIST_DOMAIN.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.ADD_TO_WISHLIST_DOMAIN_RUNTIME_EXCEPTION).printMessage());
			
			resultResponse.setErrorAPIResponse(OfferExceptionCodes.ADD_TO_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.ADD_TO_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
			
			resultResponse.setResult(OfferErrorCodes.ADDING_TO_WISHLIST_FAILED.getId(),
					OfferErrorCodes.ADDING_TO_WISHLIST_FAILED.getMsg());
				
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.ADDING_TO_WISHLIST_FAILED, OfferSuccessCodes.OFFER_ADDED_SUCCESSFULLY_TO_WISHLIST);
		return resultResponse;
		
	}

	/**
	 * Domain method to validate conditions before removing offer from wishlist
	 * @param wishlistRequest
	 * @param accountNumber
	 * @param headers
	 * @return status after updating wishlist
	 */
	public ResultResponse validateAndRemoveOfferFromWishlist(WishlistRequestDto wishlistRequest,
			String accountNumber, Headers headers) {
		
		ResultResponse resultResponse = new ResultResponse(headers.getExternalTransactionId());
		
		try {
			
			if(OfferValidator.validateDto(wishlistRequest, validator, resultResponse)
			&& Responses.setResponseAfterConditionCheck(StringUtils.equalsIgnoreCase(wishlistRequest.getAction(), OfferConstants.REMOVE_ACTION.get()),
					OfferErrorCodes.NOT_A_VALID_WISHLIST_ACTION, resultResponse)) {
				
				WishlistDomain wishlistOfferDomain = null;
				List<String> offerDomainList = null;
				OfferCatalog offer = repositoryHelper.findByOfferId(wishlistRequest.getOfferId());
				
				if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(offer), OfferErrorCodes.OFFER_WITH_ID_NOT_PRESENT, resultResponse)) {
					
					GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, headers);
					
					if(Checks.checkNoErrors(resultResponse)) {
						
						WishlistEntity wishlist = repositoryHelper.findWishlistForSpecificAccount(accountNumber, memberDetails.getMembershipCode());
						
						if(Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(wishlist), OfferErrorCodes.NO_WISHLIST_FOR_ACCOUNT, resultResponse)
						&& Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(wishlist.getOffers()), OfferErrorCodes.NO_WISHLIST_FOR_ACCOUNT, resultResponse)
						&& Responses.setResponseAfterConditionCheck(wishlist.getOffers().contains(wishlistRequest.getOfferId()), OfferErrorCodes.OFFER_NOT_IN_WISHLIST, resultResponse)) {
						
							offerDomainList = wishlist.getOffers().stream().filter(o->!o.equals(wishlistRequest.getOfferId()))
								    .collect(Collectors.toList());
						    wishlistOfferDomain = DomainConfiguration.getWishlistDomainForRemovingOffer(wishlist, accountNumber, offerDomainList, headers);
						    saveUpdateWishlist(wishlistOfferDomain, wishlist, ProcessValues.getAction(wishlist), headers);
							
					    }
						
					}
						
				}
				
			}
			
		} catch (ValidationException e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_WISHLIST_DOMAIN.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.VALIDATION_EXCEPTION).printMessage());
			
			resultResponse.setErrorAPIResponse(OfferExceptionCodes.VALIDATION_EXCEPTION.getIntId(),
					OfferExceptionCodes.VALIDATION_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
			
			resultResponse.setResult(OfferErrorCodes.REMOVING_FROM_WISHLIST_FAILED.getId(),
					OfferErrorCodes.REMOVING_FROM_WISHLIST_FAILED.getMsg());
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.UPDATE_WISHLIST_DOMAIN.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.REMOVE_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION).printMessage());
			
			resultResponse.setErrorAPIResponse(OfferExceptionCodes.REMOVE_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.REMOVE_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
			
			resultResponse.setResult(OfferErrorCodes.REMOVING_FROM_WISHLIST_FAILED.getId(),
					OfferErrorCodes.REMOVING_FROM_WISHLIST_FAILED.getMsg());
				
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.REMOVING_FROM_WISHLIST_FAILED, OfferSuccessCodes.OFFER_REMOVED_SUCCESSFULLY_FROM_WISHLIST);
		return resultResponse;
		
	}

//	/**
//	 * Domain method to retrieve list of active and eligible offers from wishlist 
//	 * @param accountNumber
//	 * @param header
//	 * @return list of eligible offers in the wishlist for member
//	 */
//	public OfferCatalogResultResponse getOffersFromWishlist(String accountNumber, Headers header) {
//		
//		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(header.getExternalTransactionId());
//		List<OfferCatalogResultResponseDto> offerCatalogResponseList = null;
//		
//		try {
//			
//			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, header);
//			
//			if(!ObjectUtils.isEmpty(memberDetails)) {
//				
//				WishlistEntity wishlist = repositoryHelper.findWishlistForSpecificAccount(accountNumber, memberDetails.getMembershipCode());
//				
//				if (Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(wishlist), OfferErrorCodes.NO_WISHLIST_FOR_ACCOUNT, resultResponse)) {
//			
//					List<String> offersList =  wishlist.getOffers();
//					
//					if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offersList), OfferErrorCodes.NO_OFFER_IN_WISHLIST, resultResponse)) {
//						
//						List<OfferCatalog> fetchedOffers = repositoryHelper.getAllActiveOffersByOfferId(offersList, header.getChannelId());
//						fetchedOffers = FilterValues.filterOfferList(fetchedOffers, Predicates.activeMerchantAndStore());
//						
//						EligibilityInfo eligibilityInfo = new EligibilityInfo();
//						ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, header, fetchedOffers, true);
//						eligibilityInfo.setMemberDetails(memberDetails);
//						helper.checkMembershipEligbilityForOffers(eligibilityInfo, accountNumber, resultResponse);
//						
//						if(Checks.checkNoErrors(resultResponse)){
//							  
//							fetchedOffers = ProcessValues.filterEligibleOffersForMember(eligibilityInfo, fetchedOffers, resultResponse);							
//							eligibilityInfo.setOfferList(fetchedOffers);
//							offerCatalogResponseList = helper.getEligibleOfferList(eligibilityInfo, resultResponse, false);
//							Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(fetchedOffers), OfferErrorCodes.NO_ACTIVE_OFFER_IN_WISHLIST, resultResponse);
//						
//						}
//						
//					}
//					
//				} 
//				
//			}
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.GET_WISHLIST_DOMAIN.get(),
//					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION).printMessage());
//			
//			resultResponse.setErrorAPIResponse(OfferExceptionCodes.GET_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getIntId(),
//					OfferExceptionCodes.GET_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
//			
//			resultResponse.setResult(OfferErrorCodes.GETTING_FROM_WISHLIST_FAILED.getId(),
//					OfferErrorCodes.GETTING_FROM_WISHLIST_FAILED.getMsg());
//				
//		}
//		
//		Responses.setResponse(resultResponse, OfferErrorCodes.GETTING_FROM_WISHLIST_FAILED, OfferSuccessCodes.OFFER_DISPLAYED_SUCCESSFULLY_FROM_WISHLIST);
//		resultResponse.setOfferCatalogs(offerCatalogResponseList);
//		return resultResponse;
//	}
	
	/**
	 * Domain method to retrieve list of active and eligible offers from wishlist 
	 * @param accountNumber
	 * @param header
	 * @return list of eligible offers in the wishlist for member
	 */
	public OfferCatalogResultResponse getActiveOffersFromWishlist(String accountNumber, Headers header) {
		
		OfferCatalogResultResponse resultResponse = new OfferCatalogResultResponse(header.getExternalTransactionId());
		List<OfferCatalogResultResponseDto> offerCatalogResponseList = null;
		
		try {
			
			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, resultResponse, header);
			
			if(!ObjectUtils.isEmpty(memberDetails)) {
				
				WishlistEntity wishlist = repositoryHelper.findWishlistForSpecificAccount(accountNumber, memberDetails.getMembershipCode());
				
				if (Responses.setResponseAfterConditionCheck(!ObjectUtils.isEmpty(wishlist), OfferErrorCodes.NO_WISHLIST_FOR_ACCOUNT, resultResponse)) {
			
					List<String> offersList =  wishlist.getOffers();
					
					if(Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(offersList), OfferErrorCodes.NO_OFFER_IN_WISHLIST, resultResponse)) {
						
						List<OfferCatalog> fetchedOffers = repositoryHelper.getAllActiveOffersByOfferId(offersList, header.getChannelId());
						fetchedOffers = FilterValues.filterOfferList(fetchedOffers, Predicates.activeMerchantAndStore());
						
						EligibilityInfo eligibilityInfo = new EligibilityInfo();
						ProcessValues.setEligibilityInfoForListingOffer(eligibilityInfo, header, fetchedOffers, true);
						eligibilityInfo.setMemberDetails(memberDetails);
						helper.checkMemberEligbilityForOffers(eligibilityInfo, accountNumber, resultResponse);
						
						if(Checks.checkNoErrors(resultResponse)){
							  
							ProcessValues.filterEligibleOffers(eligibilityInfo, fetchedOffers, resultResponse);							
							offerCatalogResponseList = helper.listOfEligibleOffers(eligibilityInfo, resultResponse, false, true);
							Responses.setResponseAfterConditionCheck(!CollectionUtils.isEmpty(eligibilityInfo.getOfferList()), OfferErrorCodes.NO_ACTIVE_OFFER_IN_WISHLIST, resultResponse);
						
						}
						
					}
					
				} 
				
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			LOG.error(new MarketplaceException(this.getClass().toString(), OfferConstants.GET_WISHLIST_DOMAIN.get(),
					e.getClass() + e.getMessage(), OfferExceptionCodes.GET_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION).printMessage());
			
			resultResponse.setErrorAPIResponse(OfferExceptionCodes.GET_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getIntId(),
					OfferExceptionCodes.GET_FROM_WISHLIST_DOMAIN_RUNTIME_EXCEPTION.getMsg()+OfferConstants.REFER_LOGS.get());
			
			resultResponse.setResult(OfferErrorCodes.GETTING_FROM_WISHLIST_FAILED.getId(),
					OfferErrorCodes.GETTING_FROM_WISHLIST_FAILED.getMsg());
				
		}
		
		Responses.setResponse(resultResponse, OfferErrorCodes.GETTING_FROM_WISHLIST_FAILED, OfferSuccessCodes.OFFER_DISPLAYED_SUCCESSFULLY_FROM_WISHLIST);
		resultResponse.setOfferCatalogs(offerCatalogResponseList);
		return resultResponse;
	}
	
}
