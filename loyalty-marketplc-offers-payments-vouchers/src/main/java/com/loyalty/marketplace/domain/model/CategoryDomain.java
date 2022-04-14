package com.loyalty.marketplace.domain.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class CategoryDomain {
	
	private String categoryId;
	private String programCode;
	private CategoryNameDomain categoryName;
	private CategoryDomain parentCategory;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	
	public CategoryDomain(CategoryBuilder category) {
		super();
		this.categoryId = category.categoryId;
		this.programCode = category.programCode;
		this.categoryName = category.categoryName;
		this.parentCategory = category.parentCategory;
		this.usrCreated = category.usrCreated;
		this.usrUpdated = category.usrUpdated;
		this.dtCreated = category.dtCreated;
		this.dtUpdated = category.dtUpdated;
	}
	
	public static class CategoryBuilder {
		
		
		private String categoryId;
		private String programCode;
		private CategoryNameDomain categoryName;
		private CategoryDomain parentCategory;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;
		
		public CategoryBuilder(String categoryId) {
			this.categoryId = categoryId;
		}
		
		public CategoryBuilder(String categoryId, String categoryNameEn, String categoryNameAr) {
			
			this.categoryId = categoryId;
			this.categoryName = new CategoryNameDomain(categoryNameEn,categoryNameAr);
		}
		
		public CategoryBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public CategoryBuilder categoryId(String categoryId) {
			this.categoryId = categoryId;
			return this;
		}
		
		public CategoryBuilder categoryName(CategoryNameDomain categoryName) {
			this.categoryName = categoryName;
			return this;
		}
		
		public CategoryBuilder parentCategory(CategoryDomain parentCategory) {
			this.parentCategory = parentCategory;
			return this;
		}
		
		public CategoryBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}
		
		public CategoryBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public CategoryBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}

		public CategoryBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}
		
		public CategoryDomain build() {
			return new CategoryDomain(this);
		}

	}
		
}
