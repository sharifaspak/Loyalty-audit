package com.loyalty.marketplace.interest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.outbound.database.entity.Category;

public interface CategotyRepository extends MongoRepository<Category, String>{
	Optional<Category>  findByCategoryId(String categoryId);
	//Optional<Category> findBySubCategoryId(String subCategoryId);

	List<Category> findByProgramCodeIgnoreCase(String programCode);
	
}
