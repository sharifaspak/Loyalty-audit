package com.loyalty.marketplace.payment.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loyalty.marketplace.payment.constants.MarketplaceCode;
import com.loyalty.marketplace.payment.inbound.dto.CreatePaymentRequest;
import com.loyalty.marketplace.payment.inbound.dto.CreatePaymentRequestObj;
import com.loyalty.marketplace.payment.inbound.dto.PaymentGatewayRequest;
import com.loyalty.marketplace.payment.outbound.dto.PaymentGatewayResponseResult;

public class Utils {

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class.getName());

	public static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static Boolean validvalidatePaymentInfo(CreatePaymentRequest createPaymentRequest) {

		CreatePaymentRequestObj request = createPaymentRequest.getCreatePaymentInfo();

		Boolean invalidSelectedPaymentItem = false;
		Boolean invalidSelectedOption = false;
		Boolean invalidPointsValue = false;
		Boolean invalidDirhamValue = false;
		Boolean invalidCardNumber = false;
		Boolean invalidCardType = false;
		Boolean invalidCardSubType = false;
		Boolean invalidCardToken = false;
		Boolean invalidCardExpiryDate = false;
		Boolean invalidmsisdn = false;
		Boolean invalidAuthorizationCode = false;
		Boolean invalidPaymentType = false;
		Boolean invalidOfferId = false;
		Boolean invalidVoucherDenomination = false;
		Boolean invalidAccountNumber = false;
		Boolean invalidatgUsername = false;
		Boolean invalidLevel = false;
		Boolean invalidPreferredNumber = false;
		Boolean invalidextTransactionId = false;
		Boolean invalidPartialTransactionId = false;
		Boolean invalidPromoCode = false;
		Boolean invaliduiLanguage = false;
		Boolean invalidOfferTittle = false;
		Boolean invalidepgTransactionID = false;
		Boolean invalidMembershipCode = false;
		Boolean invalidOrderId = false;
		Boolean invalidAdditionalParams = false;

		if (request.getSelectedPaymentItem() == null || request.getSelectedPaymentItem().isEmpty()) {
			invalidSelectedPaymentItem = true;
		}
		if (request.getSelectedOption() == null || request.getSelectedOption().isEmpty()) {
			invalidSelectedOption = true;
		}
		if (request.getAccountNumber() == null || request.getAccountNumber().isEmpty()) {
			invalidAccountNumber = true;
		}

		if (request.getAtgUsername() == null || request.getAtgUsername().isEmpty()) {
			invalidatgUsername = true;
		}

		if (request.getAuthorizationCode() == null || request.getAuthorizationCode().isEmpty()) {
			invalidAuthorizationCode = true;
		}

		if (request.getCardExpiryDate() == null || request.getCardExpiryDate().isEmpty()) {
			invalidCardExpiryDate = true;
		}

		if (request.getCardNumber() == null || request.getCardNumber().isEmpty()) {
			invalidCardNumber = true;

		}

		if (request.getCardSubType() == null || request.getCardSubType().isEmpty()) {
			invalidCardSubType = true;
		}

		if (request.getCardType() == null || request.getCardType().isEmpty()) {
			invalidCardType = true;
		}

		if (request.getCardToken() == null || request.getCardToken().isEmpty()) {
			invalidCardType = true;
		}

		if (request.getMembershipCode() == null || request.getMembershipCode().isEmpty()) {
			invalidMembershipCode = true;
		}

		if (request.getOrderId() == null || request.getOrderId().isEmpty()) {
			invalidOrderId = true;
		}

		if (request.getOfferId() == null || request.getOfferId().isEmpty()) {
			invalidOfferId = true;
		}

		if (request.getPointsValue() == null || request.getPointsValue().intValue() <= 0) {
			invalidPointsValue = true;
		}

		if (request.getDirhamValue() == null || request.getDirhamValue().doubleValue() <= 0) {
			invalidDirhamValue = true;
		}

		if (request.getPaymentType() == null || request.getPaymentType().isEmpty()) {
			invalidPaymentType = true;
		}

		if (invalidAccountNumber || invalidatgUsername || invalidAuthorizationCode || invalidCardExpiryDate
				|| invalidCardNumber || invalidCardSubType || invalidCardToken || invalidCardType || invalidDirhamValue
				|| invalidPointsValue || invalidOrderId || invalidMembershipCode || invalidOfferId || invalidPaymentType
				|| invalidSelectedOption || invalidSelectedPaymentItem) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean validatePaymentGatewayInfo(PaymentGatewayRequest request,PaymentGatewayResponseResult result) {

		Boolean invalidSelectedPaymentItem = false;

		Boolean invalidDirhamValue = false;

		Boolean invalidPointsValue = false;

		Boolean invalidSelectedOption = false;

		Boolean invalidOfferId = false;

		Boolean invalidVoucherDenomination = false;

		Boolean invalidAccountNumber = false;

		Boolean invalidPromoCode = false;

		Boolean invalidLanguage = false;

		Boolean invalidatgUsername = false;

		Boolean invalidOfferTitle = false;

		Boolean invalidCount = false;

		if (request.getSelectedPaymentItem() == null || request.getSelectedPaymentItem().isEmpty()) {
			result.setResponseCode(Integer.parseInt(MarketplaceCode.INVALID_PARAM_SELECTEDPAYMENTITEM.getId()));
			result.setResponseDescription(MarketplaceCode.INVALID_PARAM_SELECTEDPAYMENTITEM.getMsg());
			invalidSelectedPaymentItem = true;
		}
		if (request.getSelectedOption() == null || request.getSelectedOption().isEmpty()) {
			result.setResponseCode(Integer.parseInt(MarketplaceCode.INVALID_PARAM_SELECTEDOPTION.getId()));
			result.setResponseDescription(MarketplaceCode.INVALID_PARAM_SELECTEDOPTION.getMsg());
			invalidSelectedOption = true;
		}
		if (request.getAccountNumber() == null || request.getAccountNumber().isEmpty()) {
			result.setResponseCode(Integer.parseInt(MarketplaceCode.INVALID_PARAM_ACCOUNTNUMBER.getId()));
			result.setResponseDescription(MarketplaceCode.INVALID_PARAM_ACCOUNTNUMBER.getMsg());
			invalidAccountNumber = true;
		}

		if (request.getAtgUsername() == null || request.getAtgUsername().isEmpty()) {
			result.setResponseCode(Integer.parseInt(MarketplaceCode.INVALID_PARAM_ATGUSERNAME.getId()));
			result.setResponseDescription(MarketplaceCode.INVALID_PARAM_ATGUSERNAME.getMsg());
			invalidatgUsername = true;
		}

		if (request.getOfferId() == null || request.getOfferId().isEmpty()) {
			result.setResponseCode(Integer.parseInt(MarketplaceCode.INVALID_PARAM_OFFERID.getId()));
			result.setResponseDescription(MarketplaceCode.INVALID_PARAM_OFFERID.getMsg());
			invalidOfferId = true;
		}

		if (request.getPointsValue() == null || request.getPointsValue().longValue() < 0) {
			result.setResponseCode(Integer.parseInt(MarketplaceCode.INVALID_PARAM_POINTSVALUE.getId()));
			result.setResponseDescription(MarketplaceCode.INVALID_PARAM_POINTSVALUE.getMsg());
			invalidPointsValue = true;
		}

		if (request.getDirhamValue() == null || request.getDirhamValue().doubleValue() <= 0) {
			result.setResponseCode(Integer.parseInt(MarketplaceCode.INVALID_PARAM_DIRHAMVALUE.getId()));
			result.setResponseDescription(MarketplaceCode.INVALID_PARAM_DIRHAMVALUE.getMsg());
			invalidDirhamValue = true;
		}

		if (invalidSelectedPaymentItem || invalidSelectedOption || invalidAccountNumber || invalidatgUsername
				|| invalidOfferId || invalidPointsValue || invalidDirhamValue) {
			return true;
		} else {
			return false;
		}
	}

	public static double getIfNotNull(String vatPercentage) {
		if (vatPercentage == null) {
			return 0.0d;
		} else {
			return Double.parseDouble(vatPercentage);
		}
	}

	public static Integer getIntIfNotNull(String vatPercentage) {
		if (vatPercentage == null) {
			return 0;
		} else {
			return Integer.parseInt(vatPercentage);
		}
	}

	public static Double pointsToAEDRate(Integer dblPoints) {

		if (dblPoints >= 0 && dblPoints <= 55000) {
			return 0.008;
		} else if (dblPoints > 55000 && dblPoints <= 111110) {
			return 0.0088;
		} else if (dblPoints >= 111111) {
			return 0.009;
		} else {
			return 0.0;
		}

	}
	
	public static String toXML(Object data) {
        String xml = "";
        try {
            LOG.info("Generating xml for: " + data.getClass());
            JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //jaxbMarshaller.marshal(data, System.out);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(data, sw);
            xml = sw.toString();
            LOG.info(xml);
        } catch (JAXBException e) {
            //handle your exception here
        }
        return xml;
    }

	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		LOG.info(builder.toString());
		return builder.toString();
	}

	public static GetPaymentRequest setPaymentRequest(String strCustomerName, String language, Double dbAmountValue,
			String strCustomerCurrency, String getRandomNumber, String orderInfo, String orderName,
			String strReturnPath, String terminal, Long totalAmount, String accountNumber, String selectedPaymentItem,
			String strTransactionHint) {
		GetPaymentRequest getPaymentRequest = new GetPaymentRequest();
		if (strCustomerName != null)
			getPaymentRequest.setCustomerName(strCustomerName);
		if (language != null)
			getPaymentRequest.setLanguage(language);
		if (dbAmountValue != null)
			getPaymentRequest.setAmount(dbAmountValue);
		if (strCustomerCurrency != null)
			getPaymentRequest.setCurrency(strCustomerCurrency);
		if (getRandomNumber != null)
			getPaymentRequest.setOrderId(getRandomNumber);
		if (orderInfo != null)
			getPaymentRequest.setOrderInfo(orderInfo);
		if (orderName != null)
			getPaymentRequest.setOrderName(orderName);
		if (strReturnPath != null)
			getPaymentRequest.setReturnPath(strReturnPath);
		if (strTransactionHint != null)
			getPaymentRequest.setTransactionHint(strTransactionHint);
		if (terminal != null)
			getPaymentRequest.setTerminal(terminal);

		if (totalAmount != null)
			getPaymentRequest.setTotalPayment(String.valueOf(totalAmount));

		if (accountNumber != null)
			getPaymentRequest.setMobileNumber(accountNumber);

		if (selectedPaymentItem != null)
			getPaymentRequest.setTransactionType(selectedPaymentItem);
		return getPaymentRequest;
	}
	
	/**
     * round a decimal. The second argument provides the number of digits after
     * the decimal point.
     *
     * @param d            value to be rounded
     * @param decimalPlace
     * @return
     */
    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
