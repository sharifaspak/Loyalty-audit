
package com.loyalty.marketplace.voucher.maf.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.maf.inbound.dto.AdditionalInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class VoucherReconciliationResponse {

    private VoucherReconciliationResponse.ResponseData ResponseData;
    private AckMessage AckMessage;

    @Getter
    @Setter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
	public static class ResponseData {

		private String TransactionID;
		private VoucherReconciliationResponse.ResponseData.VoucherDetails VoucherDetails;
		private List<AdditionalInfo> AdditionalInfo;

		@Getter
		@Setter
		@JsonInclude(Include.NON_EMPTY)
		public static class VoucherDetails {
			private VoucherReconciliationResponse.ResponseData.VoucherDetails.Data Data;

		
			@Getter
			@Setter
			@ToString
			@JsonInclude(Include.NON_EMPTY)
			public static class Data {

				private VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Filters Filters;
				private List<VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders> Orders;

				@Getter
				@Setter
				@ToString
				@JsonInclude(Include.NON_EMPTY)
				public static class Filters {

					private String TotalOrders;
					private String Page;
					private String Limit;
				}
				@Getter
				@Setter
				@ToString
				@JsonInclude(Include.NON_EMPTY)
				public static class Orders {

					private String OrderID;
					private String OrderReferenceNumber;
					private String NumberOfItems;
					private String CustomerID;
					private String RecipientID;
					private String IPAddress;
					private String VoucherID;
					private String PartnerID;
					private String CountryID;
					private String StatusID;
					private String PaymentMethodID;
					private String Source;
					private String TotalActivationFee;
					private String TotalDeliveryFee;
					private String TotalFeeAmount;
					private String VAT;
					private String TotalVatableFee;
					private String TotalLoadAmount;
					private String TotalAmount;
					private String TotalPaid;
					private String ExpiredAfter;
					private String CreatedAt;
					private List<AdditionalInfo> AdditionalInfo;
					
				}

			}

		}

	}

}
