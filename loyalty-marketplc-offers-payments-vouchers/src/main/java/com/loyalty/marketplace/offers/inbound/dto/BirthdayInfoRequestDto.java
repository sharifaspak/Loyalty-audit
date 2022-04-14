package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class BirthdayInfoRequestDto {

	@NotBlank(message = "{validation.birthdayInfoDto.titleEn.notBlank.msg}")
	private String titleEn;
	@NotBlank(message = "{validation.birthdayInfoDto.titleAr.notBlank.msg}")
	private String titleAr;
	@NotBlank(message = "{validation.birthdayInfoDto.subTitleEn.notBlank.msg}")
	private String subTitleEn;
	@NotBlank(message = "{validation.birthdayInfoDto.subTitleAr.notBlank.msg}")
	private String subTitleAr;
	@NotBlank(message = "{validation.birthdayInfoDto.descriptionEn.notBlank.msg}")
	private String descriptionEn;
	@NotBlank(message = "{validation.birthdayInfoDto.descriptionAr.notBlank.msg}")
	private String descriptionAr;
	@NotBlank(message = "{validation.birthdayInfoDto.iconTextEn.notBlank.msg}")
	private String iconTextEn;
	@NotBlank(message = "{validation.birthdayInfoDto.iconTextAr.notBlank.msg}")
	private String iconTextAr;
	@NotBlank(message = "{validation.birthdayInfoDto.weekIconEn.notBlank.msg}")
	private String weekIconEn;
	@NotBlank(message = "{validation.birthdayInfoDto.weekIconAr.notBlank.msg}")
	private String weekIconAr;
	@Min(value=1, message="{validation.birthdayInfoDto.purchaseLimit.min.msg}")
	private Integer purchaseLimit;
	@Min(value=1, message="{validation.birthdayInfoDto.thresholdPlusValue.min.msg}")
	private Integer thresholdPlusValue;
	@Min(value=1, message="{validation.birthdayInfoDto.thresholdMinusValue.min.msg}")
	private Integer thresholdMinusValue;
	@Min(value=1, message="{validation.birthdayInfoDto.displayLimit.min.msg}")
	private Integer displayLimit;
	
}