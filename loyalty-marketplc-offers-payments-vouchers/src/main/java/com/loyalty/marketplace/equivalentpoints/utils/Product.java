package com.loyalty.marketplace.equivalentpoints.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class Product {
	
	@Value("#{'${product}'.split(',')}")
	List<String> productList;

	public Set<String> getProducts() {
		return new HashSet<>(productList);
	}

}
