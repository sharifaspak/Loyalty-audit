package com.loyalty.marketplace.offers.outbound.service.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class cacheOffersResponse {
	
	private int responseCode;
	private String responseMsg;
	
	public cacheOffersResponse() {
		
	}
}
