package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class OfferCategoryResponseDto {
	private static final Logger LOG = LoggerFactory.getLogger(OfferCategoryResponseDto.class);

	private TitleResponseDto category;
	private List<TitleResponseDto> subCategory;
	
	public static class GroupCategory {
		
		private String categoryId;
		
		public GroupCategory(OfferCategoryResponseDto offerCategoryResponseDto) {
			this.categoryId = offerCategoryResponseDto.category.getId();
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			GroupCategory that = (GroupCategory) o;
			return Objects.equal(categoryId, that.categoryId);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(categoryId);
		}
	}
	
	public OfferCategoryResponseDto mergeCategory(OfferCategoryResponseDto other) {
		assert(new GroupCategory(other).equals(new GroupCategory(this)));
		//this.subCategory.addAll(other.subCategory);
		LOG.info("this.subCategory : ",this.subCategory);
		Set<TitleResponseDto> subCategorySet = new LinkedHashSet<>(this.subCategory);
		
		subCategorySet.addAll(other.subCategory);
		List<TitleResponseDto> mergedSubCategory = new ArrayList<>(subCategorySet);
		LOG.info("mergedSubCategory : ",mergedSubCategory);
		
		return new OfferCategoryResponseDto(this.category, mergedSubCategory);
	}
}
