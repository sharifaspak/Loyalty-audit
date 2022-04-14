//package com.loyalty.marketplace.customer.segmentation.ouput.database.repository;
//
//import java.util.List;
//
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//
//import com.loyalty.marketplace.customer.segmentation.ouput.database.entity.CustomerSegment;
//
//public interface CustomerSegmentRepository extends MongoRepository<CustomerSegment, String>{
//
//	@Query("{'name' : {$in : ?0}}")
//	public List<CustomerSegment> findAllByName(List<String> segmentNames);
//
//	public CustomerSegment findByName(String name);
//	
//	
//
//}
