package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(Include.NON_NULL)
public class DenominationDto {

	private String denominationId;
	@NotEmpty(message="{validation.DenominationDto.descriptionEn.notEmpty.msg}")
	private String descriptionEn;
	private String descriptionAr;
	@NotNull(message="{validation.DenominationDto.pointValue.notNull.msg}")
	private Integer pointValue;
	@NotNull(message="{validation.DenominationDto.cost.notNull.msg}")
	private Integer dirhamValue;
}
