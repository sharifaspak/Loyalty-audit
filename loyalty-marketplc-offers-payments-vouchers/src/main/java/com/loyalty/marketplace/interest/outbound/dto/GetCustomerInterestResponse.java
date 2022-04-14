package com.loyalty.marketplace.interest.outbound.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerInterestResponse {
   private List<CategoriesInterestDto> interestList;
   private long totalRecords;
}
