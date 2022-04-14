package com.loyalty.marketplace.subscription.dm.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerListDMRequestDto {
	private List<String> customerList;
	
	private CustomerListDMRequestDto(CustomerListDMRequestDtoBuilder builder) {
		super();
		this.customerList = builder.customerList;
	}
	
	public static class CustomerListDMRequestDtoBuilder {

		private List<String> customerList;
		
		public CustomerListDMRequestDtoBuilder customerList(List<String> customerList) {
			this.customerList = customerList;
			return this;
		}
		
		public CustomerListDMRequestDto build() {
			return new CustomerListDMRequestDto(this);
		}
	}
}
