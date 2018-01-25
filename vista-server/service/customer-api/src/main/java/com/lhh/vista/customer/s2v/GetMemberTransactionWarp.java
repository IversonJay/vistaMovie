package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/4.
 */
@Setter
@Getter
public class GetMemberTransactionWarp {
    @JsonProperty(value = "UserSessionId")
    private String sessionId;
    @JsonProperty(value = "maxresults")
    private int maxresults = 5;


    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;

    @JsonProperty(value = "ReturnMemberTransactionDetails")
    private Boolean returnMemberTransactionDetails = true;
}
