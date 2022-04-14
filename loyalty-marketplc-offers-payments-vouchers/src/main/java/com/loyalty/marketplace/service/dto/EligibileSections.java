package com.loyalty.marketplace.service.dto;

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
public class EligibileSections {
	private String SectionId;
	private String SectionName;
	private String SectionDescription;
	private String SectionType;

}
