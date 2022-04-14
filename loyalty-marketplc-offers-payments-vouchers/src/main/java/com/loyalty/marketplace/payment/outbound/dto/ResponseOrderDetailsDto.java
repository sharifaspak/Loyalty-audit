package com.loyalty.marketplace.payment.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseOrderDetailsDto {

	private String orderNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date orderDate;
	private String pointsEngineConfirmationId;
	private String pointsEngineOrderNumber;
	@JsonProperty("orderLines")
	private List<ResponseOrderLinesDto> orderLines;
}
