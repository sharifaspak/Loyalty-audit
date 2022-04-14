//package com.loyalty.marketplace.customer.segmentation.constants;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import com.loyalty.marketplace.constants.RequestMappingConstants;
//
//public class CustomerSegmentConfigurationConstants extends RequestMappingConstants{
//
//	private CustomerSegmentConfigurationConstants() {}
//	
//	
//	//All controller API end points
//	public static final String MARKETPLACE = "Marketplace"; 
//	public static final String MARKETPLACE_BASE = "/marketplace";
//	public static final String CREATE_CUSTOMER_SEGMENT = "/customerSegment";
//	public static final String UPDATE_CUSTOMER_SEGMENT = "/customerSegment/{customerSegmentId}";
//	
//	//All DB constants
//	public static final String CUSTOMER_SEGMENTATION = "CustomerSegment";
//	
//	public static final List<String> OPERATIONS = 
//			Collections.unmodifiableList(Arrays.asList(CustomerSegmentConstants.EQUAL_TO.get(),
//					CustomerSegmentConstants.NOT_EQUAL_TO.get(), CustomerSegmentConstants.GREATER_THAN.get(), 
//					CustomerSegmentConstants.GREATER_THAN_EQUAL_TO.get(), CustomerSegmentConstants.LESS_THAN.get(), 
//					CustomerSegmentConstants.LESS_THAN_EQUAL_TO.get(), CustomerSegmentConstants.BETWEEN_EXCLUSIVE.get(),
//					CustomerSegmentConstants.BETWEEN_INCLUSIVE.get(), CustomerSegmentConstants.NOT_BETWEEN_EXCLUSIVE.get(), 
//					CustomerSegmentConstants.NOT_BETWEEN_INCLUSIVE.get()));
//	public static final List<String> EXACT_OPERATIONS = 
//			Collections.unmodifiableList(Arrays.asList(CustomerSegmentConstants.EQUAL_TO.get(),
//					CustomerSegmentConstants.NOT_EQUAL_TO.get(), CustomerSegmentConstants.GREATER_THAN.get(), 
//					CustomerSegmentConstants.GREATER_THAN_EQUAL_TO.get(), CustomerSegmentConstants.LESS_THAN.get(), 
//					CustomerSegmentConstants.LESS_THAN_EQUAL_TO.get()));
//	public static final List<String> RANGE_OPERATIONS = 
//			Collections.unmodifiableList(Arrays.asList(CustomerSegmentConstants.BETWEEN_EXCLUSIVE.get(),
//					CustomerSegmentConstants.BETWEEN_INCLUSIVE.get(), CustomerSegmentConstants.NOT_BETWEEN_EXCLUSIVE.get(), 
//					CustomerSegmentConstants.NOT_BETWEEN_INCLUSIVE.get()));
//	public static final List<String> UNITS = 
//			Collections.unmodifiableList(Arrays.asList(CustomerSegmentConstants.DURATION_UNIT_DAY.get(),
//					CustomerSegmentConstants.DURATION_UNIT_MONTH.get(), CustomerSegmentConstants.DURATION_UNIT_YEAR.get()));
//
//	
//	//All property file values : Member Management Service
//	public static final String  MEMBER_MANAGEMENT_URI ="${memberManagement.uri}";
//	public static final String GET_MEMBER_PATH = "${memberManagement.getMember.path}" ;
//	
//	
//	//All property file values : Decision Manager Service
//	public static final String DECISION_MANAGER_URI = "${decisionManager.uri}";
//	public static final String RULES_CHECK_PATH = "${decisionManager.rulescheck.path}";
//	
//	public static final String DEFAULT_CUSTOMER_SEGMENT = "Normal";
//	
//	
//	
//	
//	
//}
