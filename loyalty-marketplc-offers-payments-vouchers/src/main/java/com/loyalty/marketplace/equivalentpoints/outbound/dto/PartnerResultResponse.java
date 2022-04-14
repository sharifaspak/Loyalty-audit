package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PartnerResultResponse {
	
	private ApiStatus apiStatus;
	private Object result;
	private List<PartnerDto> partners;

}
