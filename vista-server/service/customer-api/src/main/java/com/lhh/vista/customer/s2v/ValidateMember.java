package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by soap on 2016/12/13.
 */
@Setter
@Getter
public class ValidateMember {
    @JsonProperty(value = "UserSessionId")
    private String userSessionId;
    @JsonProperty(value = "OptionalClientClass")
    private String optionalClientClass;
    @JsonProperty(value = "OptionalClientId")
    private String optionalClientId;
    @JsonProperty(value = "OptionalClientName")
    private String optionalClientName;
    @JsonProperty(value = "MemberCardNumber")
    private String memberCardNumber;
    @JsonProperty(value = "MemberLogin")
    private String memberLogin;
    @JsonProperty(value = "MemberPassword")
    private String memberPassword;
    @JsonProperty(value = "ReturnMember")
    private Boolean returnMember=true;
}
