package com.loyalty.marketplace.merchants.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListMerchantsWithTotal{

	List<Merchant> listMerchants;

	private long totalRecords;
	

	
}
