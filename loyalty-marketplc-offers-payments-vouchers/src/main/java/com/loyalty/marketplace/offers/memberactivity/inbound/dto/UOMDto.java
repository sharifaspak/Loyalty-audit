package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UOMDto {

	@NotEmpty(message = "3067 UOM Code is null")
	private String code;
	@NotEmpty(message = "3067 UOM Name is null")
	private String name;
	private String description;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	@NotNull(message = "3068 UOM Start Date is null")
	private Date startDate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	@NotNull(message = "3069 UOM End Date is null")
	private Date endDate;
	
}
