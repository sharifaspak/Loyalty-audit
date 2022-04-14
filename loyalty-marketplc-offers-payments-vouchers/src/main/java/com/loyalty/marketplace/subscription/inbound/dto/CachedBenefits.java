package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class CachedBenefits {
	
	private boolean isElifePrimary;
	private String elifeCatalogId;
//	private String primaryCatalogId;
	private Map<String,String> primaryCatalogIdMap;
	private List<CachedCatalogBenefits> elifeBenefits;
	private List<CachedCatalogBenefits> primaryBenefits;
	private List<CachedCatalogBenefits> bogoBenefits;
	private Map<String,List<CachedCatalogBenefits>> accountBenefits;
}
