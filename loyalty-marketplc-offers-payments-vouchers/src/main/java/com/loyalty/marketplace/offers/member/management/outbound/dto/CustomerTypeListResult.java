package com.loyalty.marketplace.offers.member.management.outbound.dto;

import java.util.List;

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
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypeListResult {
	private List<ParentChlidCustomer> customerTypeList;
}
