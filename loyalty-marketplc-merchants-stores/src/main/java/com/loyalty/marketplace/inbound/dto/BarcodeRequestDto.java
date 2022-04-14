package com.loyalty.marketplace.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BarcodeRequestDto {
	
	@Valid
	@NotNull
	private List<BarcodeDto> barcodes;

	public List<BarcodeDto> getBarcodes() {
		return  barcodes;
	}

	public void setBarcodes(List<BarcodeDto> barcodes) {
		this.barcodes = barcodes;
	}

	@Override
	public String toString() {
		return "BarcodeRequestDto [barcodes=" + barcodes + "]";
	}
	
	

}
