package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/27.
 */
@Setter
@Getter
public class CompleteOrderWarp {

    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;

    @JsonProperty(value = "UserSessionId")
    private String orderId;
    @JsonProperty(value = "PaymentInfo")
    private PaymentInfo paymentInfo;

    @JsonProperty(value = "PerformPayment")
    private Boolean performPayment;
    
    @JsonProperty(value = "CustomerPhone")
    private String customerPhone;

    @Setter
    @Getter
    public static class PaymentInfo {

        @JsonProperty(value = "CardNumber")
        private String cardNumber = "";

        @JsonProperty(value = "CardType")
        private String cardType = "";

        @JsonProperty(value = "BankTransactionNumber")
        private String bankTransactionNumber;

        @JsonProperty(value = "PaymentValueCents")
        private Integer paymentValueCents;

        @JsonProperty(value = "PaymentTenderCategory")
        private String paymentTenderCategory = "";

        @JsonProperty(value = "CardExpiryMonth")
        private String cardExpiryMonth = "00";

        @JsonProperty(value = "CardExpiryYear")
        private String cardExpiryYear = "0000";
    }

    public static final String CARD_TYPE_POYAL = "LOYAL";
    public static final String CARD_TYPE_ALIPAY = "ALIPAY";
    public static final String CARD_TYPE_QPAY = "QPAY";
    public static final String CARD_TYPE_WECHATPAY = "WECHATPAY";

    public static final String PT_OTHER = "CREDIT";
    public static final String PT_CARD = "LOYAL";
    public static final String CARDNUMBER = "9999999999999999";
}
