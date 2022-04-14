package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.mongodb.core.mapping.Field;

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
public class ProvisioningAttributes {
	
	@Field("RatePlanCode")
	private String ratePlanCode;
	@Field("RtfProductCode")
	private String rtfProductCode;
	@Field("RtfProductType")
	private String rtfProductType;
	@Field("VasCode")
	private String vasCode;
	@Field("VasActionId")
	private String vasActionId;
	@Field("PromotionalPeriod")
	private Integer promotionalPeriod;
	@Field("Feature")
	private String feature;
	@Field("ServiceId")
	private String serviceId;
	@Field("ActivityId")
	private String activityId;
	@Field("PackName")
	private String packName;
		
}
