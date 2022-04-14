//package com.loyalty.marketplace.customer.segmentation.helper;
//
//import java.util.List;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.loyalty.marketplace.customer.segmentation.ouput.database.entity.CustomerSegment;
//import com.loyalty.marketplace.customer.segmentation.ouput.database.repository.CustomerSegmentRepository;
//
//@Component
//public class CustomerSegmentRepositoryHelper {
//	
//	@Autowired
//	CustomerSegmentRepository customerSegmentRepository;
//
//
//	/**
//	 * 
//	 * @param name
//	 * @return
//	 */
//	public CustomerSegment findCustomerSegmentByName(String name) {
//		
//		return customerSegmentRepository.findByName(name);
//	}
//
//
//	/**
//	 * 
//	 * @param customerSegmentToSave
//	 * @return
//	 */
//	public CustomerSegment saveCustomerSegment(CustomerSegment customerSegment) {
//		
//		return customerSegmentRepository.save(customerSegment);
//	}
//	
//	/**
//     * 
//     * @param segmentNames
//     * @return
//     */
//    public List<CustomerSegment> getCustomerSegmentListByName(List<String> segmentNames) {
//		
//		return !CollectionUtils.isEmpty(segmentNames)
//			 ? customerSegmentRepository.findAllByName(segmentNames)
//			 : customerSegmentRepository.findAll();
//	}
//	
//}
