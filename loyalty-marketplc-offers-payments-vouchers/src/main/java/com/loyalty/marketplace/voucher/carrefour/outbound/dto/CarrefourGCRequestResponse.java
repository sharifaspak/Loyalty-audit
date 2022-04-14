
package com.loyalty.marketplace.voucher.carrefour.outbound.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AckMessage;
import com.loyalty.marketplace.voucher.carrefour.inbound.dto.AdditionalInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarrefourGCRequestResponse {

    protected CarrefourGCRequestResponse.ResponseData ResponseData;
    protected AckMessage AckMessage;
    
    @Getter
    @Setter
    @ToString
    public static class ResponseData {

        protected String TransactionID;
        protected String IsSuccessful;
        protected String ReferenceNumber;
        protected BigDecimal PreviousBalance;
        protected String Currency;
        protected BigDecimal Amount;
        protected BigDecimal Balance;
        protected String Note;
        @XmlElement(name = "ExpireDate", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected String ExpireDate;
        @XmlElement(name = "MobileNo", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected String MobileNo;
        @XmlElement(name = "CardNumber", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected String CardNumber;
        @XmlElement(name = "PinCode", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected String PinCode;
        @XmlElement(name = "CardType", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected String CardType;
        @XmlElement(name = "PreviousPoints", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected BigDecimal PreviousPoints;
        @XmlElement(name = "AddedPoints", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected BigDecimal AddedPoints;
        @XmlElement(name = "PointBalance", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected BigDecimal PointBalance;
        @XmlElement(name = "BonusAmount", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected BigDecimal BonusAmount;
        @XmlElement(name = "RedeemedPoints", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected BigDecimal RedeemedPoints;
        @XmlElement(name = "InitialBalance", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected BigDecimal InitialBalance;
        @XmlElement(name = "EarliestExpiryDate", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected String EarliestExpiryDate;
        @XmlElement(name = "EarliestExpiryAmount", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected BigDecimal EarliestExpiryAmount;
        @XmlElement(name = "BarCodeNumber", namespace = "http://www.etisalat.ae/Middleware/CustomerLoyaltyMgmt/CarrefourGCRequestResponse.xsd")
        protected String BarCodeNumber;
        @XmlElement(name = "AdditionalInfo", namespace = "http://www.etisalat.ae/Middleware/SharedResources/Common/Common.xsd")
        protected List<AdditionalInfo> AdditionalInfo;

    }  
}
