package com.loyalty.marketplace.offers.outbound.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(collection = OffersDBConstants.PAYMENT_METHOD)
public class PaymentMethod {
	
	@Id
	private String paymentMethodId;
	
	@Field("Description")
	private String description;
	
}
