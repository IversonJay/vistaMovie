package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/21.
 */
@Setter
@Getter
public class MemberValidateWarp {

    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;

    @JsonProperty(value = "UserSessionId")
    private String sessionId;

    @JsonProperty(value = "ReturnMember")
    private Boolean returnMember;

    @JsonProperty(value = "MemberLogin")
    private String userName;

    @JsonProperty(value = "MemberPassword")
    private String pass;

    @JsonProperty(value = "MemberId")
    private String memberId;

    @JsonProperty(value = "MemberCardNumber")
    private String cardNumber;
    
    @JsonProperty(value = "ClubId")
    private String clubId;
    
    @JsonProperty(value = "PreferredComplex")
    private String  preferredComplex;
}
