//package com.loyalty.marketplace.customer.segmentation.utils;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//
//import com.loyalty.marketplace.customer.segmentation.domain.CustomerSegmentDomain;
//import com.loyalty.marketplace.customer.segmentation.domain.DateValuesDomain;
//import com.loyalty.marketplace.customer.segmentation.domain.DoubleValuesDomain;
//import com.loyalty.marketplace.customer.segmentation.domain.IntegerValuesDomain;
//import com.loyalty.marketplace.customer.segmentation.domain.ListValuesDomain;
//import com.loyalty.marketplace.customer.segmentation.domain.PurchaseValuesDomain;
//import com.loyalty.marketplace.customer.segmentation.helper.dto.CustomerSegmentHeaders;
//import com.loyalty.marketplace.customer.segmentation.input.dto.CustomerSegmentRequestDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.DateValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.DoubleValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.IntegerValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.ListValuesDto;
//import com.loyalty.marketplace.customer.segmentation.input.dto.PurchaseValuesDto;
//import com.loyalty.marketplace.customer.segmentation.ouput.database.entity.CustomerSegment;
//
//
//public class CustomerSegmentDomainConfiguration {
//	
//	CustomerSegmentDomainConfiguration(){
//		
//	}
//	
//	/***
//	 * 
//	 * @param customerSegmentRequest
//	 * @param header 
//	 * @return
//	 */
//	public static CustomerSegmentDomain getCustomerSegmentDomain(boolean isInsert, CustomerSegmentRequestDto customerSegmentRequest, CustomerSegment existingCustomerSegment, CustomerSegmentHeaders header) {
//		
//		String createdUser = isInsert ? header.getUserName() : existingCustomerSegment.getName();
//		Date createdDate = isInsert ? new Date() : existingCustomerSegment.getCreatedDate();
//		return new CustomerSegmentDomain.CustomerSegmentDomainBuilder(customerSegmentRequest.getName(), 
//				 createdDate, 
//				 createdUser)
//				.id(isInsert ? null : existingCustomerSegment.getId())
//				.channelId(getListValuesDomain(customerSegmentRequest.getChannelId()))
//				.tierLevel(getListValuesDomain(customerSegmentRequest.getTierLevel()))
//				.gender(getListValuesDomain(customerSegmentRequest.getGender()))
//				.nationality(getListValuesDomain(customerSegmentRequest.getNationality()))
//				.numberType(getListValuesDomain(customerSegmentRequest.getNumberType()))
//				.accountStatus(getListValuesDomain(customerSegmentRequest.getAccountStatus()))
//				.emailVerificationStatus(getListValuesDomain(customerSegmentRequest.getEmailVerificationStatus()))
//				.days(getListValuesDomain(customerSegmentRequest.getDays()))
//				.customerType(getListValuesDomain(customerSegmentRequest.getCustomerType()))
//				.cobrandedCards(getListValuesDomain(customerSegmentRequest.getCobrandedCards()))
//				.totalTierPoints(getIntegerValuesDomain(customerSegmentRequest.getTotalTierPoints()))
//				.totalAccountPoints(getIntegerValuesDomain(customerSegmentRequest.getTotalAccountPoints()))
//				.dob(getDateValuesDomain(customerSegmentRequest.getDob()))
//				.accountStartDate(getDateValuesDomain(customerSegmentRequest.getAccountStartDate()))
//				.isFirstAccess(customerSegmentRequest.isFirstAccess())
//				.isCoBranded(customerSegmentRequest.isCoBranded())
//				.isSubscribed(customerSegmentRequest.isSubscribed())
//				.isPrimaryAccount(customerSegmentRequest.isPrimaryAccount())
//				.isTop3Account(customerSegmentRequest.isTop3Account())
//				.purchaseItems(getPurchaseDomainList(customerSegmentRequest.getPurchaseItems()))
//				.updatedDate(new Date())
//				.updatedUser(header.getUserName())
//				.build();
//	}
//    
//	/**
//	 * 
//	 * @param value
//	 * @return
//	 */
//	private static ListValuesDomain getListValuesDomain(ListValuesDto value) {
//		
//		return  !ObjectUtils.isEmpty(value)
//			  ? new ListValuesDomain(value.getEligibleTypes(), value.getExclusionTypes())
//			  : null;
//	}
//	
//	/**
//	 * 
//	 * @param value
//	 * @return
//	 */
//	private static IntegerValuesDomain getIntegerValuesDomain(IntegerValuesDto value) {
//		
//		return  !ObjectUtils.isEmpty(value)
//			  ? new IntegerValuesDomain(value.getOperation(),
//					                    value.getHighValue(), 
//					                    value.getLowValue(), 
//					                    value.getExactValue())
//			  : null;
//	}
//	
//	/**
//	 * 
//	 * @param value
//	 * @return
//	 */
//    private static DoubleValuesDomain getDoubleValuesDomain(DoubleValuesDto value) {
//		
//		return  !ObjectUtils.isEmpty(value)
//			  ? new DoubleValuesDomain(value.getOperation(),
//					                   value.getHighValue(), 
//					                   value.getLowValue(), 
//					                   value.getExactValue())
//			  : null;
//	}
//    
//    
//    /**
//     * 
//     * @param value
//     * @return
//     */
//    private static DateValuesDomain getDateValuesDomain(DateValuesDto value) {
//    	
//    	return  !ObjectUtils.isEmpty(value)
//  			  ? new DateValuesDomain(value.isDuration(), 
//  					                 value.getOperation(), 
//  					                 value.getHighValue(), 
//				                     value.getLowValue(), 
//				                     value.getExactValue(), 
//  					                 value.getDuration(), 
//  					                 value.getUnit())
//  			  : null;
//    	
//    	
//    }
//    
//    /**
//     * 
//     * @param value
//     * @return
//     */
//    private static PurchaseValuesDomain getPurchaseValuesDomain(PurchaseValuesDto value) {
//    	
//    	return  !ObjectUtils.isEmpty(value)
//  			  ? new PurchaseValuesDomain(value.getPurchaseItem(), 
//  					  value.getPaymentMethod(), 
//  					  getDoubleValuesDomain(value.getPurchaseAmount()), 
//  					  getDoubleValuesDomain(value.getSpentAmount()), 
//  					  getIntegerValuesDomain(value.getSpentPoints()), 
//  					  getDateValuesDomain(value.getPurchaseDate()))
//  			  : null;
//    	
//    	
//    }
//    
//    /**
//     * 
//     * @param purchaseItems
//     * @return
//     */
//    private static List<PurchaseValuesDomain> getPurchaseDomainList(List<PurchaseValuesDto> valueList) {
//		
//    	List<PurchaseValuesDomain> purchaseDomainList = !CollectionUtils.isEmpty(valueList)
//    			  ? new ArrayList<>(valueList.size())
//    	          : null;
//    	if(!CollectionUtils.isEmpty(valueList)) {
//    		
//    		valueList.forEach(v->purchaseDomainList.add(getPurchaseValuesDomain(v)));
//    		
//    	}
//    	
//    	return purchaseDomainList;
//	}
//	
//	
//		
//}
