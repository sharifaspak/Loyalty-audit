package com.loyalty.marketplace.interest.outbound.entity;

import java.util.Date;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.interest.inbound.dto.InterestIdRequestDto;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.CategoryName;





@Document(collection = "Interest")

public class InterestEntity {
	@Id
	private String id;

	@Field(value = "ProgramCode")
	private String programCode;

	@Field("Name")
	private CategoryName categoryName;

	@Field(value = "ImageUrl")
	private String imageUrl;

	@Field("CategoryId")
	@DBRef
	private Category categoryId;
	
	@Field("SubcategoryId")
	@DBRef
	private Category subCategoryId;


	@Field(value = "CreatedDate")
	private Date createdDate;

	@Field(value = "CreatedUser")
	private String createdUser;

	@Field(value = "UpdatedUser")
	private String updatedUser;

	@Field(value = "UpdatedDate")
	private Date updatedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public CategoryName getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(CategoryName categoryName) {
		this.categoryName = categoryName;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Category getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Category categoryId) {
		this.categoryId = categoryId;
	}

	public Category getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Category subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	
	
}
