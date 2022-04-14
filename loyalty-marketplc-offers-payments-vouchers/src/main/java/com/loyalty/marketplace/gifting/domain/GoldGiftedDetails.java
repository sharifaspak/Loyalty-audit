package com.loyalty.marketplace.gifting.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoldGiftedDetails {
	
	@Field("CertficateId")
	private String certificateId;
	
	@Field("GoldGifted")
	private Double goldGifted;	
}
