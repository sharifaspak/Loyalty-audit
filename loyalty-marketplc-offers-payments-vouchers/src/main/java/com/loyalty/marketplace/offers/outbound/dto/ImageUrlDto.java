package com.loyalty.marketplace.offers.outbound.dto;

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
public class ImageUrlDto {
	
	private String availableInChannel;
	private String imageType;
	private String imageUrl;
	private String imageUrlDr;
	private String imageUrlProd;

}
