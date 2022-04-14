package com.loyalty.marketplace.offers.helper.dto;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UpdateDate {
	
	@Field(OffersDBConstants.LAST_UPDATE_DATE)
	private Date date;
	
}
