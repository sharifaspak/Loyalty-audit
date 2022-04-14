package com.loyalty.marketplace.image.inbound.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GenerateMetaDataRequest {

	@NotEmpty(message = "{validation.merchantOfferParameters.imageType.notEmpty.msg}")
	private String imageType;
	
	@NotEmpty(message = "{validation.merchantOfferParameters.domainName.notEmpty.msg}")
	private String domainName;
	
	@NotEmpty(message = "{validation.merchantOfferParameters.availableInChannel.notEmpty.msg}")
	private String availableInChannel;
	
}
