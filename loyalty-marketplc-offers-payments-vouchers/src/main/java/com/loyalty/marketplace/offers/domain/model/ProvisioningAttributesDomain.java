package com.loyalty.marketplace.offers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProvisioningAttributesDomain {
	
	private String ratePlanCode;
	private String rtfProductCode;
	private String rtfProductType;
	private String vasCode;
	private String vasActionId;
	private Integer promotionalPeriod;
	private String feature;
	private String serviceId;
	private String activityId;
	private String packName;
	
}
