package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class CustomerType {
	
	@Field("Eligible")
	private List<String> eligibleCustomerTypes;
	@Field("Exclusion")
	private List<String> exclusionTypes;
	
}
