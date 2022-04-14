package com.loyalty.marketplace.offers.member.management.outbound.dto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EligibilitySectionDto {
	private String sectionId;
	private String sectionName;
	private String sectionDescription;
	private String sectionType;

}
