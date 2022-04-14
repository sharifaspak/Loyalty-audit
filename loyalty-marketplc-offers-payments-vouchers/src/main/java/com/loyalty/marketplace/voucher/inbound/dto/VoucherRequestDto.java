package com.loyalty.marketplace.voucher.inbound.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Barcode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VoucherRequestDto {
	
	@NotNull(message = "{validation.voucherRequestDto.membershipCode.notEmpty.msg}")
	private String membershipCode;
	@NotEmpty(message = "{validation.voucherRequestDto.accountNumber.notEmpty.msg}")
	private String accountNumber;
	@NotEmpty(message = "{validation.voucherRequestDto.voucherAction.notEmpty.msg}")
	private String voucherAction;
	@NotEmpty(message = "{validation.voucherRequestDto.offerId.notEmpty.msg}")
	private String offerId;
	private String subOfferId;
	private String denominationId;
	@NotEmpty(message = "{validation.voucherRequestDto.merchantCode.notEmpty.msg}")
	private String merchantCode;
	@NotEmpty(message = "{validation.voucherRequestDto.merchantName.notEmpty.msg}")
	private String merchantName;
	@NotEmpty(message = "{validation.voucherRequestDto.partnerCode.notEmpty.msg}")
	private String partnerCode;
	@NotNull(message = "{validation.voucherRequestDto.voucherAmount.notEmpty.msg}")
	private Double voucherAmount;
	@NotNull(message = "{validation.voucherRequestDto.numberOfVoucher.notEmpty.msg}")
    private Integer numberOfVoucher;
    private Date voucherExpiryDate;
    private Integer voucherExpiryPeriod;
    @NotNull(message = "{validation.voucherRequestDto.pointsValue.notEmpty.msg}")
    private Integer pointsValue;
    @NotNull(message = "{validation.voucherRequestDto.cost.notEmpty.msg}")
    private Double cost;
    @NotEmpty(message = "{validation.voucherRequestDto.UUID.notEmpty.msg}")
    private String uuid;
    @NotEmpty(message = "{validation.voucherRequestDto.offerType.notEmpty.msg}")
    private String offerType;
    @NotNull(message = "{validation.voucherRequestDto.barcodeType.notNull.msg}")
    private Barcode barcodeType;
    private List<String> merchantEmail;   
    private String isBirthdayGift;
    @NotEmpty(message = "{validation.voucherRequestDto.offerTypeId.notEmpty.msg}")
    private String offerTypeId;
    
    private String externalName;
    private Integer voucherDenomination;
    private boolean mambaFoodVoucher;
    private boolean subscPromo;
    private boolean gifted;
    
}
