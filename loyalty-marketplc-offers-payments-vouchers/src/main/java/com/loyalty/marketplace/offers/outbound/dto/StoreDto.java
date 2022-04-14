package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class StoreDto {
	
	private String storeCode;
	private String storeAddressEn;
	private String storeAddressAr;
	private String storeDescriptionEn;
	private String storeDescriptionAr;
	private List<String> mobileNumber;
	private List<String> storeCoordinates;

}
