package com.loyalty.marketplace.subscription.outbound.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class BenefitsResponseDto {
	
	private TitleResponseDto offerType;
	private List<OfferCategoryResponseDto> offerCategory;
	
	public static class GroupOfferType {
		
		private String offerTypeId;
		
		public GroupOfferType(BenefitsResponseDto benefitsResponseDto) {
			this.offerTypeId = benefitsResponseDto.getOfferType().getId();
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			GroupOfferType that = (GroupOfferType) o;
			return Objects.equals(offerTypeId,that.offerTypeId);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(offerTypeId);
		}
	}
	
	
	public BenefitsResponseDto mergeOfferType(BenefitsResponseDto other) {
		assert(new GroupOfferType(other).equals(new GroupOfferType(this)));
		if(null != other.offerCategory) {
			this.offerCategory.addAll(other.offerCategory);
		}
		List<OfferCategoryResponseDto> mergedCategory = new ArrayList<>();
		if(null != this.offerCategory) {
			mergedCategory =
	                new ArrayList<>(this.offerCategory.stream()
	                		.collect(Collectors.toMap(OfferCategoryResponseDto.GroupCategory::new, p -> p, OfferCategoryResponseDto::mergeCategory)).values());
		}
		return new BenefitsResponseDto(this.offerType, mergedCategory);
	}
}
