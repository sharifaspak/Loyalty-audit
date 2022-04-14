package com.loyalty.marketplace.banners.domain.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.drools.core.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.banners.constants.BannerCodes;
import com.loyalty.marketplace.banners.inbound.dto.BannerListRequestDto;
import com.loyalty.marketplace.banners.inbound.dto.BannerRequestDto;
import com.loyalty.marketplace.banners.outbound.dto.BannerListResponseDto;
import com.loyalty.marketplace.banners.outbound.dto.BannerResponseDto;
import com.loyalty.marketplace.banners.outbound.dto.BannerResultResponse;
import com.loyalty.marketplace.banners.outbound.dto.BannerServiceRequestDto;
import com.loyalty.marketplace.banners.outbound.dto.CreateBannerRequestDto;
import com.loyalty.marketplace.banners.outbound.dto.UpdateBannerRequestDto;
import com.loyalty.marketplace.banners.outbound.service.BannerService;
import com.loyalty.marketplace.banners.utils.BannerUtils;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;

import lombok.AccessLevel;
import lombok.Getter;

@Component
public class BannerDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(BannerDomain.class);
	private static final String DATE_FORMAT_CONVERSION_ERROR = "Error in date format conversion";
	
	@Getter(AccessLevel.NONE)
	@Autowired
	BannerService bannerService;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	/**
	 * 
	 * @param bannersRequestListDto
	 * @param header
	 * @return response after adding a new banner
	 */
	public ResultResponse addNewBanner(BannerListRequestDto bannersRequestListDto, Headers header) {
		
		LOG.info("Inside addNewBanner in class {}", this.getClass().getName());
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		String id = null;
		
		try{
			
		  String token = bannerService.fetchBannerToken(resultResponse, header);
		  
		  if(!ObjectUtils.isEmpty(bannersRequestListDto)
		  && !CollectionUtils.isEmpty(bannersRequestListDto.getBanners())) {
			
			  CreateBannerRequestDto bannerRequest = new CreateBannerRequestDto();
			  bannerRequest.setBanners(new ArrayList<>(1));
			  bannersRequestListDto.getBanners().forEach(request->
			  {
				  BannerServiceRequestDto banner = modelMapper.map(request, BannerServiceRequestDto.class);
				  setBannerCreateParameters(banner, bannerRequest, request, header);
				  
			  });
			  
			  id = bannerService.addNewBanner(token, bannerRequest, resultResponse, header);
		  
		  } else {
			
			  resultResponse.addErrorAPIResponse(BannerCodes.BANNER_REQUEST_EMPTY.getIntId(),
						BannerCodes.BANNER_REQUEST_EMPTY.getMsg());
			  
		  }
		  
		} catch (Exception e) {
			
			LOG.error(new MarketplaceException(this.getClass().toString(), "addNewBanner",
					e.getClass() + e.getMessage(),BannerCodes.BANNER_RUNTIME_EXCEPTION).printMessage());
			resultResponse.addErrorAPIResponse(BannerCodes.BANNER_RUNTIME_EXCEPTION.getIntId(),
					BannerCodes.BANNER_RUNTIME_EXCEPTION.getMsg()+
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName()));
			
		}
		
		BannerUtils.setResultWithMessage(resultResponse, BannerCodes.BANNER_CREATION_SUCCESS, BannerCodes.BANNER_CREATION_FAILED, id, null);
		LOG.info("Leaving addNewBanner in class {}", this.getClass().getName());
		return resultResponse;
	}

	/***
	 * 
	 * @param banner
	 * @param bannerRequest
	 * @param request
	 * @param header
	 */
	private void setBannerCreateParameters(BannerServiceRequestDto banner, CreateBannerRequestDto bannerRequest,
			BannerRequestDto request, Headers header) {
		
		if(!ObjectUtils.isEmpty(banner)) {
			
			  banner.setEligibleCustomerSubsDetails(request.getEligiblecustomerSubsDetails());
			  try {
				  LOG.info("before From Date : {}",request.getFromBirthDate());
				  LOG.info("before To Date : {}",request.getToBirthDate());
				  banner.setFromBirthDate(!StringUtils.isEmpty(request.getFromBirthDate())
						  ? Utilities.changeStringDateFormat(request.getFromBirthDate(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get(), OfferConstants.BANNER_DATE_FORMAT.get())
						  : null);
				  banner.setToBirthDate(!StringUtils.isEmpty(request.getToBirthDate())
						  ? Utilities.changeStringDateFormat(request.getToBirthDate(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get(), OfferConstants.BANNER_DATE_FORMAT.get())
						  : null);
				  LOG.info("From Date : {}",banner.getFromBirthDate());
				  LOG.info("To Date : {}",banner.getToBirthDate());
			  } catch (ParseException e) {
				LOG.info(DATE_FORMAT_CONVERSION_ERROR);
			  }
			  
			  banner.setCreateUserId(header.getUserName());
			  banner.setCreatedDateStamp(Utils.changeDateToString(new Date()));
			  banner.setLastOpUser(header.getUserName());
			  banner.setLastOpDatestamp(Utils.changeDateToString(new Date()));
			  bannerRequest.getBanners().add(banner);
		  }
	}

	/**
	 * 
	 * @param bannerId
	 * @param bannerRequestDto
	 * @param header
	 * @return
	 */
	public ResultResponse updateExistingBanner(String bannerId, BannerRequestDto bannerRequestDto, Headers header) {
		
		LOG.info("Inside updateExistingBanner in class {}", this.getClass().getName());
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		
		try{
			
			String token = bannerService.fetchBannerToken(resultResponse, header);
			
			if(!CollectionUtils.isEmpty(bannerService.fetchSpecificBanner(token, bannerId, resultResponse, header))) {
			
				UpdateBannerRequestDto bannerUpdateRequest = new UpdateBannerRequestDto();
				BannerServiceRequestDto bannerRequest = modelMapper.map(bannerRequestDto, BannerServiceRequestDto.class);
				setBannerUpdateParameters(bannerId, bannerRequest, bannerRequestDto, header, bannerUpdateRequest);
				bannerService.updateExistingBanner(token, bannerUpdateRequest, resultResponse, header);
			
			} else {
				
				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_NOT_PRESENT.getIntId(),
						BannerCodes.BANNER_NOT_PRESENT.getMsg());
				
			}
			
		} catch(Exception e) {
			
			LOG.error(new MarketplaceException(this.getClass().toString(), "updateExistingBanner",
					e.getClass() + e.getMessage(),BannerCodes.BANNER_RUNTIME_EXCEPTION).printMessage());
			resultResponse.addErrorAPIResponse(BannerCodes.BANNER_RUNTIME_EXCEPTION.getIntId(),
					BannerCodes.BANNER_RUNTIME_EXCEPTION.getMsg()+
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName()));
		}
		
		BannerUtils.setResult(resultResponse, BannerCodes.BANNER_UPDATION_SUCCESS, BannerCodes.BANNER_UPDATION_FAILED);
		LOG.info("Leaving updateExistingBanner in class {}", this.getClass().getName());
		return resultResponse;
	}

	/**
	 * 
	 * @param bannerId 
	 * @param bannerRequest
	 * @param bannerRequestDto
	 * @param header 
	 * @param bannerUpdateRequest 
	 * @throws ParseException 
	 */
	private void setBannerUpdateParameters(String bannerId, BannerServiceRequestDto bannerRequest, BannerRequestDto bannerRequestDto, Headers header, UpdateBannerRequestDto bannerUpdateRequest) {
		
		if(!ObjectUtils.isEmpty(bannerRequest)) {
			
			if(!StringUtils.isEmpty(bannerId)) {
				bannerRequest.setId(Integer.parseInt(bannerId));
				try {
					LOG.info("before From Date : {}",bannerRequestDto.getFromBirthDate());
					LOG.info("before To Date : {}",bannerRequestDto.getToBirthDate());
					bannerRequest.setFromBirthDate(!StringUtils.isEmpty(bannerRequestDto.getFromBirthDate())
							  ? Utilities.changeStringDateFormat(bannerRequestDto.getFromBirthDate(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get(), OfferConstants.BANNER_DATE_FORMAT.get())
							  : null);
					bannerRequest.setToBirthDate(!StringUtils.isEmpty(bannerRequestDto.getToBirthDate())
							  ? Utilities.changeStringDateFormat(bannerRequestDto.getToBirthDate(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get(), OfferConstants.BANNER_DATE_FORMAT.get())
							  : null);
					LOG.info("From Date : {}",bannerRequest.getFromBirthDate());
					LOG.info("To Date : {}",bannerRequest.getToBirthDate());
				} catch (ParseException e) {
					LOG.info(DATE_FORMAT_CONVERSION_ERROR);
				}
				
				bannerRequest.setEligibleCustomerSubsDetails(bannerRequestDto.getEligiblecustomerSubsDetails());
				bannerRequest.setLastOpUser(header.getUserName());
				bannerRequest.setLastOpDatestamp(Utils.changeDateToString(new Date()));
			}
			bannerUpdateRequest.setBanners(bannerRequest);
		}
		
	}

	/**
	 * 
	 * @param header
	 * @return
	 */
	public BannerResultResponse listAllBanners(Headers header) {
		
		LOG.info("Inside listAllBanners in class {}", this.getClass().getName());
		BannerResultResponse resultResponse = new BannerResultResponse(header.getExternalTransactionId());
		
		try{
			
			String token = bannerService.fetchBannerToken(resultResponse, header);
			List<BannerListResponseDto> bannerResultList = bannerService.fetchAllBanners(token, resultResponse, header);
			
			if(!CollectionUtils.isEmpty(bannerResultList)) {
				
				resultResponse.setBannerList(new ArrayList<>(1));
				bannerResultList.forEach(banner->{
					BannerResponseDto bannerResponse = modelMapper.map(banner, BannerResponseDto.class);
					try {
						LOG.info("before From Date : {}",banner.getFromBirthDate());
						LOG.info("before To Date : {}",banner.getToBirthDate());
						bannerResponse.setFromBirthDate(!StringUtils.isEmpty(banner.getFromBirthDate())
								  ? Utilities.changeStringDateFormat(banner.getFromBirthDate(), OfferConstants.BANNER_DATE_FORMAT.get(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get())
								  : null);
						bannerResponse.setToBirthDate(!StringUtils.isEmpty(banner.getToBirthDate())
								  ? Utilities.changeStringDateFormat(banner.getToBirthDate(), OfferConstants.BANNER_DATE_FORMAT.get(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get())
								  : null);
						 LOG.info("From Date : {}",bannerResponse.getFromBirthDate());
						 LOG.info("To Date : {}",bannerResponse.getToBirthDate());
					} catch (ParseException e) {
						LOG.info(DATE_FORMAT_CONVERSION_ERROR);
					}
					bannerResponse.setEligiblecustomerSubsDetails(banner.getEligibleCustomerSubsDetails());
					resultResponse.getBannerList().add(bannerResponse);
				});
			} else {
				
				resultResponse.addErrorAPIResponse(BannerCodes.NO_BANNERS_PRESENT.getIntId(),
						BannerCodes.NO_BANNERS_PRESENT.getMsg());
			}
			
		} catch(Exception e) {
			
			LOG.error(new MarketplaceException(this.getClass().toString(), "listAllBanners",
					e.getClass() + e.getMessage(),BannerCodes.BANNER_RUNTIME_EXCEPTION).printMessage());
			resultResponse.addErrorAPIResponse(BannerCodes.BANNER_RUNTIME_EXCEPTION.getIntId(),
					BannerCodes.BANNER_RUNTIME_EXCEPTION.getMsg()+
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName()));
		}
		
		BannerUtils.setResult(resultResponse, BannerCodes.BANNER_LISTING_SUCCESS, BannerCodes.BANNER_LISTING_FAILED);
		LOG.info("Leaving listAllBanners in class {}", this.getClass().getName());
		return resultResponse;
	}

	/**
	 * 
	 * @param bannerId
	 * @param header
	 * @return
	 */
	public BannerResultResponse listSpecificBanner(String bannerId, Headers header) {
		
		LOG.info("Inside listSpecificBanner in class {}", this.getClass().getName());
		BannerResultResponse resultResponse = new BannerResultResponse(header.getExternalTransactionId());
		
		try{
			
			String token = bannerService.fetchBannerToken(resultResponse, header);
			List<BannerListResponseDto> bannerResultList = bannerService.fetchSpecificBanner(token, bannerId, resultResponse, header);
			
			if(!CollectionUtils.isEmpty(bannerResultList)) {
				
				resultResponse.setBannerList(new ArrayList<>(1));
				bannerResultList.forEach(banner->{
					BannerResponseDto bannerResponse = modelMapper.map(banner, BannerResponseDto.class);
					try {
						LOG.info("before From Date : {}",banner.getFromBirthDate());
						LOG.info("before To Date : {}",banner.getToBirthDate());
						bannerResponse.setFromBirthDate(!StringUtils.isEmpty(banner.getFromBirthDate())
								  ? Utilities.changeStringDateFormat(banner.getFromBirthDate(), OfferConstants.BANNER_DATE_FORMAT.get(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get())
								  : null);
						bannerResponse.setToBirthDate(!StringUtils.isEmpty(banner.getToBirthDate())
								  ? Utilities.changeStringDateFormat(banner.getToBirthDate(), OfferConstants.BANNER_DATE_FORMAT.get(), OfferConstants.ADMIN_PORTAL_DATE_FORMAT.get())
								  : null);
						LOG.info("From Date : {}",bannerResponse.getFromBirthDate());
						LOG.info("To Date : {}",bannerResponse.getToBirthDate());
					} catch (ParseException e) {
						LOG.info(DATE_FORMAT_CONVERSION_ERROR);
					}
					bannerResponse.setEligiblecustomerSubsDetails(banner.getEligibleCustomerSubsDetails());
					resultResponse.getBannerList().add(bannerResponse);
				});
				
			} else {
				
				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_NOT_PRESENT.getIntId(),
						BannerCodes.BANNER_NOT_PRESENT.getMsg());
			}
			
		} catch(Exception e) {
			
			LOG.error(new MarketplaceException(this.getClass().toString(), "listSpecificBanner",
					e.getClass() + e.getMessage(),BannerCodes.BANNER_RUNTIME_EXCEPTION).printMessage());
			resultResponse.addErrorAPIResponse(BannerCodes.BANNER_RUNTIME_EXCEPTION.getIntId(),
					BannerCodes.BANNER_RUNTIME_EXCEPTION.getMsg()+
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName()));
		}
		
		BannerUtils.setResult(resultResponse, BannerCodes.BANNER_LISTING_SPECIFIC_SUCCESS, BannerCodes.BANNER_LISTING_SPECIFIC_FAILED);
		LOG.info("Leaving listSpecificBanner in class {}", this.getClass().getName());
		return resultResponse;
	}

	/**
	 * 
	 * @param bannerId
	 * @param header
	 * @return response after deleting banner
	 */
	public ResultResponse deleteSpecificBanner(String bannerId, Headers header) {
		
		LOG.info("Inside deleteSpecificBanner in class {}", this.getClass().getName());
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		
		try{
			
			String token = bannerService.fetchBannerToken(resultResponse, header);
			
			if(!CollectionUtils.isEmpty(bannerService.fetchSpecificBanner(token, bannerId, resultResponse, header))) {
				
				bannerService.deleteExistingBanner(token, bannerId, resultResponse, header);
			
			} else {
				
				resultResponse.addErrorAPIResponse(BannerCodes.BANNER_NOT_PRESENT.getIntId(),
						BannerCodes.BANNER_NOT_PRESENT.getMsg());
			}
			
		} catch(Exception e) {
			
			LOG.error(new MarketplaceException(this.getClass().toString(), "deleteSpecificBanner",
					e.getClass() + e.getMessage(),BannerCodes.BANNER_RUNTIME_EXCEPTION).printMessage());
			resultResponse.addErrorAPIResponse(BannerCodes.BANNER_RUNTIME_EXCEPTION.getIntId(),
					BannerCodes.BANNER_RUNTIME_EXCEPTION.getMsg()+
					exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName()));
		}
		
		BannerUtils.setResult(resultResponse, BannerCodes.BANNER_DELETION_SUCCESS, BannerCodes.BANNER_DELETION_FAILED);
		LOG.info("Leaving deleteSpecificBanner in class {}", this.getClass().getName());
		return resultResponse;
	}
	
	
}
