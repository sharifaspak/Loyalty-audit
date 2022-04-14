package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EligibileSections {
	private String sectionId;
	private String sectionName;
	private String sectionDesc;
	private String sectionType;

}
