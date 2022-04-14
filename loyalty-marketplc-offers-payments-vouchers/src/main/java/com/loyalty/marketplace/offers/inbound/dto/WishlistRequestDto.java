package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class WishlistRequestDto {
	
	@NotNull(message="{validation.wishlistRequestDto.offer.notNull.msg}")
	@NotEmpty(message="{validation.wishlistRequestDto.offer.notEmpty.msg}")
	private String offerId;
	@NotNull(message="{validation.wishlistRequestDto.action.notNull.msg}")
	@NotEmpty(message="{validation.wishlistRequestDto.action.notEmpty.msg}")
	private String action;
}
