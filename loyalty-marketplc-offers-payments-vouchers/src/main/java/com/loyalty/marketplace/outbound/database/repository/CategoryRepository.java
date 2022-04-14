package com.loyalty.marketplace.outbound.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.outbound.database.entity.Category;

public interface CategoryRepository extends MongoRepository<Category,String> {
		
	public List<Category> findByCategoryIdInAndProgramCodeIgnoreCase(List<String> categoryIdList, String program);
	public List<Category> findByParentCategoryInAndProgramCodeIgnoreCase(List<Category> parentCategoryList, String program);
	public Category findByCategoryId(String categoryId);
	
	public Category findByCategoryIdAndProgramCodeIgnoreCase(String categoryId, String programCode);
	
	public List<Category> findByParentCategory(Category parentCategory);
	
	public List<Category> findByParentCategoryAndProgramCodeIgnoreCase(Category parentCategory, String programCode);
	
	@Query("{'categoryId':?0, 'parentCategory.categoryId' : ?1}")
	public Category findByCategoryIdAndParentCategory(String subCatgeoryId, String categoryId);
	
	@Query("{'categoryId': {$in : ?0}}")
	public List<Category> findCategoryByIdList(List<String> categoryIdList);

	public List<Category> findByProgramCodeIgnoreCase(String program);
	public List<Category> findByParentCategoryNullAndProgramCodeIgnoreCase(String program);
}
