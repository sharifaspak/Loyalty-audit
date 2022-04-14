package com.loyalty.marketplace.promote.partner.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountriesResponse {
	private String id;
	private String countryName;
	private String countryCode;
	private String arabicName;
	private String isoCode;
}
