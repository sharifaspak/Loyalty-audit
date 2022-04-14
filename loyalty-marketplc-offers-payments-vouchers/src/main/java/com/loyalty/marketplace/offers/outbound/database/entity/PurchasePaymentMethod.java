package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

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
@Document(collection = OffersDBConstants.PURCHASE_PAYMENT_METHOD)
public class PurchasePaymentMethod {

	@Id
	private String purchaseItemId;

	@Field("ProgramCode")
	private String programCode;
	
	@Field("PurchaseItem")
	private String purchaseItem;
	
	@DBRef
	@Field("PaymentMethods")
	private List<PaymentMethod> paymentMethods;
	
	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;
}
