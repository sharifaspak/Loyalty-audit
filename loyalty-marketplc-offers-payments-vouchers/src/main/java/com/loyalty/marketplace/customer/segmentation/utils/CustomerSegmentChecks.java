//package com.loyalty.marketplace.customer.segmentation.utils;
//
//import java.util.Arrays;
//
//import org.apache.commons.collections.CollectionUtils;
//
//import com.loyalty.marketplace.customer.segmentation.constants.CustomerSegmentConfigurationConstants;
//import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
//import com.loyalty.marketplace.outbound.dto.ResultResponse;
//
//public class CustomerSegmentChecks{
//	
//	CustomerSegmentChecks(){}
//	
//	/**
//	 * 
//	 * @param resultResponse
//	 * @return checks if there are no errors in the result response
//	 */
//    public static boolean checkNoErrors(ResultResponse resultResponse) {
//		
//		return CollectionUtils.isEmpty(resultResponse.getApiStatus().getErrors());
//		
//	}
//    
//    /**
//     * 
//     * @param operation
//     * @return
//     */
//    public static boolean checkValidOperation(String operation) {
//    	
//    	return CollectionUtils.containsAny(CustomerSegmentConfigurationConstants.OPERATIONS, Arrays.asList(operation));	
//    	
//    }
//    
//    /**
//     * 
//     * @param operation
//     * @return
//     */
//    public static boolean checkExactOperation(String operation) {
//    	
//    	return CollectionUtils.containsAny(CustomerSegmentConfigurationConstants.EXACT_OPERATIONS, Arrays.asList(operation));	
//    	
//    }
//    
//    /**
//     * 
//     * @param operation
//     * @return
//     */
//    public static boolean checkRangeOperation(String operation) {
//    	
//    	return CollectionUtils.containsAny(CustomerSegmentConfigurationConstants.RANGE_OPERATIONS, Arrays.asList(operation));	
//    	
//    }
//    
//    /**
//	 * 
//	 * @param unit
//	 * @return
//	 */
//	public static boolean checkValidUnit(String unit) {
//		
//		return CollectionUtils.containsAny(OffersConfigurationConstants.UNITS, Arrays.asList(unit));
//	}
//	
//	
//	
//	    	
//}
