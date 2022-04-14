package com.loyalty.marketplace.gifting.inbound.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.loyalty.marketplace.offers.memberactivity.outbound.database.entity.ActivityDescription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PremiumVoucherRequest {

	@NotBlank(message = "{validation.premiumVoucherRequest.accountNumber.notBlank.msg}")
	private String accountNumber;
	@NotNull(message = "{validation.premiumVoucherRequest.pointsValue.notNull.msg}")
	private Integer pointsValue;
	@NotNull(message = "{validation.premiumVoucherRequest.activityDescription.notNull.msg}")
	private ActivityDescription activityDescription;
	
}
