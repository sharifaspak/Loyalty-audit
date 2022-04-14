package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ListValuesDto {
	
	List<String> eligibleTypes;
	List<String> exclusionTypes;

}
