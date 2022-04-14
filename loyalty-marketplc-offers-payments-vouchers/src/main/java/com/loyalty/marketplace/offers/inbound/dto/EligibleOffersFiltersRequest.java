package com.loyalty.marketplace.offers.inbound.dto;

import java.util.List;

import javax.validation.constraints.Min;

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
public class EligibleOffersFiltersRequest {

    private String accountNumber;
    private String categoryId;
    private List<String> subCategoryList;
    private String merchantCode;
    private String keywords;
    private String emirate;
    private List<String> areas;
    private boolean grouped;
    private boolean newOffer;
    @Min(value=0, message="{validation.EligibleOffersFiltersRequest.page.min.msg}")
    private Integer page;
    @Min(value=1, message="{validation.EligibleOffersFiltersRequest.pageLimit.min.msg}")
    private Integer pageLimit;
    private String latitude;
    private String longitude;
    private String offerType;
    private String offerTypePreference;
    private Integer priceOrder;
  
}