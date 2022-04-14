package com.loyalty.marketplace.merchants.domain.model;

import java.util.Date;

import com.loyalty.marketplace.domain.model.NameDomain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDomain {
	
	private String categoryId;
	private NameDomain categoryName;
	private CategoryDomain parentCategory;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	
	public CategoryDomain(CategoryBuilder category) {
		super();
		this.categoryId = category.categoryId;
		this.categoryName = category.categoryName;
		this.parentCategory = category.parentCategory;
		this.usrCreated = category.usrCreated;
		this.usrUpdated = category.usrUpdated;
		this.dtCreated = category.dtCreated;
		this.dtUpdated = category.dtUpdated;
	}
	
	public static class CategoryBuilder {
		
		private String categoryId;
		private NameDomain categoryName;
		private CategoryDomain parentCategory;
		private String usrCreated;
		private String usrUpdated;
		private Date dtCreated;
		private Date dtUpdated;
		
		public CategoryBuilder(String categoryId) {
			this.categoryId = categoryId;
		}
		
		public CategoryBuilder(String categoryNameEn, String categoryNameAr) {	
			this.categoryName = new NameDomain(categoryNameEn,categoryNameAr);
		}
		
		public CategoryBuilder categoryName(NameDomain categoryName) {
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
