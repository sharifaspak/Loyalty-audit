package com.loyalty.marketplace.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loyalty.marketplace.outbound.database.entity.Category;

public interface CategoryRepository extends MongoRepository<Category,String> {
		
	public Category findByCategoryId(String categoryId);
	public List<Category> findByParentCategory(Category parentCategory);
}
