package com.loyalty.marketplace.merchants.inbound.restcontroller.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.merchants.constants.MerchantConstants;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantDto;
import com.loyalty.marketplace.merchants.inbound.dto.RateTypeDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.merchants.outbound.dto.ImageInfo;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsStoreOfferResponse;
import com.loyalty.marketplace.merchants.outbound.dto.ListMerchantsWithTotal;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantResult;
import com.loyalty.marketplace.merchants.outbound.dto.MerchantStoreOfferResult;
import com.loyalty.marketplace.merchants.outbound.service.OfferService;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.image.MarketplaceImage;
import com.loyalty.marketplace.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.PartnerService;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;

@Component
public class MerchantControllerHelper {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	OfferService offerService;

	@Autowired
	ImageRepository imageRepository;
	
	public boolean validateRateTypeRequest(List<RateTypeDto> rateTypes, ResultResponse resultResponse,
			List<RateType> rateTypesToSave, int existingSize, String user, String program) {

		for (RateTypeDto rateType : rateTypes) {
			if (rateType.getType().equals(MerchantConstants.DISCOUNT_BILLING_CODE.get())) {
				RateType rateTypeToSave = new RateType();
				modelMapper.map(rateType, rateTypeToSave);
				rateTypeToSave.setTypeId(String.valueOf(++existingSize));
				rateTypeToSave.setDtCreated(new Date());
				rateTypeToSave.setDtUpdated(new Date());
				rateTypeToSave.setUsrCreated(user);
				rateTypeToSave.setUsrUpdated(user);
				rateTypeToSave.setProgram(program);
				rateTypesToSave.add(rateTypeToSave);
			} else {
				resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_TYPE.getIntId(),
						MerchantCodes.INVALID_TYPE.getMsg());
				resultResponse.setResult(MerchantCodes.RATE_TYPE_ADDITION_FAILED.getId(),
						MerchantCodes.RATE_TYPE_ADDITION_FAILED.getMsg());
				return false;
			}
		}

		return true;
	}
	
	public void validateUsernameExists(MerchantDto merchantDto, ResultResponse resultResponse,
			Merchant merchantDetails) {

		for (final ContactPersonDto contactPersonDto : merchantDto.getContactPersons()) {
			boolean isExists = false;
			if (StringUtils.isEmpty(contactPersonDto.getUserName())) {
				resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_USER_NAME.getIntId(),
						MerchantCodes.INVALID_USER_NAME.getMsg());
			} else {
				for (final ContactPerson contactPerson : merchantDetails.getContactPersons()) {
					if (contactPersonDto.getUserName().equals(contactPerson.getUserName())) {
						isExists = true;
					}
				}
				if (!isExists) {
					resultResponse.addErrorAPIResponse(MerchantCodes.USERNAME_NOT_FOUND.getIntId(),
							contactPersonDto.getUserName() + MerchantConstants.SEMI_COLON.get()
									+ MerchantCodes.USERNAME_NOT_FOUND.getMsg());
				}
			}
		}

	}

	public MerchantStoreOfferResult populateMerchantAttributes(Merchant merchant, MerchantStoreOfferResult merchantList) {
		
		if (null != merchant.getBarcodeType()) {
			merchantList.setBarcodeType(merchant.getBarcodeType().getName());
			merchantList.setBarcodeId(merchant.getBarcodeType().getId());
		}
		
		if (null != merchant.getCategory() && null != merchant.getCategory().getCategoryName()) {
			merchantList.setCategory(merchant.getCategory().getCategoryName().getCategoryNameEn());
		}
		
		return merchantList;
	}
	
	public ListMerchantsWithTotal listMerchantsForAdmin(String merchantStatus, Pageable pageNumberWithElements, String program) {
		
		Page<Merchant> output;
		if (!StringUtils.isEmpty(merchantStatus) && merchantStatus.equalsIgnoreCase(MerchantConstants.MERCHANT_ACTIVE_STATUS.get())) {
			//output = merchantRepository.findByStatusWithPage(MerchantConstants.MERCHANT_ACTIVE_STATUS.get(),pageNumberWithElements);
			output = merchantRepository.findByProgramCodeIgnoreCaseAndStatusWithPage(MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
		} else {
			//output  = merchantRepository.findAll(pageNumberWithElements);	
			output  = merchantRepository.findByProgramCodeIgnoreCasePagination(program, pageNumberWithElements);
		}
		ListMerchantsWithTotal result = new ListMerchantsWithTotal();
		result.setListMerchants(output.getContent().isEmpty()?new ArrayList<>():output.getContent());
		result.setTotalRecords(output.getTotalElements());
		
		return result;
	}
	
	public ListMerchantsWithTotal listMerchantsForNonAdmin(String filterFlag, String filterValue, String category,
			String token, Pageable pageNumberWithElements, String externalTransactionId, String program)
			throws UnsupportedEncodingException, MarketplaceException {
	
		Page<Merchant> merchants;
		String merchantNameEnAr = filterValue;		
		if (!StringUtils.isEmpty(filterValue)) {
			merchantNameEnAr = URLDecoder.decode(filterValue, "UTF-8");
		}
		ListMerchantsWithTotal result = new ListMerchantsWithTotal();
		if ((!StringUtils.isEmpty(category)) && category.equalsIgnoreCase(MerchantConstants.CATEGORY_ALL.get())) {
			result=listMerchantsForCategoryAll(category,filterFlag,merchantNameEnAr,token,pageNumberWithElements, externalTransactionId, program);
			} 
		else if((!StringUtils.isEmpty(category)) && !category.equalsIgnoreCase(MerchantConstants.CATEGORY_ALL.get())){
			result = listMerchantsForCategorySpecificId(category, filterFlag, merchantNameEnAr,token,pageNumberWithElements, externalTransactionId, program);
		}
		else {
			//merchants = merchantRepository.findByStatusWithPage(MerchantConstants.MERCHANT_ACTIVE_STATUS.get(),pageNumberWithElements);
			merchants = merchantRepository.findByProgramCodeIgnoreCaseAndStatusWithPage(MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
			result.setListMerchants(merchants.getContent().isEmpty()?new ArrayList<>():merchants.getContent());
			result.setTotalRecords(merchants.getTotalElements());
		}
		
		
		
		
		return result;
	}
	
	public ListMerchantsWithTotal listMerchantsForCategoryAll(String category, String filterFlag,
			String merchantNameEnAr, String token, Pageable pageNumberWithElements, String externalTransactionId, String program)
			throws MarketplaceException {
		ListMerchantsWithTotal result = new ListMerchantsWithTotal();
		
		List<Merchant> merchants;

		//Page<Merchant> allMerchants = merchantRepository.findByStatusWithPage(MerchantConstants.MERCHANT_ACTIVE_STATUS.get(),pageNumberWithElements);
		Page<Merchant> allMerchants = merchantRepository.findByProgramCodeIgnoreCaseAndStatusWithPage(
				MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
		Map<Category, List<Merchant>> groupMerchantByCategory = new HashMap<>();	
		
		List<Merchant> output = allMerchants.getContent().isEmpty()?new ArrayList<>():allMerchants.getContent();				
		groupMerchantByCategory = output.stream()
				.collect(Collectors.groupingBy(Merchant::getCategory));
		merchants = new ArrayList<>(output.size());
		for (Map.Entry<Category, List<Merchant>> entry : groupMerchantByCategory.entrySet()) {
			merchants.addAll(entry.getValue());
		}	
		
		if (!StringUtils.isEmpty(filterFlag)
				&& filterFlag.equalsIgnoreCase(MerchantConstants.FILTER_FLAG_KEYWORD.get())) {			
		//	allMerchants = merchantRepository.findByMerchantNamePagination(merchantNameEnAr,MerchantConstants.MERCHANT_ACTIVE_STATUS.get(),pageNumberWithElements);			
			allMerchants = merchantRepository.findByMerchantNamAndProgramCodeePagination(merchantNameEnAr, MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
			merchants = allMerchants.getContent().isEmpty()?new ArrayList<>():allMerchants.getContent();
		} else if (!StringUtils.isEmpty(filterFlag)	&& filterFlag.equalsIgnoreCase(MerchantConstants.FILTER_EARN.get())) {
			allMerchants=listMerchantsForFilterEarn(category,token,pageNumberWithElements, externalTransactionId, program);
			merchants = allMerchants.getContent().isEmpty()?new ArrayList<>():allMerchants.getContent();
		} else if (!StringUtils.isEmpty(filterFlag)	&& filterFlag.equalsIgnoreCase(MerchantConstants.DISCOUNT_BILLING_CODE.get())) {
			allMerchants=listMerchantsForFilterDiscountOrCash(MerchantConstants.DISCOUNT_BILLING_CODE.get(), category,token,pageNumberWithElements, program);
			merchants = allMerchants.getContent().isEmpty()?new ArrayList<>():allMerchants.getContent();
		} else if (!StringUtils.isEmpty(filterFlag)	&& filterFlag.equalsIgnoreCase(MerchantConstants.FILTER_CASH.get())) {
			allMerchants=listMerchantsForFilterDiscountOrCash(MerchantConstants.FILTER_CASH.get(), category, token, pageNumberWithElements, program);		
			merchants = allMerchants.getContent().isEmpty()?new ArrayList<>():allMerchants.getContent();
		} 
		result.setListMerchants(merchants);
		result.setTotalRecords(allMerchants.getTotalElements());		
		
		return result;
	}
	
	public ListMerchantsWithTotal listMerchantsForCategorySpecificId(String category, String filterFlag,
			String merchantNameEnAr, String token, Pageable pageNumberWithElements, String externalTransactionId, String program)
			throws MarketplaceException {
		ListMerchantsWithTotal result = new ListMerchantsWithTotal();		
		Page<Merchant> merchants;
		//merchants = merchantRepository.findByCategoryId(category, MerchantConstants.MERCHANT_ACTIVE_STATUS.get(),pageNumberWithElements);
		merchants = merchantRepository.findByCategoryIdAndProgramIgnoreCase(category, MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
		if (!StringUtils.isEmpty(filterFlag)
				&& filterFlag.equalsIgnoreCase(MerchantConstants.FILTER_FLAG_KEYWORD.get())) {			
			//merchants = merchantRepository.findByMerchantNameCategoryId(merchantNameEnAr,MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), category,pageNumberWithElements);	
			merchants = merchantRepository.findByMerchantNameCategoryIdAndProgramCode(merchantNameEnAr,MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), category, program, pageNumberWithElements);
		} else if (!StringUtils.isEmpty(filterFlag)	&& filterFlag.equalsIgnoreCase(MerchantConstants.FILTER_EARN.get())) {
			merchants=listMerchantsForFilterEarn(category,token, pageNumberWithElements, externalTransactionId, program);
		} else if (!StringUtils.isEmpty(filterFlag)	&& filterFlag.equalsIgnoreCase(MerchantConstants.DISCOUNT_BILLING_CODE.get())) {
			merchants=listMerchantsForFilterDiscountOrCash(MerchantConstants.DISCOUNT_BILLING_CODE.get(), category,token,pageNumberWithElements, program);
		} else if (!StringUtils.isEmpty(filterFlag)	&& filterFlag.equalsIgnoreCase(MerchantConstants.FILTER_CASH.get())) {
			merchants=listMerchantsForFilterDiscountOrCash(MerchantConstants.FILTER_CASH.get(), category,token,pageNumberWithElements, program);					
		} 
		result.setListMerchants(merchants.getContent().isEmpty()?new ArrayList<>():merchants.getContent());
		result.setTotalRecords(merchants.getTotalElements());		
		
		return result;
	}
	
	public Page<Merchant> listMerchantsForFilterEarn(String category,String token, Pageable pageNumberWithElements, String externalTransactionId, String program) throws MarketplaceException {
		Page<Merchant> merchants;
		List<String> partnerCodes = partnerService.getPartnerCodesByPartnerType(token, externalTransactionId);
		if(category.equalsIgnoreCase(MerchantConstants.CATEGORY_ALL.get())) {
			//merchants = merchantRepository.findByPartnerCodeListPagination(partnerCodes,MerchantConstants.MERCHANT_ACTIVE_STATUS.get(),pageNumberWithElements);	
			merchants = merchantRepository.findByPartnerCodeListAndProgramCodeIgnoreCasePagination(partnerCodes, MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
		}
		else {
			//merchants = merchantRepository.findByPartnerCodeListCategoryIdPagination(partnerCodes,MerchantConstants.MERCHANT_ACTIVE_STATUS.get(),category,pageNumberWithElements);
			merchants = merchantRepository.findByPartnerCodeListCategoryIdAndProgramCodeIgnoreCasePagination(partnerCodes, MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), category, program, pageNumberWithElements);
		}
		return merchants;
	}

	public Page<Merchant> listMerchantsForFilterDiscountOrCash(String offer, String category,String token, Pageable pageNumberWithElements, String program) {
		Page<Merchant> merchants;
		List<String> merchantIds = offerService.getMerchantsForOfferType(offer, token);
		if (category.equalsIgnoreCase(MerchantConstants.CATEGORY_ALL.get())) {
			// merchants = merchantRepository.findAllByMerchantIds(merchantIds,
			// MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), pageNumberWithElements);
			merchants = merchantRepository.findAllByMerchantIdsAndProgramCodeIgnoreCase(merchantIds,
					MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), program, pageNumberWithElements);
		} else {
//			merchants = merchantRepository.findAllByMerchantIdsCategoryId(merchantIds,
//					MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), category, pageNumberWithElements);
			merchants = merchantRepository.findAllByMerchantIdsCategoryIdAndProgramCodeIgnoreCase(merchantIds,
					MerchantConstants.MERCHANT_ACTIVE_STATUS.get(), category, program, pageNumberWithElements);
		}
		return merchants;
	}

	public MerchantResult populateImageAttributes(Merchant merchant, MerchantResult merchantResult, String program) {
		
		List<ImageInfo> imageInfoList = new ArrayList<>();
		//List<MarketplaceImage> images = imageRepository.findByImageCategoryAndDomainId(MerchantConstants.IMAGE_CATEGORY_MERCHANT_OFFER.get(), merchant.getMerchantCode());
		List<MarketplaceImage> images = imageRepository.findByImageCategoryAndDomainIdAndProgramCodeIgnoreCase(
				MerchantConstants.IMAGE_CATEGORY_MERCHANT_OFFER.get(), merchant.getMerchantCode(), program);
		if (!images.isEmpty()) {
			for (MarketplaceImage image : images) {
				ImageInfo imageInfo = new ImageInfo();
				if (null != image.getMerchantOfferImage()) {
					imageInfo.setImageType(image.getMerchantOfferImage().getImageType());
					imageInfo.setAvailableInChannel(image.getMerchantOfferImage().getAvailableInChannel());
				}
				imageInfo.setImageUrl(image.getImageUrl());
				imageInfo.setImageUrlDr(image.getImageUrlDr());
				imageInfo.setImageUrlProd(image.getImageUrlProd());
				imageInfoList.add(imageInfo);
			}

			merchantResult.setImageInfo(imageInfoList);
		}
	
		return merchantResult;
		
	}
	
}
