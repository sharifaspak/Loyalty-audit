package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeCatalogueDTO {

	private String id;
	private EventTypeCatalogueName name;
	private EventTypeCatalogueDescription description;
}
