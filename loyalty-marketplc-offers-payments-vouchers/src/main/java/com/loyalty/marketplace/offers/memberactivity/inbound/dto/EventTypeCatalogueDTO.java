package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import javax.validation.constraints.NotNull;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.EventTypeCatalogueDescription;
import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.EventTypeCatalogueName;

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
public class EventTypeCatalogueDTO {

	private String id;
	
	@NotNull
	private EventTypeCatalogueName name;
	private EventTypeCatalogueDescription description;
}
