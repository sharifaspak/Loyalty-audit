package com.loyalty.marketplace.offers.inbound.dto;

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
@NoArgsConstructor
public class EligibleOffersRequest {

    private String filterFlag;
	private String accountNumber;
    private String categoryId;
    private String subCategoryId;
    private String keywords;
    private String merchantCode;
    private Integer page;
    private Integer pageLimit;
  
}