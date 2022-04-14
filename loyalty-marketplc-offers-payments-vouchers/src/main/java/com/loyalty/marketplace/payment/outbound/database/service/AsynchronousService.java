package com.loyalty.marketplace.payment.outbound.database.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.offers.merchants.outbound.database.entity.MerchantName;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.inbound.dto.PaymentAdditionalRequest;
import com.loyalty.marketplace.payment.outbound.dto.MemberActivityResponse;

@Component
public class AsynchronousService {

	private static final Logger LOG = LoggerFactory.getLogger(AsynchronousService.class);
	
	@Autowired
	PaymentService paymentService;

	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_CASH_VOUCHER_ACCRUAL)
	public MemberActivityResponse accrualForCashVoucherPurchase(PurchaseRequestDto paymentReq, GetMemberResponseDto memberDetails,
			OfferCatalog offerCatalog, PaymentAdditionalRequest paymentAdditionalRequest, Double spentAmount, int spentPoints) {
		LOG.info("inside accrualForCashVoucherPurchase... paymentRequest : {}, offerCatalog : {}, spentAmount : {}, spentPoints : {}", paymentReq, offerCatalog, spentAmount, spentPoints);
		MemberActivityResponse memberActivityResponse = null;
		if ((MarketplaceConstants.CASHVOUCHER.getConstant()).equalsIgnoreCase(paymentReq.getSelectedPaymentItem())
			&& ((offerCatalog.getEarnMultiplier() != null && offerCatalog.getEarnMultiplier() != 0)|| (null!= offerCatalog.getAccrualDetails()
			&& offerCatalog.getAccrualDetails().getPointsEarnMultiplier()!=null && offerCatalog.getAccrualDetails().getPointsEarnMultiplier() !=0 ))) {
			List<String> eligiblePaymentMethodsForAccrual = !ObjectUtils.isEmpty(offerCatalog.getAccrualDetails())
				&& !CollectionUtils.isEmpty(offerCatalog.getAccrualDetails().getAccrualPaymentMethods())
				? offerCatalog.getAccrualDetails().getAccrualPaymentMethods().stream().map(PaymentMethod::getDescription).collect(Collectors.toList())
				: null;		 
			if (!CollectionUtils.isEmpty(eligiblePaymentMethodsForAccrual) && eligiblePaymentMethodsForAccrual.contains(paymentReq.getSelectedOption())) {
				if ((MarketplaceConstants.FULLPOINTS.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()) &&
						offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != null && offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != 0) {
					memberActivityResponse = paymentService.doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCode(), Double.valueOf(spentPoints), null);
				} else if((MarketplaceConstants.PARTIALPOINTSCC.getConstant()).equalsIgnoreCase(paymentReq.getSelectedOption()) &&
						offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != null && offerCatalog.getAccrualDetails().getPointsEarnMultiplier() != 0 &&
						offerCatalog.getEarnMultiplier() != null && offerCatalog.getEarnMultiplier() != 0) {
					paymentService.doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getPointsAccrualActivityCode().getActivityCode(), Double.valueOf(spentPoints), "_1");
					memberActivityResponse = paymentService.doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCode(), spentAmount, "_2");
				} else if (offerCatalog.getEarnMultiplier() != null && offerCatalog.getEarnMultiplier() != 0){
					memberActivityResponse = paymentService.doAccrual(paymentReq, memberDetails, offerCatalog, paymentAdditionalRequest, offerCatalog.getActivityCode().getAccrualActivityCode().getActivityCode(), spentAmount, null);	
				}
			} 
		}

		if ((MarketplaceConstants.PARTIALPOINTSCC.getConstant())
				.equalsIgnoreCase(paymentReq.getSelectedOption())) {
			MerchantName merchantName = new MerchantName();
			if (offerCatalog != null && offerCatalog.getMerchant() != null
					&& offerCatalog.getMerchant().getMerchantName() != null) {
				merchantName = offerCatalog.getMerchant().getMerchantName();
			}
			LOG.info("---Calling sendSms for cashVoucher ----");
			paymentService.cashVoucherSms(paymentReq, memberDetails, memberActivityResponse, merchantName, String.valueOf(paymentReq.getVoucherDenomination()));
			LOG.info("---Calling sendSms end ----");
		}
		return memberActivityResponse;
		
	}
}
