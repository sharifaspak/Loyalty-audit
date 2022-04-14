package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDto {

	private String partnerCode;
	private String partnerName;
	private String partnerDescription;
	private String vendorCode;
	private String partnerType;
	private String partnerErCode;
	private String partnerAddressCode;
	private List<ContactPersonDto> contactPersons;
	private List<PartnerBillingRateDto> partnerBillingRates;
}
