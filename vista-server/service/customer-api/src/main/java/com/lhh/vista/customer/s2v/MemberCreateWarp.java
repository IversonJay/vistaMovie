package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/21.
 */
@Setter
@Getter
public class MemberCreateWarp {

    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;

    @JsonProperty(value = "UserSessionId")
    private String sessionId;

    @JsonProperty(value = "LoyaltyMember")
    private LoyaltyMember memberInfo;

    @Setter
    @Getter
    public static class LoyaltyMember {
        @JsonProperty(value = "MemberId")
        private String memberId;

        @JsonProperty(value = "UserName")
        private String userName;

        @JsonProperty(value = "MobilePhone")
        private String mphone;

        @JsonProperty(value = "Password")
        private String pass;
        @JsonProperty(value = "FirstName")
        private String firstName;

        @JsonProperty(value = "LastName")
        private String lastName;

        @JsonProperty(value = "ClubID")
        private Integer clubID = 1;
        
        @JsonProperty(value = "CardNumber")
        private String cardNumber;
        
        @JsonProperty(value = "PreferredComplex")
        private Integer PreferredComplex;

    }
}
