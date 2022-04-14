package com.loyalty.marketplace.offers.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferSuccessCodes;
import com.loyalty.marketplace.offers.inbound.dto.OfferTypeDto;
import com.loyalty.marketplace.offers.inbound.dto.PurchasePaymentMethodDto;
import com.loyalty.marketplace.offers.inbound.dto.SubCategoryDto;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferTypeDescription;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchasePaymentMethod;
import com.loyalty.marketplace.offers.outbound.database.repository.MarketplaceActivityRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferTypeRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchasePaymentMethodRepository;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.database.repository.DenominationRepository;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.repository.PaymentMethodRepository;
import com.loyalty.marketplace.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
@Component
public class OfferConfigurationControllerHelper {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	OfferTypeRepository offerTypeRepository;
	
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	DenominationRepository denominationRepository;

	@Autowired
	PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	MarketplaceActivityRepository marketplaceActivityRepository;
	
	@Autowired
	PurchasePaymentMethodRepository purchasePaymentMethodRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	StoreRepository storeRepository;
	
	/**
	 * This method helps to set the details of offerType object to be saved
	 * @param userName 
	 * @param program 
	 * 
	 */
	public OfferType setOfferTypeToSave(String program, List<String> offerTypes,OfferTypeDto offerType,String userName, ResultResponse resultResponse)
	{										
		OfferType offerTypeToSave = null;
		
		if (!offerTypes.contains(offerType.getDescriptionEn().toLowerCase())) {
			
			offerTypeToSave = new OfferType();
			offerTypeToSave.setProgramCode(program);
			offerTypeToSave.setOfferTypeId(offerType.getOfferTypeId());
			OfferTypeDescription offerTypeDescription = new OfferTypeDescription();
			offerTypeDescription.setTypeDescriptionEn(offerType.getDescriptionEn());
			offerTypeDescription.setTypeDescriptionAr(offerType.getDescriptionAr());
			offerTypeToSave.setOfferDescription(offerTypeDescription);
			offerTypeToSave.setOfferTypeId(offerType.getOfferTypeId());

			List<PaymentMethod> paymentMethods = new ArrayList<>();

			if (offerType.getPaymentMethods() != null) {
				for (String method : offerType.getPaymentMethods()) {
					PaymentMethod paymentMethodToSave = paymentMethodRepository
							.findByPaymentMethodId(method);
					if(null!=paymentMethodToSave) {
                        paymentMethods.add(paymentMethodToSave);
                    }else {
                    	resultResponse.addErrorAPIResponse(OfferErrorCodes.INVALID_PAYMENTMETHOD.getIntId(),
            					OfferErrorCodes.INVALID_PAYMENTMETHOD.getMsg() + OfferConstants.UNDERSCORE_SEPARATOR.get() + offerType.getDescriptionEn() + OfferConstants.MESSAGE_SEPARATOR.get() + method);
                    }
				}
			}
			offerTypeToSave.setDtCreated(new Date());
			offerTypeToSave.setUsrCreated(userName);
			
		} else {

			resultResponse.addErrorAPIResponse(OfferErrorCodes.DUPLICATE_OFFERTYPE.getIntId(),
					OfferErrorCodes.DUPLICATE_OFFERTYPE.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + offerType.getDescriptionEn());
		}
		return offerTypeToSave;
	}
	
	/**
	 * 
	 * @param offerType
	 * @param offerTypeRequest
	 * @param resultResponse
	 * @return validated offer type sent for updation
	 * 
	 */
	public ResultResponse validateOfferTypeToUpdate(OfferType offerType,OfferTypeDto offerTypeRequest,ResultResponse resultResponse)
	{
		if(offerType==null) {
			resultResponse.addErrorAPIResponse(OfferErrorCodes.OFFERTYPE_NOT_AVAILABLE_TO_UPDATE.getIntId(),
					OfferErrorCodes.OFFERTYPE_NOT_AVAILABLE_TO_UPDATE.getMsg());
			resultResponse.setResult(OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getId(),
					OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getMsg());
			return resultResponse;
			
		}else if(!offerType.getOfferTypeId().equalsIgnoreCase(offerTypeRequest.getOfferTypeId())){
			resultResponse.addErrorAPIResponse(OfferErrorCodes.OFFERTYPE_ID_CANNOT_BE_CHANGED.getIntId(),
					OfferErrorCodes.OFFERTYPE_ID_CANNOT_BE_CHANGED.getMsg());
			resultResponse.setResult(OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getId(),
					OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getMsg());
			return resultResponse;
			
		}else if(!offerType.getOfferDescription().getTypeDescriptionEn().equalsIgnoreCase(offerTypeRequest.getDescriptionEn())){
			resultResponse.addErrorAPIResponse(OfferErrorCodes.OFFERTYPE_DESC_EN_CANNOT_BE_CHANGED.getIntId(),
					OfferErrorCodes.OFFERTYPE_DESC_EN_CANNOT_BE_CHANGED.getMsg());
			resultResponse.setResult(OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getId(),
					OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getMsg());
			return resultResponse;
		
		}else if(!offerType.getOfferDescription().getTypeDescriptionAr().equalsIgnoreCase(offerTypeRequest.getDescriptionAr())){
			resultResponse.addErrorAPIResponse(OfferErrorCodes.OFFERTYPE_DESC_AR_CANNOT_BE_CHANGED.getIntId(),
					OfferErrorCodes.OFFERTYPE_DESC_AR_CANNOT_BE_CHANGED.getMsg());
			resultResponse.setResult(OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getId(),
					OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getMsg());
			return resultResponse;
		}
		return resultResponse;
	}
	
	/**
	 * This method helps to set details of offerType object to be updated
	 * @param userName 
	 * @param program 
	 * 
	 */
	
	public ResultResponse setOfferTypeToUpdate(String program,List<String> invalidPaymentMethods,List<PaymentMethod> paymentMethodToSave,OfferType offerType,String userName,ResultResponse resultResponse)
	{
		if(!invalidPaymentMethods.isEmpty()){
			resultResponse.addErrorAPIResponse(OfferErrorCodes.PAYMENT_METHOD_NOT_EXISTING.getIntId(),
					OfferErrorCodes.PAYMENT_METHOD_NOT_EXISTING.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + invalidPaymentMethods);
			resultResponse.setResult(OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getId(),
					OfferErrorCodes.OFFERTYPE_UPDATION_FAILED.getMsg());
			return resultResponse;
		}else {
			offerType.setProgramCode(program);
			offerType.setDtCreated(offerType.getDtCreated());
			offerType.setUsrCreated(offerType.getUsrCreated());
			offerType.setDtUpdated(new Date());
			offerType.setUsrUpdated(userName);
			offerTypeRepository.save(offerType);	
			resultResponse.setResult(OfferSuccessCodes.OFFERTYPE_UPDATED_SUCCESSFULLY.getId(),
				OfferSuccessCodes.OFFERTYPE_UPDATED_SUCCESSFULLY.getMsg());
		}
		return resultResponse;
	}
	
	/**
	 * This method helps to set the details of subCategory object to be saved
	 * @param userName 
	 * @param program 
	 * 
	 */
	public Category setSubCategoryToSave(String program, List<String> categoryDescriptionEn, SubCategoryDto subCategory,String userName,ResultResponse resultResponse)
	{
		Category categoryToSave = null;
		
		if (!categoryDescriptionEn.contains(subCategory.getSubCategoryNameEn().toLowerCase())) {
	
			Category parent = categoryRepository.findByCategoryId(subCategory.getParentCategory());
			if(parent!=null) {
				categoryToSave = new Category();
				categoryToSave.setProgramCode(program);
				categoryToSave.setCategoryId(subCategory.getSubCategoryId());
				CategoryName name = new CategoryName();
				name.setCategoryNameEn(subCategory.getSubCategoryNameEn());
				name.setCategoryNameAr(subCategory.getSubCategoryNameAr());
				categoryToSave.setCategoryName(name);
				categoryToSave.setParentCategory(parent);
				categoryToSave.setDtCreated(new Date());
				categoryToSave.setUsrCreated(userName);
			}else {
				resultResponse.addErrorAPIResponse(OfferErrorCodes.PARENT_CATEGORY_NOT_EXISTING.getIntId(),
						OfferErrorCodes.PARENT_CATEGORY_NOT_EXISTING.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + subCategory.getParentCategory());
			}
			
		} else {
			resultResponse.addErrorAPIResponse(OfferErrorCodes.DUPLICATE_SUBCATEGORY.getIntId(),
					OfferErrorCodes.DUPLICATE_SUBCATEGORY.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + subCategory.getSubCategoryNameEn());
			}
		
		return categoryToSave;
		
	}	
	
	
	/**
     * This method helps to set the details of purchasePaymentMethod object to be saved
     * @param userName 
     * @param program 
     * 
     */
    public PurchasePaymentMethod setPurchasePaymentMethodToSave(String program, List<String> purchasePaymentMethods,PurchasePaymentMethodDto purchasePaymentMethod,String userName, ResultResponse resultResponse)
    {                                        
        PurchasePaymentMethod purchasePaymentMethodToSave = null;
        
        if (!purchasePaymentMethods.contains(purchasePaymentMethod.getPurchaseItem().toLowerCase())) {
            
            purchasePaymentMethodToSave = new PurchasePaymentMethod();
            purchasePaymentMethodToSave.setProgramCode(program);
            purchasePaymentMethodToSave.setPurchaseItem(purchasePaymentMethod.getPurchaseItem());

 

            List<PaymentMethod> paymentMethods = new ArrayList<>();

 

            if (purchasePaymentMethod.getPaymentMethods() != null) {
                for (String method : purchasePaymentMethod.getPaymentMethods()) {
                    PaymentMethod paymentMethodToSave = paymentMethodRepository
                            .findByPaymentMethodId(method);
                    if(null!=paymentMethodToSave) {
                        paymentMethods.add(paymentMethodToSave);
                    }else {
                        resultResponse.addErrorAPIResponse(OfferErrorCodes.INVALID_PAYMENTMETHOD.getIntId(),
                                OfferErrorCodes.INVALID_PAYMENTMETHOD.getMsg() + OfferConstants.UNDERSCORE_SEPARATOR.get() + purchasePaymentMethod.getPurchaseItem() + OfferConstants.MESSAGE_SEPARATOR.get() + method);
                    }
                }
            }
            purchasePaymentMethodToSave.setPaymentMethods(paymentMethods);
            purchasePaymentMethodToSave.setDtCreated(new Date());
            purchasePaymentMethodToSave.setUsrCreated(userName);
            
        } else {

 

            resultResponse.addErrorAPIResponse(OfferErrorCodes.DUPLICATE_PURCHASE_ITEM.getIntId(),
                    OfferErrorCodes.DUPLICATE_PURCHASE_ITEM.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + purchasePaymentMethod.getPurchaseItem());
        }
        return purchasePaymentMethodToSave;
    }
    
    /**
    *
    * @param purchasePaymentMethod
    * @param purchasePaymentMethodRequest
    * @param resultResponse
    * @return validated purchase item sent for updation
    *
    */
   public ResultResponse validatePurchaseItemToUpdate(PurchasePaymentMethod purchasePaymentMethod,PurchasePaymentMethodDto purchasePaymentMethodRequest,ResultResponse resultResponse)
   {
       if(purchasePaymentMethod==null) {
           resultResponse.addErrorAPIResponse(OfferErrorCodes.PURCHASE_ITEM_NOT_AVAILABLE_TO_UPDATE.getIntId(),
                   OfferErrorCodes.PURCHASE_ITEM_NOT_AVAILABLE_TO_UPDATE.getMsg());
           resultResponse.setResult(OfferErrorCodes.PURCHASE_PAYMENT_METHOD_UPDATION_FAILED.getId(),
                   OfferErrorCodes.PURCHASE_PAYMENT_METHOD_UPDATION_FAILED.getMsg());
           return resultResponse;
          
       }else if(!purchasePaymentMethod.getPurchaseItem().equalsIgnoreCase(purchasePaymentMethodRequest.getPurchaseItem())){
           resultResponse.addErrorAPIResponse(OfferErrorCodes.PURCHASE_ITEM_CANNOT_BE_CHANGED.getIntId(),
                   OfferErrorCodes.PURCHASE_ITEM_CANNOT_BE_CHANGED.getMsg());
           resultResponse.setResult(OfferErrorCodes.PURCHASE_PAYMENT_METHOD_UPDATION_FAILED.getId(),
                   OfferErrorCodes.PURCHASE_PAYMENT_METHOD_UPDATION_FAILED.getMsg());
           return resultResponse;
          
       }
       return resultResponse;
   }
   
   /**
    * This method helps to update paymentMethods of purchaseItem object to be updated
    * @param userName
    * @param program
    *
    */
   public ResultResponse setPurchaseItemToUpdate(String program,List<String> invalidPaymentMethods,List<PaymentMethod> paymentMethodToSave,PurchasePaymentMethod purchasePaymentMethod,String userName,ResultResponse resultResponse)
   {
       if(!invalidPaymentMethods.isEmpty()){
           resultResponse.addErrorAPIResponse(OfferErrorCodes.PAYMENT_METHOD_NOT_EXISTING.getIntId(),
                   OfferErrorCodes.PAYMENT_METHOD_NOT_EXISTING.getMsg() + OfferConstants.MESSAGE_SEPARATOR.get() + invalidPaymentMethods);
           resultResponse.setResult(OfferErrorCodes.PURCHASE_PAYMENT_METHOD_UPDATION_FAILED.getId(),
                   OfferErrorCodes.PURCHASE_PAYMENT_METHOD_UPDATION_FAILED.getMsg());
           return resultResponse;
       }else {
           purchasePaymentMethod.setPaymentMethods(paymentMethodToSave);
           purchasePaymentMethod.setProgramCode(program);
           purchasePaymentMethod.setDtCreated(purchasePaymentMethod.getDtCreated());
           purchasePaymentMethod.setUsrCreated(purchasePaymentMethod.getUsrCreated());
           purchasePaymentMethod.setDtUpdated(new Date());
           purchasePaymentMethod.setUsrUpdated(userName);
           purchasePaymentMethodRepository.save(purchasePaymentMethod);   
           resultResponse.setResult(OfferSuccessCodes.PURCHASE_PAYMENT_METHODS_UPDATED_SUCCESSFULLY.getId(),
               OfferSuccessCodes.PURCHASE_PAYMENT_METHODS_UPDATED_SUCCESSFULLY.getMsg());
       }
       return resultResponse;
   }
	
}