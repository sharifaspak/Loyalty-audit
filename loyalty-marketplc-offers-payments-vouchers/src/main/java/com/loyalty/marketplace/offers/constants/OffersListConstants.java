package com.loyalty.marketplace.offers.constants;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.loyalty.marketplace.constants.RequestMappingConstants;

/**
 * 
 * @author jaya.shukla
 *
 */
public class OffersListConstants extends RequestMappingConstants{
	
    private OffersListConstants(){
    	
    }
	
	public static final List<String> UPDATE_ACTION_LIST = 
		    Collections.unmodifiableList(Arrays.asList(OfferConstants.UPDATE_ACTION.get(), OfferConstants.ACTIVATE_ACTION.get(), OfferConstants.DEACTIVATE_ACTION.get()));
	public static final List<String> ACTIVATE_DEACTIVATE_ACTION_LIST = 
		    Collections.unmodifiableList(Arrays.asList(OfferConstants.ACTIVATE_ACTION.get(), OfferConstants.DEACTIVATE_ACTION.get()));
	public static final List<String> OFFER_STATUS_LIST = 
			Collections.unmodifiableList(Arrays.asList(OfferConstants.ACTIVE_STATUS.get(), OfferConstants.INACTIVE_STATUS.get(), OfferConstants.ALL.get()));
	public static final List<String> LIMIT_VALUES_LIST = Collections.unmodifiableList(Arrays.asList(OfferConstants.DAILY_LIMIT.get(), 
			OfferConstants.WEEKLY_LIMIT.get(), OfferConstants.MONTHLY_LIMIT.get(),
			OfferConstants.ANNUAL_LIMIT.get(), OfferConstants.TOTAL_LIMIT.get()));
	public static final List<String> COUNTER_TYPES = Collections.unmodifiableList(Arrays.asList(OfferConstants.OFFER_COUNTER.get(),
			OfferConstants.ACCOUNT_OFFER_COUNTER.get()	, OfferConstants.MEMBER_OFFER_COUNTER.get()));
	public static final List<String> BASIC_OFFER_CHECKS = Collections.unmodifiableList(Arrays.asList(OfferConstants.OFFER_STATUS_CHECK.get(),
			OfferConstants.MERCHANT_STATUS_CHECK.get()	, OfferConstants.LINKED_STORE_CHECK.get(), OfferConstants.PORTAL_CHECK.get(),
			OfferConstants.OFFER_EXPIRY_CHECK.get()));
	public static final List<String> ELIGIBLE_FLAG_VALUES = 
			Collections.unmodifiableList(Arrays.asList(OfferConstants.FLAG_SET.get(), OfferConstants.FLAG_NOT_SET.get()));
	public static final List<String> ELIGIBLE_GROUP_FLAG_VALUES = 
			Collections.unmodifiableList(Arrays.asList(OfferConstants.FLAG_SET.get(), OfferConstants.FLAG_NOT_SET.get(), OfferConstants.FLAG_PARENT.get()));
	public static final List<String> PROVISIONING_CHANNEL_LIST = 
		    Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.coms, OffersConfigurationConstants.rtf, OffersConfigurationConstants.emcais, OffersConfigurationConstants.phonyTunes, OffersConfigurationConstants.rbt));
	public static final List<String> ELIGIBLE_OFFER_PURCHASE_ITEMS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnItem, OffersConfigurationConstants.billPaymentItem, OffersConfigurationConstants.rechargeItem, OffersConfigurationConstants.cashVoucherItem, 
					OffersConfigurationConstants.discountVoucherItem, OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> ELIGIBLE_PURCHASE_ITEMS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnItem, OffersConfigurationConstants.billPaymentItem, OffersConfigurationConstants.rechargeItem, OffersConfigurationConstants.cashVoucherItem, 
					OffersConfigurationConstants.discountVoucherItem, OffersConfigurationConstants.dealVoucherItem, OffersConfigurationConstants.subscriptionItem));
	public static final List<String> ELIGIBLE_TELECOM_SPEND_ITEMS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.billPaymentItem, OffersConfigurationConstants.rechargeItem));
	public static final List<String> OFFER_PURCHASE_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnItem, OffersConfigurationConstants.billPaymentItem, OffersConfigurationConstants.rechargeItem, OffersConfigurationConstants.cashVoucherItem,
					OffersConfigurationConstants.discountVoucherItem, OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> OFFER_TYPE_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnItem, OffersConfigurationConstants.goldCertificateItem, OffersConfigurationConstants.cashVoucherItem, 
					OffersConfigurationConstants.discountVoucherItem, OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> OFFER_ID_PURCHASE_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnItem, OffersConfigurationConstants.billPaymentItem, 
					OffersConfigurationConstants.rechargeItem, OffersConfigurationConstants.cashVoucherItem, OffersConfigurationConstants.discountVoucherItem, 
					OffersConfigurationConstants.goldCertificateItem));
	public static final List<String> SUB_OFFER_ID_PURCHASE_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> OFFER_DENOMINATION_PURCHASE_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.cashVoucherItem, 
					OffersConfigurationConstants.goldCertificateItem));
	public static final List<String> OFFER_COUPON_QUANTITY_PURCHASE_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnItem,
					OffersConfigurationConstants.billPaymentItem, OffersConfigurationConstants.rechargeItem,
					OffersConfigurationConstants.cashVoucherItem, OffersConfigurationConstants.discountVoucherItem, 
					OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> ELIGIBLE_PURCHASE_OFFER_ITEMS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnItem,
					OffersConfigurationConstants.cashVoucherItem, OffersConfigurationConstants.discountVoucherItem, 
					OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> VOUCHER_PURCHASE_ITEMS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.cashVoucherItem, 
					OffersConfigurationConstants.discountVoucherItem, OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> LIFETIME_SAVINGS_PURCHASE_ITEMS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.discountVoucherItem, 
					OffersConfigurationConstants.dealVoucherItem));
	public static final List<String> CONFIGURED_RULES_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.cinemaRule));
	public static final List<String> POINTS_TYPES = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.fullPoints, 
					OffersConfigurationConstants.partialCardPoints));
	public static final List<String> AMOUNT_TYPES = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.fullCreditCard, 
					OffersConfigurationConstants.deductFromBalance, OffersConfigurationConstants.addToBill,
					OffersConfigurationConstants.applePay,OffersConfigurationConstants.samsungPay));
	public static final List<String> CARD_TYPES = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.fullCreditCard, 
//					OffersConfigurationConstants.deductFromBalance, OffersConfigurationConstants.addToBill, 
					OffersConfigurationConstants.partialCardPoints,OffersConfigurationConstants.applePay,OffersConfigurationConstants.samsungPay));
	public static final List<String> ELIGIBLE_CUSTOMER_SEGMENTS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.standardCustomerSegment, 
					OffersConfigurationConstants.subscriberCustomerSegment, 
					OffersConfigurationConstants.specialCustomerSegment));
	public static final List<String> CUSTOMER_SEGMENTS = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.customerSegments));
	public static final List<String> VOUCHER_REDEEM_TYPES = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.voucherRedeemTypes));
	public static final List<String> DISCOUNT_OFFER_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.discountOfferType));
	public static final List<String> CASH_OFFER_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.cashOfferType));
	public static final List<String> DEAL_OFFER_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.dealOfferType));
	public static final List<String> ETISALAT_BUNDLE_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.addOnOfferType));
	public static final List<String> GOLD_CERTIFICATE_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.goldCertificateOfferType));
	public static final List<String> TELECOM_OFFER_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.telecomOfferType));
	public static final List<String> LIFESTYLE_OFFER_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.lifestyleOfferType));
	public static final List<String> WELCOME_GIFT_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.welcomeGiftOfferType));
	public static final List<String> OTHER_OFFER_ID_LIST = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.otherOfferType));
	public static final List<String> VOUCHER_ACTION_TYPES = 
			Collections.unmodifiableList(Stream.of(DISCOUNT_OFFER_ID_LIST, CASH_OFFER_ID_LIST, DEAL_OFFER_ID_LIST)
            .flatMap(Collection::stream)
            .collect(Collectors.toList())); 
	public static final List<String> CREATE_OFFER_TYPE_LIST = 
			Collections.unmodifiableList(Stream.of(DISCOUNT_OFFER_ID_LIST, CASH_OFFER_ID_LIST, DEAL_OFFER_ID_LIST, ETISALAT_BUNDLE_ID_LIST, GOLD_CERTIFICATE_ID_LIST)
            .flatMap(Collection::stream)
            .collect(Collectors.toList())); 
	public static final List<String> REQUIRED_DENOMINATIONS_TYPES = 
			Collections.unmodifiableList(Stream.of(CASH_OFFER_ID_LIST, 
					  GOLD_CERTIFICATE_ID_LIST, 
					  Arrays.asList(OffersConfigurationConstants.goldCertificateItem, OffersConfigurationConstants.cashVoucherItem))
            .flatMap(Collection::stream)
            .collect(Collectors.toList()));
	public static final List<String> ANNUAL_COUNTER_RESET = 
			Collections.unmodifiableList(Stream.of(Arrays.asList(OffersConfigurationConstants.ANNUAL,
					OffersConfigurationConstants.MONTHLY,
					OffersConfigurationConstants.WEEKLY,
					OffersConfigurationConstants.DAILY))
		            .flatMap(Collection::stream)
		            .collect(Collectors.toList()));
	public static final List<String> MONTHLY_COUNTER_RESET = 
			Collections.unmodifiableList(Stream.of(Arrays.asList(
					OffersConfigurationConstants.MONTHLY,
					OffersConfigurationConstants.WEEKLY,
					OffersConfigurationConstants.DAILY))
		            .flatMap(Collection::stream)
		            .collect(Collectors.toList()));
	public static final List<String> WEEKLY_COUNTER_RESET = 
			Collections.unmodifiableList(Stream.of(Arrays.asList(OffersConfigurationConstants.WEEKLY,
					OffersConfigurationConstants.DAILY))
		            .flatMap(Collection::stream)
		            .collect(Collectors.toList()));
	public static final List<String> DAILY_COUNTER_RESET = 
			Collections.unmodifiableList(Stream.of(Arrays.asList(
					OffersConfigurationConstants.DAILY))
		            .flatMap(Collection::stream)
		            .collect(Collectors.toList()));
	public static final List<String> REVERSAL_PAYMENT_TYPES = 
			Collections.unmodifiableList(Arrays.asList(OffersConfigurationConstants.fullCreditCard, 
					OffersConfigurationConstants.partialCardPoints, OffersConfigurationConstants.samsungPay,
					OffersConfigurationConstants.applePay));
	
}
