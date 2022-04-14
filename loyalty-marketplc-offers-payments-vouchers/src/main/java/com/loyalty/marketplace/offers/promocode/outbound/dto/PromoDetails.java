package com.loyalty.marketplace.offers.promocode.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PromoDetails {

	private String promoCode;
	private int promoValue;
	private String promoType;
	private Date endDate;
	private int duration;
	private String promoTypeDescription;
}
