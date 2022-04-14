package com.loyalty.marketplace.gifting.service.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseOrderDetailsDto {
	private String orderNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date orderDate;
	@JsonIgnore
	private String pointsEngineConfirmationId;
	@JsonIgnore
	private String pointsEngineOrderNumber;
	@JsonProperty("orderLines")
	private List<ResponseOrderLinesDto> orderLines;

}
