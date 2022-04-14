package com.loyalty.marketplace.subscription.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.PaymentMethods;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferTypeRepository;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.domain.Benefits;
import com.loyalty.marketplace.subscription.domain.BenefitsCategory;
import com.loyalty.marketplace.subscription.domain.Title;
import com.loyalty.marketplace.subscription.inbound.dto.EligibleCategories;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionBenefits;
import com.loyalty.marketplace.subscription.inbound.dto.SubscriptionCatalogRequestDto;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionCatalog;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPaymentMethod;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionCatalogRepository;
import com.loyalty.marketplace.subscription.service.SubscriptionService;
import com.loyalty.marketplace.utils.MarketplaceValidator;

@Component
public class SubscriptionCatalogValidator extends MarketplaceValidator {
	
	@Value("${phoneyTunes.lifestyle.auto.renewal.packageId}")
	private String phoneyTunesLifeStyleAutoPackId;
	
	@Value("${phoneyTunes.lifestyle.one.time.packageId}")
	private String phoneyTunesLifeStyleOnePackId;
	
	@Value("${phoneyTunes.food.auto.renewal.packageId}")
	private String phoneyTunesFoodAutoPackId;
	
	@Value("${phoneyTunes.food.one.time.packageId}")
	private String phoneyTunesFoodOnePackId;
	
	@Value("${phoneyTunes.food.life.time.packageId}")
	private String phoneyTunesFoodLifeTimePackId;
	
	@Autowired
	SubscriptionCatalogRepository subscriptionCatalogRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	OfferTypeRepository offerTypeRepository;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	SubscriptionUtils subscriptionUtils;
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionCatalogValidator.class);
	
	protected SubscriptionCatalogValidator() {
		/*Prevent the instantiation of this class directly*/
	}
	
	public static boolean validateMandatoryHeaders(String program, String userName, ResultResponse result) {
		if (null == program || null == userName || program.isEmpty() || userName.isEmpty()) {
			result.setErrorAPIResponse(SubscriptionManagementCode.INVALID_HEADERS.getIntId(),
					SubscriptionManagementCode.INVALID_HEADERS.getMsg());
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean validateMandatoryHeaders(String userName, ResultResponse result) {
		if (null == userName || userName.isEmpty()) {
			result.setErrorAPIResponse(SubscriptionManagementCode.INVALID_HEADERS.getIntId(),
					SubscriptionManagementCode.INVALID_HEADERS.getMsg()+" : Missing userName");
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean validateSpentCriteriaForPaymentMethod(String paymentMethod, Double spentAmount, Integer spentPoints, ResultResponse result) {
		boolean flag = true;
		
		if((paymentMethod.equalsIgnoreCase(SubscriptionManagementConstants.PAYMENT_METHOD_CARD.get()))
				&& (ObjectUtils.isEmpty(spentAmount))) {
			result.addErrorAPIResponse(SubscriptionManagementCode.SPENT_AMOUNT_MANDATORY.getIntId(),
					SubscriptionManagementCode.SPENT_AMOUNT_MANDATORY.getMsg());
			flag = false;
			
		}
		return flag;
	}
	
	public static boolean validateMandatoryChannelId(String channelId, ResultResponse result) {
		if (null == channelId || channelId.isEmpty()) {
			result.setErrorAPIResponse(SubscriptionManagementCode.INVALID_HEADERS.getIntId(),
					SubscriptionManagementCode.INVALID_HEADERS.getMsg()+" : Missing channelId");
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean validateChargeabilityType(String chargeabilityType, ResultResponse result) {
		if (chargeabilityType.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())
				|| chargeabilityType.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())
				|| chargeabilityType.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
			return true;
		} else {
			result.setErrorAPIResponse(SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getIntId(),
					chargeabilityType + ": " + SubscriptionManagementCode.INVALID_CHARGEABILITY_TYPE.getMsg());
			return false;
		}
	}
	
	public static boolean validateSubscriptionSegment(String subscriptionSegment, ResultResponse result) {
		if(subscriptionSegment.equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get()) 
				|| subscriptionSegment.equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_FOOD.get()) 
				|| subscriptionSegment.equalsIgnoreCase(SubscriptionManagementConstants.SEGMENT_COMBO.get())) {
			return true;
		} else {
			result.setErrorAPIResponse(SubscriptionManagementCode.INVALID_SUBSCRIPTION_SEGMENT.getIntId(),
					subscriptionSegment + ": " + SubscriptionManagementCode.INVALID_SUBSCRIPTION_SEGMENT.getMsg());
			return false;
		}
	}
	
	
	public static boolean validateStatus(String status, ResultResponse result) {
		if ((status.equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_ACTIVE_STATUS.get())) 
				|| status.equalsIgnoreCase(SubscriptionManagementConstants.SUBSCRIPTION_INACTIVE_STATUS.get())) {
			return true;
		} else {
			result.setErrorAPIResponse(SubscriptionManagementCode.SUBSCRIBTION_INVALID.getIntId(),
					status + ": " + SubscriptionManagementCode.SUBSCRIBTION_INVALID.getMsg());
			return false;
		}
	}
	
	public static boolean validatePaymentMethod(List<PaymentMethod> validPaymentMethods, List<PaymentMethodDto> selectedPaymentMethods, ResultResponse result) {
		boolean flag = true;
		List<String> paymentMethods = validPaymentMethods.stream()
				.map(PaymentMethod::getDescription).collect(Collectors.toList());
		List<String> slectedPaymentMethods = selectedPaymentMethods.stream()
				.map(PaymentMethodDto::getDescription).collect(Collectors.toList());
		if(!paymentMethods.stream().map(String::toUpperCase).collect(Collectors.toList()).containsAll(slectedPaymentMethods.stream().map(String::toUpperCase).collect(Collectors.toList()))) {	
        	result.addErrorAPIResponse(SubscriptionManagementCode.INVALID_PAYMENT_METHOD.getIntId(),
    				SubscriptionManagementCode.INVALID_PAYMENT_METHOD.getMsg()+" Accepted Values : "+paymentMethods);
        	flag = false;
        }
        return flag;
    }
	
	public static boolean validatePaymentMethod(List<SubscriptionPaymentMethod> validPaymentMethods, List<PaymentMethods> selectedPaymentMethods) {
		List<String> paymentMethods = validPaymentMethods.stream()
				.map(SubscriptionPaymentMethod::getDescription).collect(Collectors.toList());
		List<String> slectedPaymentMethods = selectedPaymentMethods.stream()
				.map(PaymentMethods::getDescription).collect(Collectors.toList());
		
		return paymentMethods.stream().anyMatch(slectedPaymentMethods::contains);
        	
    }
	
	public static List<SubscriptionPaymentMethod> populateCommonPaymentMethods(List<SubscriptionPaymentMethod> catalogPaymentMethods, List<PaymentMethods> accountPaymentMethods) {
		
		return catalogPaymentMethods.stream()
				.filter(cp -> accountPaymentMethods.stream()
						.anyMatch(ap ->
								cp.getPaymentMethodId().equalsIgnoreCase(ap.getPaymentMethodId()) &&
								cp.getDescription().equalsIgnoreCase(ap.getDescription())))
						.collect(Collectors.toList());
									
    }
	
		
	public static boolean checkEligiblePaymentMethods(String paymentMethod, List<SubscriptionPaymentMethod> catalogPaymentMethod, ResultResponse result) {
		boolean found = true;	
		List<String> paymentMethods = catalogPaymentMethod.stream()
                .map(SubscriptionPaymentMethod::getDescription).collect(Collectors.toList());
		if(!paymentMethods.stream().map(String::toUpperCase).collect(Collectors.toList()).contains(paymentMethod.toUpperCase())) {
			result.addErrorAPIResponse(SubscriptionManagementCode.INELIGIBLE_PAYMENT_METHOD.getIntId(),
					SubscriptionManagementCode.INELIGIBLE_PAYMENT_METHOD.getMsg() + " Available Payment Methods : " + catalogPaymentMethod);
			found = false;
		}
	    return found;
	}
	
	public static boolean checkEligiblePaymentMethodsForAccount(String paymentMethod, String customerType, List<PaymentMethods> accountPaymentMethod, ResultResponse result) {
		boolean found = new SubscriptionUtils().validatePaymentForNonDcb(paymentMethod,customerType,result);	
		
		List<String> paymentMethods = accountPaymentMethod.stream()
                .map(PaymentMethods::getDescription).collect(Collectors.toList());
		if(found && !paymentMethods.stream().map(String::toUpperCase).collect(Collectors.toList()).contains(paymentMethod.toUpperCase())) {
			result.addErrorAPIResponse(SubscriptionManagementCode.ACCOUNT_INELIGIBLE_FOR_PAYMENT_METHOD.getIntId(),
					SubscriptionManagementCode.ACCOUNT_INELIGIBLE_FOR_PAYMENT_METHOD.getMsg() + " Available Payment Methods : " + accountPaymentMethod);
			found = false;
		}
		
	    return found;
	}
	
	public static boolean validateCustomerSegment(List<String> validCustomerSegment, List<String> customerSegment, ResultResponse result) {
		boolean flag = true;
		if(!validCustomerSegment.stream().map(String::toUpperCase).collect(Collectors.toList()).containsAll(customerSegment.stream().map(String::toUpperCase).collect(Collectors.toList()))) {	
        	result.addErrorAPIResponse(SubscriptionManagementCode.INELIGIBLE_CUSTOMER_SEGMENT.getIntId(),
    				SubscriptionManagementCode.INELIGIBLE_CUSTOMER_SEGMENT.getMsg()+" Accepted Values : "+validCustomerSegment);
        	flag = false;
        }
        return flag;
    }
	
	public static boolean validateCustomerSegment(List<String> validCustomerSegment, String customerSegment, ResultResponse result) {
		boolean flag = true;
		if(!validCustomerSegment.stream().map(String::toUpperCase).collect(Collectors.toList()).contains(customerSegment.toUpperCase())) {
        	result.addErrorAPIResponse(SubscriptionManagementCode.INELIGIBLE_CUSTOMER_SEGMENT.getIntId(),
    				SubscriptionManagementCode.INELIGIBLE_CUSTOMER_SEGMENT.getMsg()+" Accepted Values : "+validCustomerSegment);
        	flag = false;
        }
        return flag;
    }
	
		
	public List<Benefits> configureCatalogBenefits(SubscriptionCatalogRequestDto subscriptionCatalogRequestDto, Headers headers, ResultResponse resultResponse) throws SubscriptionManagementException {		
		
		List<Benefits> benefits = new ArrayList<>();
		if(null != subscriptionCatalogRequestDto.getSubscriptionBenefits()) {
			LOG.info("Subscription Benefits : {}",subscriptionCatalogRequestDto.getSubscriptionBenefits());
			List<String> offerTypeIdList = subscriptionCatalogRequestDto.getSubscriptionBenefits().stream()
					.map(SubscriptionBenefits::getEligibleOfferTypeId).collect(Collectors.toList());
			List<OfferType> availableOfferTypeList = offerTypeRepository.findOfferTypeByIdList(offerTypeIdList);
						
			for(SubscriptionBenefits subscriptionBenefit : subscriptionCatalogRequestDto.getSubscriptionBenefits()) {
				Benefits catalogBenefit = new Benefits();
				
				if(availableOfferTypeList.stream().anyMatch(p -> p.getOfferTypeId().equalsIgnoreCase(subscriptionBenefit.getEligibleOfferTypeId()))) {
					OfferType offerType = availableOfferTypeList.stream()
							.filter(p ->p.getOfferTypeId().equalsIgnoreCase(subscriptionBenefit.getEligibleOfferTypeId()))
							.findFirst().get();
					Title offerTypeTitle = new Title();
					offerTypeTitle.setId(offerType.getOfferTypeId());
					offerTypeTitle.setEnglish(offerType.getOfferDescription().getTypeDescriptionEn());
					offerTypeTitle.setArabic(offerType.getOfferDescription().getTypeDescriptionAr());
					catalogBenefit.setEligibleOfferType(offerTypeTitle);
					
					if(null != subscriptionBenefit.getEligibleOfferCategory()) {
						List<String> categoryIdList = subscriptionBenefit.getEligibleOfferCategory().stream()
								.map(EligibleCategories::getEligibleOfferCategoryId).collect(Collectors.toList());
						List<Category> availableOfferCategoryList = categoryRepository.findCategoryByIdList(categoryIdList);
						
						List<BenefitsCategory> eligibleOfferCategory = new ArrayList<>();
						for(EligibleCategories eligibleCategory : subscriptionBenefit.getEligibleOfferCategory()) {
							BenefitsCategory benefitsCategory = new BenefitsCategory();
							if(availableOfferCategoryList.stream().anyMatch(p -> p.getCategoryId().equalsIgnoreCase(eligibleCategory.getEligibleOfferCategoryId()))) {
								Category category = availableOfferCategoryList.stream().filter(p -> p.getCategoryId().equalsIgnoreCase(eligibleCategory.getEligibleOfferCategoryId())
										&& p.getParentCategory() == null).findFirst().get();
								Title categoryTitle = new Title();
								categoryTitle.setId(category.getCategoryId());
								categoryTitle.setEnglish(category.getCategoryName().getCategoryNameEn());
								categoryTitle.setArabic(category.getCategoryName().getCategoryNameAr());
								
								List<Category> availableOfferSubCategoryList = !CollectionUtils.isEmpty(eligibleCategory.getEligibleOfferSubCategoryId())
										? categoryRepository.findCategoryByIdList(eligibleCategory.getEligibleOfferSubCategoryId())
										: null;
															
								benefitsCategory.setCategory(categoryTitle);
								benefitsCategory.setSubCategory(!CollectionUtils.isEmpty(availableOfferSubCategoryList)
										?subscriptionUtils.categoryToTitle(availableOfferSubCategoryList.stream()
										.filter(p -> p.getParentCategory().getCategoryId().equalsIgnoreCase(eligibleCategory.getEligibleOfferCategoryId()))
										.collect(Collectors.toList()))
										:null);
								eligibleOfferCategory.add(benefitsCategory);
							}
						}
						catalogBenefit.setEligibleOfferCategory(eligibleOfferCategory);
					}
					benefits.add(catalogBenefit);
						
				}
			}
			
		}
		return benefits;
	}
			
	public static boolean checkEligibleCatalogByCustomerSegment(List<String> accountCustomerSegment, List<String> catalogCustomerSegment, ResultResponse result) {
		boolean flag = true;
		if(!accountCustomerSegment.stream().map(String::toUpperCase).collect(Collectors.toList()).containsAll(catalogCustomerSegment.stream().map(String::toUpperCase).collect(Collectors.toList()))) {	
        	result.addErrorAPIResponse(SubscriptionManagementCode.INELIGIBLE_CUSTOMER_SEGMENT.getIntId(),
    				SubscriptionManagementCode.INELIGIBLE_CUSTOMER_SEGMENT.getMsg());
        	flag = false;
        }
        return flag;
    }
	
	public static boolean checkEligibleCatalogByChannel(String requestChannel, List<String> catalogChannel, ResultResponse result) {
		boolean flag = true;
		if(null != catalogChannel) {
			if(!catalogChannel.stream().map(String::toUpperCase).collect(Collectors.toList()).contains(requestChannel.toUpperCase())) {
				result.setErrorAPIResponse(SubscriptionManagementCode.INELIGIBLE_CHANNEL.getIntId(),
						SubscriptionManagementCode.INELIGIBLE_CHANNEL.getMsg());
				flag = false;
	        }
		} else {
			flag = false;
		}
        return flag;
    }
	
	public static boolean checkEligibleCatalogByChannel(String requestChannel, List<String> catalogChannel) {
		boolean flag = true;
		if(null != catalogChannel) {
			if(!catalogChannel.stream().map(String::toUpperCase).collect(Collectors.toList()).contains(requestChannel.toUpperCase())) {
				flag = false;
	        }
		} else {
			flag = false;
		}
        return flag;
    }
	
	public static Date validateDate(String dateToValidate, ResultResponse result) throws SubscriptionManagementException {
		List<String> formatStrings = Arrays.asList("dd/MM/yyyy", "dd-MM-yyyy");
	    for (String formatString : formatStrings) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
	        	sdf.setLenient(false);	        	
	        	Date parsedDate = sdf.parse(dateToValidate);
	        	LOG.info("StartDate:After parsing {}",parsedDate);
	            if(parsedDate.compareTo(new Date()) < 0) {
	            	return new Date();
	            }
	        	return parsedDate;
	        }
	        catch (ParseException e) {
	        	LOG.error(new SubscriptionManagementException(SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
	        	throw new SubscriptionManagementException("SubscriptionCatalogValidator", "validateDate",
						e.getClass() + e.getMessage(), SubscriptionManagementCode.INVALID_DATE);
	        }
	    }
	    return null;	
    }
	
	public static Date calculateEndDate(String startDate, int validityPeriod, ResultResponse result) throws SubscriptionManagementException {
		List<String> formatStrings = Arrays.asList("dd/MM/yyyy", "dd-MM-yyyy");
	    for (String formatString : formatStrings) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
	            Calendar c = Calendar.getInstance();
	            c.setTime(sdf.parse(startDate));
	            
	            c.add(Calendar.DAY_OF_MONTH, validityPeriod);
	    		String endDate = sdf.format(c.getTime());
	    		
	    		return validateDate(endDate, result);
	        }
	        catch (ParseException e) {
	        	throw new SubscriptionManagementException("SubscriptionCatalogValidator", "validateDate",
						e.getClass() + e.getMessage(), SubscriptionManagementCode.INVALID_DATE);
	        }
	    }
	    return null;
	}
	
	public SubscriptionCatalog fetchSubscriptionCatalogById(String subscriptionCatalogId) {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		if(subscriptionCatalog.isPresent()) {
			return subscriptionCatalog.get();
		}
		return null;
	}
	
	public String fetchChargeabilityType(String subscriptionCatalogId) {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		if(subscriptionCatalog.isPresent()) {
			return subscriptionCatalog.get().getChargeabilityType();
		}
		return null;
	}
	
	public String fetchSubscriptionSegment(String subscriptionCatalogId) {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		if(subscriptionCatalog.isPresent()) {
			return subscriptionCatalog.get().getSubscriptionSegment();
		}
		return null;
	}
	
	public List<String> fetchSubscriptionSegment(List<String> subscriptionCatalogIdList) {
		List<SubscriptionCatalog> subscriptionCatalogList = subscriptionCatalogRepository.findByIdIn(subscriptionCatalogIdList);
		List<String> subscriptionSegments = new ArrayList<>();
		for(SubscriptionCatalog subscriptionCatalog : subscriptionCatalogList) {
			subscriptionSegments.add(subscriptionCatalog.getSubscriptionSegment());
		}
		return subscriptionSegments;
	}
	
	public boolean isOneTimeLifeTimeFree(String subscriptionCatalogId) {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		return subscriptionCatalog.isPresent()
				&& subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())
				&& (subscriptionCatalog.get().getCost().equals(0.0) || subscriptionCatalog.get().getCost() == null)
				&& (subscriptionCatalog.get().getPointsValue().equals(0) || subscriptionCatalog.get().getPointsValue() == null)
				&& (subscriptionCatalog.get().getValidityPeriod().equals(0) || subscriptionCatalog.get().getValidityPeriod() == null);
	}
	
	public boolean isLifeTimeFree(String subscriptionCatalogId) {
		Optional<SubscriptionCatalog> subscriptionCatalog = subscriptionCatalogRepository.findById(subscriptionCatalogId);
		return subscriptionCatalog.isPresent()
				&& subscriptionCatalog.get().getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())
				&& (subscriptionCatalog.get().getCost().equals(0.0) || subscriptionCatalog.get().getCost() == null)
				&& (subscriptionCatalog.get().getPointsValue().equals(0) || subscriptionCatalog.get().getPointsValue() == null)
				&& (subscriptionCatalog.get().getValidityPeriod().equals(0) || subscriptionCatalog.get().getValidityPeriod() == null);
	}
	
//	public String fetchSubscriptionSegmentByPackageId(String phoneyTunesPackageId) {
//		if(phoneyTunesPackageId.equalsIgnoreCase(phoneyTunesAutoPackId) || phoneyTunesPackageId.equalsIgnoreCase(phoneyTunesFoodPackId)) {
//			return SubscriptionManagementConstants.SEGMENT_LIFESTYLE.get();
//		} else if(phoneyTunesPackageId.equalsIgnoreCase(phoneyTunesFoodPackId)) {
//			return SubscriptionManagementConstants.SEGMENT_FOOD.get();
//		} else {
//			return null;
//		}
//	}
	
//	public String fetchChargeabilityTypeByPackageId(String phoneyTunesPackageId) {
//		if(phoneyTunesPackageId.equalsIgnoreCase(phoneyTunesAutoPackId)) {
//			return SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get();
//		} else if(phoneyTunesPackageId.equalsIgnoreCase(phoneyTunesOnePackId)) {
//			return SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get();
//		} else if(phoneyTunesPackageId.equalsIgnoreCase(phoneyTunesFoodPackId)) {
//			return SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get();
//		} else {
//			return null;
//		}
//	}
	
	public String fetchPhoneyTunesPackageId(SubscriptionCatalog subscriptionCatalog) {
		if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase("lifestyle")) {
				return phoneyTunesLifeStyleAutoPackId;
			} else if (subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase("food")) {
				return phoneyTunesFoodAutoPackId;
			}
		} else if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
			if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase("lifestyle")) {
				return phoneyTunesLifeStyleOnePackId;
			} else if (subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase("food")) {
				return phoneyTunesFoodOnePackId;
			}
		} else if(subscriptionCatalog.getChargeabilityType().equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
			if(subscriptionCatalog.getSubscriptionSegment().equalsIgnoreCase("food")) {
				return phoneyTunesFoodLifeTimePackId;
			}
		}
		return null;
	}
	
	public String fetchPhoneyTunesPackageId(String chargeabilityType, String subscriptionSegment) {
		if(chargeabilityType.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_AUTO.get())) {
			if(subscriptionSegment.equalsIgnoreCase("lifestyle")) {
				return phoneyTunesLifeStyleAutoPackId;
			} else if (subscriptionSegment.equalsIgnoreCase("food")) {
				return phoneyTunesFoodAutoPackId;
			}
		} else if(chargeabilityType.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_ONE.get())) {
			if(subscriptionSegment.equalsIgnoreCase("lifestyle")) {
				return phoneyTunesLifeStyleOnePackId;
			} else if (subscriptionSegment.equalsIgnoreCase("food")) {
				return phoneyTunesFoodOnePackId;
			}
		} else if(chargeabilityType.equalsIgnoreCase(SubscriptionManagementConstants.CHARGEABILITY_TYPE_LIFE.get())) {
			if(subscriptionSegment.equalsIgnoreCase("food")) {
				return phoneyTunesFoodLifeTimePackId;
			}
		}
		return null;
	}
}
