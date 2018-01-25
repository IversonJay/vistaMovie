package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/4.
 */
@Setter
@Getter
public class GetMemberItemWarp {
    @JsonProperty(value = "UserSessionId")
    private String sessionId;

    @JsonProperty(value = "GetConcessions")
    private boolean getConcessions = true;
    @JsonProperty(value = "GetDiscounts")
    private boolean getDiscounts = true;
    @JsonProperty(value = "GetTicketTypes")
    private boolean getTicketTypes = true;
    @JsonProperty(value = "SupressSelectedSessionDateTimeFilter")
    private boolean supressSelectedSessionDateTimeFilter = true;
    @JsonProperty(value = "GetAdvanceSeatingRecognitions")
    private boolean getAdvanceSeatingRecognitions = true;
    @JsonProperty(value = "GetAdvanceBookings")
    private boolean getAdvanceBookings = true;
    @JsonProperty(value = "maxresults")
    private int maxresults = 5;


    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;
}
