package com.loyalty.marketplace.equivalentpoints.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class ProductWithDenominations {
	
	@Value("#{'${productWithDenominations}'.split(',')}")
	List<String> productWithDenominationsList;

	public Set<String> getProductWithDenominations() {
		return new HashSet<>(productWithDenominationsList);
	}

}
